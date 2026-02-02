package com.demo.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.demo.model.Product;

@Repository
public interface ProductRepository extends JpaRepository<Product, Integer> {

    List<Product> findByStatus(String status);

    List<Product> findByCategory_CategoryId(Integer categoryId);

    List<Product> findByCategory_CategoryIdAndStatus(Integer categoryId, String status);
    
    List<Product> findBySeller_UserId(Integer userId);
    
    
    List<Product> findBySeller_State_StateIdAndStatus(
            Integer stateId,
            String status
    );
}
