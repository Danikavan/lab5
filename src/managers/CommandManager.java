package managers;

import exceptions.*;
import inputs.*;
import models.*;
import commands.*;

import java.util.*;

/**
 * Менеджер команд. Обрабатывает пользовательский ввод, выполняет команды, управляет вводом данных для составных типов.
 */
public class CommandManager {
    private final CollectionManager collectionManager;
    private InputProvider inputProvider;

    /**
     * Множество выполняемых скриптов.
     * Используется для предотвращения рекурсии (execute_script).
     */
    private Set<String> executingScripts = new HashSet<>();

    /**
     * Карта, хранящая все доступные команды.
     * Ключ — имя команды (например, "help", "add"), значение — объект команды,
     * реализующий интерфейс {@link Command}.
     */
    private final Map<String, Command> commands = new HashMap<>();

    /**
     * Создаёт менеджер команд.
     * @param cm менеджер коллекции
     * @param ip источник ввода (консоль или скрипт)
     */
    public CommandManager(CollectionManager cm, InputProvider ip) {
        this.collectionManager = cm;
        this.inputProvider = ip;
        initCommands();
    }

    /**
     * Регистрирует все доступные команды.
     */
    private void initCommands() {
        register(new HelpCommand(collectionManager, this));
        register(new InfoCommand(collectionManager, this));
        register(new ShowCommand(collectionManager, this));
        register(new AddCommand(collectionManager, this));
        register(new UpdateIdCommand(collectionManager, this));
        register(new RemoveByIdCommand(collectionManager, this));
        register(new ClearCommand(collectionManager, this));
        register(new SaveCommand(collectionManager, this));
        register(new ExecuteScriptCommand(collectionManager, this));
        register(new ExitCommand(collectionManager, this));
        register(new ShuffleCommand(collectionManager, this));
        register(new RemoveGreaterCommand(collectionManager, this));
        register(new ReorderCommand(collectionManager, this));
        register(new RemoveByPersQualCommand(collectionManager, this));
        register(new MaxByNameCommand(collectionManager, this));
        register(new CountGreaterThanDescCommand(collectionManager, this));
    }

    private void register(Command command) {
        commands.put(command.getName(), command);
    }

    /**
     * Запускает главный цикл обработки команд.
     */
    public void start() {
        mainLoop(inputProvider);
    }

    /**
     * Запускает цикл обработки команд с заданным источником ввода.
     * Используется для выполнения скриптов: временно подменяет стандартный ввод на чтение из файла.
     * @param provider источник ввода (например, {@link ScriptInputProvider})
     */
    public void startLoop(InputProvider provider) {
        mainLoop(provider);
    }

    /**
     * Основной цикл обработки команд.
     * Бесконечно читает строки из указанного источника, разбивает их на имя команды и аргумент, находит соответствующую команду в карте и выполняет её.
     * При возникновении {@link ScriptExecutionException} цикл прерывается.
     * @param provider источник ввода (консоль или файл скрипта)
     */
    private void mainLoop(InputProvider provider) {
        while (true) {
            if (provider instanceof ConsoleInputProvider) {
                System.out.print("> ");
            }
            String line = provider.readLine();
            if (line == null) break;
            line = line.trim();
            if (line.isEmpty()) continue;
            try {
                String[] parts = line.split(" ", 2);
                String cmd = parts[0];
                String arg = parts.length > 1 ? parts[1] : null;

                Command command = commands.get(cmd);
                if (command != null) {
                    command.execute(arg);
                } else {
                    System.out.println("Неизвестная команда. Введите 'help' для справки.");
                }
            } catch (ScriptExecutionException e) {
                System.out.println("Ошибка выполнения скрипта: " + e.getMessage());
                return;
            }
        }
    }

