package utn.services;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.core.io.InputStreamResource;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.reactive.function.client.WebClientResponseException;
import utn.models.dto.*;
import utn.services.internal.WebApiCallerService;

import java.io.IOException;
import java.io.InputStream;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Map;

@Service
public class MetaMapaApiService {

    private static final Logger log = LoggerFactory.getLogger(MetaMapaApiService.class);

    private final UploadService uploadService;
    private final WebApiCallerService webApiCaller;
    private final String authServiceUrl;
    private final String metamapaServiceUrl;
    private final String dinamicaServiceUrl;
    private final String estaticaServiceUrl;

    @Autowired
    public MetaMapaApiService(
            UploadService uploadService,
            WebApiCallerService webApiCaller,
            @Value("${auth.service.url}") String authServiceUrl,
            @Value("${metamapa.service.url}") String metamapaServiceUrl,
            @Value("${metamapa.dinamica.url}") String dinamicaServiceUrl,
            @Value("${metamapa.estatica.url}") String estaticaServiceUrl) {
        this.uploadService = uploadService;
        this.webApiCaller = webApiCaller;
        this.authServiceUrl = authServiceUrl;
        this.metamapaServiceUrl = metamapaServiceUrl;
        this.dinamicaServiceUrl = dinamicaServiceUrl;
        this.estaticaServiceUrl = estaticaServiceUrl;
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

    public HechoDTO obtenerHechoPorId(Long id) {
        return safeGetPublic(metamapaServiceUrl + "/hechos/" + id, HechoDTO.class);
    }

    // ====== CREAR HECHO ======
    public String crearHecho(HechoFormDTO hechoFormDTO) {
        try {
            HechoDTO hechoDTO = new HechoDTO();
            hechoDTO.setTitulo(hechoFormDTO.getTitulo());
            hechoDTO.setDescripcion(hechoFormDTO.getDescripcion());
            hechoDTO.setCategoria(hechoFormDTO.getCategoria());
            hechoDTO.setLatitud(hechoFormDTO.getLatitud());
            hechoDTO.setLongitud(hechoFormDTO.getLongitud());
            hechoDTO.setFecha_hecho(hechoFormDTO.getFecha());

            if (hechoFormDTO.getArchivo() != null && !hechoFormDTO.getArchivo().isEmpty()) {
                String pathMultimedia = uploadService.guardarArchivo(hechoFormDTO.getArchivo());
                hechoDTO.setMultimediaPath(pathMultimedia);
                System.out.println("Multimedia del hecho enviándose: " + pathMultimedia);
            }

            // Intentar con token, si existe
            try {
                return webApiCaller.post(dinamicaServiceUrl + "/hechos", hechoDTO, String.class);
            } catch (RuntimeException e) {
                // Si el error es "No hay token de acceso disponible", usar post sin token
                if (e.getMessage() != null && e.getMessage().contains("No hay token de acceso disponible")) {
                    System.out.println("⚠️ No hay token disponible, enviando hecho sin autenticación...");
                    return webApiCaller.postWithoutToken(dinamicaServiceUrl + "/hechos", hechoDTO, String.class);
                }
                throw e;
            }

        } catch (Exception e) {
            log.error("Error al crear hecho: {}", e.getMessage(), e);
            throw new RuntimeException("Error al crear hecho", e);
        }
    }


    // ====== SOLICITUD DE ELIMINACION ======
    public String crearSolicitud(SolicitudDTO solicitud) {
        return webApiCaller.post(metamapaServiceUrl + "/solicitudes", solicitud, String.class);
    }

    public List<SolicitudDTO> obtenerSolicitudesDeEliminacion() {

        return webApiCaller.getList(metamapaServiceUrl + "/admin/solicitudes", SolicitudDTO.class);

    }

    public void aceptarSolicitudEliminacion(Long id) {
        String url = metamapaServiceUrl + "/admin/solicitudes/" + id;
        webApiCaller.post(url, "aceptar", Void.class);
    }

    public void rechazarSolicitudEliminacion(Long id) {
        String url = metamapaServiceUrl + "/admin/solicitudes/" + id;
        webApiCaller.post(url, "rechazar", Void.class);
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
    public String crearColeccion(String accessToken, ColeccionDTO coleccionDTO) {

        System.out.println("POST → /admin/colecciones");
        System.out.println("ColeccionDTO: " + coleccionDTO);

        return webApiCaller.post(
                metamapaServiceUrl + "/admin/colecciones",
                coleccionDTO,
                String.class
        );
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

    // ====== CONFIG COLECCIONES ==========

    public List<String> getFuentes() {
        return webApiCaller.getList(metamapaServiceUrl + "/admin/config/fuentes", String.class);

    }

    public List<String> getAlgoritmos() {
        return webApiCaller.getList(metamapaServiceUrl + "/admin/config/algoritmos", String.class);

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

    // Enviar csv a la fuente estática
    public void importarArchivoCsv(MultipartFile archivo) throws IOException {
        RestTemplate restTemplate = new RestTemplate();
        // Crear un multipart/form-data para reenviar el archivo
        MultiValueMap<String, Object> body = new LinkedMultiValueMap<>();
        body.add("file", new MultipartInputStreamFileResource(archivo.getInputStream(), archivo.getOriginalFilename()));

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.MULTIPART_FORM_DATA);

        HttpEntity<MultiValueMap<String, Object>> requestEntity = new HttpEntity<>(body, headers);

        // Realizar el POST al backend real TODO: Acá tendría que haber usado el webApiCaller, refactorizar más adelante
        ResponseEntity<String> response = restTemplate.postForEntity(estaticaServiceUrl + "/hechos/cargar", requestEntity, String.class);

        if (!response.getStatusCode().is2xxSuccessful()) {
            throw new RuntimeException("Error al enviar archivo al backend real: " + response.getBody());
        }
    }

    // Auxiliar para el envío de csv a fuente estática
    private static class MultipartInputStreamFileResource extends InputStreamResource {
        private final String filename;

        MultipartInputStreamFileResource(InputStream inputStream, String filename) {
            super(inputStream);       // guarda el stream del archivo
            this.filename = filename; // guarda el nombre real del archivo
        }

        @Override
        public String getFilename() {
            return this.filename;     // asegura que RestTemplate conozca el nombre
        }

        @Override
        public long contentLength() {
            return -1; // evita errores si no se puede determinar el tamaño exacto
        }
    }

}



