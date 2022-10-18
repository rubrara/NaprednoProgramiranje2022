package mk.ukim.finki.av2.cards;

import java.util.Arrays;
import java.util.Collections;

public class Deck {

    private PlayingCard[] cards;
    private boolean[] isDealt;
    private int dealtTotal;

    public Deck() {
        cards = new PlayingCard[52];
        isDealt = new boolean[52];
        dealtTotal = 0;

        for (int i = 0; i < CardType.values().length; i++) {
            for (int j = 0; j < 13; j++) {
                cards[i * 13 + j] = new PlayingCard(j + 1, CardType.values()[i]);
            }
        }
    }

    public PlayingCard[] getCards() {
        return cards;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Deck deck = (Deck) o;
        return Arrays.equals(cards, deck.cards);
    }

    @Override
    public int hashCode() {
        return Arrays.hashCode(cards);
    }

    @Override
    public String toString() {
        return "Deck{" +
                "cards=" + Arrays.toString(cards) +
                '}';
    }

    // delenje na karta iu proverka dali ima ostanato karti i shuffle

    public boolean hasCardsLeft() {
        return (cards.length - dealtTotal) > 0;
    }

    public PlayingCard[] shuffle() {
        Collections.shuffle(Arrays.asList(cards));
        return cards;
    }

    public PlayingCard dealCard() {
        if (!hasCardsLeft()) throw new NoCardsLeftException();

        int card = (int) (Math.random() * 52);

        if (!isDealt[card]) {
            isDealt[card] = true;
            dealtTotal++;
            return cards[card];
        }

        return dealCard();
    }


}
