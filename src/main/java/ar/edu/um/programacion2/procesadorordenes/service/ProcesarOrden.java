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
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

@Service
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

    @Autowired
    ReporteOrdenes reporteOrdenes;

    List<Orden> listaOrdenesExitosas = new ArrayList<>();

    public ProcesarOrden(OrdenRepository ordenRepository) {
        this.ordenRepository = ordenRepository;
    }

    //FUNCION DE COMPRA
    public boolean comprar(Orden orden) {
        orden.setOperacionExitosa(true);
        orden.setOperacionObservaciones("Compra realizada con exito");
        ordenRepository.save(orden);
        log.info("COMPRA REALIZADA CON EXITO\n ORDEN: " + orden.toString());

        return true;
    }

    //FUNCION DE VENTA
    public boolean vender(Orden orden) {
        boolean valido = false;
        if (analizarOrdenes.consultarCantidadAcciones(orden) == true) {
            orden.setOperacionExitosa(true);
            //orden.setOperacionObservaciones("Compra realizada con exito");
            log.info("VENTA REALIZADA CON EXITO\n ORDEN: " + orden.toString());
            ordenRepository.save(orden);
            valido = true;
        } else {
            orden.setOperacionExitosa(false);
            //orden.setOperacionObservaciones("No se puede realizar la compra");
            ordenRepository.save(orden);
            colaOperacionesFallidas.agregarOrden(orden);
            log.info("NO SE PUDO REALIZAR LA VENTA\n ORDEN: " + orden.toString());
        }
        return valido;
    }

    public boolean procesarOrdenesInmediatas() {
        Queue<Orden> colaInmediata = colaInmediatoService.getCola();
        log.info(colaInmediata.toString());
        while (!colaInmediata.isEmpty()) {
            Orden orden = colaInmediata.poll();
            if (orden.getOperacion().equals("COMPRA")) {
                comprar(orden);
                listaOrdenesExitosas.add(orden);
            } else {
                boolean venta = vender(orden);
                if (venta == true) {
                    listaOrdenesExitosas.add(orden);
                }
            }
        }
        if (!listaOrdenesExitosas.isEmpty()) {
            System.out.println(listaOrdenesExitosas);
            reporteOrdenes.enviarOrdenes(listaOrdenesExitosas);
            listaOrdenesExitosas.clear();
        }
        log.info("COLA DE ORDENES INMEDIATAS VACIA");
        return true;
    }

    public boolean procesarOrdenesFinDia() {
        Queue<Orden> colaFinDia = colaFinDiaService.getCola();
        while (!colaFinDia.isEmpty()) {
            Orden orden = colaFinDia.poll();
            if (orden.getOperacion().equals("COMPRA")) {
                analizarOrdenes.actualizarPrecioOrdenes(orden);
                ordenRepository.save(orden);
                comprar(orden);
                //log.info("COMPRA REALIZADA CON EXITO\n ORDEN: " + orden.toString());
                listaOrdenesExitosas.add(orden);
            } else {
                boolean venta = vender(orden);
                if (venta == true) {
                    analizarOrdenes.actualizarPrecioOrdenes(orden);
                    ordenRepository.save(orden);
                    listaOrdenesExitosas.add(orden);
                }
            }
        }
        if (!listaOrdenesExitosas.isEmpty()) {
            reporteOrdenes.enviarOrdenes(listaOrdenesExitosas);
            listaOrdenesExitosas.clear();
        }
        log.info("COLA DE ORDENES FIN DIA VACIA");
        return true;
    }

    public boolean procesarOrdenesPrincDia() {
        Queue<Orden> colaPrincDia = colaPrincipioDiaService.getCola();

        while (!colaPrincDia.isEmpty()) {
            Orden orden = colaPrincDia.poll();
            if (orden.getOperacion().equals("COMPRA")) {
                analizarOrdenes.actualizarPrecioOrdenes(orden);
                ordenRepository.save(orden);
                comprar(orden);
                //log.info("COMPRA REALIZADA CON EXITO\n ORDEN: " + orden.toString());
                listaOrdenesExitosas.add(orden);
            } else {
                vender(orden);
                boolean venta = vender(orden);
                if (venta == true) {
                    analizarOrdenes.actualizarPrecioOrdenes(orden);
                    ordenRepository.save(orden);
                    listaOrdenesExitosas.add(orden);
                }
            }
        }
        if (!listaOrdenesExitosas.isEmpty()) {
            reporteOrdenes.enviarOrdenes(listaOrdenesExitosas);
            listaOrdenesExitosas.clear();
        }

        log.info("COLA DE ORDENES PRINCIPIO DIA VACIA");
        return true;
    }

    public boolean reportarOrdenesFallidas() {
        Queue<Orden> colaOrdenesFallidas = colaOperacionesFallidas.getCola();
        List<Orden> listaOrdenesFallidas = new ArrayList<>();

        while (!colaOrdenesFallidas.isEmpty()) {
            Orden orden = colaOrdenesFallidas.poll();
            listaOrdenesFallidas.add(orden);
        }
        reporteOrdenes.enviarOrdenes(listaOrdenesFallidas);
        listaOrdenesFallidas.clear();
        return true;
    }
}
