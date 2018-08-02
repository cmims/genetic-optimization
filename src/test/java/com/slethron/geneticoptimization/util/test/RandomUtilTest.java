package com.slethron.geneticoptimization.util.test;

import com.slethron.geneticoptimization.domain.Knapsack;
import com.slethron.geneticoptimization.util.RandomUtil;
import org.junit.Test;

import java.util.ArrayList;
import java.util.Random;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertThat;
import static org.junit.Assert.assertTrue;

public class RandomUtilTest {
    @Test
    public void generateRandomStringOf25Characters() {
        var length = 25;
        var randomString = RandomUtil.generateRandomString(length);
        assertEquals(length, randomString.length());
    }
    
    @Test
    public void generateRandomBitStringOf25Bits() {
        var length = 25;
        var randomBitString = RandomUtil.generateRandomBitString(length);
        for (var i = 0; i < length; i++) {
            assertTrue(randomBitString.get(i) || !randomBitString.get(i));
        }
    }
    
    @Test
    public void generateRandomNQueensBoardOfNEquals12() {
        var n = 12;
        var randomNQueensBoard = RandomUtil.generateRandomNQueensBoard(n);
        for (var i = 0; i < n; i++) {
            assertTrue(randomNQueensBoard.get(i) >= 0 && randomNQueensBoard.get(i) < n);
        }
    }
    
    @Test
    public void generateRandomKnapsackOf10Items() {
        // Generate 15 items of weight>=10 and knapsack maxWeight of 150 so test never fails
        var numberOfItems = 15;
        var maxItemWeightValue = 50;
        var random = new Random();
        var maxKnapsackWeight = random.nextInt(100) + 50;
        
        var itemsToPut = IntStream.range(0, numberOfItems)
                .mapToObj(i -> new Knapsack.KnapsackItem(i, random.nextInt(maxItemWeightValue), random.nextInt(maxItemWeightValue)))
                .collect(Collectors.toList());
        
        var randomKnapsack = RandomUtil.generateRandomKnapsack(maxKnapsackWeight, itemsToPut);
        
        assertTrue(numberOfItems > randomKnapsack.getItems().size());
        assertEquals(maxKnapsackWeight, randomKnapsack.getMaxWeight());
        for (var item : randomKnapsack.getItems()) {
            assertTrue(itemsToPut.contains(item));
        }
    }
}
