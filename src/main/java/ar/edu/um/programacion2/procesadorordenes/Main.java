package ar.edu.um.programacion2.procesadorordenes;

import ar.edu.um.programacion2.procesadorordenes.config.ApplicationProperties;
import ar.edu.um.programacion2.procesadorordenes.service.AnalizarOrdenes;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.liquibase.LiquibaseProperties;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
@EnableScheduling
@EnableConfigurationProperties({ LiquibaseProperties.class, ApplicationProperties.class })
public class Main {

    public static void main(String[] args) {}
}
