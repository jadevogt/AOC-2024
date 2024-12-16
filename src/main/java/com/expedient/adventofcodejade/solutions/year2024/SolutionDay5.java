package com.expedient.adventofcodejade.solutions.year2024;

import com.expedient.adventofcodejade.BaseSolution;
import com.expedient.adventofcodejade.common.Pair;
import com.expedient.adventofcodejade.common.PuzzleInput;
import java.util.*;

public class SolutionDay5 extends BaseSolution {
  public SolutionDay5(PuzzleInput input, PuzzleInput sampleInputOne, PuzzleInput sampleInputTwo) {
    super(input, sampleInputOne, sampleInputTwo);
  }

  /**
   * Takes in a list of Integer pairs, and uses them to assemble Rules (Map with Integer keys that
   * correspond to a pair of Sets, one with Integers that should come before the key, and one with
   * Integers that should come after the key.)
   *
   * @param rules a list of Integer pairs from the input
   * @return the Rule List
   */
  private Map<Integer, Pair<Set<Integer>, Set<Integer>>> getRules(
      List<Pair<Integer, Integer>> rules) {
    Map<Integer, Pair<Set<Integer>, Set<Integer>>> ruleLists = new HashMap<>();
    for (Pair<Integer, Integer> rule : rules) {
      Integer number = rule.one();
      int after = rule.two();
      if (!ruleLists.containsKey(number)) {
        ruleLists.put(number, new Pair<>(new HashSet<>(), new HashSet<>()));
      }
      if (!ruleLists.containsKey(after)) {
        ruleLists.put(after, new Pair<>(new HashSet<>(), new HashSet<>()));
      }
      ruleLists.get(number).two().add(after);
      ruleLists.get(after).one().add(number);
    }
    return ruleLists;
  }

  /**
   * Gets updates and rules from the input, filters out any update lists that are not equal to their
   * sorted counterpart.
   *
   * @param input the PuzzleInput
   * @return all updates from the input that are already sorted
   */
  private List<List<Integer>> getGoodUpdates(PuzzleInput input) {
    Pair<List<Pair<Integer, Integer>>, List<List<Integer>>> lists = input.day5Input();
    List<List<Integer>> updates = lists.two();
    Map<Integer, Pair<Set<Integer>, Set<Integer>>> ruleLists = getRules(lists.one());
    Comparator<Integer> comp = new UpdateComparator(ruleLists);
    return updates.stream().filter(i -> i.stream().sorted(comp).toList().equals(i)).toList();
  }

  /**
   * Gets all the sorted updates, and then provides the sum of all middle numbers from each good
   * update list
   *
   * @param input the PuzzleInput to be used for the solution
   * @return the sum of middle numbers from the good updates
   */
  @Override
  public Integer partOne(PuzzleInput input) {
    var good = getGoodUpdates(input);
    return good.stream().map(g -> g.get(g.size() / 2)).reduce(0, Integer::sum);
  }

  /**
   * Finds all update lists that are not already sorted, then sorts them and sums up the middle
   * number from each one.
   *
   * @param input the PuzzleInput to be used for the solution
   * @return the sum of all middle numbers from the sorted versions of each update list
   */
  @Override
  public Integer partTwo(PuzzleInput input) {
    var good = getGoodUpdates(input);
    var lists = input.day5Input();
    Comparator<Integer> comp = new UpdateComparator(getRules(lists.one()));
    return lists.two().stream()
        .filter(i -> !good.contains(i))
        .map(i -> i.stream().sorted(comp).toList())
        .map(i -> i.get(i.size() / 2))
        .reduce(Integer::sum)
        .orElseThrow(IllegalArgumentException::new);
  }

  /**
   * Class that uses Rules (Map of Integer keys and a Pair of Integer sets) to provide a comparison
   * method that can be used to sort a list of Integers according to the Rules
   *
   * @param rules
   */
  private record UpdateComparator(Map<Integer, Pair<Set<Integer>, Set<Integer>>> rules)
      implements Comparator<Integer> {

    @Override
    public int compare(Integer o1, Integer o2) {
      if (rules.containsKey(o1)) {
        if (rules.get(o1).two().contains(o2)) {
          return -1;
        } else if (rules.get(o1).one().contains(o2)) {
          return 1;
        }
      } else if (rules.containsKey(o2)) {
        if (rules.get(o2).two().contains(o1)) {
          return 1;
        } else if (rules.get(o2).one().contains(o1)) {
          return -1;
        }
      }
      return 0;
    }
  }
}
