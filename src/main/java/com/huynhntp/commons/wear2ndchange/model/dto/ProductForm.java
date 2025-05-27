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

    private String status;

    private Long price;
}
