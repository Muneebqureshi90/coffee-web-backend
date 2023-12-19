package com.example.coffee.serviceImpl;

import com.example.coffee.dto.ProductDto;
import com.example.coffee.entity.Category;
import com.example.coffee.entity.Product;
import com.example.coffee.expections.ResourceNotFoundException;
import com.example.coffee.repository.CategoryRepository;
import com.example.coffee.services.ProductService;
import org.hibernate.Hibernate;
import org.modelmapper.ModelMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class ProductServiceImpl implements ProductService {

    @Autowired
    private ModelMapper modelMapper;
    @Autowired
    private com.example.coffee.repository.ProductDto productRepository;
    @Autowired
    private CategoryRepository categoryRepository;
    private static final Logger logger = LoggerFactory.getLogger(ProductServiceImpl.class);

    @Override
    public ProductDto createProduct(ProductDto productDto, Integer categoryId) {
        try {
            // Fetch the managed Category entity by its ID
            Category category = categoryRepository.findById(categoryId)
                    .orElseThrow(() -> new ResourceNotFoundException("Category", "id", categoryId.toString()));

            // Map ProductDto to Product and set the managed Category
            Product product = this.modelMapper.map(productDto, Product.class);
            product.setCategory(category);
            product.setCreatedAt(LocalDateTime.now());

            if (productDto.getImageUrl() == null || productDto.getImageUrl().isEmpty()) {
                product.setImageUrl("default.png");
            } else {
                product.setImageUrl(productDto.getImageUrl());
            }

            // Save the Product entity
            Product savedProduct = productRepository.save(product);

            // Map the saved Product entity to a ProductDto for the response
            return modelMapper.map(savedProduct, ProductDto.class);
        } catch (Exception e) {
            // Log the exception for debugging
            throw new RuntimeException("Error creating product: " + e.getMessage(), e);
        }
    }



    @Override
    public void deleteProduct(Integer productId) {
        try {
            this.productRepository.deleteById(productId);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public ProductDto updateProduct(Integer productId, ProductDto productDto) {
        try {
            // Retrieve the existing product from the database
            Product existingProduct = productRepository.findById(productId)
                    .orElseThrow(() -> new ResourceNotFoundException("Product", "id", productId.toString()));

            // Update the existing product with data from ProductDto
            existingProduct.setTitle(productDto.getTitle());
            existingProduct.setDiscription(productDto.getDiscription());
            existingProduct.setAmount(productDto.getAmount());

            // Only update discountAmount if the new value is greater than the current value
            BigDecimal newDiscountAmount = productDto.getDiscount_amount();
            if (newDiscountAmount != null && newDiscountAmount.compareTo(existingProduct.getDiscount_amount()) > 0) {
                existingProduct.setDiscount_amount(newDiscountAmount);
            }

            // Update category if the new category ID is not null

            // Update category if the new category ID is not null
            Long newCategoryId = productDto.getCategory().getId();
            if (newCategoryId != null) {
                Integer categoryId = newCategoryId.intValue(); // Convert Long to Integer
                existingProduct.setCategory(categoryRepository.findById(categoryId)
                        .orElseThrow(() -> new ResourceNotFoundException("Category", "id", categoryId.toString())));
            }


            existingProduct.setImageUrl(productDto.getImageUrl());

            // Only update quantity if the new value is greater than or equal to 0
            Integer newQuantity = productDto.getQuantity();
            if (newQuantity != null && newQuantity >= 0) {
                existingProduct.setQuantity(newQuantity);
            }

            // Save the updated product
            productRepository.save(existingProduct);

            // Map the updated product entity back to a ProductDto for the response
            return modelMapper.map(existingProduct, ProductDto.class);
        } catch (ResourceNotFoundException e) {
            // Log the exception for debugging
            logger.error("Product not found: {}", e.getMessage(), e);

            // Rethrow the exception or handle it as appropriate
            throw e;
        } catch (Exception e) {
            // Log the exception for debugging
            throw new RuntimeException("Error updating product: " + e.getMessage(), e);
        }
    }


    @Override
    public List<ProductDto> getAllProducts() {
        List<Product> products = productRepository.findAll();

        // Map the list of Product entities to a list of ProductDto objects
        List<ProductDto> productDtos = products.stream()
                .map(product -> modelMapper.map(product, ProductDto.class))
                .collect(Collectors.toList());

        return productDtos;
    }

    @Override
    public ProductDto getProductById(Integer productId) {
        Product product = productRepository.findById(productId)
                .orElseThrow(() -> new ResourceNotFoundException("Product", "id", productId.toString()));
//        Hibernate.initialize(product.getReviews());
//        Hibernate.initialize(product.getRatings());
        // Map the Product entity to a ProductDto object
        return modelMapper.map(product, ProductDto.class);
    }

    @Override
    public List<ProductDto> addMultipleProducts(List<ProductDto> productDtos) {
        try {
            // Map each ProductDto to a Product entity and save it to the database
            List<Product> products = productDtos.stream()
                    .map(productDto -> {
                        Product product = modelMapper.map(productDto, Product.class);
                        // Set any additional properties or perform validation if needed
                        return product;
                    })
                    .collect(Collectors.toList());

            productRepository.saveAll(products);

            // Map the saved Product entities back to ProductDto objects for the response
            return products.stream()
                    .map(product -> modelMapper.map(product, ProductDto.class))
                    .collect(Collectors.toList());
        } catch (Exception e) {
            // Log the exception for debugging
            throw new RuntimeException("Error adding multiple products: " + e.getMessage(), e);
        }
    }
}
