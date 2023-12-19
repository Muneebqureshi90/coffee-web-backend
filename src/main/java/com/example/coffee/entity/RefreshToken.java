package com.example.coffee.entity;

import jakarta.persistence.*;
import lombok.*;

import java.time.Instant;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Table(name = "refresh_token")
@EqualsAndHashCode(exclude = "user") // Exclude user from hashCode and equals
@ToString(exclude = "user")
public class RefreshToken {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer tokenId;


    private String refreshToken;
    private Instant expiry;
    // Define a bidirectional relationship with User
    @OneToOne
    @JoinColumn(name = "user_id") // Specify the foreign key column
    private User user;
}
