package ar.edu.um.programacion2.procesadorordenes.repository;

import ar.edu.um.programacion2.procesadorordenes.domain.Orden;
import java.time.ZonedDateTime;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
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
    List<Orden> findByReportadaAndAccion(Boolean reportada, String accion);

    List<Orden> findByReportadaAndFechaOperacionBetween(Boolean reportada, ZonedDateTime fechaInicio, ZonedDateTime fechaFin);
    List<Orden> findByReportadaAndOperacionExitosa(Boolean reportada, Boolean operacion);
}
