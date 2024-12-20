package com.expedient.adventofcodejade.solutions.year2024;

import com.expedient.adventofcodejade.BaseSolution;
import com.expedient.adventofcodejade.common.PuzzleInput;
import java.util.*;

public class SolutionDay19 extends BaseSolution {
  public SolutionDay19(PuzzleInput input, PuzzleInput sampleInputOne, PuzzleInput sampleInputTwo) {
    super(input, sampleInputOne, sampleInputTwo);
  }

  public long numberOfWaysToConstruct(String pattern, List<String> rugs) {
    long[] waysToConstructAtLength = new long[pattern.length() + 1];
    waysToConstructAtLength[0] = 1;
    for (int i = 0; i < pattern.length(); ++i) {
      if (waysToConstructAtLength[i] == 0) {
        continue;
      }
      for (String rug : rugs) {
        if (pattern.regionMatches(i, rug, 0, rug.length())) {
          waysToConstructAtLength[i + rug.length()] += waysToConstructAtLength[i];
        }
      }
    }
    return waysToConstructAtLength[pattern.length()];
  }

  @Override
  public Object partOne(PuzzleInput input) {
    var listOfRugs =
        Arrays.stream(input.getLines().get(0).split(", "))
            .sorted(Comparator.comparingInt(String::length))
            .map(String::trim)
            .toList();
    var listOfPatterns = input.getLines().subList(2, input.getLines().size());
    long total = 0;
    for (String line : listOfPatterns) {
      total += numberOfWaysToConstruct(line, listOfRugs) > 0 ? 1 : 0;
    }
    return total;
  }

  @Override
  public Object partTwo(PuzzleInput input) {
    var listOfRugs =
        Arrays.stream(input.getLines().get(0).split(", "))
            .sorted(Comparator.comparingInt(String::length))
            .map(String::trim)
            .toList();
    var listOfPatterns = input.getLines().subList(2, input.getLines().size());
    long total = 0;
    for (String line : listOfPatterns) {
      total += numberOfWaysToConstruct(line, listOfRugs);
    }

    return total;
  }
}
