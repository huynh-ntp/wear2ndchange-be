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

    private String status;

    private Long price;

    private String[] imagesUrl;
}
