package managers;

import models.*;

import java.io.*;
import java.time.ZonedDateTime;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Vector;

/**
 * Отвечает за загрузку коллекции из CSV-файла и сохранение в файл.
 * Использует BufferedInputStream для чтения и FileWriter для записи.
 */
public class FileManager {
    private final String filename;

    /**
     * Создаёт менеджер для работы с указанным файлом.
     * @param filename путь к CSV-файлу (может быть null)
     */
    public FileManager(String filename) {
        this.filename = filename;
    }

    /**
     * Загружает коллекцию из CSV-файла.
     * Некорректные строки игнорируются, дубликаты id пропускаются.
     * @return вектор с загруженными объектами LabWork
     */
    public Vector<LabWork> load() {
        Vector<LabWork> collection = new Vector<>();
        if (filename == null) {
            System.out.println("Переменная окружения LABWORKS_FILE не задана. Коллекция пуста");
            return collection;
        }

        try (BufferedInputStream bis = new BufferedInputStream(new FileInputStream(filename));
             BufferedReader reader = new BufferedReader(new InputStreamReader(bis))) {

            String line;
            int lineNum = 0;
            while ((line = reader.readLine()) != null) {
                lineNum++;
                line = line.trim();
                if (line.isEmpty() || line.startsWith("#")) continue;

                LabWork labWork = parseLabWork(line);
                if (labWork == null) {
                    System.out.println("Строка " + lineNum + " пропущена: не удалось распарсить");
                    continue;
                }
                if (!labWork.isValid()) {
                    System.out.println("Строка " + lineNum + " пропущена: объект не прошел валидацию");
                    continue;
                }
                if (!isUniqueId(collection, labWork.getId())) {
                    System.out.println("Строка " + lineNum + " пропущена: id " + labWork.getId() + " уже существует");
                    continue;
                }
                collection.add(labWork);
            }
            System.out.println("Загружено " + collection.size() + " элементов из файла");

        } catch (FileNotFoundException e) {
            System.out.println("Файл не найден: " + filename + ". Будет создан новый при сохранении");
        } catch (IOException e) {
            System.out.println("Ошибка чтения файла: " + e.getMessage());
        }
        return collection;
    }

    /**
     * Проверяет, уникален ли переданный id в коллекции.
     * @param collection коллекция объектов LabWork
     * @param id проверяемый идентификатор
     * @return true, если элемент с таким id ещё не встречается
     */
    private boolean isUniqueId(Collection<LabWork> collection, Integer id) {
        return collection.stream().noneMatch(lw -> lw.getId().equals(id));
    }

    /**
     * Сохраняет коллекцию в файл в формате CSV.
     * @param collection коллекция для сохранения
     */
    public void save(Vector<LabWork> collection) {
        if (filename == null) {
            System.out.println("Переменная окружения LABWORKS_FILE не задана. Сохранение невозможно");
            return;
        }

        try (FileWriter writer = new FileWriter(filename)) {
            for (LabWork lw : collection) {
                writer.write(toCsvString(lw) + "\n");
            }
            System.out.println("Коллекция сохранена в файл " + filename);
        } catch (IOException e) {
            System.out.println("Ошибка записи файла: " + e.getMessage());
        }
    }

    /**
     * Преобразует объект LabWork в строку CSV.
     * @param lw объект
     * @return строка в формате CSV
     */
    private String toCsvString(LabWork lw) {
        String[] fields = {
                lw.getId().toString(),
                escapeCsv(lw.getName()),
                escapeCsv(lw.getCoordinates().getX().toString()),
                escapeCsv(lw.getCoordinates().getY().toString()),
                escapeCsv(lw.getCreationDate().toString()),
                lw.getMinimalPoint() == null ? "" : escapeCsv(lw.getMinimalPoint().toString()),
                String.valueOf(lw.getPersonalQualitiesMaximum()),
                escapeCsv(lw.getDescription()),
                lw.getDifficulty() == null ? "" : lw.getDifficulty().name(),
                escapeCsv(lw.getAuthor().getName()),
                String.valueOf(lw.getAuthor().getWeight()),
                lw.getAuthor().getEyeColor() == null ? "" : lw.getAuthor().getEyeColor().name(),
                lw.getAuthor().getHairColor() == null ? "" : lw.getAuthor().getHairColor().name(),
                lw.getAuthor().getNationality() == null ? "" : lw.getAuthor().getNationality().name(),
                String.valueOf(lw.getAuthor().getLocation().getX()),
                String.valueOf(lw.getAuthor().getLocation().getY()),
                String.valueOf(lw.getAuthor().getLocation().getZ()),
                escapeCsv(lw.getAuthor().getLocation().getName())
        };
        return String.join(",", fields);
    }

