package game;

import java.util.*;

public class Evaluator {

    List<Player> players;

    List<Card> tableCards;

    List<Evaluation> evaluations;

    public Evaluator(List<Player> players, List<Card> tableCards) {
        this.players = new ArrayList<>(players);
        this.tableCards = new ArrayList<>(tableCards);
        evaluations = new ArrayList<>();

        for(Player tempPlayer: players) {
            if(!tempPlayer.isFold()) {
                evaluations.add(new Evaluation(tempPlayer, evaluateAllPlayerHands(tempPlayer, tempPlayer.getHandCards(), tableCards)));
            }
            else if(tempPlayer.isFold() && players.size() == 1){
                evaluations.add(new Evaluation(tempPlayer, evaluateAllPlayerHands(tempPlayer, tempPlayer.getHandCards(), tableCards)));
            }
        }
    }

    public Evaluator() {
        
    }

    List<Player> findWinners() {
        List<Evaluation> highestRankEvaluations = new ArrayList<>();
        HandRanks highestRank = evaluations.get(0).getHandRank();

        for (Evaluation tempEval: evaluations) {
            if (tempEval.getHandRank().getValue() > highestRank.getValue()) {
                highestRank = tempEval.getHandRank();
                highestRankEvaluations.clear();
            }
            if (tempEval.getHandRank() == highestRank) {
                highestRankEvaluations.add(tempEval);
            }
        }

        List<Player> winners = new ArrayList<>();
        for(Evaluation tempEval: highestRankEvaluations) {
            winners.add(tempEval.getPlayer());
        }
        System.out.println("WINNING HAND: " + highestRankEvaluations.get(0).getHandRank());
        return winners;
    }

    private HandRanks evaluateAllPlayerHands(Player player, List<Card> playerCards, List<Card> communityCards) {
        List<Card> fullHand = new ArrayList<>(playerCards);
        fullHand.addAll(communityCards);

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
        return HandRanks.HIGH_CARD;
    }

    public HandRanks evaluatePlayerHand(List<Card> playerCards, List<Card> communityCards) {
        List<Card> fullHand = new ArrayList<>(playerCards);
        fullHand.addAll(communityCards);

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
        return HandRanks.HIGH_CARD;
    }

    private boolean isPair(List<Card> hand) {
        Map<Integer, Integer> valueCounts = new HashMap<>();

        for (Card tempCard : hand) {
            int cardValue = tempCard.getValue();
            valueCounts.put(cardValue, valueCounts.getOrDefault(cardValue, 0) + 1);
        }

        int pairCount = 0;
        for (int count : valueCounts.values()) {
            if (count == 2) {
                pairCount++;
            }
        }

        return pairCount == 1;
    }

    private boolean isTwoPair(List<Card> hand) {
        Map<Integer, Integer> valueCounts = new HashMap<>();

        for (Card tempCard : hand) {
            int cardValue = tempCard.getValue();
            valueCounts.put(cardValue, valueCounts.getOrDefault(cardValue, 0) + 1);
        }

        int pairCount = 0;
        for (int count : valueCounts.values()) {
            if (count == 2) {
                pairCount++;
            }
        }

        return pairCount == 2;
    }

    private boolean isThreeOfAKind(List<Card> hand) {
        Map<Integer, Integer> valueCounts = new HashMap<>();

        for(Card tempCard: hand) {
            int value = tempCard.getValue();
            valueCounts.put(value, valueCounts.getOrDefault(value, 0) + 1);
        }

        for(int count: valueCounts.values()) {
            if(count == 3) {
                return true;
            }
        }
        return false;
    }

