package utn.controllers;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import utn.exceptions.NotFoundException;
import utn.models.dto.ColeccionDTO;
import utn.services.ColeccionService;

import java.util.List;

@Controller
@RequestMapping("/colecciones")
public class ColeccionController {
    private ColeccionService coleccionService;

    @GetMapping
    public String listarColecciones(Model model) {
        model.addAttribute("titulo", "Colecciones");
        // TODO: agregar los atributos que necesitemos en el model despues de adaptarlo con thymeleaf
        List<ColeccionDTO> colecciones = coleccionService.obtenerTodasLasColecciones();
        model.addAttribute("colecciones", colecciones);

        return "colecciones/colecciones";
    }

    @GetMapping("/{id}")
    public String coleccionPorId(Model model, @PathVariable String id) {

        model.addAttribute("titulo", "Titulo de la coleccion");

        try {
            ColeccionDTO coleccion = coleccionService.obtenerColeccionPorId(id).get();

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
