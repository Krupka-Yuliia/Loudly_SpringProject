package com.loudlyapp.playlist;

import com.loudlyapp.song.Song;
import com.loudlyapp.song.SongRepository;
import jakarta.persistence.EntityNotFoundException;
import lombok.Getter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;



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

    public Optional<Playlist> findById(Long id) {
        return playlistRepository.findById(id);
    }

    public Playlist save(Playlist playlist) {
        return playlistRepository.save(playlist);
    }

    public void deleteById(Long id) {
        playlistRepository.deleteById(id);
    }

    public Playlist update(Long id, Playlist playlist) {
        return playlistRepository.findById(id)
                .map(existingPlaylist -> {
                    existingPlaylist.setName(playlist.getName());
                    return playlistRepository.save(existingPlaylist);
                })
                .orElseThrow(() -> new EntityNotFoundException("Playlist with id " + id + " not found"));
    }

    public List<Song> getAllSongsFromPlaylist(Long playlistId) {
        Playlist playlist = findById(playlistId).get();
        List<Song> songs = playlist.getSongs();
        return songs;
    }

    public void deleteAllSongsFromPlaylist(Long playlistId) {
        Playlist playlist = playlistRepository.findById(playlistId)
                .orElseThrow(() -> new RuntimeException("Playlist not found"));
        playlist.clearSongs();
        playlistRepository.save(playlist);
    }

    public void deleteAll() {
        playlistRepository.deleteAll();
    }

    public List<Playlist> findPlaylistsByUserId(long userId) {
        return playlistRepository.findByUserId(userId);
    }

    public Playlist addSongToPlaylist(Long playlistId, Long songId) {
        Playlist playlist = playlistRepository.findById(playlistId)
                .orElseThrow(() -> new RuntimeException("Playlist not found"));
        Song song = songRepository.findById(songId)
                .orElseThrow(() -> new RuntimeException("Song not found"));

        if (playlist.containsSong(song)) {
            throw new RuntimeException("Playlist already contains song");
        }

        playlist.addSong(song);
        return playlistRepository.save(playlist);
    }

    public void deleteSongFromPlaylist(Long playlistId, Long songId) {
        Playlist playlist = playlistRepository.findById(playlistId)
                .orElseThrow(() -> new RuntimeException("Playlist not found"));
        Song song = songRepository.findById(songId)
                .orElseThrow(() -> new RuntimeException("Song not found"));

        if (playlist.containsSong(song)) {
            playlist.removeSong(song);
            playlistRepository.save(playlist);
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
