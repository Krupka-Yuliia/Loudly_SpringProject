package com.loudlyapp.playlist;

import com.loudlyapp.song.Song;
import com.loudlyapp.song.SongRepository;
import lombok.Getter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;


//delete playlist by id
//create a playlist
//add a song to the playlist
//delete a song from a playlist

@Service
public class PlayListService {

    @Getter
    @Autowired
    private PlaylistRepository playlistRepository;

    @Autowired
    private SongRepository songRepository;

    public List<Playlist> findAll() {
        return playlistRepository.findAll();
    }

    public Playlist findByName(String name) {
        return playlistRepository.findByName(name);
    }

    public Optional<Playlist> findById(Long id) {
        return playlistRepository.findById(id);
    }

    public Playlist createPlaylist(Playlist playlist) {
        return playlistRepository.save(playlist);
    }
    public void deletePlaylist(long playlist) {
        playlistRepository.deleteById(playlist);
    }

    public List<Playlist> findPlaylistsByUserId(long userId) {
        List<Playlist> playlists = playlistRepository.findByUserId(userId);
        return playlists;
    }

    public Playlist addSongToPlaylist(Long playlistId, Long songId) {
        Playlist playlist = playlistRepository.findById(playlistId)
                .orElseThrow(() -> new RuntimeException("Playlist not found"));
        Song song = songRepository.findById(songId)
                .orElseThrow(() -> new RuntimeException("Song not found"));

        if (playlist.containsSong(song)) {
            throw new RuntimeException("Playlist already contains song");
        } else {
            playlist.addSong(song);
        }
        return playlistRepository.save(playlist);
    }

    public void deleteSongFromPlaylist(Long playlistId, Long songId) {
        Playlist playlist = playlistRepository.findById(playlistId)
                .orElseThrow(() -> new RuntimeException("Playlist not found"));
        Song song = songRepository.findById(songId)
                .orElseThrow(() -> new RuntimeException("Song not found"));

        if (playlist.containsSong(song)) {
            playlist.removeSong(song);
        } else {
            throw new RuntimeException("Playlist does not have contains song");
        }
    }

    public List<Playlist> getPlaylistsByUserId(Long userId) {
        if (userId == null) {
            throw new RuntimeException("UserId cannot be null");
        }
        else{
            List<Playlist> playlists = new ArrayList<>();
        }
        return null;
    }



}
