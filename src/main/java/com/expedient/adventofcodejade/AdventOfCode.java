package com.expedient.adventofcodejade;

import com.expedient.adventofcodejade.common.SolutionLoader;
import com.expedient.adventofcodejade.util.PrintTools;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.time.Year;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/** Advent of Code solution runner */
public class AdventOfCode {
  /**
   * Entry point for application
   *
   * @param args <b>command line arguments for application</b>
   *     <ul>
   *       <li><code>--help</code> - prints help
   *       <li><code>--test</code> - uses sample input
   *       <li><code>--all</code> - prints and runs all solutions
   *       <li><code>--year=x</code> - prints and runs solutions for year <code>x</code>
   *       <li><code>--day=x</code> - prints and runs solution for day <code>x</code>
   *     </ul>
   */
  public static void main(String[] args) {
    boolean test = Arrays.asList(args).contains("--test");
    boolean metrics = Arrays.asList(args).contains("--metrics");
    SolutionLoader loader = new SolutionLoader();
    if (!test && !Files.exists(Path.of("inputs"))) {
      System.out.println("No ./inputs directory exists, using sample inputs.");
      test = true;
    }
    if (Arrays.asList(args).contains("--help")) {
      PrintTools.printHelp();
      return;
    }
    List<String> selectedDay = Arrays.stream(args).filter(s -> s.startsWith("--day=")).toList();
    List<String> selectedYear = Arrays.stream(args).filter(s -> s.startsWith("--year=")).toList();
    if (!selectedYear.isEmpty() && !selectedDay.isEmpty()) {
      runForDay(selectedYear.get(0), selectedDay.get(0), test, loader, metrics);
      return;
    }
    if (!selectedYear.isEmpty()) {
      runAllSolutionsForYear(selectedYear.get(0), test, loader, metrics);
      return;
    }
    if (!selectedDay.isEmpty()) {
      runForDay(Integer.toString(Year.now().getValue()), selectedDay.get(0), test, loader, metrics);
      return;
    }
    if (Arrays.asList(args).contains("--all")) {
      runAllSolutions(test, loader, metrics);
    } else {
      runLatestSolution(test, loader, metrics);
    }
  }

  /**
   * Parse out the selected day from command line args and run it
   *
   * @param dayArg selected day
   * @param test whether to use sample input
   */
  public static void runForDay(
      String yearArg, String dayArg, boolean test, SolutionLoader loader, boolean metrics) {
    int selectedYear = Integer.parseInt(yearArg.split("=")[1]);
    int selectedDay = Integer.parseInt(dayArg.split("=")[1]);
    try {
      BaseSolution solution = loader.loadForDay(selectedYear, selectedDay, test);
      System.out.printf(PrintTools.dayHeader(selectedYear, selectedDay, false));
      solution.run(test, metrics);
    } catch (ClassNotFoundException e) {
      System.out.printf(
          "The selected day, %d-%d, does not have a valid solution%n", selectedYear, selectedDay);
    } catch (IOException e) {
      System.out.printf(
          "The selected day, %d-%d, has no valid input or sample input%n",
          selectedYear, selectedDay);
    }
  }

  /**
   * Get all solutions and only run the latest one
   *
   * @param test whether to use sample input
   */
  public static void runLatestSolution(boolean test, SolutionLoader loader, boolean metrics) {
    List<BaseSolution> solutions = new ArrayList<>();
    int lastYear = 0;
    int lastDay = 0;
    for (int year = 2015; year <= 2024; year++) {
      List<BaseSolution> yearSolutions = Arrays.stream(loader.loadSolutions(year, test)).toList();
      solutions.addAll(yearSolutions);
      lastDay = yearSolutions.size();
      lastYear = year;
    }
    System.out.printf(PrintTools.dayHeader(lastYear, lastDay, false));
    solutions.get(solutions.size() - 1).run(test, metrics);
  }

  public static void runAllSolutionsForYear(
      int year, boolean test, SolutionLoader loader, boolean metrics) {
    BaseSolution[] solutions = loader.loadSolutions(year, test);
    for (int i = 0; i < solutions.length; i++) {
      System.out.printf(PrintTools.dayHeader(year, i, true));
      solutions[i].run(test, metrics);
    }
  }

  public static void runAllSolutionsForYear(
      String yearInput, boolean test, SolutionLoader loader, boolean metrics) {
    int year = Integer.parseInt(yearInput.split("=")[1]);
    runAllSolutionsForYear(year, test, loader, metrics);
  }

  /**
   * Iterate over all solutions and print the solution for each, along with the header
   *
   * @param test whether to use sample input
   */
  public static void runAllSolutions(boolean test, SolutionLoader loader, boolean metrics) {
    for (int year = 2015; year <= 2024; year++) {
      runAllSolutionsForYear(year, test, loader, metrics);
    }
  }
}
