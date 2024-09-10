package com.example.football_system.service;

import com.example.football_system.model.PlayerPairDuration;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
public class PlayerPairServiceTest {

    @Autowired
    private PlayerPairService playerPairService;

    @Test
    public void testLongestPlayingPairs() {
        List<PlayerPairDuration> pairs = playerPairService.findLongestPlayingPairs();
        assertFalse(pairs.isEmpty());
        assertEquals("Player 1", pairs.get(0).getPlayer1().getFullName());  // Замени с правилното име
    }
}