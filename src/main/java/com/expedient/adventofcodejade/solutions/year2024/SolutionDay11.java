package com.expedient.adventofcodejade.solutions.year2024;

import com.expedient.adventofcodejade.BaseSolution;
import com.expedient.adventofcodejade.common.PuzzleInput;
import java.util.*;

public class SolutionDay11 extends BaseSolution {
  /*

   If the stone is engraved with the number 0, it is replaced by a stone engraved with the number 1.

   If the stone is engraved with a number that has an even number of digits, it is replaced by two stones.
   The left half of the digits are engraved on the new left stone, and the right half of the digits
   are engraved on the new right stone. (The new numbers don't keep extra leading zeroes: 1000 would become stones 10 and 0.)

   If none of the other rules apply, the stone is replaced by a new stone; the old stone's number multiplied by 2024
   is engraved on the new stone.
  */
  public SolutionDay11(PuzzleInput input, PuzzleInput sampleInputOne, PuzzleInput sampleInputTwo) {
    super(input, sampleInputOne, sampleInputTwo);
  }

  public static void applyToHashMap(HashMap<Long, Long> input) {
    var dupe = new HashMap<>(input);
    input.clear();
    for (var key : dupe.keySet()) {
      var amt = dupe.get(key);
      // Handle Rule 1
      if (key == 0) {
        input.put(1L, input.getOrDefault(1L, 0L) + amt);
      }
      // Handle Rule 2
      else if (key.toString().length() % 2 == 0) {
        var str = key.toString();
        final int mid = str.length() / 2; // get the middle of the String
        String[] parts = {str.substring(0, mid), str.substring(mid)};
        var parsedOne = Long.parseLong(parts[0]);
        var parsedTwo = Long.parseLong(parts[1]);
        input.put(parsedOne, input.getOrDefault(parsedOne, 0L) + amt);
        input.put(parsedTwo, input.getOrDefault(parsedTwo, 0L) + amt);
      }
      // Handle Rule 3
      else {
        input.put(key * 2024, input.getOrDefault(key * 2024, 0L) + amt);
      }
    }
  }

  public Long runBlinkIterations(PuzzleInput input, int iterationCount) {
    final List<Long> nums =
        Arrays.stream(input.getLines().get(0).split(" ")).map(Long::parseLong).toList();
    HashMap<Long, Long> numberMap = new HashMap<>();
    for (var num : nums) {
      if (numberMap.containsKey(num)) {
        numberMap.put(num, numberMap.get(num) + 1);
      } else {
        numberMap.put(num, 1L);
      }
    }
    for (int i = 0; i < iterationCount; i++) {
      applyToHashMap(numberMap);
      Long minitotal = 0L;
      for (var key : numberMap.keySet()) {
        minitotal += numberMap.get(key);
      }
      System.out.println("Mini Total for iterations (" + (i + 1) + "): " + minitotal);
      System.out.println(numberMap);
    }
    Long total = 0L;
    for (var key : numberMap.keySet()) {
      total += numberMap.get(key);
    }
    return total;
  }

  @Override
  public Long partOne(PuzzleInput input) {
    return runBlinkIterations(input, 25);
  }

  @Override
  public Long partTwo(PuzzleInput input) {
    return null;
    // return runBlinkIterations(input, 75);
  }
}
