package me.kkfish.competition;

import java.util.UUID;

public class CompetitionData {
    private final UUID playerUUID;
    private final String playerName;
    private int totalAmount = 0;
    private double totalValue = 0.0;
    private double maxSingleValue = 0.0;
    private double totalPoints = 0.0;

    public CompetitionData(UUID playerUUID, String playerName) {
        this.playerUUID = playerUUID;
        this.playerName = playerName;
    }

    public void addAmount() {
        totalAmount++;
    }

    public void addValue(double value) {
        totalValue += value;
        if (value > maxSingleValue) {
            maxSingleValue = value;
        }
    }

    public void addPoints(double points) {
        totalPoints += points;
    }

    public UUID getPlayerUUID() {
        return playerUUID;
    }

    public String getPlayerName() {
        return playerName;
    }

    public int getTotalAmount() {
        return totalAmount;
    }

    public double getTotalValue() {
        return totalValue;
    }

    public double getMaxSingleValue() {
        return maxSingleValue;
    }

    public double getTotalPoints() {
        return totalPoints;
    }
}
