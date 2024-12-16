package com.expedient.adventofcodejade.solutions.year2024;

import com.expedient.adventofcodejade.BaseSolution;
import com.expedient.adventofcodejade.common.Pair;
import com.expedient.adventofcodejade.common.PuzzleInput;
import com.expedient.adventofcodejade.util.NumTools;
import java.util.ArrayList;
import java.util.List;

public class SolutionDay13 extends BaseSolution {
  public SolutionDay13(PuzzleInput input, PuzzleInput sampleInputOne, PuzzleInput sampleInputTwo) {
    super(input, sampleInputOne, sampleInputTwo);
  }

  /**
   * Parse the PuzzleInput into a list of Machines
   *
   * @param input the PuzzleInput
   * @param partTwo whether this is part two, where a huge constant is added to the prize coords
   * @return A List of Machines defined by the input
   */
  public List<Machine> getMachines(PuzzleInput input, boolean partTwo) {
    Pair<Long, Long> currentA = null;
    Pair<Long, Long> currentB = null;
    Pair<Long, Long> prizeLocation;
    List<Machine> machines = new ArrayList<>();
    for (var line : input.getLines()) {
      if (line.contains("Button A")) {
        long one = Long.parseLong(line.split("Button A: X\\+")[1].split(", ")[0]);
        long two = Long.parseLong(line.split("Y+")[1]);
        currentA = new Pair<>(one, two);
      }
      if (line.contains("Button B")) {
        long one = Long.parseLong(line.split("Button B: X\\+")[1].split(", ")[0]);
        long two = Long.parseLong(line.split("Y+")[1]);
        currentB = new Pair<>(one, two);
      }
      if (line.contains("Prize") && currentA != null && currentB != null) {
        long one = Long.parseLong(line.split("Prize: X=")[1].split(", ")[0]);
        long two = Long.parseLong(line.split("Y=")[1]);
        prizeLocation =
            partTwo
                ? new Pair<>(one + 10000000000000L, two + 10000000000000L)
                : new Pair<>(one, two);
        machines.add(new Machine(currentA, currentB, prizeLocation));
      }
    }
    return machines;
  }

  /**
   * Given a list of Machines, calculates the solution for each Machine using Cramer's rule, then
   * discard every machine whose solution doesn't actually work.
   *
   * @param machines List of Machines
   * @return the total tokens required to win all machines
   */
  public long solveMachines(List<Machine> machines) {
    long total = 0;
    for (var machine : machines) {
      long[] coefficientsA = new long[] {machine.buttonA.one(), machine.buttonA.two()};
      long[] coefficientsB = new long[] {machine.buttonB.one(), machine.buttonB.two()};
      long[] constantsC = new long[] {machine.prize.one(), machine.prize.two()};
      long[] answers = NumTools.solveSystemOfEquations(coefficientsA, coefficientsB, constantsC);
      long locX = answers[0] * machine.buttonA.one() + answers[1] * machine.buttonB.one();
      long locY = answers[0] * machine.buttonA.two() + answers[1] * machine.buttonB.two();
      if (!(locX == machine.prize.one() && locY == machine.prize.two())) {
        continue;
      }
      total += (answers[0] * 3) + answers[1];
    }
    return total;
  }

  @Override
  public Long partOne(PuzzleInput input) {
    List<Machine> machines = getMachines(input, false);
    return solveMachines(machines);
  }

  @Override
  public Long partTwo(PuzzleInput input) {
    List<Machine> machines = getMachines(input, true);
    return solveMachines(machines);
  }

  /**
   * Record representing a Claw Machine
   *
   * @param buttonA the x and y change resulting from pressing button A once
   * @param buttonB the x and y change resulting from pressing button B once
   * @param prize the coordinates of the Prize
   */
  public record Machine(
      Pair<Long, Long> buttonA, Pair<Long, Long> buttonB, Pair<Long, Long> prize) {}
}
