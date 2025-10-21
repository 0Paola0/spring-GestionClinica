package sv.edu.udb.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import sv.edu.udb.model.Usuario;
import sv.edu.udb.service.AuthService;

import jakarta.servlet.http.HttpSession;
import java.util.Base64;

@Controller
public class AuthController {
    
    @Autowired
    private AuthService authService;
    
    @GetMapping("/")
    public String mostrarLogin() {
        return "login";
    }

    @GetMapping("/login")
    public String mostrarLoginConMensaje(@RequestParam(value = "registro", required = false) String registro,
                                        @RequestParam(value = "error", required = false) String error,
                                        Model model) {
        if ("exitoso".equals(registro)) {
            model.addAttribute("mensaje", "Usuario registrado exitosamente. Ya puede iniciar sesión.");
        }
        
        if ("credenciales_invalidas".equals(error)) {
            model.addAttribute("error", "Credenciales incorrectas. Verifique su nombre de usuario y contraseña.");
        }
        
        if ("rol_no_valido".equals(error)) {
            model.addAttribute("error", "Rol de usuario no válido.");
        }
        
        if ("sesion_expirada".equals(error)) {
            model.addAttribute("error", "Su sesión ha expirado. Por favor, inicie sesión nuevamente.");
        }
        
        return "login";
    }
    
    @PostMapping("/login")
    public String procesarLogin(@RequestParam("nombreUsuario") String nombreUsuario,
                               @RequestParam("contraseña") String contraseña,
                               HttpSession session,
                               Model model) {
        try {
            // Autenticar usuario
            Usuario usuario = authService.autenticarUsuario(nombreUsuario, contraseña);
            
            if (usuario != null) {
                // Guardar usuario en sesión
                session.setAttribute("usuario", usuario);
                session.setAttribute("usuarioId", usuario.getId());
                session.setAttribute("nombreUsuario", usuario.getNombreUsuario());
                session.setAttribute("rol", usuario.getRol().toString());
                
                // Redirigir según el rol
                String urlRedireccion = authService.obtenerUrlRedireccion(usuario);
                return "redirect:" + urlRedireccion;
            } else {
                model.addAttribute("error", "Credenciales incorrectas. Verifique su nombre de usuario y contraseña.");
                return "login";
            }
            
        } catch (Exception e) {
            model.addAttribute("error", "Error interno del servidor. Por favor, intente nuevamente.");
            return "login";
        }
    }
    
    @GetMapping("/principal")
    public String mostrarPrincipal(HttpSession session, Model model) {
        // Verificar si el usuario está autenticado
        Usuario usuario = (Usuario) session.getAttribute("usuario");
        
        if (usuario == null) {
            return "redirect:/login?error=sesion_expirada";
        }
        
        // Verificar que sea médico
        if (usuario.getRol() != Usuario.Rol.Medico) {
            return "redirect:/login?error=rol_no_valido";
        }
        
        // Agregar información del usuario al modelo
        model.addAttribute("usuario", usuario);
        model.addAttribute("nombreUsuario", usuario.getNombreUsuario());
        model.addAttribute("nombres", usuario.getNombres());
        model.addAttribute("apellidos", usuario.getApellidos());
        model.addAttribute("rol", usuario.getRol().toString());
        
        // Convertir foto a Base64 si existe
        if (usuario.getFoto() != null && usuario.getFoto().length > 0) {
            String fotoBase64 = Base64.getEncoder().encodeToString(usuario.getFoto());
            model.addAttribute("fotoBase64", fotoBase64);
        }
        
        return "principal";
    }
    
    @GetMapping("/logout")
    public String cerrarSesion(HttpSession session) {
        // Invalidar sesión
        session.invalidate();
        return "redirect:/login?logout=exitoso";
    }
    
    @GetMapping("/ir-a-registro")
    public String mostrarRegistro() {
        return "redirect:/registro";
    }

    @GetMapping("/recuperacion")
    public String mostrarRecuperacion() {
        return "recuperacion";
    }

}
