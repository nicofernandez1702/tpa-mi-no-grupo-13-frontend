package utn.services;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.reactive.function.client.WebClientResponseException;
import utn.models.dto.AuthResponseDTO;
import utn.models.dto.ColeccionDTO;
import utn.models.dto.HechoDTO;
import utn.models.dto.RolesPermisosDTO;
import utn.services.internal.WebApiCallerService;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Map;

@Service
public class MetaMapaApiService {

    private static final Logger log = LoggerFactory.getLogger(MetaMapaApiService.class);
    private final WebClient webClient;
    private final String authServiceUrl;
    private final String metamapaServiceUrl;

    @Autowired
    public MetaMapaApiService(
            @Value("${auth.service.url}") String authServiceUrl,
            @Value("${metamapa.service.url}") String metamapaServiceUrl) {
        this.webClient = WebClient.builder().build();
        this.authServiceUrl = authServiceUrl;
        this.metamapaServiceUrl = metamapaServiceUrl;
    }

    // ====== AUTH ======
    public AuthResponseDTO login(String username, String password) {
        try {
            return webClient.post()
                    .uri(authServiceUrl + "/auth")
                    .bodyValue(Map.of("username", username, "password", password))
                    .retrieve()
                    .bodyToMono(AuthResponseDTO.class)
                    .block();
        } catch (WebClientResponseException e) {
            log.error("Error HTTP en login: {}", e.getMessage());
            if (e.getStatusCode() == HttpStatus.NOT_FOUND) return null;
            throw new RuntimeException("Error en el servicio de autenticación: " + e.getMessage(), e);
        } catch (Exception e) {
            log.error("Error de conexión con el servicio de auth: {}", e.getMessage());
            throw new RuntimeException("Error de conexión con el servicio de autenticación: " + e.getMessage(), e);
        }
    }

    // ====== HECHOS ======
    public List<HechoDTO> obtenerTodosLosHechos(String accessToken) {
        try {
            WebClient.RequestHeadersSpec<?> request = (accessToken != null && !accessToken.isBlank())
                    ? webClient.get()
                    .uri(metamapaServiceUrl + "/admin/hechos")
                    .header(HttpHeaders.AUTHORIZATION, "Bearer " + accessToken)
                    : webClient.get().uri(metamapaServiceUrl + "/hechos");

            return request.retrieve()
                    .bodyToFlux(HechoDTO.class)
                    .collectList()
                    .blockOptional()
                    .orElse(Collections.emptyList());
        } catch (WebClientResponseException e) {
            log.error("Error HTTP al obtener hechos: {}", e.getMessage());
            throw new RuntimeException("Error al obtener hechos del backend remoto", e);
        } catch (Exception e) {
            log.error("Error de conexión al backend de hechos: {}", e.getMessage());
            throw new RuntimeException("Error de conexión con el backend remoto", e);
        }
    }

    public RolesPermisosDTO getRolesPermisos(String accessToken) {
        try {
            return webClient.get()
                    .uri(authServiceUrl + "/auth/user/roles-permisos")
                    .header(HttpHeaders.AUTHORIZATION, "Bearer " + accessToken)
                    .retrieve()
                    .bodyToMono(RolesPermisosDTO.class)
                    .block();
        } catch (WebClientResponseException e) {
            log.error("Error HTTP al obtener roles y permisos: {}", e.getMessage());
            throw new RuntimeException("Error al obtener roles y permisos del backend remoto", e);
        } catch (Exception e) {
            log.error("Error de conexión al backend de roles y permisos: {}", e.getMessage());
            throw new RuntimeException("Error de conexión con el backend remoto", e);
        }
    }

    // ====== COLECCIONES ======

    /** Crear nueva colección */
    public String crearColeccion(String accessToken, String titulo, String descripcion) {
        try {
            return webClient.post()
                    .uri(metamapaServiceUrl + "/admin/colecciones")
                    .header(HttpHeaders.AUTHORIZATION, "Bearer " + accessToken)
                    .bodyValue(Map.of("titulo", titulo, "descripcion", descripcion))
                    .retrieve()
                    .bodyToMono(String.class)
                    .block();
        } catch (Exception e) {
            log.error("Error al crear colección: {}", e.getMessage());
            throw new RuntimeException("No se pudo crear la colección", e);
        }
    }

