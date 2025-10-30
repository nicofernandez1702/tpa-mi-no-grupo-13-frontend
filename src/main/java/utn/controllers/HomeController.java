package utn.controllers;

import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import utn.models.dto.ColeccionDTO;
import utn.models.dto.HechoDTO;
import utn.services.MetaMapaApiService;

import java.util.ArrayList;
import java.util.List;

@Controller
@RequiredArgsConstructor
public class HomeController {

    private final MetaMapaApiService metaMapaApiService;

    @GetMapping("/")
    public String home(Model model, HttpSession session) {
        model.addAttribute("titulo", "Inicio");

        // Obtener el accessToken de la sesión
        String accessToken = (String) session.getAttribute("accessToken");
        if (accessToken != null) {
            // Agrego datos de usuario

            model.addAttribute("usuario", session.getAttribute("username"));
        }

        try {
            // Llamar al servicio pasando el token
            List<HechoDTO> hechos = metaMapaApiService.obtenerTodosLosHechos();
            List<ColeccionDTO> colecciones = metaMapaApiService.obtenerColecciones();
            List<ColeccionDTO> coleccionesDestacadas = colecciones.stream().limit(3).toList();

            // Seleccionar los primeros 3 hechos como destacados
            List<HechoDTO> hechosDestacados = hechos.stream().limit(3).toList();

            // Pasar los datos a la vista
            model.addAttribute("hechosDestacados", hechosDestacados);
            model.addAttribute("totalHechos", hechos.size());
            model.addAttribute("coleccionesDestacadas", coleccionesDestacadas);

        } catch (Exception e) {
            model.addAttribute("error", "No se pudieron cargar los hechos: " + e.getMessage());
        }

        return "index";
    }

    @GetMapping("/home")
    public String redirectHome() {
        return "redirect:/";
    }

    @GetMapping("/error")
    public String notFound() {
        return "error";
    }

    @GetMapping("/panel-control")
    @PreAuthorize("hasRole('ADMINISTRADOR')")
    public String panelControl(Model model, HttpSession session) {

        List<ColeccionDTO> colecciones = metaMapaApiService.obtenerColecciones();

        model.addAttribute("titulo", "Panel de Control");
        model.addAttribute("usuario", session.getAttribute("username"));
        model.addAttribute("coleccionesDestacadas", colecciones);
        return "admin/colecciones_admin";
    }

    @GetMapping("/perfil")
    public String perfil(Model model, HttpSession session) {
        model.addAttribute("titulo", "Perfil");

        String username = (String) session.getAttribute("username");
        if (username != null) {
            model.addAttribute("usuario", username);
        }

        return "perfil";
    }
}


