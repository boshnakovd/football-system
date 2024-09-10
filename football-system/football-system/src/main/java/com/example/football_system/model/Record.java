package com.example.football_system.model;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToOne;

@Entity
public class Record {

    @Id
    private Long recordId;

    @ManyToOne
    private Player player;

    @ManyToOne
    private Match match;

    private int goalsScored;
    private int assists;
    private int yellowCards;
    private int redCards;


    private int fromMinutes;


    private Integer toMinutes;


    public Record(Long recordId, Player player, Match match, int goalsScored, int assists, int yellowCards, int redCards) {}


    public Record(Long recordId, Player player, Match match, int goalsScored, int assists, int yellowCards, int redCards, int fromMinutes, Integer toMinutes) {
        this.recordId = recordId;
        this.player = player;
        this.match = match;
        this.goalsScored = goalsScored;
        this.assists = assists;
        this.yellowCards = yellowCards;
        this.redCards = redCards;
        this.fromMinutes = fromMinutes;
        this.toMinutes = toMinutes;
    }

    // Getters и Setters

    public Long getRecordId() {
        return recordId;
    }

    public void setRecordId(Long recordId) {
        this.recordId = recordId;
    }

    public Player getPlayer() {
        return player;
    }

    public void setPlayer(Player player) {
        this.player = player;
    }

    public Match getMatch() {
        return match;
    }

    public void setMatch(Match match) {
        this.match = match;
    }

    public int getGoalsScored() {
        return goalsScored;
    }

    public void setGoalsScored(int goalsScored) {
        this.goalsScored = goalsScored;
    }

    public int getAssists() {
        return assists;
    }

    public void setAssists(int assists) {
        this.assists = assists;
    }

    public int getYellowCards() {
        return yellowCards;
    }

    public void setYellowCards(int yellowCards) {
        this.yellowCards = yellowCards;
    }

    public int getRedCards() {
        return redCards;
    }

    public void setRedCards(int redCards) {
        this.redCards = redCards;
    }

    public int getFromMinutes() {
        return fromMinutes;
    }

    public void setFromMinutes(int fromMinutes) {
        this.fromMinutes = fromMinutes;
    }

    public Integer getToMinutes() {
        return toMinutes;
    }

    public void setToMinutes(Integer toMinutes) {
        this.toMinutes = toMinutes;
    }
}