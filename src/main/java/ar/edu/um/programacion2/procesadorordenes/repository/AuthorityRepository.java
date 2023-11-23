package ar.edu.um.programacion2.procesadorordenes.repository;

import ar.edu.um.programacion2.procesadorordenes.domain.Authority;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 * Spring Data JPA repository for the {@link Authority} entity.
 */
public interface AuthorityRepository extends JpaRepository<Authority, String> {}
