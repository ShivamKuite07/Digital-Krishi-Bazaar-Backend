package com.demo.service;

import java.time.LocalDateTime;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.demo.model.Cart;
import com.demo.model.CartItem;
import com.demo.model.Product;
import com.demo.model.User;
import com.demo.repository.CartItemRepository;
import com.demo.repository.CartRepository;
import com.demo.repository.ProductRepository;
import com.demo.repository.UserRepository;

@Service
public class CartService {

    @Autowired
    private CartRepository cartRepository;

    @Autowired
    private CartItemRepository cartItemRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private ProductRepository productRepository;

    /* ======================
       GET CART BY ID
       ====================== */
    public Cart getCartById(Integer cartId) {
        return cartRepository.findById(cartId)
                .orElseThrow(() -> new RuntimeException("Cart not found"));
    }

    /* ======================
       GET CART BY USER
       ====================== */
    public Cart getCartByUser(Integer userId) {

        return cartRepository
                .findByUser_UserIdAndStatus(userId, "ACTIVE")
                .orElseGet(() -> createCartForUser(userId));
    }

    /* ======================
       CREATE CART
       ====================== */
    private Cart createCartForUser(Integer userId) {

        User user = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("User not found"));

        Cart cart = new Cart();
        cart.setUser(user);
        cart.setStatus("ACTIVE");
        cart.setCreatedAt(LocalDateTime.now());

        return cartRepository.save(cart);
    }

    /* ======================
       ADD PRODUCT TO CART
       ====================== */
    public Cart addProductToCart(Integer userId, Integer productId) {

        Cart cart = getCartByUser(userId);

        Product product = productRepository.findById(productId)
                .orElseThrow(() -> new RuntimeException("Product not found"));

        CartItem cartItem = cartItemRepository
                .findByCart_CartIdAndProduct_ProductId(cart.getCartId(), productId)
                .orElse(null);

        if (cartItem == null) {
            cartItem = new CartItem();
            cartItem.setCart(cart);
            cartItem.setProduct(product);
            cartItem.setQuantity(1);
            cartItem.setPrice(product.getPrice());
            cart.getCartItems().add(cartItem);
        } else {
            cartItem.setQuantity(cartItem.getQuantity() + 1);
        }

        return cartRepository.save(cart);
    }
    
    public Cart decrementProductFromCart(Integer userId, Integer productId) {

        Cart cart = getCartByUser(userId);

        CartItem item = cart.getCartItems()
                .stream()
                .filter(ci -> ci.getProduct().getProductId().equals(productId))
                .findFirst()
                .orElseThrow(() ->
                    new RuntimeException("Product not in cart"));

        if (item.getQuantity() > 1) {
            item.setQuantity(item.getQuantity() - 1);
        } else {
            cart.getCartItems().remove(item);
            cartItemRepository.delete(item);
        }

        return cartRepository.save(cart);
    }


    /* ======================
       REMOVE PRODUCT FROM CART
       ====================== */
    public Cart removeProductFromCart(Integer userId, Integer productId) {

        Cart cart = getCartByUser(userId);

        CartItem cartItem = cartItemRepository
                .findByCart_CartIdAndProduct_ProductId(cart.getCartId(), productId)
                .orElseThrow(() -> new RuntimeException("Product not in cart"));

        cart.getCartItems().remove(cartItem);
        cartItemRepository.delete(cartItem);

        return cartRepository.save(cart);
    }
}
