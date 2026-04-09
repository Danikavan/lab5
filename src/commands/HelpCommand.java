package commands;

import managers.CollectionManager;
import managers.CommandManager;

/**
 * Команда "help".
 * Выводит список всех доступных команд с их описаниями.
 * Информация собирается динамически из всех зарегистрированных в CommandManager команд.
 */
public class HelpCommand implements Command {
    CollectionManager collectionManager;
    CommandManager commandManager;

    public HelpCommand(CollectionManager collectionManager, CommandManager commandManager) {
        this.collectionManager = collectionManager;
        this.commandManager = commandManager;
    }

    public String getDescription() {
        return "вывести список команд";
    }

    public String getName() {
        return "help";
    }

    @Override
    public void execute(String arg) {
        System.out.println("Имеющиеся команды:");

        for (Command command : commandManager.getCommands().values()) {
            System.out.println(command.getName() + " : " + command.getDescription());
        }

        System.out.println();
    }
}
