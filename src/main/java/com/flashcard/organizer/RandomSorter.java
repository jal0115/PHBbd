package com.flashcard.organizer;

import com.flashcard.FlashCard;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * Organizes flashcards in a random order.
 */
public class RandomSorter implements CardOrganizer {

    @Override
    public List<FlashCard> organize(List<FlashCard> cards) {
        List<FlashCard> shuffled = new ArrayList<>(cards);
        Collections.shuffle(shuffled);
        return shuffled;
    }
}
