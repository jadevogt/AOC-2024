package com.expedient.adventofcodejade.solutions.year2024;

import com.expedient.adventofcodejade.BaseSolution;
import com.expedient.adventofcodejade.common.*;
import java.util.*;

public class SolutionDay18 extends BaseSolution {
  public SolutionDay18(PuzzleInput input, PuzzleInput sampleInputOne, PuzzleInput sampleInputTwo) {
    super(input, sampleInputOne, sampleInputTwo);
  }

  /**
   * Given a Character, returns true if the character is considered traversable in the maze
   *
   * @param c Character from the maze
   * @return whether the Character is traversable
   */
  public static boolean isTraversable(Character c) {
    return c != '#';
  }

  /**
   * Given the Grid, current position, and a Set of previously visited Coordinate, Direction pairs,
   * find neighboring Coordinates and their associated weights (how much traversing to them will
   * impact the score)
   *
   * @param grid Character Grid derived from the input
   * @param current the current location
   * @param visited Set of visited Coordinate, Direction pairs
   * @return a list of Coordinate, Integer pairs representing neighboring vertices and their weights
   */
  public static List<Pair<Coordinate, Integer>> getNeighbors(
      Grid<Character> grid, Coordinate current, Set<Coordinate> visited) {
    List<Coordinate> directNeighbors =
        grid.matchNeighbors(current, SolutionDay18::isTraversable, true);
    directNeighbors = directNeighbors.stream().filter(c -> !visited.contains(c)).toList();
    List<Pair<Coordinate, Integer>> weightedNeighbors = new ArrayList<>();
    for (Coordinate neighbor : directNeighbors) {
      int cost = 2;
      weightedNeighbors.add(new Pair<>(neighbor, cost));
    }
    return weightedNeighbors;
  }

  /**
   * Use Dijkstra's Algorithm to find all possible shortest paths through the maze. Uses the weights
   * from the getNeighbors method
   *
   * @param grid Character Grid derived from the puzzle input
   * @param startPoint Coordinate that we start at
   * @return Map containing all Coordinates in the maze, each with a List of Coordinates leading to
   *     that Coordinate along optimal paths
   */
  public Map<Coordinate, Coordinate> findBestMazeSolution(
      Grid<Character> grid, Coordinate startPoint) {
    HashSet<Coordinate> visited = new HashSet<>();
    Queue<Pair<Coordinate, Integer>> queue =
        new PriorityQueue<>(new SolutionDay18.NeighborComparator());

    HashMap<Coordinate, Integer> distance = new HashMap<>();
    List<Coordinate> mazeCoords = grid.matchCoordinates(SolutionDay18::isTraversable);
    for (Coordinate c : mazeCoords) {
      if (c.equals(startPoint)) {
        distance.put(startPoint, 0);
      } else {
        distance.put(c, Integer.MAX_VALUE);
      }
    }

    Map<Coordinate, Coordinate> previous = new HashMap<>();
    for (Coordinate c : mazeCoords) {
      previous.put(c, null);
    }

    Coordinate currentPosition;
    queue.add(new Pair<>(startPoint, 0));
    while (!queue.isEmpty()) {
      Pair<Coordinate, Integer> current = queue.poll();
      currentPosition = current.one();
      int queuedDistance = current.two();
      if (queuedDistance > distance.get(currentPosition)) {
        continue;
      }
      List<Pair<Coordinate, Integer>> neighbors = getNeighbors(grid, currentPosition, visited);
      for (Pair<Coordinate, Integer> neighbor : neighbors) {
        if (neighbor.one().equals(startPoint)) continue;
        int newDistance = distance.get(currentPosition) + neighbor.two();
        if (newDistance < distance.get(neighbor.one())) {
          distance.put(neighbor.one(), newDistance);
          previous.put(neighbor.one(), currentPosition);
          queue.add(new Pair<>(neighbor.one(), newDistance));
        }
      }
      visited.add(currentPosition);
    }
    return previous;
  }

  public Grid<Character> mazeForPointInTime(
      Grid<Character> field, List<Coordinate> input, int numNanoSecs) {
    var gridCopy = field.duplicate();
    for (int i = 0; i < numNanoSecs; i++) {
      gridCopy.set(input.get(i), '#');
    }
    return gridCopy;
  }

  @Override
  public Object partOne(PuzzleInput input) {
    List<String> l = new ArrayList<>();
    int rowCount = input.isTest() ? 6 : 70;
    int colCount = input.isTest() ? 6 : 70;
    int step = input.isTest() ? 12 : 1024;
    for (int i = 0; i < rowCount + 1; i++) {
      l.add(".".repeat(colCount + 1));
    }
    Grid<Character> field = Grid.fromStringList(l);
    List<Coordinate> in =
        input.getLines().stream()
            .map(s -> s.split(","))
            .map(s -> new Coordinate(Integer.parseInt(s[1]), Integer.parseInt(s[0])))
            .toList();
    Coordinate startPoint = new Coordinate(0, 0);
    var g = mazeForPointInTime(field, in, step);
    var prev = findBestMazeSolution(g, startPoint);
    List<Coordinate> pathTaken = new ArrayList<>();
    Coordinate currentPosition = new Coordinate(rowCount, colCount);
    while (!currentPosition.equals(startPoint)) {
      Coordinate current = prev.get(currentPosition);
      pathTaken.add(currentPosition);
      currentPosition = current;
    }

    int total = 0;
    for (Coordinate _ : pathTaken) {
      total++;
    }
    return total;
  }

  @Override
  public Object partTwo(PuzzleInput input) {
    List<String> l = new ArrayList<>();
    int rowCount = input.isTest() ? 6 : 70;
    int colCount = input.isTest() ? 6 : 70;
    int step = input.isTest() ? 12 : 1024;
    for (int i = 0; i < rowCount + 1; i++) {
      l.add(".".repeat(colCount + 1));
    }
    Grid<Character> field = Grid.fromStringList(l);
    List<Coordinate> in =
        input.getLines().stream()
            .map(s -> s.split(","))
            .map(s -> new Coordinate(Integer.parseInt(s[1]), Integer.parseInt(s[0])))
            .toList();
    Coordinate startPoint = new Coordinate(0, 0);
    // we can start at 1024 because we know it's always possible at 1024
    for (int i = step; i < in.size(); i++) {
      var g = mazeForPointInTime(field, in, i);
      var prev = findBestMazeSolution(g, startPoint);
      Coordinate currentPosition = new Coordinate(rowCount, colCount);
      while (!currentPosition.equals(startPoint)) {
        Coordinate current = prev.get(currentPosition);
        // if we failed to solve the maze, the previous entry was the solution
        if (current == null) {
          return String.format("%d,%d", in.get(i - 1).col(), in.get(i - 1).row());
        }
        currentPosition = current;
      }
    }
    return null;
  }

  /** Sorts two Coordinate, Integer pairs based on the Integer value (score) in contained in each */
  public static class NeighborComparator implements Comparator<Pair<Coordinate, Integer>> {
    @Override
    public int compare(Pair<Coordinate, Integer> o1, Pair<Coordinate, Integer> o2) {
      if (o1.two() < o2.two()) {
        return -1;
      } else if (o1.two() > o2.two()) {
        return 1;
      }
      return 0;
    }
  }
}
