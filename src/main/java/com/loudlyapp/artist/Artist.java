package com.loudlyapp.artist;

import com.loudlyapp.song.Song;
import jakarta.persistence.*;
import lombok.*;

import java.util.List;

@Entity
@Data //getters,setters,hashcode,equals
@AllArgsConstructor
@NoArgsConstructor
public class Artist {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;
    private String nickname;
    private String biography;
    @OneToMany
    private List<Song> songs;
}
