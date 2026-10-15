package com.example.demo.service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;

import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

import com.example.demo.entity.PlayerReward;
import com.example.demo.entity.User;
import com.example.demo.repository.PlayerRewardRepository;
import com.example.demo.repository.UserRepository;

@Service
public class RewardService {

    private final UserRepository userRepository;
    private final PlayerRewardRepository playerRewardRepository;

    private static final Map<Long, Integer> REWARD_COSTS = Map.of(
            1L, 10,
            2L, 25,
            3L, 30,
            4L, 50
    );

    public RewardService(
            UserRepository userRepository,
            PlayerRewardRepository playerRewardRepository) {

        this.userRepository = userRepository;
        this.playerRewardRepository = playerRewardRepository;
    }

    public List<PlayerReward> getPlayerRewards(Long userId) {

        if (!userRepository.existsById(userId)) {
            throw new ResponseStatusException(
                    HttpStatus.NOT_FOUND,
                    "Player not found"
            );
        }

        return playerRewardRepository.findByUserId(userId);
    }

    @Transactional
    public String unlockReward(Long userId, Long rewardId) {

        User user = userRepository.findById(userId)
                .orElseThrow(() -> new ResponseStatusException(
                        HttpStatus.NOT_FOUND,
                        "Player not found"
                ));

        Integer rewardCost = REWARD_COSTS.get(rewardId);

        if (rewardCost == null) {
            throw new ResponseStatusException(
                    HttpStatus.BAD_REQUEST,
                    "Invalid reward ID"
            );
        }

        if (playerRewardRepository.existsByUserIdAndRewardId(
                userId, rewardId)) {

            throw new ResponseStatusException(
                    HttpStatus.CONFLICT,
                    "Reward already unlocked"
            );
        }

        if (user.getCoins() < rewardCost) {
            throw new ResponseStatusException(
                    HttpStatus.BAD_REQUEST,
                    "Not enough coins to unlock this reward"
            );
        }

        user.setCoins(user.getCoins() - rewardCost);
        userRepository.save(user);

        PlayerReward playerReward = new PlayerReward();
        playerReward.setUserId(userId);
        playerReward.setRewardId(rewardId);
        playerReward.setUnlockedAt(LocalDateTime.now());

        playerRewardRepository.save(playerReward);

        return "Reward unlocked successfully! " + rewardCost
                + " coins deducted.";
    }
}