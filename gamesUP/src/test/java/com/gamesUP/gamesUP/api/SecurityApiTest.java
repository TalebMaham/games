package com.gamesUP.gamesUP.api;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.gamesUP.gamesUP.config.JwtUtil;
import com.gamesUP.gamesUP.enums.Role;
import com.gamesUP.gamesUP.model.User;
import com.gamesUP.gamesUP.repositories.UserRepository;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.web.servlet.MockMvc;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
public class SecurityApiTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private JwtUtil jwtUtil;

    @Autowired
    private UserRepository userRepo;

    @Autowired
    private ObjectMapper objectMapper;

    private String adminToken;
    private String clientToken;
    private User clientUser;

    @BeforeAll
    void setup() {
        // Créer un admin
        User admin = new User("admin", "admin@example.com",12,  "pass", Role.ADMIN);
        userRepo.save(admin);
        adminToken = "Bearer " + jwtUtil.generateToken(admin.getEmail(), admin.getRole().name());

        // Créer un client
        clientUser = new User("client", "client@example.com",12,  "pass", Role.CLIENT);
        userRepo.save(clientUser);
        clientToken = "Bearer " + jwtUtil.generateToken(clientUser.getEmail(), clientUser.getRole().name());
    }

    // 1. Uniquement l'admin peut voir tous les users
    @Test
    void adminCanSeeAllUsers() throws Exception {
        mockMvc.perform(get("/api/users").header("Authorization", adminToken))
                .andExpect(status().isOk());
    }

    @Test
    void clientCannotSeeAllUsers() throws Exception {
        mockMvc.perform(get("/api/users").header("Authorization", clientToken))
                .andExpect(status().isForbidden());
    }

    // 2. L'admin peut voir tout (wishlists, purchases)
    @Test
    void adminCanSeeAllPurchases() throws Exception {
        mockMvc.perform(get("/api/purchases").header("Authorization", adminToken))
                .andExpect(status().isOk());
    }

    @Test
    void adminCanSeeAllWishlists() throws Exception {
        mockMvc.perform(get("/api/wishlists").header("Authorization", adminToken))
                .andExpect(status().isOk());
    }

    // 3. Le client peut voir tous les jeux
    @Test
    void clientCanSeeAllGames() throws Exception {
        mockMvc.perform(get("/api/games").header("Authorization", clientToken))
                .andExpect(status().isOk());
    }

    // 4. Le client voit uniquement ses achats / wishlists
    @Test
    void clientSeesOnlyHisPurchases() throws Exception {
        var result = mockMvc.perform(get("/api/purchases").header("Authorization", clientToken))
                .andExpect(status().isOk())
                .andReturn();

        String json = result.getResponse().getContentAsString();
        JsonNode root = objectMapper.readTree(json);

        for (JsonNode purchase : root) {
            long userId = purchase.get("user").get("id").asLong();
            assert userId == clientUser.getId();
        }
    }

    @Test
    void clientSeesOnlyHisWishlists() throws Exception {
        var result = mockMvc.perform(get("/api/wishlists").header("Authorization", clientToken))
                .andExpect(status().isOk())
                .andReturn();

        String json = result.getResponse().getContentAsString();
        JsonNode root = objectMapper.readTree(json);

        for (JsonNode wishlist : root) {
            long userId = wishlist.get("user").get("id").asLong();
            assert userId == clientUser.getId();
        }
    }
}
