package com.loudlyapp.controllers;

import com.loudlyapp.entities.Playlist;
import com.loudlyapp.services.PlayListService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/playlists")
public class PlaylistController {

    @Autowired
    private PlayListService playlistService;

    @GetMapping("/users/{userId}/playlists")
    public ResponseEntity<List<Playlist>> getPlaylists(@PathVariable Long userId) {
        List<Playlist> playlists = playlistService.findPlaylistsByUserId(userId);
        return ResponseEntity.ok(playlists);
    }
    @PostMapping("/{playlistId}/songs/{songId}")
    public ResponseEntity<Playlist> addSongToPlaylist(@PathVariable Long playlistId, @PathVariable Long songId) {
        Playlist updatedPlaylist = playlistService.addSongToPlaylist(playlistId, songId);
        return ResponseEntity.ok(updatedPlaylist);
    }

    @DeleteMapping("/{playlistId}")
    public ResponseEntity<Void> deletePlaylist(@PathVariable Long playlistId) {
        playlistService.deletePlaylist(playlistId);
        return ResponseEntity.noContent().build();
    }
}
