package ru.sbrf.docedit;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.PropertySource;

@SpringBootApplication
@ComponentScan
@PropertySource("classpath:application.properties")
public class SimpleDocEditorApplication {
    public static void main(String[] args) {
        ApplicationContext context = SpringApplication.run(SimpleDocEditorApplication.class, args);
    }
}
