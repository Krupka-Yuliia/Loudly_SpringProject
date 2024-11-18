package com.loudlyapp.web_controllers;

import com.loudlyapp.playlist.PlayListService;
import com.loudlyapp.playlist.PlaylistDTO;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import java.util.List;

@AllArgsConstructor
@Controller
public class PlaylistsController {
    private final PlayListService playlistService;

    @GetMapping("users/show/{userId}/playlists/show")
    public String getAllPlaylistsByUserId(@PathVariable("userId") long userId, Model model) {
        List<PlaylistDTO> playlists = playlistService.findPlaylistsByUserId(userId);
        model.addAttribute("playlists", playlists);
        return "playlists";
    }
}