package wizardquest.unit.entity;

import java.util.Arrays;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertFalse;

import wizardquest.gamemanager.Utils;

public class UtilsUnitTests {

    /**
     * The entity AI is tasked with randomly selecting targets to attack, abilities
     * to use and upgrades to purchase. Each of the methods that implement these use
     * the Utils.shuffleArray() method, which uses generics to deal with arrays of
     * any type.
     * This test runs this method on an Integer array of all ordered values 1-10
     * twice, ensuring that the original differs from both shuffled arrays, and that
     * the shuffled arrays are different from one another.
     */
    @Test
    @DisplayName("Utils - Array shuffling algorithm functions")
    void shuffleArray_works() {
        // Create an Integer array of all ordered values 1-10.
        // The original will be immutable. Two mutable copies are made of this array,
        // these will be passed into the shuffle method. Having two copies allows us
        // to demonstrate that the result of shuffling the array is different each time.
        final Integer[] testArrOriginal = { 1, 2, 3, 4, 5, 6, 7, 8, 9, 10 };
        Integer[] testArrCopy1 = testArrOriginal.clone();
        Integer[] testArrCopy2 = testArrOriginal.clone();

        // Invoke the shuffle method on both copies of the array.
        Utils.shuffleArray(testArrCopy1);
        Utils.shuffleArray(testArrCopy2);

        // Probability of a pair of arrays below having the same order = 1/(10!) = 1 in
        // ~3.26 million
        assertFalse(Arrays.equals(testArrOriginal, testArrCopy1));
        assertFalse(Arrays.equals(testArrOriginal, testArrCopy2));
        assertFalse(Arrays.equals(testArrCopy1, testArrCopy2));
    }
}
