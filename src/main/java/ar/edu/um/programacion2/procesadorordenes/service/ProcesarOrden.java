package ar.edu.um.programacion2.procesadorordenes.service;

import ar.edu.um.programacion2.procesadorordenes.ProcesadorOrdenesApp;
import ar.edu.um.programacion2.procesadorordenes.domain.Orden;
import ar.edu.um.programacion2.procesadorordenes.repository.OrdenRepository;
import java.util.ArrayList;
import java.util.List;
import java.util.Queue;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

public class ProcesarOrden {

    private static final Logger log = LoggerFactory.getLogger(ProcesadorOrdenesApp.class);
    private final OrdenRepository ordenRepository;

    @Autowired
    protected AnalizarOrdenes analizarOrdenes;

    @Autowired
    ColaInmediatoService colaInmediatoService;

    @Autowired
    ColaFinDiaService colaFinDiaService;

    @Autowired
    ColaPrincipioDiaService colaPrincipioDiaService;

    @Autowired
    ColaOperacionesFallidas colaOperacionesFallidas;

    public ProcesarOrden(OrdenRepository ordenRepository) {
        this.ordenRepository = ordenRepository;
    }

    //FUNCION DE COMPRA
    public boolean comprar(Orden orden) {
        orden.setOperacionExitosa(true);
        orden.setOperacionObservacion("Compra realizada con exito");
        return true;
    }

    //FUNCION DE VENTA
    public boolean vender(Orden orden) {
        if (analizarOrdenes.consultarCantidadAcciones(orden) == true) {
            orden.setOperacionExitosa(true);
            orden.setOperacionObservacion("Compra realizada con exito");
        } else {
            orden.setOperacionExitosa(false);
            orden.setOperacionObservacion("No se puede realizar la compra");
            colaOperacionesFallidas.agregarOrden(orden);
        }
        return true;
    }

    public boolean procesarOrdenesInmediatas() {
        Queue<Orden> colaInmediata = colaInmediatoService.getCola();
        List<Orden> ordenProcesada = new ArrayList<>();

        while (!colaInmediata.isEmpty()) {
            Orden orden = colaInmediata.poll();
            if (orden.getOperacion().equals("COMPRA")) {
                comprar(orden);
                log.info("COMPRA REALIZADA CON EXITO\n ORDEN: " + orden.toString());
                ordenRepository.save(orden);
                ordenProcesada.add(orden);
            } else {
                vender(orden);
                log.info("VENTA REALIZADA CON EXITO\n ORDEN: " + orden.toString());
                ordenRepository.save(orden);
                ordenProcesada.add(orden);
            }
            if (!ordenProcesada.isEmpty()) {
                //funcion de reporte al servicio del profe
            }
        }
        log.info("COLA DE ORDENES INMEDIATAS VACIA");
        return false;
    }

    public boolean procesarOrdenesFinDia() {
        Queue<Orden> colaFinDia = colaFinDiaService.getCola();
        List<Orden> ordenProcesada = new ArrayList<>();

        while (!colaFinDia.isEmpty()) {
            Orden orden = colaFinDia.poll();
            if (orden.getOperacion().equals("COMPRA")) {
                comprar(orden);
                log.info("COMPRA REALIZADA CON EXITO\n ORDEN: " + orden.toString());
                ordenRepository.save(orden);
                ordenProcesada.add(orden);
            } else {
                vender(orden);
                log.info("VENTA REALIZADA CON EXITO\n ORDEN: " + orden.toString());
                ordenRepository.save(orden);
                ordenProcesada.add(orden);
            }
            if (!ordenProcesada.isEmpty()) {
                //funcion de reporte al servicio del profe
            }
        }
        log.info("COLA DE ORDENES FIN DIA VACIA");
        return false;
    }

    public boolean procesarOrdenesPrincDia() {
        Queue<Orden> colaPrincDia = colaPrincipioDiaService.getCola();
        List<Orden> ordenProcesada = new ArrayList<>();

        while (!colaPrincDia.isEmpty()) {
            Orden orden = colaPrincDia.poll();
            if (orden.getOperacion().equals("COMPRA")) {
                comprar(orden);
                log.info("COMPRA REALIZADA CON EXITO\n ORDEN: " + orden.toString());
                ordenRepository.save(orden);
                ordenProcesada.add(orden);
            } else {
                vender(orden);
                log.info("VENTA REALIZADA CON EXITO\n ORDEN: " + orden.toString());
                ordenRepository.save(orden);
                ordenProcesada.add(orden);
            }
            if (!ordenProcesada.isEmpty()) {
                //funcion de reporte al servicio del profe
            }
        }
        log.info("COLA DE ORDENES PRINCIPIO DIA VACIA");
        return false;
    }
}
