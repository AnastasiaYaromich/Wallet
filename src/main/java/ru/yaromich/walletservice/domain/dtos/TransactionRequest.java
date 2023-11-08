package ru.yaromich.walletservice.domain.dtos;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class TransactionRequest {
    private String username;
    private String transactionId;
    private BigDecimal amount;
    private String type;
}
