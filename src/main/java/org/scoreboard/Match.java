package org.scoreboard;

import static org.scoreboard.Messages.*;

public class Match {

    private final String homeTeam;
    private final String awayTeam;
    private int homeScore;
    private int awayScore;
    private final long startTime;

    public Match(String homeTeam, String awayTeam) {
        this.homeTeam = homeTeam;
        this.awayTeam = awayTeam;
        this.homeScore = 0;
        this.awayScore = 0;
        this.startTime = System.nanoTime();
    }

    public static Match create(String homeTeam, String awayTeam) {
        if (homeTeam == null || awayTeam == null) {
            throw new IllegalArgumentException(TEAM_NAMES_REQUIRED);
        }

        String normalizedHome = homeTeam.strip();
        String normalizedAway = awayTeam.strip();

        if (normalizedHome.isEmpty()) {
            throw new IllegalArgumentException(HOME_TEAM_REQUIRED);
        }
        if (normalizedAway.isEmpty()) {
            throw new IllegalArgumentException(AWAY_TEAM_REQUIRED);
        }
        if (normalizedHome.equalsIgnoreCase(normalizedAway)) {
            throw new IllegalArgumentException(TEAMS_MUST_DIFFER);
        }

        return new Match(normalizedHome, normalizedAway);
    }

    public String getHomeTeam() { return homeTeam; }
    public String getAwayTeam() { return awayTeam; }
    public int getHomeScore() { return homeScore; }
    public int getAwayScore() { return awayScore; }
    public long getStartTime() { return startTime; }

    public void updateScore(int homeScore, int awayScore) {
        this.homeScore = homeScore;
        this.awayScore = awayScore;
    }

    public int getTotalScore() {
        return homeScore + awayScore;
    }

    @Override
    public String toString() {
        return homeTeam + " " + homeScore + " - " + awayTeam + " " + awayScore;
    }
}
