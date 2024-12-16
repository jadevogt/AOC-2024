package com.expedient.adventofcodejade.solutions.year2015;

import com.expedient.adventofcodejade.BaseSolution;
import com.expedient.adventofcodejade.common.PuzzleInput;
import java.util.regex.Pattern;

public class SolutionDay5 extends BaseSolution {
  public SolutionDay5(PuzzleInput input, PuzzleInput sampleInputOne, PuzzleInput sampleInputTwo) {
    super(input, sampleInputOne, sampleInputTwo);
  }

  @Override
  public String partOne(PuzzleInput input) {
    Pattern doubleVowelRegex = Pattern.compile("([aeiou]).*([aeiou])");
    Pattern repeatCharacterRegex = Pattern.compile("([a-zA-Z])\\1+");
    Pattern bannedCharacterRegex = Pattern.compile("ab|cd|pq|xy");
    return Long.toString(
        input.getLines().stream()
            .filter(s -> doubleVowelRegex.matcher(s).find())
            .filter(s -> repeatCharacterRegex.matcher(s).find())
            .filter(s -> !bannedCharacterRegex.matcher(s).find())
            .count());
  }

  @Override
  public String partTwo(PuzzleInput input) {
    return null;
  }
}
