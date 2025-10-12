package utn.repositories;

import org.springframework.stereotype.Repository;
import utn.models.dto.HechoDTO;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

@Repository
public class HechosRepository {

    private List<HechoDTO> hechos;

    public HechosRepository(){
        List<HechoDTO> hechosPrueba = Arrays.asList(
                HechoDTO.builder()
                        .id("1")
                        .titulo("Hecho de prueba 1")
                        .descripcion("descripcion 1")
                        .categoria("Pruebas")
                        .latitud(-54.054)
                        .longitud(-60.813)
                        .fecha_hecho(LocalDateTime.now())
                        .created_at(LocalDateTime.now())
                        .updated_at(LocalDateTime.now()).build(),
                HechoDTO.builder()
                        .id("2")
                        .titulo("Hecho de prueba 2")
                        .descripcion("descipcion 2")
                        .categoria("Pruebas")
                        .latitud(-53.8)
                        .longitud(-63.54)
                        .fecha_hecho(LocalDateTime.now())
                        .created_at(LocalDateTime.now())
                        .updated_at(LocalDateTime.now()).build());
        this.hechos = hechosPrueba;
    }
    // TODO
    public List<HechoDTO> findAll(){
        return hechos;
    }

    //TODO
    public Optional<HechoDTO> findById(String id){
        return hechos.stream().filter(h -> h.getId().equals(id)).findFirst();
    }
}
