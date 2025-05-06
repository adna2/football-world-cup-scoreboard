package org.scoreboard;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.NoSuchElementException;

public class Scoreboard {

    private final List<Match> matches = new ArrayList<>();

    public void startMatch(String homeTeam, String awayTeam) {
        validateTeams(homeTeam, awayTeam);
        checkIfMatchInProgress(homeTeam, awayTeam);
        matches.add(new Match(homeTeam, awayTeam));
    }

    public void updateScore(String homeTeam, String awayTeam, int homeScore, int awayScore) {
        Match match = findMatch(homeTeam, awayTeam);
        if (homeScore < 0 || awayScore < 0) {
            throw new IllegalArgumentException("Score cannot be negative.");
        }
        match.updateScore(homeScore, awayScore);
    }

    public void finishMatch(String homeTeam, String awayTeam) {
        Match match = findMatch(homeTeam, awayTeam);
        matches.remove(match);
    }

    public List<Match> getSummary() {
        return matches.stream()
                .sorted(
                        Comparator.comparingInt(Match::getTotalScore).reversed()
                                .thenComparing(Comparator.comparingLong(Match::getStartTime).reversed())
                )
                .toList();
    }

    private void validateTeams(String homeTeam, String awayTeam) {
        if (homeTeam == null || homeTeam.isBlank()) {
            throw new IllegalArgumentException("Home team name is mandatory.");
        }
        if (awayTeam == null || awayTeam.isBlank()) {
            throw new IllegalArgumentException("Away team name is mandatory.");
        }
        if (homeTeam.strip().equalsIgnoreCase(awayTeam.strip())) {
            throw new IllegalArgumentException("Teams must be different.");
        }
    }

    private void checkIfMatchInProgress(String homeTeam, String awayTeam) {
        boolean matchExists = matches.stream()
                .anyMatch(m -> m.getHomeTeam().equalsIgnoreCase(homeTeam)
                        && m.getAwayTeam().equalsIgnoreCase(awayTeam));
        if (matchExists) {
            throw new IllegalArgumentException("Match is already in progress.");
        }
    }

    private Match findMatch(String homeTeam, String awayTeam) {
        return matches.stream()
                .filter(m -> m.getHomeTeam().equalsIgnoreCase(homeTeam)
                        && m.getAwayTeam().equalsIgnoreCase(awayTeam))
                .findFirst()
                .orElseThrow(() -> new NoSuchElementException("Match not found."));
    }
}
