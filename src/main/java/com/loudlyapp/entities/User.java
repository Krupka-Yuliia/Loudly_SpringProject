package com.loudlyapp.entities;

import jakarta.persistence.*;
import lombok.*;
import java.util.List;

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
    @OneToMany
    private List<Playlist> playlists;
}

