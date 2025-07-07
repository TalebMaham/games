package com.gamesUP.gamesUP.api;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.gamesUP.gamesUP.config.JwtUtil;
import com.gamesUP.gamesUP.controller.PurchaseController;
import com.gamesUP.gamesUP.dto.PurchaseLineDto;
import com.gamesUP.gamesUP.dto.PurchaseRequestDto;
import com.gamesUP.gamesUP.model.Game;
import com.gamesUP.gamesUP.model.Purchase;
import com.gamesUP.gamesUP.model.PurchaseLine;
import com.gamesUP.gamesUP.services.PurchaseService;

import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.util.*;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(PurchaseController.class)
public class PurchaseStockControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private PurchaseService purchaseService;

    @MockBean
    private JwtUtil jwtUtil;

    @Autowired
    private ObjectMapper objectMapper;

    @Test
    void testCreatePurchaseDecrementsStock() throws Exception {
        // Créer une requête d'achat avec 1 ligne
        PurchaseRequestDto dto = new PurchaseRequestDto();
        dto.setUserId(1L);

        PurchaseLineDto lineDto = new PurchaseLineDto();
        lineDto.setGameId(100L);
        lineDto.setPrix(49.99);
        dto.setLines(List.of(lineDto));

        Purchase mockPurchase = new Purchase();
        mockPurchase.setId(42L);

        when(purchaseService.createPurchase(any())).thenReturn(mockPurchase);

        mockMvc.perform(post("/api/purchases")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(dto)))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.id").value(42));

        // Capturer le DTO passé au service
        ArgumentCaptor<PurchaseRequestDto> captor = ArgumentCaptor.forClass(PurchaseRequestDto.class);
        verify(purchaseService).createPurchase(captor.capture());

        PurchaseRequestDto sentDto = captor.getValue();
        assertEquals(1L, sentDto.getUserId());
        assertEquals(1, sentDto.getLines().size());
        assertEquals(100L, sentDto.getLines().get(0).getGameId());
    }
}
