package com.mycompany.myapp.repository;

import com.mycompany.myapp.domain.Recomendacion;

import org.springframework.data.jpa.repository.*;

import java.util.List;

/**
 * Spring Data JPA repository for the Recomendacion entity.
 */
@SuppressWarnings("unused")
public interface RecomendacionRepository extends JpaRepository<Recomendacion,Long> {

}
