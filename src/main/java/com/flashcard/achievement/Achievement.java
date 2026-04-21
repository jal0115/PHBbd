package com.flashcard.achievement;

/**
 * Represents the different achievements a user can earn during a study session.
 */
public enum Achievement {

    /**
     * Awarded when the user answers all cards correctly in a round.
     */
    CORRECT("🏆 CORRECT", "You answered ALL cards correctly this round!"),

    /**
     * Awarded when the user answers a single card more than 5 times in total.
     */
    REPEAT("🔁 REPEAT", "You answered a single card more than 5 times!"),

    /**
     * Awarded when the user answers a single card correctly at least 3 times.
     */
    CONFIDENT("💪 CONFIDENT", "You answered a card correctly at least 3 times!"),

    /**
     * Awarded when the average answer time in a round is under 5 seconds.
     */
    SPEED("⚡ SPEED", "You answered all cards in under 5 seconds on average!");

    private final String title;
    private final String description;

    Achievement(String title, String description) {
        this.title = title;
        this.description = description;
    }

    public String getTitle() {
        return title;
    }

    public String getDescription() {
        return description;
    }
}
