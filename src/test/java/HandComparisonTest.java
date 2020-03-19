import main.*;
import org.junit.Test;

import static junit.framework.Assert.assertEquals;

public class HandComparisonTest {
    protected int testValue, correctValue;


    @Test
    public void testPairWithFlush() throws Exception {
        Hand hand = new Hand(new Card[] {new Card(3, 0), new Card(2, 1), new Card(2, 8), new Card(2, 10), new Card(3, 10)});
        Hand hand2 = new Hand(new Card[] {new Card(1, 4), new Card(1, 8), new Card(1, 12), new Card(1, 9), new Card(1, 11)});
        assertEquals((hand.compareHands(hand2)), -1);
    }

    @Test
    public void testPairsWithDifferentPairValues() {
        Hand hand = new Hand(new Card[] {new Card(3, 0), new Card(2, 1), new Card(2, 8), new Card(2, 10), new Card(3, 10)});
        Hand hand2 = new Hand(new Card[] {new Card(1, 4), new Card(2, 8), new Card(0, 10), new Card(3, 12), new Card(1, 12)});
        assertEquals((hand.compareHands(hand2)), -1);
    }

    @Test
    public void testPairsWithAces() {
        Hand hand = new Hand(new Card[] {new Card(3, 0), new Card(2, 0), new Card(2, 8), new Card(2, 9), new Card(3, 10)});
        Hand hand2 = new Hand(new Card[] {new Card(1, 4), new Card(2, 8), new Card(0, 10), new Card(3, 12), new Card(1, 12)});
        assertEquals((hand.compareHands(hand2)), 1);
    }

    @Test
    public void testTieWithRoyalFlush() {
        Hand hand = new Hand(new Card[] {new Card(3, 9), new Card(3, 10), new Card(3, 11), new Card(3, 12), new Card(3, 0)});
        Hand hand2 = new Hand(new Card[] {new Card(2, 9), new Card(2, 10), new Card(2, 11), new Card(2, 12), new Card(2, 0)});
        assertEquals((hand.compareHands(hand2)), 0);
    }

}
