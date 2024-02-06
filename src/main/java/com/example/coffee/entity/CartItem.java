package com.example.coffee.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.ToString;

import java.math.BigDecimal;

@Data
@NoArgsConstructor
@Entity
@Table(name = "cart_item")
@EqualsAndHashCode(exclude = "product") // Exclude user from hashCode and equals
@ToString(exclude = "product")
public class CartItem {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @JsonIgnore
    @ManyToOne
    private Cart cart;

    @ManyToOne()
    @JoinColumn(name = "product_id", nullable = false)
    private Product product;

//    private String size;

    private Integer quantity;

    private BigDecimal price;


    private Integer discountedPrice;

//    private Integer userId;
}
