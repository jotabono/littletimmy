package com.mycompany.myapp.repository;

import com.mycompany.myapp.domain.Recomendacion;

import org.springframework.data.jpa.repository.*;
import org.springframework.data.repository.query.Param;

import java.util.List;

/**
 * Spring Data JPA repository for the Recomendacion entity.
 */
@SuppressWarnings("unused")
public interface RecomendacionRepository extends JpaRepository<Recomendacion,Long> {

    @Query("select recomendacion from Recomendacion recomendacion where recomendacion.recomendador.login = ?#{principal.username}")
    List<Recomendacion> findByRecomendadorIsCurrentUser();

    @Query("select recomendacion from Recomendacion recomendacion where recomendacion.recomendado.login = ?#{principal.username}")
    List<Recomendacion> findByRecomendadoIsCurrentUser();

    @Query("select recomendacion from Recomendacion recomendacion where recomendacion.trabajo.id = :id_trabajo and recomendacion.aceptada = true")
    List<Recomendacion> findAllRecomendacionesTrabajo(@Param("id_trabajo") Long id);

}
