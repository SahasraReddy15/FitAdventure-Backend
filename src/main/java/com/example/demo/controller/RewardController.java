
package com.example.demo.controller;

import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.example.demo.entity.PlayerReward;
import com.example.demo.service.RewardService;

@RestController
@RequestMapping("/api/rewards")
public class RewardController {

    private final RewardService rewardService;

    public RewardController(RewardService rewardService) {
        this.rewardService = rewardService;
    }

    @GetMapping
    public ResponseEntity<List<PlayerReward>> getPlayerRewards(
            @RequestParam Long userId) {

        return ResponseEntity.ok(
                rewardService.getPlayerRewards(userId)
        );
    }

    @PostMapping("/{rewardId}/unlock")
    public ResponseEntity<String> unlockReward(
            @PathVariable Long rewardId,
            @RequestParam Long userId) {

        String result = rewardService.unlockReward(
                userId, rewardId
        );

        return ResponseEntity.ok(result);
    }
}
