package JustWork.server.models;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import lombok.extern.slf4j.Slf4j;

import javax.persistence.*;
import java.util.UUID;

@Slf4j
@Entity
@Getter
@Setter
@ToString
@RequiredArgsConstructor
@Table(name = "rol")
public class Rol {

    @Id
    @GeneratedValue(strategy= GenerationType.IDENTITY)
    private long id;

    @Column(name = "uuid", columnDefinition = "uuid not null unique", nullable = false)
    private UUID uuid;

    @Enumerated(EnumType.STRING)
    private RolEnum nombre;

}
