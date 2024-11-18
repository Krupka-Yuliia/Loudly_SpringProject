package com.loudlyapp.web_controllers;

import com.loudlyapp.artist.ArtistDTO;
import com.loudlyapp.artist.ArtistService;
import com.loudlyapp.playlist.PlayListService;
import com.loudlyapp.playlist.PlaylistDTO;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;

@AllArgsConstructor
@Controller
public class PlaylistWebController {
    private final PlayListService playListService;
    private final ArtistService artistService;

    @GetMapping("/playlists/show/{playlistId}")
    public String getPlaylist(Model model, @PathVariable long playlistId) {
        Optional<PlaylistDTO> playlistDTO = playListService.findById(playlistId);
        if (playlistDTO.isPresent()) {
            PlaylistDTO currentPlaylistDTO = playlistDTO.get();

            currentPlaylistDTO.getSongs().forEach(songDTO -> {
                Optional<ArtistDTO> artistDTO = artistService.findById(songDTO.getArtistId());
                artistDTO.ifPresent(artist -> songDTO.setArtistName(artist.getNickname()));
            });

            model.addAttribute("playlist", currentPlaylistDTO);
        } else {
            model.addAttribute("error", "Playlist not found.");
        }
        return "playlist";
    }

    @GetMapping("/add_playlist")
    public String showAddPlaylistForm(@RequestParam int userId, Model model) {
        model.addAttribute("playlist", new PlaylistDTO());
        model.addAttribute("userId", userId);
        return "add_playlist_form";
    }


    @PostMapping("/playlists")
    public String addPlaylist(
            @ModelAttribute("playlist") PlaylistDTO playlistDTO,
            @RequestParam int userId) {
        playlistDTO.setUserId(userId);
        playListService.save(playlistDTO);
        return "redirect:/users/show/" + userId;
    }

}