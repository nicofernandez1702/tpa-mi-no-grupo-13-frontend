package utn.controllers;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import utn.services.HechoService;

@Controller
@RequestMapping("/hechos")
public class HechoController {
    private HechoService hechoService;

    @GetMapping
    public String listarHechos(Model model) {
        model.addAttribute("titulo", "Hechos");

        return "hechos/hechos";
    }

    @GetMapping("/agregarHecho")
    public String agregarHecho(Model model){
        model.addAttribute("titulo", "Agregar Hecho");

        return "hechos/nuevo_hecho";
    }

    @GetMapping("/{id}")
    public String hechoPorId(Model model, @PathVariable String id){
        model.addAttribute("titulo", "Titulo del Hecho");

        return "hechos/hecho_detalle";
    }
}
