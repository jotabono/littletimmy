package com.mycompany.myapp.domain;

import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;
import org.springframework.data.elasticsearch.annotations.Document;

import javax.persistence.*;
import java.io.Serializable;
import java.time.LocalDate;
import java.util.Objects;

/**
 * A Estudios.
 */
@Entity
@Table(name = "estudios")
@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
@Document(indexName = "estudios")
public class Estudios implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @Column(name = "fecha_inicio")
    private LocalDate fechaInicio;

    @Column(name = "fecha_final")
    private LocalDate fechaFinal;

    @Column(name = "actualmente")
    private Boolean actualmente;

    @Column(name = "curso")
    private String curso;

    @Column(name = "nota")
    private Float nota;

    @Column(name = "descripcion")
    private String descripcion;

    @Lob
    @Column(name = "archivos")
    private byte[] archivos;

    @Column(name = "archivos_content_type")    
    private String archivosContentType;

    @Column(name = "link")
    private String link;

    @ManyToOne
    private Centro centro;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public LocalDate getFechaInicio() {
        return fechaInicio;
    }

    public void setFechaInicio(LocalDate fechaInicio) {
        this.fechaInicio = fechaInicio;
    }

    public LocalDate getFechaFinal() {
        return fechaFinal;
    }

    public void setFechaFinal(LocalDate fechaFinal) {
        this.fechaFinal = fechaFinal;
    }

    public Boolean isActualmente() {
        return actualmente;
    }

    public void setActualmente(Boolean actualmente) {
        this.actualmente = actualmente;
    }

    public String getCurso() {
        return curso;
    }

    public void setCurso(String curso) {
        this.curso = curso;
    }

    public Float getNota() {
        return nota;
    }

    public void setNota(Float nota) {
        this.nota = nota;
    }

    public String getDescripcion() {
        return descripcion;
    }

    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }

    public byte[] getArchivos() {
        return archivos;
    }

    public void setArchivos(byte[] archivos) {
        this.archivos = archivos;
    }

    public String getArchivosContentType() {
        return archivosContentType;
    }

    public void setArchivosContentType(String archivosContentType) {
        this.archivosContentType = archivosContentType;
    }

    public String getLink() {
        return link;
    }

    public void setLink(String link) {
        this.link = link;
    }

    public Centro getCentro() {
        return centro;
    }

    public void setCentro(Centro centro) {
        this.centro = centro;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        Estudios estudios = (Estudios) o;
        if(estudios.id == null || id == null) {
            return false;
        }
        return Objects.equals(id, estudios.id);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(id);
    }

    @Override
    public String toString() {
        return "Estudios{" +
            "id=" + id +
            ", fechaInicio='" + fechaInicio + "'" +
            ", fechaFinal='" + fechaFinal + "'" +
            ", actualmente='" + actualmente + "'" +
            ", curso='" + curso + "'" +
            ", nota='" + nota + "'" +
            ", descripcion='" + descripcion + "'" +
            ", archivos='" + archivos + "'" +
            ", archivosContentType='" + archivosContentType + "'" +
            ", link='" + link + "'" +
            '}';
    }
}
