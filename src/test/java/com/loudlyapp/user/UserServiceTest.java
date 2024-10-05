package com.loudlyapp.user;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

@SpringBootTest
public class UserServiceTest {

    @Autowired
    private UserRepository userRepository;

    @Test
    public void createUserTest() {
        User user = new User();
        user.setEmail("test@test.com");
        user.setPassword("password");
        user.setRole("user");
        User savedUser = userRepository.save(user);
        assertNotNull(savedUser, "User was not created");
        assertNotNull(savedUser.getEmail(), "Email was not created");
        assertNotNull(savedUser.getPassword(), "Password was not created");
        assertNotNull(savedUser.getRole(), "Role was not created");
        assertNotNull(savedUser.getId(), "Id was not created");
    }

    @Test
    public void getUserByIdTest() {
        User user = new User();
        user.setEmail("test@test.com");
        user.setPassword("password");
        user.setRole("user");
        User savedUser = userRepository.save(user);
        Optional<User> foundById = userRepository.findById(Long.valueOf(savedUser.getId()));
        assertEquals(savedUser, foundById.get());
        assertEquals(savedUser.getEmail(), foundById.get().getEmail());
        assertEquals(savedUser.getPassword(), foundById.get().getPassword());
        assertEquals(savedUser.getRole(), foundById.get().getRole());

    }

//    @Test
//    public void deleteAllUsersTest() {
//        Collection<User> users = userRepository.findAll();
//        assertEquals(0, users.size());
//        IntStream.range(0, 10).forEach(i -> {
//            User user = new User();
//            user.setEmail("test@test.com");
//            user.setPassword("password");
//            user.setRole("user");
//            userRepository.save(user);
//        });
//        Collection<User> savedUsers = userRepository.findAll();
//        assertEquals(10, savedUsers.size());
//        userRepository.deleteAll();
//        Collection<User> deletedUsers = userRepository.findAll();
//        assertEquals(0, deletedUsers.size());
//
//    }

}
