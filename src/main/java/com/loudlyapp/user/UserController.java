package com.loudlyapp.user;

import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/users")
@AllArgsConstructor
public class UserController {

    private final UserService userService;

    @GetMapping()
    public List<User> getUsers() {
        return userService.getAllUsers();
    }

    @GetMapping("/{userId}")
    public Optional<User> getUser(@PathVariable Long userId) {
        return userService.findById(userId);
    }

    @DeleteMapping()
    public void deleteAllUsers() {
        userService.deleteAll();
    }

    @PostMapping
    public ResponseEntity<User> createUser(@RequestBody User user) {
        try {
            User createdUser = userService.save(user);
            return ResponseEntity.status(HttpStatus.CREATED).body(createdUser);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(null); // Повертаємо 400 Bad Request, якщо username або email вже існує
        }
    }

    @DeleteMapping("/{userId}")
    public void deleteUserById(@PathVariable Long userId) {
        userService.deleteUserById(userId);
    }


    @PutMapping("/{userId}")
    public User updateUser(@PathVariable Long userId, @RequestBody User user) {
        return userService.updateUser(userId, user);
    }

    @GetMapping("/searchByRole")
    public List<User> getUserByRole(@RequestParam String userRole) {
        List<User> foundByRole = userService.findByRole(userRole);
        return foundByRole;
    }

    @GetMapping("/searchByUsername")
    public Optional<User> getUserByUsername(@RequestParam String username) {
        return userService.findByUsername(username);
    }

    @GetMapping("/searchByEmail")
    public Optional<User> getUserByEmail(@RequestParam String email) {
        return userService.findByEmail(email);
    }

}
