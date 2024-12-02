package com.loudlyapp.user;

import jakarta.persistence.EntityNotFoundException;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@AllArgsConstructor
@Service
public class UserService {

    private final UserRepository userRepository;

    public Optional<UserDTO> getUserById(Long userId) {
        return userRepository.findById(userId)
                .map(this::convertToDTO);
    }


    public UserDTO save(UserDTO userDTO) {
        if (userRepository.existsByUsername(userDTO.getUsername())) {
            throw new IllegalArgumentException("Username already exists: %s".formatted(userDTO.getUsername()));
        }
        if (userRepository.existsByEmail(userDTO.getEmail())) {
            throw new IllegalArgumentException("Email already exists: %s".formatted(userDTO.getEmail()));
        }

        User user = convertToEntity(userDTO);

        User savedUser = userRepository.save(user);

        return convertToDTO(savedUser);
    }


    private UserDTO convertToDTO(User user) {
        if (user == null) {
            return null;
        }
        UserDTO userDTO = new UserDTO();
        userDTO.setId(user.getId());
        userDTO.setUsername(user.getUsername());
        userDTO.setEmail(user.getEmail());
        userDTO.setPassword(user.getPassword());
        userDTO.setRole(user.getRole());
        return userDTO;
    }

    private User convertToEntity(UserDTO userDTO) {
        if (userDTO == null) {
            return null;
        }
        User user = new User();

        user.setId(userDTO.getId());
        user.setUsername(userDTO.getUsername());
        user.setEmail(userDTO.getEmail());
        user.setPassword(userDTO.getPassword());
        user.setRole(userDTO.getRole());
        return user;
    }

    public List<UserDTO> getAllUsers() {
        List<User> users = userRepository.findAll();
        return users.stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }

    public Optional<UserDTO> findById(long id) {
        return userRepository.findById(id)
                .map(this::convertToDTO);
    }

    public void deleteAll() {
        userRepository.deleteAll();
    }


    public void deleteUserById(long id) {
        userRepository.deleteById(id);
    }

    public UserDTO updateUserDTO(Long userId, UserDTO userDTO) {
        return userRepository.findById(userId)
                .map(existingUser -> {
                    if (userDTO.getUsername() != null) {
                        existingUser.setUsername(userDTO.getUsername());
                    }
                    if (userDTO.getEmail() != null) {
                        existingUser.setEmail(userDTO.getEmail());
                    }
                    if (userDTO.getPassword() != null) {
                        existingUser.setPassword(userDTO.getPassword());
                    }
                    if (userDTO.getRole() != null) {
                        existingUser.setRole(userDTO.getRole());
                    }
                    return convertToDTO(userRepository.save(existingUser));
                })
                .orElseThrow(() -> new EntityNotFoundException("User with id " + userId + " not found"));
    }

    public UserDTO findByEmail(String email) {
        User user = userRepository.findByEmail(email);
        return convertToDTO(user);
    }


    public Optional<UserDTO> findByUsername(String username) {
        Optional<User> user = userRepository.findByUsername(username);
        return user.map(this::convertToDTO);
    }

}
