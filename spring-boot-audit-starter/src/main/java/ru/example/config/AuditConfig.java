package ru.example.config;

import org.springframework.boot.autoconfigure.AutoConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.env.Environment;
import ru.example.audit.TimeAspect;

@Configuration
public class AuditConfig {
    private final Environment environment;

    public AuditConfig(Environment environment) {
        this.environment = environment;
    }

    @Bean
    public TimeAspect timeAspect() {
        return new TimeAspect();
    }
}
