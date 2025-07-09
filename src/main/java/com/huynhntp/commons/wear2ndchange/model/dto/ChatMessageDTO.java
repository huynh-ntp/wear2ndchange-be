package com.huynhntp.commons.wear2ndchange.model.dto;

import lombok.*;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ChatMessageDTO {
    private Long senderId;
    private Long receiverId;
    private String content;
    private String timestamp;
}
