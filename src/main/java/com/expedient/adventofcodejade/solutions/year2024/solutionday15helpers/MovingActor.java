package com.expedient.adventofcodejade.solutions.year2024.solutionday15helpers;

import com.expedient.adventofcodejade.common.Coordinate;
import com.expedient.adventofcodejade.common.Direction;

public abstract class MovingActor extends Actor {

  public MovingActor(Coordinate startPosition, Actor[][] grid) {
    super(startPosition, grid);
  }

  public boolean tryMove(Direction direction, boolean dry) {
    Coordinate next =
        switch (direction) {
          case UP -> new Coordinate(position.row() - 1, position.col());
          case DOWN -> new Coordinate(position.row() + 1, position.col());
          case LEFT -> new Coordinate(position.row(), position.col() - 1);
          case RIGHT -> new Coordinate(position.row(), position.col() + 1);
        };
    if (grid[next.row()][next.col()] != null) {
      boolean didMove = grid[next.row()][next.col()].tryMove(direction, dry);
      if (didMove && !dry) {
        changePosition(next);
      }
      return didMove;
    } else {
      if (!dry) changePosition(next);
      return true;
    }
  }
}
