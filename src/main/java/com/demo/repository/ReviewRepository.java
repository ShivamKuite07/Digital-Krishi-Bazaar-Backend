package com.demo.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.demo.model.Review;

public interface ReviewRepository extends JpaRepository<Review, Integer> {

    List<Review> findByProduct_ProductId(Integer productId);

    boolean existsByProduct_ProductIdAndUser_UserId(Integer productId, Integer userId);
}
