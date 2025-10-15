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

    /**
     * Método de login: llama al backend de auth y devuelve tokens.
     */
    public AuthResponseDTO login(String username, String password) {
        try {
            AuthResponseDTO response = webClient
                    .post()
                    .uri(authServiceUrl + "/auth")
                    .bodyValue(Map.of(
                            "username", username,
                            "password", password
                    ))
                    .retrieve()
                    .bodyToMono(AuthResponseDTO.class)
                    .block();
            return response;
        } catch (WebClientResponseException e) {
            log.error("Error HTTP en login: {}", e.getMessage());
            if (e.getStatusCode() == HttpStatus.NOT_FOUND) {
                // Login fallido - credenciales incorrectas
                return null;
            }
            throw new RuntimeException("Error en el servicio de autenticación: " + e.getMessage(), e);
        } catch (Exception e) {
            log.error("Error de conexión con el servicio de auth: {}", e.getMessage());
            throw new RuntimeException("Error de conexión con el servicio de autenticación: " + e.getMessage(), e);
        }
    }

    /**
     * Llama al backend remoto y obtiene todos los hechos como DTOs.
     */
    public List<HechoDTO> obtenerTodosLosHechos(String accessToken) {
        try {
            WebClient.RequestHeadersSpec<?> request;

            if (accessToken != null && !accessToken.isBlank()) {
                // Usuario logueado → usa endpoint protegido
                request = webClient
                        .get()
                        .uri(metamapaServiceUrl + "/admin/hechos")
                        .header(HttpHeaders.AUTHORIZATION, "Bearer " + accessToken);
            } else {
                // Visitante → usa endpoint público
                request = webClient
                        .get()
                        .uri(metamapaServiceUrl + "/hechos");
            }

            return request
                    .retrieve()
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


    /**
     * Obtiene roles y permisos del usuario autenticado.
     */
    public RolesPermisosDTO getRolesPermisos(String accessToken) {
        try {
            RolesPermisosDTO response = webClient
                    .get()
                    .uri(authServiceUrl + "/auth/user/roles-permisos")
                    .header(HttpHeaders.AUTHORIZATION, "Bearer " + accessToken)
                    .retrieve()
                    .bodyToMono(RolesPermisosDTO.class)
                    .block();
            return response;
        } catch (WebClientResponseException e) {
            log.error("Error HTTP al obtener roles y permisos: {}", e.getMessage());
            throw new RuntimeException("Error al obtener roles y permisos del backend remoto", e);
        } catch (Exception e) {
            log.error("Error de conexión al backend de roles y permisos: {}", e.getMessage());
            throw new RuntimeException("Error de conexión con el backend remoto", e);
        }
    }
}
