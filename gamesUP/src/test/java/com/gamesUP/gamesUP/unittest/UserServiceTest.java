package com.gamesUP.gamesUP.unittest;

import com.gamesUP.gamesUP.enums.Role;
import com.gamesUP.gamesUP.model.User;
import com.gamesUP.gamesUP.repositories.UserRepository;
import com.gamesUP.gamesUP.services.UserService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

public class UserServiceTest {

    private UserRepository userRepo;
    private PasswordEncoder passwordEncoder;
    private UserService userService;

    @BeforeEach
    void setup() {
        userRepo = mock(UserRepository.class);
        passwordEncoder = mock(PasswordEncoder.class);
        userService = new UserService(userRepo, passwordEncoder);
    }

    @Test
    void testCreateUserSuccess() {
        String email = "test@example.com";
        String rawPassword = "pass";
        String encodedPassword = "encodedPass";

        when(userRepo.existsByEmail(email)).thenReturn(false);
        when(passwordEncoder.encode(rawPassword)).thenReturn(encodedPassword);
        when(userRepo.save(any(User.class))).thenAnswer(inv -> inv.getArgument(0));

        User user = userService.createUser("Alice", email, 25, rawPassword, Role.CLIENT);

        assertEquals(email, user.getEmail());
        assertEquals(encodedPassword, user.getPassword());
        assertEquals(Role.CLIENT, user.getRole());
        verify(userRepo).save(any(User.class));
    }

    @Test
    void testCreateUserWithExistingEmailThrows() {
        when(userRepo.existsByEmail("existing@example.com")).thenReturn(true);
        assertThrows(IllegalArgumentException.class, () ->
            userService.createUser("Bob", "existing@example.com", 30, "pwd", Role.CLIENT));
    }

    @Test
    void testAuthenticateSuccess() {
        String email = "u@example.com";
        String raw = "pwd";
        String hashed = "hashedPwd";
        User user = new User("U", email, 18, hashed, Role.CLIENT);

        when(userRepo.findByEmail(email)).thenReturn(Optional.of(user));
        when(passwordEncoder.matches(raw, hashed)).thenReturn(true);

        User result = userService.authenticate(email, raw);

        assertNotNull(result);
        assertEquals(email, result.getEmail());
    }

    @Test
    void testAuthenticateFailure() {
        when(userRepo.findByEmail("fail@example.com")).thenReturn(Optional.empty());
        assertNull(userService.authenticate("fail@example.com", "pwd"));
    }

    @Test
    void testUpdateUserRoleSuccess() {
        User user = new User("U", "x@x.com", 20, "pass", Role.CLIENT);
        user.setId(42L);

        when(userRepo.findById(42L)).thenReturn(Optional.of(user));
        when(userRepo.save(any(User.class))).thenAnswer(inv -> inv.getArgument(0));

        User updated = userService.updateUserRole(42L, Role.ADMIN);

        assertEquals(Role.ADMIN, updated.getRole());
    }

    @Test
    void testUpdateUserRoleThrowsIfNotFound() {
        when(userRepo.findById(99L)).thenReturn(Optional.empty());
        assertThrows(RuntimeException.class, () -> userService.updateUserRole(99L, Role.ADMIN));
    }

    @Test
    void testGetAllUsers() {
        when(userRepo.findAll()).thenReturn(List.of(new User(), new User()));
        assertEquals(2, userService.getAllUsers().size());
    }

    @Test
    void testGetUserByEmailSuccess() {
        String email = "hello@example.com";
        when(userRepo.findByEmail(email)).thenReturn(Optional.of(new User()));
        assertNotNull(userService.getUserByEmail(email));
    }

    @Test
    void testGetUserByEmailNotFound() {
        when(userRepo.findByEmail("x@x.com")).thenReturn(Optional.empty());
        assertThrows(RuntimeException.class, () -> userService.getUserByEmail("x@x.com"));
    }

    @Test
    void testDeleteUser() {
        userService.deleteUser(99L);
        verify(userRepo).deleteById(99L);
    }
}

