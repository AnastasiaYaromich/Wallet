package infrastructure.out.state.main.wallet;

import domain.models.Audit;
import domain.models.Transaction;
import domain.models.User;
import infrastructure.in.AuditServiceSingleton;
import infrastructure.out.factories.console.ConsoleManager;
import infrastructure.out.factories.console.ConsoleManagerSingleton;
import infrastructure.in.TransactionServiceSingleton;
import infrastructure.out.state.ConsoleState;
import infrastructure.out.state.main.MainState;
import infrastructure.out.state.main.user.RouteUserState;
import services.exceptions.transaction.NotEnoughMoneyException;
import services.exceptions.transaction.TransactionIdIsNotUniqueException;
import services.services.AuditService;
import services.services.TransactionService;

import java.io.IOException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import static infrastructure.out.util.InputUtils.isEmpty;

/**
 * The RouteUserState uses to show a transaction console state for user.
 */
public class TransactionState implements ConsoleState {
    /**
     * The consoleManager variable uses for waiting user input.
     */
    private final ConsoleManager consoleManager;
    /**
     * The userService variable uses to manage transactions.
     */
    private final TransactionService transactionService;
    /**
     * The auditService variable uses to record an audit.
     */
    private final AuditService auditService;
    /**
     * The nextState variable represents a console state.
     */
    private ConsoleState nextState;
    /**
     * The user variable uses to access a user wallet.
     */
    private final User user;

    /**
     * Constructor which initialize a transaction console state for user.
     */
    public TransactionState(User user) {
        this.consoleManager = ConsoleManagerSingleton.getInstance();
        this.transactionService = TransactionServiceSingleton.getTransactionService();
        this.auditService = AuditServiceSingleton.getAuditService();
        this.nextState = new MainState();
        this.user = user;
    }


    /**
     * The process() method shows a transaction menu for user of Wallet-Service application.
     * Method suggests user to enter what transaction type he would like to do, or he might see the history
     * and waits the user input.
     * After each of operations method ask user if he wants to continue or not.
     * @throws Exception input errors.
     */
    @Override
    public void process() throws Exception {
        String transactionId = "";
        double amount = 0.0;
        String input = "";

        try {
            System.out.println(String.format("\n" +
                    "Please enter type of operation?" +
                    " %n 'withdraw' - withdraw money from wallet," +
                    "%n 'replenish' - replenish money to wallet, " +
                    "%n 'history' - watch a transactions history."));
            input = consoleManager.readLine();

            if(input.equalsIgnoreCase("withdraw")) {
                transactionId = requestTransactionId();

                if (transactionService.isTransactionIdUnique(user.getLogin(), transactionId)) {
                    amount = requestAmountOfMoney();
                    transactionService.withdraw(user.getLogin(), amount);
                    transactionService.saveTransaction(user.getLogin(), transactionId, input, "SUCCESS", "");
                    System.out.println(String.format("You are successfully withdraw money. Current balance is: " + transactionService.currentBalance(user.getLogin()) + "$"));
                    saveAuditRecord(transactionId, "withdraw", "success", "");
                }
            }

            if(input.equalsIgnoreCase("replenish")) {
                transactionId = requestTransactionId();

                if (transactionService.isTransactionIdUnique(user.getLogin(), transactionId)) {
                    amount = requestAmountOfMoney();
                    transactionService.replenish(user.getLogin(), amount);
                    transactionService.saveTransaction(user.getLogin(), transactionId, input, "SUCCESS", "");
                    System.out.println(String.format("You are successfully replenish money. Current balance is: " + transactionService.currentBalance(user.getLogin()) + "$" ));
                    saveAuditRecord(transactionId, "replenish", "success", "");
                }
            }

            if(input.equalsIgnoreCase("history")) {
                List<Transaction> transactions = transactionService.receiveTransactionsHistory(user.getLogin());
                if(transactions == null) {
                    System.out.println(String.format("History is empty"));
                } else {
                    System.out.println(String.format("\n" + "Transactions history for " + user.getLogin() + ": "));
                    for (Transaction t: transactions) {
                        System.out.println(t.toString());
                    }
                }
            }

            nextUserAction();

        } catch (TransactionIdIsNotUniqueException | NotEnoughMoneyException e ) {
            System.out.println(e.getMessage());
            transactionService.saveTransaction(user.getLogin(), transactionId, input, "FAIL", e.getMessage());

            if(input.equalsIgnoreCase("withdraw")) {
                saveAuditRecord(transactionId, "withdraw", "fail", e.getMessage());
            } else {
                saveAuditRecord(transactionId, "withdraw", "fail", e.getMessage());
            }
            nextUserAction();
        }
    }

    /**
     * The requestAmountOfMoney() method requests from user to enter amount of money.
     * */
    public double requestAmountOfMoney() throws IOException {
        double amount = 0.0;
        do {
            System.out.print(String.format("Enter amount of money: "));
            amount = Double.parseDouble(consoleManager.readLine());
        } while (isEmpty("amount", amount));
        return amount;
    }

    /**
     * The requestTransactionId() method requests from user to enter id.
     */
    public String requestTransactionId() throws IOException {
        String transactionId = "";
        do {
            System.out.print(String.format("\n" + "Enter transaction id: "));
            transactionId = consoleManager.readLine();
        } while (isEmpty("transaction", transactionId));
        return transactionId;
    }

    /**
     * The saveAuditRecord() method create and save audit record about transaction.
     * @param id transaction id
     * @param type transaction type
     * @param status transaction status
     *
     */
    private void saveAuditRecord(String id, String type, String status, String note) {
        Audit audit = new Audit();
        audit.setType(type);
        audit.setStatus(status);
        audit.setDateTime(transactionDateTime());
        audit.setNote(note);
        audit.setBalance(transactionService.currentBalance(user.getLogin()));
        auditService.saveAudit(user.getLogin(), audit);
    }

    /**
     * The nextUserAction() method ask user if he wants to continually do transactions and
     * route user to console state which according to his answer.
     * @throws IOException input errors.
     */
    private void nextUserAction() throws IOException {
        System.out.println(String.format("\n" + "Do you want to continue? (yes/no)"));
        String input = consoleManager.readLine();
        if (!"yes".equalsIgnoreCase(input)) {
            nextState = new RouteUserState(user);
        } else {
            nextState = new TransactionState(user);
        }
    }

    /**
     * The transactionDateTime() return transaction date and time in a string format.
     * @return transaction date and time in a string format.
     */
    private String transactionDateTime() {
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
