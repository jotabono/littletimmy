package com.mycompany.myapp.repository;

import com.mycompany.myapp.domain.Messages;

import org.springframework.data.jpa.repository.*;

import java.util.List;

/**
 * Spring Data JPA repository for the Messages entity.
 */
@SuppressWarnings("unused")
public interface MessagesRepository extends JpaRepository<Messages,Long> {

    @Query("select messages from Messages messages where messages.sender.login = ?#{principal.username}")
    List<Messages> findBySenderIsCurrentUser();

}
