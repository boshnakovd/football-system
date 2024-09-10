package com.example.football_system.service;

import com.example.football_system.model.Player;
import com.example.football_system.model.PlayerPairDuration;
import com.example.football_system.model.Record;
import com.example.football_system.repository.PlayerRepository;
import com.example.football_system.repository.RecordRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
public class PlayerPairService {

    @Autowired
    private RecordRepository recordRepository;

    @Autowired
    private PlayerRepository playerRepository;

    public List<PlayerPairDuration> findLongestPlayingPairs() {
        List<Record> records = recordRepository.findAll();
        Map<String, Integer> pairMinutes = new HashMap<>();

        for (Record record1 : records) {
            for (Record record2 : records) {
                if (!record1.getPlayer().getPlayerId().equals(record2.getPlayer().getPlayerId())
                        && record1.getMatch().getId().equals(record2.getMatch().getId())) {


                    int commonMinutes = calculateCommonMinutes(record1, record2);


                    String pairKey = createPairKey(record1.getPlayer().getPlayerId(), record2.getPlayer().getPlayerId());


                    pairMinutes.put(pairKey, pairMinutes.getOrDefault(pairKey, 0) + commonMinutes);
                }
            }
        }


        return pairMinutes.entrySet().stream()
                .map(entry -> {
                    String[] ids = entry.getKey().split("_");
                    Player player1 = playerRepository.findById(Long.parseLong(ids[0])).orElse(null);
                    Player player2 = playerRepository.findById(Long.parseLong(ids[1])).orElse(null);
                    return new PlayerPairDuration(player1, player2, entry.getValue());
                })
                .sorted(Comparator.comparingInt(PlayerPairDuration::getTotalMinutesTogether).reversed())
                .collect(Collectors.toList());
    }

    private int calculateCommonMinutes(Record record1, Record record2) {
        int fromMinute = Math.max(record1.getFromMinutes(), record2.getFromMinutes());
        int toMinute = Math.min(
                record1.getToMinutes() != null ? record1.getToMinutes() : 90,
                record2.getToMinutes() != null ? record2.getToMinutes() : 90
        );
        return Math.max(0, toMinute - fromMinute);
    }

    private String createPairKey(Long player1Id, Long player2Id) {
        return player1Id < player2Id ? player1Id + "_" + player2Id : player2Id + "_" + player1Id;
    }
}