package com.loudlyapp.playlist;

import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/playlists")
@AllArgsConstructor
public class PlaylistController {

    private final PlayListService playlistService;

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

    @PostMapping("/users/{userId}")
    public Playlist createPlaylist(@PathVariable int userId, @RequestBody Playlist playlist) {
        playlist.setUserId(userId);
        return playlistService.createPlaylist(playlist);

    }

    @DeleteMapping("/{playlistId}")
    public void deletePlaylist(@PathVariable Long playlistId) {
        playlistService.deletePlaylist(playlistId);
    }
}
