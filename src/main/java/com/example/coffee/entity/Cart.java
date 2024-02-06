package com.example.coffee.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import jakarta.persistence.*;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.ToString;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Data
@NoArgsConstructor
@Entity
@Table(name = "cart")
@EqualsAndHashCode(exclude = "user") // Exclude user from hashCode and equals
@ToString(exclude = "user")
public class Cart {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @OneToOne
    @JoinColumn(name = "user_id")
    private User user;


    //    orphanRemoval = true  if some one delete the item  than it will delete from database
    @OneToMany(mappedBy = "cart", cascade = CascadeType.ALL, fetch = FetchType.EAGER)
    @Column(name = "cart_items")
    private Set<CartItem> cartItems = new HashSet<>(); // Initialize as an ArrayList

    //
    @Column(name = "total_price")
    private double totalPrice;
    //
    @Column(name = "total_items")
    private Integer totalItems;
    //
    private Integer totalDiscountedPrice;
    private Integer discounte;


}
