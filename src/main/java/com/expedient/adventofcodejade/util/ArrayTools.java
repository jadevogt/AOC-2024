package com.expedient.adventofcodejade.util;

import com.expedient.adventofcodejade.common.Coordinate;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Function;
import java.util.function.Predicate;

/**
 * Static methods useful for working with 2D arrays of any type
 */
public class ArrayTools {

    /**
     * Returns a list of Coordinates that surround the value at row, col in a given 2d array, respecting bounds
     * @param row row of center
     * @param col col of center
     * @param array 2d array
     * @param orthogonalOnly whether to exclude diagonals from output
     * @return a list of in-bounds Coordinates that surround the center point
     * @param <T> type that makes up the 2D array
     */
    public static <T> List<Coordinate> safeNeighborCoordinates(int row, int col, T[][] array, boolean orthogonalOnly) {
        var safeCoords = new ArrayList<Coordinate>();
        int minRow = row == 0 ? 0 : row - 1;
        int maxRow = row == array.length - 1 ? row : row + 1;
        int minCol = col == 0 ? 0 : col - 1;
        int maxCol = col == array[row].length - 1 ? col : col + 1;

        if (orthogonalOnly) {
            if (col > 0)
                safeCoords.add(new Coordinate(row, minCol));
            if (col != array[row].length - 1)
                safeCoords.add(new Coordinate(row, maxCol));
            if (row > 0)
                safeCoords.add(new Coordinate(minRow, col));
            if (row != array.length - 1)
                safeCoords.add(new Coordinate(maxRow, col));
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

    /**
     * Returns coordinates to the left & right or up & down from the given center point, respecting bounds
     * @param row the center row
     * @param col the center column
     * @param array the 2d array
     * @param vertical whether to give up & down rather than left & right
     * @return list of Coordinates adjacent to the center point
     * @param <T> type that makes up the 2d array
     */
    public static <T> List<Coordinate> adjacentCoordinates(int row, int col, T[][] array, boolean vertical) {
        var safeCoords = safeNeighborCoordinates(row, col, array, true);
        if (vertical) {
            return safeCoords.stream().filter(i -> i.col() == col).toList();
        }
        return safeCoords.stream().filter(i -> i.row() == row).toList();
    }

    /**
     * Checks if any values surrounding a given point in a 2d array pass the provided test
     * @param row the center row
     * @param col the center column
     * @param array the 2d array
     * @param test the test run against the values surrounding the center point
     * @param orthogonalOnly whether to exclude diagonals from the test
     * @return whether any surrounding points pass the test
     * @param <T> type that makes up the 2d array
     */
    public static <T> boolean check2dArrayNeighbors(int row, int col, T[][] array, Predicate<T> test, boolean orthogonalOnly) {
        var safe = safeNeighborCoordinates(row, col, array, orthogonalOnly);
        return safe.stream().anyMatch(i -> test.test(array[i.row()][i.col()]));
    }

    /**
     * Checks if any values surrounding a given point in a 2d array pass the provided test
     * @param row the center row
     * @param col the center column
     * @param array the 2d array
     * @param test the test run against the values surrounding the center point
     * @return whether any surrounding points pass the test
     * @param <T> type that makes up the 2d array
     */
    public static <T> boolean check2dArrayNeighbors(int row, int col, T[][] array, Predicate<T> test) {
        return check2dArrayNeighbors(row, col, array, test, false);
    }

    /**
     * Gets a list of Coordinates surrounding a given point in a 2d array that pass the provided test
     * @param row the center row
     * @param col the center column
     * @param array the 2d array
     * @param test the test run against the values surrounding the center point
     * @param orthogonalOnly whether to exclude diagonals from the test
     * @return list of Coordinates of surrounding values that pass the test
     * @param <T> type that makes up the 2d array
     */
    public static <T> List<Coordinate> getMatching2dArrayNeighbors(int row, int col, T[][] array, Predicate<T> test, boolean orthogonalOnly) {
        var safe = safeNeighborCoordinates(row, col, array, orthogonalOnly);
        return safe.stream().filter(i -> test.test(array[i.row()][i.col()])).toList();
    }

    /**
     * Gets a list of Coordinates surrounding a given point in a 2d array that pass the provided test
     * @param row the center row
     * @param col the center column
     * @param array the 2d array
     * @param test the test run against the values surrounding the center point
     * @return list of Coordinates of surrounding values that pass the test
     * @param <T> type that makes up the 2d array
     */
    public static <T> List<Coordinate> getMatching2dArrayNeighbors(int row, int col, T[][] array, Predicate<T> test) {
        return getMatching2dArrayNeighbors(row, col, array, test, false);
    }

    /**
     * Performs a flood fill operation on the provided 2D array, using the given test and transformation
     * @param row row of the start point
     * @param col column of the start point
     * @param array the 2d array (will be modified by this method)
     * @param test the test run to determine whether the value is "inside"
     * @param transformation the operation to apply to the values. this should also invalidate the "inside" test
     * @param <T> type that makes up the 2d array
     */
    public static <T> void floodFill(int row, int col, T[][] array, Predicate<T> test, Function<T, T> transformation) {
        if (!test.test(array[row][col])) {
            return;
        }
        array[row][col] = transformation.apply(array[row][col]);
        var coords = safeNeighborCoordinates(row, col, array, true);
        coords.forEach(c -> floodFill(c.row(), c.col(), array, test, transformation));
    }
}
