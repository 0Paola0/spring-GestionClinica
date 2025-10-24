package sv.edu.udb.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import sv.edu.udb.model.Laboratorio;
import sv.edu.udb.service.LaboratorioService;
import java.util.List;

@Controller
@RequestMapping("/laboratorio")
public class LaboratorioController {

    @Autowired
    private LaboratorioService laboratorioService;

    @GetMapping("/lista")
    public String lista(Model model) {
        List<Laboratorio> lista = laboratorioService.listarTodos();
        model.addAttribute("laboratorios", lista);
        return "listaLaboratorio";
    }

    @GetMapping("/nuevo")
    public String nuevo(Model model) {
        model.addAttribute("laboratorio", new Laboratorio());
        return "formLaboratorio";
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
        return "formLaboratorio";
    }

    @GetMapping("/eliminar/{id}")
    public String eliminar(@PathVariable Long id) {
        laboratorioService.eliminar(id);
        return "redirect:/laboratorio/lista";
    }
}

