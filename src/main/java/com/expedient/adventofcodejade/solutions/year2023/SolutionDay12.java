package com.expedient.adventofcodejade.solutions.year2023;

import com.expedient.adventofcodejade.BaseSolution;
import com.expedient.adventofcodejade.common.Pair;
import com.expedient.adventofcodejade.common.PuzzleInput;
import com.expedient.adventofcodejade.util.StringTools;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

public class SolutionDay12 extends BaseSolution {
  public SolutionDay12(PuzzleInput input, PuzzleInput sampleInputOne, PuzzleInput sampleInputTwo) {
    super(input, sampleInputOne, sampleInputTwo);
  }

  public static int checkAllPermutations(String input, int index, List<Integer> data) {
    if (index == input.length() - 1) {
      boolean good = true;
      for (var d : data) {
        if (StringTools.getMatcher(input.trim(), "(^|\\.)\\#{" + d.toString() + "}($|\\.)")
                .results()
                .count()
            < Collections.frequency(data, d)) good = false;
      }
      if (good) return 1;
      return 0;
    } else {
      int total = 0;
      char[] variant1 = input.toCharArray();
      if (!(variant1[index] == '?')) {
        total += checkAllPermutations(String.valueOf(variant1), index + 1, data);
      } else {
        variant1[index] = '#';
        char[] variant2 = input.toCharArray();
        variant2[index] = '.';
        total += checkAllPermutations(String.valueOf(variant1), index + 1, data);
        total += checkAllPermutations(String.valueOf(variant2), index + 1, data);
      }
      return total;
    }
  }

  @Override
  public Object partOne(PuzzleInput input) {
    List<String> lines = input.getLines();
    List<Pair<String, List<Integer>>> data =
        lines.stream()
            .map(s -> s.split(" "))
            .map(
                i ->
                    new Pair<>(
                        i[0], Arrays.stream(i[1].split(",")).map(Integer::parseInt).toList()))
            .toList();
    int total = 0;
    for (var d : data) {
      total += checkAllPermutations(d.one(), 0, d.two());
    }
    return total;
  }

  @Override
  public Object partTwo(PuzzleInput input) {
    return null;
  }
}