    /**
     * Экранирует поле для CSV: если содержит запятую или кавычки, заключает в двойные кавычки,
     * а внутренние кавычки удваивает.
     * @param value исходная строка
     * @return экранированная строка
     */
    private String escapeCsv(String value) {
        if (value == null) return "";
        if (value.contains(",") || value.contains("\"") || value.contains("\n")) {
            value = value.replace("\"", "\"\"");
            return "\"" + value + "\"";
        }
        return value;
    }

    /**
     * Разбирает строку CSV в объект LabWork.
     * @param csv строка CSV
     * @return объект LabWork или null при ошибке парсинга
     */
    private LabWork parseLabWork(String csv) {
        List<String> fields = parseCsvLine(csv);
// Также для других строковых полей, если они есть (например, для location.name)
        if (fields.size() != 18) return null;
        try {
            Integer id = Integer.parseInt(fields.get(0));
            String name = fields.get(1);
            if (name.startsWith("\"") && name.endsWith("\"")) {
                name = name.substring(1, name.length() - 1);
            }
            Float x = Float.parseFloat(fields.get(2));
            Double y = Double.parseDouble(fields.get(3));
            ZonedDateTime creationDate = ZonedDateTime.parse(fields.get(4));
            Float minimalPoint = fields.get(5).isEmpty() ? null : Float.parseFloat(fields.get(5));
            long personalMax = Long.parseLong(fields.get(6));
            String description = fields.get(7);
            if (description.startsWith("\"") && description.endsWith("\"")) {
                description = description.substring(1, description.length() - 1);
            }
            Difficulty difficulty = fields.get(8).isEmpty() ? null : Difficulty.valueOf(fields.get(8));
            String authorName = fields.get(9);
            if (authorName.startsWith("\"") && authorName.endsWith("\"")) {
                authorName = authorName.substring(1, authorName.length() - 1);
            }
            long weight = Long.parseLong(fields.get(10));
            EyeColor eyeColor = fields.get(11).isEmpty() ? null : EyeColor.valueOf(fields.get(11));
            HairColor hairColor = fields.get(12).isEmpty() ? null : HairColor.valueOf(fields.get(12));
            Country nationality = fields.get(13).isEmpty() ? null : Country.valueOf(fields.get(13));
            int locX = Integer.parseInt(fields.get(14));
            int locY = Integer.parseInt(fields.get(15));
            float locZ = Float.parseFloat(fields.get(16));
            String locName = fields.get(17).isEmpty() ? null : fields.get(17);
            if (locName.startsWith("\"") && locName.endsWith("\"")) {
                locName = locName.substring(1, locName.length() - 1);
            }

            Coordinates coordinates = new Coordinates(x, y);
            Location location = new Location(locX, locY, locZ, locName);
            Person author = new Person(authorName, weight, eyeColor, hairColor, nationality, location);
            LabWork lw = new LabWork(id, name, coordinates, creationDate, minimalPoint, personalMax, description, difficulty, author);
            return lw;

        } catch (Exception e) {
            // Любая ошибка парсинга (NumberFormatException, IllegalArgumentException и т.д.) → возвращаем null
            return null;
        }
    }

    /**
     * Разбивает CSV строку на поля с учётом кавычек.
     * @param line строка
     * @return список полей
     */
    private List<String> parseCsvLine(String line) {
        List<String> result = new ArrayList<>();
        boolean inQuotes = false;
        StringBuilder field = new StringBuilder();
        for (int i = 0; i < line.length(); i++) {
            char c = line.charAt(i);
            if (c == '"') {
                if (inQuotes && i + 1 < line.length() && line.charAt(i + 1) == '"') {
                    field.append('"'); // экранированная кавычка
                    i++;
                } else {
                    inQuotes = !inQuotes; // переключаем состояние, кавычку не добавляем
                }
            } else if (c == ',' && !inQuotes) {
                result.add(field.toString());
                field.setLength(0);
            } else {
                field.append(c);
            }
        }
        result.add(field.toString());
        return result;
    }
}