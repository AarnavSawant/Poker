import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

public class AI {
    private Player mPlayer;
    private Player mOpposition;
    private ArrayList<Integer> replaceCards = new ArrayList<Integer>();
    private int acePosition = 0;
    private double betMoney = 0;
    public AI(Player player, Player opposition) {
        mPlayer = player;
        mOpposition = opposition;
    }

    //Returns the ArrayList that contains the Indexes of the Cards that are to be replaced
    public ArrayList<Integer> getReplaceCards() {
        return replaceCards;
    }

    //Algorithm to determine which cards to exchange
    public void exchangeCards() {
        replaceCards.clear();
        //If there is a pair, two pair, three of a Kind, get rid of the card that !isGrouped
        if (mPlayer.getHand().getHandScore().get(0) == 2 || mPlayer.getHand().getHandScore().get(0) == 3 || mPlayer.getHand().getHandScore().get(0) == 4) {
            for (int i = 0; i < mPlayer.getHand().getCardArray().length; i++) {
                if (!mPlayer.getHand().getCardArray()[i].isGrouped) {
                    replaceCards.add(i);
                }
            }
            mPlayer.exchangeCards(replaceCards);
        } else if (mPlayer.getHand().getHandScore().get(0) == 8) { //if there is a Four Of A Kind, only replace the last card if it is less than 9.
            for (int i = 0; i < mPlayer.getHand().getCardArray().length; i++) {
                if (!mPlayer.getHand().getCardArray()[i].isGrouped) {
                    if (mPlayer.getHand().getCardArray()[i].getRank() < 9) {
                        replaceCards.add(i);
                    }
                }
            }
            if (!replaceCards.isEmpty()) {
                mPlayer.exchangeCards(replaceCards);
            }
        } else if (mPlayer.getHand().getHandScore().get(0) == 1) {//If there is a HighCard
            if (isNearFlush()) {
                //If it is near a Flush (meaning only one card is of a different suit), get rid of that card
                if (mPlayer.getHand().getCardArray()[0].getSuit() == mPlayer.getHand().getCardArray()[1].getSuit()) {
                    //If the first two cards have the same Suit, then get rid of the last
                    replaceCards.add(4);
                } else {
                    //Else Get rid of the first card
                    replaceCards.add(0);
                }
            } else if (isNearStraight()) {
                //If it is near a Flush (meaning only one card is of a different suit), get rid of that card
                if (mPlayer.getHand().getCardArray()[0].getSuit() + 1 == mPlayer.getHand().getCardArray()[1].getSuit()) {
                    //If the first two cards have the same Suit, then get rid of the last
                    replaceCards.add(4);
                } else {
                    //Else Get rid of the first card
                    replaceCards.add(0);
                }
            } else if (containsAce()) {
                //If it doesn't meet anything, get rid of all cards except Aces.
                for (int i = 0; i < 5; i++) {
                    if (i != acePosition) {
                        replaceCards.add(i);
                    }
                }
            } else {
                //Else replace the three lowest Cards
                Arrays.sort(mPlayer.getHand().getCardArray(), new Card.CardSorterByRank());
                replaceCards.add(0);
                replaceCards.add(1);
                replaceCards.add(2);
            }
            System.out.println(mPlayer.getName() + " chooses to swap " + replaceCards.size() + " cards.");
            if (!replaceCards.isEmpty()) {
                //Call the exchangeCards method
                mPlayer.exchangeCards(replaceCards);
            }
        }
    }

    private boolean isNearFlush() {
        //Sort Based on Suit
        Arrays.sort(mPlayer.getHand().getCardArray(), new Card.CardSorterBySuit());
        boolean isNearFlush = false;
        for (int i = 0; i < 2; i++) {
            //Iterate twice, see if any four cards consecutively have the same Suit
            if ((mPlayer.getHand().getCardArray()[i].getSuit() == mPlayer.getHand().getCardArray()[i + 1].getSuit()) && (mPlayer.getHand().getCardArray()[i].getSuit() == mPlayer.getHand().getCardArray()[i + 2].getSuit()) && (mPlayer.getHand().getCardArray()[i].getSuit() == mPlayer.getHand().getCardArray()[i + 3].getSuit())) {
                isNearFlush = true;
            }
        }
        return isNearFlush;
    }

    private boolean isNearStraight() {
        //Sort Based On Rank
        Arrays.sort(mPlayer.getHand().getCardArray(), new Card.CardSorterByRank());
        boolean isNearStraight = false;
        for (int i = 0; i < 2; i++) {
            //Iterate twice, see if any four cards consecutively are consecutive in rank as well
            if ((mPlayer.getHand().getCardArray()[i].getRank() + 1 == mPlayer.getHand().getCardArray()[i + 1].getRank()) && (mPlayer.getHand().getCardArray()[i].getRank() + 2 == mPlayer.getHand().getCardArray()[i + 2].getRank()) && (mPlayer.getHand().getCardArray()[i].getRank() + 3 == mPlayer.getHand().getCardArray()[i + 3].getRank())) {
                isNearStraight = true;
            }
        }
        return isNearStraight;
    }

