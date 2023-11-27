package ar.edu.um.programacion2.procesadorordenes.service;

import ar.edu.um.programacion2.procesadorordenes.ProcesadorOrdenesApp;
import ar.edu.um.programacion2.procesadorordenes.domain.Orden;
import ar.edu.um.programacion2.procesadorordenes.repository.OrdenRepository;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import java.time.LocalTime;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.util.List;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;

@Service
public class AnalizarOrdenes {

    private static final Logger log = LoggerFactory.getLogger(ProcesadorOrdenesApp.class);

    private final OrdenRepository ordenRepository;

    @Autowired
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
                            log.info("Accion encontrada: " + accionId);
                            valido = true;
                        } else {
                            log.info("No se encontro la accion: " + accionId);
                        }
                    }
                }
            }
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }

        return valido;
    }

    public boolean obtenerClientesServicioProfe() {
        boolean valido = false;
        String url = "http://192.168.194.254:8000";
        String endpoint = "/api/clientes/";
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

        //mapeo de clientes
        ObjectMapper accionMapper = new ObjectMapper();
        accionMapper.registerModule(new JavaTimeModule());
        accionMapper.configure(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS, false);
        int n = 0;
        try {
            List<Orden> clientesAlmacenadas = ordenRepository.findAll();

            JsonNode jsonArray = accionMapper.readTree(response);

            JsonNode clientes = jsonArray.get("clientes");

            for (Orden orden : clientesAlmacenadas) {
                Integer clienteIdAlmacenada = orden.getCliente();
                log.debug("Analaisis de orden numero: " + n++);

                if (clientes != null && clientes.isArray()) {
                    for (JsonNode cliente : clientes) {
                        int clienteId = cliente.get("id").asInt();
                        log.debug("clienteIdAlmacenado: " + clienteIdAlmacenada);
                        log.debug("clienteId: " + clienteId);

                        if (clienteId == clienteIdAlmacenada) {
                            log.info("cliente encontrado: " + clienteId);
                            valido = true;
                        } else {
                            log.info("No se encontro el cliente " + clienteId);
                        }
                    }
                }
            }
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }

        return valido;
    }

    public boolean analizarHorario() {
        boolean valido = true;
        List<Orden> fechaOperacionRepository = ordenRepository.findAll();
        for (Orden orden : fechaOperacionRepository) {
            ZonedDateTime horaOperacion = orden.getFechaOperacion();
            ZoneId zonaHoraria = ZoneId.of("UTC-3");
            LocalTime horaOperacionLocal = horaOperacion.withZoneSameInstant(zonaHoraria).toLocalTime();
            LocalTime horaInicio = LocalTime.of(8, 59);
            LocalTime horaFinal = LocalTime.of(18, 1);
            if (horaOperacionLocal.isAfter(horaInicio) && horaOperacionLocal.isBefore(horaFinal)) {
                valido = true;
                log.debug("El horario de la orden se encuentra dentro de las 9:00 am - 18:00pm");
            } else {
                valido = false;
                log.debug("El horario de la orden se encuentra fuera de las 9:00 am - 18:00pm");
            }
        }

        return valido;
    }

    @Scheduled(fixedRate = 60000)
    public void analizandoOrdenes() {
        log.debug("-------Comenzando analizis de ordenes------");
        /*        this.obtenerAccionesServicioProfe();
        this.obtenerClientesServicioProfe();*/
        this.analizarHorario();
    }
}
