package com.expedient.adventofcodejade.solutions;

import static java.lang.Math.abs;

import com.expedient.adventofcodejade.BaseSolution;
import com.expedient.adventofcodejade.common.PuzzleInput;
import java.util.Arrays;
import java.util.List;

public class SolutionDay2 extends BaseSolution {

  public SolutionDay2(PuzzleInput input, PuzzleInput sampleInputOne, PuzzleInput sampleInputTwo) {
    super(input, sampleInputOne, sampleInputTwo);
  }

  /**
   * I hate that this works and isn't even prohibitively slow. We're just taking the naive approach
   * of testing every possible variant of the given report. There's probably a faster way to do it
   * involving keeping track of how many errors there have been, but this works so whatever.
   *
   * @param nums the report
   * @return whether any variation of the report can be good
   */
  public static boolean testAllIterationsOfReport(List<Integer> nums) {
    for (int i = 0; i < nums.size(); i++) {
      if (isGoodReport(nums, i)) {
        return true;
      }
    }
    return false;
  }

  /**
   * Overload of isGoodReport that doesn't exclude any entry from the report (-1 index is never
   * used)
   *
   * @param nums the report
   * @return whether the report is good
   */
  public static boolean isGoodReport(List<Integer> nums) {
    return isGoodReport(nums, -1);
  }

  /**
   * Tells you whether the report is good. The logic will skip the given index, allowing us to test
   * variations of the given report that have any particular element removed w/o any copy operations
   *
   * @param nums the report
   * @param excludeIndex an index of an entry to skip in the report. negative numbers will have no
   *     effect
   * @return whether the report is good
   */
  public static boolean isGoodReport(List<Integer> nums, int excludeIndex) {
    int lastNum = nums.get(0);
    int startIndex = 1;
    boolean isIncreasing = true;
    boolean isDecreasing = true;
    if (excludeIndex == 0) {
      lastNum = nums.get(1);
      startIndex += 1;
    }
    for (int i = startIndex; i < nums.size(); i++) {
      if (i == excludeIndex) {
        continue;
      }
      int diff = abs(nums.get(i) - lastNum);
      if (nums.get(i) < lastNum) {
        isIncreasing = false;
      }
      if (nums.get(i) > lastNum) {
        isDecreasing = false;
      }
      if (diff > 3 || diff < 1) {
        return false;
      }
      lastNum = nums.get(i);
    }
    return isIncreasing || isDecreasing;
  }

  /**
   * Check is each line is a good report, and add up the count
   *
   * @param inputUsed the PuzzleInput to be used for the solution
   * @return string containing the number of good reports
   */
  @Override
  public String partOne(PuzzleInput inputUsed) {
    List<String> lines = inputUsed.getLines();
    int goodReports = 0;
    for (String line : lines) {
      List<Integer> splitLine = Arrays.stream(line.split(" ")).map(Integer::parseInt).toList();
      if (isGoodReport(splitLine)) {
        goodReports += 1;
      }
    }
    return Integer.toString(goodReports);
  }

  /**
   * Check is each line is a good report, including ones that may be made into good reports by
   * removing a single entry, then adds up the count
   *
   * @param inputUsed the PuzzleInput to be used for the solution
   * @return string containing the number of good reports
   */
  @Override
  public String partTwo(PuzzleInput inputUsed) {
    List<String> lines = inputUsed.getLines();
    int goodReports = 0;
    for (String line : lines) {
      List<Integer> splitLine = Arrays.stream(line.split(" ")).map(Integer::parseInt).toList();
      if (isGoodReport(splitLine)) {
        goodReports += 1;
      } else {
        if (testAllIterationsOfReport(splitLine)) {
          goodReports += 1;
        }
      }
    }
    return Integer.toString(goodReports);
  }
}
