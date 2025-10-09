package utn.repositories;

import org.springframework.stereotype.Repository;
import utn.models.dto.HechoDTO;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Repository
public class HechosRepository {

    public HechosRepository(){

    }
    // TODO
    public List<HechoDTO> findAll(){
        return new ArrayList<>();
    }

    //TODO
    public Optional<HechoDTO> findById(Long id){
        return null;
    }
}
