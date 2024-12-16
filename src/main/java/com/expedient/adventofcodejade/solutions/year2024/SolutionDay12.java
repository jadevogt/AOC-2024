package com.expedient.adventofcodejade.solutions.year2024;

import com.expedient.adventofcodejade.BaseSolution;
import com.expedient.adventofcodejade.common.Coordinate;
import com.expedient.adventofcodejade.common.Grid;
import com.expedient.adventofcodejade.common.PuzzleInput;
import java.util.HashSet;
import java.util.Set;

public class SolutionDay12 extends BaseSolution {
  public SolutionDay12(PuzzleInput input, PuzzleInput sampleInputOne, PuzzleInput sampleInputTwo) {
    super(input, sampleInputOne, sampleInputTwo);
  }

  @Override
  public Object partOne(PuzzleInput input) {
    Grid<Character> grid = input.getGrid();
    Set<Coordinate> prev = new HashSet<>();
    int total = 0;
    for (int i = 0; i < grid.rowCount(); i++) {
      for (int j = 0; j < grid.colCount(); j++) {
        final int row = i;
        final int col = j;
        if (prev.contains(new Coordinate(row, col))) {
          continue;
        }
        int perimeter =
            grid.findContiguousRegionPerimeter(
                new Coordinate(row, col), q -> q == grid.at(row, col), prev);
        int area =
            grid.findContiguousRegionArea(
                new Coordinate(row, col), q -> q == grid.at(row, col), null);
        total += (perimeter * area);
      }
    }
    return total;
  }

  @Override
  public Object partTwo(PuzzleInput input) {
    Grid<Character> grid = input.getGrid();
    Set<Coordinate> prev = new HashSet<>();
    int total = 0;
    for (int i = 0; i < grid.rowCount(); i++) {
      for (int j = 0; j < grid.colCount(); j++) {
        final int row = i;
        final int col = j;
        if (prev.contains(new Coordinate(row, col))) {
          continue;
        }
        int sideCount =
            grid.findContiguousRegionSides(
                new Coordinate(row, col), q -> q == grid.at(row, col), prev);
        int area =
            grid.findContiguousRegionArea(
                new Coordinate(row, col), q -> q == grid.at(row, col), null);

        total += (sideCount * area);
      }
    }
    return total;
  }
}
