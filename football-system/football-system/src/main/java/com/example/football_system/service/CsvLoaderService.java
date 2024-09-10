package com.example.football_system.service;

import com.example.football_system.config.CsvProperties;
import com.example.football_system.model.Match;
import com.example.football_system.model.Player;
import com.example.football_system.model.Record;
import com.example.football_system.model.Team;
import com.example.football_system.repository.MatchRepository;
import com.example.football_system.repository.PlayerRepository;
import com.example.football_system.repository.RecordRepository;
import com.example.football_system.repository.TeamRepository;
import jakarta.annotation.PostConstruct;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.BufferedReader;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.Arrays;
import java.util.Optional;
import java.util.stream.Stream;

@Service
public class CsvLoaderService {

    private static final Logger logger = LoggerFactory.getLogger(CsvLoaderService.class);
    private static final DateTimeFormatter DATE_FORMATTER = DateTimeFormatter.ofPattern("M/d/yyyy");
    private final PlayerRepository playerRepository;
    private final TeamRepository teamRepository;
    private final MatchRepository matchRepository;
    private final RecordRepository recordRepository;

    private final CsvProperties csvProperties;

    @Autowired
    public CsvLoaderService(PlayerRepository playerRepository, TeamRepository teamRepository,
                            MatchRepository matchRepository, RecordRepository recordRepository,
                            CsvProperties csvProperties) {
        this.playerRepository = playerRepository;
        this.teamRepository = teamRepository;
        this.matchRepository = matchRepository;
        this.recordRepository = recordRepository;
        this.csvProperties = csvProperties;
    }

    @PostConstruct
    public void init() {
        Stream.of("teams.csv", "players.csv", "matches.csv", "records.csv").forEach(this::loadCsvFile);
    }

    public void loadCsvFile(String fileName) {
        Path filePath = Path.of(csvProperties.getFilePath(), fileName);
        if (!Files.exists(filePath)) {
            logger.error("{} not found at the specified path", fileName);
            return;
        }

        try (BufferedReader reader = Files.newBufferedReader(filePath)) {
            String line;
            int lineNumber = 0;
            while ((line = reader.readLine()) != null) {
                lineNumber++;
                if (lineNumber == 1) continue; // Skip header line
                String[] fields = line.split(",");
                switch (fileName) {
                    case "teams.csv" -> processTeam(fields, lineNumber);
                    case "players.csv" -> processPlayer(fields, lineNumber);
                    case "matches.csv" -> processMatch(fields, lineNumber);
                    case "records.csv" -> processRecord(fields, lineNumber);
                }
            }
        } catch (IOException e) {
            logger.error("Error reading file: {}", filePath, e);
        }
    }

    private void processTeam(String[] fields, int lineNumber) {
        if (fields.length < 2) {
            logger.error("Invalid data for team at line {}: {}", lineNumber, String.join(",", fields));
            return;
        }
        try {
            Long id = Long.parseLong(fields[0]);
            String teamName = fields[1].trim();
            teamRepository.findById(id).ifPresentOrElse(
                    team -> logger.info("Team already exists: {}", teamName),
                    () -> {
                        Team newTeam = new Team(id, teamName);
                        teamRepository.save(newTeam);
                        logger.info("Team {} added successfully.", teamName);
                    }
            );
        } catch (NumberFormatException e) {
            logger.error("Invalid ID format for team at line {}: {}", lineNumber, fields[0]);
        }
    }

    private void processPlayer(String[] fields, int lineNumber) {
        if (fields.length < 5) {
            logger.error("Invalid data for player at line {}: {}", lineNumber, String.join(",", fields));
            return;
        }
        try {
            Long playerId = Long.parseLong(fields[0]);
            Integer teamNumber = Integer.parseInt(fields[1]);
            String position = fields[2].trim();
            String playerName = fields[3].trim();
            Long teamId = Long.parseLong(fields[4]);

            teamRepository.findById(teamId).ifPresentOrElse(team -> {
                Player player = new Player(playerId, playerName, teamNumber, position, team);
                playerRepository.save(player);
                logger.info("Player {} added successfully.", playerName);
            }, () -> logger.error("Team with ID {} not found for player: {} at line {}", teamId, playerName, lineNumber));
        } catch (NumberFormatException e) {
            logger.error("Number format error for player at line {}: {}", lineNumber, String.join(",", fields));
        }
    }

