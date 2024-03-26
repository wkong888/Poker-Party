package players;

import game.*;

import java.util.*;

public class AdamsPlayer extends Player {

    List<Card> playedCards;
    List<Card> deckedCards;
    int numCardsPlayed;
    int currentDeckNum;

    Evaluator evaluator;

    public AdamsPlayer(String name) {
        super(name);
        // student note, you cannot use the gameState in the constructor

        playedCards = new ArrayList<>();
        deckedCards = new ArrayList<>();
        numCardsPlayed = 0;
        currentDeckNum = 1;

        evaluator = new Evaluator();

        fillDeck();
    }

    private void fillDeck() {
        deckedCards.clear();
        for (int suit = 0; suit <= 3; suit++) {
            // 0-10 as expected, 11 = Jack, 12 = Queen, 13 = King, 14 = Ace
            // Ace *should* be treated as either 1 or 11
            for (int value = 2; value <= 14; value++) {
                deckedCards.add(new Card(suit, value)); // Add a new card to the deck for each suit and value.
            }
        }
    }

    private float simulateOutcome() {
        int numOfWins = 0;
        int numOfSimulations = 99999;
        int totalWinningHandRank = 0;
        HandRanks highestWinningRank = null;

        List<Card> simHand = new ArrayList<>(getHandCards());

        for(int i = 0; i < numOfSimulations; i++) {
            List<Card> simDeck = new ArrayList<>(deckedCards);
            List<Card> simTable = new ArrayList<>(getGameState().getTableCards());

            Collections.shuffle(simDeck);

            List<Card> simOpponentHand = new ArrayList<>(simDeck.subList(0, 2));

//            System.out.println("simulation #" + i);
//            System.out.println("myHand: " + simHand);
//            System.out.println("simOpponentHand: " + simOpponentHand);


            while(simTable.size() < 5) {
                simTable.add(simDeck.remove(0));
            }
//            System.out.println("simTable: " + simTable);

            HandRanks mySimRank = evaluator.evaluatePlayerHand(simHand, simTable);
            HandRanks opponentSimRank = evaluator.evaluatePlayerHand(simOpponentHand, simTable);

//            System.out.print(mySimRank + " vs " + opponentSimRank);

            if(mySimRank.compareTo(opponentSimRank) >= 0) {
                numOfWins++;
                totalWinningHandRank += mySimRank.getValue();
//                System.out.println(" WIN\n");
                if (highestWinningRank == null || mySimRank.compareTo(highestWinningRank) >= 0) {
                    highestWinningRank = mySimRank;
                }
            }
        }

        if(numOfWins > 0) {
            System.out.println("confidence: " + (((float)numOfWins/numOfSimulations)*(totalWinningHandRank/numOfWins)*10));
            System.out.println("average winning hand rank: " + totalWinningHandRank/numOfWins);
        } else {
            System.out.println(numOfSimulations + " simulations and 0 wins");
        }

        //System.out.println("highestWinningRank: " + highestWinningRank);
        return (float)numOfWins/numOfSimulations;
    }


