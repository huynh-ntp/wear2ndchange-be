package com.huynhntp.commons.wear2ndchange.model.dto;

import lombok.*;
import lombok.experimental.Accessors;

@Data
@Accessors(chain = true)
@AllArgsConstructor
@NoArgsConstructor
public class ProductForm {
    private String name;

    private String size;

    private String material;

    private String percentage;

    private String category;

    private Long price;

    private String status = "ACTIVE";
}
