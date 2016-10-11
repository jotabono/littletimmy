package com.mycompany.myapp.repository;

import com.mycompany.myapp.domain.Friend_user;

import org.springframework.data.jpa.repository.*;

import java.util.List;

/**
 * Spring Data JPA repository for the Friend_user entity.
 */
@SuppressWarnings("unused")
public interface Friend_userRepository extends JpaRepository<Friend_user,Long> {

    @Query("select friend_user from Friend_user friend_user where friend_user.friend_from.login = ?#{principal.username}")
    List<Friend_user> findByFriend_fromIsCurrentUser();

    @Query("select friend_user from Friend_user friend_user where friend_user.friend_to.login = ?#{principal.username}")
    List<Friend_user> findByFriend_toIsCurrentUser();

}
