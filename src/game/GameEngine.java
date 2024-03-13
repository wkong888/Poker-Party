package game;

import players.ManualPlayer;
import players.RandomPlayer;
import players.SimpleNPCPlayer;
import players.TemplatePlayer;

import java.util.*;
import java.util.stream.Collectors;

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
    private int tableRaise;
    private boolean activeBet;
    private int activeBetNumberOfPlayersLeft;

    private int numTotalGames;
    private int numRoundStage;

    private Player dealer;
    private Player small;
    private Player big;

    private int dealerIndex;

    private GameState state;

    public static int numGamesPlayed = 0;

    public GameEngine() {
        //updated 20240313 11:56
        //0 (fullSpeed) to Integer.MAX_VALUE (~24 days)
        gameSpeed = 0;

        deck = new Deck();
        deck.shuffle();

        tableCards = new ArrayList<>();

        listPlayersRemainingGame = new ArrayList<>();

        // create players here

        //listPlayersRemainingGame.add(new ManualPlayer("Manual Player"));
        addSimpleNPCs(5);
        addRandomNPCs(5);
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
        tableRaise = 0;
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
        //activeBetNumberOfPlayersLeft = 0;
        tableBet = 0;

        int playersWithAction = 0;

//        for(Player tempPlayer: listPlayersRemainingRound) {
//            if(!tempPlayer.isFold() && !tempPlayer.isAllIn()) {
//                playersWithAction++;
//            }
//        }

        if(listPlayersRemainingRound.size() == 1) {
            System.out.println("###ONE PLAYER REMAINING, ADVANCING ROUND###");
            System.out.println();
            return;
        }

//        System.out.println("betPhase() setup, playersWithAction: " + playersWithAction);
        System.out.println("Current Pot: $" + tablePot + ", Current Bet: $" + tableBet);
        sleep(gameSpeed);

//        System.out.println("entering phaseComplete: " + phaseComplete);
        while(!phaseComplete) {
//            System.out.println("starting new phaseComplete");
            List<Player> listPlayersToRemoveFromRound = new ArrayList<>();
//            System.out.println("numPlayers to remove from betting round: " + listPlayersToRemoveFromRound.size());
//            System.out.println("parsing all players in listPlayersRemainingRound\n############ ");
//            sleep(500);

            for(Player tempPlayer: listPlayersRemainingRound) {
                if(!tempPlayer.isFold() && !tempPlayer.isAllIn()) {
                    playersWithAction++;
                }
            }

            boolean allPlayersAllIn = true;
            //for all players in listPlayersRemainingRound
            //if any are !allIn, flip to false
            for(Player tempPlayer: listPlayersRemainingRound) {
                if(!tempPlayer.isAllIn()) {
                    allPlayersAllIn = false;
                }
            }

            if(allPlayersAllIn) {
                return;
            }

            for(Player tempPlayer: new ArrayList<>(listPlayersRemainingRound)) {
//                System.out.println("tempPlayer.getName(): " + tempPlayer.getName());
//                System.out.println("tempPlayer.isFold(): " + tempPlayer.isFold());
//                System.out.println("tempPlayer.isAllIn(): " + tempPlayer.isAllIn());
//                System.out.println("playersWithAction: " + playersWithAction);
//                System.out.println("listPlayersRemainingInRound.size(): " + listPlayersRemainingRound.size());
//                System.out.println("listPlayersToRemoveFromRound.size(): " + listPlayersToRemoveFromRound.size());
//                System.out.println("activeBet: " + activeBet);

                // check for fold or allin here and continue
                if(tempPlayer.isFold() || tempPlayer.isAllIn()) continue;


                // general player information
                System.out.println("Action: " + tempPlayer.getName() + ", Bank: $" + tempPlayer.getBank());
                for(Card tempCard: tempPlayer.getHandCards()) {
                    System.out.print(tempCard.toString() + " ");
                }
                System.out.println(tempPlayer.evaluatePlayerHand());
                sleep(gameSpeed);
                System.out.println("Current Pot: $" + tablePot + ", Current Bet: $" + tableBet);
                sleep(gameSpeed);


                PlayerActions action = tempPlayer.getPlayerAction(new GameState(tableCards, listPlayersNameBankMap, deck.getDeckSize(), listPlayersRemainingGame.size(), listPlayersRemainingRound.size()-listPlayersToRemoveFromRound.size(), tableAnteCountdown, tableAnteSmall, tableAnteBig, tablePot, tableBet, tableMinBet, activeBet, activeBetNumberOfPlayersLeft, numTotalGames, numRoundStage, dealer, small, big, dealerIndex));
                if(action == null) {
                    if(listPlayersRemainingRound.size()-listPlayersToRemoveFromRound.size() == 1) {
                        System.out.println("###NULL PLAYER ACTION RECEIVED, ONE PLAYER LEFT, FORCING CHECK###");
                        action = PlayerActions.CHECK;
                    } else {
                        System.out.println("###NULL PLAYER ACTION RECEIVED, FORCING FOLD###");
                        tempPlayer.setIsFold(true);
                        action = PlayerActions.FOLD;
                    }
                }

                switch(action) {
                    case FOLD:
                        System.out.println("###FOLD###");
                        playersWithAction--;
                        listPlayersToRemoveFromRound.add(tempPlayer);

                        if(activeBetNumberOfPlayersLeft > 0) {
                            activeBetNumberOfPlayersLeft--;
                        }

                        break;
                    case CHECK:
                        System.out.println("###CHECK###");
                        playersWithAction--;

                        break;
                    case CALL:
                        System.out.println("###CALL###");
                        playersWithAction--;
                        activeBet = true;

//                        System.out.println("curr table bet $" + tableBet);
//                        System.out.println("player calling $" + playerWager);
//                        System.out.println("curr player bank $" + tempPlayer.getBank());
//                        System.out.println("curr table pot $" + tablePot);
                        tempPlayer.adjustPlayerBank(-tempPlayer.getBet());
                        tablePot += tempPlayer.getBet();
//                        System.out.println("curr player new bank $" + tempPlayer.getBank());
//                        System.out.println("curr table new pot $" + tablePot);
//
//
//                        System.out.println("player call: $" + tempPlayer.getBet());
//                        System.out.println("player bank: $:" + tempPlayer.getBank());
                        if(activeBetNumberOfPlayersLeft > 0) {
                            activeBetNumberOfPlayersLeft--;
                        }

                        System.out.println("player call: $" + tempPlayer.getBet());
                        System.out.println("player bank: $" + tempPlayer.getBank());
                        break;
                    case RAISE:
                        System.out.println("###RAISE###");
                        playersWithAction--;
                        activeBet = true;

//                        System.out.println("curr table bet $" + tableBet);
//                        System.out.println("player raising $" + playerWager);
//                        System.out.println("curr player bank $" + tempPlayer.getBank());
//                        System.out.println("curr table pot $" + tablePot);
                        tempPlayer.adjustPlayerBank(-tempPlayer.getBet());
                        tablePot += tempPlayer.getBet();
                        tableBet = tempPlayer.getBet();
//                        System.out.println("curr player new bank $" + tempPlayer.getBank());
//                        System.out.println("curr table new pot $" + tablePot);

                        activeBetNumberOfPlayersLeft = listPlayersRemainingRound.size() - listPlayersToRemoveFromRound.size() - 1;
                        System.out.println("player raise: $" + tempPlayer.getBet());
                        System.out.println("player bank: $" + tempPlayer.getBank());
                        break;
                    case ALL_IN:
                        System.out.println("###ALL_IN###");
                        playersWithAction--;
                        activeBet = true;

//                        System.out.println("curr table bet $" + tableBet);
//                        System.out.println("player allIn $" + playerWager);
//                        System.out.println("curr player bank $" + tempPlayer.getBank());
//                        System.out.println("curr table pot $" + tablePot);
                        tempPlayer.adjustPlayerBank(-tempPlayer.getBet());
                        tablePot += tempPlayer.getBet();
                        tableBet = tempPlayer.getBet();
//                        System.out.println("curr player new bank $" + tempPlayer.getBank());
//                        System.out.println("curr table new pot $" + tablePot);

                        activeBetNumberOfPlayersLeft = listPlayersRemainingRound.size() - listPlayersToRemoveFromRound.size() - 1;

                        System.out.println("player all in: $" + tempPlayer.getBet());
                        System.out.println("player bank: $" + tempPlayer.getBank());
                        break;
                }




                // update bank maps after action is taken
                updateListPlayerNameBankMap();



//                System.out.println("attempting phaseComplete: " + phaseComplete);
//                System.out.println("tempPlayer.getName(): " + tempPlayer.getName());
//                System.out.println("tempPlayer.isFold(): " + tempPlayer.isFold());
//                System.out.println("tempPlayer.isAllIn(): " + tempPlayer.isAllIn());
//                System.out.println("playersWithAction: " + playersWithAction);
//
//                System.out.println("listPlayersRemainingInRound.size(): " + listPlayersRemainingRound.size());
//                System.out.println("listPlayersToRemoveFromRound.size(): " + listPlayersToRemoveFromRound.size());
//                System.out.println("activeBetNumberOfPlayersLeft: " + activeBetNumberOfPlayersLeft);
//                System.out.println("activeBet: " + activeBet);
//                System.out.println();
                System.out.println();
                sleep(10);
                if(activeBetNumberOfPlayersLeft == 0) {
                    activeBet = false;
                }

                if(playersWithAction <= 0 && !activeBet) {
                    phaseComplete = true;
                }
            }
            if(listPlayersToRemoveFromRound.size() < listPlayersRemainingRound.size()) {
                listPlayersRemainingRound.removeAll(listPlayersToRemoveFromRound);
            }
        }



    }


    private GameState getCurrentGameState() {
        // Return a new GameState object reflecting the current state
        return new GameState(tableCards, listPlayersNameBankMap, deck.getDeckSize(),
                listPlayersRemainingGame.size(), listPlayersRemainingRound.size(),
                tableAnteCountdown, tableAnteSmall, tableAnteBig, tablePot, tableBet,
                tableMinBet, activeBet, activeBetNumberOfPlayersLeft, numTotalGames,
                numRoundStage, dealer, small, big, dealerIndex);
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
            System.out.println("Number of games simulated: " + ++numGamesPlayed);
            sleep(5000);


            GameEngine game = new GameEngine();
            game.start();

            //System.exit(0);
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
    }

    private void showdownPhase() {
        System.out.println("evaluating winners");
        System.out.println("listPlayersRemainingRound.size(): " + listPlayersRemainingRound.size());
        Evaluator evaluator = new Evaluator(listPlayersRemainingRound, tableCards);
        listPlayersWinner = evaluator.findWinners();

        for(Card tempCard: tableCards) {
            System.out.print(tempCard.toString() + " ");
        }
        sleep(gameSpeed);
        System.out.println("\n");

        if (listPlayersWinner.size() == 1) {
            listPlayersWinner.get(0).adjustPlayerBank(tablePot);
            System.out.print("WINNER: " + listPlayersWinner.get(0).getName() + " ");
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
                System.out.print("New Bank: $" + winner.getBank() + " (+$" + splitPotAmount + ") ");
                for(Card tempCard: listPlayersWinner.get(0).getHandCards()) {
                    System.out.print(tempCard.toString() + " ");
                }
                System.out.println();
                sleep(gameSpeed);
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
        int smallAdjustedAnte = 0;
        int bigAdjustedAnte = 0;
        if(small.getBank() < tableAnteSmall) {
            smallAdjustedAnte = small.getBank();
            small.adjustPlayerBank(-smallAdjustedAnte);;
            tablePot += smallAdjustedAnte;
        } else {
            small.adjustPlayerBank(-tableAnteSmall);
            tablePot += tableAnteSmall;
        }
        if(big.getBank() < tableAnteBig) {
            bigAdjustedAnte = big.getBank();
            small.adjustPlayerBank(-bigAdjustedAnte);;
            tablePot += bigAdjustedAnte;
        } else {
            big.adjustPlayerBank(-tableAnteBig);
            tablePot += tableAnteBig;
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


