package com.yaromich.wallet.mappers;

import com.yaromich.wallet.domain.dto.AuditDto;
import com.yaromich.wallet.domain.dto.TransactionDto;
import com.yaromich.wallet.domain.dto.UserDto;
import com.yaromich.wallet.domain.model.Audit;
import com.yaromich.wallet.domain.model.Transaction;
import com.yaromich.wallet.domain.model.User;
import org.mapstruct.*;
import org.mapstruct.factory.Mappers;

import java.util.List;
import java.util.Optional;

@Mapper
public interface UserMapper {
    UserMapper MAPPER = Mappers.getMapper(UserMapper.class);

    //
    @Mapping(target = "transactions", qualifiedByName = "transactionListDtoToTransactionList")
    @Mapping(target = "audits", qualifiedByName = "auditListDtoToAuditList")
    User userDtoToUser(UserDto u);

    @IterableMapping(qualifiedByName = "transactionDtoToTransaction")
    @Named("transactionListDtoToTransactionList")
    List<Transaction> transactionListDtoToTransactionList(List<TransactionDto> t);

    @Named("transactionDtoToTransaction")
    Transaction transactionDtoToTransaction(TransactionDto t);


    @Mapping(target = "audits", qualifiedByName = "auditListToAuditListDto")
    @Mapping(target = "roles", source = "roles")
    @Mapping(target = "transactions", qualifiedByName = "transactionListToTransactionListDto")
    UserDto userToUserDto(User u);

    @IterableMapping(qualifiedByName = "transactionToTransactionDto")
    @Named("transactionListToTransactionListDto")
    List<TransactionDto>  transactionListToTransactionListDto(List<Transaction> t);

    @Named("transactionToTransactionDto")
    TransactionDto transactionToTransactionDto(Transaction t);

    //

    @IterableMapping(qualifiedByName = "auditDtoToAudit")
    @Named("auditListDtoToAuditList")
    List<Audit> auditListDtoToAuditList(List<AuditDto> a);

    @Named("auditDtoToAudit")
    Audit auditDtoToAudit(AuditDto a);

    @IterableMapping(qualifiedByName = "auditToAuditDto")
    @Named("auditListToAuditListDto")
    List<AuditDto>  auditListToAuditListDto(List<Audit> a);

    @Named("auditToAuditDto")
    AuditDto auditToAuditDto(Audit a);


    // AAAAAAAAAAAAAAAAAA!!!!!!!
    @AfterMapping
    default void setUser(@MappingTarget User user) {
        Optional.ofNullable(user.getTransactions())
                .ifPresent(t -> t.forEach(transaction -> transaction.setUser(user)));

        Optional.ofNullable(user.getAudits())
                .ifPresent(a -> a.forEach(audit -> audit.setUser(user)));
    }
}
