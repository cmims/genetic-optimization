package com.slethron.geneticoptimization.util;

import com.slethron.geneticoptimization.domain.BitString;
import com.slethron.geneticoptimization.domain.Knapsack;
import com.slethron.geneticoptimization.domain.NQueensBoard;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ThreadLocalRandom;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

/**
 * Utility class that generates Random objects of various types to be used in
 * generating large collections or populations of those objects. These methods didn't
 * properly belong in their respective domain classes because there are already constructors that are
 * provided. Also, as for objects like String (java.lang.String), it was necessary to have this
 * class as a helper because their is no domain class that exists, where these methods would be placed,
 * as a means of defining the object as the subject of its problem class.
 */
public class RandomUtil {
    private RandomUtil() {
    }
    
    /**
     * Generates a string containing random UTF-16 characters. An arbitrary integer within the bounds of
     * 127 (exclusive) and 32 (inclusive) can be converted into a char using the method toChars(int)
     * provided via the class java.lang.Character.
     *
     * @param length The length of the string being generated.
     * @return The generated random string
     */
    public static String generateRandomString(int length) {
        return IntStream.range(0, length)
                .mapToObj(i -> ThreadLocalRandom.current().nextInt(127 - 32) + 32)
                .map(Character::toChars)
                .map(String::valueOf)
                .collect(Collectors.joining());
    }
    
    /**
     * Generates a BitString containing random boolean values.
     *
     * @param length The bit length of the string of bits to be generated
     * @return The generated random BitString
     */
    public static BitString generateRandomBitString(int length) {
        var bits = new boolean[length];
        
        for (var i = 0; i < length; i++) {
            bits[i] = ThreadLocalRandom.current().nextBoolean();
        }
        
        return new BitString(bits);
    }
    
    /**
     * Generates an NQueensBoard object containing random integer values in each of it's columns that are
     * within the bounds of the board length. The fitness can be calculated for the board generated by this
     * method immediately upon retrieval it.
     *
     * @param n The side length (or n) of the N-Queens board.
     * @return The generated random N-Queens board object
     */
    public static NQueensBoard generateRandomNQueensBoard(int n) {
        var board = new int[n];
        for (var i = 0; i < n; i++) {
            board[i] = ThreadLocalRandom.current().nextInt(n);
        }
        
        return new NQueensBoard(board);
    }
    
    /**
     * Generates a knapsack object containing some of the items from a specified list of items. The items
     * are permutated in a way such that they are:
     * 1) random
     * 2) not all of the items specified in the list are also in the knapsack
     *
     * It is necessary to have remaining items in the list after execution of this method.
     * IllegalArgumentException is thrown if the case where the items to put are exhausted.
     *
     * @param maxWeight  The maxWeight of the knapsack object being generated
     * @param itemsToPut The items to randomly put in the bag
     * @return The generated random Knapsack object
     */
    public static Knapsack generateRandomKnapsack(int maxWeight, List<Knapsack.KnapsackItem> itemsToPut) {
        var items = new ArrayList<>(itemsToPut);
        var knapsack = new Knapsack(maxWeight);
        for (var i = items.size(); i >= 1; i--) {
            var itemToPut = items.get(ThreadLocalRandom.current().nextInt(items.size()));
            if (knapsack.put(itemToPut)) {
                items.remove(itemToPut);
            }
        }
        
        if (items.isEmpty()) {
            throw new IllegalArgumentException("Total value of items to put in knapsack must be greater than the max"
                    + " weight of the knapsack.");
        }
        
        return knapsack;
    }
}
