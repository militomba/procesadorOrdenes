package ar.edu.um.programacion2.procesadorordenes.service;

import ar.edu.um.programacion2.procesadorordenes.ProcesadorOrdenesApp;
import ar.edu.um.programacion2.procesadorordenes.domain.Orden;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Queue;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

@Service
public class ColaFinDiaService {

    private static final Logger log = LoggerFactory.getLogger(ProcesadorOrdenesApp.class);
    private Queue<Orden> cola = new LinkedList<>();

    public void agregarOrden(Orden orden) {
        cola.offer(orden);
    }

    public Queue<Orden> getCola() {
        return cola;
    }
    /*    //quitar y devolver el primer elemento de la cola
    public Orden quitarOrden() {
        return cola.poll();
    }

    // Método para ver el primer elemento de la cola sin quitarlo
    public Orden verOrdenSiguiente() {
        return cola.peek();
    }

    // Método para verificar si la cola está vacía
    public boolean ordenVacia() {
        return cola.isEmpty();
    }

    // Método para obtener el tamaño de la cola
    public int tamanoCola() {
        return cola.size();
    }

    public List<Orden> obtenerElementosDeCola() {
        List<Orden> listaOrdenes = new ArrayList<>(cola);
        return listaOrdenes;
    }*/

}
