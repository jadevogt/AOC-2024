package com.expedient.adventofcodejade.solutions.year2015;

import com.expedient.adventofcodejade.BaseSolution;
import com.expedient.adventofcodejade.common.PuzzleInput;
import com.expedient.adventofcodejade.common.RectangularPrism;
import java.util.Arrays;

public class SolutionDay2 extends BaseSolution {
  public SolutionDay2(PuzzleInput input, PuzzleInput sampleInputOne, PuzzleInput sampleInputTwo) {
    super(input, sampleInputOne, sampleInputTwo);
  }

  @Override
  public String partOne(PuzzleInput input) {
    return input.getLines().stream()
        .map(i -> Arrays.stream(i.split("x")).map(Integer::parseInt).toList())
        .map(i -> new RectangularPrism(i.get(0), i.get(1), i.get(2)))
        .map(i -> i.surfaceArea() + i.smallestSurfaceArea())
        .reduce(Integer::sum)
        .orElseThrow(IllegalArgumentException::new)
        .toString();
  }

  @Override
  public String partTwo(PuzzleInput input) {
    return input.getLines().stream()
        .map(i -> Arrays.stream(i.split("x")).map(Integer::parseInt).toList())
        .map(i -> new RectangularPrism(i.get(0), i.get(1), i.get(2)))
        .map(i -> i.volume() + i.smallestPerimeter())
        .reduce(Integer::sum)
        .orElseThrow(IllegalArgumentException::new)
        .toString();
  }
}
