package sv.edu.udb.model;

import jakarta.persistence.*;
import lombok.Data;
import java.time.LocalDate;

@Data
@Entity
@Table(name = "laboratorio")
public class Laboratorio {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "nombre_paciente", nullable = false, length = 100)
    private String nombrePaciente;

    @Column(name = "examen_especifico", nullable = false, length = 100)
    private String examenEspecifico;

    @Column(name = "tipo_examen", nullable = false, length = 50)
    private String tipoExamen;

    @Column(name = "estado", nullable = false, length = 50)
    private String estado;

    @Column(name = "fecha_programacion")
    private LocalDate fechaProgramacion;

    @Column(name = "notas", columnDefinition = "TEXT")
    private String notas;
}

