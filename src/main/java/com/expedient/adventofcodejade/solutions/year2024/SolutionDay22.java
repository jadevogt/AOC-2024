package com.expedient.adventofcodejade.solutions.year2024;

import com.expedient.adventofcodejade.BaseSolution;
import com.expedient.adventofcodejade.common.PuzzleInput;
import java.util.*;

public class SolutionDay22 extends BaseSolution {
  public SolutionDay22(PuzzleInput input, PuzzleInput sampleInputOne, PuzzleInput sampleInputTwo) {
    super(input, sampleInputOne, sampleInputTwo);
  }

  public static long mix(Long one, Long two) {
    return one ^ two;
  }

  public static long prune(Long num) {
    return num % 16777216;
  }

  public static long calculateNext(long num) {
    long newNum = prune(mix(num * 64, num));
    newNum = prune(mix((newNum / 32), newNum));
    newNum = prune(mix((newNum * 2048), newNum));
    return newNum;
  }

  @Override
  public Object partOne(PuzzleInput input) {
    long total = 0;
    for (var num : input.getLines().stream().map(Long::parseLong).toList()) {
      for (int i = 0; i < 2000; i++) {
        num = calculateNext(num);
      }
      total += num;
    }
    return total;
  }

  @Override
  public Object partTwo(PuzzleInput input) {
    List<List<Integer>> pricesList = new ArrayList<>();
    for (var num : input.getLines().stream().map(Long::parseLong).toList()) {
      List<Integer> prices = new ArrayList<>();
      prices.add((int) (num % 10));
      for (int i = 0; i < 2000; i++) {
        num = calculateNext(num);
        prices.add((int) (num % 10));
      }
      pricesList.add(prices);
    }
    HashMap<PriceChanges, Integer> changes = new HashMap<>();
    for (var list : pricesList) {
      Set<PriceChanges> found = new HashSet<>();
      for (int i = 0; i < 1997; i++) {
        int one = list.get(i + 1) - list.get(i);
        int two = list.get(i + 2) - list.get(i + 1);
        int three = list.get(i + 3) - list.get(i + 2);
        int four = list.get(i + 4) - list.get(i + 3);
        PriceChanges c = new PriceChanges(one, two, three, four);
        if (found.contains(c)) {
          continue;
        } else {
          found.add(c);
        }
        if (!changes.containsKey(c)) {
          changes.put(c, 0);
        }
        changes.put(c, changes.get(c) + list.get(i + 4));
      }
    }
    var comp = new PriceChangesComparator(changes);
    return changes.get(changes.keySet().stream().sorted(comp).toList().getLast());
  }

  public record PriceChanges(Integer one, Integer two, Integer three, Integer four) {}

  public static class PriceChangesComparator implements Comparator<PriceChanges> {
    private final Map<PriceChanges, Integer> changesMap;

    public PriceChangesComparator(Map<PriceChanges, Integer> changesMap) {
      this.changesMap = changesMap;
    }

    @Override
    public int compare(PriceChanges o1, PriceChanges o2) {
      int totalOne = changesMap.get(o1);
      int totalTwo = changesMap.get(o2);
      return Integer.compare(totalOne, totalTwo);
    }
  }
}
