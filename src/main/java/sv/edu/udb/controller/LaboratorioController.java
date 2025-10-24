package sv.edu.udb.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import sv.edu.udb.model.Laboratorio;
import sv.edu.udb.model.Paciente;
import sv.edu.udb.model.Cita;
import sv.edu.udb.service.LaboratorioService;
import sv.edu.udb.service.PacienteService;
import sv.edu.udb.service.CitaService;
import java.util.List;

@Controller
@RequestMapping("/laboratorio")
public class LaboratorioController {

    @Autowired
    private LaboratorioService laboratorioService;
    
    @Autowired
    private PacienteService pacienteService;
    
    @Autowired
    private CitaService citaService;

    @GetMapping("/lista")
    public String lista(Model model) {
        List<Laboratorio> lista = laboratorioService.listarTodos();
        model.addAttribute("laboratorios", lista);
        return "Laboratorio/listaLaboratorios";
    }

    @GetMapping("/nuevo")
    public String nuevo(Model model) {
        model.addAttribute("laboratorio", new Laboratorio());
        return "Laboratorio/formLaboratorio";
    }

    @PostMapping("/guardar")
    public String guardar(@ModelAttribute("laboratorio") Laboratorio lab) {
        laboratorioService.guardar(lab);
        return "redirect:/laboratorio/lista";
    }

    @GetMapping("/editar/{id}")
    public String editar(@PathVariable Long id, Model model) {
        Laboratorio lab = laboratorioService.obtenerPorId(id);
        model.addAttribute("laboratorio", lab);
        return "Laboratorio/formLaboratorio";
    }

    @GetMapping("/eliminar/{id}")
    public String eliminar(@PathVariable Long id) {
        laboratorioService.eliminar(id);
        return "redirect:/laboratorio/lista";
    }
    
    // Endpoint para obtener pacientes para autocompletado
    @GetMapping("/api/pacientes")
    @ResponseBody
    public List<Paciente> obtenerPacientes() {
        return pacienteService.obtenerTodos();
    }
    
    // Endpoint para obtener tipos de cita para autocompletado
    @GetMapping("/api/tipos-cita")
    @ResponseBody
    public List<String> obtenerTiposCita() {
        return List.of(
            "Consulta general",
            "Laboratorio clínico", 
            "Farmacia",
            "Especialidad médica"
        );
    }
}

