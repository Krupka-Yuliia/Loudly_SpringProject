package com.loudlyapp.playlist;

import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/playlists")
@AllArgsConstructor
public class PlaylistController{

    private final PlayListService playlistService;

    @GetMapping("/users/{userId}")
    public List<Playlist> getPlaylists(@PathVariable Long userId) {
        return playlistService.findPlaylistsByUserId(userId);
    }


    @PostMapping("/{playlistId}/songs/{songId}")
    public Playlist addSongToPlaylist(@PathVariable Long playlistId, @PathVariable Long songId) {
        return playlistService.addSongToPlaylist(playlistId, songId);
    }

    @DeleteMapping("/{playlistId}")
    public void deletePlaylist(@PathVariable Long playlistId) {
        playlistService.deletePlaylist(playlistId);
    }
}
