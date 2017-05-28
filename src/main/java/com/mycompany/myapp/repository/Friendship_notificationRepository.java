package com.mycompany.myapp.repository;

import com.mycompany.myapp.domain.Friendship_notification;

import org.springframework.data.jpa.repository.*;

import java.util.List;

/**
 * Spring Data JPA repository for the Friendship_notification entity.
 */
@SuppressWarnings("unused")
public interface Friendship_notificationRepository extends JpaRepository<Friendship_notification,Long> {

    @Query("select friendship_notification from Friendship_notification friendship_notification where friendship_notification.receptor.login = ?#{principal.username}")
    List<Friendship_notification> findByReceptorIsCurrentUser();

    @Query("select friendship_notification from Friendship_notification friendship_notification where friendship_notification.emisor.login = ?#{principal.username}")
    List<Friendship_notification> findByEmisorIsCurrentUser();

}
