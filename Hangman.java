// import necessary classes for file i/o, collections, timing, and random number generation
import java.util.ArrayList;
import java.security.SecureRandom;
import java.util.Scanner;
import java.io.File;
import java.io.FileNotFoundException;

/*
 * Suraj Narra
 * 12/14/2025
 * this is a hangman game that allows the user to guess letters to complete the word.
 * the user chooses a category and difficulty level before the game begins.
 * the user has limited chances based on hangman graphics and a time limit.
 * the game tracks wins, losses, score, and allows the user to play again.
 */

public class Hangman {
    public static void main(String[] args) {

        // scanner for user input
        Scanner input = new Scanner(System.in);

        // tracks overall game statistics
        int gamesPlayed = 0;
        int wins = 0;
        int losses = 0;
        int totalScore = 0;

        // loop that allows the user to play multiple games
        boolean playAgain = true;

        while (playAgain) {

            gamesPlayed++;

            // ask the user for category choice
            System.out.println("WELCOME TO HANGMAN!");
            System.out.println("Choose a category: color, food, animal, or random");
            String category = input.nextLine().trim().toLowerCase();

            // ask the user for difficulty level
            System.out.println("Choose difficulty: easy, medium, or hard");
            String difficulty = input.nextLine().trim().toLowerCase();

            // list that will store words from the selected file
            ArrayList<String> wordList = new ArrayList<>();

            // determines which file to load based on category
            String fileName = "";

            if (category.equals("food")) {
                fileName = "wordListFood.txt";
            } 
            else if (category.equals("animals")) {
                fileName = "wordListAnimal.txt";
            } 
            else if (category.equals("random")) {
                fileName = "wordListRandom.txt";
            } 
            else {
                fileName = "wordListColor.txt";
            }

            // attempts to load the selected word list
            try (Scanner fileScanner = new Scanner(new File(fileName))) {
                while (fileScanner.hasNextLine()) {
                    wordList.add(fileScanner.nextLine().trim());
                }
            } catch (FileNotFoundException e) {
                System.out.println("Word list file not found. exiting game.");
                break;
            }

            // determines the word range based on difficulty
            int startIndex = 0;
            int endIndex = 0;

            if (category.equals("random")) {

                // random uses groups of 4
                if (difficulty.equals("hard")) {
                    startIndex = 0;
                    endIndex = 4;
                } 
                else if (difficulty.equals("medium")) {
                    startIndex = 4;
                    endIndex = 8;
                } 
                else {
                    startIndex = 8;
                    endIndex = 12;
                }

            } 
            else {

                // other categories use groups of 3
                if (difficulty.equals("hard")) {
                    startIndex = 0;
                    endIndex = 3;
                } 
                else if (difficulty.equals("medium")) {
                    startIndex = 3;
                    endIndex = 6;
                } 
                else {
                    startIndex = 6;
                    endIndex = 9;
                }
            }

            // secure random for better word selection
            SecureRandom rand = new SecureRandom();
            String secretWord = wordList.get(startIndex + rand.nextInt(endIndex - startIndex));

            // sets time limit based on difficulty
            long timeLimit;
            if (difficulty.equals("easy")) {
                timeLimit = 180000; // 3 minutes
            } 
            else if (difficulty.equals("medium")) {
                timeLimit = 150000; // 2.5 minutes
            } 
            else {
                timeLimit = 120000; // 2 minutes
            }

            long startTime = System.currentTimeMillis();

            // displays word info
            System.out.println("Your word has " + secretWord.length() + " letters.");

            // creates display array filled with underscores
            char[] display = new char[secretWord.length()];
            for (int i = 0; i < display.length; i++) {
                display[i] = '_';
            }

            // hangman graphics representing each incorrect guess
            String[] hangmanGraphics = {
                "",
                "  +---+\n      |\n      |\n      |\n      |\n==========",
                "  +---+\n  |   |\n      |\n      |\n      |\n==========",
                "  +---+\n  |   |\n  O   |\n      |\n      |\n==========",
                "  +---+\n  |   |\n  O   |\n  |   |\n      |\n==========",
                "  +---+\n  |   |\n  O   |\n /|   |\n      |\n==========",
                "  +---+\n  |   |\n  O   |\n /|\\  |\n      |\n==========",
                "  +---+\n  |   |\n  O   |\n /|\\  |\n /    |\n==========",
                "  +---+\n  |   |\n  O   |\n /|\\  |\n / \\  |\n=========="
            };

            // initializes game variables
            ArrayList<Character> guessedLetters = new ArrayList<>();
            int hangmanProgress = 0;
            int wrongStreak = 0;
            int score = 0; // score for this game

            // assigns base points based on difficulty
            int baseScore = 0;
            if (difficulty.equals("easy")) {
                baseScore = 50;
            }
            else if (difficulty.equals("medium")) {
                baseScore = 100;
            } 
            else {
                baseScore = 150; // hard
            }
            score += baseScore;

            // main game loop
            while (hangmanProgress < hangmanGraphics.length - 1) {

                // checks time limit and displays time left
                long elapsed = System.currentTimeMillis() - startTime;
                long timeLeft = timeLimit - elapsed;

                if (timeLeft <= 0) {
                    System.out.println("Time's up! You ran out of time.");
                    break;
                }

                System.out.println("Time left: " + (timeLeft / 1000) + " seconds");

                // displays remaining attempts
                System.out.println("Tries left: " + (hangmanGraphics.length - 1 - hangmanProgress));

                System.out.print("Guess a letter: ");
                String inputLine = input.nextLine().trim().toLowerCase();

                if (inputLine.isEmpty() || inputLine.length() > 1) {
                    System.out.println("Please enter one letter only.");
                    continue;
                }

                char guess = inputLine.charAt(0);

                if (guessedLetters.contains(guess)) {
                    System.out.println("You already guessed that letter.");
                    continue;
                }

                guessedLetters.add(guess);
                boolean correctGuess = false;

                // checks if guessed letter exists in the word
                for (int i = 0; i < secretWord.length(); i++) {
                    if (secretWord.charAt(i) == guess) {
                        display[i] = guess;
                        correctGuess = true;
                    }
                }

                // handles incorrect guesses
                if (!correctGuess) {
                    hangmanProgress++;
                    wrongStreak++;
                    score -= 10; // deduct points for wrong guess
                } 
                else {
                    wrongStreak = 0;
                }

                // always prints the current hangman graphic
                System.out.println(hangmanGraphics[hangmanProgress]);

                // offers hint after 3 incorrect guesses in a row
                if (wrongStreak == 3) {
                    System.out.print("Do you want a hint? (yes/no): ");
                    String hintChoice = input.nextLine().trim().toLowerCase();

                    if (hintChoice.equals("yes")) {
                        ArrayList<Character> hiddenLetters = new ArrayList<>();
                        for (int i = 0; i < secretWord.length(); i++) {
                            if (display[i] == '_') {
                                hiddenLetters.add(secretWord.charAt(i));
                            }
                        }

                        if (!hiddenLetters.isEmpty()) {
                            char hintLetter = hiddenLetters.get(rand.nextInt(hiddenLetters.size()));
                            guessedLetters.add(hintLetter);

                            for (int i = 0; i < secretWord.length(); i++) {
                                if (secretWord.charAt(i) == hintLetter) {
                                    display[i] = hintLetter;
                                }
                            }

                            System.out.println("Hint letter revealed!");
                            score -= 20; // deduct points for using hint
                        }
                    }
                    wrongStreak = 0;
                }

                // displays word progress
                System.out.print("Word: ");
                for (char c : display) {
                    System.out.print(c + " ");
                }
                System.out.println();

                // prints all guessed letters
                System.out.print("Guessed letters: ");
                for (int i = 0; i < guessedLetters.size(); i++) {
                    System.out.print(guessedLetters.get(i));
                    if (i < guessedLetters.size() - 1) {
                        System.out.print(", ");
                    }
                }
                System.out.println();

                // checks if word is fully guessed
                boolean done = true;
                for (char c : display) {
                    if (c == '_') {
                        done = false;
                        break;
                    }
                }

                if (done) {
                    // bonus points based on remaining time
                    long elapsedTime = System.currentTimeMillis() - startTime;
                    long timeBonus = (timeLimit - elapsedTime) / 1000;
                    score += timeBonus;

                    System.out.println("\nYOU GUESSED THE WORD!!!!!!!");
                    System.out.println("\nYour score for this game: " + score);
                    wins++;
                    break;
                }
            }

            // handles loss condition
            if (hangmanProgress >= hangmanGraphics.length - 1) {
                System.out.println("Game over! The word was: " + secretWord);
                System.out.println("Your score for this game: " + score);
                losses++;
            }

            // updates total cumulative score
            totalScore += score;

            // displays statistics
            System.out.println("Games played: " + gamesPlayed);
            System.out.println("Wins: " + wins);
            System.out.println("Losses: " + losses);
            System.out.println("Total cumulative score: " + totalScore + "\n");

            // ask user if they want to play again
            System.out.print("Play again? (yes/no): ");
            playAgain = input.nextLine().trim().toLowerCase().equals("yes");
        }

        // closes scanner
        input.close();
    }
}