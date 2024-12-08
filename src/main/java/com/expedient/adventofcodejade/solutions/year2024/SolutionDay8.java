package com.expedient.adventofcodejade.solutions.year2024;

import com.expedient.adventofcodejade.BaseSolution;
import com.expedient.adventofcodejade.common.Coordinate;
import com.expedient.adventofcodejade.common.Grid;
import com.expedient.adventofcodejade.common.PuzzleInput;
import com.expedient.adventofcodejade.common.Vector2;

import java.util.*;
import java.util.stream.Collectors;

public class SolutionDay8 extends BaseSolution {
  public SolutionDay8(PuzzleInput input, PuzzleInput sampleInputOne, PuzzleInput sampleInputTwo) {
    super(input, sampleInputOne, sampleInputTwo);
  }

  /**
   * Finds the anti-nodes in the grid using the given antenna lists, and then provides the count
   *
   * @param grid the input Grid
   * @param antennaLists lists of antennas per Character antenna type
   * @param partTwo whether this is part two
   * @return the count of anti-nodes
   */
  public long getAntiNodeCount(
      Grid<Character> grid, Map<Character, List<Coordinate>> antennaLists, boolean partTwo) {
    Map<Character, Set<Coordinate>> antiNodes = new HashMap<>();
    for (Character antennaType : antennaLists.keySet()) {
      for (int i = 0; i < antennaLists.get(antennaType).size() - 1; i++) {
        for (int j = i + 1; j < antennaLists.get(antennaType).size(); j++) {
          Coordinate antennaOne = antennaLists.get(antennaType).get(i);
          Coordinate antennaTwo = antennaLists.get(antennaType).get(j);
          for (int multiple = partTwo ? 0 : 1; multiple < (partTwo ? 100 : 2); multiple++) {
            Coordinate antiNodeOne =
                new Vector2(antennaOne, antennaTwo).multiply(multiple).reverse().apply(antennaTwo);
            Coordinate antiNodeTwo =
                new Vector2(antennaTwo, antennaOne).multiply(multiple).reverse().apply(antennaOne);
            if (!antiNodes.containsKey(antennaType)
                && (grid.isSafe(antiNodeOne) || grid.isSafe(antiNodeTwo))) {
              antiNodes.put(antennaType, new HashSet<>());
            }
            if (grid.isSafe(antiNodeOne)) antiNodes.get(antennaType).add(antiNodeOne);
            if (grid.isSafe(antiNodeTwo)) antiNodes.get(antennaType).add(antiNodeTwo);
          }
        }
      }
    }
    return antiNodes.keySet().stream()
        .flatMap(i -> antiNodes.get(i).stream())
        .collect(Collectors.toSet())
        .size();
  }

  /**
   * Finds each type of antenna on the given input Grid, as well as the Coordinate for each antenna
   * of each kind
   *
   * @param grid the input Grid
   * @return Map of Lists of Coordinate per Character antenna type
   */
  public static Map<Character, List<Coordinate>> getAntennaLists(Grid<Character> grid) {
    return grid.matchCoordinates(i -> i != '.').stream()
        .map(grid::at)
        .collect(Collectors.toSet())
        .stream()
        .collect(Collectors.toMap(i -> i, x -> grid.matchCoordinates(i -> i == x)));
  }

  @Override
  public Long partOne(PuzzleInput input) {
    Grid<Character> grid = input.getGrid();
    return getAntiNodeCount(grid, getAntennaLists(grid), false);
  }

  @Override
  public Long partTwo(PuzzleInput input) {
    Grid<Character> grid = input.getGrid();
    return getAntiNodeCount(grid, getAntennaLists(grid), true);
  }
}
