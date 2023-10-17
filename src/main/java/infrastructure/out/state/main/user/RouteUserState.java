package infrastructure.out.state.main.user;

import domain.models.Audit;
import domain.models.User;
import infrastructure.out.factories.audit.AuditServiceSingleton;
import infrastructure.out.factories.console.ConsoleManager;
import infrastructure.out.factories.console.ConsoleManagerSingleton;
import infrastructure.out.factories.user.UserServiceSingleton;
import infrastructure.out.state.ConsoleState;
import infrastructure.out.state.main.MainState;
import infrastructure.out.state.main.wallet.TransactionState;
import services.exceptions.MainException;
import services.exceptions.user.UserNotFoundException;
import services.services.AuditService;
import services.services.UserService;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * The RouteUserState class uses to show a route console state for user after authorization.
 */
public class RouteUserState implements ConsoleState {

    /**
     * The consoleManager variable uses for waiting user input.
     */
    private final ConsoleManager consoleManager;
    /**
     * The userService variable uses to manage users.
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
     * The user variable uses to define a correct options that depends on user role.
     */
    private final User user;

    /**
     * Constructor which initialize a route console state for user.
     */
    public RouteUserState(User user) {
        this.consoleManager = ConsoleManagerSingleton.getInstance();
        this.userService = UserServiceSingleton.getUserService();
        this.nextState = new MainState();
        this.auditService = AuditServiceSingleton.getAuditService();
        this.user = user;
    }

    /**
     * The process() method shows a route menu for user of Wallet-Service application.
     * Method suggests user to enter what he would like to do and waits the user input.
     * If user is admin he can see audit records for another users(must choose specific user
     * from list) and can log out.
     * If user is not admin, he can go to transactions menu or log out.
     * If user log out, and he isn't an admin, method save a log-out audit record and
     * return to main state.
     * @throws Exception input errors.
     */
    public void process() throws Exception {
        if(user.getRole().equalsIgnoreCase("admin")) {
            System.out.println(String.format("\n" + "Please enter what you would like to do?" +
                    " %n 'audit' - receive an audit,%n" +
                    " 'log out' - log out from Wallet-Service."));
        } else {
            System.out.println(String.format("\n" + "Please enter what you would like to do?" +
                    " %n 'transaction' - go to transactions menu,%n" +
                    " 'log out' - log out from Wallet-Service."));
        }
        String input = consoleManager.readLine();

        if(input.equalsIgnoreCase("transaction")) {
            nextState = new TransactionState(user);

        } else if (input.equalsIgnoreCase("audit")) {
            List<User> users = usersList();

            if(!users.isEmpty()) {
                System.out.println(String.format("\n" + "Please enter for what user you would like to see audit?"));
                for (User current : users) {
                    System.out.println(current.getLogin());
                }
                input = consoleManager.readLine();

                try {
                    userService.findUserByLogin(input);
                } catch (UserNotFoundException e) {
                    System.out.println("Invalid input.");
                    nextState = new RouteUserState(user);
                }
            }
            System.out.println(String.format("\n" + "Audit for " + input + ": "));
            auditForUser(input);
            nextState = new RouteUserState(user);
        }

        else if(input.equalsIgnoreCase("log out")) {
            System.out.println(String.format("You are successfully log out."));

            if(!user.getRole().equalsIgnoreCase("admin")) {
                saveAuditRecord(user.getLogin());
                nextState = new MainState();
            }
            nextState = new MainState();
        } else {
            System.out.println("Invalid option input. Use one from above");
            nextState = new RouteUserState(user);
        }
    }


    /**
     * The usersList() method returns list of users which don't have an admin role.
     * @return list of users which don't have an admin role.
     */
    private List<User> usersList() throws MainException {
        List<User> users = new ArrayList<>();
        for (User user : userService.users()) {
            if (!user.getRole().equalsIgnoreCase("admin")) {
                users.add(user);
            }
        }
        return users;
    }

    /**
     * The auditForUser() method print all audit records for chosen user.
     * @param login user login.
     */
    private void auditForUser(String login) throws MainException {
        List<Audit> userAudit = auditService.userActionAudit(login);
        if(userAudit != null) {
            for (Audit a : userAudit) {
                System.out.println(a.toString());
            }
        }
    }

    /**
     * The saveAuditRecord() method create and save audit record about user authorization.
     * @param login user login.
     */
    private void saveAuditRecord(String login) {
        Audit audit = new Audit();
        audit.setType("log out");
        audit.setDateTime(logOutDateTime());
        audit.setStatus("success");
        auditService.saveAudit(user.getLogin(), audit);
    }

    /**
     * The authorizationDateTime() return date and time when user authorized in a string format.
     * @return date and time when user authorized in a string format.
     */
    private String logOutDateTime() {
        DateFormat dateFormat = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");
        Date date = new Date();
        return dateFormat.format(date);
    }

    /**
     * @return method nextState() returns the next console state
     * depending on user input.
     */
    @Override
    public ConsoleState nextState() {
        return nextState;
    }
}