package com.demo.controller;

import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.demo.dto.CreateReviewDTO;
import com.demo.dto.ReviewResponseDTO;
import com.demo.service.ReviewService;

@RestController
@RequestMapping("/api/reviews")
public class ReviewController {

    private final ReviewService reviewService;

    public ReviewController(ReviewService reviewService) {
        this.reviewService = reviewService;
    }

    // POST /api/reviews/{productId}
    @PostMapping("/{productId}")
    public ResponseEntity<String> addReview(
            @PathVariable Integer productId,
            @RequestBody CreateReviewDTO dto) {

        reviewService.addReview(productId, dto);
        return ResponseEntity.ok("Review added successfully");
    }

    // GET /api/reviews/{productId}
    @GetMapping("/{productId}")
    public ResponseEntity<List<ReviewResponseDTO>> getReviews(
            @PathVariable Integer productId) {

        return ResponseEntity.ok(
                reviewService.getReviewsByProduct(productId)
        );
    }
}
