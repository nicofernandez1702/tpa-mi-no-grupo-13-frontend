package utn.services;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.*;

@Service
public class UploadService {

    @Value("${app.upload.dir}")
    private String uploadDir;

    public String guardarArchivo(MultipartFile archivo) throws IOException {
        // Crear la carpeta si no existe
        Path directorio = Paths.get(uploadDir).toAbsolutePath().normalize();
        Files.createDirectories(directorio);

        // Nombre limpio y único
        String nombreArchivo = System.currentTimeMillis() + "_" + archivo.getOriginalFilename();
        Path destino = directorio.resolve(nombreArchivo);

        // Guardar el archivo físicamente
        Files.copy(archivo.getInputStream(), destino, StandardCopyOption.REPLACE_EXISTING);

        // Devolver la URL pública
        return "/uploads/" + nombreArchivo;
    }
}
