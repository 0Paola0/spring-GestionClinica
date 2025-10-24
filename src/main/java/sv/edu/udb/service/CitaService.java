package sv.edu.udb.service;

import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import sv.edu.udb.model.Cita;
import sv.edu.udb.repository.CitaRepository;

import java.sql.Timestamp;
import java.time.Instant;
import java.util.List;

@Service
public class CitaService {
    @Autowired
    private CitaRepository citaRepository;

    public Cita registrarCita(Cita cita) {
        if (cita.getFechaCreacion() == null) {
            cita.setFechaCreacion(Timestamp.from(Instant.now()));
        }
        if (cita.getEstado() == null) {
            cita.setEstado("Programada");
        }
        if (cita.getDuracion() == null || cita.getDuracion().isEmpty()) {
            cita.setDuracion("30 minutos");
        }
        return citaRepository.save(cita);
    }

    public List<Cita> obtenerTodas() {
        return citaRepository.findAll(Sort.by("fecha").ascending().and(Sort.by("hora").ascending()));
    }

    public Cita obtenerPorId(Long id) {
        return citaRepository.findById(id).orElseThrow(() -> new RuntimeException("Cita no encontrada"));
    }

    public void eliminarPorId(Long id) {
        citaRepository.deleteById(id);
    }

    public List<Cita> obtenerPorFecha(java.sql.Date fecha) {
        return citaRepository.findByFechaOrderByHoraAsc(fecha);
    }

    public List<Cita> obtenerPorEstado(String estado) {
        return citaRepository.findByEstadoOrderByFechaAsc(estado);
    }

    public List<Cita> buscarPorPaciente(String nombrePaciente) {
        return citaRepository.findByNombrePacienteContainingIgnoreCase(nombrePaciente);
    }

    public List<Cita> buscarPorDoctor(String nombreDoctor) {
        return citaRepository.findByNombreDoctorContainingIgnoreCase(nombreDoctor);
    }
}
