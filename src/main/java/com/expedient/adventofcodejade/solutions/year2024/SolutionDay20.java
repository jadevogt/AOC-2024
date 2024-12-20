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

  public int findNumGoodCheatsLong(
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

  @Override
  public Object partOne(PuzzleInput input) {
    var grid = input.getGrid();
    var mazePath = traverseMaze(input.getGrid());
    int magnitude = input.isTest() ? 2 : 100;
    return findNumGoodCheatsLong(grid, mazePath, magnitude, 2);
  }

  @Override
  public Object partTwo(PuzzleInput input) {
    var grid = input.getGrid();
    var mazePath = traverseMaze(input.getGrid());
    int magnitude = input.isTest() ? 50 : 100;
    return findNumGoodCheatsLong(grid, mazePath, magnitude, 20);
  }
}
