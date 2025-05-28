package com.huynhntp.commons.wear2ndchange.repository;

import com.huynhntp.commons.wear2ndchange.model.entity.Cart;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.*;

public interface CartRepository extends JpaRepository<Cart, Long> {
    List<Cart> findByUserId(Long userId);
    Optional<Cart> findByUserIdAndProduct_Id(Long userId, Long productId);
}
