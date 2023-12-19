package com.example.coffee.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Data
@NoArgsConstructor
@Entity
@Table(name = "orders")
public class Order {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(name = "order_id")
    private String orderId;

    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user;

    @OneToMany(mappedBy = "order", cascade = CascadeType.ALL, fetch = FetchType.EAGER)
//    @JsonManagedReference("userProductReference")
    @JsonIgnore
    private List<Product> products = new ArrayList<>();

    @Column(name = "order_date")
    private LocalDateTime orderDate;

    @Column(name = "delivery_date")
    private LocalDateTime deliveryDate;


    @OneToOne
    @JoinColumn(name = "shipping_address_id")
    private Address shippingAddress;

    @Column(name = "total_price")
    private BigDecimal totalPrice;

    @Column(name = "total_discounted_price")
    private BigDecimal totalDiscountedPrice;

    @Column(name = "order_status")
    private String orderStatus;

    @Column(name = "total_items")
    private Integer totalItems;

    @Column(name = "created_at")
    private LocalDateTime createdAt;

    // Constructors, getters, setters, and other methods can be added as needed
}
