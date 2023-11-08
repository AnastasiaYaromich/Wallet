package ru.yaromich.walletservice.domain.mappers;

import org.mapstruct.AfterMapping;
import org.mapstruct.InjectionStrategy;
import org.mapstruct.Mapper;
import org.mapstruct.MappingTarget;
import org.mapstruct.factory.Mappers;
import ru.yaromich.walletservice.domain.dtos.TransactionDto;
import ru.yaromich.walletservice.domain.models.Transaction;

import java.util.Optional;

@Mapper(componentModel = "spring", injectionStrategy = InjectionStrategy.CONSTRUCTOR)
public interface TransactionMapper {
    TransactionMapper MAPPER = Mappers.getMapper(TransactionMapper.class);

    TransactionDto transactionToTransactionDto(Transaction transaction);
    Transaction transactionDtoToTransaction(TransactionDto transactionDto);

    @AfterMapping
    default void setTransaction(@MappingTarget Transaction transaction) {
        Optional.ofNullable(transaction.getUser())
                .ifPresent(user -> user.getTransactions().add(transaction));
    }

    @AfterMapping
    default void setTransactionDto(@MappingTarget TransactionDto transactionDto) {
        Optional.ofNullable(transactionDto.getUserDto())
                .ifPresent(userDto -> userDto.getTransactions().add(transactionDto));
    }
}
