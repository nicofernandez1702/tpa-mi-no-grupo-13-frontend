package utn.services;

import org.springframework.stereotype.Service;
import utn.models.dto.ColeccionDTO;
import utn.repositories.ColeccionesRepository;

import javax.swing.text.html.Option;
import java.util.List;
import java.util.Optional;

@Service
public class ColeccionService {
    private ColeccionesRepository coleccionesRepository;

    public List<ColeccionDTO> obtenerTodasLasColecciones() {
        return coleccionesRepository.findAll();
    }

    public Optional<ColeccionDTO> obtenerColeccionPorId(String id) {
        Optional<ColeccionDTO> coleccion = coleccionesRepository.findById(id);

        return coleccion;
    }

}
