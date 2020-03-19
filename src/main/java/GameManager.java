import java.util.ArrayList;
import java.util.Scanner;
import java.util.concurrent.TimeUnit;

public class GameManager {
    private Player mUser;
    private Player mCPU;
    private AI mAI;
    private String decision;
    private boolean isPlaying = true;
    private Scanner scanner = new Scanner(System.in);
    private ArrayList<Integer> cardChange = new ArrayList<Integer>();
    private double amount = 0f;
    private double mPot = 0;
    private String name;
    private ArrayList<String> acceptableAnswers = new ArrayList<String>();
    private ArrayList<String> acceptableDecisions = new ArrayList<String>();


    public GameManager() {
        //Reads Name of Player
        System.out.println("Please enter your name");
        name = scanner.nextLine();
        //Instantiates two New Players
        mUser = new Player(new Hand(Deck.getInstance()), name);
        mCPU = new Player(new Hand(Deck.getInstance()), "COMPUTER");
        //Instantiates an AI object with the two Players
        mAI = new AI(mCPU, mUser);
        //Adds all of the Acceptable answers and decisions to be used later on
        acceptableAnswers.add("yes");
        acceptableAnswers.add("no");
        acceptableDecisions.add("raise");
        acceptableDecisions.add("call");
        acceptableDecisions.add("fold");
    }


    public void periodicPlay() throws InterruptedException {
        //Only executes if isPlaying == true. If User stops playing after one round, isPlaying gets set to false.
        while (isPlaying) {
            System.out.println();
            //Resetting Hands and the Pot
            mPot = 0;
            mUser.getHand().resetHand();
            mCPU.getHand().resetHand();
            mUser.getHand().setArrays();
            mCPU.getHand().setArrays();
            mUser.setFolded(false);
            mCPU.setFolded(false);
            cardChange.clear();

            //Paying the Ante's
            TimeUnit.SECONDS.sleep(1);
            System.out.println("Paying the 100 Dollar Ante....");
            TimeUnit.SECONDS.sleep(1);
            mUser.setBetMoney(100);
            mCPU.setBetMoney(100);
            mUser.placeBet();
            mCPU.placeBet();
            mPot += 200;

            //CPU plays the first bet...The Math.random() allows the CPU to have varying bets for each of the hand states
            mAI.firstBet(Math.random());
            TimeUnit.SECONDS.sleep(1);
            mAI.display();
            System.out.println();
            mPot += mCPU.getBetMoney(); //The Pot gains the money that the CPU bets
            TimeUnit.SECONDS.sleep(4);
            mUser.display(); //Displays the User's cards
            System.out.println("Money Currently in Pot: $" + mPot);
            System.out.println("Current Bet: " + mCPU.getBetMoney());
            System.out.println();
            //Asking for User Input for their decision(fold, raise, or call)
            do {
                TimeUnit.SECONDS.sleep(1);
                System.out.println("Would you like to fold, raise, or call?");
                decision = scanner.nextLine().toLowerCase();
            } while (!acceptableDecisions.contains(decision)); //keeps looping until the decision falls in the list of appropriate responses
            if (decision.equals("fold")) {
                mUser.foldCards();
            } else if (decision.equals("raise")) {
                do {
                    System.out.println("Please enter the amount you would like to raise by.");
                    amount = scanner.nextDouble(); //Reads the amount user wants to raise above the CPU's last bet
                } while (amount + mCPU.getBetMoney() > mUser.getBalance());
                System.out.println();                                                                                                     
                mUser.raise(amount, mCPU);//Calls Player method for Raising Bet                                                           
                System.out.println("User chooses to raise by " + amount + " leaving a balance of " + mUser.getBalance()); //Printing Bet  
                mPot += mUser.getBetMoney(); //Increase Pot my the amount the user bets                                                   
            } else {
                mUser.call(mCPU);//Matches the last CPU bet
                mPot += mUser.getBetMoney();//Increase pot by the amount user bets
            }
            //Only move to Card-Exchanging Phase if the User hasn't folded
            if (!mUser.isFolded()) {
                System.out.println("Please enter card numbers you would like to swap, and type 'DONE' after the numbers (e.g 1 2 3 DONE)");
                while (scanner.hasNextInt()) {
                    //Creating a list of indexes to send to Player.exchangeCards()
                    cardChange.add(scanner.nextInt() - 1);
                }
                System.out.println();
                mUser.exchangeCards(cardChange); //Sending List to change the cards at the given indexes
                mAI.exchangeCards(); //AI uses algorithm to decide which cards to swap
                TimeUnit.SECONDS.sleep(2);
                mAI.display();
                if (mCPU.getBalance() < mUser.getBetMoney()) {
                    mCPU.placeBet(mCPU.getBalance());
                } else {
                    mAI.secondBet();
                }
                //Only goes to User Second Bet if CPU doesn't fold
                if (!mCPU.isFolded()) {
                    mPot += mCPU.getBetMoney(); //Adds Cpu's bet money to the Pot
                    //Displaying User's New Cards and Bet Status
                    TimeUnit.SECONDS.sleep(2);
                    mUser.display();
                    System.out.println("Money Currently in Pot: " + mPot);
                    System.out.println("Current Bet: " + mCPU.getBetMoney());
                    System.out.println();
                    //Asks for User Input for Decision(Fold, Raise, Call). Loops until gets an appropriate response
                    do {
                        //Can't Raise if the opponent's bet is more than user's balance
                        if (mUser.getBalance() > mCPU.getBetMoney()) {
                            System.out.println("Second Bet: Would you like to fold, raise, or call?");
                        } else {
                            System.out.println("Second Bet: Would you like to fold or call?");
                        }
                        scanner.nextLine();
                        decision = scanner.nextLine().toLowerCase();
                    } while (!acceptableDecisions.contains(decision));
                    if (decision.equals("fold")) {
                        mUser.foldCards();
                    } else if (decision.equals("raise")) {

                        do {
                            //Asks User Input for Final Bet
                            System.out.println("Please enter the amount you would like to raise by.");
                            amount = scanner.nextDouble();
                            System.out.println();
                        } while (amount + mCPU.getBetMoney() > mUser.getBalance());
                        mUser.raise(amount, mCPU);
                        //Tells CPU to go All-In if can't match bet
                        if (mCPU.getBalance() < mUser.getBetMoney()) {
                            mCPU.placeBet(mCPU.getBalance());
                        } else {
                            mCPU.placeBet(mCPU.getBalance() - mUser.getBetMoney());
                        }
                        mPot += mUser.getBetMoney() + mCPU.getBetMoney();
                    } else {
                        //User has to go all-in if can't match CPU's bet
                        if (mUser.getBalance() > mCPU.getBetMoney()) {
                            mUser.call(mCPU);
                        } else {
                            System.out.println(mUser.getName() + " has to go all-in!!!!!");
                            mUser.placeBet(mUser.getBalance());
                        }
                        mPot += mUser.getBetMoney();
                    }
                }
            }
            //Decides how to Distribute Money
            if (mUser.isFolded()) {
                System.out.println(mUser.getName() + " decided to fold.");
                distributeMoneyIfUserFolds();
            } else if (mCPU.isFolded()) {
                distributeMoneyIfAIFolds();
            } else {
                distributeMoneyBasedOnHands();
            }
            System.out.println();
            if (mUser.getBalance() <= 0 || mCPU.getBalance() <= 0) {
                break;
            }
            //Asks whether User wants to play again
            do {
                System.out.println("Do you want to play again? Yes or No?");
                decision = scanner.nextLine().toLowerCase();
                if (decision.equals("no")) {
                    isPlaying = false;
                }
            } while (!acceptableAnswers.contains(decision));
        }
        //Ends game if someone says No or someone goe bankrupt
        System.out.println();
        if (mUser.getBalance() <= 0) {
            System.out.println(mUser.getName() + " goes bankrupt!");
            System.out.println("Thanks for playing!!");
        } else if (mCPU.getBalance() <= 0){
            System.out.println(mCPU.getName() + " goes bankrupt!");;
            System.out.println("Thanks for playing!!");
        } else {
            System.out.println("Thanks for playing!");
        }
        scanner.close();


    }

