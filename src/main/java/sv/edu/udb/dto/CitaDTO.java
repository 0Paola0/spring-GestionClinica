package sv.edu.udb.dto;

import java.time.LocalDate;
import java.time.LocalTime;

public class CitaDTO {
    private Long id;
    private String nombrePaciente;
    private String nombreDoctor;
    private String fecha;
    private String hora;
    private String tipoCita;
    private String estado;
    private String telefonoContacto;
    private String duracion;
    private String motivoConsulta;
    private String observaciones;

    // Constructores
    public CitaDTO() {}

    public CitaDTO(Long id, String nombrePaciente, String nombreDoctor, String fecha, String hora, 
                  String tipoCita, String estado, String telefonoContacto, String duracion, 
                  String motivoConsulta, String observaciones) {
        this.id = id;
        this.nombrePaciente = nombrePaciente;
        this.nombreDoctor = nombreDoctor;
        this.fecha = fecha;
        this.hora = hora;
        this.tipoCita = tipoCita;
        this.estado = estado;
        this.telefonoContacto = telefonoContacto;
        this.duracion = duracion;
        this.motivoConsulta = motivoConsulta;
        this.observaciones = observaciones;
    }

    // Getters y Setters
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getNombrePaciente() {
        return nombrePaciente;
    }

    public void setNombrePaciente(String nombrePaciente) {
        this.nombrePaciente = nombrePaciente;
    }

    public String getNombreDoctor() {
        return nombreDoctor;
    }

    public void setNombreDoctor(String nombreDoctor) {
        this.nombreDoctor = nombreDoctor;
    }

    public String getFecha() {
        return fecha;
    }

    public void setFecha(String fecha) {
        this.fecha = fecha;
    }

    public String getHora() {
        return hora;
    }

    public void setHora(String hora) {
        this.hora = hora;
    }

    public String getTipoCita() {
        return tipoCita;
    }

    public void setTipoCita(String tipoCita) {
        this.tipoCita = tipoCita;
    }

    public String getEstado() {
        return estado;
    }

    public void setEstado(String estado) {
        this.estado = estado;
    }

    public String getTelefonoContacto() {
        return telefonoContacto;
    }

    public void setTelefonoContacto(String telefonoContacto) {
        this.telefonoContacto = telefonoContacto;
    }

    public String getDuracion() {
        return duracion;
    }

    public void setDuracion(String duracion) {
        this.duracion = duracion;
    }

    public String getMotivoConsulta() {
        return motivoConsulta;
    }

    public void setMotivoConsulta(String motivoConsulta) {
        this.motivoConsulta = motivoConsulta;
    }

    public String getObservaciones() {
        return observaciones;
    }

    public void setObservaciones(String observaciones) {
        this.observaciones = observaciones;
    }
}
