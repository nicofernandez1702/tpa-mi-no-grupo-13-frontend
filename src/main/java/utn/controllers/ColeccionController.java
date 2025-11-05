package utn.controllers;

import jakarta.servlet.http.HttpSession;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import utn.exceptions.NotFoundException;
import utn.models.dto.ColeccionDTO;
import utn.models.entities.usuarios.Rol;
import utn.services.MetaMapaApiService;

import java.util.List;

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

            System.out.println("Hechos: " + coleccion.getHechos().size());

            model.addAttribute("hechos", coleccion.getHechos());
            model.addAttribute("titulo", coleccion.getTitulo());
            model.addAttribute("descripcion", coleccion.getDescripcion());

            return "colecciones/coleccion_detalle";
        }
        catch (NotFoundException ex) {
            //redirectAttributes.addFlashAttribute("mensaje", ex.getMessage());
            return "redirect:/error";
        }
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
