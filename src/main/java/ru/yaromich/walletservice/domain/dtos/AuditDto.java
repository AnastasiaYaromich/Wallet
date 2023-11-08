package ru.yaromich.walletservice.domain.dtos;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@AllArgsConstructor
@NoArgsConstructor
@Data
@Schema(description = "Модель аудита")
public class AuditDto {
    private int id;
    private String type;
    private String status;
    private BigDecimal balance;
    private String dateTime;
    private String note;
    private UserDto userDto;
}
