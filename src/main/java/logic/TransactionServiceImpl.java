package logic;

import aop.annotations.Speed;
import domain.models.Transaction;
import dto.TransactionDto;
import mappers.TransactionMapper;
import repositories.TransactionRepository;
import services.TransactionService;
import services.exceptions.NotEnoughMoneyException;
import services.exceptions.TransactionException;
import services.exceptions.TransactionIdIsNotUniqueException;
import java.math.BigDecimal;
import java.util.List;
import java.util.stream.Collectors;

@Speed
public class TransactionServiceImpl implements TransactionService {

    private final TransactionRepository transactionRepository;

    public TransactionServiceImpl(TransactionRepository transactionRepository) {
        this.transactionRepository = transactionRepository;
    }

    @Override
    public boolean isTransactionIdUnique(String login, String id) throws TransactionException {
        if(!transactionRepository.isTransactionIdUnique(login, id)) {
            throw new TransactionIdIsNotUniqueException("Transaction failed. Transaction id is not unique.");
        }
        return transactionRepository.isTransactionIdUnique(login, id);
    }

    @Override
    public void withdraw(String login, BigDecimal amount) throws TransactionException {
        if (findBalanceByLogin(login).compareTo(amount) > 0) {
            transactionRepository.withdraw(login, amount);
        } else {
            throw new NotEnoughMoneyException("Withdrawal failed. Not enough money in your wallet.");
        }
    }

    @Override
    public void replenish(String login, BigDecimal amount) {
        transactionRepository.replenish(login, amount);
    }

    @Override
    public BigDecimal findBalanceByLogin(String login) {
        return transactionRepository.findBalanceByLogin(login);
    }

    @Override
    public TransactionDto save(String login, TransactionDto transactionDto) {
       Transaction transaction = TransactionMapper.MAPPER.mapToTransaction(transactionDto);
       Transaction savedTransaction = transactionRepository.save(login, transaction);
       return TransactionMapper.MAPPER.mapToTransactionDto(savedTransaction);
    }

    @Override
    public List<TransactionDto> findAll(String login) {
        List<Transaction> transactions = transactionRepository.findAll(login);
        return transactions.stream().map((TransactionMapper.MAPPER::mapToTransactionDto))
                .collect(Collectors.toList());
    }
}
