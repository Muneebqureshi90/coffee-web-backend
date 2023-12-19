package com.example.coffee.dto;

import com.example.coffee.entity.Product;
import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import jakarta.persistence.PrePersist;
import jakarta.persistence.PreUpdate;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.PastOrPresent;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Data
@NoArgsConstructor
public class CategoryDto {

    @NotNull(message = "Category ID cannot be null")
    private Long id;

    @NotBlank(message = "Category name cannot be blank")
    private String name;

    @NotBlank(message = "Category description cannot be blank")
    private String description;

    // LocalDateTime fields don't usually need validation annotations

    @PastOrPresent(message = "Created at date must be in the past or present")
    private LocalDateTime createdAt;

    @PastOrPresent(message = "Updated at date must be in the past or present")
    private LocalDateTime updatedAt;

    @Valid // Ensures that each ProductDto in the list is also validated
    @JsonManagedReference("categoryReference")

    @NotEmpty(message = "Products list cannot be empty")
    private List<@Valid ProductDto> products = new ArrayList<>();

    // Constructors, getters, setters, and other methods

    // You can add JPA lifecycle callbacks for createdAt and updatedAt
    @PrePersist
    protected void onCreate() {
        this.createdAt = LocalDateTime.now();
        this.updatedAt = this.createdAt;
    }

    @PreUpdate
    protected void onUpdate() {
        this.updatedAt = LocalDateTime.now();
    }
}
