package com.loudlyapp.entities;

import jakarta.persistence.*;
import lombok.*;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Entity
@Data //getters,setters,hashcode,equals
@AllArgsConstructor
@NoArgsConstructor
public class Song {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;
    private String title;
    private byte[] file;
    private String format;
    private String artistId;
    private String genre;
    private Date year;
    @ManyToMany(mappedBy = "songs")
    private List<Playlist> playlists = new ArrayList<>();

}
