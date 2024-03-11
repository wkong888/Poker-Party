package game;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public abstract class Player {

    private String name;

    private List<Card> handCards;

    private int bank, bet;

    private boolean isDealer, isSmall, isBig, isFold, isAllIn, isBetActive;
    private boolean canCall, canCheck; 

    private GameState state;

    private PlayerActions playerAction;

    private Evaluator evaluator;

    private boolean debugInfo;

    public Player(String name) {
        this.name = name;

        this.handCards = new ArrayList<>();

        this.bank = 1000;
        this.bet = 0;

        this.isDealer = false;
        this.isSmall = false;
        this.isBig = false;
        this.isFold = false;
        this.isAllIn = false;
        this.isBetActive = false;

        this.canCall = true; 
        this.canCheck = true; 

        this.playerAction = null;

        this.evaluator = new Evaluator();

        this.debugInfo = true;
    }

    protected void fold() {
        if(state.getNumPlayersRemainingRound() == 1) {
            System.out.println("###ILLEGAL FOLD, ONE PLAYER REMAINING, FORCING CHECK###");
            check();
        } else {
            isFold = true;
            playerAction = PlayerActions.FOLD;
        }
    }

    protected void check() {
        if(state.isActiveBet() && state.getNumPlayersRemainingRound() != 1) {
            canCheck = false;
            if(canCall || canCheck) {
                System.out.println("###ILLEGAL CHECK, ACTIVE BET OR ONE PLAYER REMAINING, FORCING CALL###");
                call();
            } else {
                System.out.println("###UNABLE TO CHECK OR CALL, FORCING FOLD###");
                fold();
            }
        } else {
            playerAction = PlayerActions.CHECK;
        }
    }

    protected void call() {
        if(!state.isActiveBet() || state.getTableBet() >= bank) {
            canCall = false;
            if(canCall || canCheck) {
                System.out.println("###ILLEGAL CALL, NO ACTIVE BET, FORCING CHECK###");
                check(); 
            } else {
                System.out.println("###UNABLE TO CALL OR CHECK, FORCING FOLD###");
                fold();
            }
        } else {
            bet = state.getTableBet();
            playerAction = PlayerActions.CALL;
        }
    }

    protected void raise(int value) {
        if(value < state.getTableMinBet() || value + state.getTableBet() <= state.getTableBet()) {
            System.out.println("###ILLEGAL RAISE, VALUE UNDER ACCEPTED LIMIT, value = state.getTableMinBet()###");
            value = state.getTableMinBet();
            bet = value + state.getTableBet();
            playerAction = PlayerActions.RAISE;
        }
        else if(value > bank || value + state.getTableBet() >= bank) {
            System.out.println("###ILLEGAL RAISE, VALUE OVER ACCEPTED LIMIT, value = bank###");
            value = bank;
            bet = value + state.getTableBet();
            playerAction = PlayerActions.RAISE;
        } else {
            bet = value + state.getTableBet();
            playerAction = PlayerActions.RAISE;
        }
    }

    protected void allIn() {
        if(bank < state.getTableBet()) {
            System.out.println("###ILLEGAL ALL IN, BANK LESS THAN TABLE BET##");
        } else {
            bet = bank;
            isAllIn = true;
            playerAction = PlayerActions.ALL_IN;
        }
    }

    PlayerActions getPlayerAction(GameState state) {
        updateGameState(state);
        takePlayerTurn();
        return playerAction;
    }

    void newHandReset() {
        bet = 0;

        isDealer = false;
        isSmall = false;
        isBig = false;
        isFold = false;
        isAllIn = false;
        isBetActive = false;

        canCall = true;
        canCheck = true;

        handCards.clear();
    }

    public String getName() {
        return name;
    }

    public int getBank() {
        return bank;
    }

    public int getBet() {
        return bet;
    }

    int adjustPlayerBank(int value) {
        bank += value;
        return value;
    }

    void setDealer(boolean isDealer) {
        this.isDealer = isDealer;
    }

    void setSmall(boolean isSmall) {
        this.isSmall = isSmall;
    }

    void setBig(boolean isBig) {
        this.isBig = isBig;
    }

    void appendPlayerName(String name) {
        this.name += " " + name;
    }

    void addCardToPlayerHand(Card card) {
        handCards.add(card);
    }

    public boolean isDealer() {
        return isDealer;
    }

    public boolean isSmall() {
        return isSmall;
    }

    public boolean isBig() {
        return isBig;
    }

    public boolean isFold() {
        return isFold;
    }

    public boolean isAllIn() {
        return isAllIn;
    }

    protected boolean isBetActive() {
        return isBetActive;
    }

    protected List<Card> getHandCards() {
        return handCards;
    }

    protected GameState getGameState() {
        return state;
    }

    void updateGameState(GameState state) {
        canCall = true;
        canCheck = true;
        this.state = state;
    }

    public HandRanks evaluatePlayerHand() {
        return evaluator.evaluatePlayerHand(handCards, state.getTableCards());
    }

    protected abstract void takePlayerTurn();

    protected abstract boolean shouldFold();

    protected abstract boolean shouldCheck();

    protected abstract boolean shouldCall();

    protected abstract boolean shouldRaise();

    protected abstract boolean shouldAllIn();
     
    protected void printExampleStateInformation() {
        System.out.println("EXAMPLE GAME STATE INFORMATION");
        System.out.print("Table Cards: ");
        for(Card tempCard: getGameState().getTableCards())
            System.out.print(tempCard.toString() + " ");
        System.out.println();
        System.out.print("Player Banks: ");
        for(Map<String, Integer> tempMap: getGameState().getListPlayersNameBankMap()) {
            for(Map.Entry<String, Integer> map: tempMap.entrySet()) {
                System.out.print(map.getKey() + " ($" + map.getValue() + "), ");
            }
        }
        System.out.println();
        System.out.println("Deck Num Remaining Cards " + getGameState().getDeckNumRemainingCards());
        System.out.println("Player Num Left in Game " + getGameState().getNumPlayersRemainingGame());
        System.out.println("Player Num Left in Round " + getGameState().getNumPlayersRemainingRound());
        System.out.println("Table Rounds Until Ante Increase " + getGameState().getTableAnteCountdown());
        System.out.println("Table Small Ante $" + getGameState().getTableAnteSmall());
        System.out.println("Table Big Ante $" + getGameState().getTableAnteBig());
        System.out.println("Table Pot $" + getGameState().getTablePot());
        System.out.println("Table Bet $" + getGameState().getTableBet());
        System.out.println("Table Min Bet $" + getGameState().getTableMinBet());
        System.out.println("Table Num of Total Games Played " + getGameState().getNumTotalGames());
        System.out.println("Table Num of Current Round Stage " + getGameState().getNumRoundStage());
        System.out.println("Dealer Name " + getGameState().getDealer());
        System.out.println("Small Ante Name " + getGameState().getSmall());
        System.out.println("Big Ante Name " + getGameState().getBig());
    }
}
