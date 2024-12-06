package com.expedient.adventofcodejade.common;

/** Represents a cardinal direction, and provides methods for use when working with a 2D-array */
public enum Direction {
  UP {
    @Override
    public Direction turn() {
      return RIGHT;
    }

    @Override
    public Coordinate next(Coordinate location) {
      return new Coordinate(location.row() - 1, location.col());
    }
  },
  RIGHT {
    @Override
    public Direction turn() {
      return DOWN;
    }

    @Override
    public Coordinate next(Coordinate location) {
      return new Coordinate(location.row(), location.col() + 1);
    }
  },
  DOWN {
    @Override
    public Direction turn() {
      return LEFT;
    }

    @Override
    public Coordinate next(Coordinate location) {
      return new Coordinate(location.row() + 1, location.col());
    }
  },
  LEFT {
    @Override
    public Direction turn() {
      return UP;
    }

    @Override
    public Coordinate next(Coordinate location) {
      return new Coordinate(location.row(), location.col() - 1);
    }
  };

  /**
   * Returns the direction that is clockwise from the current one
   *
   * @return new direction
   */
  public abstract Direction turn();

  /**
   * Returns the direction counterclockwise from the current one
   *
   * @return new direction
   */
  public Direction turnCounterClockwise() {
    return turn().turn().turn();
  }

  /**
   * Returns the coordinate that is next from the given location in this direction
   *
   * @param location the current location
   * @return the next location, given as a Coordinate
   */
  public abstract Coordinate next(Coordinate location);
}
