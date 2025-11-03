package utn.models.dto;

import lombok.Builder;
import lombok.Data;

import java.time.LocalDateTime;

@Data
@Builder
public class SolicitudDTO {
    private LocalDateTime fechaCreacion;
    private String estado;

    // TODO consideraría que el hecho venga como objeto con sus atributos, ya que en la pantalla de solicitudes son necesarios para el modal de "Ver más"
    private String hecho;

    private final String motivo;

    // TODO los datos debajo de esta línea no están en el DTO en el back, hay que agregarlos
    private String solicitante;

}
