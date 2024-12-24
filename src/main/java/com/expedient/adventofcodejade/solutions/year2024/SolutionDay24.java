package com.expedient.adventofcodejade.solutions.year2024;

import com.expedient.adventofcodejade.BaseSolution;
import com.expedient.adventofcodejade.common.PuzzleInput;
import java.util.*;

public class SolutionDay24 extends BaseSolution {
  public SolutionDay24(PuzzleInput input, PuzzleInput sampleInputOne, PuzzleInput sampleInputTwo) {
    super(input, sampleInputOne, sampleInputTwo);
  }

  @Override
  public Object partOne(PuzzleInput input) {
    var lines = input.getLines();
    Map<String, Boolean> initialStates = new HashMap<>();
    int blankLineIdx = 0;
    for (int i = 0; i < lines.size(); i++) {
      if (lines.get(i).isBlank()) {
        blankLineIdx = i;
        break;
      }
      var gateName = lines.get(i).split(": ")[0];
      var gateValue = Objects.equals(lines.get(i).split(": ")[1], "1");
      initialStates.put(gateName, gateValue);
    }
    Queue<Connection> connections = new LinkedList<>();
    for (int i = blankLineIdx + 1; i < lines.size(); i++) {
      var split = lines.get(i).split(" ");
      var gateOneName = split[0];
      var connectionName = split[1];
      var gateTwoName = split[2];
      var outputGateName = split[4];
      if (!initialStates.containsKey(outputGateName)) {
        initialStates.put(outputGateName, null);
      }
      Relationship r =
          switch (connectionName) {
            case "XOR" -> Relationship.XOR;
            case "AND" -> Relationship.AND;
            case "OR" -> Relationship.OR;
            default -> throw new RuntimeException();
          };
      connections.add(new Connection(List.of(gateOneName, gateTwoName), r, outputGateName));
    }
    while (!connections.isEmpty()) {
      var conn = connections.poll();
      if (conn.inputGates.stream().anyMatch(n -> initialStates.get(n) == null)) {
        connections.add(conn);
        continue;
      }
      var result =
          switch (conn.relationship) {
            case OR ->
                initialStates.get(conn.inputGates.get(0))
                    || initialStates.get(conn.inputGates.get(1));
            case XOR ->
                initialStates.get(conn.inputGates.get(0))
                    ^ initialStates.get(conn.inputGates.get(1));
            case AND ->
                initialStates.get(conn.inputGates.get(0))
                    && initialStates.get(conn.inputGates.get(1));
          };
      initialStates.put(conn.outputGate, result);
    }
    var sorted =
        initialStates.keySet().stream().sorted().filter(i -> i.startsWith("z")).toList().reversed();
    StringBuilder sb = new StringBuilder();
    for (String s : sorted) {
      sb.append(initialStates.get(s) ? "1" : "0");
    }
    return Long.parseLong(sb.toString(), 2);
  }

  /**
   * This isn't really a "solution" so much as it's a program that will construct half-adders and
   * then look backwards from the outputs and find setups that seem sus. Just ctrl+F and use your
   * knowledge of <a href="https://en.wikipedia.org/wiki/Adder_(electronics))">math circuits</a> to
   * figure out what wires need to be swapped around
   *
   * @param input the PuzzleInput to be used for the solution
   * @return string with a list of sus wires
   */
  @Override
  public Object partTwo(PuzzleInput input) {
    if (input.isTest()) {
      return "SKIPPED: Solution not available with test inputs";
    }
    var lines = input.getLines();
    Map<String, Boolean> initialStates = new HashMap<>();
    int blankLineIdx = 0;
    for (int i = 0; i < lines.size(); i++) {
      if (lines.get(i).isBlank()) {
        blankLineIdx = i;
        break;
      }
      var gateName = lines.get(i).split(": ")[0];
      var gateValue = Objects.equals(lines.get(i).split(": ")[1], "1");
      initialStates.put(gateName, gateValue);
    }
    List<Connection> connections = new ArrayList<>();
    HashMap<Set<String>, String> xorConnections = new HashMap<>();
    HashMap<Set<String>, String> andConnections = new HashMap<>();
    HashMap<Set<String>, String> orConnections = new HashMap<>();
    for (int i = blankLineIdx + 1; i < lines.size(); i++) {
      var split = lines.get(i).split(" ");
      var gateOneName = split[0];
      var connectionName = split[1];
      var gateTwoName = split[2];
      var outputGateName = split[4];
      Relationship r =
          switch (connectionName) {
            case "XOR" -> {
              xorConnections.put(Set.of(gateOneName, gateTwoName), outputGateName);
              yield Relationship.XOR;
            }
            case "AND" -> {
              andConnections.put(Set.of(gateOneName, gateTwoName), outputGateName);
              yield Relationship.AND;
            }
            case "OR" -> {
              orConnections.put(Set.of(gateOneName, gateTwoName), outputGateName);
              yield Relationship.OR;
            }
            default -> throw new RuntimeException();
          };
      connections.add(new Connection(List.of(gateOneName, gateTwoName), r, outputGateName));
    }
    List<HalfAdder> halfAdders = new ArrayList<>();
    for (int i = 0; i < 100; i++) {
      var num = String.format("%02d", i);
      if (initialStates.containsKey("x" + num) && initialStates.containsKey("y" + num)) {
        var sumOut = xorConnections.get(Set.of("x" + num, "y" + num));
        var carryOut = andConnections.get(Set.of("x" + num, "y" + num));
        halfAdders.add(new HalfAdder(Set.of("x" + num, "y" + num), sumOut, carryOut));
      }
    }
    List<Output> outputs = new ArrayList<>();
    List<String> investigate = new ArrayList<>();
    for (int i = 0; i < halfAdders.size(); i++) {
      var num = String.format("%02d", i);
      var halfAdder = halfAdders.get(i);
      try {
        var conn =
            xorConnections.keySet().stream()
                .filter(k -> xorConnections.get(k).equals("z" + num))
                .findFirst()
                .orElseThrow();
        outputs.add(new Output(conn, "z" + num));
        if (!conn.contains(halfAdder.sumWire) && i != 0) {
          investigate.add("z" + num);
          investigate.add(halfAdder.sumWire);
        }
      } catch (NoSuchElementException e) {
        var sumWire = xorConnections.get(Set.of("x" + num, "y" + num));
        investigate.add("z" + num);
        investigate.add(sumWire);
      }
    }
    return "Investigate the following wires manually in the input: " + investigate;
  }

  public enum Relationship {
    OR,
    XOR,
    AND,
  }

  public record Connection(List<String> inputGates, Relationship relationship, String outputGate) {}

  public record HalfAdder(Set<String> inputs, String sumWire, String carryWire) {}

  public record Output(Set<String> inputs, String outputWire) {}
}
