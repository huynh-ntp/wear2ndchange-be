package com.huynhntp.commons.wear2ndchange.repository;

import com.huynhntp.commons.wear2ndchange.model.entity.Order;
import org.springframework.data.domain.*;
import org.springframework.data.jpa.repository.*;
import org.springframework.data.repository.query.Param;

import java.time.LocalDateTime;
import java.util.*;

public interface OrderRepository extends JpaRepository<Order, Long> {
    List<Order> findByUserIdOrderByCreatedAtDesc(Long userId);

    Page<Order> findByUserId(Long userId, Pageable pageable);

    Integer countByStatus(String status);

    @Query("SELECT SUM(o.totalAmount) FROM Order o WHERE o.status = 'RECEIVED' ")
    Double findTotalAmountSince();

    @Query(value = "SELECT EXTRACT(DOW FROM created_at) AS dow, SUM(total_amount) " +
            "FROM orders " +
            "WHERE created_at >= :fromTime " +
            "GROUP BY dow",
            nativeQuery = true)
    List<Object[]> findDailyStats(@Param("fromTime") LocalDateTime fromTime);


    @Query("SELECT EXTRACT(MONTH FROM o.createdAt), SUM(o.totalAmount) " +
            "FROM Order o " +
            "WHERE o.createdAt >= :fromTime " +
            "GROUP BY EXTRACT(MONTH FROM o.createdAt)")
    List<Object[]> findMonthlyStats(@Param("fromTime") LocalDateTime fromTime);
}


