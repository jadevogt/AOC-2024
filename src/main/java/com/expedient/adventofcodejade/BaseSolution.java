package com.expedient.adventofcodejade;

import com.expedient.adventofcodejade.common.PuzzleInput;

/**
 * Base class for all Solutions. Provides input, sample input, and methods to run part one and part two.
 */
public abstract class BaseSolution {
    private final PuzzleInput input;
    private final PuzzleInput sampleInput;

    public BaseSolution(PuzzleInput input, PuzzleInput sampleInput) {
       this.input = input;
       this.sampleInput = sampleInput;
    }

    /**
     * Returns either the input or sample input depending on whether this is a test run
     * @param test whether this is a test run
     * @return the corresponding input
     */
    public PuzzleInput getInput(boolean test) {
        return test ? sampleInput : input;
    }

    /**
     * Runs parts one and two of the solution, and prints the results.
     * @param test whether to use test inputs
     */
    public void run(boolean test) {
        String pfx = test ? "output (sample)" : "output";
        System.out.printf("Part one %s: %s%n", pfx, partOne(getInput(test)));
        System.out.printf("Part two %s: %s%n", pfx, partTwo(getInput(test)));
    }

    /**
     * Logic for part one of the solution. Must be overridden when implementing the solution.
     * @param input the PuzzleInput to be used for the solution
     * @return output of this part of the solution
     */
    public abstract String partOne(PuzzleInput input);

    /**
     * Logic for part two of the solution. Must be overridden when implementing the solution.
     * @param input the PuzzleInput to be used for the solution
     * @return output of this part of the solution
     */
    public abstract String partTwo(PuzzleInput input);
}