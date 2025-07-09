package com.huynhntp.commons.wear2ndchange.model.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ReviewDTO {
    private Long id;
    private Long accountId;
    private String accountName;
    private Long productId;
    private String productName;
    private String productImageUrl;
    private String comment;
    private Float rating;
    private LocalDateTime createdAt;
} 