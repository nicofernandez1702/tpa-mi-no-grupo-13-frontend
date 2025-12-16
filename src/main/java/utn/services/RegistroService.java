package utn.services;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import utn.models.dto.RegistroUsuarioDTO;
import utn.services.internal.WebApiCallerService;

@Service
public class RegistroService {

    private final WebApiCallerService webApiCaller;
    private final String authServiceUrl;

    public RegistroService(
            WebApiCallerService webApiCaller,
            @Value("${auth.service.url}") String authServiceUrl
    ) {
        this.webApiCaller = webApiCaller;
        this.authServiceUrl = authServiceUrl;
    }

    public void registrar(RegistroUsuarioDTO dto) {


        webApiCaller.postWithoutToken(
                authServiceUrl + "/auth/registro",
                dto,
                Void.class
        );
    }
}