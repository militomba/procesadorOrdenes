package ar.edu.um.programacion2.procesadorordenes.service;

import ar.edu.um.programacion2.procesadorordenes.ProcesadorOrdenesApp;
import ar.edu.um.programacion2.procesadorordenes.domain.Orden;
import java.util.LinkedList;
import java.util.Queue;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

@Service
public class ColaOperacionesFallidas {

    private static final Logger log = LoggerFactory.getLogger(ProcesadorOrdenesApp.class);
    private Queue<Orden> cola = new LinkedList<>();

    public void agregarOrden(Orden orden) {
        cola.offer(orden);
    }
}
