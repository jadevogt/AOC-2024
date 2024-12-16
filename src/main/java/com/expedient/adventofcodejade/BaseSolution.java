package com.expedient.adventofcodejade;

import com.expedient.adventofcodejade.common.PuzzleInput;
import java.time.Duration;
import java.time.Instant;

/**
 * Base class for all Solutions. Provides input, sample input, and methods to run part one and part
 * two.
 */
public abstract class BaseSolution {
  private final PuzzleInput input;
  private final PuzzleInput sampleInputOne;
  private final PuzzleInput sampleInputTwo;

  public BaseSolution(PuzzleInput input, PuzzleInput sampleInputOne, PuzzleInput sampleInputTwo) {
    this.input = input;
    this.sampleInputOne = sampleInputOne;
    this.sampleInputTwo = sampleInputTwo;
  }

  /**
   * Returns either the input or sample input depending on whether this is a test run
   *
   * @param test whether this is a test run
   * @param partOne whether this is for part one
   * @return the corresponding input
   */
  public PuzzleInput getInput(boolean test, boolean partOne) {
    if (test || input == null) {
      return partOne ? sampleInputOne : sampleInputTwo;
    }
    return input;
  }

  /**
   * Runs parts one and two of the solution, and prints the results.
   *
   * @param test whether to use test inputs
   */
  public void run(boolean test, boolean metrics) {
    String pfx = test ? "output (sample)" : "output";
    Instant startOne = Instant.now();
    var partOneSolution = partOne(getInput(test, true));
    Instant endOne = Instant.now();
    if (!metrics) {
      System.out.printf("Part one %s: %s%n", pfx, partOneSolution);
    } else {
      var time = Duration.between(startOne, endOne);
      System.out.printf("Part one %s: %s (took %s)%n", pfx, partOneSolution, time);
    }
    Instant startTwo = Instant.now();
    var partTwoSolution = partTwo(getInput(test, false));
    Instant endTwo = Instant.now();
    if (!metrics) {
      System.out.printf("Part two %s: %s%n", pfx, partTwoSolution);
    } else {
      var time = Duration.between(startTwo, endTwo);
      System.out.printf("Part two %s: %s (took %s)%n", pfx, partTwoSolution, time);
    }
  }

  /**
   * Logic for part one of the solution. Must be overridden when implementing the solution.
   *
   * @param input the PuzzleInput to be used for the solution
   * @return output of this part of the solution
   */
  public abstract Object partOne(PuzzleInput input);

  /**
   * Logic for part two of the solution. Must be overridden when implementing the solution.
   *
   * @param input the PuzzleInput to be used for the solution
   * @return output of this part of the solution
   */
  public abstract Object partTwo(PuzzleInput input);
}
