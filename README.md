# Flashcard CLI

A command-line flashcard study system built with Java and Maven.

## Usage

```
flashcard <cards-file> [options]

Options:
  -h, --help                Show this help message
  --order <order>           Card ordering strategy:
                              random (default)
                              worst-first
                              recent-mistakes-first
  --repetitions <num>       Correct answers required per card (default: 1)
  --invertCards             Swap question and answer display
```

## Cards File Format

```
# This is a comment
Q: What is the capital of France?
A: Paris

Q: What does JVM stand for?
A: Java Virtual Machine
```

## Building

```bash
mvn clean package
java -jar target/flashcard-cli-1.0.0.jar sample-cards.txt --order random --repetitions 2
```

## Running Tests

```bash
mvn test
```

## Achievements

| Achievement | Condition |
|-------------|-----------|
| ⚡ SPEED     | Average answer time under 5 seconds per round |
| 🏆 CORRECT   | All cards answered correctly in a round |
| 🔁 REPEAT    | A single card answered more than 5 times |
| 💪 CONFIDENT | A single card answered correctly at least 3 times |

## Architecture

- `CardOrganizer` — interface for card ordering strategies
- `RecentMistakesFirstSorter` — puts last-round mistakes first, preserving relative order
- `WorstFirstSorter` — sorts by total wrong answer count
- `RandomSorter` — shuffles cards randomly
- `AchievementTracker` — evaluates and records earned achievements
- `StudySession` — manages the full study loop
- `CardLoader` — reads cards from text files
- `Main` — CLI entry point using Apache Commons CLI
