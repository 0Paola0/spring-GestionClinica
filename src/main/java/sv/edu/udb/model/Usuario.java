package sv.edu.udb.model;
import jakarta.persistence.*;
import lombok.Data;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import jakarta.validation.constraints.Past;
import java.time.LocalDate;

@Entity
@Table(name = "usuarios")
@Data
public class Usuario {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    // Datos personales
    @NotBlank(message = "Los nombres son obligatorios")
    @Size(max = 100, message = "Los nombres no pueden exceder 100 caracteres")
    @Column(name = "nombres", length = 100)
    private String nombres;

    @NotBlank(message = "Los apellidos son obligatorios")
    @Size(max = 100, message = "Los apellidos no pueden exceder 100 caracteres")
    @Column(name = "apellidos", length = 100)
    private String apellidos;

    @NotBlank(message = "El tipo de documento es obligatorio")
    @Size(max = 50, message = "El tipo de documento no puede exceder 50 caracteres")
    @Column(name = "tipo_documento", length = 50)
    private String tipoDocumento;

    @NotBlank(message = "El número de documento es obligatorio")
    @Size(max = 20, message = "El número de documento no puede exceder 20 caracteres")
    @Column(name = "numero_documento", length = 20)
    private String numeroDocumento;

    @Past(message = "La fecha de nacimiento debe ser una fecha pasada")
    @Column(name = "fecha_nacimiento")
    private LocalDate fechaNacimiento;

    @Enumerated(EnumType.STRING)
    @Column(name = "genero", columnDefinition = "ENUM('Femenino', 'Masculino')")
    private Genero genero;

    // Información de contacto
    @NotBlank(message = "El correo electrónico es obligatorio")
    @Email(message = "El formato del correo electrónico no es válido")
    @Size(max = 100, message = "El correo electrónico no puede exceder 100 caracteres")
    @Column(name = "correo_electronico", length = 100)
    private String correoElectronico;

    @NotBlank(message = "El teléfono celular es obligatorio")
    @Size(max = 20, message = "El teléfono celular no puede exceder 20 caracteres")
    @Column(name = "telefono_celular", length = 20)
    private String telefonoCelular;

    @Size(max = 20, message = "El teléfono alternativo no puede exceder 20 caracteres")
    @Column(name = "telefono_alternativo", length = 20)
    private String telefonoAlternativo;

    @Size(max = 200, message = "La dirección no puede exceder 200 caracteres")
    @Column(name = "direccion", length = 200)
    private String direccion;

    // Acceso al sistema
    @NotBlank(message = "El nombre de usuario es obligatorio")
    @Size(max = 50, message = "El nombre de usuario no puede exceder 50 caracteres")
    @Column(name = "usuario", length = 50, unique = true)
    private String nombreUsuario;

    @NotBlank(message = "La contraseña es obligatoria")
    @Size(max = 200, message = "La contraseña no puede exceder 200 caracteres")
    @Column(name = "password", length = 200)
    private String contraseña;

    @NotNull(message = "El rol es obligatorio")
    @Enumerated(EnumType.STRING)
    @Column(name = "rol", columnDefinition = "ENUM('Administrador', 'Medico')")
    private Rol rol;

    @Lob
    @Column(name = "foto", columnDefinition = "LONGBLOB")
    private byte[] foto;

    // Enums para los campos que tienen valores específicos
    public enum Genero {
        Femenino, Masculino
    }

    public enum Rol {
        Administrador, Medico
    }
}
