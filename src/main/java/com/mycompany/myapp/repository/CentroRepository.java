package com.mycompany.myapp.repository;

import com.mycompany.myapp.domain.Centro;

import org.springframework.data.jpa.repository.*;

import java.util.List;

/**
 * Spring Data JPA repository for the Centro entity.
 */
@SuppressWarnings("unused")
public interface CentroRepository extends JpaRepository<Centro,Long> {

}
