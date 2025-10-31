package utn.controllers;

import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import utn.models.dto.ColeccionDTO;
import utn.services.MetaMapaApiService;

import java.util.List;

@Controller
@RequiredArgsConstructor
public class AdminController {

    private final MetaMapaApiService metaMapaApiService;

    @GetMapping("/panel-control")
    @PreAuthorize("hasRole('ADMINISTRADOR')")
    public String panelControl(Model model, HttpSession session) {

        List<ColeccionDTO> colecciones = metaMapaApiService.obtenerColecciones();

        model.addAttribute("titulo", "Panel de Control");
        model.addAttribute("usuario", session.getAttribute("username"));
        model.addAttribute("coleccionesDestacadas", colecciones);
        return "admin/control_panel";
    }


}
