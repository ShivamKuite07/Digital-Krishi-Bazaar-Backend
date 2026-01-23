package com.demo.service;

import java.time.LocalDateTime;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.demo.model.Cart;
import com.demo.model.CartItem;
import com.demo.model.Order;
import com.demo.model.OrderItem;
import com.demo.model.Product;
import com.demo.repository.CartRepository;
import com.demo.repository.OrderRepository;
import com.demo.repository.ProductRepository;
import com.demo.repository.UserRepository;

import jakarta.transaction.Transactional;

@Service
public class OrderService {

    @Autowired
    private OrderRepository orderRepository;

    @Autowired
    private CartRepository cartRepository;

    @Autowired
    private ProductRepository productRepository;

    @Autowired
    private UserRepository userRepository;

    /* ======================
       GET APIs
       ====================== */

    public List<Order> getAllOrders() {
        return orderRepository.findAll();
    }

    public Order getOrderById(Integer id) {
        return orderRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Order not found"));
    }

    public List<Order> getOrdersByUser(Integer userId) {
        return orderRepository.findByUser_UserId(userId);
    }

    public List<Order> getOrdersByStatus(String status) {
        return orderRepository.findByOrderStatus(status.toUpperCase());
    }

    public List<Order> getOrdersByUserAndStatus(Integer userId, String status) {
        return orderRepository.findByUser_UserIdAndOrderStatus(
                userId, status.toUpperCase());
    }

    /* ======================
       CREATE ORDER (FROM CART)
       ====================== */
    @Transactional
    public Order placeOrder(Integer userId, String paymentMode) {

        Cart cart = cartRepository
                .findByUser_UserIdAndStatus(userId, "ACTIVE")
                .orElseThrow(() -> new RuntimeException("Active cart not found"));

        Order order = new Order();
        order.setUser(cart.getUser());
        order.setOrderDate(LocalDateTime.now());
        order.setOrderStatus("TRANSIT"); // DEFAULT
        order.setPaymentMode(paymentMode);
        order.setPaymentStatus("PAID");

        int totalAmount = 0;

        for (CartItem ci : cart.getCartItems()) {

            Product product = ci.getProduct();

            if (product.getQuantityAvailable() < ci.getQuantity()) {
                throw new RuntimeException(
                    "Insufficient stock for product: " + product.getProductName()
                );
            }

            // 🔻 subtract stock
            product.setQuantityAvailable(
                    product.getQuantityAvailable() - ci.getQuantity()
            );
            productRepository.save(product);

            OrderItem oi = new OrderItem();
            oi.setOrder(order);
            oi.setProduct(product);
            oi.setQuantity(ci.getQuantity());
            oi.setPrice(ci.getPrice());

            order.getOrderItems().add(oi);

            totalAmount += ci.getQuantity() * ci.getPrice();
        }

        order.setTotalAmount(totalAmount);

        // close cart
        cart.setStatus("CHECKED_OUT");
        cartRepository.save(cart);

        return orderRepository.save(order);
    }

    /* ======================
       UPDATE ORDER STATUS
       ====================== */
    @Transactional
    public Order updateOrderStatus(Integer orderId, String newStatus) {

        Order order = getOrderById(orderId);
        String oldStatus = order.getOrderStatus();

        newStatus = newStatus.toUpperCase();

        // 🚫 Prevent invalid transitions
        if ("COMPLETED".equals(oldStatus)) {
            throw new RuntimeException("Completed order cannot be changed");
        }

        // 🔄 TRANSIT → CANCELED (ADD STOCK BACK)
        if ("TRANSIT".equals(oldStatus) && "CANCELED".equals(newStatus)) {

            for (OrderItem oi : order.getOrderItems()) {
                Product product = oi.getProduct();
                product.setQuantityAvailable(
                        product.getQuantityAvailable() + oi.getQuantity()
                );
                productRepository.save(product);
            }
        }

        order.setOrderStatus(newStatus);
        return orderRepository.save(order);
    }
}

