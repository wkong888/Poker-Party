package players;

import game.*;

import java.util.*;

public class AdamsPlayer extends Player {

    public AdamsPlayer(String name) {
        super(name);
        // student note, you cannot use the gameState in the constructor
    }

    private void fillDeck() {
       
    }

    private float simulateOutcome() {
     
    }


    @Override
    public void takePlayerTurn() {
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
        return false;
    }

    @Override
    public boolean shouldCheck() {
        return true;

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
