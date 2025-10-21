package sv.edu.udb.controller;
import jakarta.validation.Valid;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import jakarta.servlet.http.HttpSession;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import sv.edu.udb.model.Usuario;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import sv.edu.udb.model.Paciente;
import sv.edu.udb.service.PacienteService;

import java.io.IOException;
import java.sql.Timestamp;
import java.time.Instant;
import java.util.Base64;

@Controller
public class PacienteController {

    @Autowired
    private PacienteService pacienteService;

    // Listar pacientes
    @GetMapping("/pacientes")
    public String mostrarListaPacientes(HttpSession session, Model model) {
        Usuario usuario = (Usuario) session.getAttribute("usuario");

        if (usuario == null) {
            return "redirect:/login?error=sesion_expirada";
        }
        if (usuario.getRol() != Usuario.Rol.Medico) {
            return "redirect:/login?error=rol_no_valido";
        }

        model.addAttribute("pacientes", pacienteService.obtenerTodos());
        return "listaPacientes";
    }

    // Mostrar formulario para nuevo paciente
    @GetMapping("/fichaPacientes")
    public String mostrarFichaPacienteNuevo(HttpSession session, Model model) {
        Usuario usuario = (Usuario) session.getAttribute("usuario");

        if (usuario == null || usuario.getRol() != Usuario.Rol.Medico) {
            return "redirect:/login?error=sesion_expirada";
        }

        Paciente paciente = new Paciente();
        paciente.setCodigo(pacienteService.generarCodigoPaciente());

        model.addAttribute("paciente", paciente);
        model.addAttribute("modoEdicion", true);
        return "fichaPacientes";
    }

    // Guardar nuevo paciente
    @PostMapping("/guardarPaciente")
    public String guardarPaciente(
            @RequestParam("nombres") String nombres,
            @RequestParam("apellidos") String apellidos,
            @RequestParam("edad") Integer edad,
            @RequestParam("telefono") String telefono,
            @RequestParam("motivoConsulta") String motivoConsulta,
            @RequestParam("enfermedadActual") String enfermedadActual,
            @RequestParam("diabetes") Boolean diabetes,
            @RequestParam("hipertension") Boolean hipertension,
            @RequestParam("alergias") Boolean alergias,
            @RequestParam("cirugias") Boolean cirugias,
            @RequestParam("medicamentos") Boolean medicamentos,
            @RequestParam("detalleDiabetes") String detalleDiabetes,
            @RequestParam("detalleHipertension") String detalleHipertension,
            @RequestParam("detalleAlergias") String detalleAlergias,
            @RequestParam("detalleCirugias") String detalleCirugias,
            @RequestParam("detalleMedicamentos") String detalleMedicamentos,
            @RequestParam("foto") MultipartFile foto,
            Model model,
            HttpSession session,
            RedirectAttributes redirectAttributes
    ) throws IOException {

        Usuario usuario = (Usuario) session.getAttribute("usuario");
        if (usuario == null || usuario.getRol() != Usuario.Rol.Medico) {
            return "redirect:/login?error=sesion_expirada";
        }

        // Validaciones de imagen
        if (foto == null || foto.isEmpty()) {
            model.addAttribute("error", "La foto del paciente es obligatoria.");
            model.addAttribute("modoEdicion", true);
            return "fichaPacientes";
        }
        if (!foto.getContentType().startsWith("image/")) {
            model.addAttribute("error", "Por favor, seleccione un archivo de imagen válido.");
            model.addAttribute("modoEdicion", true);
            return "fichaPacientes";
        }
        if (foto.getSize() > 5 * 1024 * 1024) {
            model.addAttribute("error", "La imagen es demasiado grande. Máximo permitido: 5MB.");
            model.addAttribute("modoEdicion", true);
            return "fichaPacientes";
        }

        try {
            Paciente paciente = new Paciente();
            paciente.setNombres(nombres.trim());
            paciente.setApellidos(apellidos.trim());
            paciente.setEdad(edad);
            paciente.setTelefono(telefono.trim());
            paciente.setMotivoConsulta(motivoConsulta.trim());
            paciente.setEnfermedadActual(enfermedadActual.trim());
            paciente.setDiabetes(diabetes);
            paciente.setHipertension(hipertension);
            paciente.setAlergias(alergias);
            paciente.setCirugiasPrevias(cirugias);
            paciente.setMedicamentosActuales(medicamentos);
            paciente.setDetalleDiabetes(detalleDiabetes.trim());
            paciente.setDetalleHipertension(detalleHipertension.trim());
            paciente.setDetalleAlergias(detalleAlergias.trim());
            paciente.setDetalleCirugias(detalleCirugias.trim());
            paciente.setDetalleMedicamentos(detalleMedicamentos.trim());
            paciente.setFoto(foto.getBytes());
            paciente.setFechaRegistro(Timestamp.from(Instant.now()));
            paciente.setCodigo(pacienteService.generarCodigoPaciente());

            pacienteService.registrarPaciente(paciente);
            redirectAttributes.addFlashAttribute("mensaje", "Paciente guardado exitosamente.");
            return "redirect:/pacientes";

        } catch (Exception e) {
            model.addAttribute("error", "Ocurrió un error al registrar el paciente. Intente nuevamente.");
            model.addAttribute("modoEdicion", true);
            return "fichaPacientes";
        }
    }

