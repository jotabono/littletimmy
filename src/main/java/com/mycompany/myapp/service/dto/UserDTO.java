package com.mycompany.myapp.service.dto;

import com.mycompany.myapp.config.Constants;

import com.mycompany.myapp.domain.Authority;
import com.mycompany.myapp.domain.User;

import org.hibernate.validator.constraints.Email;

import javax.validation.constraints.*;
import java.time.LocalDate;
import java.time.ZonedDateTime;
import java.util.Set;
import java.util.stream.Collectors;
/**
 * A DTO representing a user, with his authorities.
 */
public class UserDTO {

    @NotNull
    @Pattern(regexp = Constants.LOGIN_REGEX)
    @Size(min = 1, max = 50)
    private String login;

    @Size(max = 50)
    private String firstName;

    @Size(max = 50)
    private String lastName;

    @Email
    @Size(min = 5, max = 100)
    private String email;

    private String imagen;

    private LocalDate fecha_nacimiento;

    private String dni;

    private String telefono;

    private String domicilio;

    private String web_personal;

    private String facebook;

    private String twitter;

    private String skype;

    private String ciudad;

    private String github;

    @Email
    @Size(min = 5, max = 100)
    private String correo_alternativo;

    private String carta_presentacion;

    private boolean activated = false;

    @Size(min = 2, max = 5)
    private String langKey;

    private Set<String> authorities;

    public UserDTO() {
    }

    public UserDTO(User user) {
        this(user.getLogin(), user.getFirstName(), user.getLastName(),
            user.getEmail(), user.getActivated(), user.getLangKey(),
            user.getAuthorities().stream().map(Authority::getName)
                .collect(Collectors.toSet()), user.getImagen(), user.getFecha_nacimiento(), user.getDni(), user.getTelefono(), user.getDomicilio(),
            user.getWeb_personal(), user.getFacebook(), user.getTwitter(), user.getSkype(), user.getCorreo_alternativo(), user.getCarta_presentacion(), user.getCiudad(), user.getGithub());
    }

    public UserDTO(String login, String firstName, String lastName,
                   String email, boolean activated, String langKey, Set<String> authorities, String imagen, LocalDate fecha_nacimiento,
                   String dni, String telefono, String domicilio, String web_personal, String facebook, String twitter, String skype,
                   String correo_alternativo, String carta_presentacion, String ciudad, String github) {

        this.login = login;
        this.firstName = firstName;
        this.lastName = lastName;
        this.email = email;
        this.activated = activated;
        this.langKey = langKey;
        this.authorities = authorities;
        this.imagen = imagen;
        this.fecha_nacimiento = fecha_nacimiento;
        this.dni = dni;
        this.telefono = telefono;
        this.domicilio = domicilio;
        this.web_personal = web_personal;
        this.facebook = facebook;
        this.twitter = twitter;
        this.skype = skype;
        this.correo_alternativo = correo_alternativo;
        this.carta_presentacion = carta_presentacion;
        this.ciudad = ciudad;
        this.github = github;
    }

    public String getLogin() {
        return login;
    }

    public String getFirstName() {
        return firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public String getEmail() {
        return email;
    }

    public boolean isActivated() {
        return activated;
    }

    public String getLangKey() {
        return langKey;
    }

    public Set<String> getAuthorities() {
        return authorities;
    }

    public String getImagen() {
        return imagen;
    }

    public LocalDate getFecha_nacimiento() {
        return fecha_nacimiento;
    }

    public String getDni() {
        return dni;
    }

    public String getTelefono() {
        return telefono;
    }

    public String getDomicilio() {
        return domicilio;
    }

    public String getWeb_personal() {
        return web_personal;
    }

    public String getFacebook() {
        return facebook;
    }

    public String getTwitter() {
        return twitter;
    }

    public String getSkype() {
        return skype;
    }

    public String getGithub() {
        return github;
    }

    public void setGithub(String github) {
        this.github = github;
    }

    public String getCorreo_alternativo() {
        return correo_alternativo;
    }

    public String getCarta_presentacion() {
        return carta_presentacion;
    }

    public String getCiudad() { return ciudad; }

    /*@Override
    public String toString() {
        return "UserDTO{" +
            "login='" + login + '\'' +
            ", firstName='" + firstName + '\'' +
            ", lastName='" + lastName + '\'' +
            ", email='" + email + '\'' +
            ", activated=" + activated +
            ", langKey='" + langKey + '\'' +
            ", authorities=" + authorities +
            "}";
    }*/

    @Override
    public String toString() {
        return "UserDTO{" +
            "login='" + login + '\'' +
            ", firstName='" + firstName + '\'' +
            ", lastName='" + lastName + '\'' +
            ", email='" + email + '\'' +
            ", imagen='" + imagen + '\'' +
            ", fecha_nacimiento=" + fecha_nacimiento +
            ", dni='" + dni + '\'' +
            ", telefono='" + telefono + '\'' +
            ", domicilio='" + domicilio + '\'' +
            ", web_personal='" + web_personal + '\'' +
            ", facebook='" + facebook + '\'' +
            ", twitter='" + twitter + '\'' +
            ", skype='" + skype + '\'' +
            ", correo_alternativo='" + correo_alternativo + '\'' +
            ", carta_presentacion='" + carta_presentacion + '\'' +
            ", activated=" + activated +
            ", langKey='" + langKey + '\'' +
            ", authorities=" + authorities +
            ", ciudad=" + ciudad +
            ", github=" + github +
            '}';
    }
}
