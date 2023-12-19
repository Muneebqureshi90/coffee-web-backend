package com.example.coffee.services;

import com.example.coffee.dto.CategoryDto;

import java.util.List;

public interface CategoryService {

    CategoryDto createCategory(Integer productId, CategoryDto categoryDto);

    CategoryDto updateCategory (Integer categoryId, CategoryDto categoryDto);

    void deleteCategory(Integer categoryId);

    List<CategoryDto> getAllCategories();

    CategoryDto getCategoryById(Integer categoryId);

}
