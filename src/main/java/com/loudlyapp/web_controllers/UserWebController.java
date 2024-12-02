package com.loudlyapp.web_controllers;

import com.loudlyapp.playlist.PlayListService;
import com.loudlyapp.playlist.PlaylistDTO;
import com.loudlyapp.song.SongRecommendationService;
import com.loudlyapp.user.UserDTO;
import com.loudlyapp.user.UserService;
import com.loudlyapp.utils.ResourceNotFound;
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

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Controller
@AllArgsConstructor
public class UserWebController {

    private final UserService userService;
    private final PlayListService playlistService;
    private final SongRecommendationService songRecommendationService;

    @GetMapping("/welcome")
    public String welcome() {
        return "home";
    }

    @GetMapping("/register")
    public String showRegistrationForm(Model model) {
        model.addAttribute("userDTO", new UserDTO());
        return "user_form";
    }


    @PostMapping("/register")
    public String registerUser(@ModelAttribute UserDTO userDTO, Model model, HttpSession session) {
        List<String> errors = new ArrayList<>();

        if (userDTO.getUsername() == null || userDTO.getUsername().isBlank()) {
            errors.add("Username is required.");
        }
        if (userDTO.getPassword() == null || userDTO.getPassword().isBlank()) {
            errors.add("Password is required.");
        }
        if (userDTO.getEmail() == null || userDTO.getEmail().isBlank()) {
            errors.add("Email is required.");
        }
        if (userDTO.getRole() == null || userDTO.getRole().isBlank()) {
            errors.add("Role is required.");
        }

        if (!errors.isEmpty()) {
            model.addAttribute("errors", errors);
            return "user_form";
        }
        try {
            userDTO.setPassword(new BCryptPasswordEncoder().encode(userDTO.getPassword()));
            UserDTO savedUser = userService.save(userDTO);
            session.setAttribute("user", savedUser);
            return "redirect:/profile";
        } catch (IllegalArgumentException e) {
            model.addAttribute("errors", List.of(e.getMessage()));
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

    @GetMapping(value = {"/profile", "/"})
    public String showProfile(@AuthenticationPrincipal User principal, Model model) {
        Optional<UserDTO> u = userService.findByUsername(principal.getUsername());

        if (u.isPresent()) {
            model.addAttribute("user", u.get());

            List<PlaylistDTO> playlists = playlistService.findPlaylistsByUserId(u.get().getId());
            model.addAttribute("playlists", playlists);

            String dailyRecommendation = songRecommendationService.getDailyRecommendation();
            model.addAttribute("dailyRecommendation", dailyRecommendation);
        } else {
            throw new ResourceNotFound("User not found.");
        }

        return "profile";
    }


    @GetMapping("/logout")
    public String logout(HttpSession session) {
        session.invalidate();
        return "redirect:/login";
    }
}
