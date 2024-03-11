package players;

import game.Player;

public class RandomPlayer extends Player {
    double random;

    public RandomPlayer(String name) {
        super(name);
    }

    @Override
    protected void takePlayerTurn() {
        random = Math.random();
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
    protected boolean shouldFold() {
        double localRand = Math.random();
        return localRand > random;
    }

    @Override
    protected boolean shouldCheck() {
        double localRand = Math.random();
        return localRand > random;
    }

    @Override
    protected boolean shouldCall() {
        double localRand = Math.random();
        return localRand > random;
    }

    @Override
    protected boolean shouldRaise() {
        double localRand = Math.random();
        return localRand > random;
    }

    @Override
    protected boolean shouldAllIn() {
        double localRand = Math.random();
        return localRand > random;
    }
}
