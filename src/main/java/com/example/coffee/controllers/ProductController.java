package com.example.coffee.controllers;

import com.example.coffee.dto.ProductDto;
import com.example.coffee.services.ProductService;
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
@RequestMapping("/api/v1/product")
@Tag(name = "Product Controller", description = "This is Product Controller")
public class ProductController {

    @Autowired
    private ProductService productService;

    @PostMapping("/{categoryId}")
    public ResponseEntity<ProductDto> createProduct(@RequestBody ProductDto productDto, @PathVariable("categoryId") Integer categoryId) {
        try {
            ProductDto productDto1 = this.productService.createProduct(productDto, categoryId);
            return new ResponseEntity<>(productDto1, HttpStatus.CREATED);
        } catch (Exception e) {
            e.printStackTrace();
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }


    @PutMapping("/{pId}")
    public ResponseEntity<ProductDto> updateProduct(@PathVariable("pId") Integer pId, @RequestBody ProductDto productDto) {
        try {
            ProductDto productDto1 = this.productService.updateProduct(pId, productDto);
            return new ResponseEntity<>(productDto1, HttpStatus.OK);
        } catch (Exception e) {
            e.printStackTrace();
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @GetMapping("/")
    public ResponseEntity<List<ProductDto>> getAllProduct() {
        try {
            List<ProductDto> productDto1 = this.productService.getAllProducts();
            return new ResponseEntity<>(productDto1, HttpStatus.OK);
        } catch (Exception e) {
            e.printStackTrace();
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @GetMapping("/{id}")
    public ResponseEntity<ProductDto> getProductById(@PathVariable("id") Integer id) {
        try {
            ProductDto productDto1 = this.productService.getProductById(id);
            return new ResponseEntity<>(productDto1, HttpStatus.OK);
        } catch (Exception e) {
            e.printStackTrace();
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @PostMapping("/add-multiple")
    public List<ProductDto> addMultipleProducts(@RequestBody List<ProductDto> productDtos) {
        return productService.addMultipleProducts(productDtos);
    }

}
