package com.huynhntp.commons.wear2ndchange.repository;

import com.huynhntp.commons.wear2ndchange.model.entity.Review;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface ReviewRepository extends JpaRepository<Review, Long> {
    
    @Query("SELECT r FROM Review r WHERE r.account.id = :accountId AND r.product.id = :productId")
    Optional<Review> findByAccountIdAndProductId(@Param("accountId") Long accountId, @Param("productId") Long productId);
    
    @Query("SELECT r FROM Review r JOIN FETCH r.account JOIN FETCH r.product ORDER BY r.createdAt DESC")
    Page<Review> findAllWithAccountAndProduct(Pageable pageable);
    
    @Query("SELECT r FROM Review r JOIN FETCH r.account JOIN FETCH r.product WHERE r.product.id = :productId ORDER BY r.createdAt DESC")
    Page<Review> findByProductIdWithAccountAndProduct(@Param("productId") Long productId, Pageable pageable);
    
    boolean existsByAccountIdAndProductId(Long accountId, Long productId);
}
