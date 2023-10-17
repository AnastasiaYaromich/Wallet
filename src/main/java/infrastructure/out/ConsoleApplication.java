package infrastructure.out;

/**
 * @author Anastasia Yaromich
 * @version 1.0
 * The main class ConsoleApplication.
 */
public class ConsoleApplication {

    /**
     * The method using to start an application.
     * @param args command-line arguments.
     */
    public static void main(String[] args) {
        LiquibaseConnection.connect();
        new ApplicationLogic().process();
    }
}
