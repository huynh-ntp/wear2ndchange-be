package com.huynhntp.commons.wear2ndchange.controller;

import com.huynhntp.commons.wear2ndchange.model.dto.ReviewDTO;
import com.huynhntp.commons.wear2ndchange.model.dto.ReviewForm;
import com.huynhntp.commons.wear2ndchange.service.ReviewService;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/review")
@AllArgsConstructor
public class ReviewController {

    private final ReviewService reviewService;

    @PostMapping
    public ResponseEntity<ReviewDTO> createReview(@Valid @RequestBody ReviewForm reviewForm) {
        ReviewDTO review = reviewService.createReview(reviewForm);
        return ResponseEntity.ok(review);
    }

    @GetMapping
    public Page<ReviewDTO> getAllReviews(Pageable pageable) {
        return reviewService.getAllReviews(pageable);
    }

    @GetMapping("/product/{productId}")
    public Page<ReviewDTO> getReviewsByProduct(@PathVariable Long productId, Pageable pageable) {
        return reviewService.getReviewsByProduct(productId, pageable);
    }
} 