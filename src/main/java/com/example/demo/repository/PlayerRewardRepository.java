package com.example.demo.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.example.demo.entity.PlayerReward;

public interface PlayerRewardRepository
        extends JpaRepository<PlayerReward, Long> {

    List<PlayerReward> findByUserId(Long userId);

    Optional<PlayerReward> findByUserIdAndRewardId(
            Long userId,
            Long rewardId
    );

    boolean existsByUserIdAndRewardId(
            Long userId,
            Long rewardId
    );
}