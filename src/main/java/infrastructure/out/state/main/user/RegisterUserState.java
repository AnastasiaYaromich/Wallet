package infrastructure.out.state.main.user;

import domain.models.User;
import infrastructure.out.factories.console.ConsoleManager;
import infrastructure.out.factories.console.ConsoleManagerSingleton;
import infrastructure.in.UserServiceSingleton;
import infrastructure.out.state.ConsoleState;
import infrastructure.out.state.main.MainState;
import services.services.UserService;

import static infrastructure.out.util.InputUtils.isEmpty;

/**
 * The RegisterUserState uses to show a registration console state for user.
 */
public class RegisterUserState implements ConsoleState {

    /**
     * The consoleManager variable uses for waiting user input.
     */
    private final ConsoleManager consoleManager;
    /**
     * The userService variable uses to register user in Wallet-Service.
     */
    private final UserService userService;
    /**
     * The nextState variable represents a console state.
     */
    private ConsoleState nextState;

    /**
     * Constructor which initialize a registration console state for user.
     */
    public RegisterUserState() {
        this.consoleManager = ConsoleManagerSingleton.getInstance();
        this.userService = UserServiceSingleton.getUserService();
        this.nextState = new MainState();
    }

    /**
     * The process() method shows a registration menu for user of Wallet-Service application.
     * Method suggests user to enter a login and password and waits the user input.
     * Then save new user in Wallet Service application.
     * @throws Exception input errors.
     */
    public void process() throws Exception {
        String login = "";
        String password = "";

        do {
            System.out.print(String.format("\n" + "Enter login: "));
            login = consoleManager.readLine();
        } while (isEmpty("login", login));

        do {
            System.out.print(String.format("Enter password: "));
            password = consoleManager.readLine();
        } while (isEmpty("password", password));

        try {
            userService.addUser(new User(login, password));
            System.out.println(String.format("User " + login +  " successfully registered!%n"));
        } catch (Exception e) {
            System.out.println(e.getMessage());
        } finally {
            consoleManager.clear();
        }
    }

    /**
     * @return method nextState() returns the console state
     */
    public ConsoleState nextState() {
        return nextState;
    }
}