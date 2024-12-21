package com.expedient.adventofcodejade.solutions.year2024;

import com.expedient.adventofcodejade.BaseSolution;
import com.expedient.adventofcodejade.common.Coordinate;
import com.expedient.adventofcodejade.common.Grid;
import com.expedient.adventofcodejade.common.PuzzleInput;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

public class SolutionDay20 extends BaseSolution {
  public SolutionDay20(PuzzleInput input, PuzzleInput sampleInputOne, PuzzleInput sampleInputTwo) {
    super(input, sampleInputOne, sampleInputTwo);
  }

  /**
   * Moves through the track, building a Map with each spot and its distance from the goal
   *
   * @param grid the input Grid, containing characters describing a track with a Start and End
   * @return a Map that associates each point on the track with its distance from the end
   */
  public Map<Coordinate, Integer> traverseMaze(Grid<Character> grid) {
    var mazePath = new HashMap<Coordinate, Integer>();
    var currentCoordinate = grid.matchCoordinates(c -> c == 'S').getFirst();
    var endCoordinate = grid.matchCoordinates(c -> c == 'E').getFirst();
    mazePath.put(currentCoordinate, 0);
    int total = 0;
    Set<Coordinate> visited = new HashSet<>();
    while (!currentCoordinate.equals(endCoordinate)) {
      visited.add(currentCoordinate);
      currentCoordinate =
          grid.matchNeighbors(currentCoordinate, c -> c != '#', true).stream()
              .filter(c -> !visited.contains(c))
              .findFirst()
              .orElseThrow();
      total++;
      mazePath.put(currentCoordinate, total);
    }
    return mazePath;
  }

  /**
   * Finds the number of "cheats" that can be taken through the track which provide a time
   * improvement of at least the given magnitude
   *
   * @param maze the Grid derived from the input
   * @param mazePath Map containing each point on the track and its associated distance from end
   * @param magnitude the amount of time the cheat must save to be counted
   * @param cheatLength how long the cheat is activated for (2 for part 1, 20 for part 2)
   * @return the number of cheats that fit the conditions
   */
  public int findCheats(
      Grid<Character> maze, Map<Coordinate, Integer> mazePath, int magnitude, int cheatLength) {
    var startPoints = maze.matchCoordinates(c -> c == '.' || c == 'S' || c == 'E');
    Set<Set<Coordinate>> cheats = new HashSet<>(mazePath.size() * 2);
    for (var startPoint : startPoints) {
      int startDistance = mazePath.get(startPoint);
      cheats.addAll(
          maze.coordinatesWithinTaxicabDistance(startPoint, cheatLength, c -> c != '#').stream()
              .filter(
                  c -> {
                    var endDistance = mazePath.get(c);
                    var dist = startPoint.taxiCabDistance(c);
                    if (endDistance == null) return false;
                    int Saved = Math.abs(startDistance - endDistance) - dist;
                    return (dist <= cheatLength && Saved >= magnitude);
                  })
              .map(c -> Set.of(startPoint, c))
              .collect(Collectors.toSet()));
    }
    return cheats.size();
  }

  /**
   * Finds the number of 2ns cheats that can be taken that save at least 100 ns (or 2 if we're using
   * the sample input)
   *
   * @param input the PuzzleInput to be used for the solution
   * @return the number of cheats
   */
  @Override
  public Integer partOne(PuzzleInput input) {
    var grid = input.getGrid();
    var mazePath = traverseMaze(input.getGrid());
    int magnitude = input.isTest() ? 2 : 100;
    return findCheats(grid, mazePath, magnitude, 2);
  }

  /**
   * Finds the number of 20ns cheats that can be taken that save at least 100 ns (or 2 if we're
   * using the sample input)
   *
   * @param input the PuzzleInput to be used for the solution
   * @return the number of cheats
   */
  @Override
  public Integer partTwo(PuzzleInput input) {
    var grid = input.getGrid();
    var mazePath = traverseMaze(input.getGrid());
    int magnitude = input.isTest() ? 50 : 100;
    return findCheats(grid, mazePath, magnitude, 20);
  }
}
