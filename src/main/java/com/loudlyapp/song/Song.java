package com.loudlyapp.song;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.loudlyapp.playlist.Playlist;
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
    private int artistId;
    private String genre;
    private int year;
//    @ManyToMany(mappedBy = "songs")
//    private List<Playlist> playlists = new ArrayList<>();

}
