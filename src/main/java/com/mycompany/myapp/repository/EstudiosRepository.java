package com.mycompany.myapp.repository;

import com.mycompany.myapp.domain.Estudios;

import org.springframework.data.jpa.repository.*;

import java.util.List;

/**
 * Spring Data JPA repository for the Estudios entity.
 */
public interface EstudiosRepository extends JpaRepository<Estudios,Long> {

}
