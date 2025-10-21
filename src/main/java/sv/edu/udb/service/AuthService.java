package sv.edu.udb.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import sv.edu.udb.model.Usuario;
import sv.edu.udb.repository.UsuarioRepository;

import java.util.Optional;

@Service
public class AuthService {
    
    @Autowired
    private UsuarioRepository usuarioRepository;
    
    private final BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();

    public Usuario autenticarUsuario(String nombreUsuario, String contraseña) {
        try {
            // Buscar usuario por nombre de usuario
            Optional<Usuario> usuarioOpt = usuarioRepository.findByNombreUsuario(nombreUsuario);
            
            if (usuarioOpt.isPresent()) {
                Usuario usuario = usuarioOpt.get();
                
                // Verificar contraseña
                if (passwordEncoder.matches(contraseña, usuario.getContraseña())) {
                    return usuario;
                }
            }
            
            return null;
        } catch (Exception e) {
            return null;
        }
    }

    public String obtenerUrlRedireccion(Usuario usuario) {
        if (usuario == null) {
            return "/login?error=credenciales_invalidas";
        }
        
        switch (usuario.getRol()) {
            case Medico:
                return "/principal";
            case Administrador:
                return "/admin";
            default:
                return "/login?error=rol_no_valido";
        }
    }
}
