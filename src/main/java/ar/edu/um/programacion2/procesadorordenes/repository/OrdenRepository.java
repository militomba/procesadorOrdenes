package ar.edu.um.programacion2.procesadorordenes.repository;

import ar.edu.um.programacion2.procesadorordenes.domain.Orden;
import com.google.inject.spi.StaticInjectionRequest;
import java.time.Instant;
import java.util.List;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

/**
 * Spring Data JPA repository for the Orden entity.
 */
@SuppressWarnings("unused")
@Repository
public interface OrdenRepository extends JpaRepository<Orden, Long> {
    //Reportes
    List<Orden> findByReportadaAndCliente(Boolean reportada, int clienteId);
    List<Orden> findByReportadaAndAccionId(Boolean reportada, Integer accionId);
    List<Orden> findByReportadaAndClienteAndAccionId(Boolean reportada, int clienteId, Integer accionId);
    List<Orden> findByReportadaAndFechaOperacionBetween(Boolean reportada, Instant inicioDia, Instant finDia);
}
