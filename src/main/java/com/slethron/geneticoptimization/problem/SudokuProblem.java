package com.slethron.geneticoptimization.problem;

import com.slethron.geneticoptimization.GeneticOptimizer;
import com.slethron.geneticoptimization.domain.SudokuBoard;
import com.slethron.geneticoptimization.util.NanoTimer;
import com.slethron.geneticoptimization.util.SudokuGenerator;

import java.util.List;
import java.util.Random;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

public class SudokuProblem implements GeneticOptimizer<SudokuBoard> {
    private Random random;
    
    public SudokuProblem() {
        random = new Random();
    }
    
    @Override
    public SudokuBoard generateIndividualFromParents(SudokuBoard parentA, SudokuBoard parentB) {
        var splitRow = random.nextInt(SudokuBoard.SIZE);
        var splitCol = random.nextInt(SudokuBoard.SIZE);
        var board = new int[SudokuBoard.SIZE][SudokuBoard.SIZE];
        for (var i = 0; i < SudokuBoard.SIZE; i++) {
            for (var j = 0; j < SudokuBoard.SIZE; j++) {
                if (i <= splitRow && j <= splitCol) {
                    board[i][j] = parentA.get(i, j);
                } else {
                    board[i][j] = parentB.get(i, j);
                }
            }
        }
        
        return new SudokuBoard(board, parentA.getStaticCells());
    }
    
    @Override
    public SudokuBoard mutate(SudokuBoard individual, double mutationRate) {
        var mutated = individual.clone();
        for (var i = 0; i < 9; i++) {
            for (var j = 0; j < 9; j++) {
                if (individual.isStatic(i, j)) {
                    continue;
                }
                if (random.nextDouble() <= mutationRate) {
                    var value = random.nextInt(SudokuBoard.SIZE) + 1;
                    mutated.set(i, j, value);
                }
            }
        }
        
        return mutated;
    }
    
    @Override
    public double fitness(SudokuBoard individual) {
        var numberOfConflicts = 0;
        for (var row = 0; row < 9; row++) {
            for (var column = 0; column < 9; column++) {
                var num = individual.get(row, column);
                
                if (num == 0) break;
                
                for (var i = 0; i < 9; i++) {
                    if (i != row && individual.getBoard()[i][column] == num) {
                        numberOfConflicts++;
                    }
                    if (i != column && individual.getBoard()[row][i] == num) {
                        numberOfConflicts++;
                    }
                }
                
                var subGridOriginRow = (row / 3) * 3;
                var subGridOriginCol = (column / 3) * 3;
                
                for (var i = subGridOriginRow; i < subGridOriginRow + 3; i++) {
                    for (var j = subGridOriginCol; j < subGridOriginCol + 3; j++) {
                        if (!(i == row && j == column) && individual.get(i, j) == num) {
                            numberOfConflicts++;
                        }
                    }
                }
            }
        }
        
        return numberOfConflicts;
    }
    
    private List<SudokuBoard> generatePopulationFromIndividual(int populationSize, SudokuBoard individual) {
        return IntStream.range(0, populationSize)
                .parallel().unordered()
                .mapToObj(sudokuBoard -> mutate(individual, 1))
                .collect(Collectors.toList());
    }
    
    public static void main(String[] args) {
        var nanoTimer = new NanoTimer();
        var sudokuProblem = new SudokuProblem();
        var sudokuBoard = new SudokuGenerator().generateRandomSolvableSudokuBoard();
    
        System.out.println("Starting with board: ");
        System.out.println(sudokuBoard);
        
        nanoTimer.start();
        var population = sudokuProblem.generatePopulationFromIndividual(100, sudokuBoard);
        var solution = sudokuProblem.optimize(population, 10000, .05, .25);
        nanoTimer.stop();
        
        System.out.println();
        System.out.println("Solution for board found in " + nanoTimer.toString());
        System.out.println(solution);
        System.out.println("Fitness of Solution: " + sudokuProblem.fitness(solution));
    }
}
