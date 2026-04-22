package commands;

import inputs.*;
import managers.*;
import java.io.IOException;

/**
 * Команда "execute_script".
 * Считывает и исполняет скрипт из указанного файла. В скрипте содержатся команды в том же виде, в котором их вводит пользователь в интерактивном режиме.
 * Реализована защита от рекурсивных вызовов (повторный вызов того же файла во время выполнения).
 * @see CommandManager#isExecuting(String)
 * @see CommandManager#startScript(String)
 * @see CommandManager#endScript(String)
 */
public class ExecuteScriptCommand implements Command {
    CollectionManager collectionManager;
    CommandManager commandManager;

    public ExecuteScriptCommand(CollectionManager collectionManager, CommandManager commandManager) {
        this.collectionManager = collectionManager;
        this.commandManager = commandManager;
    }

    public String getDescription() {
        return "считать и исполнить скрипт из указанного файла. Использование: execute_script file_name";
    }

    public String getName() {
        return "execute_script";
    }

    @Override
    public void execute(String arg) {
        if (arg == null) {
            System.out.println("Введите имя файла");
            return;
        }

        if (commandManager.isExecuting(arg)) {
            System.out.println("Ошибка: обнаружена рекурсия");
            return;
        }

        commandManager.startScript(arg);
        InputProvider oldProvider = commandManager.getInputProvider();

        try (ScriptInputProvider scriptInput = new ScriptInputProvider(arg)) {
            commandManager.setInputProvider(scriptInput);
            System.out.println("Выполнение скрипта " + arg + "...");
            commandManager.startLoop(scriptInput);
        } catch (IOException e) {
            System.out.println("Файл скрипта не найден: " + arg);
        } finally {
            commandManager.setInputProvider(oldProvider);
            commandManager.endScript(arg);
        }
    }
}
