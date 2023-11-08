package ru.yaromich.walletservice.domain.dtos;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.util.List;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class UserDto {
    private Long id;
    private String login;
    private String password;
    List<TransactionDto> transactions;
    List<RoleDto> roles;
    List<AuditDto> audits;
    private BigDecimal balance;
}

