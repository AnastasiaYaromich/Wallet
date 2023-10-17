package domain.models;

public class User {
    private int id;
    private String login;
    private String password;
    private String role = "user";
    private double balance = 0.0;

    public User(String login, String password) {
        this.login = login;
        this.password = password;
    }

    public User() {

    }

    public void setId(int id) {this.id = id;}

    public int getId() {return id;}

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
    
    public double getBalance() {return balance;}

    public void setBalance(double balance) {
        this.balance = balance;
    }
}
