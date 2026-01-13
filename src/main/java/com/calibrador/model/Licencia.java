package com.calibrador.model;

import java.time.LocalDate;
import java.time.LocalDateTime;

/**
 * Modelo de Licencia
 * Controla los límites y características según el plan contratado
 */
public class Licencia {

    private Integer idLicencia;
    private TipoPlan tipoPlan;
    private LocalDate fechaInicio;
    private LocalDate fechaFin;
    private String hardwareId;
    private String firmaDigital;
    private Integer maxEquipos;
    private Integer maxUsuarios;
    private String funcionesHabilitadas; // JSON con features
    private Boolean activa;
    private LocalDateTime fechaCreacion;

    // Enum para tipos de plan
    public enum TipoPlan {
        FREE("Free", 10, 1),
        PRO("Pro", 100, 10),
        ENTERPRISE("Enterprise", -1, -1); // -1 = ilimitado

        private final String descripcion;
        private final int equiposDefault;
        private final int usuariosDefault;

        TipoPlan(String descripcion, int equiposDefault, int usuariosDefault) {
            this.descripcion = descripcion;
            this.equiposDefault = equiposDefault;
            this.usuariosDefault = usuariosDefault;
        }

        public String getDescripcion() {
            return descripcion;
        }

        public int getEquiposDefault() {
            return equiposDefault;
        }

        public int getUsuariosDefault() {
            return usuariosDefault;
        }
    }

    // Constructor vacío
    public Licencia() {
        this.activa = true;
        this.fechaCreacion = LocalDateTime.now();
    }

    // Constructor con campos básicos
    public Licencia(TipoPlan tipoPlan, LocalDate fechaInicio, LocalDate fechaFin, String hardwareId) {
        this();
        this.tipoPlan = tipoPlan;
        this.fechaInicio = fechaInicio;
        this.fechaFin = fechaFin;
        this.hardwareId = hardwareId;
        this.maxEquipos = tipoPlan.getEquiposDefault();
        this.maxUsuarios = tipoPlan.getUsuariosDefault();
    }

    // Getters y Setters
    public Integer getIdLicencia() {
        return idLicencia;
    }

    public void setIdLicencia(Integer idLicencia) {
        this.idLicencia = idLicencia;
    }

    public TipoPlan getTipoPlan() {
        return tipoPlan;
    }

    public void setTipoPlan(TipoPlan tipoPlan) {
        this.tipoPlan = tipoPlan;
    }

    public LocalDate getFechaInicio() {
        return fechaInicio;
    }

    public void setFechaInicio(LocalDate fechaInicio) {
        this.fechaInicio = fechaInicio;
    }

    public LocalDate getFechaFin() {
        return fechaFin;
    }

    public void setFechaFin(LocalDate fechaFin) {
        this.fechaFin = fechaFin;
    }

    public String getHardwareId() {
        return hardwareId;
    }

    public void setHardwareId(String hardwareId) {
        this.hardwareId = hardwareId;
    }

    public String getFirmaDigital() {
        return firmaDigital;
    }

    public void setFirmaDigital(String firmaDigital) {
        this.firmaDigital = firmaDigital;
    }

    public Integer getMaxEquipos() {
        return maxEquipos;
    }

    public void setMaxEquipos(Integer maxEquipos) {
        this.maxEquipos = maxEquipos;
    }

    public Integer getMaxUsuarios() {
        return maxUsuarios;
    }

    public void setMaxUsuarios(Integer maxUsuarios) {
        this.maxUsuarios = maxUsuarios;
    }

    public String getFuncionesHabilitadas() {
        return funcionesHabilitadas;
    }

    public void setFuncionesHabilitadas(String funcionesHabilitadas) {
        this.funcionesHabilitadas = funcionesHabilitadas;
    }

    public Boolean getActiva() {
        return activa;
    }

    public void setActiva(Boolean activa) {
        this.activa = activa;
    }

    public LocalDateTime getFechaCreacion() {
        return fechaCreacion;
    }

    public void setFechaCreacion(LocalDateTime fechaCreacion) {
        this.fechaCreacion = fechaCreacion;
    }

    // Métodos auxiliares

    /**
     * Verifica si la licencia está vigente
     */
    public boolean estaVigente() {
        if (!activa) return false;
        LocalDate hoy = LocalDate.now();
        return !hoy.isBefore(fechaInicio) && !hoy.isAfter(fechaFin);
    }

    /**
     * Verifica si está próxima a vencer (30 días)
     */
    public boolean estaProximaAVencer() {
        if (fechaFin == null) return false;
        LocalDate limite = LocalDate.now().plusDays(30);
        return fechaFin.isBefore(limite);
    }

    /**
     * Obtiene días restantes de vigencia
     */
    public long getDiasRestantes() {
        if (fechaFin == null) return 0;
        return java.time.temporal.ChronoUnit.DAYS.between(LocalDate.now(), fechaFin);
    }

    /**
     * Verifica si tiene equipos ilimitados
     */
    public boolean tieneEquiposIlimitados() {
        return maxEquipos != null && maxEquipos == -1;
    }

    /**
     * Verifica si tiene usuarios ilimitados
     */
    public boolean tieneUsuariosIlimitados() {
        return maxUsuarios != null && maxUsuarios == -1;
    }

    /**
     * Verifica si es plan FREE
     */
    public boolean esFree() {
        return tipoPlan == TipoPlan.FREE;
    }

    /**
     * Verifica si es plan PRO
     */
    public boolean esPro() {
        return tipoPlan == TipoPlan.PRO;
    }

    /**
     * Verifica si es plan ENTERPRISE
     */
    public boolean esEnterprise() {
        return tipoPlan == TipoPlan.ENTERPRISE;
    }

    @Override
    public String toString() {
        return "Licencia{" +
                "idLicencia=" + idLicencia +
                ", tipoPlan=" + tipoPlan +
                ", fechaInicio=" + fechaInicio +
                ", fechaFin=" + fechaFin +
                ", activa=" + activa +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Licencia licencia = (Licencia) o;
        return idLicencia != null && idLicencia.equals(licencia.idLicencia);
    }

    @Override
    public int hashCode() {
        return idLicencia != null ? idLicencia.hashCode() : 0;
    }
}
