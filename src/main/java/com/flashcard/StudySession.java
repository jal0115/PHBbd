package com.flashcard;

import com.flashcard.achievement.Achievement;
import com.flashcard.achievement.AchievementTracker;
import com.flashcard.organizer.CardOrganizer;

import java.util.List;
import java.util.Scanner;

/**
 * Manages a flashcard study session including card presentation,
 * answer checking, repetitions, and achievement tracking.
 */
public class StudySession {

    private final List<FlashCard> cards;
    private final CardOrganizer organizer;
    private final int requiredRepetitions;
    private final boolean invertCards;
    private final AchievementTracker achievementTracker;
    private final Scanner scanner;

    /**
     * Creates a new study session.
     *
     * @param cards               list of cards to study
     * @param organizer           the card ordering strategy
     * @param requiredRepetitions how many correct answers needed per card
     * @param invertCards         if true, swap question and answer display
     */
    public StudySession(List<FlashCard> cards, CardOrganizer organizer,
                        int requiredRepetitions, boolean invertCards) {
        this.cards = cards;
        this.organizer = organizer;
        this.requiredRepetitions = requiredRepetitions;
        this.invertCards = invertCards;
        this.achievementTracker = new AchievementTracker();
        this.scanner = new Scanner(System.in);
    }

    /**
     * Starts the study session. Loops through rounds until all cards
     * have been answered correctly the required number of times.
     */
    public void start() {
        System.out.println("\n=== Study Session Started ===");
        System.out.println("Cards to study: " + cards.size());
        System.out.println("Required correct answers per card: " + requiredRepetitions);
        System.out.println("(Press Enter with no input to quit)\n");

        // Track how many correct answers each card has accumulated
        int[] correctCounts = new int[cards.size()];

        boolean sessionComplete = false;

        while (!sessionComplete) {
            List<FlashCard> roundCards = organizer.organize(cards);
            long roundStart = System.currentTimeMillis();

            for (FlashCard card : roundCards) {
                int cardIndex = cards.indexOf(card);

                // Skip cards that already met the repetition requirement
                if (correctCounts[cardIndex] >= requiredRepetitions) {
                    continue;
                }

                String prompt = invertCards ? card.getAnswer() : card.getQuestion();
                String expectedAnswer = invertCards ? card.getQuestion() : card.getAnswer();

                System.out.print("Q: " + prompt + "\n> ");
                String userInput = scanner.nextLine().trim();

                if (userInput.isEmpty()) {
                    System.out.println("\nExiting study session.");
                    printSummary();
                    return;
                }

                boolean correct = userInput.equalsIgnoreCase(expectedAnswer);
                card.recordResult(correct);

                if (correct) {
                    correctCounts[cardIndex]++;
                    System.out.println("✓ Correct!");
                } else {
                    System.out.println("✗ Wrong! The answer was: " + expectedAnswer);
                }

                // Check per-card achievements
                List<Achievement> cardAchievements = achievementTracker.checkCardAchievements(card);
                printAchievements(cardAchievements);

                System.out.println();
            }

            long roundTime = System.currentTimeMillis() - roundStart;

            // Check round achievements
            List<Achievement> roundAchievements = achievementTracker.checkRoundAchievements(roundCards, roundTime);
            printAchievements(roundAchievements);

            // Check if all cards have been answered correctly enough times
            sessionComplete = true;
            for (int count : correctCounts) {
                if (count < requiredRepetitions) {
                    sessionComplete = false;
                    break;
                }
            }

            if (!sessionComplete) {
                System.out.println("--- Starting next round ---\n");
            }
        }

        System.out.println("\n🎉 Session complete! All cards mastered.");
        printSummary();
    }

    /**
     * Prints achievements earned.
     *
     * @param achievements list of newly earned achievements to display
     */
    private void printAchievements(List<Achievement> achievements) {
        for (Achievement a : achievements) {
            System.out.println("\n★ Achievement Unlocked: " + a.getTitle());
            System.out.println("  " + a.getDescription());
        }
    }

    /**
     * Prints the end-of-session summary including all earned achievements.
     */
    private void printSummary() {
        System.out.println("\n=== Session Summary ===");
        for (FlashCard card : cards) {
            System.out.printf("  %-40s  Correct: %d / %d%n",
                card.getQuestion(), card.getTimesCorrect(), card.getTimesAnswered());
        }

        List<Achievement> earned = achievementTracker.getEarned();
        if (!earned.isEmpty()) {
            System.out.println("\nAchievements earned this session:");
            for (Achievement a : earned) {
                System.out.println("  " + a.getTitle() + " - " + a.getDescription());
            }
        } else {
            System.out.println("\nNo achievements earned this session. Keep practicing!");
        }
    }
}
