package dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotEmpty;


@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class AuditInfoDto {

    @NotEmpty
    @Min(3)
    private String username;

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }
}
