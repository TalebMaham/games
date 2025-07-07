package com.gamesUP.gamesUP.api;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.gamesUP.gamesUP.config.JwtUtil;
import com.gamesUP.gamesUP.controller.UserController;
import com.gamesUP.gamesUP.dto.LoginRequest;
import com.gamesUP.gamesUP.dto.LoginResponse;
import com.gamesUP.gamesUP.dto.UserRegistrationRequest;
import com.gamesUP.gamesUP.enums.Role;
import com.gamesUP.gamesUP.model.User;
import com.gamesUP.gamesUP.services.UserService;

import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.util.Optional;

import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(UserController.class)
public class UserControllerTest {

    @Autowired private MockMvc mockMvc;
    @Autowired private ObjectMapper objectMapper;

    @MockBean private UserService userService;
    @MockBean private JwtUtil jwtUtil;

    @Test
    void testRegisterUser() throws Exception {
        UserRegistrationRequest req = new UserRegistrationRequest();
        req.setName("Alice");
        req.setEmail("alice@example.com");
        req.setPassword("secret");
        req.setAge(25);
        req.setRole(Role.CLIENT);

        User user = new User("Alice", "alice@example.com", 25, "hashed", Role.CLIENT);
        when(userService.createUser(Mockito.any(), Mockito.any(), Mockito.anyInt(), Mockito.any(), Mockito.any()))
            .thenReturn(user);

        mockMvc.perform(post("/api/users/register")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(req)))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.name").value("Alice"))
            .andExpect(jsonPath("$.email").value("alice@example.com"));
    }

    @Test
    void testLoginSuccess() throws Exception {
        LoginRequest req = new LoginRequest("bob@example.com", "pass123");
        User user = new User("Bob", "bob@example.com", 30, "hashed", Role.ADMIN);

        when(userService.authenticate("bob@example.com", "pass123")).thenReturn(user);
        when(jwtUtil.generateToken("bob@example.com", "ADMIN")).thenReturn("mocked-token");

        mockMvc.perform(post("/api/users/login")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(req)))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.token").value("mocked-token"));
    }

    @Test
    void testLoginFailure() throws Exception {
        LoginRequest req = new LoginRequest("unknown@example.com", "wrong");

        when(userService.authenticate(Mockito.anyString(), Mockito.anyString())).thenReturn(null);

        mockMvc.perform(post("/api/users/login")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(req)))
            .andExpect(status().isUnauthorized());
    }

    @Test
    void testGetCurrentUser() throws Exception {
        String token = "Bearer abc.def.ghi";
        String email = "carol@example.com";
        User user = new User("Carol", email, 22, "pass", Role.CLIENT);

        when(jwtUtil.extractUsername("abc.def.ghi")).thenReturn(email);
        when(userService.getUserByEmail(email)).thenReturn(user);

        mockMvc.perform(get("/api/users/me")
                .header("Authorization", token))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.email").value(email));
    }

    @Test
    void testGetUserById() throws Exception {
        User user = new User("Dan", "dan@example.com", 40, "pass", Role.ADMIN);
        when(userService.getUserById(99L)).thenReturn(Optional.of(user));

        mockMvc.perform(get("/api/users/99"))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.name").value("Dan"));
    }
}
