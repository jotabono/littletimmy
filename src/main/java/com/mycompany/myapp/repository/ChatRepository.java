package com.mycompany.myapp.repository;

import com.mycompany.myapp.domain.Chat;

import com.mycompany.myapp.domain.Messages;
import org.springframework.data.jpa.repository.*;
import org.springframework.data.repository.query.Param;

import java.util.List;

/**
 * Spring Data JPA repository for the Chat entity.
 */
@SuppressWarnings("unused")
public interface ChatRepository extends JpaRepository<Chat,Long> {

    @Query("select chat from Chat chat where chat.owner.login = ?#{principal.username}")
    List<Chat> findByOwnerIsCurrentUser();

    @Query("select distinct chat from Chat chat left join fetch chat.users")
    List<Chat> findAllWithEagerRelationships();

    @Query("select chat from Chat chat left join fetch chat.users where chat.id =:id")
    Chat findOneWithEagerRelationships(@Param("id") Long id);

    @Query("select messages from Messages messages where messages.chat.id = :id ")
    List<Messages> findChatMessages(@Param("id") Long id);

    @Query("select distinct chat from Chat chat left join fetch chat.users as users where users.login = :login OR chat.owner.login = :login")
    List<Chat> findUserChats(@Param("login") String login);

}
