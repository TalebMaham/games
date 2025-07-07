package com.gamesUP.gamesUP.dto;

import java.util.List;

public class PurchaseRequestDto {
    private Long userId;
    private List<PurchaseLineDto> lines;

    // Getters/setters

    public Long getUserId() { return userId; }
    public void setUserId(Long userId) { this.userId = userId; }

    public List<PurchaseLineDto> getLines() { return lines; }
    public void setLines(List<PurchaseLineDto> lines) { this.lines = lines; }
}

