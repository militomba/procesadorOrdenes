package ar.edu.um.programacion2.procesadorordenes.service;

import ar.edu.um.programacion2.procesadorordenes.ProcesadorOrdenesApp;
import ar.edu.um.programacion2.procesadorordenes.domain.Orden;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.google.common.net.HttpHeaders;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.util.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;

@Service
public class ReporteOrdenes {

    private static final Logger log = LoggerFactory.getLogger(ProcesadorOrdenesApp.class);

    public void enviarOrdenes(List<Orden> ordenes) {
        try {
            //Las ordenes pasan a tipo json como dice el pdf del profe(con la clave "ordenes")

            ObjectMapper objectMapper = new ObjectMapper();
            objectMapper.registerModule(new JavaTimeModule());
            objectMapper.disable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS);
            // Crear una lista de mapas para representar cada orden individualmente
            List<Map<String, Object>> listaOrdenesMap = new ArrayList<>();
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss'Z'");
            for (Orden orden : ordenes) {
                Map<String, Object> ordenMap = new HashMap<>();
                ordenMap.put("cliente", orden.getCliente());
                ordenMap.put("accionId", orden.getAccionId());
                ordenMap.put("accion", orden.getAccion());
                ordenMap.put("operacion", orden.getOperacion());
                ordenMap.put("cantidad", orden.getCantidad());
                ordenMap.put("precio", orden.getPrecio());
                // Convertir la fecha a ZonedDateTime con zona horaria UTC
                ZonedDateTime fechaConvertida = ZonedDateTime.of(orden.getFechaOperacion().toLocalDateTime(), ZoneId.of("Z"));

                // Formatear la fecha en el nuevo formato
                String fechaEnNuevoFormato = fechaConvertida.format(formatter);

                ordenMap.put("fechaOperacion", fechaEnNuevoFormato);
                ordenMap.put("modo", orden.getModo());
                ordenMap.put("operacionExitosa", orden.getOperacionExitosa());
                ordenMap.put("operacionObservaciones", orden.getOperacionObservaciones());

                listaOrdenesMap.add(ordenMap);
            }
            Map<String, List<Map<String, Object>>> mapOrdenes = Collections.singletonMap("ordenes", listaOrdenesMap);
            String ordenesJSON = objectMapper.writeValueAsString(mapOrdenes);

            String token =
                "eyJhbGciOiJIUzUxMiJ9.eyJzdWIiOiJtaWxpdG9tYmExIiwiYXV0aCI6IlJPTEVfVVNFUiIsImV4cCI6MTcyOTE3OTQwOH0.JFOOJCd7_DuIAyIDgf6DGYiaWUMGAz465guJQMaIwyCUQJyWnkUJrpC6vrxP--g_j1pJAfYD21DuXXhcyAlRYQ";

            // Configurar la solicitud HTTP
            HttpClient httpClient = HttpClient.newHttpClient();

            HttpRequest request = HttpRequest
                .newBuilder()
                .uri(URI.create("http://192.168.194.254:8000/api/reporte-operaciones/reportar"))
                .header(HttpHeaders.AUTHORIZATION, "Bearer " + token)
                .header(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE)
                .POST(HttpRequest.BodyPublishers.ofString(ordenesJSON))
                .build();

            // Enviar la solicitud y manejar la respuesta
            HttpResponse<String> response = httpClient.send(request, HttpResponse.BodyHandlers.ofString());
            if (response.statusCode() == 200) {
                log.info("Órdenes enviadas con éxito. Respuesta: " + response.body());
            } else {
                log.info("Error al enviar órdenes. Código de respuesta: " + response.statusCode());
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
