package com.expedient.adventofcodejade.solutions.year2024;

import com.expedient.adventofcodejade.BaseSolution;
import com.expedient.adventofcodejade.common.Pair;
import com.expedient.adventofcodejade.common.PuzzleInput;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class SolutionDay7 extends BaseSolution {
  public SolutionDay7(PuzzleInput input, PuzzleInput sampleInputOne, PuzzleInput sampleInputTwo) {
    super(input, sampleInputOne, sampleInputTwo);
  }

  /**
   * Takes two Longs and returns a new Long that is the result of concatenating them together
   *
   * @param one the first Long
   * @param two the second Long
   * @return a new Long that is the result of concatenating the two given as if they were Strings
   */
  public static Long catLong(Long one, Long two) {
    return Long.parseLong(one.toString() + two.toString());
  }

  /**
   * Takes a set of numbers, and for each number, applies both a multiplication and addition
   * operation on it with the next operand, returning a set containing all the results, given that
   * they're not above the target value
   *
   * @param nums a set of numbers
   * @param nextOperand the next number to multiply/add to the current numbers
   * @param target the target value that is not to be exceeded
   * @param partTwo whether to include the concatenation operator
   * @return a set of results that have not exceeded the target value
   */
  public static Set<Long> apply(Set<Long> nums, Long nextOperand, Long target, boolean partTwo) {
    if (!partTwo)
      return nums.stream()
          .flatMap(n -> Stream.of(n * nextOperand, n + nextOperand))
          .filter(n -> n <= target)
          .collect(Collectors.toSet());
    return nums.stream()
        .flatMap(n -> Stream.of(n * nextOperand, n + nextOperand, catLong(n, nextOperand)))
        .filter(n -> n <= target)
        .collect(Collectors.toSet());
  }

  /**
   * Reads the input line by line, constructing pairs of Integer results and a List of operands for
   * each
   *
   * @param input the PuzzleInput
   * @return a list of Pairs of results and lists of operands
   */
  public static List<Pair<Long, List<Long>>> parseInput(PuzzleInput input) {
    List<Pair<Long, List<Long>>> equations = new ArrayList<>();
    for (String line : input.getLines()) {
      Long result = Long.parseLong(line.split(": ")[0]);
      List<Long> operands = new ArrayList<>();
      for (var operand : line.split(": ")[1].split(" ")) {
        operands.add(Long.parseLong(operand));
      }
      equations.add(new Pair<>(result, operands));
    }
    return equations;
  }

  /**
   * Checks whether the result can be found via any combination of operations on the operands
   *
   * @param equation the result and operands as a pair
   * @param partTwo whether to use the concatenation operator
   * @return whether the result can be found
   */
  public static boolean checkEquation(Pair<Long, List<Long>> equation, boolean partTwo) {
    Set<Long> numbers = new HashSet<>();
    List<Long> operands = equation.two();
    Long result = equation.one();
    numbers.add(operands.get(0));
    for (int i = 1; i < operands.size(); i++) {
      numbers = apply(numbers, operands.get(i), result, partTwo);
    }
    return numbers.contains(result);
  }

  /**
   * Finds the sum of all results that can be found via some combination of operations on the
   * numbers provided
   *
   * @param input the PuzzleInput
   * @param partTwo whether to use the Concatenation operator
   * @return the sum
   */
  public static Long performRepairTask(PuzzleInput input, boolean partTwo) {
    List<Pair<Long, List<Long>>> equations = parseInput(input);
    return equations.parallelStream()
        .filter(i -> checkEquation(i, partTwo))
        .map(Pair::one)
        .reduce(Long::sum)
        .orElseThrow();
  }

  /**
   * Provides the sum of equations that can be found using the given operands
   *
   * @param input the PuzzleInput to be used for the solution
   * @return the sum of valid equations
   */
  @Override
  public Long partOne(PuzzleInput input) {
    return performRepairTask(input, false);
  }

  /**
   * Provides the sum of equations that can be found using the given operands, allowing use of the
   * concatenation operator
   *
   * @param input the PuzzleInput to be used for the solution
   * @return the sum of valid equations
   */
  @Override
  public Long partTwo(PuzzleInput input) {
    return performRepairTask(input, true);
  }
}
