package infrastructure.out.factories.console;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

/**
 * The ConsoleManager class uses to read user input.
 */
public class ConsoleManager {
    private BufferedReader bufferedReader;

    public String readLine() throws IOException {
        if(System.console() != null) {
            return System.console().readLine();
        }
        if(bufferedReader == null) {
            bufferedReader = new BufferedReader(new InputStreamReader(System.in));
        }
        return bufferedReader.readLine();
    }

    public void clear() {
        System.out.print("\033[H\033[2J");
        System.out.flush();
    }

}