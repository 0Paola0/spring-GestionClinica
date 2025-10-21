package sv.edu.udb.repository;
import org.springframework.data.jpa.repository.JpaRepository;
import sv.edu.udb.model.Usuario;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UsuarioRepository extends JpaRepository<Usuario, Long>{
    boolean existsByNombreUsuario(String nombreUsuario);
    boolean existsByCorreoElectronico(String correoElectronico);
    Optional<Usuario> findByNombreUsuario(String nombreUsuario);
}
