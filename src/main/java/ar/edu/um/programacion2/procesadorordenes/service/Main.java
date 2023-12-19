package ar.edu.um.programacion2.procesadorordenes.service;

import ar.edu.um.programacion2.procesadorordenes.ProcesadorOrdenesApp;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

@Service
public class Main {

    private static final Logger log = LoggerFactory.getLogger(ProcesadorOrdenesApp.class);

    @Autowired
    ObtenerOrdenes obtenerOrdenes;

    @Autowired
    AnalizarOrdenes analizarOrdenes;

    @Autowired
    ProcesarOrden procesarOrden;

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
    }
}
