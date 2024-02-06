package com.example.coffee.dto;

import com.example.coffee.entity.Role;
import com.example.coffee.entity.User;
import jakarta.persistence.*;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.ToString;

import java.time.LocalDateTime;


@Data
@NoArgsConstructor

public class MessageDto {

    private Long id;


    private String content;


    private UserDto sender;


    private UserDto receiver;


//    private Role senderRole;


    private LocalDateTime createdAt;
}
