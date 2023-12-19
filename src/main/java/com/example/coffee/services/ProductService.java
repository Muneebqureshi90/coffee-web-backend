package com.example.coffee.services;

import com.example.coffee.dto.ProductDto;

import java.util.List;

public interface ProductService {

    ProductDto createProduct(ProductDto productDto, Integer categoryId);

    void deleteProduct(Integer productId);

    ProductDto updateProduct(Integer productId, ProductDto productDto);

    List<ProductDto> getAllProducts();

    ProductDto getProductById(Integer productId);

    List<ProductDto> addMultipleProducts(List<ProductDto> productDtos);


}
