package com.example.demo.dto;

public class LoginResponse {

    private Long userId;
    private String name;
    private String email;
    private int xp;
    private int level;
    private String message;

    public LoginResponse() {
    }

    public LoginResponse(
            Long userId,
            String name,
            String email,
            int xp,
            int level,
            String message) {

        this.userId = userId;
        this.name = name;
        this.email = email;
        this.xp = xp;
        this.level = level;
        this.message = message;
    }

    public Long getUserId() {
        return userId;
    }

    public String getName() {
        return name;
    }

    public String getEmail() {
        return email;
    }

    public int getXp() {
        return xp;
    }

    public int getLevel() {
        return level;
    }

    public String getMessage() {
        return message;
    }
}