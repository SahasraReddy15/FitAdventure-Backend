
package com.example.demo.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import com.example.demo.entity.Quest;

public interface QuestRepository extends JpaRepository<Quest, Long> {

    List<Quest> findByIsDailyTrue();

    boolean existsByTitleAndIsDailyTrue(String title);
}