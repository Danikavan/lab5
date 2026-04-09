package commands;

import managers.CollectionManager;
import managers.CommandManager;

/**
 * Команда "exit".
 * Завершает работу программы без автоматического сохранения.
 */
public class ExitCommand implements Command {
    CollectionManager collectionManager;
    CommandManager commandManager;

    public ExitCommand(CollectionManager collectionManager, CommandManager commandManager) {
        this.collectionManager = collectionManager;
        this.commandManager = commandManager;
    }

    public String getDescription() {
        return "завершить программу";
    }

    public String getName() {
        return "exit";
    }

    @Override
    public void execute(String arg) {
        System.out.println("Завершение программы.");
        System.exit(0);
    }
}
