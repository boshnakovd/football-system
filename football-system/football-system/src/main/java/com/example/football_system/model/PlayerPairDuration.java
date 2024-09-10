package com.example.football_system.model;


public class PlayerPairDuration {
    private Player player1;
    private Player player2;
    private int totalMinutesTogether;

    public PlayerPairDuration(Player player1, Player player2, int totalMinutesTogether) {
        this.player1 = player1;
        this.player2 = player2;
        this.totalMinutesTogether = totalMinutesTogether;
    }

    public Player getPlayer1() {
        return player1;
    }

    public Player getPlayer2() {
        return player2;
    }

    public int getTotalMinutesTogether() {
        return totalMinutesTogether;
    }
}