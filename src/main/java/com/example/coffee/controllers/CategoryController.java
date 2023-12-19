package com.example.coffee.controllers;

import com.example.coffee.dto.CategoryDto;
import com.example.coffee.expections.ApiResponse;
import com.example.coffee.expections.ResourceNotFoundException;
import com.example.coffee.repository.ProductDto;
import com.example.coffee.services.CategoryService;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@SecurityRequirement(name = "scheme1")
@CrossOrigin(origins = "*")
@RestController
@RequestMapping("/api/v1/category")
@Tag(name = "Category Controller", description = "This is Category Controller")
public class CategoryController {

    @Autowired
    private CategoryService categoryService;
    @Autowired
    private ProductDto productRepository;

    @PostMapping("/product/{productId}")
    public ResponseEntity<CategoryDto> createCategory(@PathVariable("productId") Integer productId, @RequestBody CategoryDto categoryDto) {
        try {
            CategoryDto categoryDto1 = this.categoryService.createCategory(productId, categoryDto);
            return new ResponseEntity<>(categoryDto1, HttpStatus.CREATED);

        } catch (Exception e) {
            e.printStackTrace();
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);

        }
    }

    @GetMapping("/")
    public ResponseEntity<List<CategoryDto>> getAllCategories() {
        try {
            List<CategoryDto> categoryDtos = this.categoryService.getAllCategories();
            return new ResponseEntity<>(categoryDtos, HttpStatus.OK);
        } catch (Exception e) {
            e.printStackTrace();
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @GetMapping("/{categoryId}")
    public ResponseEntity<CategoryDto> getCategoryById(@PathVariable("categoryId") Integer categoryId) {
        try {
            CategoryDto categoryDto = this.categoryService.getCategoryById(categoryId);
            return new ResponseEntity<>(categoryDto, HttpStatus.OK);
        } catch (Exception e) {
            e.printStackTrace();
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);

        }
    }

    @PutMapping("/{categoryId}")
    public ResponseEntity<CategoryDto> updateCategory(
            @PathVariable("categoryId") Integer categoryId,
            @RequestBody CategoryDto categoryDto) {
        try {
            CategoryDto updatedCategoryDto = this.categoryService.updateCategory(categoryId, categoryDto);
            return new ResponseEntity<>(updatedCategoryDto, HttpStatus.OK);
        } catch (ResourceNotFoundException e) {
            e.printStackTrace();
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        } catch (Exception e) {
            e.printStackTrace();
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @DeleteMapping("/{categoryId}")
    public ResponseEntity<ApiResponse> deleteCategory(@PathVariable("categoryId") Integer categoryId) {
        categoryService.deleteCategory(categoryId);
        String status = "OK"; // Set the appropriate status
        String time = "current_timestamp"; // Set the appropriate timestamp

        ApiResponse apiResponse = new ApiResponse("Category Deleted", true, status, time);

        return new ResponseEntity<>(apiResponse, HttpStatus.OK);

    }
    }
