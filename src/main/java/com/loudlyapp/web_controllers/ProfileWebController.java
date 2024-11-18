package com.loudlyapp.web_controllers;

import com.loudlyapp.playlist.PlayListService;
import com.loudlyapp.playlist.PlaylistDTO;
import com.loudlyapp.song.SongRecommendationService;
import com.loudlyapp.user.UserDTO;
import com.loudlyapp.user.UserService;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import java.util.List;
import java.util.Optional;

@AllArgsConstructor
@Controller
public class ProfileWebController {
    private final PlayListService playlistService;
    private final UserService userService;
    private final SongRecommendationService songRecommendationService;

    @GetMapping("users/show/{userId}")
    public String getUserProfile(@PathVariable("userId") long userId, Model model) {
        Optional<UserDTO> user = userService.findById(userId);
        if (user.isPresent()) {
            model.addAttribute("userName", user.get().getUsername());
        } else {
            model.addAttribute("error", "User not found.");
        }

        List<PlaylistDTO> playlists = playlistService.findPlaylistsByUserId(userId);
        model.addAttribute("playlists", playlists);

        String dailyRecommendation = songRecommendationService.getDailyRecommendation();
        model.addAttribute("dailyRecommendation", dailyRecommendation);

        return "profile";
    }
}
