package com.huynhntp.commons.wear2ndchange.repository;

import com.huynhntp.commons.wear2ndchange.model.entity.*;
import org.springframework.data.domain.*;
import org.springframework.data.jpa.repository.*;
import org.springframework.data.repository.query.Param;

public interface ProductRepository extends JpaRepository<Product, Long> {

    @Query("select p " +
            "from Product p " +
            "where 1 = 1 " +
            "and (:name is null or p.searchText like '%name%') " +
            "and (:category is null or p.category=:category )")
    Page<Product> searchByNameAndCategory(String name, String category, Pageable pageable);

}
