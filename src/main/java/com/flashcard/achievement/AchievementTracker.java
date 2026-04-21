package com.flashcard.achievement;

import com.flashcard.FlashCard;
import java.util.ArrayList;
import java.util.List;

/**
 * Tracks and evaluates achievements during a flashcard study session.
 * Achievements are checked after each round and after each individual answer.
 */
public class AchievementTracker {

    private final List<Achievement> earned = new ArrayList<>();

    /**
     * Checks achievements after a full round of cards.
     *
     * @param cards     the cards that were studied this round
     * @param roundTime total time taken for the round in milliseconds
     * @return list of newly earned achievements
     */
    public List<Achievement> checkRoundAchievements(List<FlashCard> cards, long roundTime) {
        List<Achievement> newAchievements = new ArrayList<>();

        // CORRECT: All cards answered correctly this round
        boolean allCorrect = cards.stream().allMatch(FlashCard::isLastAnswerCorrect);
        if (allCorrect && !earned.contains(Achievement.CORRECT)) {
            earned.add(Achievement.CORRECT);
            newAchievements.add(Achievement.CORRECT);
        }

        // SPEED: Average answer time under 5 seconds
        if (!cards.isEmpty()) {
            double avgSeconds = (roundTime / 1000.0) / cards.size();
            if (avgSeconds < 5.0 && !earned.contains(Achievement.SPEED)) {
                earned.add(Achievement.SPEED);
                newAchievements.add(Achievement.SPEED);
            }
        }

        return newAchievements;
    }

    /**
     * Checks achievements after a single card is answered.
     *
     * @param card the card just answered
     * @return list of newly earned achievements
     */
    public List<Achievement> checkCardAchievements(FlashCard card) {
        List<Achievement> newAchievements = new ArrayList<>();

        // REPEAT: Card answered more than 5 times total
        if (card.getTimesAnswered() > 5 && !earned.contains(Achievement.REPEAT)) {
            earned.add(Achievement.REPEAT);
            newAchievements.add(Achievement.REPEAT);
        }

        // CONFIDENT: Card answered correctly at least 3 times
        if (card.getTimesCorrect() >= 3 && !earned.contains(Achievement.CONFIDENT)) {
            earned.add(Achievement.CONFIDENT);
            newAchievements.add(Achievement.CONFIDENT);
        }

        return newAchievements;
    }

    /**
     * Returns all achievements earned so far.
     *
     * @return list of earned achievements
     */
    public List<Achievement> getEarned() {
        return new ArrayList<>(earned);
    }
}
