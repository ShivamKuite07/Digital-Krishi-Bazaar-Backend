package com.demo.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.demo.model.Category;
import com.demo.model.Product;

public interface CategoryRespository extends JpaRepository<Category, Integer>{
	
	List<Product> findProductsByCategoryName(String categoryName);

}
