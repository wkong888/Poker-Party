package game;

// The Evaluation class represents the result of evaluating a player's hand against the rules of the game.
public class Evaluation {

    private Player player; // The player associated with this evaluation.
    private HandRanks handRank; // The rank of the hand after evaluation.

    // Constructor to initialize an Evaluation object with a player and their hand's rank.
    public Evaluation(Player player, HandRanks handRank) {
        this.player = player;
        this.handRank = handRank;
    }

    // Returns the player associated with this evaluation.
    public Player getPlayer() {
        return player;
    }

    // Returns the rank of the hand evaluated.
    public HandRanks getHandRank() {
        return handRank;
    }
}
