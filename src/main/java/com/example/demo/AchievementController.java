
package com.example.demo;

import java.util.List;

import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/achievements")
@CrossOrigin(origins = "*")
public class AchievementController {

    private final AchievementService achievementService;

    public AchievementController(
            AchievementService achievementService) {
        this.achievementService = achievementService;
    }

    // Get all achievement definitions
    @GetMapping
    public List<Achievement> getAllAchievements() {
        return achievementService.getAllAchievements();
    }

    // Get achievements unlocked by a specific user
    @GetMapping("/user/{userId}")
    public List<UserAchievement> getUserAchievements(
            @PathVariable Long userId) {

        return achievementService
                .updateAndGetUserAchievements(userId);
    }
}
