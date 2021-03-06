package com.slethron.geneticoptimization.problem;

import com.slethron.geneticoptimization.DeterministicOptimizer;
import com.slethron.geneticoptimization.PopulationGenerator;
import com.slethron.geneticoptimization.util.RandomGeneratorUtil;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class StringMatchProblem extends PopulationGenerator<String> implements DeterministicOptimizer<String> {
    private Random random;
    private String target;
    
    public StringMatchProblem(String target) {
        random = new Random();
        this.target = target;
    }
    
    @Override
    public List<String> generateInitialPopulation(int populationSize) {
        var population = new ArrayList<String>();
        for (var i = 0; i < populationSize; i++) {
            population.add(RandomGeneratorUtil.generateRandomString(target.length()));
        }
        
        return population;
    }
    
    @Override
    public String generateIndividualFromParents(String parentA, String parentB) {
        var split = random.nextInt(parentA.length());
        var child = new StringBuilder();
        for (var i = 0; i < parentA.length(); i++) {
            if (i <= split) {
                child.append(parentA.charAt(i));
            } else {
                child.append(parentB.charAt(i));
            }
        }
        
        return child.toString();
    }
    
    @Override
    public String mutate(String individual, double rateOfMutation) {
        var sb = new StringBuilder();
        for (var i = 0; i < individual.length(); i++) {
            if (random.nextDouble() <= rateOfMutation) {
                var mutation = random.nextInt(127 - 32) + 32;
                sb.append(Character.toChars(mutation));
            } else {
                sb.append(individual.charAt(i));
            }
        }
        
        return sb.toString();
    }
    
    @Override
    public double fitness(String individual) {
        var fitVal = 0;
        for (var i = 0; i < individual.length(); i++) {
            fitVal += Math.abs(target.charAt(i) - individual.charAt(i));
        }
        
        return fitVal;
    }
}
