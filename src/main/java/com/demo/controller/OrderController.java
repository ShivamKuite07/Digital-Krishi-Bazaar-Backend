package com.demo.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.demo.dto.OrderDTO;
import com.demo.dto.OrderItemDTO;
import com.demo.model.Order;
import com.demo.service.OrderService;

@RestController
@RequestMapping("/api/orders")
public class OrderController {

    @Autowired
    private OrderService orderService;

    /* ======================
       GET ALL ORDERS
       ====================== */
    @GetMapping
    public List<OrderDTO> getAllOrders() {
        return orderService.getAllOrders()
                .stream().map(this::mapToDTO).toList();
    }

    /* ======================
       GET ORDER BY ID
       ====================== */
    @GetMapping("/{id}")
    public OrderDTO getOrderById(@PathVariable Integer id) {
        return mapToDTO(orderService.getOrderById(id));
    }

    /* ======================
       GET ORDERS BY USER
       ====================== */
    @GetMapping("/user/{userId}")
    public List<OrderDTO> getOrdersByUser(@PathVariable Integer userId) {
        return orderService.getOrdersByUser(userId)
                .stream().map(this::mapToDTO).toList();
    }

    /* ======================
       GET ORDERS BY STATUS
       ====================== */
    @GetMapping("/status/{status}")
    public List<OrderDTO> getOrdersByStatus(@PathVariable String status) {
        return orderService.getOrdersByStatus(status)
                .stream().map(this::mapToDTO).toList();
    }

    /* ======================
       GET ORDERS BY USER + STATUS
       ====================== */
    @GetMapping("/user/{userId}/status/{status}")
    public List<OrderDTO> getOrdersByUserAndStatus(
            @PathVariable Integer userId,
            @PathVariable String status) {

        return orderService.getOrdersByUserAndStatus(userId, status)
                .stream().map(this::mapToDTO).toList();
    }

    /* ======================
       PLACE ORDER
       ====================== */
    @PostMapping("/place")
    public OrderDTO placeOrder(
            @RequestParam Integer userId,
            @RequestParam String paymentMode) {

        return mapToDTO(orderService.placeOrder(userId, paymentMode));
    }

    /* ======================
       UPDATE STATUS (ADMIN)
       ====================== */
    @PutMapping("/{id}/status/{status}")
    public OrderDTO updateOrderStatus(
            @PathVariable Integer id,
            @PathVariable String status) {

        return mapToDTO(orderService.updateOrderStatus(id, status));
    }
    
    
    /* ======================
    GET ORDERS BY SELLER
    ====================== */
	 @GetMapping("/seller/{sellerId}")
	 public List<OrderDTO> getOrdersBySeller(@PathVariable Integer sellerId) {
	
	     return orderService.getOrdersBySeller(sellerId)
	             .stream()
	             .map(this::mapToDTO)
	             .toList();
	 }
	    

    /* ======================
       ENTITY → DTO
       ====================== */
    private OrderDTO mapToDTO(Order order) {

        OrderDTO dto = new OrderDTO();
        dto.setOrderId(order.getOrderId());
        dto.setOrderStatus(order.getOrderStatus());
        dto.setPaymentMode(order.getPaymentMode());
        dto.setPaymentStatus(order.getPaymentStatus());
        dto.setTotalAmount(order.getTotalAmount());
        dto.setOrderDate(order.getOrderDate());

        dto.setUserId(order.getUser().getUserId());
        dto.setUserName(order.getUser().getUserName());

        List<OrderItemDTO> items = order.getOrderItems()
                .stream()
                .map(oi -> {
                    OrderItemDTO i = new OrderItemDTO();
                    i.setOrderItemId(oi.getOrderItemId());
                    i.setProductId(oi.getProduct().getProductId());
                    i.setProductName(oi.getProduct().getProductName());
                    i.setQuantity(oi.getQuantity());
                    i.setPrice(oi.getPrice());
                    return i;
                }).toList();

        dto.setOrderItems(items);
        return dto;
    }
}
