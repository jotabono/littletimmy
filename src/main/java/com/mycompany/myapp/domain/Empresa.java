package com.mycompany.myapp.domain;

import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;
import org.springframework.data.elasticsearch.annotations.Document;

import javax.persistence.*;
import java.io.Serializable;
import java.time.ZonedDateTime;
import java.util.Objects;

/**
 * A Empresa.
 */
@Entity
@Table(name = "empresa")
@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
@Document(indexName = "empresa")
public class Empresa implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @Column(name = "nombre")
    private String nombre;

    @Column(name = "num_empleados")
    private Integer numEmpleados;

    @Column(name = "fecha_fundacion")
    private ZonedDateTime fechaFundacion;

    @Column(name = "ubicacion")
    private String ubicacion;

    @Column(name = "latitud")
    private String latitud;

    @Column(name = "longitud")
    private String longitud;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getNombre() {
        return nombre;
    }

    public Empresa nombre(String nombre) {
        this.nombre = nombre;
        return this;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public Integer getNumEmpleados() {
        return numEmpleados;
    }

    public Empresa numEmpleados(Integer numEmpleados) {
        this.numEmpleados = numEmpleados;
        return this;
    }

    public void setNumEmpleados(Integer numEmpleados) {
        this.numEmpleados = numEmpleados;
    }

    public ZonedDateTime getFechaFundacion() {
        return fechaFundacion;
    }

    public Empresa fechaFundacion(ZonedDateTime fechaFundacion) {
        this.fechaFundacion = fechaFundacion;
        return this;
    }

    public void setFechaFundacion(ZonedDateTime fechaFundacion) {
        this.fechaFundacion = fechaFundacion;
    }

    public String getUbicacion() {
        return ubicacion;
    }

    public Empresa ubicacion(String ubicacion) {
        this.ubicacion = ubicacion;
        return this;
    }

    public void setUbicacion(String ubicacion) {
        this.ubicacion = ubicacion;
    }

    public String getLatitud() {
        return latitud;
    }

    public Empresa latitud(String latitud) {
        this.latitud = latitud;
        return this;
    }

    public void setLatitud(String latitud) {
        this.latitud = latitud;
    }

    public String getLongitud() {
        return longitud;
    }

    public Empresa longitud(String longitud) {
        this.longitud = longitud;
        return this;
    }

    public void setLongitud(String longitud) {
        this.longitud = longitud;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        Empresa empresa = (Empresa) o;
        if(empresa.id == null || id == null) {
            return false;
        }
        return Objects.equals(id, empresa.id);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(id);
    }

    @Override
    public String toString() {
        return "Empresa{" +
            "id=" + id +
            ", nombre='" + nombre + "'" +
            ", numEmpleados='" + numEmpleados + "'" +
            ", fechaFundacion='" + fechaFundacion + "'" +
            ", ubicacion='" + ubicacion + "'" +
            ", latitud='" + latitud + "'" +
            ", longitud='" + longitud + "'" +
            '}';
    }
}