    private boolean containsAce() {
        //Iterate through and find the location of the ace
        for (int i = 0; i < mPlayer.getHand().getCardArray().length; i++) {
            if (mPlayer.getHand().getCardArray()[i].getRank() == 0) {
                //Only need one Ace Position as otherwise it would be a pair
                acePosition = i;
                return true;
            }
        }
        return false;
    }

    //Receives a Math.random Value
    public void firstBet(double probRoot) {
        switch (mPlayer.getHand().getmHandState()) {
            case ROYAL_FLUSH:
                //The various zones ensure that the game isn't predictable
                if (probRoot < 0.1) {
                    betMoney = mPlayer.getBalance() * 0.65;
                } else if (probRoot < 0.25) {
                    betMoney = mPlayer.getBalance() * 0.25;
                } else if (probRoot < 0.5) {
                    betMoney = mPlayer.getBalance() * 0.5;
                } else {
                    betMoney = mPlayer.getBalance() * 0.2;
                }
                break;
            case STRAIGHT_FLUSH:
                //The various zones ensure that the game isn't predictable
                if (probRoot < 0.1) {
                    betMoney = mPlayer.getBalance() * 0.4;
                } else if (probRoot < 0.25) {
                    betMoney = mPlayer.getBalance() * 0.25;
                } else if (probRoot < 0.5) {
                    betMoney = mPlayer.getBalance() * 0.5;
                } else {
                    betMoney = mPlayer.getBalance() * 0.2;
                }
                break;
            case FOUR_OF_A_KIND:
                //The various zones ensure that the game isn't predictable
                if (probRoot < 0.1) {
                    betMoney = mPlayer.getBalance() * 0.35;
                } else if (probRoot < 0.25) {
                    betMoney = mPlayer.getBalance() * 0.25;
                } else if (probRoot < 0.5) {
                    betMoney = mPlayer.getBalance() * 0.15;
                } else {
                    betMoney = mPlayer.getBalance() * 0.2;
                }
                break;
            case FULL_HOUSE:
                //The various zones ensure that the game isn't predictable
                if (probRoot < 0.1) {
                    betMoney = mPlayer.getBalance() * 0.35;
                } else if (probRoot < 0.25) {
                    betMoney = mPlayer.getBalance() * 0.25;
                } else if (probRoot < 0.5) {
                    betMoney = mPlayer.getBalance() * 0.45;
                } else {
                    betMoney = mPlayer.getBalance() * 0.2;
                }
                break;
            case FLUSH:
                //The various zones ensure that the game isn't predictable
                if (probRoot < 0.1) {
                    betMoney = mPlayer.getBalance() * 0.2;
                } else if (probRoot < 0.25) {
                    betMoney = mPlayer.getBalance() * 0.3;
                } else if (probRoot < 0.5) {
                    betMoney = mPlayer.getBalance() * 0.25;
                } else {
                    betMoney = mPlayer.getBalance() * 0.2;
                }
                break;
            case STRAIGHT:
                //The various zones ensure that the game isn't predictable
                if (probRoot < 0.1) {
                    betMoney = mPlayer.getBalance() * 0.4;
                } else if (probRoot < 0.25) {
                    betMoney = mPlayer.getBalance() * 0.3;
                } else if (probRoot < 0.5) {
                    betMoney = mPlayer.getBalance() * 0.1;
                } else {
                    betMoney = mPlayer.getBalance() * 0.2;
                }
                break;
            case THREE_OF_A_KIND:
                //The various zones ensure that the game isn't predictable
                if (probRoot < 0.25) {
                    betMoney = mPlayer.getBalance() * 0.2;
                } else if (probRoot < 0.5) {
                    betMoney = mPlayer.getBalance() * 0.3;
                } else {
                    betMoney = mPlayer.getBalance() * 0.25;
                }
                break;
            case TWO_PAIR:
                //The various zones ensure that the game isn't predictable
                if (probRoot < 0.05) {
                    betMoney = mPlayer.getBalance() * 0.3;
                } else if (probRoot < 0.15) {
                    betMoney = mPlayer.getBalance() * 0.5;
                } else if (probRoot < 0.5) {
                    betMoney = mPlayer.getBalance() * 0.25;
                } else {
                    betMoney = mPlayer.getBalance() * 0.1;
                }
                break;
            case PAIR:
                //The various zones ensure that the game isn't predictable
                if (probRoot < 0.05) {
                    betMoney = mPlayer.getBalance() * 0.3;
                } else if (probRoot < 0.15) {
                    betMoney = mPlayer.getBalance() * 0.4;
                } else if (probRoot < 0.5) {
                    betMoney = mPlayer.getBalance() * 0.15;
                } else {
                    betMoney = mPlayer.getBalance() * 0.1;
                }
                break;
            case HIGH_CARD:
                //The various zones ensure that the game isn't predictable
                if (probRoot < 0.075) {
                    betMoney = mPlayer.getBalance() * 0.3;
                } else if (probRoot < 0.1) {
                    betMoney = mPlayer.getBalance() * 0.4;
                } else {
                    betMoney = mPlayer.getBalance() * 0.1;
                }
        }
        System.out.println("AI chooses to bet $" + betMoney);
        mPlayer.setBetMoney(betMoney);
        mPlayer.placeBet();
    }

