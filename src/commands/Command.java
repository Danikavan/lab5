package commands;

public interface Command {

    /**
     * Возвращает описание команды.
     * @return описание команды (используется в help)
     */
    String getDescription();

    /**
     * Возвращает имя команды.
     * @return имя команды (используется для вызова)
     */
    String getName();

    /**
     * Выполняет команду.
     * @param arg аргумент команды (может быть {@code null})
     */
    void execute(String arg);
}
