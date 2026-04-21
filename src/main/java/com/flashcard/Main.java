package com.flashcard;

import com.flashcard.organizer.CardOrganizer;
import com.flashcard.organizer.RandomSorter;
import com.flashcard.organizer.RecentMistakesFirstSorter;
import com.flashcard.organizer.WorstFirstSorter;

import org.apache.commons.cli.CommandLine;
import org.apache.commons.cli.CommandLineParser;
import org.apache.commons.cli.DefaultParser;
import org.apache.commons.cli.HelpFormatter;
import org.apache.commons.cli.Option;
import org.apache.commons.cli.Options;
import org.apache.commons.cli.ParseException;

import java.io.IOException;
import java.util.List;

/**
 * Entry point for the Flashcard CLI application.
 *
 * <p>Usage: flashcard &lt;cards-file&gt; [options]</p>
 */
public class Main {

    /**
     * Main method - parses CLI arguments and starts the study session.
     *
     * @param args command line arguments
     */
    public static void main(String[] args) {
        Options options = buildOptions();

        // If --help is anywhere in args, just print help and exit
        for (String arg : args) {
            if (arg.equals("--help") || arg.equals("-h")) {
                printHelp(options);
                return;
            }
        }

        if (args.length < 1) {
            System.err.println("Error: Missing required argument <cards-file>");
            printHelp(options);
            System.exit(1);
        }

        String cardsFile = args[0];

        // Parse the remaining arguments (skip the cards-file positional arg)
        String[] remainingArgs = new String[args.length - 1];
        System.arraycopy(args, 1, remainingArgs, 0, remainingArgs.length);

        CommandLineParser parser = new DefaultParser();
        CommandLine cmd;

        try {
            cmd = parser.parse(options, remainingArgs);
        } catch (ParseException e) {
            System.err.println("Error: " + e.getMessage());
            printHelp(options);
            System.exit(1);
            return;
        }

        // -- Resolve order option --
        String orderValue = cmd.getOptionValue("order", "random");
        CardOrganizer organizer;

        switch (orderValue) {
            case "random":
                organizer = new RandomSorter();
                break;
            case "worst-first":
                organizer = new WorstFirstSorter();
                break;
            case "recent-mistakes-first":
                organizer = new RecentMistakesFirstSorter();
                break;
            default:
                System.err.println("Error: Invalid --order value '" + orderValue
                    + "'. Valid options: random, worst-first, recent-mistakes-first");
                System.exit(1);
                return;
        }

        // -- Resolve repetitions option --
        int repetitions = 1;
        if (cmd.hasOption("repetitions")) {
            String repValue = cmd.getOptionValue("repetitions");
            try {
                repetitions = Integer.parseInt(repValue);
                if (repetitions < 1) {
                    throw new NumberFormatException("Must be at least 1");
                }
            } catch (NumberFormatException e) {
                System.err.println("Error: --repetitions must be a positive integer, got '" + repValue + "'");
                System.exit(1);
                return;
            }
        }

        // -- Resolve invertCards flag --
        boolean invertCards = cmd.hasOption("invertCards");

        // -- Load cards --
        CardLoader loader = new CardLoader();
        List<FlashCard> cards;

        try {
            cards = loader.load(cardsFile);
        } catch (IOException e) {
            System.err.println("Error: Cannot read file '" + cardsFile + "': " + e.getMessage());
            System.exit(1);
            return;
        } catch (IllegalArgumentException e) {
            System.err.println("Error: Invalid file format - " + e.getMessage());
            System.exit(1);
            return;
        }

        if (cards.isEmpty()) {
            System.err.println("Error: No cards found in '" + cardsFile + "'");
            System.exit(1);
            return;
        }

        // -- Start session --
        System.out.println("****************************");
        System.out.println(" Welcome to Flashcard CLI! ");
        System.out.println("****************************");
        System.out.println("File: " + cardsFile);
        System.out.println("Order: " + orderValue);
        System.out.println("Repetitions required: " + repetitions);
        System.out.println("Inverted cards: " + invertCards);

        StudySession session = new StudySession(cards, organizer, repetitions, invertCards);
        session.start();
    }

    /**
     * Builds the CLI options definition.
     *
     * @return Options object with all supported options
     */
    private static Options buildOptions() {
        Options options = new Options();

        options.addOption(Option.builder("h")
            .longOpt("help")
            .desc("Show this help message")
            .build());

        options.addOption(Option.builder()
            .longOpt("order")
            .hasArg()
            .argName("order")
            .desc("Card ordering strategy. Options: random (default), worst-first, recent-mistakes-first")
            .build());

        options.addOption(Option.builder()
            .longOpt("repetitions")
            .hasArg()
            .argName("num")
            .desc("Number of correct answers required per card (default: 1)")
            .build());

        options.addOption(Option.builder()
            .longOpt("invertCards")
            .desc("Swap question and answer display (default: false)")
            .build());

        return options;
    }

    /**
     * Prints formatted help/usage information.
     *
     * @param options the CLI options to describe
     */
    private static void printHelp(Options options) {
        HelpFormatter formatter = new HelpFormatter();
        formatter.printHelp(
            "flashcard <cards-file> [options]",
            "\nOptions:",
            options,
            "\nCards file format:\n"
                + "  Q: Your question here\n"
                + "  A: The answer\n"
                + "  (blank lines and # comments are ignored)\n"
        );
    }
}
