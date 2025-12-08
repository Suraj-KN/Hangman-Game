import java.util.ArrayList;
import java.util.Random;
import java.util.Scanner;

public class Hangman {
    public static void main(String[] args) {

        ArrayList<String> colorNames = new ArrayList<>();
        colorNames.add("red");
        colorNames.add("gren");
        colorNames.add("blue");
        colorNames.add("yellow");

        Random rand = new Random();
        String secretWord = colorNames.get(rand.nextInt(colorNames.size()));

        System.out.println("WELCOME TO HANGMAN!");
        System.out.println("Your word has " + secretWord.length() + " letters.");
        System.out.print("Word: ");

        // Print underscores for each letter
        for (int i = 0; i < secretWord.length(); i++) {
            System.out.print("_ ");
        }

        System.out.println();

        Scanner input = new Scanner(System.in);
        System.out.print("guess a word:");
        String guess = input.nextLine().toLowerCase();

        if (guess.equals(secretWord)) {
            System.out.println("you guessed thw word!");
        } else {
            System.out.println("wrong word. it was: " + secretWord);
        }
    }
}
