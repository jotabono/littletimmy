package com.mycompany.myapp.domain;

import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;
import org.springframework.data.elasticsearch.annotations.Document;

import javax.persistence.*;
import java.io.Serializable;
import java.time.ZonedDateTime;
import java.util.Objects;

/**
 * A Recommend_notification.
 */
@Entity
@Table(name = "recommend_notification")
@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
@Document(indexName = "recommend_notification")
public class Recommend_notification implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @Column(name = "contenido")
    private String contenido;

    @Column(name = "fecha_recibida")
    private ZonedDateTime fechaRecibida;

    @Column(name = "leida")
    private Boolean leida;

    @ManyToOne
    private User remitente;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getContenido() {
        return contenido;
    }

    public Recommend_notification contenido(String contenido) {
        this.contenido = contenido;
        return this;
    }

    public void setContenido(String contenido) {
        this.contenido = contenido;
    }

    public ZonedDateTime getFechaRecibida() {
        return fechaRecibida;
    }

    public Recommend_notification fechaRecibida(ZonedDateTime fechaRecibida) {
        this.fechaRecibida = fechaRecibida;
        return this;
    }

    public void setFechaRecibida(ZonedDateTime fechaRecibida) {
        this.fechaRecibida = fechaRecibida;
    }

    public Boolean isLeida() {
        return leida;
    }

    public Recommend_notification leida(Boolean leida) {
        this.leida = leida;
        return this;
    }

    public void setLeida(Boolean leida) {
        this.leida = leida;
    }

    public User getRemitente() {
        return remitente;
    }

    public Recommend_notification remitente(User user) {
        this.remitente = user;
        return this;
    }

    public void setRemitente(User user) {
        this.remitente = user;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        Recommend_notification recommend_notification = (Recommend_notification) o;
        if(recommend_notification.id == null || id == null) {
            return false;
        }
        return Objects.equals(id, recommend_notification.id);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(id);
    }

    @Override
    public String toString() {
        return "Recommend_notification{" +
            "id=" + id +
            ", contenido='" + contenido + "'" +
            ", fechaRecibida='" + fechaRecibida + "'" +
            ", leida='" + leida + "'" +
            '}';
    }
}
