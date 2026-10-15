
package com.example.demo;

import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.example.demo.entity.Quest;
import com.example.demo.repository.QuestRepository;

@Configuration
public class DataInitializer {

    @Bean
    CommandLineRunner loadQuests(QuestRepository questRepository) {
        return args -> {

            if (questRepository.count() == 0) {

                Quest walk = new Quest();
                walk.setTitle("First Steps");
                walk.setDescription("Walk 1000 steps to begin your adventure.");
                walk.setTargetValue(1000);
                walk.setXpReward(50);
                walk.setActivityType("WALKING");
                questRepository.save(walk);

                Quest run = new Quest();
                run.setTitle("Speed Explorer");
                run.setDescription("Complete a 2 km run.");
                run.setTargetValue(2);
                run.setXpReward(100);
                run.setActivityType("RUNNING");
                questRepository.save(run);

                Quest workout = new Quest();
                workout.setTitle("Power Up");
                workout.setDescription("Complete one workout session.");
                workout.setTargetValue(1);
                workout.setXpReward(75);
                workout.setActivityType("WORKOUT");
                questRepository.save(workout);

                System.out.println("Sample quests added successfully!");
            }
        };
    }
}
