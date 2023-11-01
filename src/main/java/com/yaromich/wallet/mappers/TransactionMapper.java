package com.yaromich.wallet.mappers;

import com.yaromich.wallet.domain.dto.TransactionDto;
import com.yaromich.wallet.domain.model.Transaction;
import org.mapstruct.AfterMapping;
import org.mapstruct.Mapper;
import org.mapstruct.MappingTarget;
import org.mapstruct.factory.Mappers;

import java.util.Optional;

@Mapper
public interface TransactionMapper {
    TransactionMapper MAPPER = Mappers.getMapper(TransactionMapper.class);

    TransactionDto transactionToTransactionDto(Transaction transaction);
    Transaction transactionDtoToTransaction(TransactionDto transactionDto);

    @AfterMapping
    default void setTransaction(@MappingTarget Transaction transaction) {
        Optional.ofNullable(transaction.getUser())
                .ifPresent(user -> user.getTransactions().add(transaction));
    }
}
