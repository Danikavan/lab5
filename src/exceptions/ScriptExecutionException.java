package exceptions;

/**
 * Исключение, выбрасываемое при ошибках выполнения скрипта.
 * Например, неверный формат данных, неожиданный конец файла и т.п.
 */
public class ScriptExecutionException extends RuntimeException {
    public ScriptExecutionException(String message) {
        super(message);
    }
}
