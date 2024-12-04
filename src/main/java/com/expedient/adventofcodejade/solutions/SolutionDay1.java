package com.expedient.adventofcodejade.solutions;

import com.expedient.adventofcodejade.BaseSolution;
import com.expedient.adventofcodejade.common.PuzzleInput;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class SolutionDay1 extends BaseSolution {

  public SolutionDay1(PuzzleInput input, PuzzleInput sampleInputOne, PuzzleInput sampleInputTwo) {
    super(input, sampleInputOne, sampleInputTwo);
  }

  @Override
  public String partOne(PuzzleInput inputUsed) {
    List<List<Integer>> lists = inputUsed.getTwoLists(Integer::parseInt, "   ");
    List<Integer> listOne = lists.get(0);
    List<Integer> listTwo = lists.get(1);
    int totalDistance = 0;
    Collections.sort(listOne);
    Collections.sort(listTwo);
    for (int i = 0; i < listOne.size(); i++) {
      totalDistance += Math.abs(listOne.get(i) - listTwo.get(i));
    }
    return Integer.toString(totalDistance);
  }

  @Override
  public String partTwo(PuzzleInput inputUsed) {
    List<List<Integer>> lists = inputUsed.getTwoLists(Integer::parseInt, "   ");
    List<Integer> listOne = lists.get(0);
    List<Integer> listTwo = lists.get(1);
    ArrayList<Integer> listThree = new ArrayList<>();
    Collections.sort(listOne);
    Collections.sort(listTwo);
    for (int number : listOne) {
      listThree.add(number * Collections.frequency(listTwo, number));
    }
    return Integer.toString(listThree.stream().reduce(0, Integer::sum));
  }
}
