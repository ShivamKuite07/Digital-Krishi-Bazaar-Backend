package com.demo.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.demo.model.Cart;

public interface CartRepository extends JpaRepository<Cart, Integer> {

    Optional<Cart> findByUser_UserIdAndStatus(Integer userId, String status);
}
