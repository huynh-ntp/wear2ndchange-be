package com.huynhntp.commons.wear2ndchange.repository;

import com.huynhntp.commons.wear2ndchange.enums.CategoryEnum;
import com.huynhntp.commons.wear2ndchange.model.entity.Product;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

public interface ProductRepository extends JpaRepository<Product, Long> {

    @Query("select p " +
            "from Product p " +
            "where 1 = 1 " +
            "and (:name is null or p.searchText like %:name%) " +
            "and (:category is null or p.category=:category )" +
            "and (:status is null or p.status = :status) ")
    Page<Product> searchByNameAndStatusAndCategory(String name, String status, CategoryEnum category, Pageable pageable);

}
