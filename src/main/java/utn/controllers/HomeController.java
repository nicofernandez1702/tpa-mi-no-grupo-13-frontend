package utn.controllers;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class HomeController {

    @GetMapping("/")
    public String home() {
        return "redirect:/home";
    }

    @GetMapping("/404")
    public String notFound() {
        return "404";
    }
}
