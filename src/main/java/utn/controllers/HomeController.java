package utn.controllers;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class HomeController {

    @GetMapping("/")
    public String home(Model model) {
        model.addAttribute("titulo", "Inicio");

        return "index";
    }

    @GetMapping("/home")
    public String home(){
        return "redirect:/";
    }

    @GetMapping("/error")
    public String notFound() {
        return "error";
    }
}
