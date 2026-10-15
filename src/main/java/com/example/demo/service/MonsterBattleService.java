
package com.example.demo.service;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.example.demo.entity.MonsterBattle;
import com.example.demo.repository.MonsterBattleRepository;
import com.example.demo.repository.UserRepository;

@Service
public class MonsterBattleService {

    private final MonsterBattleRepository monsterBattleRepository;
    private final UserRepository userRepository;

    private static final String[] MONSTER_NAMES = {
        "Forest Slime",
        "Mystic Fox",
        "Baby Dragon",
        "Ice Golem"
    };

    private static final int[] MONSTER_HP = {
        50, 75, 100, 125
    };

    public MonsterBattleService(
            MonsterBattleRepository monsterBattleRepository,
            UserRepository userRepository) {
        this.monsterBattleRepository = monsterBattleRepository;
        this.userRepository = userRepository;
    }

    private int getMonsterIndex(String monsterName) {
        for (int i = 0; i < MONSTER_NAMES.length; i++) {
            if (MONSTER_NAMES[i].equals(monsterName)) {
                return i;
            }
        }

        return 0;
    }

    @Transactional
    public MonsterBattle getBattle(Long userId) {
        userRepository.findById(userId)
                .orElseThrow(() ->
                        new IllegalArgumentException("User not found"));

        return monsterBattleRepository.findByUserId(userId)
                .orElseGet(() -> {
                    MonsterBattle battle = new MonsterBattle();

                    battle.setUserId(userId);
                    battle.setMonsterName(MONSTER_NAMES[0]);
                    battle.setCurrentHp(MONSTER_HP[0]);
                    battle.setMaxHp(MONSTER_HP[0]);
                    battle.setDefeated(false);

                    return monsterBattleRepository.save(battle);
                });
    }

    @Transactional
    public MonsterBattle damageMonster(Long userId, int damage) {

        MonsterBattle battle = getBattle(userId);

        System.out.println("HP BEFORE ATTACK: " + battle.getCurrentHp());
        System.out.println("DAMAGE RECEIVED: " + damage);

        if (battle.isDefeated()) {
            return battle;
        }

        int remainingHp = Math.max(
            0, battle.getCurrentHp() - damage
        );

        battle.setCurrentHp(remainingHp);
        battle.setDefeated(remainingHp == 0);

        System.out.println("HP AFTER ATTACK: " + remainingHp);

        return monsterBattleRepository.save(battle);
    }
    @Transactional
    public MonsterBattle startNextBattle(Long userId) {
        MonsterBattle battle = getBattle(userId);

        if (!battle.isDefeated()) {
            throw new IllegalStateException(
                    "Defeat the current monster before starting another battle");
        }

        int nextIndex = getMonsterIndex(battle.getMonsterName()) + 1;

        if (nextIndex >= MONSTER_NAMES.length) {
            throw new IllegalStateException(
                    "Congratulations! You defeated every monster!");
        }

        battle.setMonsterName(MONSTER_NAMES[nextIndex]);
        battle.setCurrentHp(MONSTER_HP[nextIndex]);
        battle.setMaxHp(MONSTER_HP[nextIndex]);
        battle.setDefeated(false);

        return monsterBattleRepository.save(battle);
    }
}
