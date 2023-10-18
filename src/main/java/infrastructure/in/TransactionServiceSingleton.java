package infrastructure.in;

import logic.TransactionServiceImpl;
import services.services.TransactionService;

public class TransactionServiceSingleton {
    private static TransactionService transactionServiceInstance;

    /**
     * @return TransactionService implementation
     */
    public static TransactionService getTransactionService() {
        if(transactionServiceInstance == null) {
            transactionServiceInstance = new TransactionServiceImpl(TransactionRepositorySingleton.getTransactionRepositoryInstance());
        }
        return transactionServiceInstance;
    }
}
