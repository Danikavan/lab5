package commands;

import exceptions.ScriptExecutionException;
import managers.CollectionManager;
import managers.CommandManager;
import models.LabWork;

/**
 * Команда "add".
 * Добавляет новый элемент в коллекцию. Пользователь последовательно вводит все поля (название, координаты, минимальный балл и т.д.).
 * Id и дата создания генерируются автоматически.
 */
public class AddCommand implements Command {
    CollectionManager collectionManager;
    CommandManager commandManager;

    public AddCommand(CollectionManager collectionManager, CommandManager commandManager) {
        this.collectionManager = collectionManager;
        this.commandManager = commandManager;
    }

    public String getDescription() {
        return "добавить новый элемент в коллекцию";
    }

    public String getName() {
        return "add {element}";
    }

    @Override
    public void execute(String arg) {
        try {
            LabWork labWork = commandManager.inputLabWork(null);
            collectionManager.add(labWork);
            System.out.println("Элемент добавлен с id = " + labWork.getId());
        } catch (ScriptExecutionException e) {
            throw e;
        } catch (Exception e) {
            System.out.println("Ошибка при добавлении: " + e.getMessage());
        }
    }
}