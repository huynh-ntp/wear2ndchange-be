package com.huynhntp.commons.wear2ndchange.controller;

import com.huynhntp.commons.wear2ndchange.model.dto.ChatMessageDTO;
import com.huynhntp.commons.wear2ndchange.model.entity.ChatMessage;
import com.huynhntp.commons.wear2ndchange.repository.ChatMessageRepository;
import lombok.AllArgsConstructor;
import org.springframework.messaging.handler.annotation.*;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Controller;

import java.time.LocalDateTime;

@Controller
@AllArgsConstructor
public class ChatController {
    private final SimpMessagingTemplate messagingTemplate;
    private final ChatMessageRepository chatMessageRepository;

    @MessageMapping("/chat.send")
    public void sendPrivateMessage(@Payload ChatMessageDTO message) {
        ChatMessage entity = new ChatMessage();
        entity.setSenderId(message.getSenderId());
        entity.setReceiverId(message.getReceiverId());
        entity.setContent(message.getContent());
        entity.setTimestamp(LocalDateTime.now());

        chatMessageRepository.save(entity);

        message.setTimestamp(entity.getTimestamp().toString());

        messagingTemplate.convertAndSendToUser(
                message.getReceiverId().toString(),
                "/queue/messages",
                message
        );
    }
}
