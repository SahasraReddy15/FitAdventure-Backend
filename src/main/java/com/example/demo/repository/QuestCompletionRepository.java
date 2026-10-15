
package com.example.demo.repository;
import org.springframework.data.jpa.repository.JpaRepository;

import com.example.demo.entity.QuestCompletion;

public interface QuestCompletionRepository
        extends JpaRepository<QuestCompletion, Long> {

    boolean existsByUserIdAndQuestId(Long userId, Long questId);

    long countByUserId(Long userId);
}
