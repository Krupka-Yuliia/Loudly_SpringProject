package com.loudlyapp.user;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.Collection;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.IntStream;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
public class UserServiceTest {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private UserService userService;

    private static final String DEFAULT_PASSWORD = "password";
    private static final String DEFAULT_ROLE = "user";
    @AfterEach
    void tearDown() {
        this.userService.deleteAll();
    }

    private User createUser(String username) {
        User user = new User();
        user.setUsername(username);
        user.setPassword(DEFAULT_PASSWORD);
        user.setEmail(UUID.randomUUID().toString());
        user.setRole(DEFAULT_ROLE);
        return user;
    }

    @Test
    public void createAndSaveUserTest() {
        User user = createUser(UUID.randomUUID().toString());
        User savedUser = userService.save(user);

        assertNotNull(savedUser, "User was not created");
        assertNotNull(savedUser.getEmail(), "Email was not created");
        assertNotNull(savedUser.getPassword(), "Password was not created");
        assertNotNull(savedUser.getRole(), "Role was not created");
        assertNotNull(savedUser.getId(), "Id was not created");
    }

    @Test
    public void getUserByIdTest() {
        User user = createUser(UUID.randomUUID().toString());
        User savedUser = userService.save(user);
        Optional<User> foundById = userService.findById(savedUser.getId());
        assertEquals(savedUser, foundById.get());
        assertEquals(savedUser.getEmail(), foundById.get().getEmail());
        assertEquals(savedUser.getPassword(), foundById.get().getPassword());
        assertEquals(savedUser.getRole(), foundById.get().getRole());

    }

    @Test
    public void findAllUsersTest() {
        User user = createUser(UUID.randomUUID().toString());
        userService.save(user);
        Collection<User> users = userService.getAllUsers();
        assertNotNull(users);
        assertEquals(users.size(), 1);
    }

    @Test
    public void deleteAllUsersTest() {
        Collection<User> users = userService.getAllUsers();
        assertEquals(0, users.size());
        IntStream.range(0, 10).forEach(i -> {
            User user = new User();
            user.setUsername("testUse" + i);
            user.setEmail("test@tst.com" + i);
            user.setPassword("password");
            user.setRole("user");
            userService.save(user);
        });
        Collection<User> savedUsers = userService.getAllUsers();
        assertEquals(10, savedUsers.size());
        userRepository.deleteAll();
        Collection<User> deletedUsers = userService.getAllUsers();
        assertEquals(0, deletedUsers.size());

    }

    @Test
    public void deleteUserByIdTest() {
        User user = createUser(UUID.randomUUID().toString());
        User savedUser = userService.save(user);
        assertNotNull(savedUser, "User was not created");

        userService.deleteUserById(savedUser.getId());

        Optional<User> deletedUser = userService.findById(Long.valueOf(savedUser.getId()));

        Assertions.assertFalse(deletedUser.isPresent());

    }

    @Test
    public void updateUserTest() {
        User user = createUser(UUID.randomUUID().toString());
        User savedUser = userService.save(user);

        assertNotNull(savedUser, "User was not created");

        User updatedUser = new User();
        updatedUser.setUsername(UUID.randomUUID().toString());
        updatedUser.setEmail(user.getEmail());
        updatedUser.setPassword(user.getPassword());
        updatedUser.setRole(user.getRole());

        userService.updateUser((long) savedUser.getId(), updatedUser);

        Optional<User> getUpdatedUser = userService.findById(savedUser.getId());
        assertNotNull(getUpdatedUser, "Updated user not found");
        assertEquals(getUpdatedUser.get().getEmail(), updatedUser.getEmail());
        assertEquals(getUpdatedUser.get().getPassword(), updatedUser.getPassword());
        assertEquals(getUpdatedUser.get().getUsername(), updatedUser.getUsername());
        assertEquals(getUpdatedUser.get().getRole(), updatedUser.getRole());
        assertEquals(getUpdatedUser.get().getId(), savedUser.getId(), "ID should remain the same");
    }


    @Test
    public void findUserByEmailTest() {
        User user = createUser(UUID.randomUUID().toString());
        User savedUser = userService.save(user);
        assertNotNull(savedUser, "User was not created");

       Optional<User> foundByEmail = userService.findByEmail(savedUser.getEmail());
       assertEquals(savedUser, foundByEmail.get());
    }

    @Test
    public void findUserByUsernameTest() {
        User user = createUser(UUID.randomUUID().toString());
        User savedUser = userService.save(user);
        assertNotNull(savedUser, "User was not created");

        Optional<User> foundByUsername = userService.findByUsername(savedUser.getUsername());
        assertEquals(savedUser, foundByUsername.get());
    }

    @Test
    public void findUserByRoleTest() {
        User user = createUser(UUID.randomUUID().toString());
        user.setRole("admin");
        User savedUser = userService.save(user);
        assertNotNull(savedUser, "User was not created");

        List<User> foundByRole = userService.findByRole(savedUser.getRole());
        assertFalse(foundByRole.isEmpty());
        assertEquals(savedUser, foundByRole.get(0));
    }

}
