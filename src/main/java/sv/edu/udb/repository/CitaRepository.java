package sv.edu.udb.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import sv.edu.udb.model.Cita;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CitaRepository extends JpaRepository<Cita, Long> {
    List<Cita> findByFechaOrderByHoraAsc(java.sql.Date fecha);
    List<Cita> findByEstadoOrderByFechaAsc(String estado);
    List<Cita> findByNombrePacienteContainingIgnoreCase(String nombrePaciente);
    List<Cita> findByNombreDoctorContainingIgnoreCase(String nombreDoctor);
}
