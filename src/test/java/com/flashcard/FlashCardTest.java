package com.flashcard;

import com.flashcard.achievement.Achievement;
import com.flashcard.achievement.AchievementTracker;
import com.flashcard.organizer.RecentMistakesFirstSorter;
import com.flashcard.organizer.WorstFirstSorter;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.DisplayName;

import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Unit tests for the Flashcard CLI system.
 */
public class FlashCardTest {

    @Test
    @DisplayName("FlashCard records correct answers")
    public void testRecordCorrect() {
        FlashCard card = new FlashCard("1+1", "2");
        card.recordResult(true);
        assertEquals(1, card.getTimesCorrect());
        assertEquals(1, card.getTimesAnswered());
        assertTrue(card.isLastAnswerCorrect());
    }

    @Test
    @DisplayName("FlashCard records wrong answers")
    public void testRecordWrong() {
        FlashCard card = new FlashCard("1+1", "2");
        card.recordResult(false);
        assertEquals(0, card.getTimesCorrect());
        assertEquals(1, card.getTimesWrong());
        assertFalse(card.isLastAnswerCorrect());
    }

    @Test
    @DisplayName("RecentMistakesFirstSorter puts wrong cards first")
    public void testRecentMistakesFirstSorter() {
        FlashCard card1 = new FlashCard("Q1", "A1");
        FlashCard card2 = new FlashCard("Q2", "A2");
        FlashCard card3 = new FlashCard("Q3", "A3");

        card1.recordResult(true);   // correct
        card2.recordResult(false);  // wrong - should come first
        card3.recordResult(true);   // correct

        RecentMistakesFirstSorter sorter = new RecentMistakesFirstSorter();
        List<FlashCard> sorted = sorter.organize(Arrays.asList(card1, card2, card3));

        assertEquals(card2, sorted.get(0), "Wrong card should be first");
        // card1 and card3 should follow, preserving their relative order
        assertEquals(card1, sorted.get(1));
        assertEquals(card3, sorted.get(2));
    }

    @Test
    @DisplayName("WorstFirstSorter puts most-missed cards first")
    public void testWorstFirstSorter() {
        FlashCard card1 = new FlashCard("Q1", "A1");
        FlashCard card2 = new FlashCard("Q2", "A2");

        // card1 wrong 0 times, card2 wrong 3 times
        card2.recordResult(false);
        card2.recordResult(false);
        card2.recordResult(false);

        WorstFirstSorter sorter = new WorstFirstSorter();
        List<FlashCard> sorted = sorter.organize(Arrays.asList(card1, card2));

        assertEquals(card2, sorted.get(0), "Most missed card should be first");
    }

    @Test
    @DisplayName("AchievementTracker awards CORRECT achievement")
    public void testCorrectAchievement() {
        FlashCard card = new FlashCard("Q", "A");
        card.recordResult(true);

        AchievementTracker tracker = new AchievementTracker();
        List<Achievement> achievements = tracker.checkRoundAchievements(
            Arrays.asList(card), 3000L
        );

        assertTrue(achievements.contains(Achievement.CORRECT));
    }

    @Test
    @DisplayName("AchievementTracker awards REPEAT achievement after 5+ answers")
    public void testRepeatAchievement() {
        FlashCard card = new FlashCard("Q", "A");
        for (int i = 0; i < 6; i++) {
            card.recordResult(i % 2 == 0);
        }

        AchievementTracker tracker = new AchievementTracker();
        List<Achievement> achievements = tracker.checkCardAchievements(card);

        assertTrue(achievements.contains(Achievement.REPEAT));
    }

    @Test
    @DisplayName("AchievementTracker awards CONFIDENT achievement after 3 correct")
    public void testConfidentAchievement() {
        FlashCard card = new FlashCard("Q", "A");
        card.recordResult(true);
        card.recordResult(true);
        card.recordResult(true);

        AchievementTracker tracker = new AchievementTracker();
        List<Achievement> achievements = tracker.checkCardAchievements(card);

        assertTrue(achievements.contains(Achievement.CONFIDENT));
    }

    @Test
    @DisplayName("AchievementTracker awards SPEED achievement under 5s average")
    public void testSpeedAchievement() {
        FlashCard card = new FlashCard("Q", "A");
        card.recordResult(true);

        AchievementTracker tracker = new AchievementTracker();
        // 1 card, 2000ms = 2 seconds average -> under 5s
        List<Achievement> achievements = tracker.checkRoundAchievements(
            Arrays.asList(card), 2000L
        );

        assertTrue(achievements.contains(Achievement.SPEED));
    }

    @Test
    @DisplayName("AchievementTracker does not award CORRECT when a card is wrong")
    public void testNoCorrectAchievementWhenWrong() {
        FlashCard card1 = new FlashCard("Q1", "A1");
        FlashCard card2 = new FlashCard("Q2", "A2");
        card1.recordResult(true);
        card2.recordResult(false);

        AchievementTracker tracker = new AchievementTracker();
        List<Achievement> achievements = tracker.checkRoundAchievements(
            Arrays.asList(card1, card2), 5000L
        );

        assertFalse(achievements.contains(Achievement.CORRECT));
    }
}
