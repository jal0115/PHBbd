package com.flashcard.organizer;

import com.flashcard.FlashCard;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

/**
 * Organizes flashcards so the most-missed cards appear first.
 * Cards with more wrong answers are shown first.
 */
public class WorstFirstSorter implements CardOrganizer {

    @Override
    public List<FlashCard> organize(List<FlashCard> cards) {
        List<FlashCard> sorted = new ArrayList<>(cards);
        sorted.sort(Comparator.comparingInt(FlashCard::getTimesWrong).reversed());
        return sorted;
    }
}
