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
}
