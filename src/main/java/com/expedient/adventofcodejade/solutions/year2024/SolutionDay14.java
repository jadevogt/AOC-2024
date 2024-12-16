package com.expedient.adventofcodejade.solutions.year2024;

import com.expedient.adventofcodejade.BaseSolution;
import com.expedient.adventofcodejade.common.Coordinate;
import com.expedient.adventofcodejade.common.Pair;
import com.expedient.adventofcodejade.common.PuzzleInput;
import com.expedient.adventofcodejade.common.Vector2;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Optional;

public class SolutionDay14 extends BaseSolution {
  public SolutionDay14(PuzzleInput input, PuzzleInput sampleInputOne, PuzzleInput sampleInputTwo) {
    super(input, sampleInputOne, sampleInputTwo);
  }

  public record Robot(Coordinate position, Vector2 velocity, int rowCount, int colCount) {
    public Robot performStep() {
      var rat =
          new Robot(
              velocity.applyWithWraparound(position, rowCount, colCount),
              velocity,
              rowCount,
              colCount);
      return rat;
    }

    public Optional<Integer> getQuadrant() {
      int deadRow = (rowCount - 1) / 2;
      int deadCol = (colCount - 1) / 2;
      if (position.row() < deadRow && position.col() < deadCol) {
        return Optional.of(1);
      }
      if (position.row() < deadRow && position.col() > deadCol) {
        return Optional.of(2);
      }
      if (position.row() > deadRow && position.col() < deadCol) {
        return Optional.of(3);
      }
      if (position.row() > deadRow && position.col() > deadCol) {
        return Optional.of(4);
      }
      return Optional.empty();
    }
  }

  public static void printRobots(List<Robot> robots, int rowCount, int colCount, String title) {
    StringBuilder stringBuilder = new StringBuilder();
    for (int i = 0; i < rowCount; i++) {
      for (int j = 0; j < colCount; j++) {
        final int row = i;
        final int col = j;
        long robotCount =
            robots.stream().filter(r -> r.position.col() == col && r.position.row() == row).count();
        if (robotCount > 0) {
          stringBuilder.append(Long.toString(robotCount));
        } else {
          stringBuilder.append(" ");
        }
      }
      stringBuilder.append("\n");
    }
    clearConsole();
    System.out.print(stringBuilder.toString());
    System.out.printf(
        "================================%s===================================%n", title);
  }

  public static final void clearConsole() {
    try {
      final String os = System.getProperty("os.name");

      if (os.contains("Windows")) {
        Runtime.getRuntime().exec("cls");
      } else {
        Runtime.getRuntime().exec("clear");
      }
    } catch (final Exception e) {
      //  Handle any exceptions.
    }
  }

  @Override
  public Object partOne(PuzzleInput input) {
    var lines = input.getLines();
    List<Robot> robots = new ArrayList<>();
    int rowCount = 103;
    int colCount = 101;
    for (var line : lines) {
      int positionCol = Integer.parseInt(line.split("p=")[1].split(",")[0]);
      int positionRow = Integer.parseInt(line.split(",")[1].split(" ")[0]);
      int velocityCol = Integer.parseInt(line.split("v=")[1].split(",")[0]);
      int velocityRow = Integer.parseInt(line.split("v=")[1].split(",")[1]);
      robots.add(
          new Robot(
              new Coordinate(positionRow, positionCol),
              new Vector2(velocityRow, velocityCol),
              rowCount,
              colCount));
    }
    var robotLocations = new HashMap<Coordinate, Integer>();
    int coolDown = 0;
    for (int i = 0; i < 999999999; i++) {
      robotLocations.clear();
      robots = robots.stream().map(Robot::performStep).toList();
      for (var robot : robots) {
        if (robotLocations.containsKey(robot.position)) {
          robotLocations.put(robot.position, robotLocations.get(robot.position) + 1);
        } else {
          robotLocations.put(robot.position, 1);
        }
      }
      int count = 0;
      for (var r : robotLocations.keySet()) {
        for (var s : robotLocations.keySet()) {
          if (s.distance(r) < 2) {
            count++;
          }
        }
      }
      if (count > 875) {
        printRobots(robots, rowCount, colCount, Integer.toString(i));
        coolDown = 5;
      } else if (coolDown > 0) {
        printRobots(robots, rowCount, colCount, Integer.toString(i));
        coolDown--;
      }
    }
    int q1 = 0;
    int q2 = 0;
    int q3 = 0;
    int q4 = 0;
    for (var robot : robots) {
      Optional<Integer> quad = robot.getQuadrant();
      if (quad.isPresent()) {
        if (quad.get() == 1) q1++;
        if (quad.get() == 2) q2++;
        if (quad.get() == 3) q3++;
        if (quad.get() == 4) q4++;
      }
    }
    return q1 * q2 * q3 * q4;
  }

  @Override
  public Object partTwo(PuzzleInput input) {
    return null;
  }
}
