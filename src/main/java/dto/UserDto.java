package dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import javax.validation.constraints.*;
import java.math.BigDecimal;

@AllArgsConstructor
@NoArgsConstructor
@Setter
@Getter
public class UserDto {
    private int id;

    @NotNull
    @Size.List({
            @Size(min = 6, message = "Name should have at least 6 characters"),
            @Size(max = 9, message = "Name should have at most 9 characters")
    })
    private String login;


    @NotNull
    @Size.List({
            @Size(min = 6, message = "Name should have at least 6 characters"),
            @Size(max = 9, message = "Name should have at most 9 characters")
    })
    private String password;

    private String role;
    private BigDecimal balance;


    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getLogin() {
        return login;
    }

    public void setLogin(String login) {
        this.login = login;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getRole() {
        return role;
    }

    public void setRole(String role) {
        this.role = role;
    }

    public BigDecimal getBalance() {
        return balance;
    }

    public void setBalance(BigDecimal balance) {
        this.balance = balance;
    }
}
