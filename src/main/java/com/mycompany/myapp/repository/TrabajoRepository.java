package com.mycompany.myapp.repository;

import com.mycompany.myapp.domain.Trabajo;

import org.springframework.data.jpa.repository.*;

import java.util.List;

/**
 * Spring Data JPA repository for the Trabajo entity.
 */
@SuppressWarnings("unused")
public interface TrabajoRepository extends JpaRepository<Trabajo,Long> {

}