    private void processMatch(String[] fields, int lineNumber) {
        if (fields.length < 5) {
            logger.error("Insufficient data at line {}: {}", lineNumber, Arrays.toString(fields));
            return;
        }
        try {
            Long matchId = Long.parseLong(fields[0]);
            String homeTeamName = fields[1].trim();
            String awayTeamName = fields[2].trim();
            String score = fields[3].trim();
            LocalDate matchDate = LocalDate.parse(fields[4], DATE_FORMATTER);


            int homeGoals = 0;
            int awayGoals = 0;

            if (score.contains("-")) {
                String[] scoreParts = score.split("-");
                if (scoreParts.length == 2) {
                    homeGoals = parseScore(scoreParts[0].trim());
                    awayGoals = parseScore(scoreParts[1].trim());
                } else {
                    logger.error("Invalid score format at line {}: '{}'", lineNumber, score);
                    return;
                }
            } else {
                logger.error("Invalid score format at line {}: '{}'", lineNumber, score);
                return;
            }

            Team homeTeam = teamRepository.findByName(homeTeamName)
                    .orElseThrow(() -> new IllegalArgumentException("Home team not found: " + homeTeamName));
            Team awayTeam = teamRepository.findByName(awayTeamName)
                    .orElseThrow(() -> new IllegalArgumentException("Away team not found: " + awayTeamName));

            Match match = new Match(matchId, homeTeam, awayTeam, homeGoals, awayGoals, matchDate);
            matchRepository.save(match);
            logger.info("Match {} added successfully.", matchId);
        } catch (NumberFormatException | DateTimeParseException e) {
            logger.error("Error processing match data at line {}: {}, data: {}", lineNumber, e.getMessage(), Arrays.toString(fields));
        } catch (IllegalArgumentException e) {
            logger.error("Validation error at line {}: {}, data: {}", lineNumber, e.getMessage(), Arrays.toString(fields));
        }
    }

    private int parseScore(String scorePart) {

        if (scorePart.contains("(")) {

            return Integer.parseInt(scorePart.split("\\(")[0].trim());
        } else {
            return Integer.parseInt(scorePart.trim());
        }
    }

    private void processRecord(String[] fields, int lineNumber) {
        if (fields.length < 7) {
            logger.error("Invalid data for record at line {}: too few fields {}", lineNumber, String.join(",", fields));
            return;
        }
        try {
            Long recordId = Long.parseLong(fields[0]);
            Long playerId = Long.parseLong(fields[1]);
            int goalsScored = Integer.parseInt(fields[2]);
            int assists = Integer.parseInt(fields[3]);
            int yellowCards = Integer.parseInt(fields[4]);
            int redCards = Integer.parseInt(fields[5]);
            Long matchId = Long.parseLong(fields[6]);

            Player player = playerRepository.findById(playerId)
                    .orElseThrow(() -> new IllegalArgumentException("Player not found: " + playerId));
            Match match = matchRepository.findById(matchId)
                    .orElseThrow(() -> new IllegalArgumentException("Match not found: " + matchId));

            Record record = new Record(recordId, player, match, goalsScored, assists, yellowCards, redCards);
            recordRepository.save(record);
            logger.info("Record {} added successfully.", recordId);
        } catch (NumberFormatException e) {
            logger.error("Number format error at line {}: {}", lineNumber, e.getMessage());
        } catch (IllegalArgumentException e) {
            logger.error("Validation error at line {}: {}", lineNumber, e.getMessage());
        }
    }
}
