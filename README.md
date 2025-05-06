# Live Football World Cup Scoreboard

A simple library to simulate a live scoreboard for ongoing World Cup matches. It allows users to:

- Start a new match with initial scores set to 0-0
- Update the score of an ongoing match
- Finish a match, removing it from the scoreboard
- Get a summary of ongoing matches, ordered by total score and start time

## Features

- In-memory storage using standard Java collections
- Input validation (null, blank, duplicate matches, same teams, negative scores)
- Case-insensitive team names
- Summary ordered by total score, then by most recent start time
- Fully tested using JUnit

## Example:

```java
Scoreboard scoreboard = new Scoreboard();

scoreboard.startMatch("Mexico", "Canada");
scoreboard.startMatch("Spain", "Brazil");
scoreboard.updateScore("Spain", "Brazil", 10, 2);
scoreboard.updateScore("Mexico", "Canada", 0, 5);

List<Match> summary = scoreboard.getSummary();
summary.forEach(System.out::println);
```

## Assumptions & Considerations
- Each team name must be non-null, non-blank, and different from the other
- A match cannot be started more than once between the same teams
- Negative scores are not allowed

## Possible Future Improvements
- Thread safety for concurrent access
- Make Match immutable (return new instance on score update)
- Extend sorting logic for handling complex tiebreakers
- Consider using Lombok to reduce boilerplate code
