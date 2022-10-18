package mk.ukim.finki.av2.cards;

import java.util.Arrays;

public class MultipleDeck {

    private Deck[] decks;

    public MultipleDeck(int number) {
        this.decks = new Deck[number];
        for (int i = 0; i < number; i++) {
            decks[i] = new Deck();
        }
    }

    @Override
    public String toString() {
        return "MultipleDeck{" +
                "decks=" + Arrays.toString(decks) +
                '}';
    }

    public static void main(String[] args) {
        MultipleDeck decks = new MultipleDeck(3);
        System.out.println(decks);

        decks.decks[0].shuffle();
        System.out.println(decks.decks[0]);
    }
}
