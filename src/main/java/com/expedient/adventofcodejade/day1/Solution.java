package com.expedient.adventofcodejade.day1;

import com.expedient.adventofcodejade.util.PuzzleInput;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;

public class Solution {
    public static void main(String[] args) throws IOException {
       var input = new PuzzleInput("inputs/inputOne");
       var solutionOne = partOne(input);
       var solutionTwo = partTwo(input);
       System.out.println("Part one solution: " + solutionOne);
       System.out.println("Part two solution: " + solutionTwo);
    }

    public static int partOne(PuzzleInput input) {
        var lists = input.getTwoLists(Integer::parseInt, "   ");
        var listOne = lists.get(0);
        var listTwo = lists.get(1);
        int totalDistance = 0;
        Collections.sort(listOne);
        Collections.sort(listTwo);
        for (int i = 0; i < listOne.size(); i++) {
            totalDistance += Math.abs(listOne.get(i) - listTwo.get(i));
        }
        return totalDistance;
    }

    public static int partTwo(PuzzleInput input) {
        var lists = input.getTwoLists(Integer::parseInt, "   ");
        var listOne = lists.get(0);
        var listTwo = lists.get(1);
        var listThree = new ArrayList<Integer>();
        Collections.sort(listOne);
        Collections.sort(listTwo);
        for (Integer integer : listOne) {
            listThree.add(integer * Collections.frequency(listTwo, integer));
        }
        return listThree.stream().reduce(0, Integer::sum);
    }
}
