package ar.edu.um.programacion2.procesadorordenes.service;

import ar.edu.um.programacion2.procesadorordenes.ProcesadorOrdenesApp;
import ar.edu.um.programacion2.procesadorordenes.domain.Orden;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Queue;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

@Service
public class ColaPrincipioDiaService {

    private static final Logger log = LoggerFactory.getLogger(ProcesadorOrdenesApp.class);
    private Queue<Orden> cola = new LinkedList<>();

    public void agregarOrden(Orden orden) {
        cola.offer(orden);
    }

    public Queue<Orden> getCola() {
        return cola;
    }
    /*    public List<Orden> obtenerElementosDeCola() {
        List<Orden> listaOrdenes = new ArrayList<>(cola);
        return listaOrdenes;
    }*/
}
