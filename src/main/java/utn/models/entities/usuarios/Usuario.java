package utn.models.entities.usuarios;

import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
public class Usuario {
    private Long id;
    private String nombre;
    private String apellido;
    private String nombreDeUsuario;
    private String contrasenia;
    private Rol rol;
    private List<Permiso> permisos;
}
