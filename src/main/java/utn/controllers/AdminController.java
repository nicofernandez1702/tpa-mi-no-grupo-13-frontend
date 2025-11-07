package utn.controllers;

import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import utn.models.dto.ColeccionDTO;
import utn.models.dto.HechoDTO;
import utn.models.dto.SolicitudDTO;
import utn.services.MetaMapaApiService;

import java.util.ArrayList;
import java.util.List;

@Controller
@RequiredArgsConstructor
public class AdminController {

    private final MetaMapaApiService metaMapaApiService;

    @GetMapping("/panel-control")
    @PreAuthorize("hasRole('ADMINISTRADOR')")
    public String panelControl(Model model, HttpSession session) {

        List<ColeccionDTO> colecciones = metaMapaApiService.obtenerColecciones();

        model.addAttribute("titulo", "Panel de Control");
        model.addAttribute("usuario", session.getAttribute("username"));
        model.addAttribute("coleccionesDestacadas", colecciones);
        return "admin/control_panel";
    }

    // TODO Anotacion: Probablemente falle cuando hayan solicitudes de prueba porque en el template uso datos del hecho
    //  correspondiente a la solicitud, y en el DTO no viene el hecho como objeto, sino como String */
    @GetMapping("/solicitudes_eliminacion")
    public String solicitudes(Model model, HttpSession session) {
        model.addAttribute("titulo", "Solicitudes");

        String accessToken = (String) session.getAttribute("accessToken");
        if (accessToken != null) {
            model.addAttribute("usuario", session.getAttribute("username"));
        }

        try {
            List<SolicitudDTO> solicitudes = metaMapaApiService.obtenerSolicitudesDeEliminacion();
            model.addAttribute("solicitudes", solicitudes != null ? solicitudes : List.of());
        } catch (Exception e) {
            model.addAttribute("solicitudes", List.of());
            model.addAttribute("errorCarga", "No se pudieron cargar las solicitudes: " + e.getMessage());
        }

        return "admin/solicitudes_eliminacion";
    }

    @PostMapping("/solicitudes_eliminacion/{id}/aceptar")
    public String aceptarSolicitud(@PathVariable Long id, RedirectAttributes redirectAttributes) {
        try {
            metaMapaApiService.aceptarSolicitudEliminacion(id);
            redirectAttributes.addFlashAttribute("mensajeExito", "Solicitud aceptada correctamente.");
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("mensajeError", "Error al aceptar la solicitud: " + e.getMessage());
        }
        return "redirect:/solicitudes_eliminacion";
    }

    @PostMapping("/solicitudes_eliminacion/{id}/rechazar")
    public String rechazarSolicitud(@PathVariable Long id, RedirectAttributes redirectAttributes) {
        try {
            metaMapaApiService.rechazarSolicitudEliminacion(id);
            redirectAttributes.addFlashAttribute("mensajeExito", "Solicitud rechazada correctamente.");
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("mensajeError", "Error al rechazar la solicitud: " + e.getMessage());
        }
        return "redirect:/solicitudes_eliminacion";
    }

    @GetMapping("/hechos_pendientes")
    public String hechosPendientes(Model model, HttpSession session) {

        model.addAttribute("titulo", "Hechos Pendientes");

        String accessToken =  (String) session.getAttribute("accessToken");
        if (accessToken != null) {
            model.addAttribute("usuario", session.getAttribute("username"));
        }
        // TODO funcion obtenerHechosPendientes()
        // List<HechoDTO> hechosPendientes = metaMapaApiService.obtenerHechosPendientes();

        //model.addAttribute("hechos", hechosPendientes);

        return "admin/hechos_pendientes";
    }

    @GetMapping("/importar_csv")
    public String importarCsv(Model model, HttpSession session) {
        model.addAttribute("titulo", "Importar CSV");

        String accessToken =  (String) session.getAttribute("accessToken");
        if (accessToken != null) {
            model.addAttribute("usuario", session.getAttribute("username"));
        }

        return "admin/importar_csv";
    }

    @PostMapping("/importar_csv")
    public ResponseEntity<String> importarArchivo(@RequestParam("archivo") MultipartFile archivo) {
        try {
            // delegamos toda la lógica al servicio
            metaMapaApiService.importarArchivoCsv(archivo);
            return ResponseEntity.ok("Archivo enviado correctamente al backend real");
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.internalServerError().body("Error al procesar el archivo: " + e.getMessage());
        }
    }

}
