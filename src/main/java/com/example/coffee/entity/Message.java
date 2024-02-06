package com.example.coffee.entity;

import jakarta.persistence.*;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.ToString;

import java.time.LocalDateTime;

@Entity
@Table(name = "message")
@Data
@NoArgsConstructor
//@EqualsAndHashCode(exclude = "user") // Exclude user from hashCode and equals
//@ToString(exclude = "user")
public class Message {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "content")
    private String content;

    @ManyToOne
    @JoinColumn(name = "sender_id")
    private User sender;

    @ManyToOne
    @JoinColumn(name = "receiver_id")
    private User receiver;

//    @ManyToOne
//    @JoinColumn(name = "role_id")
//    private Role senderRole;

    @Column(name = "created_at")
    private LocalDateTime createdAt;
}
