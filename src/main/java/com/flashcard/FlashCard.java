package com.flashcard;

/**
 * Represents a single flashcard with a question and answer.
 */
public class FlashCard {

    private final String question;
    private final String answer;
    private int timesAnswered;
    private int timesCorrect;
    private boolean lastAnswerCorrect;

    /**
     * Constructs a FlashCard with the given question and answer.
     *
     * @param question the question text
     * @param answer   the correct answer text
     */
    public FlashCard(String question, String answer) {
        this.question = question;
        this.answer = answer;
        this.timesAnswered = 0;
        this.timesCorrect = 0;
        this.lastAnswerCorrect = true;
    }

    public String getQuestion() {
        return question;
    }

    public String getAnswer() {
        return answer;
    }

    public int getTimesAnswered() {
        return timesAnswered;
    }

    public int getTimesCorrect() {
        return timesCorrect;
    }

    public boolean isLastAnswerCorrect() {
        return lastAnswerCorrect;
    }

    /**
     * Records a result for this card.
     *
     * @param correct true if the answer was correct
     */
    public void recordResult(boolean correct) {
        timesAnswered++;
        if (correct) {
            timesCorrect++;
        }
        lastAnswerCorrect = correct;
    }

    /**
     * Returns the number of incorrect answers.
     *
     * @return wrong answer count
     */
    public int getTimesWrong() {
        return timesAnswered - timesCorrect;
    }

    @Override
    public String toString() {
        return "FlashCard{question='" + question + "', answer='" + answer + "'}";
    }
}
