package wizardquest.unit.entity;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.Arrays;

import wizardquest.entity.EntityAIInterface;
import wizardquest.entity.EntityAISingleton;

import static org.junit.jupiter.api.Assertions.assertFalse;

public class RandomEntityAIUnitTests {

    /**
     * The entity AI is tasked with randomly selecting targets to attack, abilities
     * to use
     * and upgrades to purchase. Each of the methods that implement these use the
     * private
     * method shuffleArray(), which uses generics to deal with arrays of any type.
     * This test runs this method on an Integer array of all ordered values 1-10
     * twice,
     * ensuring that the original differs from both shuffled arrays, and that the
     * shuffled
     * arrays are different from one another.
     *
     * @throws ClassNotFoundException    if the private class specified for
     *                                   reflection cannot
     *                                   be found.
     * @throws NoSuchMethodException     if the private method specified for
     *                                   reflection cannot
     *                                   be found.
     * @throws IllegalAccessException    if reflection fails.
     * @throws InvocationTargetException if reflection fails.
     */
    @Test
    @DisplayName("RandomEntityAI - Array shuffling algorithm functions")
    void shuffleArray_works() throws ClassNotFoundException, NoSuchMethodException,
            IllegalAccessException, InvocationTargetException {
        EntityAIInterface entity = EntityAISingleton.getInstance();
        // Create an Integer array of all ordered values 1-10.
        // The original will be immutable. Two mutable copies are made of this array,
        // these will be passed into the shuffle method. Having two copies allows us
        // to demonstrate that the result of shuffling the array is different each time.
        final Integer[] testArrOriginal = { 1, 2, 3, 4, 5, 6, 7, 8, 9, 10 };
        Integer[] testArrCopy1 = testArrOriginal.clone();
        Integer[] testArrCopy2 = testArrOriginal.clone();
        // Use reflection to access the private RandomEntityAI class.
        Class<?> RandomEntityAI = Class.forName("wizardquest.entity.EntityAISingleton$RandomEntityAI");
        // Use reflection to access the private shuffleArray method.
        Method shuffleArray = RandomEntityAI.getDeclaredMethod("shuffleArray", Object[].class);
        shuffleArray.setAccessible(true);
        // Invoke the shuffle method on both copies of the array.
        shuffleArray.invoke(entity, (Object) testArrCopy1);
        shuffleArray.invoke(entity, (Object) testArrCopy2);
        // Probability of a pair of arrays below having the same order = 1/(10!) = 1 in
        // ~3.26 million
        assertFalse(Arrays.equals(testArrOriginal, testArrCopy1));
        assertFalse(Arrays.equals(testArrOriginal, testArrCopy2));
        assertFalse(Arrays.equals(testArrCopy1, testArrCopy2));
    }
}