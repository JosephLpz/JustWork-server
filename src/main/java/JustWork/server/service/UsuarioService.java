package JustWork.server.service;

import JustWork.server.models.Usuario;
import JustWork.server.repository.UsuarioRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.token.Sha512DigestUtils;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Service
@Slf4j
public class UsuarioService implements UserDetailsService {

    @Autowired
    UsuarioRepository usuarioRepository;


    private static final String ERROR_USUARIO = "Usuario";
    private static final String ACCESO_DENEGADO = "acceso denegado";

    public void firmar(Usuario usuario){
        String nombreMetodo = Thread.currentThread().getStackTrace()[1].getMethodName();
        String cabecera = "["+ nombreMetodo +"] ";
        log.debug(cabecera + "Firmamos Usuario: " + usuario.getEmail());
        usuario.setFirma(usuario.constructSignature());
        guardar(usuario);
    }

    public void guardar(Usuario usuario) {
        String nombreMetodo = Thread.currentThread().getStackTrace()[1].getMethodName();
        String cabecera = "["+ nombreMetodo +"] ";
        log.debug(cabecera + "Guardamos Usuario: " + usuario.getEmail());
        usuarioRepository.save(usuario);
    }

    public Usuario getFromAuthentication(Authentication authentication) {
        if (authentication==null) throw new UsernameNotFoundException(ERROR_USUARIO);
        return getFromUuid(UUID.fromString(authentication.getName()));
    }

    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        Usuario usuario = getFromEmail(email);
        return new User(usuario.getRut(), "$2a$10$slYQmyNdGzTn7ZLBXBChFOC9f6kFjAqPhccnP6DxlWXx2lPk1C3G6", new ArrayList<>());
    }

    public Usuario getFromEmail(String email){
        return usuarioRepository.findByEmailAndEliminadoIsFalse(email).orElse(null);
    }

    public Usuario getFromUuid(UUID uuid){
        return usuarioRepository.findByUuidAndEliminadoFalse(uuid).orElseThrow(() -> new UsernameNotFoundException(ERROR_USUARIO));
    }

    public void olvidoContraseña(String correo) {
        log.debug("[olvidoContraseña] iniciando...");
    }

    /*public void cambiarContraseña(CambiarContraseñaRequestDTO cambiarContraseñaRequestDTO){
        Usuario usuario = this.getFromEmail(cambiarContraseñaRequestDTO.getCorreo());
        usuario.setPasswordSha512(Sha512DigestUtils.shaHex(cambiarContraseñaRequestDTO.getPassword()));
        firmar(usuario);
    }*/
}
