package ru.yaromich.walletservice.domain.models;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class Audit {
    private int id;
    private String type;
    private String status;
    private BigDecimal balance;
    private String dateTime;
    private String note;
    private User user;
}
