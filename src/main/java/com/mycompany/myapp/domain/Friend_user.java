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
 * A Friend_user.
 */
@Entity
@Table(name = "friend_user")
@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
@Document(indexName = "friend_user")
public class Friend_user implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @Column(name = "friendship")
    private Boolean friendship;

    @Column(name = "friendship_date")
    private ZonedDateTime friendship_date;

    @ManyToOne
    @NotNull
    private User friend_from;

    @ManyToOne
    @NotNull
    private User friend_to;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Boolean isFriendship() {
        return friendship;
    }

    public Friend_user friendship(Boolean friendship) {
        this.friendship = friendship;
        return this;
    }

    public void setFriendship(Boolean friendship) {
        this.friendship = friendship;
    }

    public ZonedDateTime getFriendship_date() {
        return friendship_date;
    }

    public Friend_user friendship_date(ZonedDateTime friendship_date) {
        this.friendship_date = friendship_date;
        return this;
    }

    public void setFriendship_date(ZonedDateTime friendship_date) {
        this.friendship_date = friendship_date;
    }

    public User getFriend_from() {
        return friend_from;
    }

    public Friend_user friend_from(User user) {
        this.friend_from = user;
        return this;
    }

    public void setFriend_from(User user) {
        this.friend_from = user;
    }

    public User getFriend_to() {
        return friend_to;
    }

    public Friend_user friend_to(User user) {
        this.friend_to = user;
        return this;
    }

    public void setFriend_to(User user) {
        this.friend_to = user;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        Friend_user friend_user = (Friend_user) o;
        if(friend_user.id == null || id == null) {
            return false;
        }
        return Objects.equals(id, friend_user.id);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(id);
    }

    @Override
    public String toString() {
        return "Friend_user{" +
            "id=" + id +
            ", friendship='" + friendship + "'" +
            ", friendship_date='" + friendship_date + "'" +
            '}';
    }
}
