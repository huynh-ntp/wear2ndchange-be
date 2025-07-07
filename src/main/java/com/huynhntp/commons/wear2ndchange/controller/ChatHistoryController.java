package com.huynhntp.commons.wear2ndchange.controller;

import com.huynhntp.commons.wear2ndchange.model.entity.ChatMessage;
import com.huynhntp.commons.wear2ndchange.repository.ChatMessageRepository;
import lombok.AllArgsConstructor;
import org.springframework.data.domain.*;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/chat")
@AllArgsConstructor
public class ChatHistoryController {

    private final ChatMessageRepository chatMessageRepository;

    @GetMapping("/history")
    public Page<ChatMessage> getChatHistory(
            @RequestParam Long user1,
            @RequestParam Long user2,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "20") int size
    ) {
        Pageable pageable = PageRequest.of(page, size, Sort.by("timestamp").descending());
        return chatMessageRepository.findChatBetweenUsers(user1, user2, pageable);
    }
}

