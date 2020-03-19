import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;

public class Hand {
    private ArrayList<Integer> handScore = new ArrayList<Integer>();
    private Deck deck;
    private Card[] cardArray = new Card[5];
    private ArrayList<Card> discardedCards = new ArrayList<Card>();
    private int[] ranks = new int[13];
    private int[] suits = new int[4];
    private int highValue = 0;
    private int fourValue = 0;
    private int[] pairValue = new int[2];
    private int threeValue = 0;
    ArrayList<Integer> indexes = new ArrayList<>();
    int indexesCounter;

    private HandState mHandState;//TODO Make Private

    public HandState getmHandState() {
        return mHandState;
    }

    public ArrayList<Integer> getHandScore() {
        return handScore;
    }

    //Enum for Each Hand Possibility
    public enum HandState {
        DEFAULT, HIGH_CARD, PAIR, TWO_PAIR, THREE_OF_A_KIND, STRAIGHT, FLUSH, FULL_HOUSE, FOUR_OF_A_KIND, STRAIGHT_FLUSH, ROYAL_FLUSH
    }

    //Receives the Deck.getInstance(). Sets all of the arrays
    public Hand(Deck deck) {
        indexes.clear();
        this.deck = deck;
        mHandState = HandState.DEFAULT;
        setArrays();
    }

    public Hand(Card[] cardArray) {
        this.cardArray = cardArray;
    }

    void setArrays() {
        //Sets the Cards in the Hand
        setCardArray();
        //Increments Each of the 13 ranks based on the number of occurrences in the hand
        setRankArray();
        //Increments Each of the 4 suits based on the number of occurences
        setSuitsArray();
        //Sets the handScore arrayList used hor hand Compariosn
        setHandScore();
    }


    public Card[] getCardArray() {
        return cardArray;
    }

    public void setCardArray(Card[] cardArray) {
        this.cardArray = cardArray;
    }

    // Adds all of the discarded cards back to the Deck along with the cards in the Hand
    public void resetHand() {
        for (int i = 0; i < cardArray.length; i++) {
            deck.addToDeck(cardArray[i]);
        }
        for (int i = 0; i < discardedCards.size(); i++) {
            deck.addToDeck(discardedCards.get(i));
        }
        highValue = 0;
        fourValue = 0;
        Arrays.fill(pairValue, 0);
        threeValue = 0;
    }

    //Draws cards from the Deck and sorts it based on Rank
    private void setCardArray() {
        for (int i = 0; i < 5; i++) {
            cardArray[i] = deck.drawFromDeck();
        }
        Arrays.sort(cardArray, new Card.CardSorterByRank());
        checkIfGrouped();
    }

    //Checks if any cards are part of a pair, three of a kind, or four of a kind
    private void checkIfGrouped() {
        for (int i = 0; i < cardArray.length; i++) {
            //Checks if two consecutive cards have the same rank
            if ((i != 4 && cardArray[i].getRank() == (cardArray[i + 1].getRank())) || (i != 0 && cardArray[i].getRank() == cardArray[i - 1].getRank())) {
                cardArray[i].isGrouped = true;
            }
        }
    }

    //Iterates through the cardArray and increments the rank array based on the card's rank
    private void setRankArray() {
        Arrays.fill(ranks, 0);
        for (int i = 0; i < cardArray.length; i++) {
            ranks[cardArray[i].getRank() - 1]++;
        }
    }

    //Iterates through the cardArray and increments the suit array based on the card's suit
    private void setSuitsArray() {
        Arrays.fill(suits, 0);
        for (int i = 0; i < cardArray.length; i++) {
            suits[cardArray[i].getSuit()]++;
        }
    }


    //Evaluates if all cards in the cardArray are consecutive
    private boolean isStraight() {
        //Iterates through ranks and sees if any five in a row are equal to one
        for (int i = 0; i < ranks.length - 4; i++) {
            if ((ranks[i] == 1) && (ranks[i + 1] == 1) && (ranks[i + 2] == 1) && (ranks[i + 3] == 1) && (ranks[i + 4] == 1)) {
                highValue = i + 4;
                return true;
            }
        }
        //Makes sure to take the Ace in to account as it can work with the King as well as the 2.
        if (ranks[0] == 1 && ranks[12] == 1 && ranks[11] == 1 && ranks[10] == 1 && ranks[9] == 1) {
            highValue = 14;
            return true;
        }
        return false;
    }

