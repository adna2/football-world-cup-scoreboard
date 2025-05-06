package org.scoreboard;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.List;
import java.util.NoSuchElementException;

import static org.junit.jupiter.api.Assertions.*;
import static org.scoreboard.Messages.*;

public class ScoreboardTest {

    private Scoreboard scoreboard;

    @BeforeEach
    void setUp() {
        scoreboard = new Scoreboard();
    }

    @Test
    void shouldStartMatchSuccessfully() {
        scoreboard.startMatch("Mexico", "Canada");
        List<Match> matches = scoreboard.getSummary();
        assertEquals(1, matches.size());
        assertEquals("Mexico", matches.get(0).getHomeTeam());
        assertEquals("Canada", matches.get(0).getAwayTeam());
    }

    @Test
    void shouldNotAllowBlankTeamName() {
        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () ->
                scoreboard.startMatch(" ", "Canada")
        );
        assertEquals(HOME_TEAM_REQUIRED, exception.getMessage());
    }

    @Test
    void shouldNotAllowSameTeamMatch() {
        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () ->
                scoreboard.startMatch("Mexico", "Mexico")
        );
        assertEquals(TEAMS_MUST_DIFFER, exception.getMessage());
    }

    @Test
    void shouldNotAllowDuplicateMatch() {
        scoreboard.startMatch("Mexico", "Canada");
        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () ->
                scoreboard.startMatch("Canada", "Mexico")
        );
        assertEquals(MATCH_IN_PROGRESS, exception.getMessage());
    }

    @Test
    void shouldUpdateScoreSuccessfully() {
        scoreboard.startMatch("Mexico", "Canada");
        scoreboard.updateScore("Mexico", "Canada", 0, 1);
        Match match = scoreboard.getSummary().get(0);
        assertEquals(0, match.getHomeScore());
        assertEquals(1, match.getAwayScore());
        assertEquals(1, match.getTotalScore());
    }

    @Test
    void shouldNotUpdateScoreForNonexistentMatch() {
        NoSuchElementException exception = assertThrows(NoSuchElementException.class, () ->
                scoreboard.updateScore("X", "Y", 1, 0)
        );
        assertEquals(MATCH_NOT_FOUND, exception.getMessage());
    }

    @Test
    void shouldNotAllowNegativeScore() {
        scoreboard.startMatch("Mexico", "Canada");
        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () ->
                scoreboard.updateScore("Mexico", "Canada", -1, 0)
        );
        assertEquals(NEGATIVE_SCORE_NOT_ALLOWED, exception.getMessage());
    }

    @Test
    void shouldFinishMatchSuccessfully() {
        scoreboard.startMatch("Mexico", "Canada");
        scoreboard.finishMatch("Mexico", "Canada");
        assertEquals(0, scoreboard.getSummary().size());
    }

    @Test
    void shouldNotFinishNonexistentMatch() {
        NoSuchElementException exception = assertThrows(NoSuchElementException.class, () ->
                scoreboard.finishMatch("X", "Y")
        );
        assertEquals(MATCH_NOT_FOUND, exception.getMessage());
    }

    @Test
    void shouldReturnMatchSummaryInCorrectOrder() {
        scoreboard.startMatch("Mexico", "Canada");
        scoreboard.startMatch("Spain", "Brazil");
        scoreboard.startMatch("Germany", "France");
        scoreboard.startMatch("Uruguay", "Italy");
        scoreboard.startMatch("Argentina", "Australia");

        scoreboard.updateScore("Mexico", "Canada", 0, 5);
        scoreboard.updateScore("Spain", "Brazil", 10, 2);
        scoreboard.updateScore("Germany", "France", 2, 2);
        scoreboard.updateScore("Uruguay", "Italy", 6, 6);
        scoreboard.updateScore("Argentina", "Australia", 3, 1);

        List<Match> summary = scoreboard.getSummary();
        assertEquals("Uruguay", summary.get(0).getHomeTeam());   // Total score 12, more recent match
        assertEquals("Spain", summary.get(1).getHomeTeam());     // Total score 12
        assertEquals("Mexico", summary.get(2).getHomeTeam());    // Total score 5
        assertEquals("Argentina", summary.get(3).getHomeTeam()); // Total score 4, more recent match
        assertEquals("Germany", summary.get(4).getHomeTeam());   // Total score 4
    }
}
