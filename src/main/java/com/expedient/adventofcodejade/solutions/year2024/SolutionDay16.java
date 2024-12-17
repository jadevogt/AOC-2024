package com.expedient.adventofcodejade.solutions.year2024;

import com.expedient.adventofcodejade.BaseSolution;
import com.expedient.adventofcodejade.Triplet;
import com.expedient.adventofcodejade.common.*;

import java.util.*;
import java.util.stream.Stream;

public class SolutionDay16 extends BaseSolution {

  public SolutionDay16(PuzzleInput input, PuzzleInput sampleInputOne, PuzzleInput sampleInputTwo) {
    super(input, sampleInputOne, sampleInputTwo);
  }

  /**
   * Given a Character, returns true if the character is considered traversable in the maze
   *
   * @param c Character from the maze
   * @return whether the Character is traversable
   */
  public static boolean isTraversable(Character c) {
    return c == '.' || c == 'S' || c == 'E';
  }

  /**
   * Given the Grid, current position, current direction, and a Set of previously visited
   * Coordinate, Direction pairs, find neighboring Coordinates and their associated weights (how
   * much traversing to them will impact the score)
   *
   * @param grid Character Grid derived from the input
   * @param current the current location
   * @param currentDirection the current direction
   * @param visited Set of visited Coordinate, Direction pairs
   * @return a list of Coordinate, Integer pairs representing neighboring vertices and their weights
   */
  public static List<Pair<Coordinate, Integer>> getNeighbors(
      Grid<Character> grid,
      Coordinate current,
      Direction currentDirection,
      Set<Pair<Coordinate, Direction>> visited) {
    List<Coordinate> directNeighbors =
        grid.matchNeighbors(current, SolutionDay16::isTraversable, true);
    directNeighbors =
        directNeighbors.stream()
            .filter(c -> !visited.contains(new Pair<>(c, current.directionToCoordinate(c))))
            .toList();
    List<Pair<Coordinate, Integer>> weightedNeighbors = new ArrayList<>();
    for (Coordinate neighbor : directNeighbors) {
      int cost = 2;
      if (((currentDirection == Direction.RIGHT || currentDirection == Direction.LEFT)
              && neighbor.row() != current.row())
          || ((currentDirection == Direction.UP || currentDirection == Direction.DOWN)
              && neighbor.col() != current.col())) {
        cost += 1000;
      }
      weightedNeighbors.add(new Pair<>(neighbor, cost));
    }
    return weightedNeighbors;
  }

  /**
   * Sorts two Coordinate, Integer, Direction triplets based on the Integer value (score) in
   * contained in each
   */
  public static class NeighborComparator
      implements Comparator<Triplet<Coordinate, Integer, Direction>> {
    @Override
    public int compare(
        Triplet<Coordinate, Integer, Direction> o1, Triplet<Coordinate, Integer, Direction> o2) {
      if (o1.two() < o2.two()) {
        return -1;
      } else if (o1.two() > o2.two()) {
        return 1;
      }
      return 0;
    }
  }

  /**
   * Use Dijkstra's Algorithm to find all possible shortest paths through the maze. Uses the weights
   * from the getNeighbors method to account for the fact that turns matter more than steps
   *
   * @param grid Character Grid derived from the puzzle input
   * @param startPoint Coordinate that we start at
   * @return Map containing all Coordinates in the maze, each with a List of Coordinate, Direction
   *     pairs that leading to that Coordinate along optimal paths
   */
  public Map<Pair<Coordinate, Direction>, List<Pair<Coordinate, Direction>>> findAllMazeSolutions(
      Grid<Character> grid, Coordinate startPoint) {
    HashSet<Pair<Coordinate, Direction>> visited = new HashSet<>();
    Queue<Triplet<Coordinate, Integer, Direction>> queue =
        new PriorityQueue<>(new NeighborComparator());

    HashMap<Pair<Coordinate, Direction>, Integer> distance = new HashMap<>();
    List<Coordinate> mazeCoords = grid.matchCoordinates(SolutionDay16::isTraversable);
    for (Coordinate c : mazeCoords) {
      if (c.equals(startPoint)) {
        distance.put(new Pair<>(startPoint, Direction.RIGHT), 0);
      } else {
        for (Direction d :
            Stream.of(Direction.UP, Direction.DOWN, Direction.LEFT, Direction.RIGHT).toList())
          distance.put(new Pair<>(c, d), Integer.MAX_VALUE / 2);
      }
    }

    Map<Pair<Coordinate, Direction>, List<Pair<Coordinate, Direction>>> previous = new HashMap<>();
    for (Coordinate c : mazeCoords) {
      for (Direction d :
          Stream.of(Direction.UP, Direction.DOWN, Direction.LEFT, Direction.RIGHT).toList()) {
        previous.put(new Pair<>(c, d), new ArrayList<>());
      }
    }

    Coordinate currentPosition;
    Direction currentDirection;
    queue.add(new Triplet<>(startPoint, 0, Direction.RIGHT));
    while (!queue.isEmpty()) {
      Triplet<Coordinate, Integer, Direction> current = queue.poll();
      currentPosition = current.one();
      currentDirection = current.three();
      int queuedDistance = current.two();
      if (queuedDistance > distance.get(new Pair<>(currentPosition, currentDirection))) {
        continue;
      }
      List<Pair<Coordinate, Integer>> neighbors =
          getNeighbors(grid, currentPosition, currentDirection, visited);
      for (Pair<Coordinate, Integer> neighbor : neighbors) {
        if (neighbor.one().equals(startPoint)) continue;
        Direction newDirection = currentPosition.directionToCoordinate(neighbor.one());
        int newDistance =
            distance.get(new Pair<>(currentPosition, currentDirection)) + neighbor.two();
        if (newDistance <= distance.get(new Pair<>(neighbor.one(), newDirection))) {
          if (newDistance < distance.get(new Pair<>(neighbor.one(), newDirection))) {
            distance.put(new Pair<>(neighbor.one(), newDirection), newDistance);
            previous.put(new Pair<>(neighbor.one(), newDirection), new ArrayList<>());
          }
          List<Pair<Coordinate, Direction>> list =
              previous.get(new Pair<>(neighbor.one(), newDirection));
          list.add(new Pair<>(currentPosition, currentDirection));
          queue.add(new Triplet<>(neighbor.one(), newDistance, newDirection));
        }
      }
      visited.add(new Pair<>(currentPosition, currentDirection));
    }
    return previous;
  }

