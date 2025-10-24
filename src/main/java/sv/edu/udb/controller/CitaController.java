package sv.edu.udb.controller;

import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import sv.edu.udb.model.Cita;
import sv.edu.udb.model.Paciente;
import sv.edu.udb.model.Usuario;
import sv.edu.udb.service.CitaService;
import sv.edu.udb.service.PacienteService;
import sv.edu.udb.service.UsuarioService;

import java.sql.Date;
import java.sql.Time;
import java.util.List;

@Controller
public class CitaController {

    @Autowired
    private CitaService citaService;

    @Autowired
    private PacienteService pacienteService;

    @Autowired
    private UsuarioService usuarioService;

    // Mostrar formulario de registro de citas
    @GetMapping("/registroCitas")
    public String mostrarFormularioCitas(HttpSession session, Model model) {
        Usuario usuario = (Usuario) session.getAttribute("usuario");

        if (usuario == null) {
            return "redirect:/login?error=sesion_expirada";
        }
        if (usuario.getRol() != Usuario.Rol.Medico) {
            return "redirect:/login?error=rol_no_valido";
        }

        // Cargar pacientes y doctores para los selects
        List<Paciente> pacientes = pacienteService.obtenerTodos();
        List<Usuario> doctores = usuarioService.obtenerTodos().stream()
                .filter(u -> u.getRol() == Usuario.Rol.Medico)
                .toList();

        model.addAttribute("pacientes", pacientes);
        model.addAttribute("doctores", doctores);
        model.addAttribute("nombreUsuario", usuario.getNombres() + " " + usuario.getApellidos());
        
        // Agregar foto del usuario si existe
        if (usuario.getFoto() != null && usuario.getFoto().length > 0) {
            String fotoBase64 = java.util.Base64.getEncoder().encodeToString(usuario.getFoto());
            model.addAttribute("fotoBase64", fotoBase64);
        }

        return "registroCitas";
    }

    // Guardar nueva cita
    @PostMapping("/guardarCita")
    public String guardarCita(
            @RequestParam("paciente") String nombrePaciente,
            @RequestParam("doctor") String nombreDoctor,
            @RequestParam("fecha") Date fecha,
            @RequestParam("hora") String horaString,
            @RequestParam("tipo") String tipoCita,
            @RequestParam("estado") String estado,
            @RequestParam(value = "telefono", required = false) String telefonoContacto,
            @RequestParam("duracion") String duracion,
            @RequestParam(value = "motivo", required = false) String motivoConsulta,
            @RequestParam(value = "observaciones", required = false) String observaciones,
            HttpSession session,
            RedirectAttributes redirectAttributes
    ) {
        Usuario usuario = (Usuario) session.getAttribute("usuario");
        if (usuario == null || usuario.getRol() != Usuario.Rol.Medico) {
            return "redirect:/login?error=sesion_expirada";
        }

        try {
            // Convertir String a Time
            Time hora = Time.valueOf(horaString + ":00"); // Agregar segundos si no los tiene
            
            Cita cita = new Cita();
            cita.setNombrePaciente(nombrePaciente.trim());
            cita.setNombreDoctor(nombreDoctor.trim());
            cita.setFecha(fecha);
            cita.setHora(hora);
            
            // Asignar tipo de cita y estado directamente
            cita.setTipoCita(tipoCita);
            cita.setEstado(estado);
            
            cita.setTelefonoContacto(telefonoContacto != null ? telefonoContacto.trim() : null);
            cita.setDuracion(duracion);
            cita.setMotivoConsulta(motivoConsulta != null ? motivoConsulta.trim() : null);
            cita.setObservaciones(observaciones != null ? observaciones.trim() : null);

            citaService.registrarCita(cita);
            redirectAttributes.addFlashAttribute("mensaje", "Cita registrada exitosamente.");
            return "redirect:/listaCitas";

        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("error", "Ocurrió un error al registrar la cita: " + e.getMessage());
            return "redirect:/registroCitas";
        }
    }

    // Listar todas las citas
    @GetMapping("/listaCitas")
    public String mostrarListaCitas(HttpSession session, Model model) {
        Usuario usuario = (Usuario) session.getAttribute("usuario");

        if (usuario == null) {
            return "redirect:/login?error=sesion_expirada";
        }
        if (usuario.getRol() != Usuario.Rol.Medico) {
            return "redirect:/login?error=rol_no_valido";
        }

        List<Cita> citas = citaService.obtenerTodas();
        model.addAttribute("citas", citas);
        model.addAttribute("nombreUsuario", usuario.getNombres() + " " + usuario.getApellidos());
        
        // Agregar foto del usuario si existe
        if (usuario.getFoto() != null && usuario.getFoto().length > 0) {
            String fotoBase64 = java.util.Base64.getEncoder().encodeToString(usuario.getFoto());
            model.addAttribute("fotoBase64", fotoBase64);
        }

        return "listaCitas";
    }

