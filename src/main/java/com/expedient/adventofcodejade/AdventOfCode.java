package com.expedient.adventofcodejade;

import com.expedient.adventofcodejade.util.Formatting;
import com.expedient.adventofcodejade.common.SolutionLoader;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Arrays;

/**
 * Advent of Code solution runner
 */
public class AdventOfCode {
    /**
     * Entry point for application
     * @param args <b>command line arguments for application</b><ul>
     *             <li><code>--help</code>    - prints help</li>
     *             <li><code>--test</code>    - uses sample input</li>
     *             <li><code>--all</code>     - prints and runs all solutions</li>
     *             <li><code>--day=x</code> - prints and runs solution for day <code>x</code></li>
     *             </ul>
     */
    public static void main(String[] args) {
        boolean test = Arrays.asList(args).contains("--test");
        SolutionLoader loader = new SolutionLoader();
        if (!test && !Files.exists(Path.of("inputs"))) {
            System.out.println("No ./inputs directory exists, using sample inputs.");
            test = true;
        }
        if (Arrays.asList(args).contains("--help")) {
            Formatting.printHelp();
            return;
        }
        var selected = Arrays.stream(args).filter(s -> s.startsWith("--day=")).toList();
        if (!selected.isEmpty()) {
            runForDay(selected.get(0), test, loader);
            return;
        }
        if (Arrays.asList(args).contains("--all")) {
            runAllSolutions(test, loader);
        } else {
            runLatestSolution(test, loader);
        }
    }

    /**
     * Parse out the selected day from command line args and run it
     * @param dayArg selected day
     * @param test whether to use sample input
     */
    public static void runForDay(String dayArg, boolean test, SolutionLoader loader) {
        var selectedDay = Integer.parseInt(dayArg.split("=")[1]);
        try {
            var solution = loader.loadForDay(selectedDay, test);
            System.out.printf(Formatting.dayHeader(selectedDay, false));
            solution.run(test);
        } catch (ClassNotFoundException e) {
            System.out.printf("The selected day, %d, does not have a valid solution%n", selectedDay);
        } catch (IOException e) {
            System.out.printf("The selected day, %d, has no valid input or sample input%n", selectedDay);
        }
    }

    /**
     * Get all solutions and only run the latest one
     * @param test whether to use sample input
     */
    public static void runLatestSolution(boolean test, SolutionLoader loader) {
        var solutions = loader.loadSolutions(test);
        int last = solutions.length - 1;
        System.out.printf(Formatting.dayHeader(last, true));
        solutions[last].run(test);
    }

    /**
     * Iterate over all solutions and print the solution for each, along with the header
     * @param test whether to use sample input
     */
    public static void runAllSolutions(boolean test, SolutionLoader loader) {
        var solutions = loader.loadSolutions(test);
        for (int i = 0; i < solutions.length; i++) {
            System.out.printf(Formatting.dayHeader(i, true));
            solutions[i].run(test);
        }
    }
}
