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
    private Centro centro_estudios;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public LocalDate getFechaInicio() {
        return fechaInicio;
    }

    public Estudios fechaInicio(LocalDate fechaInicio) {
        this.fechaInicio = fechaInicio;
        return this;
    }

    public void setFechaInicio(LocalDate fechaInicio) {
        this.fechaInicio = fechaInicio;
    }

    public LocalDate getFechaFinal() {
        return fechaFinal;
    }

    public Estudios fechaFinal(LocalDate fechaFinal) {
        this.fechaFinal = fechaFinal;
        return this;
    }

    public void setFechaFinal(LocalDate fechaFinal) {
        this.fechaFinal = fechaFinal;
    }

    public Boolean isActualmente() {
        return actualmente;
    }

    public Estudios actualmente(Boolean actualmente) {
        this.actualmente = actualmente;
        return this;
    }

    public void setActualmente(Boolean actualmente) {
        this.actualmente = actualmente;
    }

    public String getCurso() {
        return curso;
    }

    public Estudios curso(String curso) {
        this.curso = curso;
        return this;
    }

    public void setCurso(String curso) {
        this.curso = curso;
    }

    public Float getNota() {
        return nota;
    }

    public Estudios nota(Float nota) {
        this.nota = nota;
        return this;
    }

    public void setNota(Float nota) {
        this.nota = nota;
    }

    public String getDescripcion() {
        return descripcion;
    }

    public Estudios descripcion(String descripcion) {
        this.descripcion = descripcion;
        return this;
    }

    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }

    public byte[] getArchivos() {
        return archivos;
    }

    public Estudios archivos(byte[] archivos) {
        this.archivos = archivos;
        return this;
    }

    public void setArchivos(byte[] archivos) {
        this.archivos = archivos;
    }

    public String getArchivosContentType() {
        return archivosContentType;
    }

    public Estudios archivosContentType(String archivosContentType) {
        this.archivosContentType = archivosContentType;
        return this;
    }

    public void setArchivosContentType(String archivosContentType) {
        this.archivosContentType = archivosContentType;
    }

    public String getLink() {
        return link;
    }

    public Estudios link(String link) {
        this.link = link;
        return this;
    }

    public void setLink(String link) {
        this.link = link;
    }

    public Centro getCentro_estudios() {
        return centro_estudios;
    }

    public Estudios centro_estudios(Centro centro) {
        this.centro_estudios = centro;
        return this;
    }

    public void setCentro_estudios(Centro centro) {
        this.centro_estudios = centro;
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
