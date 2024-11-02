package com.loudlyapp.playlist;

import com.loudlyapp.song.SongDTO;
import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/playlists")
@AllArgsConstructor
public class PlaylistController {

    private final PlayListService playlistService;

    @PostMapping("/users/{userId}")
    public PlaylistDTO createPlaylist(@PathVariable int userId, @RequestBody PlaylistDTO playlistDTO) {
        playlistDTO.setUserId(userId);
        return playlistService.save(playlistDTO);
    }

    @GetMapping("/{playlistId}")
    public Optional<PlaylistDTO> getPlaylistById(@PathVariable long playlistId) {
        return playlistService.findById(playlistId);
    }

    @GetMapping
    public List<PlaylistDTO> getPlaylists() {
        return playlistService.findAll();
    }

    @GetMapping("/users/{userId}")
    public List<PlaylistDTO> getPlaylistsByUserId(@PathVariable Long userId) {
        return playlistService.findPlaylistsByUserId(userId);
    }

    @PostMapping("/{playlistId}/songs/{songId}")
    public PlaylistDTO addSongToPlaylist(@PathVariable Long playlistId, @PathVariable Long songId) {
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
    public List<SongDTO> getPlaylistSongs(@PathVariable Long playlistId) {
        return playlistService.getAllSongsFromPlaylist(playlistId);
    }

    @PutMapping("/{playlistId}")
    public PlaylistDTO updatePlaylist(@PathVariable Long playlistId, @RequestBody PlaylistDTO playlistDTO) {
        return playlistService.update(playlistId, playlistDTO);
    }

    @DeleteMapping("/{playlistId}/songs/{songId}")
    public void deleteSongFromPlaylist(@PathVariable Long playlistId, @PathVariable Long songId) {
        playlistService.deleteSongFromPlaylist(playlistId, songId);
    }
}
