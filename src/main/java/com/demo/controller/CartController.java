package com.demo.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.demo.dto.CartDTO;
import com.demo.dto.CartItemDTO;
import com.demo.model.Cart;
import com.demo.service.CartService;

@RestController
@RequestMapping("/api/cart")
public class CartController {

    @Autowired
    private CartService cartService;

    /* ======================
       GET CART BY ID
       ====================== */
    @GetMapping("/{id}")
    public CartDTO getCartById(@PathVariable Integer id) {
        return mapToDTO(cartService.getCartById(id));
    }

    /* ======================
       GET CART BY USER
       ====================== */
    @GetMapping("/user/{userId}")
    public CartDTO getCartByUser(@PathVariable Integer userId) {
        return mapToDTO(cartService.getCartByUser(userId));
    }

    /* ======================
       ADD PRODUCT
       ====================== */
    @PostMapping("/add/product/{productId}")
    public CartDTO addProduct(
            @RequestParam Integer userId,
            @PathVariable Integer productId) {

        return mapToDTO(cartService.addProductToCart(userId, productId));
    }

    /* ======================
       REMOVE PRODUCT
       ====================== */
    @DeleteMapping("/remove/product/{productId}")
    public CartDTO removeProduct(
            @RequestParam Integer userId,
            @PathVariable Integer productId) {

        return mapToDTO(cartService.removeProductFromCart(userId, productId));
    }
    
    
    /* ======================
    DECREMENT PRODUCT
    ====================== */
	 @PostMapping("/decrement/product/{productId}")
	 public CartDTO decrementProduct(
	         @RequestParam Integer userId,
	         @PathVariable Integer productId) {
	
	     return mapToDTO(
	         cartService.decrementProductFromCart(userId, productId)
	     );
	 }


    /* ======================
       ENTITY → DTO
       ====================== */
    private CartDTO mapToDTO(Cart cart) {

        CartDTO dto = new CartDTO();
        dto.setCartId(cart.getCartId());
        dto.setStatus(cart.getStatus());
        dto.setCreatedAt(cart.getCreatedAt());

        dto.setUserId(cart.getUser().getUserId());
        dto.setUserName(cart.getUser().getUserName());

        List<CartItemDTO> items = cart.getCartItems()
                .stream()
                .map(ci -> {
                    CartItemDTO itemDTO = new CartItemDTO();
                    itemDTO.setCartItemsId(ci.getCartItemsId());
                    itemDTO.setQuantity(ci.getQuantity());
                    itemDTO.setPrice(ci.getPrice());
                    itemDTO.setProductId(ci.getProduct().getProductId());
                    itemDTO.setProductName(ci.getProduct().getProductName());
                    itemDTO.setImageUrl(ci.getProduct().getImageUrl());
                    return itemDTO;
                })
                .toList();

        dto.setCartItems(items);
        return dto;
    }
}

