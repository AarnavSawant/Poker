import main.*;
import org.junit.Test;

import static junit.framework.Assert.assertEquals;

public class HandEvaluatorTest {
    @Test
    public void testPair() throws Exception {
        Hand hand = new Hand(new Card[] {new Card(3, 0), new Card(2, 1), new Card(2, 8), new Card(2, 10), new Card(3, 10)});
        assertEquals(hand.getmHandState(), Hand.HandState.PAIR);
    }

    @Test
    public void testTwoPair() throws Exception {
        Hand hand = new Hand(new Card[] {new Card(3, 0), new Card(2, 0), new Card(2, 8), new Card(2, 10), new Card(3, 10)});
        assertEquals(hand.getmHandState(), Hand.HandState.TWO_PAIR);
    }

    @Test
    public void testThreeOfAKind() throws Exception {
        Hand hand = new Hand(new Card[] {new Card(3, 0), new Card(2, 0), new Card(2, 0), new Card(2, 9), new Card(3, 10)});
        assertEquals(hand.getmHandState(), Hand.HandState.THREE_OF_A_KIND);
    }

    @Test
    public void testFlush() throws Exception {
        Hand hand = new Hand(new Card[] {new Card(3, 0), new Card(3, 1), new Card(3, 5), new Card(3, 9), new Card(3, 10)});
        assertEquals(hand.getmHandState(), Hand.HandState.FLUSH);
    }

    @Test
    public void testStraight() throws Exception {
        Hand hand = new Hand(new Card[] {new Card(0, 6), new Card(1, 7), new Card(2, 8), new Card(3, 9), new Card(1, 10)});
        assertEquals(hand.getmHandState(), Hand.HandState.STRAIGHT);
    }

    @Test
    public void testFourOfAKind() throws Exception {
        Hand hand = new Hand(new Card[] {new Card(3, 0), new Card(2, 0), new Card(1, 0), new Card(0, 0), new Card(3, 10)});
        assertEquals(hand.getmHandState(), Hand.HandState.FOUR_OF_A_KIND);
    }

    @Test
    public void testStraightFlush() throws Exception {
        Hand hand = new Hand(new Card[] {new Card(3, 6), new Card(3, 7), new Card(3, 8), new Card(3, 9), new Card(3, 10)});
        assertEquals(hand.getmHandState(), Hand.HandState.STRAIGHT_FLUSH);
    }

    @Test
    public void testFullHouse() throws Exception {
        Hand hand = new Hand(new Card[] {new Card(3, 0), new Card(2, 0), new Card(1, 0), new Card(2, 10), new Card(3, 10)});
        assertEquals(hand.getmHandState(), Hand.HandState.FULL_HOUSE);
    }

    @Test
    public void testRoyalFlush() throws Exception {
        Hand hand = new Hand(new Card[] {new Card(3, 9), new Card(3, 10), new Card(3, 11), new Card(3, 12), new Card(3, 0)});
        assertEquals(hand.getmHandState(), Hand.HandState.ROYAL_FLUSH);
    }



}

