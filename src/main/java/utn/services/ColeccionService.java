package utn.services;

import org.springframework.stereotype.Service;
import utn.models.dto.ColeccionDTO;
import utn.repositories.ColeccionesRepository;

import java.util.List;

@Service
public class ColeccionService {
    private ColeccionesRepository coleccionesRepository;

    public List<ColeccionDTO> obtenerTodasLasColecciones() {
        return coleccionesRepository.findAll();
    }

}
