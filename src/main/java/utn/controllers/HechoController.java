package utn.controllers;

import jakarta.servlet.http.HttpSession;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import utn.exceptions.NotFoundException;
import utn.models.dto.HechoDTO;
import utn.models.entities.usuarios.Usuario;
import utn.services.HechoService;
import utn.services.MetaMapaApiService;

import java.util.List;

@Controller
@RequestMapping("/hechos")
public class HechoController {
    private HechoService hechoService;
    private MetaMapaApiService metaMapaApiService;

    public HechoController(MetaMapaApiService metaMapaApiService,  HechoService hechoService) {
        this.metaMapaApiService = metaMapaApiService;
        this.hechoService = hechoService;
    }

    @GetMapping
    public String listarHechos(Model model, HttpSession session) {
        model.addAttribute("titulo", "Hechos");

        String accessToken =  (String) session.getAttribute("accessToken");
        if (accessToken != null) {
            model.addAttribute("usuario", session.getAttribute("username"));
        }
        try {
            List<HechoDTO> hechos = metaMapaApiService.obtenerTodosLosHechos();

            model.addAttribute("hechos", hechos);
        }
        catch (Exception e) {
            model.addAttribute("No se pudieron cargar los hechos: ", e.getMessage());
        }

        return "hechos/hechos";
    }

    @GetMapping("/agregarHecho")
    public String agregarHecho(Model model, HttpSession session) {
        model.addAttribute("titulo", "Agregar Hecho");

        String accessToken =  (String) session.getAttribute("accessToken");
        if (accessToken != null) {
            model.addAttribute("usuario", session.getAttribute("username"));
        }

        return "hechos/nuevo_hecho";
    }

    @GetMapping("/{id}")
    public String hechoPorId(Model model, @PathVariable String id){
        try {
            HechoDTO hecho = hechoService.obtenerHechoPorId(id).get();
            model.addAttribute("hecho", hecho);
            model.addAttribute("titulo", hecho.getTitulo());
            model.addAttribute("ubicacion", "HACER FUNCION hecho.getUbicacion()");

            return "hechos/hecho_detalle";
        }
        catch (NotFoundException e) {
            return "redirect:/error";
        }
    }

    @GetMapping("/{id}/editar")
    public String editarHecho(Model model, @PathVariable String id){
        try {
            HechoDTO hecho = hechoService.obtenerHechoPorId(id).get();
            model.addAttribute("hecho", hecho);
            model.addAttribute("titulo", hecho.getTitulo() + " - Editar");
            model.addAttribute("ubicacion", "HACER FUNCION hecho.getUbicacion()");

            return "hechos/hecho_editar";
        }
        catch (NotFoundException e) {
            return "redirect:/error";
        }
    }

    @GetMapping("/misHechos")
    public String misHechos(Model model, HttpSession session){
        model.addAttribute("titulo", "Mis hechos");

        String accessToken =  (String) session.getAttribute("accessToken");
        if (accessToken != null) {
            model.addAttribute("usuario", session.getAttribute("username"));

            // TODO Función del service que retorne los hechos del usuario logeado

        }

        return "hechos/hechos_contribuyente";
    }
}
