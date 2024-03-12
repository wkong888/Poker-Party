package game;

import players.ManualPlayer;
import players.RandomPlayer;
import players.SimpleNPCPlayer;
import players.TemplatePlayer;

import java.util.*;

public class GameEngine {
    private int gameSpeed;

    private Deck deck;

    private List<Card> tableCards;

    private List<Player> listPlayersRemainingGame;
    private List<Player> listPlayersRemainingRound;
    private List<Player> listPlayersWinner;
    private List<Map<String, Integer>> listPlayersNameBankMap;

    private int tableAnteCountdown;
    private int tableAnteSmall;
    private int tableAnteBig;
    private int tablePot;
    private int tableBet;
    private int tableMinBet;
    private boolean activeBet;
    private int activeBetNumberOfPlayersLeft;

    private int numTotalGames;
    private int numRoundStage;

    private Player dealer;
    private Player small;
    private Player big;

    private int dealerIndex;

    private GameState state; 

    public GameEngine() {
        //0 (fullSpeed) to Integer.MAX_VALUE (eternity)
        gameSpeed = 0;

        deck = new Deck();
        deck.shuffle();

        tableCards = new ArrayList<>();

        listPlayersRemainingGame = new ArrayList<>();

        // create players here

        //listPlayersRemainingGame.add(new ManualPlayer("Manual Player"));
        addSimpleNPCs(0);
        addRandomNPCs(1000);
        addTempPlayers(0);
        Collections.shuffle(listPlayersRemainingGame);


        listPlayersRemainingRound = new ArrayList<>(listPlayersRemainingGame);
        listPlayersWinner = new ArrayList<>();
        listPlayersNameBankMap = new ArrayList<>();

        tableAnteCountdown = listPlayersRemainingRound.size();
        tableAnteSmall = 2;
        tableAnteBig = tableAnteSmall * 2;
        tablePot = 0;
        tableBet = 0;
        tableMinBet = tableAnteBig;
        activeBet = false;
        activeBetNumberOfPlayersLeft = 0;

        numTotalGames = 0;
        numRoundStage = 0;

        dealerIndex = 0;

        state = new GameState(tableCards, listPlayersNameBankMap, deck.getDeckSize(), listPlayersRemainingGame.size(), listPlayersRemainingRound.size(), tableAnteCountdown, tableAnteSmall, tableAnteBig, tablePot, tableBet, tableMinBet, activeBet, activeBetNumberOfPlayersLeft, numTotalGames, numRoundStage, dealer, small, big, dealerIndex);
        for(Player tempPlayer: listPlayersRemainingRound) {
            tempPlayer.updateGameState(state);  
        }
    }

    public void start() {
        printWelcome();

        while(listPlayersRemainingGame.size() > 1) {
            //pre-flop
            numRoundStage = 0;
            rotateDealer();
            dealHoleCards(2);
            printGameDetails();
            betPhase();

            //flop
            numRoundStage++;
            dealTableCards(3);
            printGameDetails();
            betPhase();

            //turn
            numRoundStage++;
            dealTableCards(1);
            printGameDetails();
            betPhase();

            //river
            numRoundStage++;
            dealTableCards(1);
            printGameDetails();
            betPhase();
            showdownPhase();

            newHandReset();
        }
    }