  /**
   * Represents parsed grid (maze), starting point, and ending point derived from Puzzle Input
   *
   * @param grid
   * @param startPoint
   * @param endPoint
   */
  public record SolutionDay16Input(
      Grid<Character> grid, Coordinate startPoint, Coordinate endPoint) {
    public static SolutionDay16Input fromPuzzleInput(PuzzleInput input) {
      Grid<Character> grid = Grid.fromStringList(input.getLines());
      Coordinate endPoint = grid.matchCoordinates(c -> c == 'E').getFirst();
      Coordinate startPoint = grid.matchCoordinates(c -> c == 'S').getFirst();
      return new SolutionDay16Input(grid, startPoint, endPoint);
    }
  }

  /**
   * Find one of the shortest possible paths through the maze with the given parameters, and then
   * calculate its score.
   *
   * @param input the PuzzleInput to be used for the solution
   * @return the score of one of the shortest paths through the maze
   */
  @Override
  public Object partOne(PuzzleInput input) {
    SolutionDay16Input in = SolutionDay16Input.fromPuzzleInput(input);
    Map<Pair<Coordinate, Direction>, List<Pair<Coordinate, Direction>>> previous =
        findAllMazeSolutions(in.grid(), in.startPoint());
    Coordinate currentPosition = in.endPoint();
    Direction currentDirection;
    List<Pair<Coordinate, Direction>> pathTaken = new ArrayList<>();
    // we don't know what direction the path came from, so just try all of them I guess
    for (Direction d :
        Stream.of(Direction.UP, Direction.DOWN, Direction.LEFT, Direction.RIGHT).toList()) {
      try {
        currentDirection = d;
        while (!currentPosition.equals(in.startPoint())) {
          List<Pair<Coordinate, Direction>> current =
              previous.get(new Pair<>(currentPosition, currentDirection));
          pathTaken.add(new Pair<>(currentPosition, currentDirection));
          currentPosition = current.getFirst().one();
          currentDirection = current.getFirst().two();
        }
        break;
      } catch (NullPointerException ignored) {
      }
    }

    Direction cd = Direction.RIGHT;
    int total = 0;
    for (Pair<Coordinate, Direction> p : pathTaken) {
      if (p.two() != cd) {
        total += 1000;
        cd = p.two();
      }
      total++;
    }
    return total;
  }

  /**
   * Find all shortest paths through the maze, and then figure out how many coordinates (seats) lie
   * along those paths
   *
   * @param input the PuzzleInput to be used for the solution
   * @return the number of seats that lie along the path
   */
  @Override
  public Object partTwo(PuzzleInput input) {
    SolutionDay16Input in = SolutionDay16Input.fromPuzzleInput(input);
    Map<Pair<Coordinate, Direction>, List<Pair<Coordinate, Direction>>> previous =
        findAllMazeSolutions(in.grid(), in.startPoint());
    Set<Coordinate> seats = new HashSet<>();
    Coordinate currentPosition = in.endPoint();
    Direction currentDirection;
    for (Direction d :
        Stream.of(Direction.UP, Direction.DOWN, Direction.LEFT, Direction.RIGHT).toList()) {
      Queue<Pair<Coordinate, Direction>> toCheck = new LinkedList<>();
      Set<Pair<Coordinate, Direction>> alreadyChecked = new HashSet<>();
      toCheck.add(new Pair<>(currentPosition, d));
      while (!toCheck.isEmpty()) {
        Pair<Coordinate, Direction> c = toCheck.poll();
        if (alreadyChecked.contains(c)) continue;
        alreadyChecked.add(c);
        currentPosition = c.one();
        currentDirection = c.two();
        seats.add(currentPosition);
        if (currentPosition == in.startPoint()) {
          continue;
        }
        List<Pair<Coordinate, Direction>> stepList =
            previous.get(new Pair<>(currentPosition, currentDirection));
        for (Pair<Coordinate, Direction> step : stepList) {
          toCheck.add(new Pair<>(step.one(), step.two()));
        }
      }
    }
    return seats.size();
  }
}
