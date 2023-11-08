package ru.example.annotation;

import org.springframework.context.annotation.Import;
import ru.example.config.AuditConfig;

import java.lang.annotation.*;

@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
@Import(AuditConfig.class)
@Inherited
public @interface EnableLoggable {
}
