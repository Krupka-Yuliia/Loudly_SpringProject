package com.loudlyapp.controllers;

import com.loudlyapp.entities.User;
import com.loudlyapp.services.UserService;
import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/users")
@AllArgsConstructor
public class UserController {

    private final UserService userService;

    @GetMapping()
    public List<User> getUsers() {
        return userService.getAllUsers();
    }

}
