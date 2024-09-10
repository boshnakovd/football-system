package com.example.football_system.controller;

import com.example.football_system.model.PlayerPairDuration;
import com.example.football_system.service.PlayerPairService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/playerpairs")
public class PlayerPairController {

    @Autowired
    private PlayerPairService playerPairService;

    @GetMapping("/longest")
    public List<PlayerPairDuration> getLongestPlayingPairs() {
        return playerPairService.findLongestPlayingPairs();
    }
}

