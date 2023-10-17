package infrastructure.in;

import repositories.repositories.TransactionRepository;

public class TransactionRepositorySingleton {
    private static TransactionRepository transactionRepositoryInstance;

    /**
     * @return TransactionRepository implementation
     */
    public static TransactionRepository getTransactionRepositoryInstance() {
        if(transactionRepositoryInstance == null) {
            transactionRepositoryInstance = new TransactionInMemory();
        }
        return transactionRepositoryInstance;
    }
}
