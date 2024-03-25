package players;

import game.Card;
import game.Deck;
import game.Player;

import java.util.ArrayList;
import java.util.List;

public class AdamsPlayer extends Player {

    List<Card> playedCards;
    List<Card> deckedCards;
    int numCardsPlayed;

    public AdamsPlayer(String name) {
        super(name);

        playedCards = new ArrayList<>();
        deckedCards = new ArrayList<>();
        numCardsPlayed = 0;

        for (int suit = 0; suit <= 3; suit++) {
            // 0-10 as expected, 11 = Jack, 12 = Queen, 13 = King, 14 = Ace
            // Ace *should* be treated as either 1 or 11
            for (int value = 2; value <= 14; value++) {
                deckedCards.add(new Card(suit, value)); // Add a new card to the deck for each suit and value.
            }
        }

    }

    @Override
    public void takePlayerTurn() {




        for(Card playedCard: playedCards) {
            System.out.print(playedCard.toString()+ " ");
        }
        System.out.println();

        for(Card deckCard: deckedCards) {
            System.out.print(deckCard.toString() + " ");
        }
        System.out.println();

        if(shouldFold()) {
            fold();
        }
        else if(shouldCheck()) {
            check();
        }
        else if(shouldCall()) {
            call();
        }
        else if(shouldRaise()) {
            raise(getGameState().getTableMinBet());
        }
        else if(shouldAllIn()) {
            allIn();
        }

    }

    @Override
    public boolean shouldFold() {
        int betOnTable = getGameState().getTableBet();

        int myCurrentBank = getBank();
        int currentGameStage = getGameState().getNumRoundStage();

        if(betOnTable > myCurrentBank * .25 || currentGameStage > 1) {
            return true;
        }

        return false;
    }

    @Override
    public boolean shouldCheck() {
        if(!isBetActive()) {
            return true;
        }
        return false;

    }

    @Override
    public boolean shouldCall() {
        int betOnTable = getGameState().getTableBet();
        int myCurrentBank = getBank();

        if(isBetActive() && betOnTable < myCurrentBank * .1) {
            return true;
        }
        return false;
    }

    @Override
    public boolean shouldRaise() {
        int handValue = evaluatePlayerHand().getValue();
        if(handValue >= 2) {

        }

        int potOnTable = getGameState().getTablePot();
        int betOnTable = getGameState().getTableBet();

        int myCurrentBank = getBank();
        int currentGameStage = getGameState().getNumRoundStage();



        return false;
    }

    @Override
    public boolean shouldAllIn() {
        return false;
    }
}