    private void betPhase() {
        boolean phaseComplete = false;
        boolean activeBet = false;

        int playersWithAction = 0;

        for(Player tempPlayer: listPlayersRemainingRound) {
            if(!tempPlayer.isFold() && !tempPlayer.isAllIn()) {
                playersWithAction++;
            }
        }

        System.out.println("Current Pot: $" + tablePot + ", Current Bet: $" + tableBet);
        sleep(gameSpeed);
        System.out.println();

        while(!phaseComplete) {
            List<Player> listPlayersToRemoveFromRound = new ArrayList<>();

            for(Player tempPlayer: new ArrayList<>(listPlayersRemainingRound)) {

                if(tempPlayer.isFold() && tempPlayer.isAllIn()) continue;

                if(listPlayersRemainingRound.size()-1 == listPlayersToRemoveFromRound.size()) return;

                System.out.println("Action: " + tempPlayer.getName() + ", Bank: $" + tempPlayer.getBank());
                for(Card tempCard: tempPlayer.getHandCards()) {
                    System.out.print(tempCard.toString() + " ");
                }
                System.out.println(tempPlayer.evaluatePlayerHand());
                sleep(gameSpeed);
                System.out.println("Current Pot: $" + tablePot + ", Current Bet: $" + tableBet);
                sleep(gameSpeed);

                updateListPlayerNameBankMap();

                PlayerActions action = tempPlayer.getPlayerAction(new GameState(tableCards, listPlayersNameBankMap, deck.getDeckSize(), listPlayersRemainingGame.size(), listPlayersRemainingRound.size(), tableAnteCountdown, tableAnteSmall, tableAnteBig, tablePot, tableBet, tableMinBet, activeBet, activeBetNumberOfPlayersLeft, numTotalGames, numRoundStage, dealer, small, big, dealerIndex));
                if(action == null) {
                    System.out.println("###NULL PLAYER ACTION RECEIVED, FORCING FOLD###");
                    action = PlayerActions.FOLD;
                }

                switch (action) {
                    case FOLD:
                        System.out.println("###FOLD###");
                        playersWithAction--;
                        listPlayersToRemoveFromRound.add(tempPlayer);

                        if(activeBetNumberOfPlayersLeft > 0){
                            activeBetNumberOfPlayersLeft--;
                        }
                        sleep(gameSpeed);
                        break;
                    case CHECK:
                        System.out.println("###CHECK###");
                        playersWithAction--;

                        if(activeBetNumberOfPlayersLeft > 0){
                            activeBetNumberOfPlayersLeft--;
                        }
                        sleep(gameSpeed);
                        break;
                    case CALL:
                        System.out.println("###CALL###");
                        playersWithAction--;

                        int amountCallToPullFromPlayer = tempPlayer.adjustPlayerBank(-tempPlayer.getBet());
                        tablePot += -amountCallToPullFromPlayer;

                        if(activeBetNumberOfPlayersLeft > 0){
                            activeBetNumberOfPlayersLeft--;
                        }
                        System.out.println("bet: $" + tempPlayer.getBet() + ", new bank: $" + tempPlayer.getBank());
                        System.out.println("new pot: $" + tablePot);
                        sleep(gameSpeed);
                        break;
                    case RAISE:
                        System.out.println("###RAISE###");
                        playersWithAction--;

                        activeBet = true;

                        int amountRaiseToPullFromPlayer = tempPlayer.adjustPlayerBank(-tempPlayer.getBet());
                        tableBet = tempPlayer.getBet();
                        tablePot += -amountRaiseToPullFromPlayer;

                        playersWithAction += listPlayersRemainingRound.size()-1;
                        activeBetNumberOfPlayersLeft = listPlayersRemainingRound.size()-1;

                        sleep(gameSpeed);
                        break;
                    case ALL_IN:
                        System.out.println("###ALL IN###");
                        playersWithAction--;
                        listPlayersToRemoveFromRound.add(tempPlayer);

                        activeBet = true;

                        int amountAllInToPullFromPlayer = tempPlayer.adjustPlayerBank(-tempPlayer.getBet());
                        tableBet = tempPlayer.getBet();
                        tablePot += -amountAllInToPullFromPlayer;


                        playersWithAction += listPlayersRemainingRound.size()-1;
                        activeBetNumberOfPlayersLeft = listPlayersRemainingRound.size()-1;

                        if(activeBetNumberOfPlayersLeft > 0){
                            activeBetNumberOfPlayersLeft--;
                        }
                        System.out.println("bet: $" + tempPlayer.getBet() + ", new bank: $" + tempPlayer.getBank());
                        System.out.println("new pot: $" + tablePot);
                        sleep(gameSpeed);
                        break;
                }
                updateListPlayerNameBankMap();
                System.out.println();
                sleep(gameSpeed);

                if(activeBetNumberOfPlayersLeft == 0) activeBet = false;

                if(playersWithAction <= 0  & !activeBet) {
                    phaseComplete = true;
                    break;
                }
            }
            listPlayersRemainingRound.removeAll(listPlayersToRemoveFromRound);
        }
    }