    // Ver cita individual
    @GetMapping("/citas/ver/{id}")
    public String verCita(@PathVariable Long id, Model model, HttpSession session) {
        Usuario usuario = (Usuario) session.getAttribute("usuario");
        if (usuario == null || usuario.getRol() != Usuario.Rol.Medico) {
            return "redirect:/login?error=sesion_expirada";
        }

        try {
            Cita cita = citaService.obtenerPorId(id);
            
            // Cargar pacientes y doctores para los selects
            List<Paciente> pacientes = pacienteService.obtenerTodos();
            List<Usuario> doctores = usuarioService.obtenerTodos().stream()
                    .filter(u -> u.getRol() == Usuario.Rol.Medico)
                    .toList();

            model.addAttribute("cita", cita);
            model.addAttribute("pacientes", pacientes);
            model.addAttribute("doctores", doctores);
            model.addAttribute("nombreUsuario", usuario.getNombres() + " " + usuario.getApellidos());
            model.addAttribute("modoEdicion", false); // Modo solo lectura
            
            // Agregar foto del usuario si existe
            if (usuario.getFoto() != null && usuario.getFoto().length > 0) {
                String fotoBase64 = java.util.Base64.getEncoder().encodeToString(usuario.getFoto());
                model.addAttribute("fotoBase64", fotoBase64);
            }

            return "registroCitas";
        } catch (Exception e) {
            return "redirect:/listaCitas?error=cita_no_encontrada";
        }
    }

    // Editar cita
    @GetMapping("/citas/editar/{id}")
    public String editarCita(@PathVariable Long id, Model model, HttpSession session) {
        Usuario usuario = (Usuario) session.getAttribute("usuario");
        if (usuario == null || usuario.getRol() != Usuario.Rol.Medico) {
            return "redirect:/login?error=sesion_expirada";
        }

        try {
            Cita cita = citaService.obtenerPorId(id);
            
            // Cargar pacientes y doctores para los selects
            List<Paciente> pacientes = pacienteService.obtenerTodos();
            List<Usuario> doctores = usuarioService.obtenerTodos().stream()
                    .filter(u -> u.getRol() == Usuario.Rol.Medico)
                    .toList();

            model.addAttribute("cita", cita);
            model.addAttribute("pacientes", pacientes);
            model.addAttribute("doctores", doctores);
            model.addAttribute("nombreUsuario", usuario.getNombres() + " " + usuario.getApellidos());
            model.addAttribute("modoEdicion", true); // Modo edición
            
            // Agregar foto del usuario si existe
            if (usuario.getFoto() != null && usuario.getFoto().length > 0) {
                String fotoBase64 = java.util.Base64.getEncoder().encodeToString(usuario.getFoto());
                model.addAttribute("fotoBase64", fotoBase64);
            }

            return "registroCitas";
        } catch (Exception e) {
            return "redirect:/listaCitas?error=cita_no_encontrada";
        }
    }

    // Actualizar cita
    @PostMapping("/citas/actualizar")
    public String actualizarCita(
            @RequestParam("id") Long id,
            @RequestParam("paciente") String nombrePaciente,
            @RequestParam("doctor") String nombreDoctor,
            @RequestParam("fecha") Date fecha,
            @RequestParam("hora") String horaString,
            @RequestParam("tipo") String tipoCita,
            @RequestParam("estado") String estado,
            @RequestParam(value = "telefono", required = false) String telefonoContacto,
            @RequestParam("duracion") String duracion,
            @RequestParam(value = "motivo", required = false) String motivoConsulta,
            @RequestParam(value = "observaciones", required = false) String observaciones,
            HttpSession session,
            RedirectAttributes redirectAttributes
    ) {
        Usuario usuario = (Usuario) session.getAttribute("usuario");
        if (usuario == null || usuario.getRol() != Usuario.Rol.Medico) {
            return "redirect:/login?error=sesion_expirada";
        }

        try {
            Cita cita = citaService.obtenerPorId(id);
            
            // Convertir String a Time
            Time hora = Time.valueOf(horaString + ":00");
            
            cita.setNombrePaciente(nombrePaciente.trim());
            cita.setNombreDoctor(nombreDoctor.trim());
            cita.setFecha(fecha);
            cita.setHora(hora);
            cita.setTipoCita(tipoCita);
            cita.setEstado(estado);
            cita.setTelefonoContacto(telefonoContacto != null ? telefonoContacto.trim() : null);
            cita.setDuracion(duracion);
            cita.setMotivoConsulta(motivoConsulta != null ? motivoConsulta.trim() : null);
            cita.setObservaciones(observaciones != null ? observaciones.trim() : null);

            citaService.registrarCita(cita);
            redirectAttributes.addFlashAttribute("mensaje", "Cita actualizada exitosamente.");
            return "redirect:/listaCitas";

        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("error", "Error al actualizar la cita: " + e.getMessage());
            return "redirect:/citas/editar/" + id;
        }
    }

    // Eliminar cita
    @PostMapping("/citas/eliminar/{id}")
    public String eliminarCita(@PathVariable Long id, RedirectAttributes redirectAttributes, HttpSession session) {
        Usuario usuario = (Usuario) session.getAttribute("usuario");
        if (usuario == null || usuario.getRol() != Usuario.Rol.Medico) {
            return "redirect:/login?error=sesion_expirada";
        }

        try {
            citaService.eliminarPorId(id);
            redirectAttributes.addFlashAttribute("mensaje", "Cita eliminada correctamente.");
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("error", "Error al eliminar la cita: " + e.getMessage());
        }
        
        return "redirect:/listaCitas";
    }
}