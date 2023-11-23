package ar.edu.um.programacion2.procesadorordenes;

import ar.edu.um.programacion2.procesadorordenes.ProcesadorOrdenesApp;
import ar.edu.um.programacion2.procesadorordenes.config.AsyncSyncConfiguration;
import ar.edu.um.programacion2.procesadorordenes.config.EmbeddedSQL;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.DirtiesContext;

/**
 * Base composite annotation for integration tests.
 */
@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
@SpringBootTest(classes = { ProcesadorOrdenesApp.class, AsyncSyncConfiguration.class })
@EmbeddedSQL
@DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_CLASS)
public @interface IntegrationTest {
}
