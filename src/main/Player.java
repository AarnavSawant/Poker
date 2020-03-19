package main;

import java.util.ArrayList;

public class Player  {
    private double balance = 5000; //Dollars
    private double betMoney = 0;
    private String name = "";
    private Hand hand;
    private boolean isFolded = false;

    public Player(Hand hand, String name){
        //Player has a hand and a Name
        this.hand = hand;
        this.name = name;
    }

    //Balance becomes less according to the money placed in the bet
    public void placeBet() {
        balance = balance - betMoney;
    }

    ///Get isFolded()
    public boolean isFolded() {
        return isFolded;
    }

    public Hand getHand() {
        return hand;
    }

    public String getName() {
        return name;
    }

    public void setBetMoney(double betMoney) {
        this.betMoney = 0;
        this.betMoney = betMoney;
    }

    public double getBalance() {
        return  balance;
    }

    public double getBetMoney() {
        return betMoney;
    }

    public void setBalance(double balance) {
        this.balance = balance;
    }

    //Places a bet with the same price as the other Player's most recent bet
    public void call(Player other) {
        setBetMoney(other.getBetMoney());
        placeBet();
    }

    //Adds the amount to the other Player's last bet
    public void raise(double amount, Player other) {
        setBetMoney(other.getBetMoney() + amount);
        placeBet();
    }

    //sets boolean isFolded toTrue
    //Tells the hand to reset
    public void foldCards() {
        isFolded = true;
        hand.resetHand();
    }

    public void setFolded(boolean folded) {
        isFolded = folded;
    }

    //Calls to Hand.replaceCards()
    public void exchangeCards(ArrayList<Integer> cardNum) {
        hand.replaceCards(cardNum);
    }

    //Displays Name, Balance, and Cards
    public void display() {
        System.out.println("Name: " + name);
        System.out.println("Balance: " + balance);
        System.out.println("Cards: ");
        hand.displayCards();
    }

    public void placeBet(double amount) {
        this.betMoney = amount;
        balance -= betMoney;

    }




}
