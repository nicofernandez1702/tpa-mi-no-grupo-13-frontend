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

    private final WebApiCallerService webApiCaller;
    private final String authServiceUrl;
    private final String metamapaServiceUrl;

    @Autowired
    public MetaMapaApiService(
            WebApiCallerService webApiCaller,
            @Value("${auth.service.url}") String authServiceUrl,
            @Value("${metamapa.service.url}") String metamapaServiceUrl) {
        this.webApiCaller = webApiCaller;
        this.authServiceUrl = authServiceUrl;
        this.metamapaServiceUrl = metamapaServiceUrl;
    }

    public AuthResponseDTO login(String username, String password) {
        try {
            Map<String, String> body = Map.of("username", username, "password", password);

            // login sin usar WebApiCaller porque no hay token todavía
            return WebClient.builder().build()
                    .post()
                    .uri(authServiceUrl + "/auth")
                    .bodyValue(body)
                    .retrieve()
                    .bodyToMono(AuthResponseDTO.class)
                    .block();
        } catch (Exception e) {
            log.error("Error en login: {}", e.getMessage());
            throw new RuntimeException("Error al autenticar: " + e.getMessage(), e);
        }
    }

    // ====== HECHOS ======
    public List<HechoDTO> obtenerTodosLosHechos(String accessToken) {
        String url = (accessToken != null && !accessToken.isBlank())
                ? metamapaServiceUrl + "/admin/hechos"
                : metamapaServiceUrl + "/hechos";
        try {
            return webApiCaller.getList(url, HechoDTO.class);
        } catch (Exception e) {
            log.error("Error al obtener hechos: {}", e.getMessage());
            throw new RuntimeException("No se pudieron obtener los hechos", e);
        }
    }

    public RolesPermisosDTO getRolesPermisos(String accessToken) {
        try {
            String url = authServiceUrl + "/auth/user/roles-permisos";
            return webApiCaller.getWithAuth(url, accessToken, RolesPermisosDTO.class);
        } catch (Exception e) {
            log.error("Error al obtener roles y permisos: {}", e.getMessage());
            throw new RuntimeException("Error al obtener roles y permisos", e);
        }
    }

    // ====== COLECCIONES ======

    public String crearColeccion(String accessToken, String titulo, String descripcion) {
        try {
            Map<String, String> body = Map.of("titulo", titulo, "descripcion", descripcion);
            return webApiCaller.post(metamapaServiceUrl + "/admin/colecciones", body, String.class);
        } catch (Exception e) {
            log.error("Error al crear colección: {}", e.getMessage());
            throw new RuntimeException("No se pudo crear la colección", e);
        }
    }

    public List<ColeccionDTO> obtenerColecciones(String accessToken) {
        try {
            return webApiCaller.getList(metamapaServiceUrl + "/admin/colecciones", ColeccionDTO.class);
        } catch (Exception e) {
            log.error("Error al obtener colecciones: {}", e.getMessage());
            throw new RuntimeException("No se pudieron obtener las colecciones", e);
        }
    }

    public ColeccionDTO obtenerColeccionPorId(String accessToken, Long id) {
        try {
            String url = metamapaServiceUrl + "/admin/colecciones/" + id;
            return webApiCaller.get(url, ColeccionDTO.class);
        } catch (Exception e) {
            log.error("Error al obtener colección por ID: {}", e.getMessage());
            throw new RuntimeException("No se pudo obtener la colección", e);
        }
    }

    public List<HechoDTO> obtenerHechosPorColeccion(String accessToken, Long id) {
        try {
            String url = metamapaServiceUrl + "/admin/colecciones/" + id + "/hechos";
            return webApiCaller.getList(url, HechoDTO.class);
        } catch (Exception e) {
            log.error("Error al obtener hechos de colección: {}", e.getMessage());
            throw new RuntimeException("No se pudieron obtener los hechos de la colección", e);
        }
    }

    public String actualizarColeccion(String accessToken, Long id, String nuevoTitulo, String nuevaDescripcion) {
        try {
            String url = String.format(
                    "%s/admin/colecciones/%d?nuevoTitulo=%s&nuevaDescripcion=%s",
                    metamapaServiceUrl, id, nuevoTitulo, nuevaDescripcion
            );
            return webApiCaller.put(url, null, String.class);
        } catch (Exception e) {
            log.error("Error al actualizar colección: {}", e.getMessage());
            throw new RuntimeException("No se pudo actualizar la colección", e);
        }
    }

    public String eliminarColeccion(String accessToken, Long id) {
        try {
            webApiCaller.delete(metamapaServiceUrl + "/admin/colecciones/" + id);
            return "Colección eliminada correctamente";
        } catch (Exception e) {
            log.error("Error al eliminar colección: {}", e.getMessage());
            throw new RuntimeException("No se pudo eliminar la colección", e);
        }
    }

    public String eliminarTodasLasColecciones(String accessToken) {
        try {
            webApiCaller.delete(metamapaServiceUrl + "/admin/colecciones");
            return "Todas las colecciones fueron eliminadas";
        } catch (Exception e) {
            log.error("Error al eliminar todas las colecciones: {}", e.getMessage());
            throw new RuntimeException("No se pudieron eliminar las colecciones", e);
        }
    }

    public String modificarFuenteColeccion(String accessToken, Long id, String fuente, String operacion) {
        try {
            String url = String.format(
                    "%s/admin/colecciones/%d/fuentes?fuente=%s&operacion=%s",
                    metamapaServiceUrl, id, fuente, operacion
            );
            // PATCH -> se usa WebClient directo o se agrega soporte en WebApiCaller si querés
            return webApiCaller.put(url, null, String.class);
        } catch (Exception e) {
            log.error("Error al modificar fuente de colección: {}", e.getMessage());
            throw new RuntimeException("No se pudo modificar la fuente", e);
        }
    }
}


