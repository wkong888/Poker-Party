package game;

import java.util.List;
import java.util.Map;

public class GameState {

    private List<Card> tableCards;
    private List<Map<String, Integer>> listPlayersNameBankMap;

    private int deckNumRemainingCards, numPlayersRemainingGame, numPlayersRemainingRound, tableAnteCountdown, tableAnteSmall, tableAnteBig, tablePot, tableBet, tableMinBet, activeBetNumberOfPlayersLeft, numTotalGames, numRoundStage, dealerIndex;

    private boolean activeBet;

    private Player dealer, small, big;

    public GameState(List<Card> tableCards, List<Map<String, Integer>> listPlayersNameBankMap, int deckNumRemainingCards, int numPlayersRemainingGame, int numPlayersRemainingRound, int tableAnteCountdown, int tableAnteSmall, int tableAnteBig, int tablePot, int tableBet, int tableMinBet, boolean activeBet, int activeBetNumberOfPlayersLeft, int numTotalGames, int numRoundStage, Player dealer, Player small, Player big, int dealerIndex) {
        this.tableCards = tableCards;
        this.listPlayersNameBankMap = listPlayersNameBankMap;

        this.deckNumRemainingCards = deckNumRemainingCards;
        this.numPlayersRemainingGame = numPlayersRemainingGame;
        this.numPlayersRemainingRound = numPlayersRemainingRound;
        this.tableAnteCountdown = tableAnteCountdown;
        this.tableAnteSmall = tableAnteSmall;
        this.tableAnteBig = tableAnteBig;
        this.tablePot = tablePot;
        this.tableBet = tableBet;
        this.tableMinBet = tableMinBet;
        this.activeBetNumberOfPlayersLeft = activeBetNumberOfPlayersLeft;
        this.numTotalGames = numTotalGames;
        this.numRoundStage = numRoundStage;
        this.dealerIndex = dealerIndex;

        this.activeBet = activeBet;

        this.dealer = dealer;
        this.small = small;
        this.big = big;
    }

    public List<Card> getTableCards() {
        return tableCards;
    }

    public List<Map<String, Integer>> getListPlayersNameBankMap() {
        return listPlayersNameBankMap;
    }

    public int getDeckNumRemainingCards() {
        return deckNumRemainingCards;
    }

    public int getNumPlayersRemainingGame() {
        return numPlayersRemainingGame;
    }

    public int getNumPlayersRemainingRound() {
        return numPlayersRemainingRound;
    }

    public int getTableAnteCountdown() {
        return tableAnteCountdown;
    }

    public int getTableAnteSmall() {
        return tableAnteSmall;
    }

    public int getTableAnteBig() {
        return tableAnteBig;
    }

    public int getTablePot() {
        return tablePot;
    }

    public int getTableBet() {
        return tableBet;
    }

    public int getTableMinBet() {
        return tableMinBet;
    }

    public boolean isActiveBet() {
        return activeBet;
    }

    public int getNumTotalGames() {
        return numTotalGames;
    }

    public int getNumRoundStage() {
        return numRoundStage;
    }

    public int getDealerIndex() {
        return dealerIndex;
    }

    public String getDealer() {
        return dealer.getName();
    }

    public String getSmall() {
        return small.getName();
    }

    public String getBig() {
        return big.getName();
    }
}
