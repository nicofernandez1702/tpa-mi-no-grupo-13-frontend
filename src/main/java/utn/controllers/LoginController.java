package utn.controllers;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import utn.models.dto.RegistroUsuarioDTO;
import utn.services.RegistroService;

@Controller
public class LoginController {

    private final RegistroService registroService;

    public LoginController(RegistroService registroService) {
        this.registroService = registroService;
    }

    @GetMapping("/login")
    public String login(){

        return "login";
    }

    @GetMapping("/registro")
    public String registro(){

        return "registro";
    }

    @PostMapping("/registro")
    public String registrar(@ModelAttribute RegistroUsuarioDTO dto) {

        registroService.registrar(dto);

        return "redirect:/login";
    }

}
