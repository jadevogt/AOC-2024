package com.expedient.adventofcodejade.solutions.year2023;

import com.expedient.adventofcodejade.BaseSolution;
import com.expedient.adventofcodejade.common.Pair;
import com.expedient.adventofcodejade.common.PuzzleInput;
import com.expedient.adventofcodejade.util.StringTools;

import java.util.Arrays;
import java.util.List;

public class SolutionDay12 extends BaseSolution {
  public SolutionDay12(PuzzleInput input, PuzzleInput sampleInputOne, PuzzleInput sampleInputTwo) {
    super(input, sampleInputOne, sampleInputTwo);
  }

  @Override
  public Object partOne(PuzzleInput input) {
    List<String> lines = input.getLines();
    List<Pair<Character[], List<Integer>>> data =
        lines.stream()
            .map(s -> s.split(" "))
            .map(
                i ->
                    new Pair<>(
                        StringTools.ToCharacterArray(i[0]),
                        Arrays.stream(i[1].split(",")).map(Integer::parseInt).toList()))
            .toList();
    return null;
  }

  @Override
  public Object partTwo(PuzzleInput input) {
    return null;
  }
}
