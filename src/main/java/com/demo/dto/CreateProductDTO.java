package com.demo.dto;

public class CreateProductDTO {

    private String productName;
    private String description;
    private Integer price;
    private Integer quantityAvailable;
    private String unit;
    private String imageUrl;

    private Integer categoryId;
    private Integer sellerId;
    
    
	public String getProductName() {
		return productName;
	}
	public void setProductName(String productName) {
		this.productName = productName;
	}
	public String getDescription() {
		return description;
	}
	public void setDescription(String description) {
		this.description = description;
	}
	public Integer getPrice() {
		return price;
	}
	public void setPrice(Integer price) {
		this.price = price;
	}
	public Integer getQuantityAvailable() {
		return quantityAvailable;
	}
	public void setQuantityAvailable(Integer quantityAvailable) {
		this.quantityAvailable = quantityAvailable;
	}
	public String getUnit() {
		return unit;
	}
	public void setUnit(String unit) {
		this.unit = unit;
	}
	public String getImageUrl() {
		return imageUrl;
	}
	public void setImageUrl(String imageUrl) {
		this.imageUrl = imageUrl;
	}
	public Integer getCategoryId() {
		return categoryId;
	}
	public void setCategoryId(Integer categoryId) {
		this.categoryId = categoryId;
	}
	public Integer getSellerId() {
		return sellerId;
	}
	public void setSellerId(Integer sellerId) {
		this.sellerId = sellerId;
	}

    // getters & setters
    
}
