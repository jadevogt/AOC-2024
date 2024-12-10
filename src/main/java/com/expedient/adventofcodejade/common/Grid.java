package com.expedient.adventofcodejade.common;

import com.expedient.adventofcodejade.util.StringTools;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.function.Function;
import java.util.function.Predicate;

/** Manages a 2D array and provides methods to interact with it */
public class Grid<T> {
  private T[][] array;

  private Grid(T[][] array) {
    this.array = array;
  }

  /**
   * Get the row count
   *
   * @return the Grid's row count
   */
  public int rowCount() {
    return array.length;
  }

  /**
   * Get the column count
   *
   * @return the Grid's column count
   */
  public int colCount() {
    return array[0].length;
  }

  /**
   * Returns false if the given coordinate would be out of bounds
   *
   * @param location a coordinate
   * @return false if oob
   */
  public boolean isSafe(Coordinate location) {
    return location.row() >= 0
        && location.row() < rowCount()
        && location.col() >= 0
        && location.col() < colCount();
  }

  public static Grid<Character> fromStringList(List<String> lines) {
    List<String> linesCleaned = lines.stream().filter(i -> !i.isEmpty()).toList();
    if (linesCleaned.isEmpty()) {
      throw new IllegalArgumentException("Invalid input: The input cannot be empty");
    }
    int rows = linesCleaned.size();
    int cols = linesCleaned.get(0).length();
    if (linesCleaned.stream().anyMatch(i -> i.length() != cols)) {
      throw new IllegalArgumentException(
          "Invalid input: The input has rows with inconsistent lengths.");
    }
    Character[][] array = new Character[rows][cols];
    for (int i = 0; i < rows; i++) {
      array[i] = StringTools.ToCharacterArray(linesCleaned.get(i));
    }
    return new Grid<>(array);
  }

  /**
   * Get the value at the given coordinates
   *
   * @param row value row
   * @param col value column
   * @return the value of type T
   */
  public T at(int row, int col) {
    return array[row][col];
  }

  /**
   * Get the value at the given Coordinate
   *
   * @param c coordinate for value
   * @return the value of type T
   */
  public T at(Coordinate c) {
    return array[c.row()][c.col()];
  }

  /**
   * Set the value at the given coordinates
   *
   * @param row value row
   * @param col value column
   * @param val the value to set
   */
  public void set(int row, int col, T val) {
    array[row][col] = val;
  }

  /**
   * Set the value at the given Coordinate
   *
   * @param c value Coordinate
   * @param val the value to set
   */
  public void set(Coordinate c, T val) {
    array[c.row()][c.col()] = val;
  }

  /**
   * Returns the backing array for the Grid
   *
   * @return 2D backing array
   */
  public T[][] getArray() {
    return array;
  }

  /**
   * Returns a deep copy of the backing array for the Grid
   *
   * @return deep copy of the 2D backing array
   */
  public T[][] cloneArray() {
    T[][] copy = array.clone();
    for (int row = 0; row < rowCount(); row++) {
      copy[row] = array[row].clone();
    }
    return copy;
  }

  /**
   * Sets the backing array for the Grid
   *
   * @param newArray 2D array
   */
  public void setArray(T[][] newArray) {
    array = newArray;
  }

