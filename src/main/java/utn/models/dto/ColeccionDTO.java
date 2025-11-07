package utn.models.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ColeccionDTO {
    private String id;            // ID de la colección
    private String titulo;      // Título de la colección
    private String descripcion;
    private List<String> fuentes;  // seleccionadas por el usuario
    private String algoritmo;
    private List<HechoDTO> hechos;  // Lista de hechos en formato HechoDTO
}
