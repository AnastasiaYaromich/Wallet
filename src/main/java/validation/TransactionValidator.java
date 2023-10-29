package validation;
import dto.TransactionDto;

public class TransactionValidator {
    public static boolean isValid(TransactionDto transactionDto) {
        String transactionId = transactionDto.getTransactionId();
        String type = transactionDto.getType();
        String condition = transactionDto.getCondition();

        return transactionId != null && transactionId.length() <= 9
                && type != null && condition != null;
    }
}
