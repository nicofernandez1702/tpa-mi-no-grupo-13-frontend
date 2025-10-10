package utn.repositories;

import org.springframework.stereotype.Repository;
import utn.models.dto.ColeccionDTO;
import utn.models.dto.HechoDTO;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

@Repository
public class ColeccionesRepository {

    private List<ColeccionDTO> colecciones;

    public ColeccionesRepository() {
        this.colecciones = new ArrayList<>();

        List<HechoDTO> hechosColeccion1 = Arrays.asList(
                HechoDTO.builder()
                        .id("1")
                        .titulo("Hecho de prueba col1")
                        .descripcion("descripcion 1")
                        .categoria("Pruebas")
                        .latitud(-54.054)
                        .longitud(-60.813)
                        .fecha_hecho(LocalDateTime.now())
                        .created_at(LocalDateTime.now())
                        .updated_at(LocalDateTime.now()).build(),
                HechoDTO.builder()
                .id("2")
                .titulo("Hecho de prueba col1")
                .descripcion("descipcion 2")
                .categoria("Pruebas")
                .latitud(-53.8)
                .longitud(-63.54)
                .fecha_hecho(LocalDateTime.now())
                .created_at(LocalDateTime.now())
                .updated_at(LocalDateTime.now()).build());

        List<ColeccionDTO> coleccionesPrueba = Arrays.asList(
                ColeccionDTO.builder()
                        .id("1")
                        .titulo("Manifestaciones")
                        .descripcion("Movilizaciones sociales")
                        .hechos(hechosColeccion1).build(),
                ColeccionDTO.builder()
                        .id("2")
                        .titulo("Vientos y huracanes")
                        .descripcion("Vientos fuertes en el país")
                        .hechos(null).build(),
                ColeccionDTO.builder()
                        .id("3")
                        .titulo("Inundaciones")
                        .descripcion("Inundaciones en la república Argentina")
                        .hechos(hechosColeccion1).build()
                );
        this.colecciones = coleccionesPrueba;
    }

    public List<ColeccionDTO> findAll(){
        return this.colecciones;
    }

    public Optional<ColeccionDTO> findById(String id){
        return colecciones.stream().filter(coleccion -> coleccion.getId().equals(id)).findFirst();
    }
}
