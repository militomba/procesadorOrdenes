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
    private final ColaFinDiaService colaFinDiaService;

    @Autowired
    private final ColaPrincipioDiaService colaPrincipioDiaService;

    @Autowired
    private final ColaInmediatoService colaInmediatoService;

    @Autowired
    private final ColaOperacionesFallidas colaOperacionesFallidas;

    @Autowired
    public AnalizarOrdenes(
        OrdenRepository ordenRepository,
        ColaFinDiaService colaFinDiaService,
        ColaPrincipioDiaService colaPrincipioDiaService,
        ColaInmediatoService colaInmediatoService,
        ColaOperacionesFallidas colaOperacionesFallidas
    ) {
        this.ordenRepository = ordenRepository;
        this.colaFinDiaService = colaFinDiaService;
        this.colaPrincipioDiaService = colaPrincipioDiaService;
        this.colaInmediatoService = colaInmediatoService;
        this.colaOperacionesFallidas = colaOperacionesFallidas;
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
                        if (accionId.equals(accionIdAlmacenada)) {
                            log.info("Accion encontrada: " + accionId);
                            valido = true;
                        }/*else {
                            log.info("No se encontro la accion: " + accionId);
                        }*/
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

        try {
            List<Orden> clientesAlmacenadas = ordenRepository.findAll();

            JsonNode jsonArray = accionMapper.readTree(response);

            JsonNode clientes = jsonArray.get("clientes");

            for (Orden orden : clientesAlmacenadas) {
                Integer clienteIdAlmacenada = orden.getCliente();

                if (clientes != null && clientes.isArray()) {
                    for (JsonNode cliente : clientes) {
                        Integer clienteId = cliente.get("id").asInt();
                        /*log.info("clienteIdAlmacenado: " + clienteIdAlmacenada);
                        log.info("clienteId: " + clienteId);*/

                        if (clienteId.equals(clienteIdAlmacenada)) {
                            log.info("cliente encontrado: " + clienteId);
                            valido = true;
                        }/*else {
                            log.info("No se encontro el cliente " + clienteId);
                        }*/
                    }
                }
            }
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }

        return valido;
    }

    public Orden actualizarPrecioOrdenes(Orden orden) {
        List<Orden> ordenesAlmacenadas = ordenRepository.findAll();

        String url = "http://192.168.194.254:8000";
        String endpoint = "/api/acciones/ultimovalor/" + orden.getAccion();
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

        try {
            JsonNode jsonArray = accionMapper.readTree(response);

            JsonNode ultimoValor = jsonArray.get("ultimoValor");

            String modoOrden = orden.getModo();

            if (modoOrden.equals("PRINCIPIODIA") || modoOrden.equals("FINDIA")) {
                Double precioAlmacenado = orden.getPrecio();
                log.info("precio viejo: " + precioAlmacenado);
                if (ultimoValor != null) {
                    Double valor = ultimoValor.get("valor").asDouble();
                    orden.setPrecio(valor);
                    ordenRepository.save(orden);
                    log.info("precio nuevo: " + valor);
                }
            }
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }

        return null;
    }

    public String repartirOrdenesColas() {
        try {
            List<Orden> listaOrdenes = ordenRepository.findAll();
            Boolean obtenerCliente = this.obtenerClientesServicioProfe();
            Boolean obtenerAcciones = this.obtenerAccionesServicioProfe();
            for (Orden orden : listaOrdenes) {
                if (obtenerCliente.equals(true) && obtenerAcciones.equals(true)) {
                    String modoOrden = orden.getModo();

                    if (modoOrden.equals("AHORA")) {
                        log.info("-----ANALIZANDO HORARIO DE ORDENES INMEDIATAS-----");
                        ZonedDateTime horaOperacion = orden.getFechaOperacion();
                        ZoneId zonaHoraria = ZoneId.of("UTC-3");
                        LocalTime horaOperacionLocal = horaOperacion.withZoneSameInstant(zonaHoraria).toLocalTime();
                        LocalTime horaInicio = LocalTime.of(8, 59);
                        LocalTime horaFinal = LocalTime.of(18, 1);
                        if (horaOperacionLocal.isAfter(horaInicio) && horaOperacionLocal.isBefore(horaFinal)) {
                            colaInmediatoService.agregarOrden(orden);
                            log.info("Orden Inmediata almacenada en la cola: " + orden.toString());
                            //log.info();("El horario de la orden se encuentra dentro de las 9:00 am - 18:00pm");
                        } else {
                            colaOperacionesFallidas.agregarOrden(orden);
                            log.info("No se alamcenp la orden porque el horario de la orden se encuentra fuera de las 9:00 am - 18:00pm");
                        }
                    }
                    if (modoOrden.equals("PRINCIPIODIA")) {
                        this.actualizarPrecioOrdenes(orden);
                        ordenRepository.save(orden);
                        colaPrincipioDiaService.agregarOrden(orden);
                        log.info("Orden Principio Dia almacenada en la cola: " + orden.toString());
                    }
                    if (modoOrden.equals("FINDIA")) {
                        this.actualizarPrecioOrdenes(orden);
                        ordenRepository.save(orden);
                        colaFinDiaService.agregarOrden(orden);
                        log.info("Orden Fin Dia almacenada en la cola: " + orden.toString());
                    }
                } else {
                    colaOperacionesFallidas.agregarOrden(orden);
                    log.info("Orden fallida almacenada " + orden.toString());
                }
            }
        } catch (Exception e) {
            log.error("Error al agregar Orden a la cola", e);
        }
        return null;
    }

    public boolean consultarCantidadAcciones(Orden orden) {
        boolean valido = false;
        String url = "http://192.168.194.254:8000";
        String endpoint =
            "/api/reporte-operaciones/consulta_cliente_accion?clienteId=" + orden.getCliente() + "&accionId=" + orden.getAccionId();
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

            JsonNode cantidadActualNode = jsonArray.get("cantidadActual");

            if (cantidadActualNode == null || cantidadActualNode.isNull()) {
                log.info("EL CLIENTE NO TIENE ACCIONES PARA VENDER");
                valido = false;
            }
            int cantidadActual = cantidadActualNode.asInt();
            int resultado = orden.getCantidad();

            if (resultado <= cantidadActual) {
                log.info("¡Operacion exitosa!");
                valido = true;
            } else {
                log.info("La cantidad de ordenes es mayor a la cantidad actual. !Operacion fallida¡");
                valido = false;
            }
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }

        return valido;
    }
}
/*    @Scheduled(fixedRate = 60000)
    public void analizandoOrdenes() {
        log.info("-------COMENZANDO ANALISIS DE ORDENES-----\n");
        log.info("\n-------ANALIZANDO LA EXISTENCIA DEL CLIENTE-----");
        this.obtenerClientesServicioProfe();
        log.info("\n-------ANALIZANDO LA EXISTENCIA DE LA ACCION-----");
        this.obtenerAccionesServicioProfe();
        this.repartirOrdenesColas();
    }
}*/
