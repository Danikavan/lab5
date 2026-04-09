package inputs;

import java.util.Scanner;

/**
 * Реализация InputProvider для чтения из консоли.
 */
public class ConsoleInputProvider implements InputProvider {
    private final Scanner scanner = new Scanner(System.in);

    @Override
    public String readLine() {
        return scanner.nextLine();
    }
}
