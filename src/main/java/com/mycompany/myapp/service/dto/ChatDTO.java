package com.mycompany.myapp.service.dto;

import com.mycompany.myapp.domain.Messages;
import com.mycompany.myapp.domain.User;

import java.time.ZonedDateTime;
import java.util.List;
import java.util.Set;

/**
 * Created by jotabono on 18/1/17.
 */
public class ChatDTO {


    private Long id;

    private String name;

    private ZonedDateTime creationDate;

    private User owner;

    private Set<User> users;

    private List<Messages> messages;

    public ChatDTO(Long id, String name, ZonedDateTime creationDate, User owner, Set<User> users, List<Messages> messages) {
        this.id = id;
        this.name = name;
        this.creationDate = creationDate;
        this.owner = owner;
        this.users = users;
        this.messages = messages;
    }
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }
    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public ZonedDateTime getCreationDate() {
        return creationDate;
    }

    public void setCreationDate(ZonedDateTime creationDate) {
        this.creationDate = creationDate;
    }

    public User getOwner() {
        return owner;
    }

    public void setOwner(User owner) {
        this.owner = owner;
    }

    public Set<User> getUsers() {
        return users;
    }

    public void setUsers(Set<User> users) {
        this.users = users;
    }

    public List<Messages> getMessages() {
        return messages;
    }

    public void setMessages(List<Messages> messages) {
        this.messages = messages;
    }

    @Override
    public String toString() {
        return "ChatDTO{" +
            "id '" + id + '\'' +
            "name='" + name + '\'' +
            ", creationDate=" + creationDate +
            ", owner=" + owner +
            ", users=" + users +
            ", messages=" + messages +
            '}';
    }
}
