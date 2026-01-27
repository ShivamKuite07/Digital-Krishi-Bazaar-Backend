package com.demo.dto;

public class CartItemDTO {

    private Integer cartItemsId;
    private Integer quantity;
    private Integer price;

    private Integer productId;
    private String productName;
    
    private String imageUrl;

    
    
	public String getImageUrl() {
		return imageUrl;
	}
	public void setImageUrl(String imageUrl) {
		this.imageUrl = imageUrl;
	}
	public Integer getCartItemsId() {
		return cartItemsId;
	}
	public void setCartItemsId(Integer cartItemsId) {
		this.cartItemsId = cartItemsId;
	}
	public Integer getQuantity() {
		return quantity;
	}
	public void setQuantity(Integer quantity) {
		this.quantity = quantity;
	}
	public Integer getPrice() {
		return price;
	}
	public void setPrice(Integer price) {
		this.price = price;
	}
	public Integer getProductId() {
		return productId;
	}
	public void setProductId(Integer productId) {
		this.productId = productId;
	}
	public String getProductName() {
		return productName;
	}
	public void setProductName(String productName) {
		this.productName = productName;
	}

    // getters & setters
    
}
