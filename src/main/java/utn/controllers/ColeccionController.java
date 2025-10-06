package utn.controllers;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/colecciones")
public class ColeccionController {

    @GetMapping
    public String listarColecciones(Model model) {
        model.addAttribute("titulo", "Colecciones");
        // TODO: agregar los atributos que necesitemos en el model despues de adaptarlo con thymeleaf

        return "colecciones/colecciones";
    }
}
