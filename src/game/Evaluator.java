package game;

import java.util.*;

public class Evaluator {

    List<Player> players; // List to store players participating in the current evaluation.
    List<Card> tableCards; // List to store the community cards on the table.
    List<Evaluation> evaluations; // List to store the evaluations of each player's hand.

    public Evaluator() {

    }

    // Constructor for the Evaluator class. It initializes the evaluator with a list of players and table cards.
    public Evaluator(List<Player> players, List<Card> tableCards) {
        this.players = new ArrayList<>(players); // Create a new list of players from the provided list to ensure encapsulation.
        this.tableCards = new ArrayList<>(tableCards); // Create a new list of table cards from the provided list for the same reason.
        evaluations = new ArrayList<>(); // Initialize the evaluations list.

        // Loop through each player to evaluate their hands.
        for(Player tempPlayer: players) {
            // If the player has not folded, evaluate their hand and add the evaluation to the list.
            if(!tempPlayer.isFold()) {
                evaluations.add(new Evaluation(tempPlayer, evaluateAllPlayerHands(tempPlayer, tempPlayer.getHandCards(), tableCards)));
            }
            // Special case: if there's only one player left (who has folded), still evaluate their hand.
            else if(tempPlayer.isFold() && players.size() == 1){
                evaluations.add(new Evaluation(tempPlayer, evaluateAllPlayerHands(tempPlayer, tempPlayer.getHandCards(), tableCards)));
            }
        }
    }

    // Identifies the winner(s) based on the highest hand rank among all players.
    List<Player> findWinners() {
        List<Evaluation> highestRankEvaluations = new ArrayList<>(); // Stores evaluations with the highest hand rank.
        HandRanks highestRank = evaluations.get(0).getHandRank(); // Initially set to the first player's hand rank.

        // Iterate through all evaluations to find the highest hand rank.
        for (Evaluation tempEval: evaluations) {
            // If a higher hand rank is found, update highestRank and clear previous highest evaluations.
            if (tempEval.getHandRank().getValue() > highestRank.getValue()) {
                highestRank = tempEval.getHandRank();
                highestRankEvaluations.clear();
            }
            // If the current evaluation's hand rank matches the highest found, add it to the list.
            if (tempEval.getHandRank() == highestRank) {
                highestRankEvaluations.add(tempEval);
            }
        }

        List<Player> winners = new ArrayList<>(); // List to store the winner(s).
        // Add all players associated with the highest hand rank evaluations to the winners list.
        for(Evaluation tempEval: highestRankEvaluations) {
            winners.add(tempEval.getPlayer());
        }
        // Announce the winning hand rank.
        System.out.println("WINNING HAND: " + highestRankEvaluations.get(0).getHandRank());
        return winners; // Return the list of winner(s).
    }

    // Evaluates the combined hand of a player and the community cards to determine the best possible hand rank.
    private HandRanks evaluateAllPlayerHands(Player player, List<Card> playerCards, List<Card> communityCards) {
        List<Card> fullHand = new ArrayList<>(playerCards); // Combine player's cards with community cards.
        fullHand.addAll(communityCards);

        // Check for each hand rank in descending order of rank.
        if(isRoyalFlush(fullHand)) {
            return HandRanks.ROYAL_FLUSH; // Highest possible hand.
        }
        else if(isStraightFlush(fullHand)) {
            return HandRanks.STRAIGHT_FLUSH; // Second highest hand.
        }
        else if(isFourOfAKind(fullHand)) {
            return HandRanks.FOUR_OF_A_KIND; // Third highest hand.
        }
        else if(isFullHouse(fullHand)){
            return HandRanks.FULL_HOUSE; // Fourth highest hand.
        }
        else if(isFlush(fullHand)) {
            return HandRanks.FLUSH; // Fifth highest hand.
        }
        else if(isStraightWheel(fullHand)) {
            return HandRanks.STRAIGHT; // Sixth highest hand, includes special case "wheel" straight (A-2-3-4-5).
        }
        else if(isThreeOfAKind(fullHand)) {
            return HandRanks.THREE_OF_A_KIND; // Seventh highest hand.
        }
        else if(isTwoPair(fullHand)) {
            return HandRanks.TWO_PAIR; // Eighth highest hand.
        }
        else if(isPair(fullHand)) {
            return HandRanks.PAIR; // Ninth highest hand.
        }
        return HandRanks.HIGH_CARD; // Lowest possible hand.
    }

    // Public method to evaluate a player's hand against the community cards, similar to evaluateAllPlayerHands but accessible externally.
    public HandRanks evaluatePlayerHand(List<Card> playerCards, List<Card> communityCards) {
        List<Card> fullHand = new ArrayList<>(playerCards); // Combine player's cards with community cards.
        fullHand.addAll(communityCards);

        // The checks are identical to evaluateAllPlayerHands, determining the best hand rank possible.
        if(isRoyalFlush(fullHand)) {
            return HandRanks.ROYAL_FLUSH;
        }
        else if(isStraightFlush(fullHand)) {
            return HandRanks.STRAIGHT_FLUSH;
        }
        else if(isFourOfAKind(fullHand)) {
            return HandRanks.FOUR_OF_A_KIND;
        }
        else if(isFullHouse(fullHand)){
            return HandRanks.FULL_HOUSE;
        }
        else if(isFlush(fullHand)) {
            return HandRanks.FLUSH;
        }
        else if(isStraightWheel(fullHand)) {
            return HandRanks.STRAIGHT;
        }
        else if(isThreeOfAKind(fullHand)) {
            return HandRanks.THREE_OF_A_KIND;
        }
        else if(isTwoPair(fullHand)) {
            return HandRanks.TWO_PAIR;
        }
        else if(isPair(fullHand)) {
            return HandRanks.PAIR;
        }
        return HandRanks.HIGH_CARD; // Default return if no other hand is identified.
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
            if (count == 2) { // If exactly two cards of the same value are found, it's a pair.
                pairCount++; // Increment the pair counter.
            }
        }

        return pairCount == 1; // Return true if exactly one pair is found, false otherwise.
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
            if (count == 2) { // If exactly two cards of the same value are found, it's a pair.
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
