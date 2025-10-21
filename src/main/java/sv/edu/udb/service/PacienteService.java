package sv.edu.udb.service;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import sv.edu.udb.model.Paciente;
import sv.edu.udb.repository.PacienteRepository;
import java.sql.Timestamp;
import java.time.Instant;
import java.util.List;

@Service
public class PacienteService {
    @Autowired
    private PacienteRepository pacienteRepository;

    public String generarCodigoPaciente() {
        long total = pacienteRepository.count() + 1;
        return String.format("PA%03d", total);
    }
    public Paciente registrarPaciente(Paciente paciente) {
        if (paciente.getCodigo() == null || paciente.getCodigo().isBlank()) {
            paciente.setCodigo(generarCodigoPaciente());
        }
        paciente.setFechaRegistro(Timestamp.from(Instant.now()));
        return pacienteRepository.save(paciente);
    }
    public List<Paciente> obtenerTodos() {
        return pacienteRepository.findAll();
    }
    public Paciente obtenerPorId(Long id) {
        return pacienteRepository.findById(id).orElseThrow(() -> new RuntimeException("Paciente no encontrado"));
    }

    public void eliminarPorId(Long id) {
        pacienteRepository.deleteById(id);
    }
    @Transactional
    public void reordenarCodigos() {
        List<Paciente> pacientes = pacienteRepository.findAll(Sort.by("id"));
        int contador = 1;
        for (Paciente p : pacientes) {
            p.setCodigo(String.format("PAD%02d", contador));
            contador++;
        }
        pacienteRepository.saveAll(pacientes);
    }




}
