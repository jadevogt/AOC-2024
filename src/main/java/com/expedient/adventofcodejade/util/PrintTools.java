package com.expedient.adventofcodejade.util;

/** Static methods used for formatting text */
public class PrintTools {
  /** Width of the output terminal, used when formatting headers */
  public static final int TERM_WIDTH = 72;

  /**
   * Creates a nice-looking header containing the given text
   *
   * @param input text included in the header
   * @return the formatted header
   */
  public static String formatHeader(String input) {
    int len = input.length();
    int repeat = TERM_WIDTH - len - 3;
    return "== %s %s%n".formatted(input, "=".repeat(repeat));
  }

  /** Print usage information for the command line */
  public static void printHelp() {
    System.out.printf("usage: AdventOfCode [--all] [--test] [--help]%n [--day=num]");
  }

  /**
   * Generate a header for a given day
   *
   * @param day day number
   * @param isIndex whether the day number is an array index (adds 1 to the visual number)
   * @return the formatted header
   */
  public static String dayHeader(int day, boolean isIndex) {
    return formatHeader("Day %d".formatted(isIndex ? day + 1 : day));
  }
}
