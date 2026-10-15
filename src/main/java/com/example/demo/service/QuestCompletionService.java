package com.example.demo.service;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.Map;

import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

import com.example.demo.AchievementService;
import com.example.demo.entity.Quest;
import com.example.demo.entity.QuestCompletion;
import com.example.demo.entity.User;
import com.example.demo.repository.QuestCompletionRepository;
import com.example.demo.repository.QuestRepository;
import com.example.demo.repository.UserRepository;

@Service
public class QuestCompletionService {

    private final UserRepository userRepository;
    private final QuestRepository questRepository;
    private final QuestCompletionRepository completionRepository;
    private final AchievementService achievementService;

    private static final Map<Long, Integer> QUEST_COIN_REWARDS = Map.of(
            1L, 20,
            2L, 15,
            3L, 25
    );

    private static final int XP_PER_LEVEL = 100;

    public QuestCompletionService(
            UserRepository userRepository,
            QuestRepository questRepository,
            QuestCompletionRepository completionRepository,
            AchievementService achievementService) {

        this.userRepository = userRepository;
        this.questRepository = questRepository;
        this.completionRepository = completionRepository;
        this.achievementService = achievementService;
    }

    private int getCoinReward(Quest quest) {
        if (quest.isDaily()) {
            return Math.max(10, quest.getXpReward() / 2);
        }

        return QUEST_COIN_REWARDS.getOrDefault(quest.getId(), 0);
    }

    private int calculateLevel(int totalXp) {
        return Math.max(1, Math.max(0, totalXp) / XP_PER_LEVEL + 1);
    }

    private void updateStreak(User user) {
        LocalDate today = LocalDate.now();
        LocalDate lastDate = user.getLastStreakDate();

        // A quest has already counted for today's streak.
        if (today.equals(lastDate)) {
            return;
        }

        if (lastDate == null) {
            // First-ever streak day.
            user.setCurrentStreak(1);
        } else if (ChronoUnit.DAYS.between(lastDate, today) == 1) {
            // Quest completed on consecutive calendar days.
            user.setCurrentStreak(user.getCurrentStreak() + 1);
        } else {
            // One or more calendar days were missed.
            user.setCurrentStreak(1);
        }

        user.setLastStreakDate(today);

        if (user.getCurrentStreak() > user.getLongestStreak()) {
            user.setLongestStreak(user.getCurrentStreak());
        }
    }

    @Transactional
    public String completeQuest(
            Long userId,
            Long questId,
            int activityValue) {

        User user = userRepository.findById(userId)
                .orElseThrow(() -> new ResponseStatusException(
                        HttpStatus.NOT_FOUND,
                        "User not found"
                ));

        Quest quest = questRepository.findById(questId)
                .orElseThrow(() -> new ResponseStatusException(
                        HttpStatus.NOT_FOUND,
                        "Quest not found"
                ));

        if (activityValue <= 0) {
            throw new ResponseStatusException(
                    HttpStatus.BAD_REQUEST,
                    "Activity value must be greater than zero"
            );
        }

        if (activityValue < quest.getTargetValue()) {
            throw new ResponseStatusException(
                    HttpStatus.BAD_REQUEST,
                    "Target not reached. Required: "
                            + quest.getTargetValue()
                            + ", submitted: " + activityValue
            );
        }

        if (completionRepository.existsByUserIdAndQuestId(
                userId, questId)) {

            throw new ResponseStatusException(
                    HttpStatus.CONFLICT,
                    "Quest already completed"
            );
        }

        int oldLevel = calculateLevel(user.getXp());

        QuestCompletion completion = new QuestCompletion();
        completion.setUserId(userId);
        completion.setQuestId(questId);
        completion.setCompletedAt(LocalDateTime.now());
        completion.setActivityValue(activityValue);

        completionRepository.save(completion);

        int updatedXp = user.getXp() + quest.getXpReward();
        int newLevel = calculateLevel(updatedXp);

        int coinsEarned = getCoinReward(quest);
        int updatedCoins = user.getCoins() + coinsEarned;

        user.setXp(updatedXp);
        user.setLevel(newLevel);
        user.setCoins(updatedCoins);

        // Update the streak after a valid quest completion.
        updateStreak(user);

        userRepository.save(user);

        // Monster damage is handled only by the frontend attack request.
        // This prevents the quest completion service from attacking twice.

        // Preserve existing achievement functionality.
        achievementService.updateAndGetUserAchievements(userId);

        String levelMessage = "";

        if (newLevel > oldLevel) {
            levelMessage = " LEVEL UP! You reached Level "
                    + newLevel + "!";
        }

        return "Quest completed! You earned "
                + quest.getXpReward()
                + " XP and "
                + coinsEarned
                + " coins. Total XP: "
                + updatedXp
                + ". Level: "
                + newLevel
                + ". Total coins: "
                + updatedCoins
                + ". Current streak: "
                + user.getCurrentStreak()
                + " day(s)."
                + levelMessage;
    }
}