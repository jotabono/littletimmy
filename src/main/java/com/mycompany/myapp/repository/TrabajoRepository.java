package com.mycompany.myapp.repository;

import com.mycompany.myapp.domain.Trabajo;

import org.springframework.data.jpa.repository.*;
import org.springframework.data.repository.query.Param;

import java.util.List;

/**
 * Spring Data JPA repository for the Trabajo entity.
 */
@SuppressWarnings("unused")
public interface TrabajoRepository extends JpaRepository<Trabajo,Long> {

    @Query("select trabajo from Trabajo trabajo where trabajo.trabajador.login = ?#{principal.username}")
    List<Trabajo> findByTrabajadorIsCurrentUser();

    @Query("select trabajo from Trabajo trabajo where trabajo.trabajador.login = :usuario")
    List<Trabajo> findByTrabajosByTrabajador(@Param("usuario") String usuario);

    @Query("select trabajos from Trabajo trabajos where trabajos.trabajador.login = :login")/*and trabajos.actualmente = true*/
    List<Trabajo> findByUserTrabajo(@Param("login")String login);

}
