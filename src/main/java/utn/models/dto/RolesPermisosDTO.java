package utn.models.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import utn.models.entities.usuarios.Permiso;
import utn.models.entities.usuarios.Rol;

import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class RolesPermisosDTO {
    private String username;
    private Rol rol;
    private List<Permiso> permisos;
}
