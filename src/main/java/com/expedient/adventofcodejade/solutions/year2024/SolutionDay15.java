package com.expedient.adventofcodejade.solutions.year2024;

import com.expedient.adventofcodejade.BaseSolution;
import com.expedient.adventofcodejade.common.Coordinate;
import com.expedient.adventofcodejade.common.Direction;
import com.expedient.adventofcodejade.common.Grid;
import com.expedient.adventofcodejade.common.PuzzleInput;
import com.expedient.adventofcodejade.solutions.year2024.solutionday15helpers.*;
import java.util.*;

public class SolutionDay15 extends BaseSolution {
  public SolutionDay15(PuzzleInput input, PuzzleInput sampleInputOne, PuzzleInput sampleInputTwo) {
    super(input, sampleInputOne, sampleInputTwo);
  }

  @Override
  public Object partOne(PuzzleInput input) {
    List<String> lines = input.getLines();
    List<String> gridLines = new ArrayList<>();
    for (String line : lines) {
      if (line.isBlank()) {
        break;
      }
      gridLines.add(line);
    }
    String robotInstructions = lines.getLast();
    var grid = Grid.fromStringList(gridLines);
    Actor[][] field = new Actor[grid.rowCount()][grid.colCount()];
    Robot robot = null;
    for (int i = 0; i < grid.rowCount(); i++) {
      for (int j = 0; j < grid.colCount(); j++) {
        switch (grid.at(i, j)) {
          case '@' -> {
            robot = new Robot(new Coordinate(i, j), field);
            field[i][j] = robot;
          }
          case 'O' -> {
            field[i][j] = new Block(new Coordinate(i, j), field);
          }
          case '#' -> {
            field[i][j] = new Wall(new Coordinate(i, j), field);
          }
        }
      }
    }
    if (robot == null) {
      return "ERROR";
    }
    var chars = robotInstructions.toCharArray();
    for (char aChar : chars) {
      switch (aChar) {
        case '^' -> {
          robot.tryMove(Direction.UP, false);
        }
        case '<' -> {
          robot.tryMove(Direction.LEFT, false);
        }
        case '>' -> {
          robot.tryMove(Direction.RIGHT, false);
        }
        case 'v' -> {
          robot.tryMove(Direction.DOWN, false);
        }
      }
    }
    int total = 0;
    for (int i = 0; i < field.length; i++) {
      for (int j = 0; j < field[0].length; j++) {
        if (field[i][j] instanceof Block) {
          total += (field[i][j].getPosition().col() + field[i][j].getPosition().row() * 100);
        }
      }
    }
    return total;
  }

  @Override
  public Object partTwo(PuzzleInput input) {
    List<String> lines = input.getLines();
    List<String> gridLines = new ArrayList<>();
    for (String line : lines) {
      if (line.isBlank()) {
        break;
      }
      gridLines.add(line);
    }
    String robotInstructions = lines.getLast();
    var grid = Grid.fromStringList(gridLines);
    Actor[][] field = new Actor[grid.rowCount()][grid.colCount() * 2];
    Robot robot = null;
    for (int i = 0; i < grid.rowCount(); i += 1) {
      for (int j = 0; j < grid.colCount() * 2; j += 2) {
        switch (grid.at(i, j / 2)) {
          case '@' -> {
            robot = new Robot(new Coordinate(i, j), field);
            field[i][j] = robot;
          }
          case 'O' -> {
            var blk = new DoubleBox(new Coordinate(i, j), field);
            field[i][j] = blk;
            field[i][j + 1] = blk;
          }
          case '#' -> {
            field[i][j] = new Wall(new Coordinate(i, j), field);
            field[i][j + 1] = new Wall(new Coordinate(i, j + 1), field);
          }
        }
      }
    }
    if (robot == null) {
      return "ERROR";
    }
    var chars = robotInstructions.toCharArray();
    for (int i = 0; i < chars.length; i++) {
      char aChar = chars[i];
      switch (aChar) {
        case '^' -> {
          robot.tryMove(Direction.UP, false);
        }
        case '<' -> {
          robot.tryMove(Direction.LEFT, false);
        }
        case '>' -> {
          robot.tryMove(Direction.RIGHT, false);
        }
        case 'v' -> {
          robot.tryMove(Direction.DOWN, false);
        }
      }
    }
    int total = 0;
    for (int i = 0; i < field.length; i++) {
      for (int j = 0; j < field[0].length; j++) {
        if (field[i][j] instanceof DoubleBox) {
          var p = field[i][j].getPosition();
          if (p.equals(new Coordinate(i, j))) {
            total += (p.col() + p.row() * 100);
          }
        }
      }
    }
    return total;
  }
}
