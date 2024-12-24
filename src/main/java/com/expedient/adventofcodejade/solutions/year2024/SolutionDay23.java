package com.expedient.adventofcodejade.solutions.year2024;

import com.expedient.adventofcodejade.BaseSolution;
import com.expedient.adventofcodejade.common.PuzzleInput;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Set;
import java.util.stream.Collectors;

public class SolutionDay23 extends BaseSolution {
  public SolutionDay23(PuzzleInput input, PuzzleInput sampleInputOne, PuzzleInput sampleInputTwo) {
    super(input, sampleInputOne, sampleInputTwo);
  }

  public boolean hasThreeInCommon(Set<String> one, Set<String> two) {
    var found = one.stream().filter(two::contains).count() >= 3;
    return found;
  }

  public Set<String> threeThatAreInCommon(Set<String> one, Set<String> two, Set<String> three) {
    var newSet = new HashSet<String>();
    for (String s : one) {
      if (two.contains(s) && three.contains(s)) {
        newSet.add(s);
      }
    }
    return newSet;
  }

  @Override
  public Object partOne(PuzzleInput input) {
    Set<Set<String>> connections = new HashSet<>();
    for (String line : input.getLines()) {
      String pc = line.split("-")[0];
      String other = line.split("-")[1];
      connections.add(Set.of(pc, other));
    }
    Set<Set<String>> networks = new HashSet<>();
    for (var i : connections) {
      for (var j : connections) {
        if (i.equals(j)) continue;
        if (i.stream().noneMatch(x -> j.contains(x))) continue;
        var network = new HashSet<String>();
        network.addAll(i);
        network.addAll(j);
        networks.add(network);
      }
    }
    Set<Set<String>> tripleNetworks = new HashSet<>();
    for (var network : networks) {
      if (connections.stream().filter(i -> network.containsAll(i)).count() >= 3) {
        tripleNetworks.add(network);
      }
    }
    return tripleNetworks.stream().filter(s -> s.stream().anyMatch(i -> i.startsWith("t"))).count();
  }

  /**
   * Finds <a href="https://en.wikipedia.org/wiki/Clique_problem">maximal clique</a> starting from
   * each computer in the list, saving the largest one encountered so far. Returns a string joining
   * all PCs in the sorted largest clique
   *
   * @param input the PuzzleInput to be used for the solution
   * @return joined string listing all PCs in the largest LAN party (alphabetical order)
   */
  @Override
  public Object partTwo(PuzzleInput input) {
    HashMap<String, Set<String>> connections = new HashMap<>();
    for (String line : input.getLines()) {
      String pc = line.split("-")[0];
      String other = line.split("-")[1];
      if (!connections.containsKey(pc)) {
        connections.put(pc, new HashSet<>());
      }
      if (!connections.containsKey(other)) {
        connections.put(other, new HashSet<>());
      }
      connections.get(other).add(pc);
      connections.get(pc).add(other);
    }
    Set<String> largestClique = new HashSet<>();
    for (var current : connections.keySet()) {
      var clique = new HashSet<String>();
      clique.add(current);
      for (var key : connections.keySet()) {
        if (connections.get(key).containsAll(clique)) {
          clique.add(key);
        }
      }
      if (clique.size() > largestClique.size()) {
        largestClique = clique;
      }
    }
    return largestClique.stream().sorted().collect(Collectors.joining(","));
  }
}
