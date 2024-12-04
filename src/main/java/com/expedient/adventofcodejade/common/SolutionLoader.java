package com.expedient.adventofcodejade.common;

import com.expedient.adventofcodejade.BaseSolution;
import java.io.IOException;
import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;

/**
 * Class responsible for dynamically loading Solutions located in <code>
 * com.expedient.adventofcodejade.solutions</code>.
 */
public class SolutionLoader {
  private final String inputsDirectory;
  private final String solutionsPackage;
  private final ClassLoader classLoader;

  public SolutionLoader(String inputsDirectory, String solutionsPackage) {
    this.inputsDirectory = inputsDirectory;
    this.solutionsPackage = solutionsPackage;
    this.classLoader = this.getClass().getClassLoader();
  }

  public SolutionLoader() {
    this("inputs", "com.expedient.adventofcodejade.solutions");
  }

  /**
   * Dynamically load the Solution class in the package for the given day
   *
   * @param day the day of the month to use for the solution
   * @return The BaseSolution instance for the given day, located in the day's respective package as
   *     a class named "Solution"
   * @throws ClassNotFoundException there's no class for the given day
   * @throws RuntimeException there's some other error with the constructor or something
   * @throws IOException the sample input could not be found
   */
  public BaseSolution loadForDay(int day, boolean test)
      throws ClassNotFoundException, RuntimeException, IOException {
    String inputPath = "%s/%d".formatted(inputsDirectory, day);
    String className = "%s.SolutionDay%d".formatted(solutionsPackage, day);
    PuzzleInput sampleInputOne = PuzzleInput.sampleForDay(day, true);
    PuzzleInput sampleInputTwo = PuzzleInput.sampleForDay(day, false);
    PuzzleInput input;
    try {
      input = PuzzleInput.fromPath(inputPath);
    } catch (IOException e) {
      if (!test) {
        System.out.printf(
            "No input for day %d found in ./inputs, using sample input instead.%n", day);
      }
      input = null;
    }
    try {
      Class<?> cls = classLoader.loadClass(className);
      Constructor<?> constructor =
          cls.getConstructor(PuzzleInput.class, PuzzleInput.class, PuzzleInput.class);
      return (BaseSolution) constructor.newInstance(input, sampleInputOne, sampleInputTwo);
    } catch (NoSuchMethodException
        | InvocationTargetException
        | InstantiationException
        | IllegalAccessException e) {
      throw new RuntimeException(e);
    }
  }

  /**
   * Tries to get solutions for days 1-25, breaks out of the loop is any are missing. There should
   * never be more than 25 days before Christmas lol
   *
   * @return Array of BaseSolution instances
   * @throws RuntimeException Some error was encountered instantiating the classes
   */
  public BaseSolution[] loadSolutions(boolean test) throws RuntimeException {
    ArrayList<BaseSolution> solutions = new ArrayList<>(25);
    for (int i = 1; i < 26; i++) {
      try {
        BaseSolution solution = loadForDay(i, test);
        solutions.add(solution);
      } catch (ClassNotFoundException | IOException e) {
        break;
      }
    }
    return solutions.toArray(BaseSolution[]::new);
  }
}
