package com.mycompany.myapp.service.dto;

import com.mycompany.myapp.domain.Recomendacion;
import com.mycompany.myapp.domain.Trabajo;
import java.util.List;

/**
 * Created by jotabono on 22/11/16.
 */
public class TrabajoDTO {
    private Trabajo trabajo;
    private List<Recomendacion> recomendacion;

    public Trabajo getTrabajo() {
        return trabajo;
    }

    public void setTrabajo(Trabajo trabajo) {
        this.trabajo = trabajo;
    }

    public List<Recomendacion> getRecomendacion() {
        return recomendacion;
    }

    public void setRecomendacion(List<Recomendacion> recomendacion) {
        this.recomendacion = recomendacion;
    }

    @Override
    public String toString() {
        return "TrabajoDTO{" +
            "trabajo=" + trabajo +
            ", recomendacion=" + recomendacion +
            '}';
    }
}
