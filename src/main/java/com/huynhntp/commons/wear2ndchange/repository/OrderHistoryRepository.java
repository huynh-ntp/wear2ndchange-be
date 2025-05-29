package com.huynhntp.commons.wear2ndchange.repository;

import com.huynhntp.commons.wear2ndchange.model.entity.OrderHistory;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface OrderHistoryRepository extends JpaRepository<OrderHistory, Long> {
    List<OrderHistory> findByUserIdOrderByCreatedAtDesc(Long userId);

}
