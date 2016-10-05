package com.mycompany.myapp.domain;

import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;
import org.springframework.data.elasticsearch.annotations.Document;

import javax.persistence.*;
import java.io.Serializable;
import java.time.LocalDate;
import java.util.Objects;

/**
 * A Trabajo.
 */
@Entity
@Table(name = "trabajo")
@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
@Document(indexName = "trabajo")
public class Trabajo implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @Column(name = "cargo")
    private String cargo;

    @Column(name = "fecha_inicio")
    private LocalDate fechaInicio;

    @Column(name = "fecha_fin")
    private LocalDate fechaFin;

    @Column(name = "actualmente")
    private Boolean actualmente;

    @Column(name = "descripcion_cargo")
    private String descripcionCargo;

    @Lob
    @Column(name = "multimedia")
    private byte[] multimedia;

    @Column(name = "multimedia_content_type")
    private String multimediaContentType;

    @ManyToOne
    private Empresa empresa;

    @ManyToOne
    private User trabajador;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getCargo() {
        return cargo;
    }

    public Trabajo cargo(String cargo) {
        this.cargo = cargo;
        return this;
    }

    public void setCargo(String cargo) {
        this.cargo = cargo;
    }

    public LocalDate getFechaInicio() {
        return fechaInicio;
    }

    public Trabajo fechaInicio(LocalDate fechaInicio) {
        this.fechaInicio = fechaInicio;
        return this;
    }

    public void setFechaInicio(LocalDate fechaInicio) {
        this.fechaInicio = fechaInicio;
    }

    public LocalDate getFechaFin() {
        return fechaFin;
    }

    public Trabajo fechaFin(LocalDate fechaFin) {
        this.fechaFin = fechaFin;
        return this;
    }

    public void setFechaFin(LocalDate fechaFin) {
        this.fechaFin = fechaFin;
    }

    public Boolean isActualmente() {
        return actualmente;
    }

    public Trabajo actualmente(Boolean actualmente) {
        this.actualmente = actualmente;
        return this;
    }

    public void setActualmente(Boolean actualmente) {
        this.actualmente = actualmente;
    }

    public String getDescripcionCargo() {
        return descripcionCargo;
    }

    public Trabajo descripcionCargo(String descripcionCargo) {
        this.descripcionCargo = descripcionCargo;
        return this;
    }

    public void setDescripcionCargo(String descripcionCargo) {
        this.descripcionCargo = descripcionCargo;
    }

    public byte[] getMultimedia() {
        return multimedia;
    }

    public Trabajo multimedia(byte[] multimedia) {
        this.multimedia = multimedia;
        return this;
    }

    public void setMultimedia(byte[] multimedia) {
        this.multimedia = multimedia;
    }

    public String getMultimediaContentType() {
        return multimediaContentType;
    }

    public Trabajo multimediaContentType(String multimediaContentType) {
        this.multimediaContentType = multimediaContentType;
        return this;
    }

    public void setMultimediaContentType(String multimediaContentType) {
        this.multimediaContentType = multimediaContentType;
    }

    public Empresa getEmpresa() {
        return empresa;
    }

    public Trabajo empresa(Empresa empresa) {
        this.empresa = empresa;
        return this;
    }

    public void setEmpresa(Empresa empresa) {
        this.empresa = empresa;
    }

    public User getTrabajador() {
        return trabajador;
    }

    public Trabajo trabajador(User user) {
        this.trabajador = user;
        return this;
    }

    public void setTrabajador(User user) {
        this.trabajador = user;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        Trabajo trabajo = (Trabajo) o;
        if(trabajo.id == null || id == null) {
            return false;
        }
        return Objects.equals(id, trabajo.id);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(id);
    }

    @Override
    public String toString() {
        return "Trabajo{" +
            "id=" + id +
            ", cargo='" + cargo + "'" +
            ", fechaInicio='" + fechaInicio + "'" +
            ", fechaFin='" + fechaFin + "'" +
            ", actualmente='" + actualmente + "'" +
            ", descripcionCargo='" + descripcionCargo + "'" +
            ", multimedia='" + multimedia + "'" +
            ", multimediaContentType='" + multimediaContentType + "'" +
            '}';
    }
}
