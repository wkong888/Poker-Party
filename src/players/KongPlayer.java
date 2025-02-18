package players;

import game.HandRanks;
import game.Player;

public class KongPlayer extends Player {

    /**
     * Constructs a new Player with the specified name.
     * Initializes the player's hand, bank balance, and various status flags.
     *
     * @param name The name of the player.
     */
    public KongPlayer(String name) {
        super(name);
    }

    @Override
    protected void takePlayerTurn() {
        if (shouldFold()) {
            fold();
        } else if (shouldCheck()) {
            check();
        } else if (shouldCall()) {
            call();
        } else if (shouldRaise()) {
            raise(getGameState().getTableMinBet() * 2); // Aggressively raise double the minimum bet
        } else if (shouldAllIn()) {
            allIn();
        }
    }

    @Override
    protected boolean shouldFold() {
        // Only fold if the bet is extremely high and hand is weak
        return getGameState().getTableBet() > getBank() * 0.5 && evaluatePlayerHand().getValue() <= HandRanks.HIGH_CARD.getValue();
    }

    @Override
    protected boolean shouldCheck() {
        // Check only if there's no active bet
        return !getGameState().isActiveBet();
    }

    @Override
    protected boolean shouldCall() {
        // Call aggressively if the bet is reasonable compared to bank
        return getGameState().isActiveBet() && getGameState().getTableBet() < getBank() * 0.2;
    }

    @Override
    protected boolean shouldRaise() {
        // Raise aggressively if hand is above high card or if bet is manageable
        boolean hasStrongHand = evaluatePlayerHand().getValue() >= HandRanks.PAIR.getValue();
        boolean betIsSmallPercentageOfBank = getGameState().getTableBet() < getBank() * 0.25;
        return hasStrongHand || betIsSmallPercentageOfBank;
    }

    @Override
    protected boolean shouldAllIn() {
        // Go all-in if hand is very strong or if aggressive play is required
        return evaluatePlayerHand().getValue() >= HandRanks.STRAIGHT.getValue() || getBank() < getGameState().getTableBet() * 3;
    }
}