    //Checks if all Cards have same Suit
    private boolean isFlush() {
        //Iterates through suits and sees if any index has a value of 5, or the length of the Hand/cardArray
        for (int i = 0; i < suits.length; i++) {
            if (suits[i] == cardArray.length) {
                return true;
            }
        }
        return false;
    }

    //Checks if there are four of any rank
    public boolean isFourOfAKind() {
        //Iterates through Ranks and checks if any value is equal to four
        for (int i = 0; i < ranks.length - 3; i++) {
            if (ranks[i] > 0) {
                indexesCounter++;
            }
            if (ranks[i] == 4) {
                fourValue = i + 1;
                return true;
            }
        }
        return false;
    }

    //Checks if there are three of any rank
    public boolean isThreeOfAKind() {
        //Iterates through Ranks and checks if any value is equal to three
        for (int i = 0; i < ranks.length - 2; i++) {
            if (ranks[i] == 3) {
                threeValue = i + 1;
                return true;
            }
        }
        return false;
    }


    //gets the number of Pairs
    public int getNumPairs() {
        int counter = 0;
        int j = 0;
        //if any value in ranks is 2, add the value of the pair into the array called pairvalue
        for (int i = 0; i < ranks.length; i++) {
            if ((ranks[i] == 2)) {
                if (i == 0) {
                    //If there are a pair of aces, make sure they have the highest pairValues
                    pairValue[i] = 14;
                } else {
                    pairValue[j] = i;
                }
                counter++;
                j++;
            }
        }
        Arrays.sort(pairValue);
        return counter;
    }

    //If getNumPairs() returns 2, then there  are 2 pairs
    private boolean isTwoPairs() {
        return getNumPairs() == 2;
    }

    //If getNumPairs() returns 1, then there is 1 pair
    private boolean isOnePair() {
        return getNumPairs() == 1;
    }

    //A StraightFLush meats both isFlush() and isStraight()
    private boolean isStraightFlush() {
        return isFlush() && isStraight();
    }

    //If is a three of a Kind and has a pair, then it is a FullHouse
    private boolean isFullHouse() {
        return isThreeOfAKind() && isOnePair();
    }

    //If is  straight and the highest Value is 14, then is a Royal FLush
    private boolean isRoyalFlush() {
        return isStraight() && highValue == 14;
    }

    //Sets the Enum HandState based on the various conditions
    private void setHandState() {
        if (isRoyalFlush()) {
            mHandState = HandState.ROYAL_FLUSH;
        } else if (isStraightFlush()) {
            mHandState = HandState.STRAIGHT_FLUSH;
        } else if (isFourOfAKind()) {
            mHandState = HandState.FOUR_OF_A_KIND;
        } else if (isFullHouse()) {
            mHandState = HandState.FULL_HOUSE;
        } else if (isFlush()) {
            mHandState = HandState.FLUSH;
        } else if (isStraight()) {
            mHandState = HandState.STRAIGHT;
        } else if (isThreeOfAKind()) {
            mHandState = HandState.THREE_OF_A_KIND;
        } else if (isTwoPairs()) {
            mHandState = HandState.TWO_PAIR;
        } else if (isOnePair()) {
            mHandState = HandState.PAIR;
        } else {
            mHandState = HandState.HIGH_CARD;
        }
    }

