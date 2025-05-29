package com.huynhntp.commons.wear2ndchange.repository;

import com.huynhntp.commons.wear2ndchange.model.entity.Order;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface OrderRepository extends JpaRepository<Order, Long> {
    List<Order> findByUserIdOrderByCreatedAtDesc(Long userId);
}

