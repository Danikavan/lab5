package inputs;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;

/**
 * Реализация InputProvider для чтения из файла скрипта.
 * При достижении конца файла возвращает null.
 */
public class ScriptInputProvider implements InputProvider, AutoCloseable {
    private final BufferedReader reader;

    /**
     * Создаёт провайдер для чтения из файла.
     * @param filename имя файла скрипта
     * @throws FileNotFoundException если файл не найден
     */
    public ScriptInputProvider(String filename) throws FileNotFoundException {
        this.reader = new BufferedReader(new FileReader(filename));
    }

    @Override public String readLine() {
        try {
            return reader.readLine();
        } catch (IOException e) {
            return null;
        }
    }

    @Override
    public void close() throws IOException {
        reader.close();
    }
}