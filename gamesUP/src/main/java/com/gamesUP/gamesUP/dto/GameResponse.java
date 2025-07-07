package com.gamesUP.gamesUP.dto;

public class GameResponse {
    private Long id;
    private String title;
    private String authorName;
    private String publisherName;

    public GameResponse() {}

    public GameResponse(Long id, String title, String authorName, String publisherName) {
        this.id = id;
        this.title = title;
        this.authorName = authorName;
        this.publisherName = publisherName;
    }

    public Long getId() { return id; }
    public String getTitle() { return title; }
    public String getAuthorName() { return authorName; }
    public String getPublisherName() { return publisherName; }

    public void setId(Long id) { this.id = id; }
    public void setTitle(String title) { this.title = title; }
    public void setAuthorName(String authorName) { this.authorName = authorName; }
    public void setPublisherName(String publisherName) { this.publisherName = publisherName; }
}
