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

    // ====== LOGIN ======
    public AuthResponseDTO login(String username, String password) {
        try {
            Map<String, String> body = Map.of("username", username, "password", password);
            return WebClient.builder().build()
                    .post()
                    .uri(authServiceUrl + "/auth")
                    .bodyValue(body)
                    .retrieve()
                    .bodyToMono(AuthResponseDTO.class)
                    .block();
        } catch (Exception e) {
            log.error("Error en login: {}", e.getMessage());
            throw new RuntimeException("Error al autenticar", e);
        }
    }

    // ====== HECHOS PÚBLICOS ======
    public List<HechoDTO> obtenerTodosLosHechos() {
        return safeGetListPublic(metamapaServiceUrl + "/hechos", HechoDTO.class);
    }

    // ====== COLECCIONES PÚBLICAS ======
    public List<ColeccionDTO> obtenerColecciones() {
        return safeGetListPublic(metamapaServiceUrl + "/admin/colecciones", ColeccionDTO.class);
    }

    public ColeccionDTO obtenerColeccionPorId(Long id) {
        return safeGetPublic(metamapaServiceUrl + "/colecciones/" + id, ColeccionDTO.class);
    }

    public List<HechoDTO> obtenerHechosPorColeccion(Long id) {
        return safeGetListPublic(metamapaServiceUrl + "/admin/colecciones/" + id + "/hechos", HechoDTO.class);
    }

    // ====== COLECCIONES CON TOKEN (admin) ======
    public String crearColeccion(String accessToken, String titulo, String descripcion) {
        Map<String, String> body = Map.of("titulo", titulo, "descripcion", descripcion);
        return webApiCaller.post(metamapaServiceUrl + "/admin/colecciones", body, String.class);
    }

    public String actualizarColeccion(String accessToken, Long id, String nuevoTitulo, String nuevaDescripcion) {
        String url = String.format("%s/admin/colecciones/%d?nuevoTitulo=%s&nuevaDescripcion=%s",
                metamapaServiceUrl, id, nuevoTitulo, nuevaDescripcion);
        return webApiCaller.put(url, null, String.class);
    }

    public String eliminarColeccion(String accessToken, Long id) {
        webApiCaller.delete(metamapaServiceUrl + "/admin/colecciones/" + id);
        return "Colección eliminada correctamente";
    }

    public String eliminarTodasLasColecciones(String accessToken) {
        webApiCaller.delete(metamapaServiceUrl + "/admin/colecciones");
        return "Todas las colecciones fueron eliminadas";
    }

    public String modificarFuenteColeccion(String accessToken, Long id, String fuente, String operacion) {
        String url = String.format("%s/admin/colecciones/%d/fuentes?fuente=%s&operacion=%s",
                metamapaServiceUrl, id, fuente, operacion);
        return webApiCaller.put(url, null, String.class);
    }

    // ====== ROLES Y PERMISOS ======
    public RolesPermisosDTO getRolesPermisos(String accessToken) {
        return webApiCaller.getWithAuth(authServiceUrl + "/auth/user/roles-permisos", accessToken, RolesPermisosDTO.class);
    }

    // ====== HELPERS PÚBLICOS ======
    private <T> T safeGetPublic(String url, Class<T> responseType) {
        try {
            return webApiCaller.getPublic(url, responseType);
        } catch (Exception e) {
            log.error("Error al obtener recurso público {}: {}", url, e.getMessage());
            throw new RuntimeException("Error al obtener recurso público", e);
        }
    }

    private <T> List<T> safeGetListPublic(String url, Class<T> responseType) {
        try {
            return webApiCaller.getListPublic(url, responseType);
        } catch (Exception e) {
            log.error("Error al obtener lista pública {}: {}", url, e.getMessage());
            throw new RuntimeException("Error al obtener lista pública", e);
        }
    }
}



