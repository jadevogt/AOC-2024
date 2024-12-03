package com.expedient.adventofcodejade.solutions;

import com.expedient.adventofcodejade.BaseSolution;
import com.expedient.adventofcodejade.common.PuzzleInput;

import java.util.ArrayList;
import java.util.Collections;

public class SolutionDay1 extends BaseSolution {

    public SolutionDay1(PuzzleInput input, PuzzleInput sampleInputOne, PuzzleInput sampleInputTwo) {
        super(input, sampleInputOne, sampleInputTwo);
    }

    @Override
    public String partOne(PuzzleInput inputUsed) {
        var lists = inputUsed.getTwoLists(Integer::parseInt, "   ");
        var listOne = lists.get(0);
        var listTwo = lists.get(1);
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
        var lists = inputUsed.getTwoLists(Integer::parseInt, "   ");
        var listOne = lists.get(0);
        var listTwo = lists.get(1);
        var listThree = new ArrayList<Integer>();
        Collections.sort(listOne);
        Collections.sort(listTwo);
        for (Integer integer : listOne) {
            listThree.add(integer * Collections.frequency(listTwo, integer));
        }
        return Integer.toString(listThree.stream().reduce(0, Integer::sum));
    }
}
