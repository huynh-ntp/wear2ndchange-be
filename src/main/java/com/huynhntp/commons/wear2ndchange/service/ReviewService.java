package com.huynhntp.commons.wear2ndchange.service;

import com.huynhntp.commons.wear2ndchange.config.exception.BusinessException;
import com.huynhntp.commons.wear2ndchange.enums.ProductAndOrderStatusEnum;
import com.huynhntp.commons.wear2ndchange.model.dto.ReviewDTO;
import com.huynhntp.commons.wear2ndchange.model.dto.ReviewForm;
import com.huynhntp.commons.wear2ndchange.model.entity.*;
import com.huynhntp.commons.wear2ndchange.repository.*;
import lombok.AllArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
@AllArgsConstructor
public class ReviewService {

    private final ReviewRepository reviewRepository;
    private final AccountRepository accountRepository;
    private final ProductRepository productRepository;
    private final OrderRepository orderRepository;
    private final OrderItemRepository orderItemRepository;
    private final AuthService authService;

    @Transactional
    public ReviewDTO createReview(ReviewForm reviewForm) {
        Long userId = authService.getUserId();
        
        // Validate user exists
        Account account = accountRepository.findById(userId)
                .orElseThrow(() -> new BusinessException("Account not found"));

        // Validate product exists
        Product product = productRepository.findById(reviewForm.getProductId())
                .orElseThrow(() -> new BusinessException("Product not found"));

        // Check if user has already reviewed this product
        if (reviewRepository.existsByAccountIdAndProductId(userId, reviewForm.getProductId())) {
            throw new BusinessException("You have already reviewed this product");
        }

        // Validate that user has successfully purchased this product
        if (!hasUserPurchasedProduct(userId, reviewForm.getProductId())) {
            throw new BusinessException("You can only review products you have successfully purchased");
        }

        // Create the review
        Review review = new Review()
                .setAccount(account)
                .setProduct(product)
                .setComment(reviewForm.getComment())
                .setRating(reviewForm.getRating())
                .setCreatedAt(LocalDateTime.now());

        Review savedReview = reviewRepository.save(review);
        
        return mapToDTO(savedReview);
    }

    @Transactional(readOnly = true)
    public Page<ReviewDTO> getAllReviews(Pageable pageable) {
        Page<Review> reviews = reviewRepository.findAllWithAccountAndProduct(pageable);
        List<ReviewDTO> reviewDTOs = reviews.getContent().stream()
                .map(this::mapToDTO)
                .collect(Collectors.toList());
        
        return new PageImpl<>(reviewDTOs, pageable, reviews.getTotalElements());
    }

    @Transactional(readOnly = true)
    public Page<ReviewDTO> getReviewsByProduct(Long productId, Pageable pageable) {
        // Validate product exists
        if (!productRepository.existsById(productId)) {
            throw new BusinessException("Product not found");
        }
        
        Page<Review> reviews = reviewRepository.findByProductIdWithAccountAndProduct(productId, pageable);
        List<ReviewDTO> reviewDTOs = reviews.getContent().stream()
                .map(this::mapToDTO)
                .collect(Collectors.toList());
        
        return new PageImpl<>(reviewDTOs, pageable, reviews.getTotalElements());
    }

    private boolean hasUserPurchasedProduct(Long userId, Long productId) {
        // Check if there's any completed order containing this product for this user
        List<Order> userOrders = orderRepository.findByUserIdAndStatus(userId, ProductAndOrderStatusEnum.RECEIVED.toString());
        
        for (Order order : userOrders) {
            boolean hasProduct = orderItemRepository.existsByOrderIdAndProductId(order.getId(), productId);
            if (hasProduct) {
                return true;
            }
        }
        
        return false;
    }

    private ReviewDTO mapToDTO(Review review) {
        // Get the first product image URL if available
        String imageUrl = null;
        if (review.getProduct().getImages() != null && !review.getProduct().getImages().isEmpty()) {
            imageUrl = review.getProduct().getImages().get(0).getUrl();
        }
        
        return new ReviewDTO(
                review.getId(),
                review.getAccount().getId(),
                review.getAccount().getFullName() == null || review.getAccount().getFullName().isBlank() ? review.getAccount().getUsername() : review.getAccount().getFullName(),
                review.getProduct().getId(),
                review.getProduct().getName(),
                imageUrl,
                review.getComment(),
                review.getRating(),
                review.getCreatedAt()
        );
    }
} 