    // методы ввода
    /**
     * Ввод объекта LabWork. Если existing != null, предлагает его поля как значения по умолчанию.
     * @param existing существующий объект (для update) или null
     * @return новый объект LabWork (без id и creationDate)
     */
    public LabWork inputLabWork(LabWork existing) {
        String name = inputString("Название работы", false, false, existing == null ? null : existing.getName());
        Coordinates coordinates = inputCoordinates(existing == null ? null : existing.getCoordinates());

        Float minimalPoint = inputFloat("Минимальный балл (может быть null, >0 если задан)", existing == null ? null : existing.getMinimalPoint());
        long personalMax = inputLong("Максимум личных качеств (>0)", existing == null ? null : existing.getPersonalQualitiesMaximum());
        String description = inputString("Описание", false, false, existing == null ? null : existing.getDescription());
        Difficulty difficulty = inputEnum("Сложность (может быть null)", Difficulty.class, existing == null ? null : existing.getDifficulty());
        Person author = inputPerson(existing == null ? null : existing.getAuthor());

        return new LabWork(name, coordinates, minimalPoint, personalMax, description, difficulty, author);
    }

    /**
     * Запрашивает у пользователя координаты (x и y).
     * Если передан существующий объект Coordinates, его значения предлагаются как значения по умолчанию. Поля x и y обязательны и не могут быть null.
     * @param existing существующий объект Coordinates (может быть null)
     * @return новый объект Coordinates с введёнными значениями
     * @throws ScriptExecutionException если в режиме скрипта ввод не соответствует требованиям
     */
    private Coordinates inputCoordinates(Coordinates existing) {
        System.out.println("Ввод координат:");
        Float x = inputFloat("Координата x (не null)", existing == null ? null : existing.getX());
        Double y = inputDouble("Координата y (не null)", existing == null ? null : existing.getY());
        return new Coordinates(x, y);
    }

    /**
     * Запрашивает у пользователя данные об авторе: имя, вес, цвет глаз, цвет волос, национальность и местоположение.
     * Если передан существующий объект Person, его значения используются как значения по умолчанию. Имя и вес обязательны, цвета и национальность могут быть null.
     * Для ввода местоположения вызывается {@link #inputLocation(Location)}.
     * @param existing существующий объект Person (может быть null)
     * @return новый объект Person с введёнными значениями
     * @throws ScriptExecutionException если в режиме скрипта ввод не соответствует требованиям
     */
    private Person inputPerson(Person existing) {
        System.out.println("Ввод автора:");
        String name = inputString("Имя автора", false, false, existing == null ? null : existing.getName());
        long weight = inputLong("Вес (>0)", existing == null ? null : existing.getWeight());
        EyeColor eyeColor = inputEnum("Цвет глаз (может быть null)", EyeColor.class, existing == null ? null : existing.getEyeColor());
        HairColor hairColor = inputEnum("Цвет волос (может быть null)", HairColor.class, existing == null ? null : existing.getHairColor());
        Country nationality = inputEnum("Национальность (может быть null)", Country.class, existing == null ? null : existing.getNationality());
        Location location = inputLocation(existing == null ? null : existing.getLocation());
        return new Person(name, weight, eyeColor, hairColor, nationality, location);
    }

    /**
     * Запрашивает у пользователя местоположение: координаты x, y, z и название.
     * Если передан существующий объект Location, его значения используются как значения по умолчанию. Все координаты обязательны, название может быть null.
     * @param existing существующий объект Location (может быть null)
     * @return новый объект Location с введёнными значениями
     * @throws ScriptExecutionException если в режиме скрипта ввод не соответствует требованиям
     */
    private Location inputLocation(Location existing) {
        System.out.println("Ввод местоположения:");
        int x = inputInt("Координата x", existing == null ? null : existing.getX());
        int y = inputInt("Координата y", existing == null ? null : existing.getY());
        float z = inputFloat("Координата z", existing == null ? null : existing.getZ());
        String name = inputString("Название (может быть null)", true, true, existing == null ? null : existing.getName());
        return new Location(x, y, z, name);
    }

