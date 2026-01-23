
package com.demo.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.demo.model.Order;


public interface OrderRepository extends JpaRepository<Order, Integer> {

    List<Order> findByUser_UserId(Integer userId);

    List<Order> findByOrderStatus(String status);

    List<Order> findByUser_UserIdAndOrderStatus(Integer userId, String status);
}
