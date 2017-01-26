package com.mycompany.myapp.repository;

import com.mycompany.myapp.domain.Friend_user;

import org.springframework.data.jpa.repository.*;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Set;

/**
 * Spring Data JPA repository for the Friend_user entity.
 */
@SuppressWarnings("unused")
public interface Friend_userRepository extends JpaRepository<Friend_user,Long> {

    @Query("select friend_user from Friend_user friend_user where friend_user.friend_from.login = ?#{principal.username}")
    List<Friend_user> findByFriend_fromIsCurrentUser();

    @Query("select friend_user from Friend_user friend_user where friend_user.friend_to.login = ?#{principal.username}")
    List<Friend_user> findByFriend_toIsCurrentUser();

    @Query("select friend_user from Friend_user friend_user where friend_user.friend_to.login = ?#{principal.username} OR friend_user.friend_from.login = ?#{principal.username}")
    List<Friend_user> findByFriendOfUser();

    @Query("select friend_user from Friend_user friend_user where friend_user.friendship = true AND (friend_user.friend_to.login = :login OR friend_user.friend_from.login = :login)")
    Set<Friend_user> findFriendsOfUser(@Param("login") String login);

    @Query("select friend_user from Friend_user friend_user where (friend_user.friend_from.login = :login AND friend_user.friend_to.login = :login2) " +
        "OR (friend_user.friend_from.login = :login2 AND friend_user.friend_to.login = :login)")
    Friend_user findExistFriendship(@Param("login") String login, @Param("login2") String login2);
}
