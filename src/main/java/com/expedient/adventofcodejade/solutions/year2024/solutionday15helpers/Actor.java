package com.expedient.adventofcodejade.solutions.year2024.solutionday15helpers;

import com.expedient.adventofcodejade.common.Coordinate;
import com.expedient.adventofcodejade.common.Direction;

public abstract class Actor {
  final Actor[][] grid;
  Coordinate position;

  public Actor(Coordinate startPosition, Actor[][] grid) {
    this.position = startPosition;
    this.grid = grid;
  }

  public Coordinate getPosition() {
    return this.position;
  }

  public void changePosition(Coordinate newPosition) {
    this.grid[position.row()][position.col()] = null;
    this.grid[newPosition.row()][newPosition.col()] = this;
    this.position = newPosition;
  }

  public abstract boolean tryMove(Direction direction, boolean dry);
}
