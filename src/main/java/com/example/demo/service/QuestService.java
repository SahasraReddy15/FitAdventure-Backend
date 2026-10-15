
package com.example.demo.service;

import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.example.demo.entity.Quest;
import com.example.demo.repository.QuestRepository;

@Service
public class QuestService {

    private final QuestRepository questRepository;

    public QuestService(QuestRepository questRepository) {
        this.questRepository = questRepository;
    }

    public List<Quest> getAllQuests() {
        return questRepository.findAll();
    }

    @Transactional
    public void createDailyQuestTemplates() {

        createQuestIfMissing(
            "Forest Explorer",
            "Walk 5000 steps",
            5000,
            80,
            "STEPS"
        );

        createQuestIfMissing(
            "Warrior Training",
            "Complete 10 squats",
            10,
            40,
            "SQUATS"
        );

        createQuestIfMissing(
            "Recovery Magic",
            "Stretch for 5 minutes",
            5,
            30,
            "STRETCH"
        );

        createQuestIfMissing(
            "Hydration Challenge",
            "Drink 6 glasses of water",
            6,
            30,
            "WATER"
        );
    }

    private void createQuestIfMissing(
            String title,
            String description,
            int targetValue,
            int xpReward,
            String activityType) {

        if (questRepository.existsByTitleAndIsDailyTrue(title)) {
            return;
        }

        Quest quest = new Quest();
        quest.setTitle(title);
        quest.setDescription(description);
        quest.setTargetValue(targetValue);
        quest.setXpReward(xpReward);
        quest.setActivityType(activityType);
        quest.setDaily(true);

        questRepository.save(quest);
    }
}
