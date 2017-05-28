package com.mycompany.myapp.domain;

import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;
import org.springframework.data.elasticsearch.annotations.Document;

import javax.persistence.*;
import java.io.Serializable;
import java.time.ZonedDateTime;
import java.util.Objects;

/**
 * A Friendship_notification.
 */
@Entity
@Table(name = "friendship_notification")
@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
@Document(indexName = "friendship_notification")
public class Friendship_notification implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @Column(name = "fecha_recibida")
    private ZonedDateTime fechaRecibida;

    @Column(name = "leida")
    private Boolean leida;

    @ManyToOne
    private User receptor;

    @ManyToOne
    private Friend_user friendship;

    @ManyToOne
    private User emisor;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public ZonedDateTime getFechaRecibida() {
        return fechaRecibida;
    }

    public Friendship_notification fechaRecibida(ZonedDateTime fechaRecibida) {
        this.fechaRecibida = fechaRecibida;
        return this;
    }

    public void setFechaRecibida(ZonedDateTime fechaRecibida) {
        this.fechaRecibida = fechaRecibida;
    }

    public Boolean isLeida() {
        return leida;
    }

    public Friendship_notification leida(Boolean leida) {
        this.leida = leida;
        return this;
    }

    public void setLeida(Boolean leida) {
        this.leida = leida;
    }

    public User getReceptor() {
        return receptor;
    }

    public Friendship_notification receptor(User user) {
        this.receptor = user;
        return this;
    }

    public void setReceptor(User user) {
        this.receptor = user;
    }

    public Friend_user getFriendship() {
        return friendship;
    }

    public Friendship_notification friendship(Friend_user friend_user) {
        this.friendship = friend_user;
        return this;
    }

    public void setFriendship(Friend_user friend_user) {
        this.friendship = friend_user;
    }

    public User getEmisor() {
        return emisor;
    }

    public Friendship_notification emisor(User user) {
        this.emisor = user;
        return this;
    }

    public void setEmisor(User user) {
        this.emisor = user;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        Friendship_notification friendship_notification = (Friendship_notification) o;
        if(friendship_notification.id == null || id == null) {
            return false;
        }
        return Objects.equals(id, friendship_notification.id);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(id);
    }

    @Override
    public String toString() {
        return "Friendship_notification{" +
            "id=" + id +
            ", fechaRecibida='" + fechaRecibida + "'" +
            ", leida='" + leida + "'" +
            '}';
    }
}
