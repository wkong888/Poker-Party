package players;

import game.Player;

import java.util.Scanner;

public class ManualPlayer extends Player {
    Scanner kb; // Scanner object for reading user input.

    /**
     * Constructor for a ManualPlayer object.
     * @param name The name of the player.
     */
    public ManualPlayer(String name) {
        super(name); // Call the constructor of the superclass (Player).
        kb = new Scanner(System.in); // Initialize the Scanner object to read from the console.
    }

    /**
     * Method that defines the logic for taking a player's turn.
     * The player makes decisions based on user input from the console.
     */
    @Override
    public void takePlayerTurn() {
        String response = ""; // Initialize the response string.
        do {
            System.out.println("fold, check, call, raise, or all in? [fold/check/call/raise/all]");
            response = kb.nextLine().trim().toLowerCase(); // Read the user's response and convert it to lowercase.
        } while(!response.equals("fold") && !response.equals("check") &&!response.equals("call") && !response.equals("raise") && !response.equals("all")); // Repeat until a valid response is entered.
        switch(response) {
            case "fold":
                fold(); // The player folds if the user enters "fold".
                break;
            case "check":
                check(); // The player checks if the user enters "check".
                break;
            case "call":
                call(); // The player calls if the user enters "call".
                break;
            case "raise":
                System.out.println("amount?"); // Prompt the user to enter the raise amount.
                raise(kb.nextInt()); // The player raises the entered amount.
                kb.nextLine(); // Consume the newline character.
                break;
            case "all":
                allIn(); // The player goes all-in if the user enters "all".
                break;
        }
    }

    // The following methods always return false, indicating that the player should not perform the corresponding action.
    // These methods are overridden from the superclass (Player) and are not used in the ManualPlayer class.
    @Override
    public boolean shouldFold() {
        return false;
    }

    @Override
    public boolean shouldCheck() {
        return false;
    }

    @Override
    public boolean shouldCall() {
        return false;
    }

    @Override
    public boolean shouldRaise() {
        return false;
    }

    @Override
    public boolean shouldAllIn() {
        return false;
    }
}
