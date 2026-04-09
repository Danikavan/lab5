package commands;

import managers.CollectionManager;
import managers.CommandManager;

/**
 * Команда "info".
 * Выводит информацию о коллекции: тип, дата инициализации, количество элементов.
 */
public class InfoCommand implements Command {
    CollectionManager collectionManager;
    CommandManager commandManager;

    public InfoCommand(CollectionManager collectionManager, CommandManager commandManager) {
        this.collectionManager = collectionManager;
        this.commandManager = commandManager;
    }

    public String getDescription() {
        return "информация о коллекции";
    }

    public String getName() {
        return "info";
    }

    @Override
    public void execute(String arg) {
        System.out.println("Тип коллекции: " + collectionManager.getCollection().getClass().getName());
        System.out.println("Дата инициализации: " + collectionManager.getInitDate());
        System.out.println("Количество элементов: " + collectionManager.getCollection().size());
    }
}
