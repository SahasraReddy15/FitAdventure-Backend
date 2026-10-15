
package com.example.demo.controller;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;

import com.example.demo.entity.MonsterBattle;
import com.example.demo.service.MonsterBattleService;

@RestController
@RequestMapping("/api/monster-battle")
public class MonsterBattleController {

    private final MonsterBattleService monsterBattleService;

    public MonsterBattleController(
            MonsterBattleService monsterBattleService) {
        this.monsterBattleService = monsterBattleService;
    }

    @GetMapping("/{userId}")
    public MonsterBattle getBattle(@PathVariable Long userId) {
        try {
            return monsterBattleService.getBattle(userId);
        } catch (IllegalArgumentException ex) {
            throw new ResponseStatusException(
                    HttpStatus.NOT_FOUND, ex.getMessage());
        }
    }

    @PostMapping("/{userId}/attack")
    public MonsterBattle attackMonster(@PathVariable Long userId) {
        try {
            return monsterBattleService.damageMonster(userId, 25);
        } catch (IllegalArgumentException ex) {
            throw new ResponseStatusException(
                    HttpStatus.NOT_FOUND, ex.getMessage());
        }
    }

    @PostMapping("/{userId}/next")
    public MonsterBattle startNextBattle(@PathVariable Long userId) {
        try {
            return monsterBattleService.startNextBattle(userId);
        } catch (IllegalArgumentException ex) {
            throw new ResponseStatusException(
                    HttpStatus.NOT_FOUND, ex.getMessage());
        } catch (IllegalStateException ex) {
            throw new ResponseStatusException(
                    HttpStatus.CONFLICT, ex.getMessage());
        }
    }
}
