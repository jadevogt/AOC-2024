package com.expedient.adventofcodejade.solutions.year2024;

import com.expedient.adventofcodejade.BaseSolution;
import com.expedient.adventofcodejade.common.Coordinate;
import com.expedient.adventofcodejade.common.PuzzleInput;
import com.expedient.adventofcodejade.common.Grid;
import java.util.List;

public class SolutionDay4 extends BaseSolution {
  public SolutionDay4(PuzzleInput input, PuzzleInput sampleInputOne, PuzzleInput sampleInputTwo) {
    super(input, sampleInputOne, sampleInputTwo);
  }

  /**
   * Find all 'X' Characters in the input grid, then find surrounding 'M' Characters for each one.
   * After this, continue in the same direction as the 'M' to look for 'A' and then 'S'. Count each
   * full occurrence.
   *
   * @param input the PuzzleInput to be used for the solution
   * @return string with the number of times that XMAS was found in the input
   */
  @Override
  public String partOne(PuzzleInput input) {
    Grid<Character> wordSearch = input.getGrid();
    List<Coordinate> starts = wordSearch.matchCoordinates(i -> i == 'X');
    int count = 0;
    for (Coordinate start : starts) {
      List<Coordinate> potentials = wordSearch.matchNeighbors(start, i -> i == 'M');
      for (Coordinate potential : potentials) {
        try {
          Coordinate aLetter = start.nextInDirection(potential);
          if (wordSearch.at(aLetter) == 'A') {
            Coordinate sLetter = potential.nextInDirection(aLetter);
            if (wordSearch.at(sLetter) == 'S') {
              count++;
            }
          }
        } catch (ArrayIndexOutOfBoundsException ignored) {
        }
      }
    }
    return Integer.toString(count);
  }

  /**
   * Find each 'A' Character in the input grid. For each one, find non-orthogonal neighbors that are
   * 'M' characters. Then, for each 'M' neighbor, check for 'S' in the opposite direction. If there
   * are two 'MAS' sequences, add to the count.
   *
   * @param input the PuzzleInput to be used for the solution
   * @return string with the number of times X-MASes were found in the input
   */
  @Override
  public String partTwo(PuzzleInput input) {
    Grid<Character> wordSearch = input.getGrid();
    List<Coordinate> starts = wordSearch.matchCoordinates(i -> i == 'A');
    int count = 0;
    for (Coordinate start : starts) {
      List<Coordinate> potentials = wordSearch.matchNeighbors(start, i -> i == 'M');
      potentials = potentials.stream().filter(x -> !x.isOrthogonal(start)).toList();
      boolean found = false;
      for (Coordinate potential : potentials) {
        try {
          Coordinate sLetter = start.inOppositeDirection(potential);
          if (wordSearch.at(sLetter) == 'S') {
            if (found) {
              count++;
            } else {
              found = true;
            }
          }
        } catch (ArrayIndexOutOfBoundsException ignored) {
        }
      }
    }
    return Integer.toString(count);
  }
}
