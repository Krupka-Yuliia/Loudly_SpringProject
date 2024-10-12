package com.loudlyapp.playlist;

import com.loudlyapp.song.Song;
import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/playlists")
@AllArgsConstructor
public class PlaylistController {

    @Autowired
    private final PlayListService playlistService;

    @PostMapping("/users/{userId}")
    public Playlist createPlaylist(@PathVariable int userId, @RequestBody Playlist playlist) {
        playlist.setUserId(userId);
        return playlistService.save(playlist);

    }

    @GetMapping("/{playlistId}")
    public Optional<Playlist> getPlaylistById(@PathVariable long playlistId) {
        return playlistService.findById(playlistId);
    }

    @GetMapping
    public List<Playlist> getPlaylists() {
        return playlistService.findAll();
    }

    @GetMapping("/users/{userId}")
    public List<Playlist> getPlaylistsByUserId(@PathVariable Long userId) {
        return playlistService.findPlaylistsByUserId(userId);
    }

    @PostMapping("/{playlistId}/songs/{songId}")
    public Playlist addSongToPlaylist(@PathVariable Long playlistId, @PathVariable Long songId) {
        return playlistService.addSongToPlaylist(playlistId, songId);
    }


    @DeleteMapping
    public void deleteAllPlaylists() {
        playlistService.deleteAll();
    }


    @DeleteMapping("/{playlistId}")
    public void deletePlaylistById(@PathVariable Long playlistId) {
        playlistService.deleteById(playlistId);
    }

    @DeleteMapping("/{playlistId}/songs")
    public void deletePlaylistSongs(@PathVariable Long playlistId) {
        playlistService.deleteAllSongsFromPlaylist(playlistId);
    }

    @GetMapping("/{playlistId}/songs")
    public List<Song> getPlaylistSongs(@PathVariable Long playlistId) {
        return playlistService.getAllSongsFromPlaylist(playlistId);
    }

    @PutMapping("/{playlistId}")
    public Playlist updatePlaylist(@PathVariable Long playlistId, @RequestBody Playlist playlist) {
        Playlist updatedPlaylist = playlistService.update(playlistId, playlist);
        return updatedPlaylist;
    }

    @DeleteMapping("/{playlistId}/songs/{songId}")
    public void deleteSongFromPlaylist(
            @PathVariable Long playlistId,
            @PathVariable Long songId) {
        playlistService.deleteSongFromPlaylist(playlistId, songId);
    }



}