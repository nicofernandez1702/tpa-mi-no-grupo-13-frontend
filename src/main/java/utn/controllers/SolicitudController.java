package utn.controllers;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import utn.models.dto.HechoFormDTO;
import utn.models.dto.SolicitudDTO;
import utn.services.MetaMapaApiService;

@Controller
@RequestMapping("/solicitudes")
public class SolicitudController {

    private MetaMapaApiService metaMapaApiService;

    public SolicitudController(MetaMapaApiService metaMapaApiService) {
        this.metaMapaApiService = metaMapaApiService;
    }

    @PostMapping
    public String crearSolicitud(@ModelAttribute SolicitudDTO solicitudDTO, RedirectAttributes redirectAttrs) {
        try {
            metaMapaApiService.crearSolicitud(solicitudDTO);
            redirectAttrs.addFlashAttribute("exito", "La solicitud se envió correctamente.");
        } catch (Exception e) {
            redirectAttrs.addFlashAttribute("error", "Error al enviar la solicitud: " + e.getMessage());
        }

        // Redirigir al detalle del hecho
        return "redirect:/hechos/" + solicitudDTO.getHecho();
    }


}