    /** Obtener todas las colecciones */
    public List<ColeccionDTO> obtenerColecciones(String accessToken) {
        try {
            return webClient.get()
                    .uri(metamapaServiceUrl + "/admin/colecciones")
                    .header(HttpHeaders.AUTHORIZATION, "Bearer " + accessToken)
                    .retrieve()
                    .bodyToFlux(ColeccionDTO.class)
                    .collectList()
                    .blockOptional()
                    .orElse(Collections.emptyList());
        } catch (Exception e) {
            log.error("Error al obtener colecciones: {}", e.getMessage());
            throw new RuntimeException("No se pudieron obtener las colecciones", e);
        }
    }

    /** Obtener una colección por ID */
    public ColeccionDTO obtenerColeccionPorId(String accessToken, Long id) {
        try {
            return webClient.get()
                    .uri(metamapaServiceUrl + "/admin/colecciones/{id}", id)
                    .header(HttpHeaders.AUTHORIZATION, "Bearer " + accessToken)
                    .retrieve()
                    .bodyToMono(ColeccionDTO.class)
                    .block();
        } catch (Exception e) {
            log.error("Error al obtener colección por ID: {}", e.getMessage());
            throw new RuntimeException("No se pudo obtener la colección", e);
        }
    }

    /** Obtener los hechos asociados a una colección */
    public List<HechoDTO> obtenerHechosPorColeccion(String accessToken, Long id) {
        try {
            return webClient.get()
                    .uri(metamapaServiceUrl + "/admin/colecciones/{id}/hechos", id)
                    .header(HttpHeaders.AUTHORIZATION, "Bearer " + accessToken)
                    .retrieve()
                    .bodyToFlux(HechoDTO.class)
                    .collectList()
                    .blockOptional()
                    .orElse(Collections.emptyList());
        } catch (Exception e) {
            log.error("Error al obtener hechos de colección: {}", e.getMessage());
            throw new RuntimeException("No se pudieron obtener los hechos de la colección", e);
        }
    }

    /** Actualizar título y descripción de una colección */
    public String actualizarColeccion(String accessToken, Long id, String nuevoTitulo, String nuevaDescripcion) {
        try {
            return webClient.put()
                    .uri(uriBuilder -> uriBuilder
                            .path(metamapaServiceUrl + "/admin/colecciones/{id}")
                            .queryParam("nuevoTitulo", nuevoTitulo)
                            .queryParam("nuevaDescripcion", nuevaDescripcion)
                            .build(id))
                    .header(HttpHeaders.AUTHORIZATION, "Bearer " + accessToken)
                    .retrieve()
                    .bodyToMono(String.class)
                    .block();
        } catch (Exception e) {
            log.error("Error al actualizar colección: {}", e.getMessage());
            throw new RuntimeException("No se pudo actualizar la colección", e);
        }
    }

    /** Eliminar colección por ID */
    public String eliminarColeccion(String accessToken, Long id) {
        try {
            return webClient.delete()
                    .uri(metamapaServiceUrl + "/admin/colecciones/{id}", id)
                    .header(HttpHeaders.AUTHORIZATION, "Bearer " + accessToken)
                    .retrieve()
                    .bodyToMono(String.class)
                    .block();
        } catch (Exception e) {
            log.error("Error al eliminar colección: {}", e.getMessage());
            throw new RuntimeException("No se pudo eliminar la colección", e);
        }
    }

    /** Eliminar todas las colecciones */
    public String eliminarTodasLasColecciones(String accessToken) {
        try {
            return webClient.delete()
                    .uri(metamapaServiceUrl + "/admin/colecciones")
                    .header(HttpHeaders.AUTHORIZATION, "Bearer " + accessToken)
                    .retrieve()
                    .bodyToMono(String.class)
                    .block();
        } catch (Exception e) {
            log.error("Error al eliminar todas las colecciones: {}", e.getMessage());
            throw new RuntimeException("No se pudieron eliminar las colecciones", e);
        }
    }

    /** Agregar o quitar fuente de una colección */
    public String modificarFuenteColeccion(String accessToken, Long id, String fuente, String operacion) {
        try {
            return webClient.patch()
                    .uri(uriBuilder -> uriBuilder
                            .path(metamapaServiceUrl + "/admin/colecciones/{id}/fuentes")
                            .queryParam("fuente", fuente)
                            .queryParam("operacion", operacion)
                            .build(id))
                    .header(HttpHeaders.AUTHORIZATION, "Bearer " + accessToken)
                    .retrieve()
                    .bodyToMono(String.class)
                    .block();
        } catch (Exception e) {
            log.error("Error al modificar fuente: {}", e.getMessage());
            throw new RuntimeException("No se pudo modificar la fuente de la colección", e);
        }
    }
}

