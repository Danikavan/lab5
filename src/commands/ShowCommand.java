package commands;

import managers.CollectionManager;
import managers.CommandManager;

/**
 * Команда "show".
 * Выводит в стандартный поток вывода все элементы коллекции в строковом представлении.
 */
public class ShowCommand implements Command {
    CollectionManager collectionManager;
    CommandManager commandManager;

    public ShowCommand(CollectionManager collectionManager, CommandManager commandManager) {
        this.collectionManager = collectionManager;
        this.commandManager = commandManager;
    }

    public String getDescription() {
        return "показать все элементы";
    }

    public String getName() {
        return "show";
    }

    @Override
    public void execute(String arg) {
        if (collectionManager.getCollection().isEmpty()) {
            System.out.println("Коллекция пуста.");
        } else {
            collectionManager.getCollection().forEach(System.out::println);
        }
    }
}
