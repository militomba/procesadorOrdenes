package ar.edu.um.programacion2.procesadorordenes.service;

import ar.edu.um.programacion2.procesadorordenes.ProcesadorOrdenesApp;
import ar.edu.um.programacion2.procesadorordenes.domain.Orden;
import ar.edu.um.programacion2.procesadorordenes.repository.OrdenRepository;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import java.util.List;
import java.util.concurrent.CompletableFuture;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.web.reactive.function.client.WebClient;

public class AnalizarOrdenes {

    private static final Logger log = LoggerFactory.getLogger(ProcesadorOrdenesApp.class);
    private final OrdenRepository ordenRepository;

    public AnalizarOrdenes(OrdenRepository ordenRepository) {
        this.ordenRepository = ordenRepository;
    }

    public boolean obtenerAccionesServicioProfe() {
        boolean valido = false;
        String url = "http://192.168.194.254:8000";
        String endpoint = "/api/acciones/";
        String token =
            "eyJhbGciOiJIUzUxMiJ9.eyJzdWIiOiJtaWxpdG9tYmExIiwiYXV0aCI6IlJPTEVfVVNFUiIsImV4cCI6MTcyOTE3OTQwOH0.JFOOJCd7_DuIAyIDgf6DGYiaWUMGAz465guJQMaIwyCUQJyWnkUJrpC6vrxP--g_j1pJAfYD21DuXXhcyAlRYQ";

        WebClient webClient = WebClient
            .builder()
            .baseUrl(url)
            .defaultHeader(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE)
            .defaultHeader(HttpHeaders.ACCEPT, MediaType.APPLICATION_JSON_VALUE)
            .defaultHeader(HttpHeaders.AUTHORIZATION, "Bearer " + token)
            .build();

        String response = webClient.get().uri(endpoint).retrieve().bodyToMono(String.class).block();

        //mapeo de acciones
        ObjectMapper accionMapper = new ObjectMapper();
        accionMapper.registerModule(new JavaTimeModule());
        accionMapper.configure(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS, false);

        try {
            List<Orden> ordenesAlmacenadas = ordenRepository.findAll();

            JsonNode jsonArray = accionMapper.readTree(response);

            JsonNode acciones = jsonArray.get("acciones");
            for (Orden orden : ordenesAlmacenadas) {
                Integer accionIdAlmacenada = orden.getAccionId();

                if (acciones != null && acciones.isArray() && acciones.size() > 0) {
                    for (JsonNode accion : acciones) {
                        Integer accionId = accion.get("id").asInt();
                        if (accionId == accionIdAlmacenada) {
                            log.info("Accion encontrada");
                            valido = true;
                        } else {
                            log.info("No se encontro la accion");
                        }
                    }
                }
            }
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }

        return valido;
    }
}
