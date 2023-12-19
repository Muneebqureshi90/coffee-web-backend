package com.example.coffee.repository;

import com.example.coffee.entity.Product;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ProductDto extends JpaRepository<Product,Integer> {
}
