package mk.ukim.finki.av2.cards;

import java.util.Objects;

public class PlayingCard {

    private int rank;
    private CardType cardType;

    public PlayingCard(int rank, CardType cardType) {
        this.rank = rank;
        this.cardType = cardType;
    }

    public int getRank() {
        return rank;
    }

    public void setRank(int rank) {
        this.rank = rank;
    }

    public CardType getCardType() {
        return cardType;
    }

    public void setCardType(CardType cardType) {
        this.cardType = cardType;
    }

    @Override
    public String toString() {
        return String.format("%d %s", rank, cardType.name());
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        PlayingCard that = (PlayingCard) o;
        return rank == that.rank && cardType == that.cardType;
    }

    @Override
    public int hashCode() {
        return Objects.hash(rank, cardType);
    }
}
