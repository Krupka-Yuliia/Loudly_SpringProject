package com.loudlyapp.web_controllers;

import com.loudlyapp.playlist.PlayListService;
import com.loudlyapp.playlist.PlaylistDTO;
import com.loudlyapp.user.UserDTO;
import com.loudlyapp.user.UserService;
import com.loudlyapp.utils.ResourceNotFound;
import com.loudlyapp.utils.SongNotFoundException;
import lombok.AllArgsConstructor;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.User;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.List;
import java.util.Optional;

@AllArgsConstructor
@Controller
public class PlaylistWebController {
    private final PlayListService playListService;
    private final UserService userService;

    @GetMapping("/playlists")
    public String getAllPlaylists(@AuthenticationPrincipal User principal, Model model) {
        Optional<UserDTO> user = userService.findByUsername(principal.getUsername());
        List<PlaylistDTO> playlists = playListService.getPlaylists(user.get().getId());
        model.addAttribute("playlists", playlists);
        return "playlists";
    }

    @GetMapping("/playlists/{playlistId}")
    public String getPlaylist(Model model, @PathVariable long playlistId) {
        Optional<PlaylistDTO> playlist = playListService.findById(playlistId);
        if (playlist.isPresent()) {
            model.addAttribute("playlist", playlist.get());
            return "playlist";
        } else {
            throw new ResourceNotFound("Playlist not found.");
        }
    }

    @GetMapping("/add_playlist")
    public String showAddPlaylistForm(@AuthenticationPrincipal User principal, Model model) {
        Optional<UserDTO> user = userService.findByUsername(principal.getUsername());
        model.addAttribute("playlist", new PlaylistDTO());
        model.addAttribute("userId", user.get().getId());
        return "add_playlist_form";
    }


    @PostMapping("/playlists")
    public String addPlaylist(
            @ModelAttribute("playlist") PlaylistDTO playlistDTO,
            @RequestParam int userId
    ) {
        playlistDTO.setUserId(userId);
        playListService.save(playlistDTO);
        return "redirect:/playlists";
    }

    @PostMapping("/playlists/add_song")
    public String addSongToPlaylist(
            @RequestParam Long playlistId,
            @RequestParam Long songId,
            RedirectAttributes redirectAttributes
    ) {
        boolean songExists = playListService.isSongInPlaylist(playlistId, songId);

        if (songExists) {
            redirectAttributes.addFlashAttribute("error", "Song is already in this playlist");
        } else {
            playListService.addSongToPlaylist(playlistId, songId);
            redirectAttributes.addFlashAttribute("success", "Song added to playlist successfully");
        }

        return "redirect:/songs/" + songId;
    }

}