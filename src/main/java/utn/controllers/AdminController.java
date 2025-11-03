package utn.controllers;

import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
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

        String accessToken =  (String) session.getAttribute("accessToken");
        if (accessToken != null) {
            model.addAttribute("usuario", session.getAttribute("username"));
        }
        try {
            // TODO hacer función metaMapaApiService.obtenerSolicitudesDeEliminacion()
            List<SolicitudDTO> solicitudes = new ArrayList<>();
            // solicitudes = metaMapaApiService.obtenerSolicitudesDeEliminacion();
            model.addAttribute("solicitudes", solicitudes);
        } catch (Exception e) {
            model.addAttribute("No se pudieron cargar las solicitudes: ", e.getMessage());
        }

        return "admin/solicitudes_eliminacion";
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

}
