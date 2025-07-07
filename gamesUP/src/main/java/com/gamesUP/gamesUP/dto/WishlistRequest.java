package com.gamesUP.gamesUP.dto;

import java.util.List;

public class WishlistRequest {
    private Long userId;
    private List<Long> gameIds;
    public WishlistRequest() {} 

    public Long getUserId() { return userId; }
    public List<Long> getGameIds() { return gameIds; }

    public void setUserId(Long userId) { this.userId = userId; }
    public void setGameIds(List<Long> gameIds) { this.gameIds = gameIds; }
}
