package com.expedient.adventofcodejade.common;

/**
 * Represents a 2D vector between two Coordinates
 *
 * @param rowDiff the difference in rows
 * @param colDiff the difference in columns
 */
public record Vector2(int rowDiff, int colDiff) {
  public Vector2(Coordinate one, Coordinate two) {
    this(one.row() - two.row(), one.col() - two.col());
  }
  

  /**
   * Reverses the direction of the Vector2
   *
   * @return vector but reversed
   */
  public Vector2 reverse() {
    return new Vector2(rowDiff() * -1, colDiff() * -1);
  }

  /**
   * Multiplies the magnitude of the Vector2
   *
   * @param multiple magnitude
   * @return the multiplied Vector2
   */
  public Vector2 multiply(int multiple) {
    return new Vector2(rowDiff() * multiple, colDiff() * multiple);
  }

  /**
   * Applies the Vector2 to a given Coordinate
   *
   * @param src the source Coordinate
   * @return a Coordinate that is the Vector2 away from the given Coordinate
   */
  public Coordinate apply(Coordinate src) {
    return new Coordinate(src.row() + rowDiff(), src.col() + colDiff());
  }

  public Coordinate applyWithWraparound(Coordinate src, int rowCount, int colCount) {
    int newRow = (src.row() + rowDiff());
    int newCol = (src.col() + colDiff());
    newRow = newRow % rowCount;
    newCol = newCol % colCount;
    newRow = newRow < 0 ? newRow + rowCount : newRow;
    newCol = newCol < 0 ? newCol + colCount : newCol;
    return new Coordinate(newRow, newCol);
  }
}
