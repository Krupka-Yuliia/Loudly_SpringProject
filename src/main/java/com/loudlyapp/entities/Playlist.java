package com.loudlyapp.entities;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
public class Playlist {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;
    private String name;
    @Column(name = "user_id")
    private int userId;

    @ManyToMany
    @JoinTable(
            name = "playlist_songs",
            joinColumns = @JoinColumn(name = "playlist_id"),
            inverseJoinColumns = @JoinColumn(name = "song_id")
    )
    private List<Song> songs = new ArrayList<>();

    public void addSong(Song song) {
        if (songs == null) {
            songs = new ArrayList<>();
        }
        songs.add(song);
    }

    public boolean containsSong(Song song) {
        return songs != null && songs.contains(song);
    }

    public Collection<Song> getSongs() {
        return songs;
    }

    public void removeSong(Song song) {
        if (songs != null) {
            songs.remove(song);
        }
    }

}
