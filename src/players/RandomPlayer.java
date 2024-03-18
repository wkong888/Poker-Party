package players;

import game.Player;

public class RandomPlayer extends Player {
    double random; // A random number used to make decisions.

    /**
     * Constructor for a RandomPlayer object.
     * @param name The name of the player.
     */
    public RandomPlayer(String name) {
        super(name); // Call the constructor of the superclass (Player).
    }

    /**
     * Method that defines the logic for taking a player's turn.
     * The player makes decisions randomly based on the current game state.
     */
    @Override
    protected void takePlayerTurn() {
        random = Math.random(); // Generate a new random number for each turn.
        if(shouldFold()) {
            fold(); // The player folds if the random number is greater than the stored random number.
        }
        else if(shouldCheck()) {
            check(); // The player checks if the random number is greater than the stored random number.
        }
        else if(shouldCall()) {
            call(); // The player calls if the random number is greater than the stored random number.
        }
        else if(shouldRaise()) {
            raise(getGameState().getTableMinBet()); // The player raises the minimum bet if the random number is greater than the stored random number.
        }
        else if(shouldAllIn()) {
            allIn(); // The player goes all-in if the random number is greater than the stored random number.
        }
    }

    /**
     * Method to determine if the player should fold.
     * @return True if the player should fold, false otherwise.
     */
    @Override
    protected boolean shouldFold() {
        double localRand = Math.random(); // Generate a new random number.
        return localRand > random; // The player folds if the new random number is greater than the stored random number.
    }

    /**
     * Method to determine if the player should check.
     * @return True if the player should check, false otherwise.
     */
    @Override
    protected boolean shouldCheck() {
        double localRand = Math.random(); // Generate a new random number.
        return localRand > random; // The player checks if the new random number is greater than the stored random number.
    }

    /**
     * Method to determine if the player should call.
     * @return True if the player should call, false otherwise.
     */
    @Override
    protected boolean shouldCall() {
        double localRand = Math.random(); // Generate a new random number.
        return localRand > random; // The player calls if the new random number is greater than the stored random number.
    }

    /**
     * Method to determine if the player should raise.
     * @return True if the player should raise, false otherwise.
     */
    @Override
    protected boolean shouldRaise() {
        double localRand = Math.random(); // Generate a new random number.
        return localRand > random; // The player raises if the new random number is greater than the stored random number.
    }

    /**
     * Method to determine if the player should go all-in.
     * @return True if the player should go all-in, false otherwise.
     */
    @Override
    protected boolean shouldAllIn() {
        double localRand = Math.random(); // Generate a new random number.
        return localRand > random; // The player goes all-in if the new random number is greater than the stored random number.
    }
}