    // ввод примитивов
    /**
     * Запрашивает у пользователя строковое значение.
     * Поддерживает значения по умолчанию, возможность ввода null и пустых строк.
     * При работе в режиме скрипта некорректный ввод вызывает {@link ScriptExecutionException}.
     * @param prompt приглашение к вводу
     * @param nullable разрешён ли ввод пустой строки
     * @param allowEmpty разрешена ли непустая строка, состоящая только из пробелов
     * @param defaultValue значение по умолчанию
     * @return введённая строка, null или значение по умолчанию
     * @throws ScriptExecutionException если в режиме скрипта ввод не соответствует требованиям
     */
    private String inputString(String prompt, boolean nullable, boolean allowEmpty, String defaultValue) {
        while (true) {
            if (inputProvider instanceof ConsoleInputProvider) {
                System.out.print(prompt + (defaultValue != null ? " (текущее: " + defaultValue + "): " : ": "));
            }
            String line = inputProvider.readLine();
            if (line == null) {
                if (inputProvider instanceof ConsoleInputProvider) {
                    return defaultValue != null ? defaultValue : (nullable ? null : "");
                } else {
                    handleInputError("Неожиданный конец файла");
                    // Исключение будет выброшено, выполнение не продолжается
                }
            }
            line = line.trim();
            if (line.isEmpty()) {
                if (defaultValue != null) return defaultValue;
                if (nullable) return null;
                handleInputError("Поле не может быть пустым");
                continue; // для консоли повторим ввод, для скрипта исключение прервёт
            }
            return line;
        }
    }

    /**
     * Запрашивает у пользователя целое число типа long.
     * Значение должно быть строго больше 0.
     * В режиме скрипта при ошибке выбрасывает {@link ScriptExecutionException}.
     * @param prompt приглашение к вводу
     * @param defaultValue значение по умолчанию (может быть null)
     * @return введённое число (гарантированно больше 0)
     * @throws ScriptExecutionException если в режиме скрипта введено некорректное значение
     */
    private Long inputLong(String prompt, Long defaultValue) {
        while (true) {
            String str = inputString(prompt, false, false, defaultValue == null ? null : defaultValue.toString());
            if (str == null) return null;
            try {
                long val = Long.parseLong(str);
                if (val <= 0) {
                    handleInputError("Значение должно быть > 0");
                    continue;
                }
                return val;
            } catch (NumberFormatException e) {
                handleInputError("Введите целое число");
            }
        }
    }

    /**
     * Запрашивает у пользователя целое число типа int.
     * В режиме скрипта при ошибке выбрасывает {@link ScriptExecutionException}.
     * @param prompt приглашение к вводу
     * @param defaultValue значение по умолчанию (может быть null)
     * @return введённое число
     * @throws ScriptExecutionException если в режиме скрипта введено некорректное значение
     */
    private Integer inputInt(String prompt, Integer defaultValue) {
        while (true) {
            String str = inputString(prompt, false, false, defaultValue == null ? null : defaultValue.toString());
            if (str == null) return null;
            try {
                return Integer.parseInt(str);
            } catch (NumberFormatException e) {
                handleInputError("Введите целое число");
            }
        }
    }

    /**
     * Запрашивает у пользователя число с плавающей точкой типа float.
     * В режиме скрипта при ошибке выбрасывает {@link ScriptExecutionException}.
     * @param prompt приглашение к вводу
     * @param defaultValue значение по умолчанию (может быть null)
     * @return введённое число
     * @throws ScriptExecutionException если в режиме скрипта введено некорректное значение
     */
    private Float inputFloat(String prompt, Float defaultValue) {
        while (true) {
            String str = inputString(prompt, true, false, defaultValue == null ? null : defaultValue.toString());
            if (str == null) return null;
            try {
                float val = Float.parseFloat(str);
                if (val <= 0) {
                    handleInputError("Значение должно быть > 0");
                    continue;
                }
                return val;
            } catch (NumberFormatException e) {
                handleInputError("Введите число");
            }
        }
    }

