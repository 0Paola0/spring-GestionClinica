package sv.edu.udb.repository;
import org.springframework.data.jpa.repository.JpaRepository;
import sv.edu.udb.model.Paciente;
import org.springframework.stereotype.Repository;

@Repository
public interface PacienteRepository extends JpaRepository<Paciente, Long>{
    boolean existsByCodigo(String codigo);
}
