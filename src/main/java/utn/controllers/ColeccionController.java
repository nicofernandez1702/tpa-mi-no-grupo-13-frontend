package utn.controllers;

import jakarta.servlet.http.HttpSession;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import utn.exceptions.NotFoundException;
import utn.models.dto.ColeccionDTO;
import utn.models.dto.HechoDTO;
import utn.services.ColeccionService;
import utn.services.MetaMapaApiService;

import java.util.List;

@Controller
@RequestMapping("/colecciones")
public class ColeccionController {
    private ColeccionService coleccionService;
    private final MetaMapaApiService metaMapaApiService;

    public ColeccionController(ColeccionService coleccionService, MetaMapaApiService metaMapaApiService) {
        this.coleccionService = coleccionService;
        this.metaMapaApiService = metaMapaApiService;
    }

    @GetMapping
    public String listarColecciones(Model model, HttpSession session) {
        model.addAttribute("titulo", "Colecciones");
        // TODO: agregar los atributos que necesitemos en el model despues de adaptarlo con thymeleaf

        String accessToken =  (String) session.getAttribute("accessToken");
        if (accessToken != null) {
            model.addAttribute("usuario", session.getAttribute("username"));
        }
        try {
            List<ColeccionDTO> colecciones = metaMapaApiService.obtenerColecciones();

            model.addAttribute("colecciones", colecciones);
        }
        catch (Exception e) {
            model.addAttribute("No se pudieron cargar las colecciones: ", e.getMessage());
        }

        return "colecciones/colecciones";
    }

    @GetMapping("/{id}")
    public String coleccionPorId(Model model, @PathVariable Long id,HttpSession session) {

        model.addAttribute("titulo", "Titulo de la coleccion");

        try {



            ColeccionDTO coleccion = metaMapaApiService.obtenerColeccionPorId(id);

            model.addAttribute("hechos", coleccion.getHechos());
            model.addAttribute("titulo", coleccion.getTitulo());
            model.addAttribute("descripcion", coleccion.getDescripcion());

            return "colecciones/coleccion_detalle";
        }
        catch (NotFoundException ex) {
            //redirectAttributes.addFlashAttribute("mensaje", ex.getMessage());
            return "redirect:/error";
        }
    }
}
