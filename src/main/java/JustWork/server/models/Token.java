package JustWork.server.models;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.token.Sha512DigestUtils;

import javax.persistence.*;
import java.time.LocalDateTime;

@Slf4j
@Entity
@Getter
@Setter
@ToString
@RequiredArgsConstructor
@Table(name = "token")
public class Token {

    @Id
    @GeneratedValue(strategy= GenerationType.IDENTITY)
    private long id;

    private LocalDateTime fechaCreacion;

    private LocalDateTime fechaExpiracion;

    private String shaToken;

    private String ssoAccessToken;

    private LocalDateTime ssoFechaExpiracion;

    @ManyToOne(cascade = CascadeType.DETACH, fetch = FetchType.EAGER)
    private Usuario usuario;

    private String ssoRefreshToken;

    private String firma;

    @JsonIgnore
    public String constructSignature() {
        return Sha512DigestUtils.shaHex(id+"_"+shaToken+"_"+(usuario!=null?usuario.getId():"null"));
    }

    public boolean isVigente() {
        if (fechaExpiracion==null) return false;
        return fechaExpiracion.isAfter(LocalDateTime.now());
    }

}
