
package com.example.demo.controller;

import java.util.List;
import java.util.Map;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.example.demo.entity.Quest;
import com.example.demo.repository.QuestRepository;
import com.example.demo.service.QuestCompletionService;
import com.example.demo.service.GameLevelService;
import com.example.demo.service.QuestService;

@RestController
@RequestMapping("/api/quests")
public class QuestController {

    private final QuestRepository questRepository;
    private final QuestCompletionService questCompletionService;
    private final GameLevelService gameLevelService;
    private final QuestService questService;

    public QuestController(
            QuestRepository questRepository,
            QuestCompletionService questCompletionService,
            GameLevelService gameLevelService,
            QuestService questService) {

        this.questRepository = questRepository;
        this.questCompletionService = questCompletionService;
        this.gameLevelService = gameLevelService;
        this.questService = questService;
    }

    // Get all quests
    @GetMapping
    public ResponseEntity<List<Quest>> getAllQuests() {
        return ResponseEntity.ok(questRepository.findAll());
    }

    // Get daily quest templates
    @GetMapping("/daily")
    public ResponseEntity<List<Quest>> getDailyQuests() {

        List<Quest> dailyQuests = questService.getAllQuests()
                .stream()
                .filter(Quest::isDaily)
                .toList();

        return ResponseEntity.ok(dailyQuests);
    }

    // Complete a quest
    @PostMapping("/{questId}/complete")
    public ResponseEntity<String> completeQuest(
            @PathVariable Long questId,
            @RequestParam Long userId,
            @RequestParam int activityValue) {

        String result = questCompletionService.completeQuest(
                userId, questId, activityValue);

        return ResponseEntity.ok(result);
    }

    // Calculate level and progress toward the next level
    @GetMapping("/level-status")
    public ResponseEntity<Map<String, Object>> getLevelStatus(
            @RequestParam int xp) {

        int safeXp = Math.max(0, xp);
        int level = gameLevelService.calculateLevel(safeXp);

        return ResponseEntity.ok(Map.of(
                "xp", safeXp,
                "level", level,
                "xpForNextLevel",
                    gameLevelService.getXpForNextLevel(safeXp),
                "enchantedForestUnlocked", level >= 2,
                "crystalLakeUnlocked", level >= 3,
                "dragonMountainUnlocked", level >= 5,
                "royalCastleUnlocked", level >= 8
        ));
    }
}