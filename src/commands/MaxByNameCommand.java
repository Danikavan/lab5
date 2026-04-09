package commands;

import managers.CollectionManager;
import managers.CommandManager;
import models.LabWork;

/**
 * Команда "max_by_name".
 * Выводит элемент коллекции с максимальным значением поля name.
 */
public class MaxByNameCommand implements Command {
    CollectionManager collectionManager;
    CommandManager commandManager;

    public MaxByNameCommand(CollectionManager collectionManager, CommandManager commandManager) {
        this.collectionManager = collectionManager;
        this.commandManager = commandManager;
    }

    public String getDescription() {
        return "вывести любой объект из коллекции, значение поля name которого является максимальным";
    }

    public String getName() {
        return "max_by_name";
    }

    @Override
    public void execute(String arg) {
        LabWork max = collectionManager.maxByName();
        if (max == null) {
            System.out.println("Коллекция пуста.");
        } else {
            System.out.println("Элемент с максимальным именем: " + max);
        }
    }
}
