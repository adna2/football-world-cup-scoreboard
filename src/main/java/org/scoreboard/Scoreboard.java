package org.scoreboard;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.NoSuchElementException;

import static org.scoreboard.Messages.*;

public class Scoreboard {

    private final List<Match> matches = new ArrayList<>();

    public void startMatch(String homeTeam, String awayTeam) {
        Match match = Match.create(homeTeam, awayTeam);
        checkIfMatchInProgress(homeTeam, awayTeam);
        matches.add(match);
    }

    public void updateScore(String homeTeam, String awayTeam, int homeScore, int awayScore) {
        Match match = findMatch(homeTeam, awayTeam);
        if (homeScore < 0 || awayScore < 0) {
            throw new IllegalArgumentException(NEGATIVE_SCORE_NOT_ALLOWED);
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

    private void checkIfMatchInProgress(String homeTeam, String awayTeam) {
        boolean matchExists = matches.stream()
                .anyMatch(m -> (m.getHomeTeam().equalsIgnoreCase(homeTeam) && m.getAwayTeam().equalsIgnoreCase(awayTeam)) ||
                        (m.getHomeTeam().equalsIgnoreCase(awayTeam) && m.getAwayTeam().equalsIgnoreCase(homeTeam)));
        if (matchExists) {
            throw new IllegalArgumentException(MATCH_IN_PROGRESS);
        }
    }

    private Match findMatch(String homeTeam, String awayTeam) {
        return matches.stream()
                .filter(m -> m.getHomeTeam().equalsIgnoreCase(homeTeam)
                        && m.getAwayTeam().equalsIgnoreCase(awayTeam))
                .findFirst()
                .orElseThrow(() -> new NoSuchElementException(MATCH_NOT_FOUND));
    }
}
