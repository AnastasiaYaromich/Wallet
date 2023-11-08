package ru.yaromich.walletservice.domain.dtos;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class TransactionDto {
    private int id;
    private String transactionId;
    private String type;
    private String condition;
    private String note;
    private UserDto userDto;
}
