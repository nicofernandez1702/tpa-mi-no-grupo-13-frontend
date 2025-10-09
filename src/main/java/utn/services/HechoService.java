package utn.services;

import org.springframework.stereotype.Service;
import utn.models.dto.HechoDTO;
import utn.repositories.HechosRepository;

import java.util.List;

@Service
public class HechoService {

    private HechosRepository hechosRepository;

    public List<HechoDTO> obtenerTodosLosHechos(){
        return hechosRepository.findAll();
    }
}
