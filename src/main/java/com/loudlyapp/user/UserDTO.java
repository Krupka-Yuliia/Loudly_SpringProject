package com.loudlyapp.user;

public record UserDTO(
        int id,
        String username,
        String email,
        String role
) {
}
