package com.expedient.adventofcodejade.util;

public class NumTools {
  /** powers of ten used in quick int concatenation */
  private static final int[] POWERS_OF_TEN_INT = {
    10, 100, 1000, 10000, 100000, 1000000, 10000000, 100000000, 1000000000,
  };

  /** powers of ten used in quick long concatenation */
  private static final long[] POWERS_OF_TEN_LONG = {
    10L,
    100L,
    1000L,
    10000L,
    100000L,
    1000000L,
    10000000L,
    100000000L,
    1000000000L,
    10000000000L,
    100000000000L,
    1000000000000L,
    10000000000000L,
    100000000000000L,
    1000000000000000L,
    10000000000000000L,
    100000000000000000L,
    1000000000000000000L
  };

  /**
   * Concatenates two ints without using string operations
   *
   * @param one the first int
   * @param two the second int
   * @return a new int that is the result of concatenating the two given as if they were Strings
   */
  public static int fastCatInt(int one, int two) {
    int power = 0;
    while (two >= POWERS_OF_TEN_INT[power]) power++;
    return POWERS_OF_TEN_INT[power] * one + two;
  }

  /**
   * Concatenates two longs without using string operations
   *
   * @param one the first long
   * @param two the second long
   * @return a new long that is the result of concatenating the two given as if they were Strings
   */
  public static long fastCat(long one, long two) {
    int power = 0;
    while (two >= POWERS_OF_TEN_LONG[power]) power++;
    return POWERS_OF_TEN_LONG[power] * one + two;
  }

  /**
   * Use <a href="https://www.intmath.com/matrices-determinants/1-determinants.php">Cramer's
   * Rule</a> to solve a 2x2 system of equations with determinants, providing a set of values for x
   * and y that satisfies the given equations
   *
   * @param coefficientsA the coefficients a1 and a2
   * @param coefficientsB the coefficients b1 and b2
   * @param constantsC the constants on the other side of the equals sign
   * @return an array containing a value for x and y that satisfies both equations
   */
  public static long[] solveSystemOfEquations(
      long[] coefficientsA, long[] coefficientsB, long[] constantsC) {
    long[] a = coefficientsA;
    long[] b = coefficientsB;
    long[] c = constantsC;
    long x = (c[0] * b[1] - c[1] * b[0]) / ((a[0] * b[1]) - (a[1] * b[0]));
    long y = (a[0] * c[1] - a[1] * c[0]) / ((a[0] * b[1]) - (a[1] * b[0]));
    return new long[] {x, y};
  }
}
