package commands;

import managers.CollectionManager;
import managers.CommandManager;

/**
 * Команда "save".
 * Сохраняет текущее состояние коллекции в CSV-файл, указанный в переменной окружения.
 */
public class SaveCommand implements Command {
    CollectionManager collectionManager;
    CommandManager commandManager;

    public SaveCommand(CollectionManager collectionManager, CommandManager commandManager) {
        this.collectionManager = collectionManager;
        this.commandManager = commandManager;
    }

    public String getDescription() {
        return "сохранить коллекцию в файл";
    }

    public String getName() {
        return "save";
    }

    @Override
    public void execute(String arg) {
        collectionManager.save();
    }
}
