package com.flashcard.organizer;

import com.flashcard.FlashCard;
import java.util.ArrayList;
import java.util.List;

/**
 * Organizes flashcards so that cards answered incorrectly in the previous
 * round appear first, preserving relative order within each group.
 *
 * <p>Strategy: Cards that were answered incorrectly last time come first.
 * Cards that were answered correctly last time come after.
 * Within each group, the relative order is preserved (stable partition).</p>
 */
public class RecentMistakesFirstSorter implements CardOrganizer {

    @Override
    public List<FlashCard> organize(List<FlashCard> cards) {
        List<FlashCard> wrongFirst = new ArrayList<>();
        List<FlashCard> correctAfter = new ArrayList<>();

        for (FlashCard card : cards) {
            if (!card.isLastAnswerCorrect()) {
                wrongFirst.add(card);
            } else {
                correctAfter.add(card);
            }
        }

        // Wrong answers go first, then correct ones - internal order preserved
        wrongFirst.addAll(correctAfter);
        return wrongFirst;
    }
}
