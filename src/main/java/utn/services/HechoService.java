package utn.services;

import org.springframework.stereotype.Service;
import utn.exceptions.NotFoundException;
import utn.models.dto.HechoDTO;
import utn.repositories.HechosRepository;

import java.util.List;
import java.util.Optional;

@Service
public class HechoService {

    private HechosRepository hechosRepository;

    public HechoService(HechosRepository hechosRepository) {
        this.hechosRepository = hechosRepository;
    }

    public List<HechoDTO> obtenerTodosLosHechos(){
        return hechosRepository.findAll();
    }

    public Optional<HechoDTO> obtenerHechoPorId(String idHecho){
        HechoDTO hecho = intentarRecuperarHecho(idHecho);

        return Optional.of(hecho);
    }

    public HechoDTO intentarRecuperarHecho(String idHecho){
        Optional<HechoDTO> hecho = hechosRepository.findById(idHecho);
        if(hecho.isEmpty()){
            throw new NotFoundException("Hecho");
        }
        return hecho.get();
    }
}
