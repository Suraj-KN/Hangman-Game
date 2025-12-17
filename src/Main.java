// Import necessary classes for file I/O, collections, and random number generation
import java.util.ArrayList;
import java.security.SecureRandom;
import java.util.Scanner;
import java.io.File;
import java.io.FileNotFoundException;
/*
* Suraj Narra
* 12/14/2025
* This is a hangman game that allows the user to guess letters to complete the word.
* The user has 8 chances to guess the word, represented with different hangman graphics.
* The user can also guess the word at any time. the user can also choose to play again.
 */
public class Main {
    public static void main(String[] args) {

        // gets word lists from the wordlist file
        ArrayList<String> colorNames = new ArrayList<>();
        try (Scanner fileScanner = new Scanner(new File("wordListColor.txt"))) {
            // adds each individual word of the file into the colornames lsit
            while (fileScanner.hasNextLine()) {
                colorNames.add(fileScanner.nextLine().trim());
            }
        } catch (FileNotFoundException e) {
            // Falls back to the old wordlist if the file isnt found (backup)
            System.out.println("Word list file not found. Using default colors.");
            colorNames.add("red");
            colorNames.add("blue");
            colorNames.add("yellow");
        }

        // secure random for better randomization
        SecureRandom rand = new SecureRandom();
        // picks random words
        String secretWord = colorNames.get(rand.nextInt(colorNames.size()));

        // welcome to game and game initialization
        System.out.println("WELCOME TO HANGMAN!");
        System.out.println("Your word has " + secretWord.length() + " letters.");

        // prints out underscores for each letter of the random word
        char[] display = new char[secretWord.length()];
        for (int i = 0; i < display.length; i++) {
            display[i] = '_';
        }

        // Hangman graphics array where each index represents a stage of the hangman
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
        
        // initialize game variables
        Scanner input = new Scanner(System.in);
        ArrayList<Character> guessedLetters = new ArrayList<>();
        int hangmanProgress = 0;

        try {
            // Main game loop, goes until either the player guesses the word or runs out of guesses
            while (hangmanProgress < hangmanGraphics.length - 1) {
                System.out.print("Guess a letter: ");
                String inputLine = input.nextLine().trim().toLowerCase();
                
                // makes sure the user entered something
                if (inputLine.isEmpty()) {
                    System.out.println("Please enter a letter.");
                    continue;
                }

                // only takes the first letter if the user entered more than one
                if (inputLine.length() > 1) {
                    System.out.println("Please enter only one letter.");
                    continue;
                }

                char guess = inputLine.charAt(0);

                // checks if letter was already guessed
                if (guessedLetters.contains(guess)) {
                    System.out.println("You already guessed that letter.");
                    continue;
                }
                
                // adds guess to list of guessed letters
                guessedLetters.add(guess);
                boolean correctGuess = false;

                // checks if guessed letter is in the randomly chosen word
                for (int i = 0; i < secretWord.length(); i++) {
                    if (secretWord.charAt(i) == guess) {
                        display[i] = guess;
                        correctGuess = true;
                    }
                }
                
                // if the letter wasnt correct, the next version of the hangman is printed
                if (!correctGuess) {
                    hangmanProgress++;
                    System.out.println(hangmanGraphics[hangmanProgress]);
                }

                // displays current progress
                System.out.print("Word: ");
                for (char c : display) {
                    System.out.print(c + " ");
                }
                System.out.println();

                // prints all previous guessesw
                System.out.print("Guessed letters: ");
                for (int i = 0; i < guessedLetters.size(); i++) {
                    System.out.print(guessedLetters.get(i));
                    if (i < guessedLetters.size() - 1) {
                        System.out.print(", ");
                    }
                }
                System.out.println();

                // checks if the word is fully guessed
                boolean done = true;
                for (char c : display) {
                    if (c == '_') {
                        done = false;
                        break;
                    }
                }

                // if the word is finished, the player wins and the loop ends
                if (done) {
                    System.out.println("You guessed the word! It was: " + secretWord);
                    break;
                }
            }
            
            // if the hangman is done, the player loses
            if (hangmanProgress >= hangmanGraphics.length - 1) {
                System.out.println("Game over! The word was: " + secretWord);
            }
        } finally {
            // closes the scanner
            input.close();
        }
    }
}


// next steps:
// add more word lists
// optoin to choose different difficulty(based on number of letters)
// add more features
// time limit?
// different ways to make more difficult or easier
// get wordlists from external places
// display on web page (maybe)