    @Override
    public void takePlayerTurn() {


        System.out.println("^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^");
//
//        System.out.println("evaluatePlayerHand() " + evaluatePlayerHand());
//        System.out.println("getGameState().getNumDecksUsed(): " + getGameState().getNumDecksUsed());

        if(getGameState().getNumRoundStage() == 0) {
//            System.out.println("playedCards.clear()");
            playedCards.clear();
        }

        if(currentDeckNum != getGameState().getNumDecksUsed()) {
//            System.out.println("fillDeck()");
            fillDeck();
            currentDeckNum++;
        }

//        System.out.println();
        // add hand cards to playedCards
        for(Card handCard: getHandCards()) {
            if(!playedCards.contains(handCard))
                playedCards.add(handCard);
        }
        // add table cards to playedCards
        for(Card tableCard: getGameState().getTableCards()) {
            if(!playedCards.contains(tableCard))
                playedCards.add(tableCard);
        }
        // print known played cards for the round
        // this should be reset each round == 0
        // for(Card playedCard: playedCards) {
        //     System.out.print(playedCard.toString()+ " ");
        // }
        // System.out.println("\ncards played " + playedCards.size());


//        System.out.println();
        // remove playedCards from deckCards
        deckedCards.removeAll(playedCards);
        // print known remaining cards in deck
        // for(Card deckCard: deckedCards) {
        //     System.out.print(deckCard.toString() + " ");
        // }
        // System.out.println("\ncards remaining " + deckedCards.size() + "\n");


        int levelOnePair = 0;
        int levelTwoPair = 0;
        int levelThreePair = 0;

        int levelOne2Pair = 0;
        int levelTwo2Pair = 0;
        int levelThree2Pair = 0;

        int levelOne3Kind = 0;
        int levelTwo3Kind = 0;
        int levelThree3Kind = 0;

        int levelOneStraight = 0;
        int levelTwoStraight = 0;
        int levelThreeStraight = 0;

        int levelOneFlush = 0;
        int levelTwoFlush = 0;
        int levelThreeFlush = 0;

        int levelOneFull = 0;
        int levelTwoFull = 0;
        int levelThreeFull = 0;

        int levelOne4Kind = 0;
        int levelTwo4Kind = 0;
        int levelThree4Kind = 0;

        int levelOneStraightFlush = 0;
        int levelTwoStraightFlush = 0;
        int levelThreeStraightFlush = 0;

        int levelOneRoyalFlush = 0;
        int levelTwoRoyalFlush = 0;
        int levelThreeRoyalFlush = 0;

//        System.out.println("level one inspection");
        for (Card levelOne : deckedCards) {
            List<Card> tempCards = new ArrayList<>(getGameState().getTableCards());
            tempCards.addAll(getHandCards());
            if(tempCards.size() < 7) tempCards.add(levelOne);

            for(Card tempCard: tempCards) {
                System.out.print(tempCard + " ");
            }

            if (isRoyalFlush(tempCards)) {
                System.out.print(HandRanks.ROYAL_FLUSH + " ");
                levelOneRoyalFlush++;
            }
            if (isStraightFlush(tempCards)) {
                System.out.print(HandRanks.STRAIGHT_FLUSH + " ");
                levelOneStraightFlush++;
            }
            if (isFourOfAKind(tempCards)) {
                System.out.print(HandRanks.FOUR_OF_A_KIND + " ");
                levelOne4Kind++;
            }
            if (isFullHouse(tempCards)) {
                System.out.print(HandRanks.FULL_HOUSE + " ");
                levelOneFull++;
            }
            if (isFlush(tempCards)) {
                System.out.print(HandRanks.FLUSH + " ");
                levelOneFlush++;
            }
            if (isStraightWheel(tempCards)) {
                System.out.print(HandRanks.STRAIGHT + " ");
                levelOneStraight++;
            }
            if (isThreeOfAKind(tempCards)) {
                System.out.print(HandRanks.THREE_OF_A_KIND + " ");
                levelOne3Kind++;
            }
            if (isTwoPair(tempCards)) {
                System.out.print(HandRanks.TWO_PAIR + " ");
                levelOne2Pair++;
            }
            if (isPair(tempCards)) {
                System.out.print(HandRanks.PAIR + " ");
                levelOnePair++;
            }

            System.out.println();
        }

//        System.out.println("level two inspection");
        for (int i = 0; i < deckedCards.size(); i++) {
            for (int j = i + 1; j < deckedCards.size(); j++) {
                List<Card> tempCards = new ArrayList<>(getGameState().getTableCards());
                tempCards.addAll(getHandCards());
                if(tempCards.size() < 7) tempCards.add(deckedCards.get(i));
                if(tempCards.size() < 7) tempCards.add(deckedCards.get(j));
//                for(Card tempCard: tempCards) {
//                    System.out.print(tempCard + " ");
//                }

                if (isRoyalFlush(tempCards)) {
//                    System.out.print(HandRanks.ROYAL_FLUSH);
                    levelTwoRoyalFlush++;
                }
                if (isStraightFlush(tempCards)) {
//                    System.out.print(HandRanks.STRAIGHT_FLUSH);
                    levelTwoStraightFlush++;
                }
                if (isFourOfAKind(tempCards)) {
//                    System.out.print(HandRanks.FOUR_OF_A_KIND);
                    levelTwo4Kind++;
                }
                if (isFullHouse(tempCards)) {
//                    System.out.print(HandRanks.FULL_HOUSE);
                    levelTwoFull++;
                }
                if (isFlush(tempCards)) {
//                    System.out.print(HandRanks.FLUSH);
                    levelTwoFlush++;
                }
                if (isStraightWheel(tempCards)) {
//                    System.out.print(HandRanks.STRAIGHT);
                    levelTwoStraight++;
                }
                if (isThreeOfAKind(tempCards)) {
//                    System.out.print(HandRanks.THREE_OF_A_KIND);
                    levelTwo3Kind++;
                }
                if (isTwoPair(tempCards)) {
//                    System.out.print(HandRanks.TWO_PAIR);
                    levelTwo2Pair++;
                }
                if (isPair(tempCards)) {
//                    System.out.print(HandRanks.PAIR);
                    levelTwoPair++;
                }

//                System.out.println();
            }
        }

//        System.out.println("level three inspection");
        for (int i = 0; i < deckedCards.size(); i++) {
            for (int j = i + 1; j < deckedCards.size(); j++) {
                for (int k = j + 1; k < deckedCards.size(); k++) {
                    List<Card> tempCards = new ArrayList<>(getGameState().getTableCards());
                    tempCards.addAll(getHandCards());
                    if(tempCards.size() < 7) tempCards.add(deckedCards.get(i));
                    if(tempCards.size() < 7) tempCards.add(deckedCards.get(j));
                    if(tempCards.size() < 7) tempCards.add(deckedCards.get(k));
//                    for(Card tempCard: tempCards) {
//                        System.out.print(tempCard + " ");
//                    }
                    if (isRoyalFlush(tempCards)) {
//                    System.out.print(HandRanks.ROYAL_FLUSH);
                        levelThreeRoyalFlush++;
                    }
                    if (isStraightFlush(tempCards)) {
//                    System.out.print(HandRanks.STRAIGHT_FLUSH);
                        levelThreeStraightFlush++;
                    }
                    if (isFourOfAKind(tempCards)) {
//                    System.out.print(HandRanks.FOUR_OF_A_KIND);
                        levelThree4Kind++;
                    }
                    if (isFullHouse(tempCards)) {
//                    System.out.print(HandRanks.FULL_HOUSE);
                        levelThreeFull++;
                    }
                    if (isFlush(tempCards)) {
//                    System.out.print(HandRanks.FLUSH);
                        levelThreeFlush++;
                    }
                    if (isStraightWheel(tempCards)) {
//                    System.out.print(HandRanks.STRAIGHT);
                        levelThreeStraight++;
                    }
                    if (isThreeOfAKind(tempCards)) {
//                    System.out.print(HandRanks.THREE_OF_A_KIND);
                        levelThree3Kind++;
                    }
                    if (isTwoPair(tempCards)) {
//                    System.out.print(HandRanks.TWO_PAIR);
                        levelThree2Pair++;
                    }
                    if (isPair(tempCards)) {
//                    System.out.print(HandRanks.PAIR);
                        levelThreePair++;
                    }
//                    System.out.println();
                }
            }
        }

        // Output the counts
        System.out.println("drawOnePair: " + levelOnePair + ", " + String.format("%.2f%%", (double)levelOnePair/deckedCards.size()*100));
        // System.out.println("drawTwoPair: " + levelTwoPair + ", " + String.format("%.2f%%", (((double)levelTwoPair/(deckedCards.size() * (deckedCards.size()-1)))*100)));
        // System.out.println("drawThreePair: " + levelThreePair + ", " + String.format("%.2f%%", (((double)levelThreePair/(deckedCards.size() * (deckedCards.size()-1) * (deckedCards.size()-2)))*100)));
        System.out.println("drawTwoPair: " + levelTwoPair + ", " + String.format("%.2f%%", (double)levelTwoPair / combination(deckedCards.size(), 2) * 100));
        System.out.println("drawThreePair: " + levelThreePair + ", " + String.format("%.2f%%", (double)levelThreePair / combination(deckedCards.size(), 3) * 100));

        System.out.println("drawOne2Pair: " + levelOne2Pair + ", " + String.format("%.2f%%", (double)levelOne2Pair/deckedCards.size()*100));
        System.out.println("drawTwo2Pair: " + levelTwo2Pair + ", " + String.format("%.2f%%", ((double)levelTwo2Pair / combination(deckedCards.size(), 2) * 100)));
        System.out.println("drawThree2Pair: " + levelThree2Pair + ", " + String.format("%.2f%%", ((double)levelThree2Pair / combination(deckedCards.size(), 3) * 100)));

        System.out.println("drawOne3Kind: " + levelOne3Kind + ", " + String.format("%.2f%%", (double)levelOne3Kind/deckedCards.size()));
        System.out.println("drawTwo3Kind: " + levelTwo3Kind + ", " + String.format("%.2f%%", ((double)levelTwo3Kind / combination(deckedCards.size(), 2) * 100)));
        System.out.println("drawThree3Kind: " + levelThree3Kind + ", " + String.format("%.2f%%", ((double)levelThree3Kind / combination(deckedCards.size(), 3) * 100)));

        System.out.println("drawOneStraight: " + levelOneStraight + ", " + String.format("%.2f%%", (double)levelOneStraight/deckedCards.size()*100));
        System.out.println("drawTwoStraight: " + levelTwoStraight + ", " + String.format("%.2f%%", ((double)levelTwoStraight / combination(deckedCards.size(), 2) * 100)));
        System.out.println("drawThreeStraight: " + levelThreeStraight + ", " + String.format("%.2f%%", ((double)levelThreeStraight / combination(deckedCards.size(), 3) * 100)));

        System.out.println("drawOneFlush: " + levelOneFlush + ", " + String.format("%.2f%%", (double)levelOneFlush/deckedCards.size()*100));
        System.out.println("drawTwoFlush: " + levelTwoFlush + ", " + String.format("%.2f%%", ((double)levelTwoFlush / combination(deckedCards.size(), 2) * 100)));
        System.out.println("drawThreeFlush: " + levelThreeFlush + ", " + String.format("%.2f%%", ((double)levelThreeFlush / combination(deckedCards.size(), 3) * 100)));

        System.out.println("drawOneFull: " + levelOneFull + ", " + String.format("%.2f%%", (double)levelOneFull/deckedCards.size()));
        System.out.println("drawTwoFull: " + levelTwoFull + ", " + String.format("%.2f%%", ((double)levelTwoFull / combination(deckedCards.size(), 2) * 100)));
        System.out.println("drawThreeFull: " + levelThreeFull + ", " + String.format("%.2f%%", ((double)levelThreeFull / combination(deckedCards.size(), 3) * 100)));

        System.out.println("drawOne4Kind: " + levelOne4Kind + ", " + String.format("%.2f%%", (double)levelOne4Kind/deckedCards.size()*100));
        System.out.println("drawTwo4Kind: " + levelTwo4Kind + ", " + String.format("%.2f%%", ((double)levelTwo4Kind / combination(deckedCards.size(), 2) * 100)));
        System.out.println("drawThree4Kind: " + levelThree4Kind + ", " + String.format("%.2f%%", ((double)levelThree4Kind / combination(deckedCards.size(), 3) * 100)));

        System.out.println("drawOneStraightFlush: " + levelOneStraightFlush + ", " + String.format("%.2f%%", (double)levelOneStraightFlush/deckedCards.size()*100));
        System.out.println("drawTwoStraightFlush: " + levelTwoStraightFlush + ", " + String.format("%.2f%%", ((double)levelTwoStraightFlush / combination(deckedCards.size(), 2) * 100)));
        System.out.println("drawThreeStraightFlush: " + levelThreeStraightFlush + ", " + String.format("%.2f%%", ((double)levelThreeStraightFlush / combination(deckedCards.size(), 3) * 100)));

        System.out.println("drawOneRoyalFlush: " + levelOneRoyalFlush + ", " + String.format("%.2f%%", (double)levelOneRoyalFlush/deckedCards.size()*100));
        System.out.println("drawTwoRoyalFlush: " + levelTwoRoyalFlush + ", " + String.format("%.2f%%", ((double)levelTwoRoyalFlush / combination(deckedCards.size(), 2) * 100)));
        System.out.println("drawThreeRoyalFlush: " + levelThreeRoyalFlush + ", " + String.format("%.2f%%", ((double)levelThreeRoyalFlush / combination(deckedCards.size(), 3) * 100)));


        System.out.println("simulateOutcome(): " + String.format("%.2f%%", simulateOutcome()*100));

        // List<Card> possibleTableCards = new ArrayList<>(getGameState().getTableCards());
        // for(Card levelOne: new ArrayList<>(deckedCards)) {
        //     if(possibleTableCards.size() < 5) {
        //         possibleTableCards.add(levelOne);
        //         if(evaluator.evaluatePlayerHand(getHandCards(), possibleTableCards).equals(HandRanks.PAIR)) levelOnePair++;

        //     }
        // }


//        int twoPairCount = 0;
//        int threeOfAKindCount = 0;
//        //List<Card> possibleTableCards = new ArrayList<>(getGameState().getTableCards());
//        for(Card deckCard: deckedCards) {
//            possibleTableCards.add(deckCard);
//            if(evaluator.evaluatePlayerHand(getHandCards(), possibleTableCards).equals(HandRanks.PAIR)) {
//                System.out.println("pair found " + ++pairCount);
//                System.out.println(getHandCards());
//                System.out.println(possibleTableCards);
//            }
//
//
//            if(evaluator.evaluatePlayerHand(getHandCards(), possibleTableCards).equals(HandRanks.TWO_PAIR)) {
//                System.out.println("twoPair found " + ++twoPairCount);
//                System.out.println(getHandCards());
//                System.out.println(possibleTableCards);
//            }
//            if(evaluator.evaluatePlayerHand(getHandCards(), possibleTableCards).equals(HandRanks.THREE_OF_A_KIND)) {
//                System.out.println("threeOfAKind found " + ++threeOfAKindCount);
//                System.out.println(getHandCards());
//                System.out.println(possibleTableCards);
//            }
//
//
//            possibleTableCards.remove(deckCard);
//        }
//
//
//        System.out.println("pair probability " + pairCount/(double)getGameState().getDeckNumRemainingCards());
//        System.out.println("twoPair probability " + twoPairCount/(double)getGameState().getDeckNumRemainingCards());
//        System.out.println("threeOfAKind probability " + threeOfAKindCount/(double)getGameState().getDeckNumRemainingCards());



//
//        System.out.println("getGameState().getNumPlayersRemainingGame(): " + getGameState().getNumPlayersRemainingGame());
//        System.out.println("getGameState().getNumPlayersRemainingRound(): " + getGameState().getNumPlayersRemainingRound());
//        System.out.println("getGameState().getNumTotalGames(): " + getGameState().getNumTotalGames());
//        System.out.println("getGameState().getDeckNumRemainingCards(): " + getGameState().getDeckNumRemainingCards());
//        System.out.println("getGameState().getDeckNumRemainingCards() - (getGameState().getNumPlayersRemainingGame()-1) * 2 * getGameState().getNumTotalGames(): " + (getGameState().getDeckNumRemainingCards() - (getGameState().getNumPlayersRemainingGame()-1) * 2 * getGameState().getNumTotalGames()));
//        System.out.println("playedCards.size(): " + playedCards.size());
//        System.out.println("known num of cards played: " + (playedCards.size() + (getGameState().getNumPlayersRemainingGame()-1) * 2 * getGameState().getNumTotalGames()));
//        System.out.println("getGameState().getNumDecksUsed(): " + getGameState().getNumDecksUsed());
//
//
//
//        Set<String> playerNames = new HashSet<>(); // Use a Set to store unique player names
//        for (Map<String, Integer> playerBankMap : getGameState().getListPlayersNameBankMap()) {
//            playerNames.addAll(playerBankMap.keySet()); // Add all keys to the Set
//        }
//        List<String> uniquePlayerNames = new ArrayList<>(playerNames); // Convert Set to List
//        System.out.println(uniquePlayerNames);

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

    private long combination(int n, int k) {
        long result = 1;
        for (int i = 0; i < k; i++) {
            result *= (n - i);
            result /= (i + 1);
        }
        return result;
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

    private boolean isPair(List<Card> hand) {
        // Create a map to count occurrences of each card value in the hand.
        Map<Integer, Integer> valueCounts = new HashMap<>();

        // Iterate through each card in the hand.
        for (Card tempCard : hand) {
            int cardValue = tempCard.getValue(); // Get the value of the current card.
            // Update the count for this card value, incrementing by 1 or initializing to 1 if not already in the map.
            valueCounts.put(cardValue, valueCounts.getOrDefault(cardValue, 0) + 1);
        }

        int pairCount = 0; // Initialize a counter for pairs found.
        // Iterate through the counts of each card value.
        for (int count : valueCounts.values()) {
            if (count >= 2) { // If exactly two cards of the same value are found, it's a pair.
                pairCount++; // Increment the pair counter.
            }
        }

        return pairCount >= 1; // Return true if exactly one pair is found, false otherwise.
    }

    private boolean isTwoPair(List<Card> hand) {
        // Create a map to count occurrences of each card value in the hand.
        Map<Integer, Integer> valueCounts = new HashMap<>();

        // Iterate through each card in the hand to populate the valueCounts map.
        for (Card tempCard : hand) {
            int cardValue = tempCard.getValue();
            // Increment the count for this card value or initialize it to 1 if it's not already in the map.
            valueCounts.put(cardValue, valueCounts.getOrDefault(cardValue, 0) + 1);
        }

        int pairCount = 0; // Initialize a counter for the number of pairs found.
        // Iterate through the counts of each card value to find pairs.
        for (int count : valueCounts.values()) {
            if (count >= 2) { // If exactly two cards of the same value are found, it's a pair.
                pairCount++; // Increment the pair counter for each pair found.
            }
        }

        return pairCount == 2; // Return true if exactly two pairs are found, false otherwise.
    }

    private boolean isThreeOfAKind(List<Card> hand) {
        // Initialize a map to count occurrences of each card value in the hand.
        Map<Integer, Integer> valueCounts = new HashMap<>();

        // Iterate through each card in the hand.
        for(Card tempCard: hand) {
            int value = tempCard.getValue();
            // Update the count for this card value, incrementing by 1 or initializing to 1 if not already in the map.
            valueCounts.put(value, valueCounts.getOrDefault(value, 0) + 1);
        }

        // Check the counts of each card value to find if any value occurs exactly three times.
        for(int count: valueCounts.values()) {
            if(count == 3) {
                // If a card value occurs exactly three times, it's a three-of-a-kind.
                return true;
            }
        }
        // Return false if no three-of-a-kind is found.
        return false;
    }

    private boolean isStraightWheel(List<Card> hand) {
        // Initialize a TreeSet to automatically sort and remove duplicate ranks, including a special case for Ace.
        TreeSet<Integer> sortedRanks = new TreeSet<>();

        // Iterate through each card in the hand.
        for (Card card : hand) {
            int rank = card.getValue();
            sortedRanks.add(rank);
            // Ace can act as the lowest card (value 1) in a "wheel" straight (A-2-3-4-5).
            if (rank == 14) { // Ace's value is 14
                sortedRanks.add(1); // Add Ace as the lowest value for straight wheel evaluation.
            }
        }

        // Convert the sorted set to a list for indexed access.
        ArrayList<Integer> uniqueRanks = new ArrayList<>(sortedRanks);

        // Check for consecutive ranks to identify a straight.
        for (int i = 0; i < uniqueRanks.size() - 4; i++) { // Ensure there's room for 5 consecutive ranks.
            boolean isStraight = true;
            for (int j = 0; j < 4; j++) { // Check the next four cards for consecutive values.
                if (uniqueRanks.get(i + j) + 1 != uniqueRanks.get(i + j + 1)) {
                    isStraight = false; // Not consecutive, break and check the next sequence.
                    break;
                }
            }
            if (isStraight) {
                return true; // Found a straight.
            }
        }
        return false; // No straight found.
    }

    private boolean isStraight(List<Card> hand) {
        // Initialize a TreeSet to sort the card ranks and remove duplicates for a straight evaluation.
        TreeSet<Integer> sortedRanks = new TreeSet<>();

        // Iterate through each card in the hand, adding its rank to the sorted set.
        for (Card card : hand) {
            int rank = card.getValue();
            sortedRanks.add(rank);
        }

        // Convert the sorted set to a list for easier indexed access.
        ArrayList<Integer> uniqueRanks = new ArrayList<>(sortedRanks);

        // Iterate through the list to check for 5 consecutive ranks.
        for (int i = 0; i < uniqueRanks.size() - 4; i++) { // Ensure there's room for 5 consecutive ranks.
            boolean isStraight = true;
            for (int j = 0; j < 4; j++) { // Check the next four cards for consecutive values.
                if (uniqueRanks.get(i + j) + 1 != uniqueRanks.get(i + j + 1)) {
                    isStraight = false; // If any pair of consecutive cards is not actually consecutive, break.
                    break;
                }
            }
            if (isStraight) {
                return true; // A straight has been found.
            }
        }
        return false; // No straight found in the hand.
    }

    private boolean isFlush(List<Card> hand) {
        // Create a map to count the occurrences of each suit in the hand.
        Map<Integer, Integer> suitCounts = new HashMap<>();
        // Iterate through each card in the hand.
        for (Card card : hand) {
            Integer suit = card.getSuit();
            // Increment the count for this suit or initialize it to 1 if it's not already in the map.
            suitCounts.put(suit, suitCounts.getOrDefault(suit, 0) + 1);
        }

        // Check if any suit count is 5 or more, indicating a flush.
        for (Integer count : suitCounts.values()) {
            if (count >= 5) {
                return true; // A flush is found.
            }
        }

        return false; // No flush found.
    }

    private boolean isFullHouse(List<Card> hand) {
        if (hand.size() < 5) {
            return false; // A full house is not possible with less than 5 cards.
        }

        Map<Integer, Integer> rankCounts = new HashMap<>();
        for (Card card : hand) {
            int rank = card.getValue();
            // Count the occurrences of each rank in the hand.
            rankCounts.put(rank, rankCounts.getOrDefault(rank, 0) + 1);
        }

        boolean hasThreeOfAKind = false;
        boolean hasPair = false;

        for (int count : rankCounts.values()) {
            if (count == 3) {
                hasThreeOfAKind = true; // Found three cards of the same rank.
            } else if (count == 2) {
                hasPair = true; // Found two cards of the same rank.
            }
        }

        // A full house requires both a three of a kind and a pair.
        return hasThreeOfAKind && hasPair;
    }

    private boolean isFourOfAKind(List<Card> hand) {
        if (hand.size() < 4) {
            return false; // A four of a kind is not possible with less than 4 cards.
        }

        Map<Integer, Integer> rankCounts = new HashMap<>();
        for (Card card : hand) {
            int rank = card.getValue();
            // Count the occurrences of each rank in the hand.
            rankCounts.put(rank, rankCounts.getOrDefault(rank, 0) + 1);
        }

        for (int count : rankCounts.values()) {
            if (count == 4) {
                return true; // Found four cards of the same rank, indicating a four of a kind.
            }
        }

        return false; // No four of a kind found.
    }

    private boolean isStraightFlush(List<Card> hand) {
        if (hand.size() < 5) {
            return false; // A straight flush is not possible with less than 5 cards.
        }

        Map<Integer, List<Card>> cardsBySuit = new HashMap<>();
        // Group cards by their suit.
        for (Card card : hand) {
            List<Card> suitedCards = cardsBySuit.computeIfAbsent(card.getSuit(), k -> new ArrayList<>());
            suitedCards.add(card);
        }

        // Check each suit group for a straight.
        for (List<Card> suitedCards : cardsBySuit.values()) {
            if (suitedCards.size() >= 5 && isStraight(suitedCards)) {
                return true; // Found a straight flush.
            }
        }

        return false; // No straight flush found.
    }

    private boolean isRoyalFlush(List<Card> hand) {
        if (!isStraightFlush(hand)) {
            return false; // If there's no straight flush, there can't be a royal flush.
        }

        Map<Integer, List<Card>> suitGroups = new HashMap<>();
        // Group cards by their suit.
        for (Card card : hand) {
            suitGroups.computeIfAbsent(card.getSuit(), k -> new ArrayList<>()).add(card);
        }

        // Check each suit group for a royal flush.
        for (List<Card> suitedCards : suitGroups.values()) {
            suitedCards.sort(Comparator.comparingInt(Card::getValue));
            // Check if the highest straight flush includes an Ace as the highest card and starts with a 10.
            if (suitedCards.size() >= 5 && suitedCards.get(suitedCards.size() - 1).getValue() == 14 && suitedCards.get(suitedCards.size() - 5).getValue() == 10) {
                return true; // Found a royal flush.
            }
        }

        return false; // No royal flush found.
    }
}