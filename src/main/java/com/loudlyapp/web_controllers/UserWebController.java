package com.loudlyapp.web_controllers;

import com.loudlyapp.playlist.PlayListService;
import com.loudlyapp.playlist.PlaylistDTO;
import com.loudlyapp.song.SongRecommendationService;
import com.loudlyapp.user.UserDTO;
import com.loudlyapp.user.UserService;
import jakarta.servlet.http.HttpSession;
import lombok.AllArgsConstructor;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;

import java.util.List;

@Controller
@AllArgsConstructor
public class UserWebController {

    private final UserService userService;
    private final PlayListService playlistService;
    private final SongRecommendationService songRecommendationService;

    @GetMapping("/register")
    public String showRegistrationForm(Model model) {
        model.addAttribute("userDTO", new UserDTO());
        return "user_form";
    }

    @PostMapping("/register")
    public String registerUser(@ModelAttribute UserDTO userDTO, Model model, HttpSession session) {
        try {
            userDTO.setPassword(new BCryptPasswordEncoder().encode(userDTO.getPassword()));
            UserDTO savedUser = userService.save(userDTO);
            session.setAttribute("user", savedUser);
            return String.format("redirect:/users/show/%d", savedUser.getId());
        } catch (IllegalArgumentException e) {
            model.addAttribute("errorMessage", e.getMessage());
            return "user_form";
        }
    }

    @GetMapping("/login")
    public String showLoginForm(Model model) {
        if (!model.containsAttribute("userDTO")) {
            model.addAttribute("userDTO", new UserDTO());
        }
        return "login_form";
    }

    @GetMapping(value = {"/home", "/profile", "/"})
    public String showProfile(@AuthenticationPrincipal User principal, Model model) {
        UserDTO u = userService.findByEmail(principal.getUsername());

        model.addAttribute("user", u);
        model.addAttribute("userName", u.getUsername());

        List<PlaylistDTO> playlists = playlistService.findPlaylistsByUserId(u.getId());
        model.addAttribute("playlists", playlists);
        String dailyRecommendation = songRecommendationService.getDailyRecommendation();
        model.addAttribute("dailyRecommendation", dailyRecommendation);
        return "profile";
    }

    @GetMapping("/logout")
    public String logout(HttpSession session) {
        session.invalidate();
        return "redirect:/login";
    }



}
