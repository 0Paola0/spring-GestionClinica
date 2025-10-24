package sv.edu.udb.controller;

import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import sv.edu.udb.dto.CitaDTO;
import sv.edu.udb.model.Cita;
import sv.edu.udb.model.Usuario;
import sv.edu.udb.service.CitaService;

import java.sql.Date;
import java.sql.Time;
import java.text.SimpleDateFormat;
import java.util.Base64;
import java.util.List;
import java.util.stream.Collectors;

@Controller
public class AgendaController {

    @Autowired
    private CitaService citaService;

    @GetMapping("/agenda")
    public String mostrarAgenda(HttpSession session, Model model) {
        Usuario usuario = (Usuario) session.getAttribute("usuario");

        if (usuario == null) {
            return "redirect:/login?error=sesion_expirada";
        }
        if (usuario.getRol() != Usuario.Rol.Medico) {
            return "redirect:/login?error=rol_no_valido";
        }

        // Obtener todas las citas y convertir a DTOs
        List<Cita> citas = citaService.obtenerTodas();
        List<CitaDTO> citasDTO = citas.stream().map(this::convertirADTO).collect(Collectors.toList());
        model.addAttribute("citas", citasDTO);

        // Agregar datos del usuario al modelo
        model.addAttribute("usuario", usuario);
        model.addAttribute("nombreUsuario", usuario.getNombres() + " " + usuario.getApellidos());
        
        if (usuario.getFoto() != null && usuario.getFoto().length > 0) {
            String fotoBase64 = Base64.getEncoder().encodeToString(usuario.getFoto());
            model.addAttribute("fotoBase64", fotoBase64);
        }

        return "agenda";
    }

    @GetMapping("/api/citas")
    @ResponseBody
    public List<CitaDTO> obtenerCitasAPI(HttpSession session) {
        Usuario usuario = (Usuario) session.getAttribute("usuario");

        if (usuario == null || usuario.getRol() != Usuario.Rol.Medico) {
            return List.of();
        }

        List<Cita> citas = citaService.obtenerTodas();
        return citas.stream().map(this::convertirADTO).collect(Collectors.toList());
    }

    private CitaDTO convertirADTO(Cita cita) {
        CitaDTO dto = new CitaDTO();
        dto.setId(cita.getId());
        dto.setNombrePaciente(cita.getNombrePaciente());
        dto.setNombreDoctor(cita.getNombreDoctor());
        
        // Convertir Date a String
        if (cita.getFecha() != null) {
            dto.setFecha(cita.getFecha().toString());
        }
        
        // Convertir Time a String
        if (cita.getHora() != null) {
            dto.setHora(cita.getHora().toString());
        }
        
        dto.setTipoCita(cita.getTipoCita());
        dto.setEstado(cita.getEstado());
        dto.setTelefonoContacto(cita.getTelefonoContacto());
        dto.setDuracion(cita.getDuracion());
        dto.setMotivoConsulta(cita.getMotivoConsulta());
        dto.setObservaciones(cita.getObservaciones());
        
        return dto;
    }
}
