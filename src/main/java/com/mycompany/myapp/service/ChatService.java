/**
 * Created by Xavi on 25/01/2017.
 */

package com.mycompany.myapp.service;

import com.mycompany.myapp.domain.Chat;
import com.mycompany.myapp.domain.Messages;
import com.mycompany.myapp.domain.User;
import com.mycompany.myapp.repository.MessagesRepository;
import com.mycompany.myapp.repository.UserRepository;
import com.mycompany.myapp.security.SecurityUtils;
import com.mycompany.myapp.web.websocket.ActivityService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.ApplicationListener;
import org.springframework.messaging.handler.annotation.DestinationVariable;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.messaging.simp.SimpMessageSendingOperations;
import org.springframework.stereotype.Controller;
import org.springframework.web.socket.messaging.SessionDisconnectEvent;

import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import javax.inject.Inject;

@Controller
public class ChatService implements ApplicationListener<SessionDisconnectEvent> {

    private static final Logger log = LoggerFactory.getLogger(ActivityService.class);

    private DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

    @Inject
    SimpMessageSendingOperations messagingTemplate;

    @Inject
    private MessagesRepository messagesRepository;

    @Inject
    private UserRepository userRepository;

    @MessageMapping("/topic/sendMessage/{chat_id}")
    @SendTo("/topic/messages/{chat_id}")
    public Messages receive(@DestinationVariable Long chat_id, Messages messages) throws Exception {
        log.debug(""+messages);
        User user = userRepository.findOneByLogin(SecurityUtils.getCurrentUserLogin()).get();
        messages.setSender(user);
        ZonedDateTime now = ZonedDateTime.now();
        messages.setSendDate(now);
        Chat chat = new Chat();
        chat.setId(chat_id);
        messages.setChat(chat);
        Messages result = messagesRepository.save(messages);
        return result;
    }

    @Override
    public void onApplicationEvent(SessionDisconnectEvent event) {

    }

}
