package com.expedient.adventofcodejade.solutions.year2024;

import com.expedient.adventofcodejade.BaseSolution;
import com.expedient.adventofcodejade.common.*;
import com.expedient.adventofcodejade.util.StringTools;
import java.util.HashSet;
import java.util.Set;

public class SolutionDay6 extends BaseSolution {

  public SolutionDay6(PuzzleInput input, PuzzleInput sampleInputOne, PuzzleInput sampleInputTwo) {
    super(input, sampleInputOne, sampleInputTwo);
  }

  /**
   * Identify the next tile in front of the guard. If the next tile is OOB, raise an exception. If
   * the next tile is an obstruction, turn clockwise. Otherwise, move forward into the tile.
   *
   * @param grid the Grid from the input
   * @param location the current location of the actor
   * @param direction the actor's direction
   * @param obstruction the coordinates of a placed obstruction
   * @return a Pair containing the new coordinate and the new direction
   */
  public static Pair<Coordinate, Direction> doStep(
      Grid<Character> grid, Coordinate location, Direction direction, Coordinate obstruction) {
    Coordinate newLocation = location;
    Direction newDirection = direction;
    Coordinate next = direction.next(location);
    if (!grid.isSafe(next)) {
      throw new ArrayIndexOutOfBoundsException();
    }
    if ((!StringTools.isInString(grid.at(next), ".^")) || next.equals(obstruction)) {
      newDirection = direction.turn();
    } else {
      newLocation = next;
    }
    return new Pair<>(newLocation, newDirection);
  }

  /**
   * Continuously performs doStep, adding each Coordinate visited to a Set. Once the actor moves
   * OOB, break out of the loop and then return the Set of visited Coordinates.
   *
   * @param grid the Grid from the input
   * @param location the starting actor Coordinate
   * @param direction the starting actor Direction
   * @return the Set of visited Coordinates
   */
  public static Set<Coordinate> getAllVisitedLocations(
      Grid<Character> grid, Coordinate location, Direction direction) {
    Set<Coordinate> visited = new HashSet<>();
    visited.add(location);
    while (true) {
      try {
        Pair<Coordinate, Direction> next = doStep(grid, location, direction, null);
        visited.add(next.one());
        location = next.one();
        direction = next.two();
      } catch (ArrayIndexOutOfBoundsException e) {
        break;
      }
    }
    return visited;
  }

  /**
   * Given a grid and a designated obstruction, continuously performs Steps, adding the actor's
   * visited coordinates and direction as Pairs to a Set. This goes on until the actor goes OOB
   * (which means no loop), or the actor's current direction and location are found in the Set,
   * which means they must have looped.
   *
   * @param grid the Grid from the input
   * @param obstruction a location that will be treated as a wall
   * @return whether the grid and obstruction result in a situation that causes a loop
   */
  public boolean detectLoop(Grid<Character> grid, Coordinate obstruction) {
    Coordinate character = grid.matchCoordinates(i -> i == '^').get(0);
    Direction direction = Direction.UP;
    HashSet<Pair<Coordinate, Direction>> visited = new HashSet<>();
    visited.add(new Pair<>(character, direction));
    while (true) {
      try {
        Pair<Coordinate, Direction> next = doStep(grid, character, direction, obstruction);
        if (visited.contains(next)) {
          return true;
        }
        visited.add(next);
        character = next.one();
        direction = next.two();
      } catch (ArrayIndexOutOfBoundsException e) {
        return false;
      }
    }
  }

  /**
   * Finds the number of unique locations visited by the actor, based on the original map
   *
   * @param input the PuzzleInput to be used for the solution
   * @return the number of unique locations
   */
  @Override
  public Integer partOne(PuzzleInput input) {
    Grid<Character> grid = input.getGrid();
    Coordinate character = grid.matchCoordinates(i -> i == '^').get(0);
    Direction direction = Direction.UP;
    return getAllVisitedLocations(grid, character, direction).size();
  }

  /**
   * Finds the number of locations where an obstruction may be placed on the grid to cause the actor
   * to move in an infinite loop
   *
   * @param input the PuzzleInput to be used for the solution
   * @return the number of potential obstruction locations
   */
  @Override
  public Integer partTwo(PuzzleInput input) {
    Grid<Character> grid = input.getGrid();
    Coordinate character = grid.matchCoordinates(i -> i == '^').get(0);
    Direction direction = Direction.UP;
    Set<Coordinate> visited = getAllVisitedLocations(grid, character, direction);
    int count = 0;
    for (Coordinate location : visited) {
      if (detectLoop(grid, location)) {
        count++;
      }
    }
    return count;
  }
}
