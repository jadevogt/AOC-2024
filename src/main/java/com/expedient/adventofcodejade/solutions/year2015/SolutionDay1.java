package com.expedient.adventofcodejade.solutions.year2015;

import com.expedient.adventofcodejade.BaseSolution;
import com.expedient.adventofcodejade.common.PuzzleInput;
import com.expedient.adventofcodejade.util.StringTools;

public class SolutionDay1 extends BaseSolution {
  public SolutionDay1(PuzzleInput input, PuzzleInput sampleInputOne, PuzzleInput sampleInputTwo) {
    super(input, sampleInputOne, sampleInputTwo);
  }

  @Override
  public String partOne(PuzzleInput input) {
    String line = input.getString();
    Character[] chars = StringTools.ToCharacterArray(line);
    int floor = 0;
    for (Character c : chars) {
      if (c == '(') floor++;
      else if (c == ')') floor--;
    }
    return Integer.toString(floor);
  }

  @Override
  public String partTwo(PuzzleInput input) {
    String line = input.getString();
    Character[] chars = StringTools.ToCharacterArray(line);
    int floor = 0;
    for (int i = 0; i < chars.length; i++) {
      if (chars[i] == '(') floor++;
      else if (chars[i] == ')') floor--;
      if (floor < 0) {
        return Integer.toString(i + 1);
      }
    }
    return "FAILED: Input never reached basement";
  }
}
