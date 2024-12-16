package com.expedient.adventofcodejade.solutions.year2024.solutionday15helpers;

import com.expedient.adventofcodejade.common.Coordinate;
import com.expedient.adventofcodejade.common.Direction;

public class DoubleBox extends Actor {
  public DoubleBox(Coordinate startPosition, Actor[][] grid) {
    super(startPosition, grid);
  }

  @Override
  public void changePosition(Coordinate newPosition) {
    this.grid[position.row()][position.col()] = null;
    this.grid[position.row()][position.col() + 1] = null;
    this.grid[newPosition.row()][newPosition.col()] = this;
    this.grid[newPosition.row()][newPosition.col() + 1] = this;
    this.position = newPosition;
  }

  @Override
  public boolean tryMove(Direction direction, boolean dry) {
    Coordinate[] toCheck =
        switch (direction) {
          case UP ->
              new Coordinate[] {
                new Coordinate(position.row() - 1, position.col()),
                new Coordinate(position.row() - 1, position.col() + 1)
              };
          case LEFT -> new Coordinate[] {new Coordinate(position.row(), position.col() - 1)};
          case DOWN ->
              new Coordinate[] {
                new Coordinate(position.row() + 1, position.col()),
                new Coordinate(position.row() + 1, position.col() + 1)
              };
          case RIGHT -> new Coordinate[] {new Coordinate(position.row(), position.col() + 2)};
        };
    for (Coordinate check : toCheck) {
      var n = grid[check.row()][check.col()];
      if (n == null) continue;
      if (n.position == this.position) continue;
      if (!n.tryMove(direction, true)) return false;
    }
    // if (dry) return true;
    for (Coordinate check : toCheck) {
      var n = grid[check.row()][check.col()];
      if (n == null) continue;
      if (n.position == this.position) continue;
      n.tryMove(direction, false);
    }
    switch (direction) {
      case UP -> {
        changePosition(new Coordinate(position.row() - 1, position.col()));
      }
      case DOWN -> {
        changePosition(new Coordinate(position.row() + 1, position.col()));
      }
      case RIGHT -> {
        changePosition(new Coordinate(position.row(), position.col() + 1));
      }
      case LEFT -> {
        changePosition(new Coordinate(position.row(), position.col() - 1));
      }
    }
    return true;
  }
}
