package com.expedient.adventofcodejade.util;

import com.expedient.adventofcodejade.common.Coordinate;
import java.util.ArrayList;
import java.util.List;
import java.util.function.Function;
import java.util.function.Predicate;

/** Static methods useful for working with 2D arrays of any type */
public class Array2DTools {

  /**
   * Returns a list of Coordinates that surround the value at row, col in a given 2d array,
   * respecting bounds
   *
   * @param center Coordinate of center
   * @param array 2d array
   * @param orthogonalOnly whether to exclude diagonals from output
   * @return a list of in-bounds Coordinates that surround the center point
   * @param <T> type that makes up the 2D array
   */
  public static <T> List<Coordinate> safeNeighborCoordinates(
      Coordinate center, T[][] array, boolean orthogonalOnly) {
    ArrayList<Coordinate> safeCoords = new ArrayList<>();
    int row = center.row();
    int col = center.col();
    int minRow = row == 0 ? 0 : row - 1;
    int maxRow = row == array.length - 1 ? row : row + 1;
    int minCol = col == 0 ? 0 : col - 1;
    int maxCol = col == array[row].length - 1 ? col : col + 1;

    if (orthogonalOnly) {
      if (col > 0) safeCoords.add(new Coordinate(row, minCol));
      if (col != array[row].length - 1) safeCoords.add(new Coordinate(row, maxCol));
      if (row > 0) safeCoords.add(new Coordinate(minRow, col));
      if (row != array.length - 1) safeCoords.add(new Coordinate(maxRow, col));
      return safeCoords;
    }
    for (int currentRow = minRow; currentRow <= maxRow; currentRow++) {
      for (int currentCol = minCol; currentCol <= maxCol; currentCol++) {
        if (!(currentRow == row && currentCol == col)) {
          safeCoords.add(new Coordinate(currentRow, currentCol));
        }
      }
    }

    return safeCoords;
  }

  public static <T> List<Coordinate> matchCoordinates(T[][] array, Predicate<T> test) {
    ArrayList<Coordinate> matches = new ArrayList<>();
    for (int row = 0; row < array.length; row++) {
      for (int col = 0; col < array[row].length; col++) {
        if (test.test(array[row][col])) {
          matches.add(new Coordinate(row, col));
        }
      }
    }
    return matches;
  }

  /**
   * Returns coordinates to the left & right or up & down from the given center point, respecting
   * bounds
   *
   * @param center Coordinate of center
   * @param array the 2d array
   * @param vertical whether to give up & down rather than left & right
   * @return list of Coordinates adjacent to the center point
   * @param <T> type that makes up the 2d array
   */
  public static <T> List<Coordinate> adjacentCoordinates(
      Coordinate center, T[][] array, boolean vertical) {
    List<Coordinate> safeCoords = safeNeighborCoordinates(center, array, true);
    if (vertical) {
      return safeCoords.stream().filter(i -> i.col() == center.col()).toList();
    }
    return safeCoords.stream().filter(i -> i.row() == center.row()).toList();
  }

  /**
   * Checks if any values surrounding a given point in a 2d array pass the provided test
   *
   * @param center Coordinate of center
   * @param array the 2d array
   * @param test the test run against the values surrounding the center point
   * @param orthogonalOnly whether to exclude diagonals from the test
   * @return whether any surrounding points pass the test
   * @param <T> type that makes up the 2d array
   */
  public static <T> boolean checkNeighbors(
      Coordinate center, T[][] array, Predicate<T> test, boolean orthogonalOnly) {
    List<Coordinate> safe = safeNeighborCoordinates(center, array, orthogonalOnly);
    return safe.stream().anyMatch(i -> test.test(array[i.row()][i.col()]));
  }

  /**
   * Checks if any values surrounding a given point in a 2d array pass the provided test
   *
   * @param center Coordinate of center
   * @param array the 2d array
   * @param test the test run against the values surrounding the center point
   * @return whether any surrounding points pass the test
   * @param <T> type that makes up the 2d array
   */
  public static <T> boolean checkNeighbors(Coordinate center, T[][] array, Predicate<T> test) {
    return checkNeighbors(center, array, test, false);
  }

  /**
   * Gets a list of Coordinates surrounding a given point in a 2d array that pass the provided test
   *
   * @param center Coordinate of center
   * @param array the 2d array
   * @param test the test run against the values surrounding the center point
   * @param orthogonalOnly whether to exclude diagonals from the test
   * @return list of Coordinates of surrounding values that pass the test
   * @param <T> type that makes up the 2d array
   */
  public static <T> List<Coordinate> matchNeighbors(
      Coordinate center, T[][] array, Predicate<T> test, boolean orthogonalOnly) {
    List<Coordinate> safe = safeNeighborCoordinates(center, array, orthogonalOnly);
    return safe.stream().filter(i -> test.test(array[i.row()][i.col()])).toList();
  }

  /**
   * Gets a list of Coordinates surrounding a given point in a 2d array that pass the provided test
   *
   * @param center Coordinate of center
   * @param array the 2d array
   * @param test the test run against the values surrounding the center point
   * @return list of Coordinates of surrounding values that pass the test
   * @param <T> type that makes up the 2d array
   */
  public static <T> List<Coordinate> matchNeighbors(
      Coordinate center, T[][] array, Predicate<T> test) {
    return matchNeighbors(center, array, test, false);
  }

  /**
   * Performs a flood fill operation on the provided 2D array, using the given test and
   * transformation
   *
   * @param center Coordinate of center
   * @param array the 2d array (will be modified by this method)
   * @param test the test run to determine whether the value is "inside"
   * @param transformation the operation to apply to the values. this should also invalidate the
   *     "inside" test
   * @param <T> type that makes up the 2d array
   */
  public static <T> void floodFill(
      Coordinate center, T[][] array, Predicate<T> test, Function<T, T> transformation) {
    if (!test.test(array[center.row()][center.col()])) {
      return;
    }
    array[center.row()][center.col()] = transformation.apply(array[center.row()][center.col()]);
    List<Coordinate> coords = safeNeighborCoordinates(center, array, true);
    coords.forEach(c -> floodFill(c, array, test, transformation));
  }
}
