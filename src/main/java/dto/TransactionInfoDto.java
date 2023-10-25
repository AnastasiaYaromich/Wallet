package dto;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import javax.validation.constraints.DecimalMax;
import javax.validation.constraints.NotEmpty;
import java.math.BigDecimal;

@NoArgsConstructor
@Getter
@Setter
public class TransactionInfoDto {

    @NotEmpty
    private String transactionId;

    @DecimalMax(value = "5000.0", inclusive = false)
    private BigDecimal amount;

    public String getTransactionId() {
        return transactionId;
    }

    public void setTransactionId(String transactionId) {
        this.transactionId = transactionId;
    }

    public BigDecimal getAmount() {
        return amount;
    }

    public void setAmount(BigDecimal amount) {
        this.amount = amount;
    }
}
