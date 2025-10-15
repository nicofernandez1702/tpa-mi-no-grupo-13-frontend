package utn.controllers;

import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import utn.models.dto.HechoDTO;
import utn.services.MetaMapaApiService;

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
        if (accessToken == null) {
            // Si no hay token, redirigir al login
            return "redirect:/login";
        }

        try {
            // Llamar al servicio pasando el token
            List<HechoDTO> hechos = metaMapaApiService.obtenerTodosLosHechos(accessToken);

            // Seleccionar los primeros 3 hechos como destacados
            List<HechoDTO> hechosDestacados = hechos.stream().limit(3).toList();

            // Pasar los datos a la vista
            model.addAttribute("hechosDestacados", hechosDestacados);
            model.addAttribute("totalHechos", hechos.size());

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
}


