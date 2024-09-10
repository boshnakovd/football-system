package com.example.football_system.controller;

import com.example.football_system.service.CsvLoaderService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;
import org.springframework.http.HttpStatus;

import java.util.concurrent.CompletableFuture;

@RestController
public class CsvController {

    private final CsvLoaderService csvLoaderService;
    private static final Logger logger = LoggerFactory.getLogger(CsvController.class);

    public CsvController(CsvLoaderService csvLoaderService) {
        this.csvLoaderService = csvLoaderService;
    }

    @GetMapping("/loadTeams")
    public CompletableFuture<ResponseEntity<String>> loadTeams() {
        return CompletableFuture.supplyAsync(() -> {
            try {
                csvLoaderService.loadCsvFile("teams.csv");
                return ResponseEntity.ok("Teams loaded successfully!");
            } catch (Exception e) {
                logger.error("Error loading teams: ", e);
                throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "Error loading teams", e);
            }
        });
    }

    @GetMapping("/loadPlayers")
    public CompletableFuture<ResponseEntity<String>> loadPlayers() {
        return CompletableFuture.supplyAsync(() -> {
            try {
                csvLoaderService.loadCsvFile("players.csv");
                return ResponseEntity.ok("Players loaded successfully!");
            } catch (Exception e) {
                logger.error("Error loading players: ", e);
                throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "Error loading players", e);
            }
        });
    }

    @GetMapping("/loadMatches")
    public CompletableFuture<ResponseEntity<String>> loadMatches() {
        return CompletableFuture.supplyAsync(() -> {
            try {
                csvLoaderService.loadCsvFile("matches.csv");
                return ResponseEntity.ok("Matches loaded successfully!");
            } catch (Exception e) {
                logger.error("Error loading matches: ", e);
                throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "Error loading matches", e);
            }
        });
    }

    @GetMapping("/loadRecords")
    public CompletableFuture<ResponseEntity<String>> loadRecords() {
        return CompletableFuture.supplyAsync(() -> {
            try {
                csvLoaderService.loadCsvFile("records.csv");
                return ResponseEntity.ok("Records loaded successfully!");
            } catch (Exception e) {
                logger.error("Error loading records: ", e);
                throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "Error loading records", e);
            }
        });
    }
}
