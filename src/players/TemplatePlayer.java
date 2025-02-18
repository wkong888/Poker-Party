package players;

import game.Player;

public class TemplatePlayer extends Player {

    public TemplatePlayer(String name) {
        super(name);
    }

    @Override
    public void takePlayerTurn() {
        if(shouldAllIn()) {
            allIn();
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
            fold();
        }

    }

    @Override
    public boolean shouldFold() {
        return true;
    }

    @Override
    public boolean shouldCheck() {
//        if(!getGameState().isActiveBet()){
//            return true;
//        }
        return false;
    }

    @Override
    public boolean shouldCall() {
        return false;
    }

    @Override
    public boolean shouldRaise() {
        if(getGameState().isActiveBet()){
            if(getBank()>(getGameState().getTableBet()*2)){
                return true;
            }

        }
        return false;
    }

    @Override
    public boolean shouldAllIn() {
        return false;
    }
}