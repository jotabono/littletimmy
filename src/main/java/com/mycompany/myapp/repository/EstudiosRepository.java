package com.mycompany.myapp.repository;

import com.mycompany.myapp.domain.Estudios;

import org.springframework.data.jpa.repository.*;
import org.springframework.data.repository.query.Param;

import java.util.List;

/**
 * Spring Data JPA repository for the Estudios entity.
 */
@SuppressWarnings("unused")
public interface EstudiosRepository extends JpaRepository<Estudios,Long> {

    @Query("select estudios from Estudios estudios where estudios.user.login = ?#{principal.username}")
    List<Estudios> findByUserIsCurrentUser();

    @Query("select estudios from Estudios estudios where estudios.user.login = :login")
    List<Estudios> findByUserEstudio(@Param("login")String login);
}
