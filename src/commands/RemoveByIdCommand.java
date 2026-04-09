package commands;

import managers.CollectionManager;
import managers.CommandManager;

/**
 * Команда "remove_by_id".
 * Удаляет элемент из коллекции по заданному идентификатору.
 * Если элемент с таким id не найден, выводится сообщение об ошибке.
 */
public class RemoveByIdCommand implements Command {
    CollectionManager collectionManager;
    CommandManager commandManager;

    public RemoveByIdCommand(CollectionManager collectionManager, CommandManager commandManager) {
        this.collectionManager = collectionManager;
        this.commandManager = commandManager;
    }

    public String getDescription() {
        return "удалить элемент из коллекции по его id";
    }

    public String getName() {
        return "remove_by_id id";
    }

    @Override
    public void execute(String arg) {
        if (arg == null) {
            System.out.println("Не указан id. Использование: remove_by_id id");
            return;
        }
        try {
            Integer id = Integer.parseInt(arg);
            if (collectionManager.removeById(id)) {
                System.out.println("Элемент с id " + id + " удалён.");
            } else {
                System.out.println("Элемент с id " + id + " не найден.");
            }
        } catch (NumberFormatException e) {
            System.out.println("id должен быть целым числом.");
        }
    }
}
