package utn.controllers;

import jakarta.servlet.http.HttpSession;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import utn.exceptions.NotFoundException;
import utn.models.dto.ColeccionDTO;
import utn.models.dto.HechoDTO;
import utn.models.dto.HechoFormDTO;
import utn.models.entities.Categoria;
import utn.models.entities.usuarios.Usuario;
import utn.services.MetaMapaApiService;

import java.util.List;
import java.util.Objects;

@Controller
@RequestMapping("/hechos")
public class HechoController {
    private MetaMapaApiService metaMapaApiService;

    public HechoController(MetaMapaApiService metaMapaApiService) {
        this.metaMapaApiService = metaMapaApiService;
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
        try {
            List<HechoDTO> hechos = metaMapaApiService.obtenerTodosLosHechos();

            model.addAttribute("categorias", Categoria.values());
        }
        catch (Exception e) {
            model.addAttribute("No se pudieron cargar los hechos: ", e.getMessage());
        }

        return "hechos/nuevo_hecho";
    }

    @PostMapping("/nuevo")
    public String crearHecho(@ModelAttribute HechoFormDTO hechoFormDTO, RedirectAttributes redirectAttributes) {
        try {
            metaMapaApiService.crearHecho(hechoFormDTO);
            redirectAttributes.addFlashAttribute("exito", "Hecho creado correctamente");
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("error", "Error al crear el hecho: " + e.getMessage());
        }
        return "redirect:/hechos/agregarHecho";
    }

    @GetMapping("/{id}")
    public String hechoPorId(
            Model model,
            @PathVariable String id,
            @RequestParam(required = false) String from,
            @RequestParam(required = false) String coleccionId,
            HttpSession session) {

        String accessToken = (String) session.getAttribute("accessToken");
        if (accessToken != null) {
            model.addAttribute("usuario", session.getAttribute("username"));
        }

        try {
            HechoDTO hecho = metaMapaApiService.obtenerHechoPorId(Long.valueOf(id));
            model.addAttribute("hecho", hecho);
            model.addAttribute("titulo", hecho.getTitulo());

            // 👉 CONTEXTO DE NAVEGACIÓN
            if ("coleccion".equals(from) && coleccionId != null) {
                ColeccionDTO coleccion = metaMapaApiService.obtenerColeccionPorId(Long.valueOf(coleccionId));
                model.addAttribute("breadcrumbLabel", coleccion.getTitulo());
                model.addAttribute("breadcrumbUrl", "/colecciones/" + coleccionId);
            } else {
                model.addAttribute("breadcrumbLabel", "Mapa de hechos");
                model.addAttribute("breadcrumbUrl", "/hechos");
            }

            model.addAttribute("contenido", "hechos/hecho_detalle");

            System.out.println("from = " + from);
            System.out.println("coleccionId = " + coleccionId);


            return "layout/base";
        }
        catch (NotFoundException e) {
            return "redirect:/error";
        }
    }

    @GetMapping("/{id}/editar")
    public String editarHecho(Model model, @PathVariable Long id, HttpSession session) {

        String accessToken =  (String) session.getAttribute("accessToken");
        if (accessToken != null) {
            model.addAttribute("usuario", session.getAttribute("username"));
        }

        try {
            HechoDTO hecho = metaMapaApiService.obtenerHechoPorId(id);
            model.addAttribute("hecho", hecho);
            model.addAttribute("titulo", hecho.getTitulo() + " - Editar");
            model.addAttribute("ubicacion", "HACER FUNCION hecho.getUbicacion()");

            return "hechos/hecho_editar";
        }
        catch (NotFoundException e) {
            return "redirect:/error";
        }
    }

    @GetMapping("/user/{usuario}")
    public String misHechos(Model model, @PathVariable String usuario, HttpSession session){
        model.addAttribute("titulo", "Mis hechos");

        String accessToken =  (String) session.getAttribute("accessToken");
        if (accessToken != null) {
            model.addAttribute("usuario", session.getAttribute("username"));

            // TODO Función del service que retorne los hechos del usuario logeado

        }

        return "hechos/hechos_contribuyente";
    }
}
