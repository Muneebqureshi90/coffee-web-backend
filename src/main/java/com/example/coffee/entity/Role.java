package com.example.coffee.entity;


import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Set;

@Entity
@Data
@NoArgsConstructor
@Table(name = "role")
public class Role {
    @Id
    private Integer id;
    private String name;
//    @ManyToMany(mappedBy = "roles")
//    private Set<User> users;

    @OneToMany(mappedBy = "role", fetch = FetchType.EAGER)
    private Set<User> users;
}