    private void setHandScore() {
        handScore.clear();
        setHandState();

        //Uses a switch statement to add scores in to handScore, an ArrayList that gets used for comparisons
        //Always adds the ordinal of mHandState as the firstValue
        switch (mHandState) {
            case ROYAL_FLUSH:
                handScore.add(HandState.ROYAL_FLUSH.ordinal());
                break;
            case STRAIGHT_FLUSH:
                handScore.add(HandState.STRAIGHT_FLUSH.ordinal());
                handScore.add(highValue); //Second Value is the highest card of the Straight Flush
                break;
            case FOUR_OF_A_KIND:
                handScore.add(HandState.FOUR_OF_A_KIND.ordinal());
                handScore.add(fourValue);//Second Value is he rank of the cards in the four of a Kind
                for (int i = cardArray.length - 1; i >= 0; i--) {
                    if (!cardArray[i].isGrouped) //The rest of the Values are the rest of the cards in order from Greatest -> Least
                        handScore.add(cardArray[i].getRank());
                }
                break;
            case FULL_HOUSE:
                handScore.add(HandState.FULL_HOUSE.ordinal());
                handScore.add(threeValue); //Second Value is the Three-Of-A-Kind Rank
                handScore.add(pairValue[1]);//Third Value is the Pair Rank
                break;
            case STRAIGHT:
                handScore.add(HandState.STRAIGHT.ordinal());
                handScore.add(highValue); //Second Value is the highest Rank of the Straight
                break;
            case FLUSH:
                handScore.add(HandState.FLUSH.ordinal());
                for (int i = cardArray.length - 1; i >= 0; i--) {
                    handScore.add(cardArray[i].getRank()); //The rest of the values are the cards in order from Greatest -> Least
                }
                break;
            case THREE_OF_A_KIND:
                handScore.add(HandState.THREE_OF_A_KIND.ordinal());
                handScore.add(threeValue); //Second Value is the Three-Of-A-Kind Rank
                for (int i = cardArray.length - 1; i >= 0; i--) {
                    if (!cardArray[i].isGrouped)
                        handScore.add(cardArray[i].getRank()); //The rest of the values are the cards in order from Greatest -> Least
                }
                break;
            case TWO_PAIR:
                handScore.add(HandState.TWO_PAIR.ordinal());
                handScore.add(pairValue[1]); //Second Value is the Highest Pair Rank
                handScore.add(pairValue[0]); //Third Value is the Lowest Pair Rank
                for (int i = cardArray.length - 1; i >= 0; i--) {
                    if (!cardArray[i].isGrouped) //Last Value is the rank of the remaining card
                        handScore.add(cardArray[i].getRank());
                }
                break;
            case PAIR:
                handScore.add(HandState.PAIR.ordinal());
                handScore.add(pairValue[1]); //Second Value is the Pair Rank
                for (int i = cardArray.length - 1; i >= 0; i--) {
                    if (!cardArray[i].isGrouped) {
                        if (cardArray[i].getRank() == 1) {
                            handScore.add(2, 14); //The rest of the values are the cards in order from Greatest -> Least
                        }
                        handScore.add(cardArray[i].getRank());
                    }
                }
                break;
            case HIGH_CARD:
                handScore.add(HandState.HIGH_CARD.ordinal());
                //All of the values are the cards in order from Greatest -> Least
                for (int i = cardArray.length - 1; i >= 0; i--) {
                    if (cardArray[i].getRank() == 1) {
                        handScore.add(1, 14);
                    }
                    handScore.add(cardArray[i].getRank());
                }
                break;
            default:
                System.out.println("Unexpected Hand State");
        }
    }

    //The method for exchanging cards
    public void replaceCards(ArrayList<Integer> cardNum) {
        for (int i = 0; i < cardNum.size(); i++) {
            discardedCards.add(cardArray[cardNum.get(i)]);
            cardArray[cardNum.get(i)] = deck.drawFromDeck();
        }
        Collections.sort(Arrays.asList(cardArray), new Card.CardSorterByRank());
        checkIfGrouped();
        setSuitsArray();
        setRankArray();
        setHandState();
        setHandScore();
    }

    @Override
    public String toString() {
        return Arrays.toString(cardArray);
    }


    //Iterates through handScore for two hands. Evaluates until oneHand's value at a given index is better than the other's at a given index
    public int compareHands(Hand other) {
        for (int i = 0; i < handScore.size(); i++) {
            if (handScore.get(i).equals(other.handScore.get(i))) {
                continue;
            }
            if (handScore.get(i) > (other.handScore.get(i))) {
                return 1;
            } else {
                return -1;
            }
        }
        return 0;

    }

    //Used to show User their Cards
    public void displayCards() {
        for (int i = 0; i < cardArray.length; i++) {
            System.out.print(i + 1 + ". ");
            System.out.println(cardArray[i]);
        }
        System.out.println("Current State -------> " + mHandState);

    }




}
