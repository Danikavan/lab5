package commands;

import managers.CollectionManager;
import managers.CommandManager;

/**
 * Команда "remove_all_by_personal_qualities_maximum".
 * Удаляет все элементы, у которых значение поля personalQualitiesMaximum равно заданному.
 */
public class RemoveByPersQualCommand implements Command {
    CollectionManager collectionManager;
    CommandManager commandManager;

    public RemoveByPersQualCommand(CollectionManager collectionManager, CommandManager commandManager) {
        this.collectionManager = collectionManager;
        this.commandManager = commandManager;
    }

    public String getDescription() {
        return "удалить из коллекции все элементы, значение поля personalQualitiesMaximum которого эквивалентно заданному";
    }

    public String getName() {
        return "remove_all_by_personal_qualities_maximum personalQualitiesMaximum";
    }

    @Override
    public void execute(String arg) {
        if (arg == null) {
            System.out.println("Не указано значение. Использование: remove_all_by_personal_qualities_maximum value");
            return;
        }
        try {
            long value = Long.parseLong(arg);
            collectionManager.removeAllByPersonalQualitiesMaximum(value);
            System.out.println("Удалены элементы с personalQualitiesMaximum = " + value);
        } catch (NumberFormatException e) {
            System.out.println("Значение должно быть целым числом.");
        }
    }
}
