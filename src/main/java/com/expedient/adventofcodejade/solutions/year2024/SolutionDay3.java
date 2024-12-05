package com.expedient.adventofcodejade.solutions.year2024;

import com.expedient.adventofcodejade.BaseSolution;
import com.expedient.adventofcodejade.common.PuzzleInput;
import com.expedient.adventofcodejade.util.StringTools;
import java.util.regex.Matcher;

public class SolutionDay3 extends BaseSolution {
  public SolutionDay3(PuzzleInput input, PuzzleInput sampleInputOne, PuzzleInput sampleInputTwo) {
    super(input, sampleInputOne, sampleInputTwo);
  }

  /**
   * @param input the PuzzleInput to be used for the solution
   * @return
   */
  @Override
  public String partOne(PuzzleInput input) {
    String regexPattern = "mul\\(\\d{1,3},\\d{1,3}\\)";
    Matcher m = StringTools.getMatcher(input.getString(), regexPattern);
    int total = 0;
    while (m.find()) {
      String g = m.group();
      int mul1 = Integer.parseInt(g.split(",")[0].replace("mul(", ""));
      int mul2 = Integer.parseInt(g.split(",")[1].replace(")", ""));
      total += mul1 * mul2;
    }
    return Integer.toString(total);
  }

  @Override
  public String partTwo(PuzzleInput input) {
    String regexPattern = "(mul\\(\\d{1,3},\\d{1,3}\\))|(do\\(\\))|(don't\\(\\))";
    Matcher m = StringTools.getMatcher(input.getString(), regexPattern);
    int total = 0;
    boolean active = true;
    while (m.find()) {
      String g = m.group();
      if (g.equals("do()")) {
        active = true;
        continue;
      }
      if (g.equals("don't()")) {
        active = false;
        continue;
      }
      if (active) {
        int mul1 = Integer.parseInt(g.split(",")[0].replace("mul(", ""));
        int mul2 = Integer.parseInt(g.split(",")[1].replace(")", ""));
        total += mul1 * mul2;
      }
    }
    return Integer.toString(total);
  }
}
