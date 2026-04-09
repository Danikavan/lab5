import inputs.*;
import managers.*;

/**
 * Главный класс приложения.
 * Читает переменную окружения LABWORKS_FILE, создаёт менеджеры и запускает интерактивный режим.
 */
public class Main {
    public static void main(String[] args) {
        String filename = System.getenv("LABWORKS_FILE");
        FileManager fileManager = new FileManager(filename);
        CollectionManager collectionManager = new CollectionManager(fileManager);
        InputProvider consoleInput = new ConsoleInputProvider();
        CommandManager commandManager = new CommandManager(collectionManager, consoleInput);
        commandManager.start();
    }
}