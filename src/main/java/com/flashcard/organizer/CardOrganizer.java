package com.flashcard.organizer;

import com.flashcard.FlashCard;
import java.util.List;

/**
 * Interface for organizing the order of flashcards during a study session.
 * Implementations define different sorting/ordering strategies.
 */
public interface CardOrganizer {

    /**
     * Organizes the given list of flashcards according to this strategy.
     *
     * @param cards the list of cards to organize
     * @return a new list of cards in the desired order
     */
    List<FlashCard> organize(List<FlashCard> cards);
}
