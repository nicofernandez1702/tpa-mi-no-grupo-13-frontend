package utn.controllers;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import utn.exceptions.NotFoundException;
import utn.models.dto.HechoDTO;
import utn.services.HechoService;

import java.util.List;

@Controller
@RequestMapping("/hechos")
public class HechoController {
    private HechoService hechoService;

    public HechoController(HechoService hechoService) {
        this.hechoService = hechoService;
    }

    @GetMapping
    public String listarHechos(Model model) {
        List<HechoDTO> hechos = hechoService.obtenerTodosLosHechos();
        model.addAttribute("titulo", "Hechos");
        model.addAttribute("hechos", hechos);

        return "hechos/hechos";
    }

    @GetMapping("/agregarHecho")
    public String agregarHecho(Model model){
        model.addAttribute("titulo", "Agregar Hecho");

        return "hechos/nuevo_hecho";
    }

    @GetMapping("/{id}")
    public String hechoPorId(Model model, @PathVariable String id){
        try {
            HechoDTO hecho = hechoService.obtenerHechoPorId(id).get();
            model.addAttribute("hecho", hecho);
            model.addAttribute("titulo", hecho.getTitulo());

            return "hechos/hecho_detalle";
        }
        catch (NotFoundException e) {
            return "redirect:/error";
        }
    }
}
