package com.expedient.adventofcodejade.solutions.year2024;

import com.expedient.adventofcodejade.BaseSolution;
import com.expedient.adventofcodejade.common.PuzzleInput;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class SolutionDay1 extends BaseSolution {

  public SolutionDay1(PuzzleInput input, PuzzleInput sampleInputOne, PuzzleInput sampleInputTwo) {
    super(input, sampleInputOne, sampleInputTwo);
  }

  /**
   * Get two Integer lists, corresponding to each column of the input. Sort both lists. Compare each
   * item in each list and add up the total distance.
   *
   * @param inputUsed the PuzzleInput to be used for the solution
   * @return string with the total distance between the numbers in each list
   */
  @Override
  public Integer partOne(PuzzleInput inputUsed) {
    List<List<Integer>> lists = inputUsed.getTwoLists(Integer::parseInt, "   ");
    List<Integer> listOne = lists.get(0);
    List<Integer> listTwo = lists.get(1);
    int totalDistance = 0;
    Collections.sort(listOne);
    Collections.sort(listTwo);
    for (int i = 0; i < listOne.size(); i++) {
      totalDistance += Math.abs(listOne.get(i) - listTwo.get(i));
    }
    return totalDistance;
  }

  /**
   * Get two Integer lists, corresponding to each column of the input. Create a third Integer list.
   * Sort both lists, then populate it with each number in the first list multiplied by the number
   * of times it appears in the second.
   *
   * @param inputUsed the PuzzleInput to be used for the solution
   * @return the sum of frequencies of the numbers in column 1 in column 2
   */
  @Override
  public Integer partTwo(PuzzleInput inputUsed) {
    List<List<Integer>> lists = inputUsed.getTwoLists(Integer::parseInt, "   ");
    List<Integer> listOne = lists.get(0);
    List<Integer> listTwo = lists.get(1);
    ArrayList<Integer> listThree = new ArrayList<>();
    Collections.sort(listOne);
    Collections.sort(listTwo);
    for (int number : listOne) {
      listThree.add(number * Collections.frequency(listTwo, number));
    }
    return listThree.stream().reduce(0, Integer::sum);
  }
}
