package com.loudlyapp.web_controllers;

import com.loudlyapp.playlist.PlayListService;
import com.loudlyapp.playlist.PlaylistDTO;
import com.loudlyapp.song.SongRecommendationService;
import com.loudlyapp.user.UserDTO;
import com.loudlyapp.user.UserService;
import jakarta.servlet.http.HttpSession;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;

import java.util.List;
import java.util.Optional;

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

    @PostMapping("/login")
    public String loginUser(@ModelAttribute("userDTO") UserDTO userDTO, Model model, HttpSession session) {
        try {
            UserDTO existingUser = userService.findByEmailAndPassword(userDTO.getEmail(), userDTO.getPassword());
            if (existingUser != null) {
                session.setAttribute("user", existingUser);
                return String.format("redirect:/users/show/%d", existingUser.getId());
            } else {
                model.addAttribute("errorMessage", "Invalid email or password");
                model.addAttribute("userDTO", userDTO);
                return "login_form";
            }
        } catch (Exception e) {
            model.addAttribute("errorMessage", "An error occurred during login");
            model.addAttribute("userDTO", userDTO);
            return "login_form";
        }
    }

    @GetMapping("users/show/{userId}")
    public String showProfile(@PathVariable("userId") long userId, Model model, HttpSession session) {
        UserDTO user = (UserDTO) session.getAttribute("user");

        if (user != null && user.getId() == userId) {
            model.addAttribute("user", user);
            model.addAttribute("userName", user.getUsername());
        } else {
            Optional<UserDTO> userFromDb = userService.findById(userId);
            if (userFromDb.isPresent()) {
                model.addAttribute("user", userFromDb.get());
                model.addAttribute("userName", userFromDb.get().getUsername());
            } else {
                model.addAttribute("error", "User not found.");
                return "profile";
            }
        }


        List<PlaylistDTO> playlists = playlistService.findPlaylistsByUserId(userId);
        model.addAttribute("playlists", playlists);
        String dailyRecommendation = songRecommendationService.getDailyRecommendation();
        model.addAttribute("dailyRecommendation", dailyRecommendation);

        return "profile";
    }


}