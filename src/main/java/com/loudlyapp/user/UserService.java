package com.loudlyapp.user;

import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@AllArgsConstructor

public class UserService {

    private final UserRepository userRepository;

    public List<User> getAllUsers() {
        return userRepository.findAll();
    }
    public Optional<User> findById(long id) {
        return userRepository.findById(id);
    }

    public void deleteAll(){
        userRepository.deleteAll();
    }

    public User save(User user) {
        return userRepository.save(user);
    }

}
