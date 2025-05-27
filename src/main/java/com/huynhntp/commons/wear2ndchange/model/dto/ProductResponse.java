package com.huynhntp.commons.wear2ndchange.model.dto;

import lombok.*;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ProductResponse {
    private Long id;

    private String name;

    private String size;

    private String material;

    private String percentage;

    private String category;

    private Long price;

    private String status;

    private String[] imagesUrl;
}
