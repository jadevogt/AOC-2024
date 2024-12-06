package com.expedient.adventofcodejade.solutions.year2015;

import com.expedient.adventofcodejade.BaseSolution;
import com.expedient.adventofcodejade.common.Coordinate;
import com.expedient.adventofcodejade.common.PuzzleInput;
import com.expedient.adventofcodejade.util.StringTools;

import java.util.HashMap;
import java.util.Map;

public class SolutionDay3 extends BaseSolution {
  public SolutionDay3(PuzzleInput input, PuzzleInput sampleInputOne, PuzzleInput sampleInputTwo) {
    super(input, sampleInputOne, sampleInputTwo);
  }

  @Override
  public String partOne(PuzzleInput input) {
    Map<Coordinate, Integer> coords = new HashMap<>();
    int x = 0;
    int y = 0;
    coords.put(new Coordinate(x, y), 1);
    for (char c : StringTools.ToCharacterArray(input.getString())) {
      switch (c) {
        case 'v' -> y--;
        case '^' -> y++;
        case '>' -> x++;
        case '<' -> x--;
      }
      Coordinate current = new Coordinate(x, y);
      if (coords.containsKey(current)) {
        coords.put(current, coords.get(current) + 1);
      } else {
        coords.put(current, 1);
      }
    }
    return Long.toString(coords.values().stream().filter(i -> i >= 1).count());
  }

  @Override
  public String partTwo(PuzzleInput input) {
    Map<Coordinate, Integer> coords = new HashMap<>();
    int x1 = 0;
    int y1 = 0;
    int x2 = 0;
    int y2 = 0;
    coords.put(new Coordinate(x1, y1), 2);
    boolean turn = false;
    for (char c : StringTools.ToCharacterArray(input.getString())) {
      Coordinate current;
      if (turn) {
        switch (c) {
          case 'v' -> y1--;
          case '^' -> y1++;
          case '>' -> x1++;
          case '<' -> x1--;
        }
        current = new Coordinate(x1, y1);
      } else {
        switch (c) {
          case 'v' -> y2--;
          case '^' -> y2++;
          case '>' -> x2++;
          case '<' -> x2--;
        }
        current = new Coordinate(x2, y2);
      }
      if (coords.containsKey(current)) {
        coords.put(current, coords.get(current) + 1);
      } else {
        coords.put(current, 1);
      }
      turn = !turn;
    }
    return Long.toString(coords.values().stream().filter(i -> i >= 1).count());
  }
}