    /**
     * Запрашивает у пользователя число с плавающей точкой типа double.
     * В режиме скрипта при ошибке выбрасывает {@link ScriptExecutionException}.
     * @param prompt приглашение к вводу
     * @param defaultValue значение по умолчанию (может быть null)
     * @return введённое число
     * @throws ScriptExecutionException если в режиме скрипта введено некорректное значение
     */
    private Double inputDouble(String prompt, Double defaultValue) {
        while (true) {
            String str = inputString(prompt, false, false, defaultValue == null ? null : defaultValue.toString());
            if (str == null) return null;
            try {
                double val = Double.parseDouble(str);
                if (val <= 0) { // если требуется положительное
                    handleInputError("Значение должно быть > 0");
                    continue;
                }
                return val;
            } catch (NumberFormatException e) {
                handleInputError("Введите число");
            }
        }
    }

    /**
     * Запрашивает у пользователя значение перечисления (enum).
     * Допустимые значения выводятся на экран. Поддерживается null (пустая строка).
     * В режиме скрипта при ошибке выбрасывает {@link ScriptExecutionException}.
     * @param prompt приглашение к вводу
     * @param enumClass класс перечисления (например, Difficulty.class)
     * @param defaultValue значение по умолчанию (может быть null)
     * @param <T> тип перечисления
     * @return выбранная константа enum, null или значение по умолчанию
     * @throws ScriptExecutionException если в режиме скрипта ввод не соответствует допустимым значениям
     */
    private <T extends Enum<T>> T inputEnum(String prompt, Class<T> enumClass, T defaultValue) {
        T[] constants = enumClass.getEnumConstants();
        if (inputProvider instanceof ConsoleInputProvider) {
            System.out.println("Допустимые значения: " + Arrays.toString(constants));
        }
        while (true) {
            String str = inputString(prompt, true, false, defaultValue == null ? null : defaultValue.name());
            if (str == null) return null;
            try {
                return Enum.valueOf(enumClass, str.trim().toUpperCase());
            } catch (IllegalArgumentException e) {
                handleInputError("Недопустимое значение. Введите одно из: " + Arrays.toString(constants));
            }
        }
    }

    /**
     * Обрабатывает ошибку ввода.
     * В консольном режиме выводит сообщение об ошибке и позволяет повторить ввод.
     * В режиме скрипта выбрасывает {@link ScriptExecutionException} с переданным сообщением, что приводит к прерыванию выполнения скрипта.
     * @param message текст сообщения об ошибке
     * @throws ScriptExecutionException если текущий источник ввода - скрипт
     */
    private void handleInputError(String message) {
        if (inputProvider instanceof ConsoleInputProvider) {
            System.out.println(message);
        } else {
            throw new ScriptExecutionException(message);
        }
    }

    // методы для работы со скриптами

    /**
     * Проверяет, выполняется ли в данный момент указанный скрипт.
     *
     * @param fileName имя файла скрипта
     * @return {@code true}, если скрипт уже выполняется, иначе {@code false}
     */
    public boolean isExecuting(String fileName) {
        return executingScripts.contains(fileName);
    }

    /**
     * Помечает скрипт как выполняемый.
     *
     * @param fileName имя файла скрипта
     */
    public void startScript(String fileName) {
        executingScripts.add(fileName);
    }

    /**
     * Удаляет скрипт из списка выполняемых.
     *
     * @param fileName имя файла скрипта
     */
    public void endScript(String fileName) {
        executingScripts.remove(fileName);
    }

    // геттеры

    public InputProvider getInputProvider() {
        return inputProvider;
    }

    public void setInputProvider(InputProvider provider) {
        this.inputProvider = provider;
    }

    public Map<String, Command> getCommands() {
        return commands;
    }
}