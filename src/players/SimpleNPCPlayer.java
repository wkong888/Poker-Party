package players;

import game.HandRanks;
import game.Player;

public class SimpleNPCPlayer extends Player {

    public SimpleNPCPlayer(String name) {
        super(name);
    }

    @Override
    protected void takePlayerTurn() {
//        System.out.println("im in takePlayerTurn()");
        if (shouldFold()) {
//            System.out.println("im going to fold");
            fold();
        } else if (shouldCheck()) {
//            System.out.println("im going to check");
            check();
        } else if (shouldCall()) {
//            System.out.println("im going to call");
            call();
        } else if (shouldRaise()) {
//            System.out.println("im going to raise");
            raise(getGameState().getTableMinBet()); // Example: always raises the minimum bet
        } else if (shouldAllIn()) {
//            System.out.println("im going to all");
            allIn();
        }        
    }

    @Override
    protected boolean shouldFold() {
        // Example logic: fold if the bet is more than 10% of the bank
        return getGameState().getTableBet() > getBank() * 0.1;
    }

    @Override
    protected boolean shouldCheck() {
        // Example logic: check if no active bet
        return !getGameState().isActiveBet();
    }

    @Override
    protected boolean shouldCall() {
        // Call only if the bet is very small compared to the bank, making it less likely to call on larger bets
        return getGameState().isActiveBet() && getGameState().getTableBet() < getBank() * 0.03;
    }

    @Override
    protected boolean shouldRaise() {
        // Expand raise conditions to include scenarios where previously a call might have been made
        // Example: raise if the hand is decent (lower than before) or if the bet is a small percentage of the bank
        boolean hasDecentHand = evaluatePlayerHand().getValue() > HandRanks.HIGH_CARD.getValue();
        boolean betIsSmallPercentageOfBank = getGameState().getTableBet() < getBank() * 0.05;
        return hasDecentHand || betIsSmallPercentageOfBank;
    }

    @Override
    protected boolean shouldAllIn() {
        // Example logic: all in if the hand is very good
        return evaluatePlayerHand().getValue() >= HandRanks.FULL_HOUSE.getValue();
    }
}
