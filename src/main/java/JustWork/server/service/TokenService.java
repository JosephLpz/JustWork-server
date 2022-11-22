package JustWork.server.service;

import JustWork.server.config.JwtUtils;
import JustWork.server.models.Token;
import JustWork.server.models.Usuario;
import JustWork.server.repository.TokenRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.token.Sha512DigestUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
@Slf4j
public class TokenService {
    @Autowired
    TokenRepository tokenRepository;
    @Autowired
    JwtUtils jwtTokenUtil;

    public TokenService() {
        super();
    }

    @Transactional
    public void signAndSave(Token token) {
        log.debug("vamos a guardar firma de id:"+token.getId());
        if (token.getId()<1){
            tokenRepository.save(token);
        }
        token.setFirma(token.constructSignature());
        tokenRepository.save(token);
        tokenRepository.flush();
    }

    @Transactional
    public Token getFromShaToken(String requestTokenHeader) {
        Token byShaToken = tokenRepository.findByShaToken(requestTokenHeader);
        log.debug("[getFromShaToken] byShaToken:"+byShaToken);
        return byShaToken;
    }

    @Transactional
    public String crear(Usuario usuario){
        String nombreMetodo = Thread.currentThread().getStackTrace()[1].getMethodName();
        log.debug("["+nombreMetodo+"] Preparamos token para usuario: " + usuario.getEmail());
        var expirationDate = new Date(new Date().getTime()+86400000L*365);
        Map<String, Object> claims = new HashMap<>();
        final String jwtToken = jwtTokenUtil.doGenerateToken(claims, String.valueOf(usuario.getUuid()), expirationDate);
        var token = new Token();
        setupTokenCrear(token, usuario,expirationDate,jwtToken);
        signAndSave(token);
        return "Bearer " + jwtToken;
    }

    public void setupTokenCrear(Token token, Usuario usuario, Date expirationDate, String jwtToken){
        token.setUsuario(usuario);
        token.setFechaCreacion(LocalDateTime.now());
        token.setFechaExpiracion(LocalDateTime.ofInstant(expirationDate.toInstant(), ZoneId.systemDefault()));
        token.setShaToken(Sha512DigestUtils.shaHex(jwtToken));
        ;
    }

    public void deleteFromUsuario(Usuario usuario) {
        String nombreMetodo = Thread.currentThread().getStackTrace()[1].getMethodName();
        String cabecera = "["+nombreMetodo+"] ";
        List<Token> listado = getFromUsuario(usuario);
        log.info(cabecera + "Eliminamos un total de {} token(s): ", listado.size());
        if (listado.isEmpty()) return;
        tokenRepository.deleteAll(listado);
    }

    public List<Token> getFromUsuario(Usuario usuario){
        return tokenRepository.findByUsuario(usuario);
    }

}
