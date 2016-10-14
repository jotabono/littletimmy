package com.mycompany.myapp.repository;

import com.mycompany.myapp.domain.Recommend_notification;

import org.springframework.data.jpa.repository.*;

import java.util.List;

/**
 * Spring Data JPA repository for the Recommend_notification entity.
 */
@SuppressWarnings("unused")
public interface Recommend_notificationRepository extends JpaRepository<Recommend_notification,Long> {

    @Query("select recommend_notification from Recommend_notification recommend_notification where recommend_notification.remitente.login = ?#{principal.username}")
    List<Recommend_notification> findByRemitenteIsCurrentUser();

}
