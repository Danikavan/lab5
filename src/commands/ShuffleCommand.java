package commands;

import managers.CollectionManager;
import managers.CommandManager;

/**
 * Команда "shuffle".
 * Перемешивает элементы коллекции в случайном порядке.
 */
public class ShuffleCommand implements Command {
    CollectionManager collectionManager;
    CommandManager commandManager;

    public ShuffleCommand(CollectionManager collectionManager, CommandManager commandManager) {
        this.collectionManager = collectionManager;
        this.commandManager = commandManager;
    }

    public String getDescription() {
        return "перемешать элементы коллекции в случайном порядке";
    }

    public String getName() {
        return "shuffle";
    }

    @Override
    public void execute(String arg) {
        collectionManager.shuffle();
        System.out.println("Коллекция перемешана.");
    }
}