package sv.edu.udb.model;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Entity
@Table(name = "pacientes")
@Data
public class Paciente {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "codigo", length = 20, unique = true)
    private String codigo;

    @NotBlank(message = "Los nombres son obligatorios")
    @Column(name = "nombres", length = 100)
    private String nombres;

    @NotBlank(message = "Los apellidos son obligatorios")
    @Column(name = "apellidos", length = 100)
    private String apellidos;

    @NotNull(message = "La edad es obligatoria")
    @Column(name = "edad")
    private Integer edad;

    @NotBlank(message = "El teléfono es obligatorio")
    @Column(name = "telefono", length = 20)
    private String telefono;

    @NotBlank(message = "El motivo de consulta es obligatorio")
    @Column(name = "motivo_consulta", columnDefinition = "TEXT")
    private String motivoConsulta;

    @NotBlank(message = "La enfermedad actual es obligatoria")
    @Column(name = "enfermedad_actual", columnDefinition = "TEXT")
    private String enfermedadActual;

    @NotNull(message = "Debe indicar si el paciente tiene diabetes")
    @Column(name = "diabetes")
    private Boolean diabetes;

    @NotBlank(message = "Debe proporcionar detalles sobre la diabetes")
    @Column(name = "detalle_diabetes", columnDefinition = "TEXT")
    private String detalleDiabetes;

    @NotNull(message = "Debe indicar si el paciente tiene hipertensión")
    @Column(name = "hipertension")
    private Boolean hipertension;

    @NotBlank(message = "Debe proporcionar detalles sobre la hipertensión")
    @Column(name = "detalle_hipertension", columnDefinition = "TEXT")
    private String detalleHipertension;

    @NotNull(message = "Debe indicar si el paciente tiene alergias")
    @Column(name = "alergias")
    private Boolean alergias;

    @NotBlank(message = "Debe proporcionar detalles sobre las alergias")
    @Column(name = "detalle_alergias", columnDefinition = "TEXT")
    private String detalleAlergias;

    @Column(name = "cirugias_previas")
    private Boolean cirugiasPrevias;

    @NotBlank(message = "Debe proporcionar detalles sobre las cirugías previas")
    @Column(name = "detalle_cirugias", columnDefinition = "TEXT")
    private String detalleCirugias;

    @Column(name = "medicamentos_actuales")
    private Boolean medicamentosActuales;

    @NotBlank(message = "Debe proporcionar detalles sobre los medicamentos actuales")
    @Column(name = "detalle_medicamentos", columnDefinition = "TEXT")
    private String detalleMedicamentos;

    @Column(name = "foto")
    @Lob
    private byte[] foto;

    @Column(name = "fecha_registro")
    private java.sql.Timestamp fechaRegistro;

}
