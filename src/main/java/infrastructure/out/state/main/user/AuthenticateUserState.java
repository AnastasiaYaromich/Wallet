package infrastructure.out.state.main.user;

import domain.models.Audit;
import domain.models.User;
import infrastructure.in.AuditServiceSingleton;
import infrastructure.out.factories.console.ConsoleManager;
import infrastructure.out.factories.console.ConsoleManagerSingleton;
import infrastructure.in.UserServiceSingleton;
import infrastructure.out.state.ConsoleState;
import infrastructure.out.state.main.MainState;
import services.exceptions.MainException;
import services.exceptions.user.UserNotFoundException;
import services.services.AuditService;
import services.services.UserService;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

import static infrastructure.out.util.InputUtils.isEmpty;

/**
 * The AuthenticateUserState uses to show an authentication console state for user.
 */
public class AuthenticateUserState implements ConsoleState {

    /**
     * The consoleManager variable uses for waiting user input.
     */
    private final ConsoleManager consoleManager;
    /**
     * The userService variable uses to check user credentials and authenticate.
     */
    private final UserService userService;

    /**
     * The auditService variable uses to record an audit.
     */
    private final AuditService auditService;

    /**
     * The nextState variable represents a console state.
     */
    private ConsoleState nextState;

    /**
     * Constructor which initialize an authentication console state for user.
     */
    public AuthenticateUserState() {
        this.consoleManager = ConsoleManagerSingleton.getInstance();
        this.userService = UserServiceSingleton.getUserService();
        this.auditService = AuditServiceSingleton.getAuditService();
        this.nextState = new MainState();
    }

    /**
     * The process() method shows an authenticate menu for user of Wallet-Service application.
     * Method suggests user to enter a credentials and waits the user input.
     * Then checks if user with these credentials exists.
     * If user exists method save an authorization audit record and initialize a new console state.
     * If user does not exist method shows the message about invalid credentials.
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
            if (isUserExist(login, password)) {
                System.out.print(String.format("\n" + String.format("Hello " + login + ". You are successfully authenticated.")));
                saveAuditRecord(login);
                nextState = new RouteUserState(userService.findUserByLogin(login));
            }
        } catch (UserNotFoundException e) {
            System.out.println(e.getMessage());
        }
    }

    /**
     * The isUserExist() method checks if user with the passed parameters exists.
     * @param login user login.
     * @param password user password.
     * @return true if user with these credentials exists.
     * @throws MainException throws when user with these credentials does not exist.
     */
    private boolean isUserExist(String login, String password) throws MainException {
        User user = userService.findUserByLogin(login);
        if(password.equals(user.getPassword())) {
            return true;
        } else throw new UserNotFoundException("User with this credentials does not exist.");
    }

    /**
     * The saveAuditRecord() method create and save audit record about user authorization.
     * @param login user login.
     */
    private void saveAuditRecord(String login) {
        Audit audit = new Audit();
        audit.setType("authenticate");
        audit.setStatus("success");
        audit.setDateTime(authorizationDateTime());
        auditService.saveAudit(login, audit);
    }

    /**
     * The authorizationDateTime() return date and time when user authorized in a string format.
     * @return date and time when user authorized in a string format.
     */
    private String authorizationDateTime() {
        DateFormat dateFormat = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");
        Date date = new Date();
        return dateFormat.format(date);
    }

    /**
     * @return method nextState() returns the next console state
     * depending on user input.
     */
    public ConsoleState nextState() {
        return nextState;
    }
}