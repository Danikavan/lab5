package commands;

import managers.CollectionManager;
import managers.CommandManager;

/**
 * Команда "clear".
 * Очищает коллекцию (удаляет все элементы).
 */
public class ClearCommand implements Command {
    CollectionManager collectionManager;
    CommandManager commandManager;

    public ClearCommand(CollectionManager collectionManager, CommandManager commandManager) {
        this.collectionManager = collectionManager;
        this.commandManager = commandManager;
    }

    public String getDescription() {
        return "очистить коллекцию";
    }

    public String getName() {
        return "clear";
    }

    @Override
    public void execute(String arg) {
        collectionManager.clear();
        System.out.println("Коллекция очищена.");
    }
}
