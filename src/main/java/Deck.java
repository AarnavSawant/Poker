import java.util.ArrayList;

public class Deck {
    private static Deck mInstance;

    //Singleton because want only one instance of Deck
    public static Deck getInstance() {
        if (mInstance == null) {
            mInstance = new Deck();
        }
        return mInstance;
    }

    private ArrayList<Card> cards = new ArrayList<Card>();
    private Deck() {
        //Creating the deck of 52 Cards
        for (int i = 0; i < 4; i++) {
            for (int j = 0; j < 13; j++) {
                Card card = new Card(i, j);
                cards.add(card);
            }
        }
        //Shuffling the Cards
        for (int i = 0; i < cards.size();  i++) {
            int rand = (int) Math.floor(Math.random() * 52);
            Card temp = cards.get(i);
            cards.set(i, cards.get(rand));
            cards.set(rand, temp);
        }
    }

    public synchronized Card drawFromDeck() {
        //Get top card of shuffled Deck
        return (Card) cards.remove(0);
    }

    public synchronized boolean addToDeck(Card card) {
        //Add cards to end of Deck
        return cards.add(card);
    }


}
