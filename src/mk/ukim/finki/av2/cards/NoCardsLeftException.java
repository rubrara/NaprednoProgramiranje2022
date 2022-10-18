package mk.ukim.finki.av2.cards;

public class NoCardsLeftException extends RuntimeException {
    public NoCardsLeftException() {
        super("There are no cards left in the deck!");
    }
}
