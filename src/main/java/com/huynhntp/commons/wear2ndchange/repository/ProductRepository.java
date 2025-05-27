package com.huynhntp.commons.wear2ndchange.repository;

import com.huynhntp.commons.wear2ndchange.model.entity.*;
import org.springframework.data.domain.*;
import org.springframework.data.jpa.repository.*;
import org.springframework.data.repository.query.Param;

public interface ProductRepository extends JpaRepository<Product, Long> {

    @Query(value = """
        SELECT * FROM product
        WHERE (:name IS NULL OR LOWER(name) LIKE LOWER(CONCAT('%', :name, '%')))
          AND (:category IS NULL OR category = :category)
        """,
            countQuery = """
        SELECT COUNT(*) FROM product
        WHERE (:name IS NULL OR LOWER(name) LIKE LOWER(CONCAT('%', :name, '%')))
          AND (:category IS NULL OR category = :category)
        """,
            nativeQuery = true)
    Page<Product> searchByNameAndCategory(
            @Param("name") String name,
            @Param("category") String category,
            Pageable pageable);

}
