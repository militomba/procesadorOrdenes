package ar.edu.um.programacion2.procesadorordenes.web.rest;

import ar.edu.um.programacion2.procesadorordenes.domain.Orden;
import ar.edu.um.programacion2.procesadorordenes.service.ReporteOrdenes;
import java.time.LocalDate;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.util.List;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * ReportesResource controller
 */
@RestController
@RequestMapping("/api/reportes-resource")
public class ReportesResource {

    private final Logger log = LoggerFactory.getLogger(ReportesResource.class);

    @Autowired
    ReporteOrdenes reporteOrdenes;

    @GetMapping("/cliente/{id}")
    public List<Orden> filtrarPorCLiente(@PathVariable int id) {
        return reporteOrdenes.getReporteClienteId(id);
    }

    @GetMapping("/accionId/{accionId}")
    public List<Orden> filtrarPorAccionId(@PathVariable int accionId) {
        return reporteOrdenes.getReporteAccionId(accionId);
    }

    /*    @GetMapping("/cliente/{clienteId}/accion/{accionId}")
    public List<Orden> filtrarPorClienteIdyAccionId(@PathVariable int clienteId, @PathVariable Integer accionId) {
        return reporteOrdenes.getReporteClienteIdAccion(clienteId, accionId);
    }*/

    @GetMapping("/fechaOperacion/{fechaInicio}/{fechaFin}")
    public List<Orden> filtrarPorFecha(
        @PathVariable @DateTimeFormat(pattern = "yyyy-MM-dd") LocalDate fechaInicio,
        @PathVariable @DateTimeFormat(pattern = "yyyy-MM-dd") LocalDate fechaFin
    ) {
        ZonedDateTime principioFecha = fechaInicio.atStartOfDay(ZoneId.systemDefault());
        ZonedDateTime finFecha = fechaFin.atStartOfDay(ZoneId.systemDefault());
        return reporteOrdenes.getReporteFechaOperacion(principioFecha, finFecha);
    }

    @GetMapping("/codigoAccion/{accion}")
    public List<Orden> filtrarPorCodigoAccion(@PathVariable String accion) {
        accion = accion.toUpperCase();
        return reporteOrdenes.getReporteAccion(accion);
    }
}
