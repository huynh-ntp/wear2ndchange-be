package com.huynhntp.commons.wear2ndchange.model.dto;

import com.huynhntp.commons.wear2ndchange.model.entity.ProductImage;
import lombok.*;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ProductDTO {
    private Long id;

    private String name;

    private String size;

    private String material;

    private String percentage;

    private String category;

    private Long price;

    private String status;

    private List<ProductImage> images;
}