    private void distributeMoneyIfUserFolds() {
        //Give the CPU the money in the Pot
        System.out.println("CPU earns " + mPot + " their balance of " + mUser.getBalance());
        mCPU.setBalance(mCPU.getBalance() + mPot);

        //Set all of the betMoney back to 0
        mCPU.setBetMoney(0);
        mUser.setBetMoney(0);
    }

    private void distributeMoneyIfAIFolds() {
        //Give User the money in the Pot
        System.out.println("User earns " + mPot + " their balance of " + mUser.getBalance());
        mUser.setBalance(mUser.getBalance() + mPot);

        //Set all of the betMoney back to 0
        mUser.setBetMoney(0);
        mCPU.setBetMoney(0);
    }

    private void distributeMoneyBasedOnHands() {
        mUser.display();
        System.out.println();
        mCPU.display();

        //Check which hand is better and distribute money accordingly
        if (mUser.getHand().compareHands(mCPU.getHand()) == -1) {
            //If CPU Wins, Give CPU full Pot
            mCPU.setBalance(mCPU.getBalance() + mPot);
            mCPU.setBetMoney(0);
            mUser.setBetMoney(0);
            System.out.println("COMPUTER wins the round and now gains $" + mPot + " to have a balance of " + mCPU.getBalance());
        } else if (mUser.getHand().compareHands(mCPU.getHand()) == 1) {
            //If User Wins, Give User full Pot
            System.out.println("User wins the round");
            mUser.setBalance(mUser.getBalance() + mPot);
            mUser.setBetMoney(0);
            mCPU.setBetMoney(0);
            System.out.println("USER wins the round and now gains $" + mPot + " to have a balance of " + mUser.getBalance());
        } else {
            //If Tied, Split the Pot
            System.out.println("Game Tied");
            mUser.setBalance(mUser.getBetMoney() + mPot/2);
            mCPU.setBalance(mCPU.getBetMoney() + mPot/2);
            mCPU.setBetMoney(0);
            mCPU.setBetMoney(0);
        }
    }











}
