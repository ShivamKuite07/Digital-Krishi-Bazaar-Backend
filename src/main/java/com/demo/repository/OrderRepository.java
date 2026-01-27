
package com.demo.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.demo.model.Order;


public interface OrderRepository extends JpaRepository<Order, Integer> {

    List<Order> findByUser_UserId(Integer userId);

    List<Order> findByOrderStatus(String status);

    List<Order> findByUser_UserIdAndOrderStatus(Integer userId, String status);
    
    
    @Query("""
    	    SELECT DISTINCT o
    	    FROM Order o
    	    JOIN o.orderItems oi
    	    JOIN oi.product p
    	    WHERE p.seller.userId = :sellerId
    	""")
    	List<Order> findOrdersBySellerId(@Param("sellerId") Integer sellerId);
}
