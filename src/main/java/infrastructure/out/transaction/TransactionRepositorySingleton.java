package infrastructure.out.transaction;

import aop.annotations.Speed;
import repositories.TransactionRepository;
import java.sql.Connection;
import java.sql.SQLException;

@Speed
public class TransactionRepositorySingleton {
    private static TransactionRepository transactionRepositoryInstance;

    public static TransactionRepository getTransactionRepositoryInstance() throws SQLException {
        if(transactionRepositoryInstance == null) {
            transactionRepositoryInstance = new TransactionRepositoryImpl();
        }
        return transactionRepositoryInstance;
    }
}
