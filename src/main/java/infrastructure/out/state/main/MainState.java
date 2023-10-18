package infrastructure.out.state.main;

import infrastructure.out.factories.console.ConsoleManager;
import infrastructure.out.factories.console.ConsoleManagerSingleton;
import infrastructure.in.UserServiceSingleton;
import infrastructure.out.state.ConsoleState;
import infrastructure.out.state.main.user.AuthenticateUserState;
import infrastructure.out.state.main.user.RegisterUserState;
import services.services.UserService;

/**
 * The MainState class uses to show a main console state for user.
 */
public class MainState implements ConsoleState {

    /**
     * The consoleManager variable uses for waiting user input.
     */
    private final ConsoleManager consoleManager;
    /**
     * The nextState variable represents a current console state.
     */
    private ConsoleState nextState;
    /**
     * The userService variable uses for administrator appointment.
     */
    private UserService userService;

    /**
     * Constructor which initialize a main console state for user.
     */
    public MainState() {
        this.consoleManager = ConsoleManagerSingleton.getInstance();
        this.userService = UserServiceSingleton.getUserService();
    }

    /**
     * The process() method shows a main menu for user of Wallet-Service application.
     * Method suggests user to enter an option and waits the user input.
     * Then initialize a new console state depending on user input.
     * If user enter nothing or enter an option which does not exist, he will see
     * a message about invalid input and suggestion to enter a correct option.
     * @throws Exception input errors.
     */
    public void process() throws Exception {


        System.out.println("\n" + String.format("Welcome to the Wallet-Service!" +
                " Please enter what you would like to do?" +
                " %n 'register' - create a wallet,%n" +
                " 'authenticate' - login to existing wallet."
        ));

        String input = consoleManager.readLine();

        if(input == null) {
            System.out.println("Invalid input");

        } else switch(input) {
            case "register":
                nextState = new RegisterUserState();
                break;
            case "authenticate":
                nextState = new AuthenticateUserState();
                break;
            default:
                System.out.println("Invalid input. Use one from above.");
                break;
        }
    }


    /**
     *
     * @return method nextState() returns the next console state
     * depending on user input.
     */
    public ConsoleState nextState() {
        return nextState;
    }
}