    // Ver paciente en modo solo lectura
    @GetMapping("/pacientes/ver/{id}")
    public String verPaciente(@PathVariable Long id, Model model, HttpSession session) {
        Usuario usuario = (Usuario) session.getAttribute("usuario");
        if (usuario == null || usuario.getRol() != Usuario.Rol.Medico) {
            return "redirect:/login?error=sesion_expirada";
        }

        Paciente paciente = pacienteService.obtenerPorId(id);
        model.addAttribute("paciente", paciente);
        model.addAttribute("modoEdicion", false);

        if (paciente.getFoto() != null && paciente.getFoto().length > 0) {
            String fotoBase64 = Base64.getEncoder().encodeToString(paciente.getFoto());
            model.addAttribute("fotoBase64", fotoBase64);
        }

        return "fichaPacientes";
    }

    // Editar paciente
    @GetMapping("/pacientes/editar/{id}")
    public String editarPaciente(@PathVariable Long id, Model model, HttpSession session) {
        Usuario usuario = (Usuario) session.getAttribute("usuario");
        if (usuario == null || usuario.getRol() != Usuario.Rol.Medico) {
            return "redirect:/login?error=sesion_expirada";
        }

        Paciente paciente = pacienteService.obtenerPorId(id);
        model.addAttribute("paciente", paciente);
        model.addAttribute("modoEdicion", true);

        if (paciente.getFoto() != null && paciente.getFoto().length > 0) {
            String fotoBase64 = Base64.getEncoder().encodeToString(paciente.getFoto());
            model.addAttribute("fotoBase64", fotoBase64);
        }

        return "fichaPacientes";
    }

    // Actualizar paciente
    @PostMapping("/pacientes/actualizar")
    public String actualizarPaciente(
            @RequestParam("id") Long id,
            @RequestParam("nombres") String nombres,
            @RequestParam("apellidos") String apellidos,
            @RequestParam("edad") Integer edad,
            @RequestParam("telefono") String telefono,
            @RequestParam("motivoConsulta") String motivoConsulta,
            @RequestParam("enfermedadActual") String enfermedadActual,
            @RequestParam("diabetes") Boolean diabetes,
            @RequestParam("hipertension") Boolean hipertension,
            @RequestParam("alergias") Boolean alergias,
            @RequestParam("cirugias") Boolean cirugias,
            @RequestParam("medicamentos") Boolean medicamentos,
            @RequestParam("detalleDiabetes") String detalleDiabetes,
            @RequestParam("detalleHipertension") String detalleHipertension,
            @RequestParam("detalleAlergias") String detalleAlergias,
            @RequestParam("detalleCirugias") String detalleCirugias,
            @RequestParam("detalleMedicamentos") String detalleMedicamentos,
            @RequestParam(value = "foto", required = false) MultipartFile foto,
            HttpSession session,
            RedirectAttributes redirectAttributes,
            Model model
    ) throws IOException {

        Usuario usuario = (Usuario) session.getAttribute("usuario");
        if (usuario == null || usuario.getRol() != Usuario.Rol.Medico) {
            return "redirect:/login?error=sesion_expirada";
        }

        if (id == null) {
            redirectAttributes.addFlashAttribute("error", "No se recibió el ID del paciente para actualizar.");
            return "redirect:/pacientes";
        }

        try {
            Paciente paciente = pacienteService.obtenerPorId(id);
            paciente.setNombres(nombres.trim());
            paciente.setApellidos(apellidos.trim());
            paciente.setEdad(edad);
            paciente.setTelefono(telefono.trim());
            paciente.setMotivoConsulta(motivoConsulta.trim());
            paciente.setEnfermedadActual(enfermedadActual.trim());
            paciente.setDiabetes(diabetes);
            paciente.setHipertension(hipertension);
            paciente.setHipertension(hipertension);
            paciente.setAlergias(alergias);
            paciente.setCirugiasPrevias(cirugias);
            paciente.setMedicamentosActuales(medicamentos);
            paciente.setDetalleDiabetes(detalleDiabetes.trim());
            paciente.setDetalleHipertension(detalleHipertension.trim());
            paciente.setDetalleAlergias(detalleAlergias.trim());
            paciente.setDetalleCirugias(detalleCirugias.trim());
            paciente.setDetalleMedicamentos(detalleMedicamentos.trim());

            if (foto != null && !foto.isEmpty()) {
                if (!foto.getContentType().startsWith("image/")) {
                    model.addAttribute("error", "Por favor, seleccione un archivo de imagen válido.");
                    model.addAttribute("modoEdicion", true);
                    model.addAttribute("paciente", paciente);
                    return "fichaPacientes";
                }
                if (foto.getSize() > 5 * 1024 * 1024) {
                    model.addAttribute("error", "La imagen es demasiado grande. Máximo permitido: 5MB.");
                    model.addAttribute("modoEdicion", true);
                    model.addAttribute("paciente", paciente);
                    return "fichaPacientes";
                }
                paciente.setFoto(foto.getBytes());
            }

            pacienteService.registrarPaciente(paciente); // reutilizas el save

            redirectAttributes.addFlashAttribute("mensaje", "Paciente actualizado exitosamente.");
            return "redirect:/pacientes";

        } catch (Exception e) {
            model.addAttribute("error", "Error al actualizar el paciente: " + e.getMessage());
            model.addAttribute("modoEdicion", true);
            return "fichaPacientes";
        }
    }

    // Eliminar paciente
    @PostMapping("/pacientes/eliminar/{id}")
    public String eliminarPaciente(@PathVariable Long id, RedirectAttributes redirectAttributes, HttpSession session) {
        Usuario usuario = (Usuario) session.getAttribute("usuario");
        if (usuario == null || usuario.getRol() != Usuario.Rol.Medico) {
            return "redirect:/login?error=sesion_expirada";
        }

        pacienteService.eliminarPorId(id);
        pacienteService.reordenarCodigos();
        redirectAttributes.addFlashAttribute("mensaje", "Paciente eliminado correctamente.");
        return "redirect:/pacientes";
    }
}




