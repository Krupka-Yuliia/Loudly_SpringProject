package com.loudlyapp.playlist;

import com.loudlyapp.song.SongDTO;
import com.loudlyapp.user.User;
import com.loudlyapp.user.UserDTO;
import com.loudlyapp.user.UserService;
import lombok.AllArgsConstructor;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/playlists")
@AllArgsConstructor
public class PlaylistController {

    private final PlayListService playlistService;
    private final UserService userService;

    @PostMapping
    public PlaylistDTO createPlaylist(@AuthenticationPrincipal User user, @RequestBody PlaylistDTO playlistDTO) {
        Optional<UserDTO> u = userService.findByUsername(user.getUsername());
        playlistDTO.setUserId(u.get().getId());
        return playlistService.save(playlistDTO);
    }

    @GetMapping("/{playlistId}")
    public Optional<PlaylistDTO> getPlaylistById(@PathVariable long playlistId) {
        return playlistService.findById(playlistId);
    }

    @GetMapping()
    public List<PlaylistDTO> getPlaylistsByUserId(@AuthenticationPrincipal User user) {
        Optional<UserDTO> u = userService.findByUsername(user.getUsername());
        return playlistService.getPlaylists(u.get().getId());
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
