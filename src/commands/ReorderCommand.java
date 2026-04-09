package commands;

import managers.CollectionManager;
import managers.CommandManager;

/**
 * Команда "reorder".
 * Сортирует коллекцию в порядке, обратном естественному (по убыванию).
 */
public class ReorderCommand implements Command {
    CollectionManager collectionManager;
    CommandManager commandManager;

    public ReorderCommand(CollectionManager collectionManager, CommandManager commandManager) {
        this.collectionManager = collectionManager;
        this.commandManager = commandManager;
    }

    public String getDescription() {
        return "отсортировать коллекцию в порядке, обратном нынешнему";
    }

    public String getName() {
        return "reorder";
    }

    @Override
    public void execute(String arg) {
        collectionManager.reorder();
        System.out.println("Коллекция отсортирована в обратном порядке.");
    }
}
