package utn.controllers;

import jakarta.servlet.http.HttpSession;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import utn.exceptions.NotFoundException;
import utn.models.dto.ColeccionDTO;
import utn.models.dto.HechoDTO;
import utn.models.entities.usuarios.Rol;
import utn.services.MetaMapaApiService;

import java.util.List;
import java.util.Objects;

@Controller
@RequestMapping("/colecciones")
public class ColeccionController {
    private final MetaMapaApiService metaMapaApiService;

    public ColeccionController(MetaMapaApiService metaMapaApiService) {
        this.metaMapaApiService = metaMapaApiService;
    }

    @GetMapping
    public String listarColecciones(Model model, HttpSession session) {
        model.addAttribute("titulo", "Colecciones");
        // TODO: agregar los atributos que necesitemos en el model despues de adaptarlo con thymeleaf

        try {
            List<ColeccionDTO> colecciones = metaMapaApiService.obtenerColecciones();

            model.addAttribute("colecciones", colecciones);
        }
        catch (Exception e) {
            model.addAttribute("No se pudieron cargar las colecciones: ", e.getMessage());
        }

        String accessToken =  (String) session.getAttribute("accessToken");
        if (accessToken != null) {
            model.addAttribute("usuario", session.getAttribute("username"));

            Rol rol = (Rol) session.getAttribute("rol");
            if (rol == Rol.ADMINISTRADOR) {

                // TODO: Tal vez esto lo cambiaría por un redirect y haría otro endpoint para colecciones_admin
                return "admin/colecciones_admin";
            }
        }

        return "colecciones/colecciones";
    }

    @GetMapping("/{id}")
    public String coleccionPorId(Model model, @PathVariable Long id,HttpSession session) {

        model.addAttribute("titulo", "Titulo de la coleccion");

        String accessToken =  (String) session.getAttribute("accessToken");
        if (accessToken != null) {
            model.addAttribute("usuario", session.getAttribute("username"));
        }
        try {

            ColeccionDTO coleccion = metaMapaApiService.obtenerColeccionPorId(id);

            // Obtener categorías únicas
            List<String> categorias = coleccion.getHechos().stream()
                    .map(HechoDTO::getCategoria)
                    .filter(Objects::nonNull)
                    .distinct()
                    .sorted()
                    .toList();

            System.out.println(categorias);

            System.out.println("Hechos: " + coleccion.getHechos().size());

            model.addAttribute("categorias", categorias);
            model.addAttribute("coleccion", coleccion);
            model.addAttribute("hechos", coleccion.getHechos());
            model.addAttribute("titulo", coleccion.getTitulo());
            model.addAttribute("descripcion", coleccion.getDescripcion());
            model.addAttribute("contenido", "colecciones/coleccion_detalle");

            System.out.println(coleccion.getId());

            return "layout/base";
        }
        catch (NotFoundException ex) {
            //redirectAttributes.addFlashAttribute("mensaje", ex.getMessage());
            return "redirect:/error";
        }
    }

    @GetMapping("/nueva")
    public String nuevaColeccion(Model model, HttpSession session) {

        model.addAttribute("titulo", "Crear nueva colección");

        String accessToken = (String) session.getAttribute("accessToken");
        if (accessToken != null) {
            model.addAttribute("usuario", session.getAttribute("username"));
        }

        try {
            // Traemos las listas desde el back
            List<String> fuentes = metaMapaApiService.getFuentes();
            List<String> algoritmos = metaMapaApiService.getAlgoritmos();

            System.out.println("Hechos: " + fuentes.size());
            System.out.println("Algoritmos: " + algoritmos.size());

            // Las agregamos al modelo para que se usen en los dropdowns
            model.addAttribute("fuentes", fuentes);
            model.addAttribute("algoritmos", algoritmos);

            // También podés agregar un DTO vacío para vincular el form
            model.addAttribute("coleccionForm", new ColeccionDTO());

            return "admin/nueva_coleccion";

        } catch (NotFoundException ex) {
            model.addAttribute("error", "No se pudieron cargar los datos: " + ex.getMessage());
            return "redirect:/error";
        }
    }

    @PostMapping("/nueva")
    public String crearColeccion(
            @ModelAttribute("coleccionForm") ColeccionDTO dto,
            HttpSession session) {

        String accessToken = (String) session.getAttribute("accessToken");

        metaMapaApiService.crearColeccion(accessToken,dto);

        return "redirect:/colecciones";
    }

    @GetMapping("/{id}/editar")
    public String editarColeccion(Model model, @PathVariable Long id, HttpSession session) {
        String accessToken =  (String) session.getAttribute("accessToken");
        if (accessToken != null) {
            model.addAttribute("usuario", session.getAttribute("username"));

            try {
                ColeccionDTO coleccion = metaMapaApiService.obtenerColeccionPorId(id);

                model.addAttribute("coleccion", coleccion);
                model.addAttribute("titulo", coleccion.getTitulo());
                model.addAttribute("descripcion", coleccion.getDescripcion());

                return "colecciones/coleccion_editar";
            }
            catch (NotFoundException ex) {
                return "redirect:/error";
            }
        }
        else {
            return "redirect:/error";
        }

    }
}
