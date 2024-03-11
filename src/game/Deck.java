package game;

import java.util.ArrayList;
import java.util.Collections;

public class Deck {
    private ArrayList<Card> cards;

    public Deck() {
        this.cards = new ArrayList<>();
        // 0 = heart, 1 = club, 2 = diamond, 3 = spade
        for (int suit = 0; suit <= 3; suit++) {
            // 0-10 as expected, 11 = Jack, 12 = Queen, 13 = King, 14 = Ace
            // Ace *should* be treated as either 1 or 11
            for (int value = 2; value <= 14; value++) {
                cards.add(new Card(suit, value));
            }
        }
    }

    public void shuffle() {
        Collections.shuffle(cards);
    }

    public Card topCard() {
        if (cards.isEmpty()) {
            for (int suit = 0; suit <= 3; suit++) {
                for (int value = 2; value <= 14; value++) {
                    cards.add(new Card(suit, value));
                }
            }
            shuffle();
        }
        return cards.remove(cards.size() - 1);
    }

    public int getDeckSize() {
        return cards.size();
    }

}

