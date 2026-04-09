package commands;

import exceptions.ScriptExecutionException;
import managers.CollectionManager;
import managers.CommandManager;
import models.LabWork;

/**
 * Команда "remove_greater".
 * Удаляет из коллекции все элементы, превышающие заданный (сравнение по умолчанию).
 * Пользователь вводит этот объект.
 */
public class RemoveGreaterCommand implements Command {
    CollectionManager collectionManager;
    CommandManager commandManager;

    public RemoveGreaterCommand(CollectionManager collectionManager, CommandManager commandManager) {
        this.collectionManager = collectionManager;
        this.commandManager = commandManager;
    }

    public String getDescription() {
        return "удалить из коллекции все элементы, превышающие заданный";
    }

    public String getName() {
        return "remove_greater {element}";
    }

    @Override
    public void execute(String arg) {
        try {
            LabWork sample = commandManager.inputLabWork(null);
            collectionManager.removeGreater(sample);
            System.out.println("Удалены элементы, превышающие заданный.");
        } catch (ScriptExecutionException e) {
            throw e;
        } catch (Exception e) {
            System.out.println("Ошибка: " + e.getMessage());
        }
    }
}
