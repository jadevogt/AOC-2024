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

  public static boolean isTraversable(Character c) {
    return c == '.' || c == 'S' || c == 'E';
  }

  public static Direction directionFromTwoCoordinates(Coordinate c1, Coordinate c2) {
    Direction newDirection = Direction.RIGHT;
    if (c1.row() < c2.row()) {
      newDirection = Direction.DOWN;
    }
    if (c1.row() > c2.row()) {
      newDirection = Direction.UP;
    }
    if (c1.col() < c2.col()) {
      newDirection = Direction.RIGHT;
    }
    if (c1.col() > c2.col()) {
      newDirection = Direction.LEFT;
    }
    return newDirection;
  }

  public static Map<Coordinate, Integer> getNeighbors(
      Grid<Character> grid,
      Coordinate current,
      Direction currentDirection,
      Set<Pair<Coordinate, Direction>> visited) {
    var directNeighbors = grid.matchNeighbors(current, SolutionDay16::isTraversable, true);
    directNeighbors =
        directNeighbors.stream()
            .filter(c -> !visited.contains(new Pair<>(c, directionFromTwoCoordinates(current, c))))
            .toList();
    Map<Coordinate, Integer> weightedNeighbors = new HashMap<>();
    for (var neighbor : directNeighbors) {
      int cost = 2;
      if (((currentDirection == Direction.RIGHT || currentDirection == Direction.LEFT)
              && neighbor.row() != current.row())
          || ((currentDirection == Direction.UP || currentDirection == Direction.DOWN)
              && neighbor.col() != current.col())) {
        cost += 1000;
      }
      weightedNeighbors.put(neighbor, cost);
    }
    return weightedNeighbors;
  }

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

  @Override
  public Object partOne(PuzzleInput input) {
    Grid<Character> grid = Grid.fromStringList(input.getLines());
    Coordinate endPoint = grid.matchCoordinates(c -> c == 'E').getFirst();
    Coordinate startPoint = grid.matchCoordinates(c -> c == 'S').getFirst();
    List<Pair<Coordinate, Direction>> pathTaken = new ArrayList<>();
    HashSet<Pair<Coordinate, Direction>> visited = new HashSet<>();
    Queue<Triplet<Coordinate, Integer, Direction>> queue =
        new PriorityQueue<>(new NeighborComparator());

    HashMap<Pair<Coordinate, Direction>, Integer> distance = new HashMap<>();
    var mazeCoords = grid.matchCoordinates(SolutionDay16::isTraversable);
    for (var c : mazeCoords) {
      if (c.equals(startPoint)) {
        distance.put(new Pair<>(startPoint, Direction.RIGHT), 0);
      } else {
        for (Direction d :
            Stream.of(Direction.UP, Direction.DOWN, Direction.LEFT, Direction.RIGHT).toList())
          distance.put(new Pair<>(c, d), Integer.MAX_VALUE / 2);
      }
    }

    Map<Pair<Coordinate, Direction>, Pair<Coordinate, Direction>> previous = new HashMap<>();
    for (var c : mazeCoords) {
      for (Direction d :
          Stream.of(Direction.UP, Direction.DOWN, Direction.LEFT, Direction.RIGHT).toList()) {
        previous.put(new Pair<>(c, d), null);
      }
    }

    Coordinate currentPosition;
    Direction currentDirection;
    queue.add(new Triplet<>(startPoint, 0, Direction.RIGHT));
    while (!queue.isEmpty()) {
      var current = queue.poll();
      currentPosition = current.one();
      currentDirection = current.three();
      int queuedDistance = current.two();
      if (queuedDistance > distance.get(new Pair<>(currentPosition, currentDirection))) {
        continue;
      }
      var neighbors = getNeighbors(grid, currentPosition, currentDirection, visited);
      for (var neighbor : neighbors.keySet()) {
        if (neighbor.equals(startPoint)) continue;
        Direction newDirection = directionFromTwoCoordinates(currentPosition, neighbor);
        int newDistance =
            distance.get(new Pair<>(currentPosition, currentDirection)) + neighbors.get(neighbor);
        if (newDistance < distance.get(new Pair<>(neighbor, newDirection))) {
          distance.put(new Pair<>(neighbor, newDirection), newDistance);
          previous.put(
              new Pair<>(neighbor, newDirection), new Pair<>(currentPosition, currentDirection));
          queue.add(new Triplet<>(neighbor, newDistance, newDirection));
        }
      }
      visited.add(new Pair<>(currentPosition, currentDirection));
    }

    currentPosition = endPoint;
    currentDirection = Direction.DOWN;
    for (var d :
        Stream.of(Direction.UP, Direction.DOWN, Direction.LEFT, Direction.RIGHT).toList()) {
      try {
        currentDirection = d;
        while (!currentPosition.equals(startPoint)) {
          var current = previous.get(new Pair<>(currentPosition, currentDirection));
          pathTaken.add(new Pair<>(currentPosition, currentDirection));
          currentPosition = current.one();
          currentDirection = current.two();
        }
        break;
      } catch (NullPointerException ignored) {
      }
    }

    Direction cd = Direction.RIGHT;
    int total = 0;
    for (var p : pathTaken) {
      if (p.two() != cd) {
        total += 1000;
        cd = p.two();
      }
      total++;
    }
    return total;
  }

  @Override
  public Object partTwo(PuzzleInput input) {
    Grid<Character> grid = Grid.fromStringList(input.getLines());
    Coordinate endPoint = grid.matchCoordinates(c -> c == 'E').getFirst();
    Coordinate startPoint = grid.matchCoordinates(c -> c == 'S').getFirst();
    List<Pair<Coordinate, Direction>> pathTaken = new ArrayList<>();
    HashSet<Pair<Coordinate, Direction>> visited = new HashSet<>();
    Queue<Triplet<Coordinate, Integer, Direction>> queue =
        new PriorityQueue<>(new NeighborComparator());

    HashMap<Pair<Coordinate, Direction>, Integer> distance = new HashMap<>();
    var mazeCoords = grid.matchCoordinates(SolutionDay16::isTraversable);
    for (var c : mazeCoords) {
      if (c.equals(startPoint)) {
        distance.put(new Pair<>(startPoint, Direction.RIGHT), 0);
      } else {
        for (Direction d :
            Stream.of(Direction.UP, Direction.DOWN, Direction.LEFT, Direction.RIGHT).toList())
          distance.put(new Pair<>(c, d), Integer.MAX_VALUE / 2);
      }
    }

    Map<Pair<Coordinate, Direction>, List<Pair<Coordinate, Direction>>> previous = new HashMap<>();
    for (var c : mazeCoords) {
      for (Direction d :
          Stream.of(Direction.UP, Direction.DOWN, Direction.LEFT, Direction.RIGHT).toList()) {
        previous.put(new Pair<>(c, d), new ArrayList<>());
      }
    }

    Coordinate currentPosition;
    Direction currentDirection;
    queue.add(new Triplet<>(startPoint, 0, Direction.RIGHT));
    while (!queue.isEmpty()) {
      var current = queue.poll();
      currentPosition = current.one();
      currentDirection = current.three();
      int queuedDistance = current.two();
      if (queuedDistance > distance.get(new Pair<>(currentPosition, currentDirection))) {
        continue;
      }
      var neighbors = getNeighbors(grid, currentPosition, currentDirection, visited);
      for (var neighbor : neighbors.keySet()) {
        if (neighbor.equals(startPoint)) continue;
        Direction newDirection = directionFromTwoCoordinates(currentPosition, neighbor);
        int newDistance =
            distance.get(new Pair<>(currentPosition, currentDirection)) + neighbors.get(neighbor);
        if (newDistance <= distance.get(new Pair<>(neighbor, newDirection))) {
          if (newDistance < distance.get(new Pair<>(neighbor, newDirection))) {
            distance.put(new Pair<>(neighbor, newDirection), newDistance);
            previous.put(new Pair<>(neighbor, newDirection), new ArrayList<>());
          }
          var list = previous.get(new Pair<>(neighbor, newDirection));
          list.add(new Pair<>(currentPosition, currentDirection));
          queue.add(new Triplet<>(neighbor, newDistance, newDirection));
        }
      }
      visited.add(new Pair<>(currentPosition, currentDirection));
    }

    currentPosition = endPoint;
    Set<Coordinate> seats = new HashSet<>();
    for (var d :
        Stream.of(Direction.UP, Direction.DOWN, Direction.LEFT, Direction.RIGHT).toList()) {
      Queue<Pair<Coordinate, Direction>> toCheck = new LinkedList<>();
      Set<Pair<Coordinate, Direction>> alreadyChecked = new HashSet<>();
      toCheck.add(new Pair<>(currentPosition, d));
      while (!toCheck.isEmpty()) {
        var c = toCheck.poll();
        if (alreadyChecked.contains(c)) continue;
        alreadyChecked.add(c);
        currentPosition = c.one();
        currentDirection = c.two();
        seats.add(currentPosition);
        if (currentPosition == startPoint) {
          continue;
        }
        var stepList = previous.get(new Pair<>(currentPosition, currentDirection));
        for (var step : stepList) {
          toCheck.add(new Pair<>(step.one(), step.two()));
        }
      }
    }
    return seats.size();
  }
}
