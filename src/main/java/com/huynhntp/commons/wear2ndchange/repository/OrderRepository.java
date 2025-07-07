package com.huynhntp.commons.wear2ndchange.repository;

import com.huynhntp.commons.wear2ndchange.model.entity.Order;
import org.springframework.data.domain.*;
import org.springframework.data.jpa.repository.*;
import org.springframework.data.repository.query.Param;

import java.time.LocalDateTime;
import java.util.*;

public interface OrderRepository extends JpaRepository<Order, Long> {
    Page<Order> findByUserIdOrderByCreatedAtDesc(Long userId, Pageable pageable);

    Page<Order> findAllByOrderByCreatedAtDesc(Pageable pageable);

    Page<Order> findByUserId(Long userId, Pageable pageable);

    List<Order> findByUserIdAndStatus(Long userId, String status);

    Integer countByStatus(String status);

    Optional<Order> findByOrderCode(String orderCode);

    @Query("SELECT SUM(o.totalAmount) FROM Order o WHERE o.status = 'RECEIVED' ")
    Double findTotalAmountSince();

    @Query(value = "SELECT DATE(created_at) AS date, SUM(total_amount) AS total " +
            "FROM orders " +
            "WHERE created_at >= :fromTime and status = 'RECEIVED' " +
            "GROUP BY DATE(created_at) " +
            "ORDER BY DATE(created_at) " +
            "LIMIT 7",
            nativeQuery = true)
    List<Object[]> findDailyStats(@Param("fromTime") LocalDateTime fromTime);


    @Query(value = "SELECT EXTRACT(MONTH FROM created_at) AS month, SUM(total_amount) AS total " +
            "FROM orders " +
            "WHERE created_at >= :fromTime and status = 'RECEIVED' " +
            "GROUP BY EXTRACT(MONTH FROM created_at) " +
            "ORDER BY month " +
            "LIMIT 12",
            nativeQuery = true)
    List<Object[]> findMonthlyStats(@Param("fromTime") LocalDateTime fromTime);
}


