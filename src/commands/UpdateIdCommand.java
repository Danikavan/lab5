package commands;

import exceptions.ScriptExecutionException;
import managers.CollectionManager;
import managers.CommandManager;
import models.LabWork;

/**
 * Команда "update".
 * Обновляет значение элемента коллекции, id которого равен заданному.
 * Пользователь последовательно вводит новые значения всех полей (кроме id и creationDate, которые сохраняются из существующего элемента). Если элемент не найден, выводится ошибка.
 */
public class UpdateIdCommand implements Command {
    CollectionManager collectionManager;
    CommandManager commandManager;

    public UpdateIdCommand(CollectionManager collectionManager, CommandManager commandManager) {
        this.collectionManager = collectionManager;
        this.commandManager = commandManager;
    }

    public String getDescription() {
        return "обновить значение элемента коллекции, id которого равен заданному";
    }

    public String getName() {
        return "update id {element}";
    }

    @Override
    public void execute(String arg) {
        if (arg == null) {
            System.out.println("Не указан id. Использование: update id");
            return;
        }
        try {
            Integer id = Integer.parseInt(arg);
            LabWork existing = collectionManager.findById(id);
            if (existing == null) {
                System.out.println("Элемент с id " + id + " не найден.");
                return;
            }
            LabWork updated = commandManager.inputLabWork(existing);
            updated.setId(existing.getId());
            updated.setCreationDate(existing.getCreationDate());
            collectionManager.update(id, updated);
            System.out.println("Элемент с id " + id + " обновлён.");
        } catch (NumberFormatException e) {
            System.out.println("id должен быть целым числом.");
        } catch (ScriptExecutionException e) {
            throw e;
        } catch (Exception e) {
            System.out.println("Ошибка при обновлении: " + e.getMessage());
        }
    }
}
