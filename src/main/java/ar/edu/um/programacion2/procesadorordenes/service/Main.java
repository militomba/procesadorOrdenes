package ar.edu.um.programacion2.procesadorordenes.service;

import ar.edu.um.programacion2.procesadorordenes.ProcesadorOrdenesApp;
import ar.edu.um.programacion2.procesadorordenes.domain.Orden;
import ar.edu.um.programacion2.procesadorordenes.repository.OrdenRepository;
import java.util.List;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

@Service
public class Main {

    private static final Logger log = LoggerFactory.getLogger(ProcesadorOrdenesApp.class);
    private final OrdenRepository ordenRepository;

    @Autowired
    ObtenerOrdenes obtenerOrdenes;

    @Autowired
    AnalizarOrdenes analizarOrdenes;

    @Autowired
    ProcesarOrden procesarOrden;

    @Autowired
    ReporteOrdenes reporteOrdenes;

    public Main(OrdenRepository ordenRepository) {
        this.ordenRepository = ordenRepository;
    }

    @Scheduled(fixedRate = 60000)
    public void obtenerOrdenes() {
        log.info("1.OBTENER Y ALMACNAR ORDENES");
        obtenerOrdenes.obtenerOrdenesServicioProfe();
        obtenerOrdenes.almacenarOrdenes();
    }

    @Scheduled(fixedRate = 60000)
    public void analizarOrdenes() {
        log.info("2.ANALIZANDO ORDENES");
        /*  analizarOrdenes.obtenerAccionesServicioProfe();
        analizarOrdenes.obtenerClientesServicioProfe();*/
        analizarOrdenes.repartirOrdenesColas();
    }

    @Scheduled(fixedRate = 90000)
    public void procesarOrdenes() {
        log.info("3.PROCESANDO ORDENES");
        procesarOrden.procesarOrdenesInmediatas();
        procesarOrden.procesarOrdenesPrincDia();
        procesarOrden.procesarOrdenesFinDia();
        procesarOrden.reportarOrdenesFallidas();
        List<Orden> ordenes = ordenRepository.findAll();
        for (Orden ordenReportada : ordenes) {
            Boolean reporte = ordenReportada.getReportada();
            Integer accionId = ordenReportada.getAccionId();
            int cliente = ordenReportada.getCliente();
            if (reporte.equals(true)) {
                reporteOrdenes.getReporteAccionId(accionId);
                reporteOrdenes.getReporteClienteId(cliente);
                //reporteOrdenes.getReporteClienteIdAccion(cliente, accionId);
            }
        }
    }
    /*        reporteOrdenes.getReporteClienteId();
        reporteOrdenes.getReporteAccionId();*/

}
