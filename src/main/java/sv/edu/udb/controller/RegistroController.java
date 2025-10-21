package sv.edu.udb.controller;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.BeanPropertyBindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import sv.edu.udb.model.Usuario;
import sv.edu.udb.service.UsuarioService;

import jakarta.validation.Valid;
import java.io.IOException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

@Controller
public class RegistroController {
    @Autowired
    private UsuarioService usuarioService;

    @GetMapping("/registro")
    public String mostrarFormulario(Model model) {
        model.addAttribute("usuario", new Usuario());
        return "registro";
    }

    @PostMapping("/registro")
    public String procesarRegistro(@RequestParam(value = "nombres", required = false) String nombres,
                                   @RequestParam(value = "apellidos", required = false) String apellidos,
                                   @RequestParam(value = "tipoDocumento", required = false) String tipoDocumento,
                                   @RequestParam(value = "numeroDocumento", required = false) String numeroDocumento,
                                   @RequestParam(value = "correoElectronico", required = false) String correoElectronico,
                                   @RequestParam(value = "telefonoCelular", required = false) String telefonoCelular,
                                   @RequestParam(value = "telefonoAlternativo", required = false) String telefonoAlternativo,
                                   @RequestParam(value = "direccion", required = false) String direccion,
                                   @RequestParam(value = "nombreUsuario", required = false) String nombreUsuario,
                                   @RequestParam(value = "contraseña", required = false) String contraseña,
                                   @RequestParam(value = "rol", required = false) String rol,
                                   @RequestParam(value = "genero", required = false) String genero,
                                   @RequestParam(value = "foto", required = false) MultipartFile foto,
                                   @RequestParam(value = "fechaNacimientoStr", required = false) String fechaNacimientoStr,
                                   Model model) {
        Usuario usuario = new Usuario();
        try {
            // Crear objeto Usuario manualmente
            usuario.setNombres(nombres);
            usuario.setApellidos(apellidos);
            usuario.setTipoDocumento(tipoDocumento);
            usuario.setNumeroDocumento(numeroDocumento);
            usuario.setCorreoElectronico(correoElectronico);
            usuario.setTelefonoCelular(telefonoCelular);
            usuario.setTelefonoAlternativo(telefonoAlternativo);
            usuario.setDireccion(direccion);
            usuario.setNombreUsuario(nombreUsuario);
            usuario.setContraseña(contraseña);
            
            // Convertir rol de String a enum
            if (rol != null && !rol.isEmpty()) {
                try {
                    usuario.setRol(Usuario.Rol.valueOf(rol));
                } catch (IllegalArgumentException e) {
                    // Rol inválido, se manejará en las validaciones
                }
            }
            
            // Convertir género de String a enum
            if (genero != null && !genero.isEmpty()) {
                try {
                    usuario.setGenero(Usuario.Genero.valueOf(genero));
                } catch (IllegalArgumentException e) {
                    // Género inválido, se manejará en las validaciones
                }
            }
            
            // Crear BindingResult manualmente para las validaciones
            BindingResult bindingResult = new BeanPropertyBindingResult(usuario, "usuario");
            // Convertir fecha de nacimiento de String a LocalDate si se proporciona
            if (fechaNacimientoStr != null && !fechaNacimientoStr.isEmpty()) {
                try {
                    LocalDate fechaNacimiento = LocalDate.parse(fechaNacimientoStr);
                    // Validar que la fecha no sea futura
                    if (fechaNacimiento.isAfter(LocalDate.now())) {
                        bindingResult.rejectValue("fechaNacimiento", "error.fechaNacimiento", "Campo FECHA DE NACIMIENTO: La fecha de nacimiento no puede ser futura");
                    } else {
                        usuario.setFechaNacimiento(fechaNacimiento);
                    }
                } catch (Exception e) {
                    bindingResult.rejectValue("fechaNacimiento", "error.fechaNacimiento", "Campo FECHA DE NACIMIENTO: Formato de fecha inválido");
                }
            }
            
            // Validar manualmente los campos requeridos con mensajes específicos
            if (usuario.getNombres() == null || usuario.getNombres().trim().isEmpty()) {
                bindingResult.rejectValue("nombres", "error.nombres", "Campo NOMBRES: Los nombres son obligatorios");
            } else if (usuario.getNombres().trim().length() < 2) {
                bindingResult.rejectValue("nombres", "error.nombres", "Campo NOMBRES: Debe tener al menos 2 caracteres");
            }
            
            if (usuario.getApellidos() == null || usuario.getApellidos().trim().isEmpty()) {
                bindingResult.rejectValue("apellidos", "error.apellidos", "Campo APELLIDOS: Los apellidos son obligatorios");
            } else if (usuario.getApellidos().trim().length() < 2) {
                bindingResult.rejectValue("apellidos", "error.apellidos", "Campo APELLIDOS: Debe tener al menos 2 caracteres");
            }
            
            if (usuario.getTipoDocumento() == null || usuario.getTipoDocumento().trim().isEmpty()) {
                bindingResult.rejectValue("tipoDocumento", "error.tipoDocumento", "Campo TIPO DE DOCUMENTO: Debe seleccionar un tipo de documento");
            }
            
            if (usuario.getNumeroDocumento() == null || usuario.getNumeroDocumento().trim().isEmpty()) {
                bindingResult.rejectValue("numeroDocumento", "error.numeroDocumento", "Campo NÚMERO DE DOCUMENTO: El número de documento es obligatorio");
            } else if (!usuario.getNumeroDocumento().matches("^[0-9-]+$")) {
                bindingResult.rejectValue("numeroDocumento", "error.numeroDocumento", "Campo NÚMERO DE DOCUMENTO: Solo se permiten números y guiones");
            } else if (usuario.getNumeroDocumento().length() < 8) {
                bindingResult.rejectValue("numeroDocumento", "error.numeroDocumento", "Campo NÚMERO DE DOCUMENTO: Debe tener al menos 8 caracteres");
            }
            
            if (usuario.getCorreoElectronico() == null || usuario.getCorreoElectronico().trim().isEmpty()) {
                bindingResult.rejectValue("correoElectronico", "error.correoElectronico", "Campo CORREO ELECTRÓNICO: El correo electrónico es obligatorio");
            } else if (!usuario.getCorreoElectronico().matches("^[A-Za-z0-9+_.-]+@[A-Za-z0-9.-]+\\.[A-Za-z]{2,}$")) {
                bindingResult.rejectValue("correoElectronico", "error.correoElectronico", "Campo CORREO ELECTRÓNICO: Formato de correo inválido (ejemplo: usuario@dominio.com)");
            }
            
            if (usuario.getTelefonoCelular() == null || usuario.getTelefonoCelular().trim().isEmpty()) {
                bindingResult.rejectValue("telefonoCelular", "error.telefonoCelular", "Campo TELÉFONO CELULAR: El teléfono celular es obligatorio");
            } else if (!usuario.getTelefonoCelular().matches("^[0-9-]+$")) {
                bindingResult.rejectValue("telefonoCelular", "error.telefonoCelular", "Campo TELÉFONO CELULAR: Solo se permiten números y guiones");
            } else if (usuario.getTelefonoCelular().replaceAll("[^0-9]", "").length() < 8) {
                bindingResult.rejectValue("telefonoCelular", "error.telefonoCelular", "Campo TELÉFONO CELULAR: Debe tener al menos 8 dígitos");
            }
            
            // Validar teléfono alternativo si se proporciona
            if (usuario.getTelefonoAlternativo() != null && !usuario.getTelefonoAlternativo().trim().isEmpty()) {
                if (!usuario.getTelefonoAlternativo().matches("^[0-9-]+$")) {
                    bindingResult.rejectValue("telefonoAlternativo", "error.telefonoAlternativo", "Campo TELÉFONO ALTERNATIVO: Solo se permiten números y guiones");
                } else if (usuario.getTelefonoAlternativo().replaceAll("[^0-9]", "").length() < 8) {
                    bindingResult.rejectValue("telefonoAlternativo", "error.telefonoAlternativo", "Campo TELÉFONO ALTERNATIVO: Debe tener al menos 8 dígitos");
                }
            }
            
            if (usuario.getNombreUsuario() == null || usuario.getNombreUsuario().trim().isEmpty()) {
                bindingResult.rejectValue("nombreUsuario", "error.nombreUsuario", "Campo NOMBRE DE USUARIO: El nombre de usuario es obligatorio");
            } else if (usuario.getNombreUsuario().trim().length() < 3) {
                bindingResult.rejectValue("nombreUsuario", "error.nombreUsuario", "Campo NOMBRE DE USUARIO: Debe tener al menos 3 caracteres");
            } else if (!usuario.getNombreUsuario().matches("^[a-zA-Z0-9._-]+$")) {
                bindingResult.rejectValue("nombreUsuario", "error.nombreUsuario", "Campo NOMBRE DE USUARIO: Solo se permiten letras, números, puntos, guiones y guiones bajos");
            }
            
            if (usuario.getContraseña() == null || usuario.getContraseña().trim().isEmpty()) {
                bindingResult.rejectValue("contraseña", "error.contraseña", "Campo CONTRASEÑA: La contraseña es obligatoria");
            } else if (usuario.getContraseña().length() < 6) {
                bindingResult.rejectValue("contraseña", "error.contraseña", "Campo CONTRASEÑA: Debe tener al menos 6 caracteres");
            }
            
            if (usuario.getRol() == null) {
                bindingResult.rejectValue("rol", "error.rol", "Campo ROL: Debe seleccionar un rol de usuario");
            }
            
            // Validar errores de validación
            if (bindingResult.hasErrors()) {
                System.out.println("=== ERRORES DE VALIDACIÓN ENCONTRADOS ===");
                bindingResult.getAllErrors().forEach(error -> {
                    System.out.println("Error: " + error.getDefaultMessage());
                });
                
                // Crear mensaje detallado de errores
                StringBuilder errorMessage = new StringBuilder("⚠️ Se encontraron errores en el formulario:\n\n");
                bindingResult.getAllErrors().forEach(error -> {
                    errorMessage.append("• ").append(error.getDefaultMessage()).append("\n");
                });
                
                model.addAttribute("error", errorMessage.toString());
                model.addAttribute("usuario", usuario);
                return "registro";
            }

            // Validar si el usuario ya existe
            if (usuarioService.existeUsuario(usuario.getNombreUsuario())) {
                model.addAttribute("error", "Campo NOMBRE DE USUARIO: El nombre de usuario ya existe. Por favor, elija otro.");
                model.addAttribute("usuario", usuario);
                return "registro";
            }

            // Validar si el correo ya existe
            if (usuarioService.existeCorreo(usuario.getCorreoElectronico())) {
                model.addAttribute("error", "Campo CORREO ELECTRÓNICO: El correo electrónico ya está registrado. Por favor, use otro correo.");
                model.addAttribute("usuario", usuario);
                return "registro";
            }

            // Procesar imagen si fue subida
            if (foto != null && !foto.isEmpty()) {
                // Validar que sea una imagen
                if (!foto.getContentType().startsWith("image/")) {
                    model.addAttribute("error", "Campo FOTO: Por favor, seleccione un archivo de imagen válido (JPG, PNG, GIF, etc.).");
                    model.addAttribute("usuario", usuario);
                    return "registro";
                }
                // Validar tamaño de imagen (máximo 5MB)
                if (foto.getSize() > 5 * 1024 * 1024) {
                    model.addAttribute("error", "Campo FOTO: La imagen es demasiado grande. El tamaño máximo permitido es 5MB.");
                    model.addAttribute("usuario", usuario);
                    return "registro";
                }
                usuario.setFoto(foto.getBytes());
            }

            // Guardar usuario
            usuarioService.registrarUsuario(usuario);
            model.addAttribute("mensaje", "Usuario registrado exitosamente. Ya puede iniciar sesión.");
            return "redirect:/login?registro=exitoso";

        } catch (IOException e) {
            model.addAttribute("error", "Error al procesar la imagen. Por favor, intente nuevamente.");
            model.addAttribute("usuario", usuario);
            return "registro";
        } catch (Exception e) {
            System.out.println("Error inesperado: " + e.getMessage());
            e.printStackTrace();
            model.addAttribute("error", "Ocurrió un error inesperado. Por favor, intente nuevamente. Error: " + e.getMessage());
            model.addAttribute("usuario", usuario);
            return "registro";
        }
    }
}
