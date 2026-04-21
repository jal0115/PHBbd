package com.flashcard;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * Loads flashcards from a text file.
 *
 * <p>File format: Each card is defined by two lines:
 * <pre>
 * Q: What is the capital of France?
 * A: Paris
 * </pre>
 * Blank lines between cards are ignored.
 * Lines starting with '#' are treated as comments.
 * </p>
 */
public class CardLoader {

    /**
     * Loads flashcards from the specified file path.
     *
     * @param filePath path to the cards text file
     * @return list of loaded FlashCard objects
     * @throws IOException if the file cannot be read
     * @throws IllegalArgumentException if the file format is invalid
     */
    public List<FlashCard> load(String filePath) throws IOException {
        List<FlashCard> cards = new ArrayList<>();

        try (BufferedReader reader = new BufferedReader(new FileReader(filePath))) {
            String line;
            String currentQuestion = null;

            while ((line = reader.readLine()) != null) {
                line = line.trim();

                // Skip empty lines and comments
                if (line.isEmpty() || line.startsWith("#")) {
                    continue;
                }

                if (line.startsWith("Q:")) {
                    currentQuestion = line.substring(2).trim();
                } else if (line.startsWith("A:")) {
                    if (currentQuestion == null) {
                        throw new IllegalArgumentException(
                            "Answer found without a preceding question in file: " + filePath
                        );
                    }
                    String answer = line.substring(2).trim();
                    cards.add(new FlashCard(currentQuestion, answer));
                    currentQuestion = null;
                } else {
                    throw new IllegalArgumentException(
                        "Invalid line format (expected 'Q:' or 'A:'): " + line
                    );
                }
            }

            if (currentQuestion != null) {
                throw new IllegalArgumentException(
                    "Question without an answer at end of file: " + currentQuestion
                );
            }
        }

        return cards;
    }
}
