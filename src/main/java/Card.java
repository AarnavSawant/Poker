import java.util.Comparator;

public class Card  {
    private int suit;
    private int rank;
    public boolean isGrouped;

    public Card(int suit, int rank) {
        //Suit and rank are integers
        this.suit = suit;
        this.rank = rank;
    }

    private String [] ranks = new String [] {"A", "2", "3", "4", "5", "6", "7", "8", "9", "10", "J", "Q", "K"};
    private String [] suits = new String [] {"HEARTS", "DIAMONDS", "CLUBS", "SPADES"};

    public int getSuit() {
        //Returns integer suit
        return suit;
    }

    public int getRank() {
        //Returns the rank + 1 to get the numerical value of the card
        return rank + 1;
    }

    public String getRankAsString(int ranker) {
        //Gets the String Value that actually goes on the card
        return ranks[ranker];
    }

    public String getSuitAsString() {
        //Gets the String Suit
        return suits[suit];
    }

    @Override
    public String toString() {
        return ranks[rank] + " of " + suits[suit];
    }

    static class CardSorterByRank implements Comparator<Card> {
        //Comparator to sort Cards based on Rank

        @Override
        public int compare(Card o1, Card o2) {
            return o1.getRank() - o2.getRank();
        }
    }

    static class CardSorterBySuit implements Comparator<Card> {
        //Comparator to sort cards based on Suit

        @Override
        public int compare(Card o1, Card o2) {
            return o1.getSuit() - o2.getSuit();
        }
    }


}
