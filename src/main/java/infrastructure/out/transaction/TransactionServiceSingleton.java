package infrastructure.out.transaction;

import aop.annotations.Speed;
import logic.TransactionServiceImpl;
import services.TransactionService;

import java.sql.SQLException;

@Speed
public class TransactionServiceSingleton {
    private static TransactionService transactionServiceInstance;

    public static TransactionService getTransactionService() throws SQLException {
        if(transactionServiceInstance == null) {
            transactionServiceInstance = new TransactionServiceImpl(TransactionRepositorySingleton.getTransactionRepositoryInstance());
        }
        return transactionServiceInstance;
    }
}
