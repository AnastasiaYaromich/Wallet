package infrastructure.out.factories.console;

public class ConsoleManagerSingleton {
    private static ConsoleManager consoleManager = null;

    /**
     * @return ConsoleManager instance.
     */
    public static ConsoleManager getInstance() {
        if(consoleManager == null) {
            consoleManager = new ConsoleManager();
        }
        return consoleManager;
    }
}
