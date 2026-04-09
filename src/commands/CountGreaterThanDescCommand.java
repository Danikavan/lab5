package commands;

import managers.CollectionManager;
import managers.CommandManager;

/**
 * Команда "count_greater_than_description".
 * Выводит количество элементов, значение поля description которых больше заданной строки.
 */
public class CountGreaterThanDescCommand implements Command {
    CollectionManager collectionManager;
    CommandManager commandManager;

    public CountGreaterThanDescCommand(CollectionManager collectionManager, CommandManager commandManager) {
        this.collectionManager = collectionManager;
        this.commandManager = commandManager;
    }

    public String getDescription() {
        return "вывести количество элементов, значение поля description которых больше заданного";
    }

    public String getName() {
        return "count_greater_than_description description";
    }

    @Override
    public void execute(String arg) {
        if (arg == null) {
            System.out.println("Не указано описание. Использование: count_greater_than_description description");
            return;
        }
        long count = collectionManager.countGreaterThanDescription(arg);
        System.out.println("Количество элементов с описанием больше \"" + arg + "\": " + count);
    }
}
