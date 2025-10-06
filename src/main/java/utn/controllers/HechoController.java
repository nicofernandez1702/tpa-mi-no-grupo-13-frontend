package utn.controllers;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/hechos")
public class HechoController {

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
}
