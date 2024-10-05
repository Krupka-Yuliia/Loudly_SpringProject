package com.loudlyapp.user;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Data //getters,setters,hashcode,equals
@AllArgsConstructor
@NoArgsConstructor
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;
    private String username;
    private String password;
    private String email;
    private String role;
}

