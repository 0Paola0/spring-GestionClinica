package sv.edu.udb.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

import java.sql.Date;
import java.sql.Time;
import java.sql.Timestamp;

@Entity
@Table(name = "Citas")
@Data
public class Cita {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank(message = "El nombre del paciente es obligatorio")
    @Column(name = "nombre_paciente", length = 100)
    private String nombrePaciente;

    @NotBlank(message = "El nombre del doctor es obligatorio")
    @Column(name = "nombre_doctor", length = 100)
    private String nombreDoctor;

    @NotNull(message = "La fecha es obligatoria")
    @Column(name = "fecha")
    private Date fecha;

    @NotNull(message = "La hora es obligatoria")
    @Column(name = "hora")
    private Time hora;

    @Column(name = "tipo_cita")
    private String tipoCita;

    @Column(name = "estado")
    private String estado;

    @Column(name = "telefono_contacto", length = 15)
    private String telefonoContacto;

    @Column(name = "duracion", length = 20)
    private String duracion;

    @Column(name = "motivo_consulta", columnDefinition = "TEXT")
    private String motivoConsulta;

    @Column(name = "observaciones", columnDefinition = "TEXT")
    private String observaciones;

    @Column(name = "fecha_creacion")
    private Timestamp fechaCreacion;

    public enum TipoCita {
        CONSULTA_GENERAL("Consulta general"),
        LABORATORIO_CLINICO("Laboratorio clínico"),
        FARMACIA("Farmacia"),
        ESPECIALIDAD_MEDICA("Especialidad médica");

        private final String descripcion;

        TipoCita(String descripcion) {
            this.descripcion = descripcion;
        }

        public String getDescripcion() {
            return descripcion;
        }
    }

    public enum EstadoCita {
        PROGRAMADA("Programada"),
        CANCELADA("Cancelada"),
        PENDIENTE("Pendiente"),
        RETRASADA("Retrasada");

        private final String descripcion;

        EstadoCita(String descripcion) {
            this.descripcion = descripcion;
        }

        public String getDescripcion() {
            return descripcion;
        }
    }
}
