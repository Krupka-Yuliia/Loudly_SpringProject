package com.loudlyapp.playlist;

import com.loudlyapp.song.Song;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;

@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
public class Playlist {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;
    private String name;

    @Column(name = "user_id")
    private int userId;

    @ManyToMany(fetch = FetchType.EAGER)
    @JoinTable(
            name = "playlist_songs",
            joinColumns = @JoinColumn(name = "playlist_id"),
            inverseJoinColumns = @JoinColumn(name = "song_id")
    )
    private List<Song> songs = new ArrayList<>();

    public void addSong(Song song) {
        songs.add(song);
    }

    public boolean containsSong(Song song) {
        return songs.contains(song);
    }

    public void removeSong(Song song) {
        songs.remove(song);
    }

    public void clearSongs() {
        songs.clear();
    }
}
