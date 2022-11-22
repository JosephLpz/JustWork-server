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
import java.util.HashSet;
import java.util.Set;
import java.util.UUID;

@Slf4j
@Entity
@Getter
@Setter
@ToString
@RequiredArgsConstructor
@Table(name = "usuario")
public class Usuario {

    @Id
    @GeneratedValue(strategy= GenerationType.IDENTITY)
    private long id;

    @Column(name = "uuid", columnDefinition = "uuid not null unique", nullable = false)
    private UUID uuid;

    private String rut;

    private String nombre;

    private String email;

    private String apellido;

    private String telefono;

    private String tipo;

    @Column(name = "eliminado", columnDefinition = "bool")
    private boolean eliminado;

    private LocalDateTime fechaCreacion;

    private LocalDateTime fechaActualizacion;

    private String passwordSha512;

    private String firma;

    @ManyToMany(fetch = FetchType.LAZY)
    @JoinTable(name = "usuario_roles",
            joinColumns = @JoinColumn(name = "usuario_id"),
            inverseJoinColumns = @JoinColumn(name = "rol_id"))
    private Set<Rol> roles = new HashSet<>();

    @Version
    private int version;

    @JsonIgnore
    public String constructSignature() {
        return Sha512DigestUtils.shaHex(id+"_"+uuid.toString()+"_"+passwordSha512+"_"+rut+"_"+email+"_"+eliminado);
    }

}
