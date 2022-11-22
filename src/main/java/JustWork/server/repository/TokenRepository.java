package JustWork.server.repository;

import JustWork.server.models.Token;
import JustWork.server.models.Usuario;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface TokenRepository extends JpaRepository<Token, Long> {
    Token findByShaToken(String shaToken);
    List<Token> findByUsuario(Usuario usuario);
}
