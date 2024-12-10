package com.expedient.adventofcodejade.solutions.year2024;

import com.expedient.adventofcodejade.BaseSolution;
import com.expedient.adventofcodejade.common.Grid;
import com.expedient.adventofcodejade.common.PuzzleInput;

import java.util.Set;

public class SolutionDay10 extends BaseSolution {
  public SolutionDay10(PuzzleInput input, PuzzleInput sampleInputOne, PuzzleInput sampleInputTwo) {
    super(input, sampleInputOne, sampleInputTwo);
  }

  /**
   * Finds the sum of the score of each trailhead found in the input Grid
   *
   * @param input the PuzzleInput to be used for the solution
   * @return the sum of the scores of each trailhead
   */
  @Override
  public Integer partOne(PuzzleInput input) {
    var grid = input.getGrid();
    return grid.matchCoordinates(i -> i == '0').stream()
        .map(i -> Grid.findTrailStepScore(grid, i, 0))
        .map(Set::size)
        .reduce(Integer::sum)
        .orElseThrow();
  }

  /**
   * Finds the sum of the number of unique trails that can be taken from each trailhead found in the
   * given input Grid
   *
   * @param input the PuzzleInput to be used for the solution
   * @return the sum of the scores of each trailhead
   */
  @Override
  public Integer partTwo(PuzzleInput input) {
    var grid = input.getGrid();
    return grid.matchCoordinates(i -> i == '0').stream()
        .map(i -> Grid.findTrailStepUnique(grid, i, 0))
        .reduce(Integer::sum)
        .orElseThrow();
  }
}
