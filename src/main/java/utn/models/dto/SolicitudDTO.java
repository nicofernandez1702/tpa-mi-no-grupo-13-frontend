package utn.models.dto;

import lombok.Builder;
import lombok.Data;

import java.time.LocalDateTime;

@Data
@Builder
public class SolicitudDTO {
    private LocalDateTime fechaCreacion;
    private String estado;

    private String hecho;

    private final String motivo;


}
