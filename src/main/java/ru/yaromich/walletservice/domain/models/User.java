package ru.yaromich.walletservice.domain.models;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.util.List;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class User {
    private Long id;
    private String login;
    private String password;
    List<Transaction> transactions;
    List<Role> roles;
    List<Audit> audits;
    private BigDecimal balance;
}
