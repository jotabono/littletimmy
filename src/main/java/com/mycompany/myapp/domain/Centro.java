package com.mycompany.myapp.domain;

import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;
import org.springframework.data.elasticsearch.annotations.Document;

import javax.persistence.*;
import java.io.Serializable;
import java.time.LocalDate;
import java.util.Objects;

/**
 * A Centro.
 */
@Entity
@Table(name = "centro")
@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
@Document(indexName = "centro")
public class Centro implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @Column(name = "nombre")
    private String nombre;

    @Column(name = "num_empleados")
    private Integer numEmpleados;

    @Column(name = "fecha_fundacion")
    private LocalDate fechaFundacion;

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

    public Centro nombre(String nombre) {
        this.nombre = nombre;
        return this;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public Integer getNumEmpleados() {
        return numEmpleados;
    }

    public Centro numEmpleados(Integer numEmpleados) {
        this.numEmpleados = numEmpleados;
        return this;
    }

    public void setNumEmpleados(Integer numEmpleados) {
        this.numEmpleados = numEmpleados;
    }

    public LocalDate getFechaFundacion() {
        return fechaFundacion;
    }

    public Centro fechaFundacion(LocalDate fechaFundacion) {
        this.fechaFundacion = fechaFundacion;
        return this;
    }

    public void setFechaFundacion(LocalDate fechaFundacion) {
        this.fechaFundacion = fechaFundacion;
    }

    public String getUbicacion() {
        return ubicacion;
    }

    public Centro ubicacion(String ubicacion) {
        this.ubicacion = ubicacion;
        return this;
    }

    public void setUbicacion(String ubicacion) {
        this.ubicacion = ubicacion;
    }

    public String getLatitud() {
        return latitud;
    }

    public Centro latitud(String latitud) {
        this.latitud = latitud;
        return this;
    }

    public void setLatitud(String latitud) {
        this.latitud = latitud;
    }

    public String getLongitud() {
        return longitud;
    }

    public Centro longitud(String longitud) {
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
        Centro centro = (Centro) o;
        if(centro.id == null || id == null) {
            return false;
        }
        return Objects.equals(id, centro.id);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(id);
    }

    @Override
    public String toString() {
        return "Centro{" +
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
