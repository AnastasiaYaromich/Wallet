package ru.yaromich.walletservice.domain.mappers;

import org.mapstruct.*;
import org.mapstruct.factory.Mappers;
import ru.yaromich.walletservice.domain.dtos.AuditDto;
import ru.yaromich.walletservice.domain.dtos.RoleDto;
import ru.yaromich.walletservice.domain.dtos.TransactionDto;
import ru.yaromich.walletservice.domain.dtos.UserDto;
import ru.yaromich.walletservice.domain.models.Audit;
import ru.yaromich.walletservice.domain.models.Role;
import ru.yaromich.walletservice.domain.models.Transaction;
import ru.yaromich.walletservice.domain.models.User;

import java.util.List;
import java.util.Optional;

@Mapper(componentModel = "spring", injectionStrategy = InjectionStrategy.CONSTRUCTOR)
public interface UserMapper {
    UserMapper MAPPER = Mappers.getMapper(UserMapper.class);

    @Mapping(target = "transactions", qualifiedByName = "transactionListDtoToTransactionList")
    @Mapping(target = "roles", qualifiedByName = "roleListDtoToRoleList")
    @Mapping(target = "audits", qualifiedByName = "auditListDtoToAuditList")
    User userDtoToUser(UserDto u);

    @IterableMapping(qualifiedByName = "transactionDtoToTransaction")
    @Named("transactionListDtoToTransactionList")
    List<Transaction> transactionListDtoToTransactionList(List<TransactionDto> t);

    @IterableMapping(qualifiedByName = "roleDtoToRole")
    @Named("roleListDtoToRoleList")
    List<Role> roleListDtoToRoleList(List<RoleDto> r);

    @IterableMapping(qualifiedByName = "auditDtoToAudit")
    @Named("auditListDtoToAuditList")
    List<Audit> auditListDtoToAuditList(List<AuditDto> a);

    @Named("transactionDtoToTransaction")
    Transaction transactionDtoToTransaction(TransactionDto t);

    @Named("roleDtoToRole")
    Role roleDtoToRole(RoleDto r);

    @Named("auditDtoToAudit")
    Audit auditDtoToAudit(AuditDto a);

    //////////////////////////////

    @Mapping(target = "transactions", qualifiedByName = "transactionListToTransactionListDto")
    @Mapping(target = "roles", qualifiedByName = "roleListToRoleListDto")
    @Mapping(target = "audits", qualifiedByName = "auditListToAuditListDto")
    UserDto userToUserDto(User u);

    @IterableMapping(qualifiedByName = "transactionToTransactionDto")
    @Named("transactionListToTransactionListDto")
    List<TransactionDto> transactionListToTransactionListDto(List<Transaction> t);

    @IterableMapping(qualifiedByName = "roleToRoleDto")
    @Named("roleListToRoleListDto")
    List<RoleDto> roleListToRoleListDto(List<Role> r);

    @IterableMapping(qualifiedByName = "auditToAuditDto")
    @Named("auditListToAuditListDto")
    List<AuditDto> auditListToAuditListDto(List<Audit> a);

    @Named("transactionToTransactionDto")
    TransactionDto transactionToTransactionDto(Transaction t);

    @Named("roleToRoleDto")
    RoleDto roleToRoleDto(Role r);

    @Named("auditToAuditDto")
    AuditDto auditToAuditDto(Audit a);

    @AfterMapping
    default void setUser(@MappingTarget User user) {
        Optional.ofNullable(user.getTransactions())
                .ifPresent(t -> t.forEach(transaction -> transaction.setUser(user)));

        Optional.ofNullable(user.getAudits())
                .ifPresent(a-> a.forEach(audit -> audit.setUser(user)));
    }

    @AfterMapping
    default void setUserDto(@MappingTarget UserDto userDto) {
        Optional.ofNullable(userDto.getTransactions())
                .ifPresent(t -> t.forEach(transactionDto -> transactionDto.setUserDto(userDto)));

        Optional.ofNullable(userDto.getAudits())
                .ifPresent(a -> a.forEach(auditDto -> auditDto.setUserDto(userDto)));
    }
}

