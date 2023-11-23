package ar.edu.um.programacion2.procesadorordenes.repository;

import ar.edu.um.programacion2.procesadorordenes.domain.Orden;
import java.time.ZonedDateTime;
import java.util.List;
import java.util.Optional;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

/**
 * Spring Data JPA repository for the Orden entity.
 */
@SuppressWarnings("unused")
@Repository
public interface OrdenRepository extends JpaRepository<Orden, Long> {
    boolean existsByFechaOperacion(ZonedDateTime fechaOperacion);
    List<Orden> findByFechaOperacion(ZonedDateTime fechaOperacion);
}
