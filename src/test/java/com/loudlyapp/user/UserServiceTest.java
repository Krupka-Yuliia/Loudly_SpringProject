package com.loudlyapp.user;

import com.loudlyapp.AbstractMySQLContainerBaseTest;
import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.testcontainers.containers.MySQLContainer;

import java.util.Collection;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.IntStream;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
public class UserServiceTest extends AbstractMySQLContainerBaseTest {

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


    private UserDTO createUserDTO(String username) {
        UserDTO userDTO = new UserDTO();
        userDTO.setUsername(username);
        userDTO.setPassword(DEFAULT_PASSWORD);
        userDTO.setEmail(UUID.randomUUID().toString());
        userDTO.setRole(DEFAULT_ROLE);
        return userDTO;
    }

    @Test
    public void createAndSaveUserTest() {
        UserDTO userDTO = createUserDTO(UUID.randomUUID().toString());
        UserDTO savedUserDTO = userService.save(userDTO);
        assertNotNull(savedUserDTO, "UserDTO was not created");
        assertNotNull(savedUserDTO.getEmail(), "Email was not created");
        assertNotNull(savedUserDTO.getPassword(), "Password was not created");
        assertNotNull(savedUserDTO.getRole(), "Role was not created");
    }

    @Test
    public void getUserByIdTest() {
        UserDTO userDTO = createUserDTO(UUID.randomUUID().toString());
        UserDTO savedUserDTO = userService.save(userDTO);
        Optional<UserDTO> foundByIdDTO = userService.getUserById((long) savedUserDTO.getId());

        assertTrue(foundByIdDTO.isPresent(), "UserDTO not found by ID");
        assertEquals(savedUserDTO, foundByIdDTO.get());
        assertEquals(savedUserDTO.getEmail(), foundByIdDTO.get().getEmail());
        assertEquals(savedUserDTO.getPassword(), foundByIdDTO.get().getPassword());
        assertEquals(savedUserDTO.getRole(), foundByIdDTO.get().getRole());
    }


    @Test
    public void findAllUsersTest() {
        UserDTO userDTO = createUserDTO(UUID.randomUUID().toString());
        userService.save(userDTO);
        Collection<UserDTO> usersDTO = userService.getAllUsers();
        assertNotNull(usersDTO);
        assertEquals(usersDTO.size(), 1);
    }

    @Test
    public void deleteAllUsersTest() {
        Collection<UserDTO> usersDTO = userService.getAllUsers();
        assertEquals(0, usersDTO.size());
        IntStream.range(0, 10).forEach(i -> {
            UserDTO userDTO = new UserDTO();
            userDTO.setUsername("testUse" + i);
            userDTO.setEmail("test@tst.com" + i);
            userDTO.setPassword("password");
            userDTO.setRole("UserDTO");
            userService.save(userDTO);
        });
        Collection<UserDTO> savedUserDTOs = userService.getAllUsers();
        assertEquals(10, savedUserDTOs.size());
        userRepository.deleteAll();
        Collection<UserDTO> deletedUserDTOs = userService.getAllUsers();
        assertEquals(0, deletedUserDTOs.size());

    }

    @Test
    public void deleteUserByIdTest() {
        UserDTO userDTO = createUserDTO(UUID.randomUUID().toString());
        UserDTO savedUser = userService.save(userDTO);
        assertNotNull(savedUser, "User was not created");

        userService.deleteUserById(savedUser.getId());

        Optional<UserDTO> deletedUser = userService.findById(savedUser.getId());
        Assertions.assertFalse(deletedUser.isPresent(), "User was not deleted");
    }


    @Test
    public void updateUserDTOTest() {
        UserDTO userDTO = createUserDTO(UUID.randomUUID().toString());
        UserDTO savedUserDTO = userService.save(userDTO);

        assertNotNull(savedUserDTO, "User was not created");

        UserDTO updatedUserDTO = new UserDTO();
        updatedUserDTO.setUsername(UUID.randomUUID().toString());
        updatedUserDTO.setEmail(savedUserDTO.getEmail());
        updatedUserDTO.setPassword(savedUserDTO.getPassword());
        updatedUserDTO.setRole(savedUserDTO.getRole());

        userService.updateUserDTO((long) savedUserDTO.getId(), updatedUserDTO);

        Optional<UserDTO> getUpdatedUser = userService.findById(savedUserDTO.getId());
        assertTrue(getUpdatedUser.isPresent(), "Updated user not found");

        assertEquals(updatedUserDTO.getUsername(), getUpdatedUser.get().getUsername(), "Username should be updated");
        assertEquals(savedUserDTO.getEmail(), getUpdatedUser.get().getEmail(), "Email should remain the same");
        assertEquals(savedUserDTO.getPassword(), getUpdatedUser.get().getPassword(), "Password should remain the same");
        assertEquals(savedUserDTO.getRole(), getUpdatedUser.get().getRole(), "Role should remain the same");
        assertEquals(savedUserDTO.getId(), getUpdatedUser.get().getId(), "ID should remain the same");
    }

}
