package com.example.football_system.service;

import com.example.football_system.model.Player;
import com.example.football_system.model.Record;
import com.example.football_system.repository.RecordRepository;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
public class PlayerStatsService {

    private final RecordRepository recordRepository;

    public PlayerStatsService(RecordRepository recordRepository) {
        this.recordRepository = recordRepository;
    }

    public List<String> findLongestPlayingPair() {
        List<Record> records = recordRepository.findAll();
        Map<String, Integer> timeTogether = new HashMap<>();

        for (Record record1 : records) {
            for (Record record2 : records) {
                if (!record1.getPlayer().equals(record2.getPlayer()) && record1.getMatch().equals(record2.getMatch())) {
                    int timePlayed = Math.min(
                            record1.getToMinutes() == null ? 90 : record1.getToMinutes(),
                            record2.getToMinutes() == null ? 90 : record2.getToMinutes()
                    ) - Math.max(record1.getFromMinutes(), record2.getFromMinutes());
                    String pairKey = generatePairKey(record1.getPlayer(), record2.getPlayer());
                    timeTogether.put(pairKey, timeTogether.getOrDefault(pairKey, 0) + timePlayed);
                }
            }
        }

        return getTopPlayingPairs(timeTogether);
    }

    private String generatePairKey(Player player1, Player player2) {
        if (player1.getPlayerId() < player2.getPlayerId()) {
            return player1.getPlayerId() + "," + player2.getPlayerId();
        } else {
            return player2.getPlayerId() + "," + player1.getPlayerId();
        }
    }
    private List<String> getTopPlayingPairs(Map<String, Integer> timeTogether) {
        List<Map.Entry<String, Integer>> sortedList = new ArrayList<>(timeTogether.entrySet());
        sortedList.sort((entry1, entry2) -> entry2.getValue() - entry1.getValue());

        List<String> result = new ArrayList<>();
        for (Map.Entry<String, Integer> entry : sortedList) {
            result.add(entry.getKey() + ", " + entry.getValue());
        }
        return result;
    }
}