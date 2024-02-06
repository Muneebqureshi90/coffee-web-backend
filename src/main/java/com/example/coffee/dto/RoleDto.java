package com.example.coffee.dto;

import com.example.coffee.entity.Message;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Set;

@Data
@NoArgsConstructor
public class RoleDto {

    private Integer id;
    private String name;
//    private Set<Message> messages;
}
