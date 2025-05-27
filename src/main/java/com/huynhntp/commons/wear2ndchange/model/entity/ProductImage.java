package com.huynhntp.commons.wear2ndchange.model.entity;


import jakarta.persistence.*;
import lombok.Data;

@Entity
@Data
public class ProductImage {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String url;

    private Boolean isMainImage;

    @ManyToOne
    @JoinColumn(name = "product_id")
    private Product product;
}

