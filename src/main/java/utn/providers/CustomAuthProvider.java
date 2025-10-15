package utn.providers;


import utn.models.dto.AuthResponseDTO;
import utn.models.dto.RolesPermisosDTO;
import utn.services.MetaMapaApiService;

import jakarta.servlet.http.HttpServletRequest;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import java.util.ArrayList;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import java.util.List;


@Component
public class CustomAuthProvider implements AuthenticationProvider {

    private static final Logger log = LoggerFactory.getLogger(CustomAuthProvider.class);
    private final MetaMapaApiService externalAuthService;

    public CustomAuthProvider(MetaMapaApiService externalAuthService) {
        this.externalAuthService = externalAuthService;
    }

    @Override
    public Authentication authenticate(Authentication authentication) throws AuthenticationException {
        String username = authentication.getName();
        String password = authentication.getCredentials().toString();

        log.info("Intentando autenticar usuario '{}'", username);

        try {
            // 🔹 1. Llamada al servicio externo
            AuthResponseDTO authResponse = externalAuthService.login(username, password);

            if (authResponse == null) {
                log.warn("El servicio de autenticación devolvió null para el usuario '{}'", username);
                throw new BadCredentialsException("Usuario o contraseña inválidos");
            }

            log.info("Login exitoso en servicio externo para '{}'", username);
            log.debug("Access Token recibido: {}", authResponse.getAccessToken());
            log.debug("Refresh Token recibido: {}", authResponse.getRefreshToken());

            // 🔹 2. Guardar en sesión
            ServletRequestAttributes attributes = (ServletRequestAttributes) RequestContextHolder.currentRequestAttributes();
            HttpServletRequest request = attributes.getRequest();

            request.getSession().setAttribute("accessToken", authResponse.getAccessToken());
            request.getSession().setAttribute("refreshToken", authResponse.getRefreshToken());
            request.getSession().setAttribute("username", username);

            // 🔹 3. Obtener roles y permisos
            log.info("Buscando roles y permisos para '{}'", username);
            RolesPermisosDTO rolesPermisos = externalAuthService.getRolesPermisos(authResponse.getAccessToken());

            if (rolesPermisos == null) {
                log.warn("No se obtuvieron roles ni permisos para '{}'", username);
            } else {
                log.info("Rol asignado: {}", rolesPermisos.getRol());
                log.info("Permisos: {}", rolesPermisos.getPermisos());
            }

            // 🔹 4. Cargar en sesión
            request.getSession().setAttribute("rol", rolesPermisos.getRol());
            request.getSession().setAttribute("permisos", rolesPermisos.getPermisos());

            List<GrantedAuthority> authorities = new ArrayList<>();
            if (rolesPermisos != null && rolesPermisos.getPermisos() != null) {
                rolesPermisos.getPermisos().forEach(permiso ->
                        authorities.add(new SimpleGrantedAuthority(permiso.name())));
            }
            if (rolesPermisos != null && rolesPermisos.getRol() != null) {
                authorities.add(new SimpleGrantedAuthority("ROLE_" + rolesPermisos.getRol().name()));
            }

            log.info("Autenticación completa para '{}'", username);
            return new UsernamePasswordAuthenticationToken(username, password, authorities);

        } catch (BadCredentialsException e) {
            log.warn("Credenciales inválidas para '{}': {}", username, e.getMessage());
            throw e;
        } catch (RuntimeException e) {
            log.error("Error inesperado al autenticar '{}': {}", username, e.getMessage());
            throw new BadCredentialsException("Error en el sistema de autenticación: " + e.getMessage(), e);
        }
    }

    @Override
    public boolean supports(Class<?> authentication) {
        return authentication.equals(UsernamePasswordAuthenticationToken.class);
    }
}
