package sv.edu.udb.service;

import org.springframework.stereotype.Service;
import org.springframework.beans.factory.annotation.Autowired;
import sv.edu.udb.model.Laboratorio;
import sv.edu.udb.repository.LaboratorioRepository;
import java.util.List;

@Service
public class LaboratorioService {

    @Autowired
    private LaboratorioRepository laboratorioRepository;

    public List<Laboratorio> listarTodos() {
        return laboratorioRepository.findAll();
    }

    public Laboratorio guardar(Laboratorio lab) {
        return laboratorioRepository.save(lab);
    }

    public Laboratorio obtenerPorId(Long id) {
        return laboratorioRepository.findById(id).orElse(null);
    }

    public void eliminar(Long id) {
        laboratorioRepository.deleteById(id);
    }
}

