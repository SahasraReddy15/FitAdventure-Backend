
package com.example.demo;

import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import com.example.demo.service.QuestService;

@Component
public class DailyQuestInitializer implements CommandLineRunner {

    private final QuestService questService;

    public DailyQuestInitializer(QuestService questService) {
        this.questService = questService;
    }

    @Override
    public void run(String... args) {
        questService.createDailyQuestTemplates();
        System.out.println("Daily quest templates initialized!");
    }
}