    private boolean isStraightWheel(List<Card> hand) {
        TreeSet<Integer> sortedRanks = new TreeSet<>();

        for (Card card : hand) {
            int rank = card.getValue();
            sortedRanks.add(rank);
            if (rank == 14) {
                sortedRanks.add(1);
            }
        }

        ArrayList<Integer> uniqueRanks = new ArrayList<>(sortedRanks);

        for (int i = 0; i < uniqueRanks.size() - 4; i++) {
            boolean isStraight = true;
            for (int j = 0; j < 4; j++) {
                if (uniqueRanks.get(i + j) + 1 != uniqueRanks.get(i + j + 1)) {
                    isStraight = false;
                    break;
                }
            }
            if (isStraight) {
                return true;
            }
        }
        return false;
    }

    private boolean isStraight(List<Card> hand) {
        TreeSet<Integer> sortedRanks = new TreeSet<>();

        for (Card card : hand) {
            int rank = card.getValue();
            sortedRanks.add(rank);
        }

        ArrayList<Integer> uniqueRanks = new ArrayList<>(sortedRanks);

        for (int i = 0; i < uniqueRanks.size() - 4; i++) {
            boolean isStraight = true;
            for (int j = 0; j < 4; j++) {
                if (uniqueRanks.get(i + j) + 1 != uniqueRanks.get(i + j + 1)) {
                    isStraight = false;
                    break;
                }
            }
            if (isStraight) {
                return true;
            }
        }
        return false;
    }

    private boolean isFlush(List<Card> hand) {
        Map<Integer, Integer> suitCounts = new HashMap<>();
        for (Card card : hand) {
            Integer suit = card.getSuit();
            suitCounts.put(suit, suitCounts.getOrDefault(suit, 0) + 1);
        }

        for (Integer count : suitCounts.values()) {
            if (count >= 5) {
                return true;
            }
        }

        return false;
    }

    private boolean isFullHouse(List<Card> hand) {
        if (hand.size() < 5) {
            return false;
        }

        Map<Integer, Integer> rankCounts = new HashMap<>();
        for (Card card : hand) {
            int rank = card.getValue();
            rankCounts.put(rank, rankCounts.getOrDefault(rank, 0) + 1);
        }

        boolean hasThreeOfAKind = false;
        boolean hasPair = false;

        for (int count : rankCounts.values()) {
            if (count == 3) {
                hasThreeOfAKind = true;
            } else if (count == 2) {
                hasPair = true;
            }
        }

        return hasThreeOfAKind && hasPair;
    }

    private boolean isFourOfAKind(List<Card> hand) {
        if (hand.size() < 4) {
            return false;
        }

        Map<Integer, Integer> rankCounts = new HashMap<>();
        for (Card card : hand) {
            int rank = card.getValue();
            rankCounts.put(rank, rankCounts.getOrDefault(rank, 0) + 1);
        }

        for (int count : rankCounts.values()) {
            if (count == 4) {
                return true;
            }
        }

        return false;
    }

    private boolean isStraightFlush(List<Card> hand) {
        if (hand.size() < 5) {
            return false;
        }

        Map<Integer, List<Card>> cardsBySuit = new HashMap<>();
        for (Card card : hand) {
            List<Card> suitedCards = cardsBySuit.computeIfAbsent(card.getSuit(), k -> new ArrayList<>());
            suitedCards.add(card);
        }

        for (List<Card> suitedCards : cardsBySuit.values()) {
            if (suitedCards.size() >= 5 && isStraight(suitedCards)) {
                return true;
            }
        }

        return false;
    }

    private boolean isRoyalFlush(List<Card> hand) {
        if (!isStraightFlush(hand)) {
            return false;
        }

        Map<Integer, List<Card>> suitGroups = new HashMap<>();
        for (Card card : hand) {
            suitGroups.computeIfAbsent(card.getSuit(), k -> new ArrayList<>()).add(card);
        }

        for (List<Card> suitedCards : suitGroups.values()) {
            suitedCards.sort(Comparator.comparingInt(Card::getValue));
            if (suitedCards.size() >= 5) {
                if (suitedCards.get(suitedCards.size() - 1).getValue() == 14 &&
                        suitedCards.get(suitedCards.size() - 5).getValue() == 10) {
                    return true;
                }
            }
        }

        return false;
    }
}
