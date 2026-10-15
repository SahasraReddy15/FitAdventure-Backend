
package com.example.demo.service;

import org.springframework.stereotype.Service;

@Service
public class GameLevelService {

    public int calculateLevel(int totalXp) {
        return Math.max(1, Math.max(0, totalXp) / 100 + 1);
    }

    public int getXpForNextLevel(int totalXp) {
        int safeXp = Math.max(0, totalXp);
        return calculateLevel(safeXp) * 100 - safeXp;
    }

    public boolean isZoneUnlocked(int level, String zoneName) {
        if (zoneName == null) {
            return false;
        }

        switch (zoneName.trim().toLowerCase()) {
            case "starter village":
                return level >= 1;
            case "enchanted forest":
                return level >= 2;
            case "crystal lake":
                return level >= 3;
            case "dragon mountain":
                return level >= 5;
            case "royal castle":
                return level >= 8;
            default:
                return false;
        }
    }
}
