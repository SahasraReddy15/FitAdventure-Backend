
package com.example.demo;

import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.example.demo.repository.QuestCompletionRepository;

import jakarta.annotation.PostConstruct;

@Service
public class AchievementService {

    private final AchievementRepository achievementRepository;
    private final UserAchievementRepository userAchievementRepository;
    private final QuestCompletionRepository questCompletionRepository;

    public AchievementService(
            AchievementRepository achievementRepository,
            UserAchievementRepository userAchievementRepository,
            QuestCompletionRepository questCompletionRepository) {
        this.achievementRepository = achievementRepository;
        this.userAchievementRepository = userAchievementRepository;
        this.questCompletionRepository = questCompletionRepository;
    }

    @PostConstruct
    public void initializeAchievements() {
        addAchievement("FIRST_QUEST", "First Steps",
                "Complete your first quest.", "🏅", 1);

        addAchievement("QUEST_EXPLORER", "Quest Explorer",
                "Complete 5 quests.", "🧭", 5);

        addAchievement("FITNESS_HERO", "Fitness Hero",
                "Complete 10 quests.", "⚔️", 10);

        addAchievement("LEGENDARY_WARRIOR", "Legendary Warrior",
                "Complete 25 quests.", "👑", 25);
    }

    private void addAchievement(
            String code, String name, String description,
            String icon, int requiredQuests) {

        if (achievementRepository.findByCode(code).isEmpty()) {
            achievementRepository.save(new Achievement(
                    code, name, description, icon, requiredQuests));
        }
    }

    @Transactional
    public List<UserAchievement> updateAndGetUserAchievements(Long userId) {
        long completedQuests =
                questCompletionRepository.countByUserId(userId);

        List<Achievement> achievements =
                achievementRepository.findAll();

        for (Achievement achievement : achievements) {
            if (completedQuests >= achievement.getRequiredQuests()) {
                boolean alreadyUnlocked =
                        userAchievementRepository
                                .findByUserIdAndAchievementId(
                                        userId, achievement.getId())
                                .isPresent();

                if (!alreadyUnlocked) {
                    userAchievementRepository.save(
                            new UserAchievement(
                                    userId, achievement.getId()));
                }
            }
        }

        return userAchievementRepository.findByUserId(userId);
    }

    public List<Achievement> getAllAchievements() {
        return achievementRepository.findAll();
    }
}
