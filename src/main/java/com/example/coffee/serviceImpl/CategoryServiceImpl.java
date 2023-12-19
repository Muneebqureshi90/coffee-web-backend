package com.example.coffee.serviceImpl;


import com.example.coffee.controllers.AddressController;
import com.example.coffee.dto.CategoryDto;
import com.example.coffee.dto.UserDto;
import com.example.coffee.entity.Category;
import com.example.coffee.entity.Product;
import com.example.coffee.entity.User;
import com.example.coffee.expections.ResourceNotFoundException;
import com.example.coffee.repository.CategoryRepository;
import com.example.coffee.repository.ProductDto;
import com.example.coffee.services.CategoryService;
import jakarta.transaction.Transactional;
import org.modelmapper.ModelMapper;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class CategoryServiceImpl implements CategoryService {


    @Autowired
    private ModelMapper modelMapper;
    @Autowired
    private CategoryRepository categoryRepository;
    @Autowired
    private ProductDto productRepository;


    private static final Logger logger = LoggerFactory.getLogger(CategoryDto.class);


//    @Override
//    public CategoryDto createCategory(Integer productId, CategoryDto categoryDto) {
//
//        try {
//            Product product = this.productRepository.findById(productId)
//                    .orElseThrow(() -> new ResourceNotFoundException("product", "id", productId.toString()));
//
//            Category category = this.modelMapper.map(categoryDto, Category.class);
//
//            List<Product> productList = new ArrayList<>();
//            productList.add(product);
//
//            category.setProducts(productList);
//
//            Category category1 = categoryRepository.save(category);
//            // Your logic for saving the category entity goes here
//
//            return modelMapper.map(category1, CategoryDto.class);
//        } catch (DataAccessException ex) {
//            // Log the exception for debugging
//            logger.error("Error saving rating data: {}", ex.getMessage(), ex);
//
//            // Throw a custom exception or handle it as appropriate
//            throw new RuntimeException("Error saving rating data: " + ex.getMessage(), ex);
//        } catch (ResourceNotFoundException ex) {
//            // Log the exception for debugging
//            logger.error("Error fetching user or product data: {}", ex.getMessage(), ex);
//
//            // Rethrow the exception or handle it as appropriate
//            throw ex;
//        } catch (Exception ex) {
//            // Log the exception for debugging
//            logger.error("Unexpected error: {}", ex.getMessage(), ex);
//
//            // Throw a custom exception or handle it as appropriate
//            throw new RuntimeException("Unexpected error: " + ex.getMessage(), ex);
//        }
//    }

    @Override
    @Transactional
    public CategoryDto createCategory(Integer productId, CategoryDto categoryDto) {
        try {
            Product product = productRepository.findById(productId)
                    .orElseThrow(() -> new ResourceNotFoundException("Product", "id", productId.toString()));

            Category category = modelMapper.map(categoryDto, Category.class);
            category.setProducts(List.of(product)); // Ensure the product is managed
            Category saveUser = categoryRepository.save(category);
            return modelMapper.map(saveUser, CategoryDto.class);
            // Additional category creation logic...


        } catch (Exception e) {
            // Handle exceptions appropriately
            throw new RuntimeException("Error creating category: " + e.getMessage(), e);
        }
    }


    @Override
    @Transactional
    public CategoryDto updateCategory(Integer categoryId, CategoryDto categoryDto) {
        try {
            // Find the existing Category entity
            Category existingCategory = this.categoryRepository.findById(categoryId)
                    .orElseThrow(() -> new ResourceNotFoundException("category", "id", categoryId.toString()));

            // Map properties from CategoryDto to existing Category entity
            this.modelMapper.map(categoryDto, existingCategory);

            // Save the updated Category entity
            Category updatedCategory = this.categoryRepository.save(existingCategory);

            // Map the updated Category entity back to a CategoryDto and return it
            return this.modelMapper.map(updatedCategory, CategoryDto.class);
        } catch (Exception e) {
            // Log the exception for debugging
            logger.error("Error updating category: {}", e.getMessage(), e);

            // Rethrow the exception or handle it as appropriate
            throw new RuntimeException("Error updating category: " + e.getMessage(), e);
        }
    }


    @Override
    public void deleteCategory(Integer categoryId) {

        if (categoryRepository.existsById(categoryId)) {
            // Delete the address by ID
            categoryRepository.deleteById(categoryId);
        } else {
            // If the address does not exist, throw an exception or handle it as needed
            throw new ResourceNotFoundException("Address", "id", categoryId.toString());
        }
    }

    @Override
    public List<CategoryDto> getAllCategories() {

        List<Category> category = this.categoryRepository.findAll();

        List<CategoryDto> categoryDtos = category.stream()
                .map(address -> modelMapper.map(category, CategoryDto.class))
                .collect(Collectors.toList());


        return categoryDtos;
    }

    @Override
    public CategoryDto getCategoryById(Integer categoryId) {

        Category category = this.categoryRepository.findById(categoryId)
                .orElseThrow(() -> new ResourceNotFoundException("Category", "id", categoryId.toString()));

        CategoryDto categoryDto = modelMapper.map(category, CategoryDto.class);

        return categoryDto;
    }
}