    private void newHandReset() {
        removeBankruptPlayers();

        deck = new Deck();
        deck.shuffle();

        tableCards.clear();
        tablePot = 0;
        tableBet = 0;

        numTotalGames++;
        listPlayersRemainingRound.clear();

        listPlayersRemainingGame.add(listPlayersRemainingGame.remove(0));

        for(Player tempPlayer: listPlayersRemainingGame) {
            listPlayersRemainingRound.add(tempPlayer);
        }
        if(listPlayersRemainingRound.size() == 1) {
            System.out.println("FINAL WINNER: " + listPlayersRemainingRound.get(0).getName() + ", BANK: $" + listPlayersRemainingRound.get(0).getBank());
            System.exit(0);
        }

        for(Player tempPlayer: listPlayersRemainingRound) {
            tempPlayer.newHandReset();
        }

        listPlayersNameBankMap.clear();
        for(Player tempPlayer: listPlayersRemainingRound) {
            Map<String, Integer> tempMap = new HashMap<>();
            tempMap.put(tempPlayer.getName(), tempPlayer.getBank());
            listPlayersNameBankMap.add(tempMap);
        }
    }

    private void updateListPlayerNameBankMap() {
        listPlayersNameBankMap = new ArrayList<>();
        for(Player tempPlayer: listPlayersRemainingRound) {
            Map<String, Integer> tempMap = new HashMap<>();
            tempMap.put(tempPlayer.getName(), tempPlayer.getBank());
            listPlayersNameBankMap.add(tempMap);
        }
    }

    private void removeBankruptPlayers() {

        for(Player tempPlayer: new ArrayList<>(listPlayersRemainingGame)) {
            if(tempPlayer.getBank() <= 0) {
                if(listPlayersRemainingGame.size() > 1) {
                    listPlayersRemainingGame.remove(tempPlayer);
                }
            }
        }


//        for(Player tempPlayer: new ArrayList<>(listPlayersRemainingGame)) {
//            if(tempPlayer.getBank() <= 0) {
//                listPlayersRemainingGame.remove(tempPlayer);
//            }
//        }
    }

    private void showdownPhase() {
        Evaluator evaluator = new Evaluator(listPlayersRemainingRound, tableCards);
        listPlayersWinner = evaluator.findWinners();

        for(Card tempCard: tableCards) {
            System.out.print(tempCard.toString() + " ");
        }
        sleep(gameSpeed);
        System.out.println("\n");

        if (listPlayersWinner.size() == 1) {
            listPlayersWinner.get(0).adjustPlayerBank(tablePot);
            System.out.println("WINNER: " + listPlayersWinner.get(0).getName());
            sleep(gameSpeed);
            for(Card tempCard: listPlayersWinner.get(0).getHandCards()) {
                System.out.print(tempCard.toString() + " ");
            }
            sleep(gameSpeed);
            System.out.println("New Bank: $" + listPlayersWinner.get(0).getBank() + " (+$" + tablePot + ")");
            sleep(gameSpeed);
            System.out.println();
        } else {
            int splitPotAmount = tablePot / listPlayersWinner.size();
            for (Player winner : listPlayersWinner) {
                winner.adjustPlayerBank(splitPotAmount);
                System.out.println("SPLIT POT: $" + winner.getName());
                System.out.println("New Bank: $" + winner.getBank() + " (+$" + splitPotAmount + ")");
                sleep(gameSpeed);
                System.out.println();
            }
        }
    }

    private void dealTableCards(int num) {
        for(int i = 0; i < num; i++) {
            tableCards.add(deck.topCard());
        }
    }

    private void dealHoleCards(int num) {
        for(int i = 0; i < num; i++) {
            for(Player tempPlayer: listPlayersRemainingGame) {
                tempPlayer.addCardToPlayerHand(deck.topCard());
            }
        }
    }

    private void rotateDealer() {
        dealer = listPlayersRemainingGame.get(0);
        if(listPlayersRemainingGame.size() <= 2) {
            small = listPlayersRemainingGame.get(0);
            big = listPlayersRemainingGame.get(1);
        } else {
            small = listPlayersRemainingGame.get(1);
            big = listPlayersRemainingGame.get(2);
        }
        ante();
    }

