
package com.demo.controller;



import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.demo.dto.CreateProductDTO;
import com.demo.dto.ProductDTO;
import com.demo.model.Product;
import com.demo.service.ProductService;


@RestController
@RequestMapping("/api/products")
public class ProductController {

    @Autowired
    private ProductService productService;

    /* ======================
       GET ALL
       ====================== */
    @GetMapping
    public List<ProductDTO> getAllProducts() {
        return productService.getAllProducts()
                .stream()
                .map(this::mapToDTO)
                .toList();
    }

    /* ======================
       GET BY ID
       ====================== */
    @GetMapping("/{id}")
    public ProductDTO getProductById(@PathVariable Integer id) {
        return mapToDTO(productService.getProductById(id));
    }

    /* ======================
       GET BY STATUS
       ====================== */
    @GetMapping("/status/{status}")
    public List<ProductDTO> getByStatus(@PathVariable String status) {
        return productService.getProductsByStatus(status)
                .stream()
                .map(this::mapToDTO)
                .toList();
    }

    /* ======================
       GET BY CATEGORY
       ====================== */
    @GetMapping("/category/{categoryId}")
    public List<ProductDTO> getByCategory(@PathVariable Integer categoryId) {
        return productService.getProductsByCategory(categoryId)
                .stream()
                .map(this::mapToDTO)
                .toList();
    }

    /* ======================
       GET BY CATEGORY + STATUS
       ====================== */
    @GetMapping("/category/{categoryId}/status/{status}")
    public List<ProductDTO> getByCategoryAndStatus(
            @PathVariable Integer categoryId,
            @PathVariable String status) {

        return productService.getProductsByCategoryAndStatus(categoryId, status)
                .stream()
                .map(this::mapToDTO)
                .toList();
    }

    /* ======================
       CREATE PRODUCT
       ====================== */
    @PostMapping
    public ProductDTO addProduct(@RequestBody CreateProductDTO dto) {
        return mapToDTO(productService.addProduct(dto));
    }

    /* ======================
       ADMIN: UPDATE STATUS
       ====================== */
    @PutMapping("/{id}/status/{status}")
    public ProductDTO updateStatus(
            @PathVariable Integer id,
            @PathVariable String status) {

        return mapToDTO(productService.updateProductStatus(id, status));
    }
    
    
    @PutMapping("/{id}")
    public ProductDTO updateProduct(
            @PathVariable Integer id,
            @RequestBody CreateProductDTO dto) {

        return mapToDTO(productService.updateProductById(id, dto));
    }

    

    /* ======================
       ENTITY → DTO
       ====================== */
    private ProductDTO mapToDTO(Product p) {

        ProductDTO dto = new ProductDTO();
        dto.setProductId(p.getProductId());
        dto.setProductName(p.getProductName());
        dto.setDescription(p.getDescription());
        dto.setPrice(p.getPrice());
        dto.setQuantityAvailable(p.getQuantityAvailable());
        dto.setUnit(p.getUnit());
        dto.setStatus(p.getStatus());
        dto.setImageUrl(p.getImageUrl());
        dto.setCreatedAt(p.getCreatedAt());

        if (p.getCategory() != null) {
            dto.setCategoryId(p.getCategory().getCategoryId());
            dto.setCategoryName(p.getCategory().getCategoryName());
        }

        if (p.getSeller() != null) {
            dto.setSellerId(p.getSeller().getUserId());
            dto.setSellerName(p.getSeller().getUserName());
        }

        return dto;
    }
    
    
    
    
    
    
    @GetMapping("/region/user/{userId}")
    public List<ProductDTO> getProductsForUserRegion(
            @PathVariable Integer userId) {

        return productService.getProductsForUserRegion(userId)
                .stream()
                .map(this::mapToDTO)
                .toList();
    }

}
