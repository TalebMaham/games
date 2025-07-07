package com.gamesUP.gamesUP.api;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.gamesUP.gamesUP.config.JwtUtil;
import com.gamesUP.gamesUP.controller.PurchaseController;
import com.gamesUP.gamesUP.dto.PurchaseRequestDto;
import com.gamesUP.gamesUP.model.Game;
import com.gamesUP.gamesUP.model.Purchase;
import com.gamesUP.gamesUP.model.PurchaseLine;
import com.gamesUP.gamesUP.model.User;
import com.gamesUP.gamesUP.services.PurchaseService;

import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.boot.autoconfigure.security.servlet.SecurityAutoConfiguration;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.util.*;

import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(controllers = PurchaseController.class, excludeAutoConfiguration = SecurityAutoConfiguration.class)
public class PurchaseTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private PurchaseService purchaseService;

    @MockBean
    private JwtUtil jwtUtil;

    @Autowired
    private ObjectMapper objectMapper;

    @Test
    void testCreatePurchase() throws Exception {
        PurchaseRequestDto dto = new PurchaseRequestDto();
        dto.setUserId(1L);
        dto.setLines(new ArrayList<>());

        Purchase purchase = new Purchase();
        purchase.setId(1L);

        when(purchaseService.createPurchase(Mockito.any(PurchaseRequestDto.class)))
                .thenReturn(purchase);

        mockMvc.perform(post("/api/purchases")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(dto)))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.id").value(1L));
    }

    @Test
    void testGetPurchasesAsAdmin() throws Exception {
        String token = "fake-token";
        String email = "admin@gamesup.com";
        when(jwtUtil.getUsernameFromToken(token)).thenReturn(email);
        when(jwtUtil.getRoleFromToken(token)).thenReturn("ADMIN");

        Purchase purchase = new Purchase();
        purchase.setId(1L);

        when(purchaseService.getAll()).thenReturn(List.of(purchase));

        mockMvc.perform(get("/api/purchases")
                .header("Authorization", "Bearer " + token))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$[0].id").value(1L));
    }

    @Test
    void testGetPurchasesAsUser() throws Exception {
        String token = "fake-token";
        String email = "user@gamesup.com";
        when(jwtUtil.getUsernameFromToken(token)).thenReturn(email);
        when(jwtUtil.getRoleFromToken(token)).thenReturn("USER");

        Purchase purchase = new Purchase();
        purchase.setId(2L);

        when(purchaseService.getPurchasesByUserEmail(email)).thenReturn(List.of(purchase));

        mockMvc.perform(get("/api/purchases")
                .header("Authorization", "Bearer " + token))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$[0].id").value(2L));
    }

    @Test
    void testGetPurchasesByUserId() throws Exception {
        Purchase purchase = new Purchase();
        purchase.setId(3L);

        when(purchaseService.getByUser(42L)).thenReturn(List.of(purchase));

        mockMvc.perform(get("/api/purchases/user/42"))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$[0].id").value(3L));
    }
}
