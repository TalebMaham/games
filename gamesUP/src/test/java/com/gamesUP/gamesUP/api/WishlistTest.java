package com.gamesUP.gamesUP.api;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.gamesUP.gamesUP.config.JwtUtil;
import com.gamesUP.gamesUP.controller.WishlistController;
import com.gamesUP.gamesUP.dto.WishlistRequest;
import com.gamesUP.gamesUP.model.Game;
import com.gamesUP.gamesUP.model.User;
import com.gamesUP.gamesUP.model.Wishlist;
import com.gamesUP.gamesUP.services.WishlistService;

import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.boot.autoconfigure.security.servlet.SecurityAutoConfiguration;

import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.util.List;

import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(controllers = WishlistController.class, excludeAutoConfiguration = SecurityAutoConfiguration.class)
public class WishlistTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private WishlistService wishlistService;

    @MockBean
    private JwtUtil jwtUtil;

    @Autowired
    private ObjectMapper objectMapper;

    @Test
    void testCreateWishlist() throws Exception {
        WishlistRequest request = new WishlistRequest();
        request.setUserId(1L);
        request.setGameIds(List.of(10L, 11L));

        Wishlist wishlist = new Wishlist();
        wishlist.setId(1L);

        when(wishlistService.createWishlist(1L, List.of(10L, 11L))).thenReturn(wishlist);

        mockMvc.perform(post("/api/wishlists")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request)))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.id").value(1L));
    }

    @Test
    void testGetWishlistsAsAdmin() throws Exception {
        String token = "fake-token";
        String email = "admin@gamesup.com";

        when(jwtUtil.getRoleFromToken(token)).thenReturn("ADMIN");
        when(jwtUtil.getUsernameFromToken(token)).thenReturn(email);

        Wishlist wishlist = new Wishlist();
        wishlist.setId(2L);

        when(wishlistService.getAll()).thenReturn(List.of(wishlist));

        mockMvc.perform(get("/api/wishlists")
                .header("Authorization", "Bearer " + token))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$[0].id").value(2L));
    }

    @Test
    void testGetWishlistsAsUser() throws Exception {
        String token = "fake-token";
        String email = "user@gamesup.com";

        when(jwtUtil.getRoleFromToken(token)).thenReturn("USER");
        when(jwtUtil.getUsernameFromToken(token)).thenReturn(email);

        Wishlist wishlist = new Wishlist();
        wishlist.setId(3L);

        when(wishlistService.getWishlistsByUserEmail(email)).thenReturn(List.of(wishlist));

        mockMvc.perform(get("/api/wishlists")
                .header("Authorization", "Bearer " + token))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$[0].id").value(3L));
    }

    @Test
    void testGetWishlistByUserId() throws Exception {
        Wishlist wishlist = new Wishlist();
        wishlist.setId(4L);

        when(wishlistService.getByUserId(1L)).thenReturn(List.of(wishlist));

        mockMvc.perform(get("/api/wishlists/user/1"))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$[0].id").value(4L));
    }
}
