package com.huynhntp.commons.wear2ndchange.model.entity;

import com.huynhntp.commons.wear2ndchange.common.UtilsService;
import jakarta.persistence.*;
import lombok.Data;
import lombok.experimental.Accessors;

import java.util.ArrayList;
import java.util.List;

@Data
@Entity
@Table(name = "product")
@Accessors(chain = true)
public class Product {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String name;

    private String size;

    private String material;

    private String percentage;

    private String status;

    private Long price;

    private String searchText;

    private void updateSearchText() {
        if (name != null && !name.isEmpty()) {
            this.searchText = UtilsService.removeAccents(name);
        }
    }

    @PrePersist
    private void prePersist() {
        updateSearchText();
    }

    @PreUpdate
    private void preUpdate() {
        updateSearchText();
    }

    @OneToMany(mappedBy = "product", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<ProductImage> images = new ArrayList<>();

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "account_id", nullable = false)
    private Account createBy;

}
