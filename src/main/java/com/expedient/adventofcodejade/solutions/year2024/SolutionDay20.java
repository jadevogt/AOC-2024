package com.expedient.adventofcodejade.solutions.year2024;

import com.expedient.adventofcodejade.BaseSolution;
import com.expedient.adventofcodejade.Triplet;
import com.expedient.adventofcodejade.common.Coordinate;
import com.expedient.adventofcodejade.common.Grid;
import com.expedient.adventofcodejade.common.Pair;
import com.expedient.adventofcodejade.common.PuzzleInput;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.Stream;

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

  public int findNumGoodCheats(
      Grid<Character> maze, Map<Coordinate, Integer> mazePath, int magnitude) {
    var matches = maze.matchCoordinates(c -> c == '#');
    Set<Set<Coordinate>> cheats = new HashSet<>();
    for (var match : matches) {
      for (var neighbor : maze.matchNeighbors(match, c -> c != '#', true)) {
        if (mazePath.get(neighbor) == null
            || mazePath.get(match.inOppositeDirection(neighbor)) == null) continue;
        if (Math.abs(mazePath.get(match.inOppositeDirection(neighbor)) - mazePath.get(neighbor)) - 2
            >= magnitude) {
          cheats.add(
              Stream.of(match.inOppositeDirection(neighbor), neighbor).collect(Collectors.toSet()));
        }
      }
    }
    return cheats.size();
  }

  public int findNumGoodCheatsLong(
      Grid<Character> maze, Map<Coordinate, Integer> mazePath, int magnitude) {
    var startPoints = maze.matchCoordinates(c -> c == '.' || c == 'S' || c == 'E');
    Set<Set<Coordinate>> cheats = new HashSet<>();
    for (var startPoint : startPoints) {
      if (mazePath.get(startPoint) == null) continue;
      var endPoints =
          maze.matchCoordinatesByCoordinate(c -> startPoint.taxiCabDistance(c) <= 20).stream()
              .filter(c -> maze.at(c) != '#')
              .toList();
      for (var endPoint : endPoints) {
        if (mazePath.get(endPoint) == null) continue;
        if (Math.abs(mazePath.get(startPoint) - mazePath.get(endPoint))
                - startPoint.taxiCabDistance(endPoint)
            >= magnitude) {
          cheats.add(Stream.of(startPoint, endPoint).collect(Collectors.toSet()));
        }
      }
    }
    return cheats.size();
  }

  @Override
  public Object partOne(PuzzleInput input) {
    var grid = input.getGrid();
    var mazePath = traverseMaze(input.getGrid());
    return findNumGoodCheats(grid, mazePath, 100);
  }

  @Override
  public Object partTwo(PuzzleInput input) {
    var grid = input.getGrid();
    var mazePath = traverseMaze(input.getGrid());
    return findNumGoodCheatsLong(grid, mazePath, 100);
  }
}
