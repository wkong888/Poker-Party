package game;

/**
 * Enum representing different hand ranks in a poker game.
 */
public enum HandRanks {
    HIGH_CARD(1), // Represents a high card hand.
    PAIR(2), // Represents a pair hand.
    TWO_PAIR(3), // Represents a two pair hand.
    THREE_OF_A_KIND(4), // Represents a three of a kind hand.
    STRAIGHT(5), // Represents a straight hand.
    FLUSH(6), // Represents a flush hand.
    FULL_HOUSE(7), // Represents a full house hand.
    FOUR_OF_A_KIND(8), // Represents a four of a kind hand.
    STRAIGHT_FLUSH(9), // Represents a straight flush hand.
    ROYAL_FLUSH(10); // Represents a royal flush hand.

    private int value;

    /**
     * Constructor for HandRanks enum.
     * @param value The numerical value associated with the hand rank.
     */
    HandRanks(int value) {
        this.value = value;
    }

    /**
     * Get the numerical value associated with the hand rank.
     * @return The numerical value of the hand rank.
     */
    public int getValue() {
        return this.value;
    }
}
