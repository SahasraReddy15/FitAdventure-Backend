
package com.example.demo.controller;

import java.util.List;
import java.util.Map;
import java.util.Objects;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;

import com.example.demo.entity.Quest;
import com.example.demo.entity.QuestCompletion;
import com.example.demo.entity.User;
import com.example.demo.repository.QuestCompletionRepository;
import com.example.demo.repository.QuestRepository;
import com.example.demo.repository.UserRepository;

@RestController
@RequestMapping("/api/users")
public class UserProgressController {

    private final UserRepository userRepository;
    private final QuestCompletionRepository completionRepository;
    private final QuestRepository questRepository;

    public UserProgressController(
            UserRepository userRepository,
            QuestCompletionRepository completionRepository,
            QuestRepository questRepository) {

        this.userRepository = userRepository;
        this.completionRepository = completionRepository;
        this.questRepository = questRepository;
    }

    @GetMapping("/{userId}/progress")
    public Map<String, Object> getProgress(@PathVariable Long userId) {

        User user = userRepository.findById(userId)
                .orElseThrow(() -> new ResponseStatusException(
                        HttpStatus.NOT_FOUND, "User not found"));

        List<QuestCompletion> completions =
                completionRepository.findAll()
                        .stream()
                        .filter(c -> Objects.equals(c.getUserId(), userId))
                        .toList();

        List<Long> completedQuestIds = completions.stream()
                .map(QuestCompletion::getQuestId)
                .filter(Objects::nonNull)
                .distinct()
                .toList();

        List<Map<String, Object>> questHistory = completions.stream()
                .map(completion -> {
                    Quest quest = questRepository
                            .findById(completion.getQuestId())
                            .orElse(null);

                    return Map.<String, Object>of(
                            "questId", completion.getQuestId(),
                            "questTitle", quest != null
                                    ? quest.getTitle() : "Unknown Quest",
                            "activityValue", completion.getActivityValue(),
                            "completedAt",
                                    completion.getCompletedAt().toString(),
                            "xpEarned", quest != null
                                    ? quest.getXpReward() : 0
                    );
                })
                .toList();

        return Map.ofEntries(
                Map.entry("userId", user.getId()),
                Map.entry("name", user.getName()),
                Map.entry("email", user.getEmail()),
                Map.entry("xp", user.getXp()),
                Map.entry("level", user.getLevel()),
                Map.entry("coins", user.getCoins()),
                Map.entry("currentStreak", user.getCurrentStreak()),
                Map.entry("longestStreak", user.getLongestStreak()),
                Map.entry("lastStreakDate",
                        user.getLastStreakDate() != null
                                ? user.getLastStreakDate().toString() : ""),
                Map.entry("completedQuests", completedQuestIds.size()),
                Map.entry("completedQuestIds", completedQuestIds),
                Map.entry("questHistory", questHistory)
        );
    }
}
