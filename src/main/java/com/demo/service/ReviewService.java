package com.demo.service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;

import com.demo.dto.CreateReviewDTO;
import com.demo.dto.ReviewResponseDTO;
import com.demo.model.Product;
import com.demo.model.Review;
import com.demo.model.User;
import com.demo.repository.ProductRepository;
import com.demo.repository.ReviewRepository;
import com.demo.repository.UserRepository;

@Service
public class ReviewService {

    private final ReviewRepository reviewRepository;
    private final ProductRepository productRepository;
    private final UserRepository userRepository;

    public ReviewService(
            ReviewRepository reviewRepository,
            ProductRepository productRepository,
            UserRepository userRepository) {
        this.reviewRepository = reviewRepository;
        this.productRepository = productRepository;
        this.userRepository = userRepository;
    }

    // POST review
    public void addReview(Integer productId, CreateReviewDTO dto) {

        if (reviewRepository.existsByProduct_ProductIdAndUser_UserId(productId, dto.getUserId())) {
            throw new RuntimeException("User already reviewed this product");
        }

        Product product = productRepository.findById(productId)
                .orElseThrow(() -> new RuntimeException("Product not found"));

        User user = userRepository.findById(dto.getUserId())
                .orElseThrow(() -> new RuntimeException("User not found"));

        Review review = new Review();
        review.setProduct(product);
        review.setUser(user);
        review.setRating(dto.getRating());
        review.setComment(dto.getComment());
        review.setCreatedAt(LocalDateTime.now());

        reviewRepository.save(review);
    }

    // GET reviews
    public List<ReviewResponseDTO> getReviewsByProduct(Integer productId) {

        return reviewRepository.findByProduct_ProductId(productId)
                .stream()
                .map(review -> {
                    ReviewResponseDTO dto = new ReviewResponseDTO();
                    dto.setReviewId(review.getReviewId());
                    dto.setRating(review.getRating());
                    dto.setComment(review.getComment());
                    dto.setCreatedAt(review.getCreatedAt());
                    dto.setUserId(review.getUser().getUserId());
                    dto.setUserName(review.getUser().getUserName());
                    return dto;
                })
                .collect(Collectors.toList());
    }
}
