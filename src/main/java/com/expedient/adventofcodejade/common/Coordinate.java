package com.expedient.adventofcodejade.common;

/**
 * Represents a row, col Coordinate on a 2D array
 *
 * @param row
 * @param col
 */
public record Coordinate(int row, int col) {
  /**
   * Provided a Coordinates, returns another Coordinate that's one additional step in the same
   * direction from this one
   *
   * @param other Ending Coordinate
   * @return Coordinate that continues in a line out from the second
   */
  public Coordinate nextInDirection(Coordinate other) {
    int r = other.row();
    int c = other.col();
    if (other.row() > row()) {
      r++;
    }
    if (other.row() < row()) {
      r--;
    }
    if (other.col() > col()) {
      c++;
    }
    if (other.col() < col()) {
      c--;
    }
    return new Coordinate(r, c);
  }

  /**
   * Provided other Coordinate, returns another Coordinate that's on the opposite side of this one
   * from the second
   *
   * @param other Ending Coordinate
   * @return Coordinate one step from the first in the opposite direction
   */
  public Coordinate inOppositeDirection(Coordinate other) {
    int r = row();
    int c = col();
    if (other.row() > row()) {
      r--;
    }
    if (other.row() < row()) {
      r++;
    }
    if (other.col() > col()) {
      c--;
    }
    if (other.col() < col()) {
      c++;
    }
    return new Coordinate(r, c);
  }

  /**
   * Determines whether this Coordinate is orthogonal to another
   *
   * @param other the other coordinate
   * @return whether they're orthogonal
   */
  public boolean isOrthogonal(Coordinate other) {
    return row() == other.row() || col() == other.col();
  }

  /**
   * Finds the taxicab distance between this coordinate and another
   *
   * @param other the other coordinate
   * @return the taxicab distance between the two
   */
  public int taxiCabDistance(Coordinate other) {
    int rowDist = Math.abs(row() - other.row());
    int colDist = Math.abs(col() - other.col());
    return rowDist + colDist;
  }

  /**
   * Finds the Euclidean distance between this coordinate and another
   *
   * @param other the other coordinate
   * @return the Euclidean distance between the two coordinates
   */
  public double distance(Coordinate other) {
    int rowDist = row() - other.row();
    int colDist = col() - other.col();
    return Math.sqrt(Math.pow(rowDist, 2) + Math.pow(colDist, 2));
  }
}
