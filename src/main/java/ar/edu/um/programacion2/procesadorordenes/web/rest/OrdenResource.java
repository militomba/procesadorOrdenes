package ar.edu.um.programacion2.procesadorordenes.web.rest;

import ar.edu.um.programacion2.procesadorordenes.domain.Orden;
import ar.edu.um.programacion2.procesadorordenes.repository.OrdenRepository;
import ar.edu.um.programacion2.procesadorordenes.service.OrdenService;
import ar.edu.um.programacion2.procesadorordenes.web.rest.errors.BadRequestAlertException;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;
import tech.jhipster.web.util.HeaderUtil;
import tech.jhipster.web.util.PaginationUtil;
import tech.jhipster.web.util.ResponseUtil;

/**
 * REST controller for managing {@link ar.edu.um.programacion2.procesadorordenes.domain.Orden}.
 */
@RestController
@RequestMapping("/api")
public class OrdenResource {

    private final Logger log = LoggerFactory.getLogger(OrdenResource.class);

    private static final String ENTITY_NAME = "orden";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final OrdenService ordenService;

    private final OrdenRepository ordenRepository;

    public OrdenResource(OrdenService ordenService, OrdenRepository ordenRepository) {
        this.ordenService = ordenService;
        this.ordenRepository = ordenRepository;
    }

    /**
     * {@code POST  /ordens} : Create a new orden.
     *
     * @param orden the orden to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new orden, or with status {@code 400 (Bad Request)} if the orden has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/ordens")
    public ResponseEntity<Orden> createOrden(@RequestBody Orden orden) throws URISyntaxException {
        log.debug("REST request to save Orden : {}", orden);
        if (orden.getId() != null) {
            throw new BadRequestAlertException("A new orden cannot already have an ID", ENTITY_NAME, "idexists");
        }
        Orden result = ordenService.save(orden);
        return ResponseEntity
            .created(new URI("/api/ordens/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * {@code PUT  /ordens/:id} : Updates an existing orden.
     *
     * @param id the id of the orden to save.
     * @param orden the orden to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated orden,
     * or with status {@code 400 (Bad Request)} if the orden is not valid,
     * or with status {@code 500 (Internal Server Error)} if the orden couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/ordens/{id}")
    public ResponseEntity<Orden> updateOrden(@PathVariable(value = "id", required = false) final Long id, @RequestBody Orden orden)
        throws URISyntaxException {
        log.debug("REST request to update Orden : {}, {}", id, orden);
        if (orden.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, orden.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!ordenRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Orden result = ordenService.update(orden);
        return ResponseEntity
            .ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, orden.getId().toString()))
            .body(result);
    }

    /**
     * {@code PATCH  /ordens/:id} : Partial updates given fields of an existing orden, field will ignore if it is null
     *
     * @param id the id of the orden to save.
     * @param orden the orden to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated orden,
     * or with status {@code 400 (Bad Request)} if the orden is not valid,
     * or with status {@code 404 (Not Found)} if the orden is not found,
     * or with status {@code 500 (Internal Server Error)} if the orden couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/ordens/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public ResponseEntity<Orden> partialUpdateOrden(@PathVariable(value = "id", required = false) final Long id, @RequestBody Orden orden)
        throws URISyntaxException {
        log.debug("REST request to partial update Orden partially : {}, {}", id, orden);
        if (orden.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, orden.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!ordenRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<Orden> result = ordenService.partialUpdate(orden);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, orden.getId().toString())
        );
    }

    /**
     * {@code GET  /ordens} : get all the ordens.
     *
     * @param pageable the pagination information.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of ordens in body.
     */
    @GetMapping("/ordens")
    public ResponseEntity<List<Orden>> getAllOrdens(@org.springdoc.api.annotations.ParameterObject Pageable pageable) {
        log.debug("REST request to get a page of Ordens");
        Page<Orden> page = ordenService.findAll(pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    /**
     * {@code GET  /ordens/:id} : get the "id" orden.
     *
     * @param id the id of the orden to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the orden, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/ordens/{id}")
    public ResponseEntity<Orden> getOrden(@PathVariable Long id) {
        log.debug("REST request to get Orden : {}", id);
        Optional<Orden> orden = ordenService.findOne(id);
        return ResponseUtil.wrapOrNotFound(orden);
    }

    /**
     * {@code DELETE  /ordens/:id} : delete the "id" orden.
     *
     * @param id the id of the orden to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/ordens/{id}")
    public ResponseEntity<Void> deleteOrden(@PathVariable Long id) {
        log.debug("REST request to delete Orden : {}", id);
        ordenService.delete(id);
        return ResponseEntity
            .noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString()))
            .build();
    }
}
