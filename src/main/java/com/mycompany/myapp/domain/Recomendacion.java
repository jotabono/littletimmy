package com.mycompany.myapp.domain;

import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;
import org.springframework.data.elasticsearch.annotations.Document;

import javax.persistence.*;
import javax.validation.constraints.*;
import java.io.Serializable;
import java.time.ZonedDateTime;
import java.util.Objects;

/**
 * A Recomendacion.
 */
@Entity
@Table(name = "recomendacion")
@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
@Document(indexName = "recomendacion")
public class Recomendacion implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @Column(name = "texto")
    private String texto;

    @Column(name = "fecha_envio")
    private ZonedDateTime fechaEnvio;

    @Column(name = "fecha_resolucion")
    private ZonedDateTime fechaResolucion;

    @Column(name = "aceptada")
    private Boolean aceptada;

    @ManyToOne
    @NotNull
    private User recomendador;

    @ManyToOne
    @NotNull
    private User recomendado;

    @ManyToOne
    @NotNull
    private Trabajo trabajo;

    @ManyToOne
    @NotNull
    private Empresa empresa;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getTexto() {
        return texto;
    }

    public Recomendacion texto(String texto) {
        this.texto = texto;
        return this;
    }

    public void setTexto(String texto) {
        this.texto = texto;
    }

    public ZonedDateTime getFechaEnvio() {
        return fechaEnvio;
    }

    public Recomendacion fechaEnvio(ZonedDateTime fechaEnvio) {
        this.fechaEnvio = fechaEnvio;
        return this;
    }

    public void setFechaEnvio(ZonedDateTime fechaEnvio) {
        this.fechaEnvio = fechaEnvio;
    }

    public ZonedDateTime getFechaResolucion() {
        return fechaResolucion;
    }

    public Recomendacion fechaResolucion(ZonedDateTime fechaResolucion) {
        this.fechaResolucion = fechaResolucion;
        return this;
    }

    public void setFechaResolucion(ZonedDateTime fechaResolucion) {
        this.fechaResolucion = fechaResolucion;
    }

    public Boolean isAceptada() {
        return aceptada;
    }

    public Recomendacion aceptada(Boolean aceptada) {
        this.aceptada = aceptada;
        return this;
    }

    public void setAceptada(Boolean aceptada) {
        this.aceptada = aceptada;
    }

    public User getRecomendador() {
        return recomendador;
    }

    public Recomendacion recomendador(User user) {
        this.recomendador = user;
        return this;
    }

    public void setRecomendador(User user) {
        this.recomendador = user;
    }

    public User getRecomendado() {
        return recomendado;
    }

    public Recomendacion recomendado(User user) {
        this.recomendado = user;
        return this;
    }

    public void setRecomendado(User user) {
        this.recomendado = user;
    }

    public Trabajo getTrabajo() {
        return trabajo;
    }

    public Recomendacion trabajo(Trabajo trabajo) {
        this.trabajo = trabajo;
        return this;
    }

    public void setTrabajo(Trabajo trabajo) {
        this.trabajo = trabajo;
    }

    public Empresa getEmpresa() {
        return empresa;
    }

    public Recomendacion empresa(Empresa empresa) {
        this.empresa = empresa;
        return this;
    }

    public void setEmpresa(Empresa empresa) {
        this.empresa = empresa;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        Recomendacion recomendacion = (Recomendacion) o;
        if(recomendacion.id == null || id == null) {
            return false;
        }
        return Objects.equals(id, recomendacion.id);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(id);
    }

    @Override
    public String toString() {
        return "Recomendacion{" +
            "id=" + id +
            ", texto='" + texto + "'" +
            ", fechaEnvio='" + fechaEnvio + "'" +
            ", fechaResolucion='" + fechaResolucion + "'" +
            ", aceptada='" + aceptada + "'" +
            '}';
    }
}
