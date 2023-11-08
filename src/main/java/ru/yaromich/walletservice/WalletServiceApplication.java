package ru.yaromich.walletservice;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import ru.example.annotation.EnableLoggable;

@EnableLoggable
@SpringBootApplication
public class WalletServiceApplication {
    public static void main(String[] args) {
      SpringApplication.run(WalletServiceApplication.class, args);

    }

}
