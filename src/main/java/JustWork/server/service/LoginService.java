package JustWork.server.service;

import JustWork.server.dto.LoginRequestDTO;
import JustWork.server.models.Usuario;
import JustWork.server.repository.UsuarioRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.client.HttpClientErrorException;

@Slf4j
@Service
public class LoginService {


    @Autowired
    private UsuarioRepository usuarioRepository;

    public String login(LoginRequestDTO loginRequestDTO){
        //Usuario usuario = usuarioRepository.findByEmailAndEliminadoIsFalse(loginRequestDTO.getEmail());
        //if(usuario==null) throw new HttpClientErrorException.NotFound("",404,null,null,null);
    return "";
    }
}