    private void ante() {
        if(small.getBank() < tableAnteSmall) {
            int smallAdjust = small.adjustPlayerBank(-small.getBank());
            tablePot += -smallAdjust;
        } else {
            int tableSmall = small.adjustPlayerBank(-tableAnteSmall);
            tablePot += -tableSmall;
        }
        if(big.getBank() < tableAnteBig) {
            int bigAdjust = big.adjustPlayerBank(-big.getBank());
            tablePot += -bigAdjust;
        } else {
            int tableBig = big.adjustPlayerBank(-tableAnteBig);
            tablePot += -tableBig;
        }

        tableAnteCountdown--;
        if(tableAnteCountdown == 0) {
            tableAnteSmall += tableAnteSmall;
            tableAnteBig = tableAnteSmall*2;
            tableMinBet = tableAnteBig;
            tableAnteCountdown = listPlayersRemainingRound.size();
        }
    }

    private void printWelcome() {
        System.out.println("Welcome to the Poker Party");
        sleep(gameSpeed);
        System.out.println("House Rules:");
        sleep(gameSpeed);
        System.out.println("Dealer rotates, but first action is on the dealer");
        sleep(gameSpeed);
        System.out.println("Ante increases after full dealer rotation");
        sleep(gameSpeed);
        System.out.println("Minimum bet is the big blind");
        sleep(gameSpeed);
        System.out.println();
    }

    private void printGameDetails() {
        for(int i = 0; i < 20; i++) {
            System.out.print("~");
        }
        System.out.println();

        if(numRoundStage == 0) {
            System.out.println("GAME #" + numTotalGames);
            sleep(gameSpeed);
            System.out.println("PRE-FLOP");
            sleep(gameSpeed);
            System.out.println("Dealer: " + dealer.getName());
            sleep(gameSpeed);
            System.out.println("Small: " + small.getName() + ", $" + tableAnteSmall);
            sleep(gameSpeed);
            System.out.println("Big: " + big.getName() + ", $" + tableAnteBig);
            sleep(gameSpeed);
            System.out.println();
        }
        else if(numRoundStage == 1) {
            System.out.println("FLOP");
            sleep(gameSpeed);
        }
        else if(numRoundStage == 2) {
            System.out.println("TURN");
            sleep(gameSpeed);
        }
        else if(numRoundStage == 3) {
            System.out.println("RIVER");
            sleep(gameSpeed);
        }

        System.out.println("Table Cards:");
        if(tableCards.size() == 0) {
            System.out.print("[  ] [  ]");
            sleep(gameSpeed);
        }
        for(Card tempCard: tableCards) {
            System.out.print(tempCard.toString() + " ");
        }
        sleep(gameSpeed);
        System.out.println("\n");

        for(Player tempPlayer: listPlayersRemainingRound) {
            if(!tempPlayer.isFold()) {
                System.out.println("Player: " + tempPlayer.getName() + ", Bank: $" + tempPlayer.getBank());
                for(Card tempCard: tempPlayer.getHandCards()) {
                    System.out.print(tempCard.toString() + " ");
                }
                System.out.println(tempPlayer.evaluatePlayerHand());
                sleep(gameSpeed);
                System.out.println();
            }
        }
        System.out.println();
    }

    void setTableBet(int tableBet) {
        this.tableBet = tableBet;
    }

    private void addTempPlayers(int numPlayers) {
        for(int i = 0; i < numPlayers; i++) {
            String tempName = "TemplatePlayer#" + i;
            listPlayersRemainingGame.add(new TemplatePlayer(tempName));
        }
    }

    private void addSimpleNPCs(int numPlayers) {
        for(int i = 0; i < numPlayers; i++) {
            String tempName = "SimpleNPCPlayer#" + i;
            listPlayersRemainingGame.add(new SimpleNPCPlayer(tempName));
        }
    }

    private void addRandomNPCs(int numPlayers) {
        for(int i = 0; i < numPlayers; i++) {
            String tempName = "RandomPlayer#" + i;
            listPlayersRemainingGame.add(new RandomPlayer(tempName));
        }
    }

    private void sleep(int ms) {
        try {
            Thread.sleep(ms);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
            System.out.println("Thread was interrupted, Failed to complete operation");
        }
    }
}


