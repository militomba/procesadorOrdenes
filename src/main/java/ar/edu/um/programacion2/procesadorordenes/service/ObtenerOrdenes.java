package ar.edu.um.programacion2.procesadorordenes.service;

import ar.edu.um.programacion2.procesadorordenes.ProcesadorOrdenesApp;
import ar.edu.um.programacion2.procesadorordenes.domain.Orden;
import ar.edu.um.programacion2.procesadorordenes.repository.OrdenRepository;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import java.time.ZonedDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CompletableFuture;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;

@Service
public class ObtenerOrdenes {

    private static final Logger log = LoggerFactory.getLogger(ProcesadorOrdenesApp.class);
    private final OrdenRepository ordenRepository;
    private List<Orden> ordenesFinal;

    public ObtenerOrdenes(OrdenRepository ordenRepository) {
        this.ordenRepository = ordenRepository;
    }

    @Value("${procesador_ordenes.token}")
    protected String token;

    @Value("${procesador_ordenes.url}")
    protected String url;

    //Obtener ordenes del servicio del profe

    public CompletableFuture<List<Orden>> obtenerOrdenesServicioProfe() {
        CompletableFuture<List<Orden>> resultFuture = new CompletableFuture<>();
        //String url = "http://192.168.194.254:8000";
        String endpoint = "/api/ordenes/ordenes";
        /*String token =
            "eyJhbGciOiJIUzUxMiJ9.eyJzdWIiOiJtaWxpdG9tYmExIiwiYXV0aCI6IlJPTEVfVVNFUiIsImV4cCI6MTcyOTE3OTQwOH0.JFOOJCd7_DuIAyIDgf6DGYiaWUMGAz465guJQMaIwyCUQJyWnkUJrpC6vrxP--g_j1pJAfYD21DuXXhcyAlRYQ";
*/
        WebClient webClient = WebClient
            .builder()
            .baseUrl(url)
            .defaultHeader(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE)
            .defaultHeader(HttpHeaders.ACCEPT, MediaType.APPLICATION_JSON_VALUE)
            .defaultHeader(HttpHeaders.AUTHORIZATION, "Bearer " + token)
            .build();
        this.ordenesFinal = new ArrayList<>();
        String response = webClient.get().uri(endpoint).retrieve().bodyToMono(String.class).block();

        //mapeo de ordenes
        ObjectMapper ordenMapper = new ObjectMapper();
        ordenMapper.registerModule(new JavaTimeModule());
        ordenMapper.configure(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS, false);
        try {
            JsonNode jsonArray = ordenMapper.readTree(response);

            JsonNode ordenes = jsonArray.get("ordenes");
            for (JsonNode resultadoOrden : ordenes) {
                Orden nuevaOrden = ordenMapper.treeToValue(resultadoOrden, Orden.class);
                nuevaOrden.operacionExitosa(false);
                nuevaOrden.operacionObservaciones("");
                nuevaOrden.reportada(false);
                log.info("-----ORDEN OBTENIDAS DEL SERVICIO DEL PROFE------" + resultadoOrden);
                this.ordenesFinal.add(nuevaOrden);
            }
            //log.info("ORDENES: " + this.ordenesFinal.toString());
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }

        resultFuture.complete(this.ordenesFinal);
        return resultFuture;
    }

    //ALMACENAR ORDENES EN LA BASE DE DATOS
    public void almacenarOrdenes() {
        List<Orden> ordenesAlmacenadas = ordenRepository.findAll();

        for (Orden ordenes : this.ordenesFinal) {
            ZonedDateTime fechaOperacion = ordenes.getFechaOperacion();
            String modo = ordenes.getModo();
            /*            if(!ordenesAlmacenadas.isEmpty()){
                for (Orden ordenAlmacenada : ordenesAlmacenadas){
                    ZonedDateTime fechaOperacionAlmacenado= ordenAlmacenada.getFechaOperacion();
                    String  modoAlmacenado = ordenAlmacenada.getModo();
                    if(!fechaOperacion.equals(fechaOperacionAlmacenado) && modo.equals(modoAlmacenado)){
                        ordenRepository.save(ordenes);
                    }else{
                        log.info("Orden omitida (ya existe): " + ordenes.toString());
                    }
                }*/
            if (ordenesAlmacenadas.isEmpty()) {
                ordenRepository.save(ordenes);
            }
        }
    }
}
/*    public void almacenarOrdenes() {
        List<Orden> ordenesAlmacenadas = ordenRepository.findAll();
        for (Orden orden : this.ordenesFinal) {
            boolean ordenAlmacenada = ordenesAlmacenadas
                .stream()
                .anyMatch(ord -> ord.getFechaOperacion().equals(orden.getFechaOperacion()));
            if (!ordenAlmacenada) {
                this.ordenRepository.save(orden);
            }
        }
    }*/
/*    @Scheduled(fixedRate = 60000)
    public void obtenerResultados() {
        log.info("-------Comenzando peticion------");
        this.obtenerOrdenesServicioProfe();
        this.almacenarOrdenes();
    }*/
