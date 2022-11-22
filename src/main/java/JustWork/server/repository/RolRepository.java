package JustWork.server.repository;

import JustWork.server.models.Rol;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface RolRepository extends JpaRepository<Long, Rol> {

    Rol findByNombre(String nombre);
}