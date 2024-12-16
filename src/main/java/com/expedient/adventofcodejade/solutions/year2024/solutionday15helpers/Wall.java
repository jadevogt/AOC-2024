package com.expedient.adventofcodejade.solutions.year2024.solutionday15helpers;

import com.expedient.adventofcodejade.common.Coordinate;
import com.expedient.adventofcodejade.common.Direction;

public class Wall extends Actor {

  public Wall(Coordinate startPosition, Actor[][] grid) {
    super(startPosition, grid);
  }

  @Override
  public boolean tryMove(Direction direction, boolean dry) {
    return false;
  }
}