    //Second Time, actually bets according to handValue
    //If the Player's balance is more than the opposition, it will raise. Otherwise it will Match. There is logic in GameManager for if it is less than the Opposition. In that case, it would just bet all of its balance.
    public void secondBet() {
        switch (mPlayer.getHand().getmHandState()) {
            case ROYAL_FLUSH:
                //Go All-In
                betMoney = mPlayer.getBalance()- mOpposition.getBetMoney() > 0 ? mPlayer.getBalance()- mOpposition.getBetMoney() : mOpposition.getBetMoney();
                break;
            case STRAIGHT_FLUSH:
                //Raise by 0.7 times the Oppostion's Bet
                betMoney = mOpposition.getBetMoney() * 1.7 < mPlayer.getBalance() ? mOpposition.getBetMoney() * 0.7 : mOpposition.getBetMoney();
                break;
            case FOUR_OF_A_KIND:
                //Raise by 0.6 times the Oppostion's Bet
                betMoney = mOpposition.getBetMoney() * 1.6 < mPlayer.getBalance() ? mOpposition.getBetMoney() * 0.6 : mOpposition.getBetMoney();
                break;
            case FULL_HOUSE:
                //Raise by 0.5 times the Oppostion's Bet
                betMoney = mOpposition.getBetMoney() * 1.5 < mPlayer.getBalance() ? mOpposition.getBetMoney() * 0.5 : mOpposition.getBetMoney();;
                break;
            case FLUSH:
                //Raise by 0.4 times the Oppostion's Bet
                betMoney = mOpposition.getBetMoney() * 1.4 < mPlayer.getBalance() ? mOpposition.getBetMoney() * 0.4 : mOpposition.getBetMoney();;
                break;
            case STRAIGHT:
                //Raise by 0.3 times the Oppostion's Bet
                betMoney = mOpposition.getBetMoney() * 1.3 < mPlayer.getBalance() ? mOpposition.getBetMoney() * 0.3 : mOpposition.getBetMoney();;
                break;
            case THREE_OF_A_KIND:
                //Raise by 0.25 times the Oppostion's Bet
                betMoney = mOpposition.getBetMoney() * 1.25 < mPlayer.getBalance() ? mOpposition.getBetMoney() * 0.25 : mOpposition.getBetMoney();;
                break;
            case TWO_PAIR:
                //Raise by 0.15 times the Oppostion's Bet
                betMoney = mOpposition.getBetMoney() * 1.15 < mPlayer.getBalance() ? mOpposition.getBetMoney() * 0.15 : mOpposition.getBetMoney();;
                break;
            case PAIR:
                //Raise by 0.1 times the Oppostion's Bet
                betMoney = mOpposition.getBetMoney() * 1.1 < mPlayer.getBalance() ? mOpposition.getBetMoney() * 0.1 : mOpposition.getBetMoney();
                break;
            case HIGH_CARD:
                //Half the time fold...half the time raise by 0.05;
                if (Math.random() > 0.5) {
                    betMoney = mOpposition.getBetMoney() * 1.05 < mPlayer.getBalance() ? mOpposition.getBetMoney() * 0.05 : mOpposition.getBetMoney();;
                }
                betMoney = 0;
        }
        //if the betMoney is 0, FOLD
        if (betMoney == 0) {
            System.out.println(mPlayer.getName() + " decides to fold.");
            mPlayer.foldCards();
        } else if (betMoney != mOpposition.getBetMoney()) {//If not calling, RAISE
            System.out.println("AI chooses to raise $" + betMoney);
            mPlayer.raise(betMoney, mOpposition);
        } else {
            System.out.println("AI chooses to call"); //else CALL
            mPlayer.call(mOpposition);
        }
    }

    //Display the AI's cards
    public void display() {
        System.out.println("Name: " + mPlayer.getName());
        System.out.println("Balance: " + mPlayer.getBalance());
        System.out.println("Money Currently in Bet: " + mPlayer.getBetMoney());
        System.out.println("Cards: ");
        System.out.println("1. *********");
        System.out.println("2. *********");
        System.out.println("3. *********");
        System.out.println("4. *********");
        System.out.println("5. *********");
    }





}