  /**
   * Returns a list of Coordinates that surround the value at row, col in a given 2d array,
   * respecting bounds
   *
   * @param center Coordinate of center
   * @param orthogonalOnly whether to exclude diagonals from output
   * @return a list of in-bounds Coordinates that surround the center point
   */
  public List<Coordinate> safeNeighborCoordinates(Coordinate center, boolean orthogonalOnly) {
    ArrayList<Coordinate> safeCoords = new ArrayList<>();
    int row = center.row();
    int col = center.col();
    int minRow = row == 0 ? 0 : row - 1;
    int maxRow = row == rowCount() - 1 ? row : row + 1;
    int minCol = col == 0 ? 0 : col - 1;
    int maxCol = col == colCount() - 1 ? col : col + 1;

    if (orthogonalOnly) {
      if (col > 0) safeCoords.add(new Coordinate(row, minCol));
      if (col != colCount() - 1) safeCoords.add(new Coordinate(row, maxCol));
      if (row > 0) safeCoords.add(new Coordinate(minRow, col));
      if (row != rowCount() - 1) safeCoords.add(new Coordinate(maxRow, col));
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

  public List<Coordinate> matchCoordinates(Predicate<T> test) {
    ArrayList<Coordinate> matches = new ArrayList<>();
    for (int row = 0; row < rowCount(); row++) {
      for (int col = 0; col < colCount(); col++) {
        if (test.test(at(row, col))) {
          matches.add(new Coordinate(row, col));
        }
      }
    }
    return matches;
  }

  /**
   * Returns coordinates to the left and right or up and down from the given center point,
   * respecting bounds
   *
   * @param center Coordinate of center
   * @param vertical whether to give up and down rather than left and right
   * @return list of Coordinates adjacent to the center point
   */
  public List<Coordinate> adjacentCoordinates(Coordinate center, boolean vertical) {
    List<Coordinate> safeCoords = safeNeighborCoordinates(center, true);
    if (vertical) {
      return safeCoords.stream().filter(i -> i.col() == center.col()).toList();
    }
    return safeCoords.stream().filter(i -> i.row() == center.row()).toList();
  }

  /**
   * Checks if any values surrounding a given point in a 2d array pass the provided test
   *
   * @param center Coordinate of center
   * @param test the test run against the values surrounding the center point
   * @param orthogonalOnly whether to exclude diagonals from the test
   * @return whether any surrounding points pass the test
   */
  public boolean checkNeighbors(Coordinate center, Predicate<T> test, boolean orthogonalOnly) {
    List<Coordinate> safe = safeNeighborCoordinates(center, orthogonalOnly);
    return safe.stream().anyMatch(i -> test.test(at(i)));
  }

  /**
   * Checks if any values surrounding a given point in a 2d array pass the provided test
   *
   * @param center Coordinate of center
   * @param test the test run against the values surrounding the center point
   * @return whether any surrounding points pass the test
   */
  public boolean checkNeighbors(Coordinate center, Predicate<T> test) {
    return checkNeighbors(center, test, false);
  }

  /**
   * Gets a list of Coordinates surrounding a given point in a 2d array that pass the provided test
   *
   * @param center Coordinate of center
   * @param test the test run against the values surrounding the center point
   * @param orthogonalOnly whether to exclude diagonals from the test
   * @return list of Coordinates of surrounding values that pass the test
   */
  public List<Coordinate> matchNeighbors(
      Coordinate center, Predicate<T> test, boolean orthogonalOnly) {
    List<Coordinate> safe = safeNeighborCoordinates(center, orthogonalOnly);
    return safe.stream().filter(i -> test.test(at(i))).toList();
  }

  /**
   * Gets a list of Coordinates surrounding a given point in a 2d array that pass the provided test
   *
   * @param center Coordinate of center
   * @param test the test run against the values surrounding the center point
   * @return list of Coordinates of surrounding values that pass the test
   */
  public List<Coordinate> matchNeighbors(Coordinate center, Predicate<T> test) {
    return matchNeighbors(center, test, false);
  }

  /**
   * Performs a flood fill operation on the provided 2D array, using the given test and
   * transformation
   *
   * @param center Coordinate of center
   * @param test the test run to determine whether the value is "inside"
   * @param transformation the operation to apply to the values. this should also invalidate the
   *     "inside" test
   */
  public Grid<T> floodFill(Coordinate center, Predicate<T> test, Function<T, T> transformation) {
    if (!test.test(at(center))) {
      return this;
    }
    set(center, transformation.apply(at(center)));
    List<Coordinate> coords = safeNeighborCoordinates(center, true);
    coords.forEach(c -> floodFill(c, test, transformation));
    return this;
  }

  /**
   * @param grid
   * @param center
   * @param currentStep
   * @return
   */
  public static int findTrailStepUnique(
      Grid<Character> grid, Coordinate center, final int currentStep) {
    if (grid.at(center) == '9') {
      return 1;
    }
    List<Coordinate> candidates = grid.safeNeighborCoordinates(center, true);
    candidates =
        candidates.stream().filter(i -> grid.at(i) == (char) ('0' + currentStep + 1)).toList();
    int total = 0;
    for (Coordinate candidate : candidates) {
      total += findTrailStepUnique(grid, candidate, currentStep + 1);
    }
    return total;
  }

  public static Set<Coordinate> findTrailStepScore(
      Grid<Character> grid, Coordinate center, final int currentStep) {
    if (grid.at(center) == '9') {
      Set<Coordinate> endpoints = new HashSet<>();
      endpoints.add(center);
      return endpoints;
    }
    List<Coordinate> candidates = grid.safeNeighborCoordinates(center, true);
    candidates =
        candidates.stream().filter(i -> grid.at(i) == (char) ('0' + currentStep + 1)).toList();
    Set<Coordinate> endpoints = new HashSet<>();
    for (Coordinate candidate : candidates) {
      endpoints.addAll(findTrailStepScore(grid, candidate, currentStep + 1));
    }
    return endpoints;
  }

  /** Prints the 2D array to stdout */
  public void print() {
    for (int rows = 0; rows < rowCount(); rows++) {
      for (int cols = 0; cols < colCount(); cols++) {
        System.out.print(at(rows, cols));
      }
      System.out.println();
    }
  }

  /**
   * Performs a deep copy of the backing array and then returns a new Grid instance from the copy
   *
   * @return Fresh, cloned Grid
   */
  public Grid<T> duplicate() {
    T[][] clone = cloneArray();
    return new Grid<>(clone);
  }
}
