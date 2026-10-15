
package com.example.demo.dto;

public class UserResponse {

    private Long id;
    private String name;
    private String email;
    private int xp;
    private int level;

    public UserResponse(Long id, String name, String email,
                        int xp, int level) {
        this.id = id;
        this.name = name;
        this.email = email;
        this.xp = xp;
        this.level = level;
    }

    public Long getId() {
        return id;
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
}
