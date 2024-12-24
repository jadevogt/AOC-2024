package com.expedient.adventofcodejade.solutions.year2024;

import com.expedient.adventofcodejade.BaseSolution;
import com.expedient.adventofcodejade.common.Pair;
import com.expedient.adventofcodejade.common.PuzzleInput;
import com.expedient.adventofcodejade.util.StringTools;
import java.util.*;

public class SolutionDay21 extends BaseSolution {
  public SolutionDay21(PuzzleInput input, PuzzleInput sampleInputOne, PuzzleInput sampleInputTwo) {
    super(input, sampleInputOne, sampleInputTwo);
  }

  public static void printInputList(List<Input> inputs) {
    for (var input : inputs) {
      var ch =
          switch (input) {
            case UP -> '^';
            case DOWN -> 'v';
            case LEFT -> '<';
            case RIGHT -> '>';
            case ENTER -> 'A';
          };
      System.out.print(ch);
    }
    System.out.println("");
  }

  @Override
  public Object partOne(PuzzleInput input) {
    var lines = input.getLines();

    int total = 0;
    var numberPad = new NumberPad();
    var firstDirectionalPad = new DirectionalPad();
    var secondDirectionalPad = new DirectionalPad();
    for (var line : lines) {
      List<Input> allIns = new ArrayList<>();
      int num = Integer.parseInt(line.substring(0, 3));

      for (var c : StringTools.ToCharacterArray(line)) {

        var numPadInputs = numberPad.pressButton(c);
        for (var d1 : numPadInputs) {
          var d1PadInputs = firstDirectionalPad.fromInput(d1);
          for (var d2 : d1PadInputs) {
            allIns.addAll(secondDirectionalPad.fromInput(d2));
          }
        }
      }
      total += num * allIns.size();
    }
    return total;
  }

  @Override
  public Object partTwo(PuzzleInput input) {
    var lines = input.getLines();
    long total = 0;
    var numberPad = new NumberPad();
    int numDPads = 1;
    List<DirectionalPad> dPads = new ArrayList<>();
    for (int i = 0; i < numDPads; i++) {
      dPads.add(new DirectionalPad());
    }
    PriorityQueue<PadTask> padTasks = new PriorityQueue<>(new PadTaskComparator());
    for (var line : lines) {
      long t = 0;
      int num = Integer.parseInt(line.substring(0, 3));
      for (var c : StringTools.ToCharacterArray(line)) {
        var numPadInputs = numberPad.pressButton(c);
        for (var numPadInput : numPadInputs) {
          padTasks.add(new PadTask(numPadInput, 0));
          while (!padTasks.isEmpty()) {
            var task = padTasks.poll();
            if (task.padDepth < numDPads) {
              var result = dPads.get(task.padDepth).fromInput(task.input);
              for (int i = result.size() - 1; i >= 0; i--) {
                padTasks.add(new PadTask(result.get(i), task.padDepth + 1));
              }
            } else {
              t++;
            }
          }
        }
        total += t * num;
      }
    }
    return null;
  }

  public enum Input {
    UP,
    DOWN,
    LEFT,
    RIGHT,
    ENTER,
  }

  public record PadTask(Input input, Integer padDepth) {}

  public class NumberPad {
    private static Character[][] nums =
        new Character[][] {
          {'7', '8', '9'},
          {'4', '5', '6'},
          {'1', '2', '3'},
          {null, '0', 'A'}
        };
    private List<Input> currentInputs;
    private int currentCol;
    private int currentRow;
    private Input lastRequested;

    public NumberPad() {
      this.currentRow = 3;
      this.currentCol = 2;
      this.currentInputs = new ArrayList<>();
      this.lastRequested = null;
    }

    public Pair<Integer, Integer> findButton(Character button) {
      for (int row = 0; row < nums.length; row++) {
        for (int col = 0; col < nums[row].length; col++) {
          if (nums[row][col] == button) {
            return new Pair<>(row, col);
          }
        }
      }
      throw new RuntimeException();
    }

    public void checkValid() {
      if (currentRow < 0 || currentRow > nums.length) {
        throw new RuntimeException();
      }
      if (currentCol < 0 || currentCol > nums[currentRow].length) {
        throw new RuntimeException();
      }
      if (nums[currentRow][currentCol] == null) {
        throw new RuntimeException();
      }
    }

    public void moveUp() {
      currentRow--;
      checkValid();
      currentInputs.add(Input.UP);
      lastRequested = Input.UP;
    }

    public void moveDown() {
      currentRow++;
      checkValid();
      currentInputs.add(Input.DOWN);
      lastRequested = Input.DOWN;
    }

    public void moveLeft() {
      currentCol--;
      checkValid();
      currentInputs.add(Input.LEFT);
      lastRequested = Input.LEFT;
    }

    public void moveRight() {
      currentCol++;
      checkValid();
      currentInputs.add(Input.RIGHT);
      lastRequested = Input.RIGHT;
    }

    public List<Input> pressButton(Character desiredButton) {
      var location = findButton(desiredButton);
      int virtualRow = currentRow;
      int virtualCol = currentCol;
      List<Input> inputQueue = new ArrayList<>();
      while (nums[virtualRow][virtualCol] != desiredButton) {
        if (nums[virtualRow][location.two()] != null) {
          while (virtualCol > location.two()) {
            inputQueue.add(Input.LEFT);
            virtualCol--;
          }
        }
        while (virtualRow > location.one()) {
          inputQueue.add(Input.UP);
          virtualRow--;
        }
        while (virtualRow < location.one()) {
          inputQueue.add(Input.DOWN);
          virtualRow++;
        }

        while (virtualCol < location.two()) {
          inputQueue.add(Input.RIGHT);
          virtualCol++;
        }
      }
      PriorityQueue<Input> sortedInputs = new PriorityQueue<>(new NumberPad.InputComparator(null));
      sortedInputs.addAll(inputQueue);
      while (!sortedInputs.isEmpty()) {
        switch (sortedInputs.poll()) {
          case DOWN -> moveDown();
          case LEFT -> moveLeft();
          case RIGHT -> moveRight();
          case UP -> moveUp();
          default -> {}
        }
        var x = new PriorityQueue<>(new NumberPad.InputComparator(lastRequested));
        x.addAll(sortedInputs);
        sortedInputs = x;
      }
      currentInputs.add(Input.ENTER);
      var ins = currentInputs;
      currentInputs = new ArrayList<>();
      lastRequested = null;
      return ins;
    }

    private class InputComparator implements Comparator<Input> {
      private Input lastInserted;

      public InputComparator(Input lastInserted) {
        this.lastInserted = lastInserted;
      }

      @Override
      public int compare(Input o1, Input o2) {
        if (o1.equals(Input.LEFT) && nums[currentRow][currentCol - 1] == null) {
          return 1;
        }
        if (o2.equals(Input.LEFT) && nums[currentRow][currentCol - 1] == null) {
          return -1;
        }
        if (o1.equals(Input.DOWN) && nums[currentRow + 1][currentCol] == null) {
          return 1;
        }
        if (o2.equals(Input.DOWN) && nums[currentRow + 1][currentCol] == null) {
          return -1;
        }
        if (o1.equals(lastRequested)) {
          return -1;
        } else if (o2.equals(lastRequested)) {
          return 1;
        }
        return 0;
      }
    }
  }
  ;

  public class DirectionalPad {
    private static Character[][] dirs =
        new Character[][] {
          {null, '^', 'A'},
          {'<', 'v', '>'}
        };
    private List<Input> currentInputs;
    private int currentCol;
    private int currentRow;
    private Input lastRequested;

    public DirectionalPad() {
      this.currentRow = 0;
      this.currentCol = 2;
      this.currentInputs = new ArrayList<>();
      this.lastRequested = null;
    }

    public Character getCurrentSelected() {
      return dirs[currentRow][currentCol];
    }

    public Pair<Integer, Integer> findButton(Character button) {
      for (int row = 0; row < dirs.length; row++) {
        for (int col = 0; col < dirs[row].length; col++) {
          if (dirs[row][col] == button) {
            return new Pair<>(row, col);
          }
        }
      }
      throw new RuntimeException();
    }

    public void checkValid() {
      if (currentRow < 0 || currentRow > dirs.length) {
        throw new RuntimeException();
      }
      if (currentCol < 0 || currentCol > dirs[currentRow].length) {
        throw new RuntimeException();
      }
      if (dirs[currentRow][currentCol] == null) {
        throw new RuntimeException();
      }
    }

    public void moveUp() {
      currentRow--;
      checkValid();
      currentInputs.add(Input.UP);
      lastRequested = Input.UP;
    }

    public void moveDown() {
      currentRow++;
      checkValid();
      currentInputs.add(Input.DOWN);
      lastRequested = Input.DOWN;
    }

    public void moveLeft() {
      currentCol--;
      checkValid();
      currentInputs.add(Input.LEFT);
      lastRequested = Input.LEFT;
    }

    public void moveRight() {
      currentCol++;
      checkValid();
      currentInputs.add(Input.RIGHT);
      lastRequested = Input.RIGHT;
    }

    public List<Input> pressButton(Character desiredButton) {
      var location = findButton(desiredButton);
      int virtualRow = currentRow;
      int virtualCol = currentCol;
      List<Input> inputQueue = new ArrayList<>();
      while (dirs[virtualRow][virtualCol] != desiredButton) {
        while (virtualRow > location.one() && dirs[virtualRow - 1][virtualCol] != null) {
          inputQueue.add(Input.UP);
          virtualRow--;
        }
        while (virtualRow < location.one()) {
          inputQueue.add(Input.DOWN);
          virtualRow++;
        }
        while (virtualCol > location.two() && dirs[virtualRow][virtualCol - 1] != null) {
          inputQueue.add(Input.LEFT);
          virtualCol--;
        }
        while (virtualCol < location.two()) {
          inputQueue.add(Input.RIGHT);
          virtualCol++;
        }
      }
      PriorityQueue<Input> sortedInputs = new PriorityQueue<>(new InputComparator());
      sortedInputs.addAll(inputQueue);
      while (!sortedInputs.isEmpty()) {
        switch (sortedInputs.poll()) {
          case DOWN -> moveDown();
          case LEFT -> moveLeft();
          case RIGHT -> moveRight();
          case UP -> moveUp();
          default -> {}
        }
        var x = new PriorityQueue<>(new DirectionalPad.InputComparator());
        x.addAll(sortedInputs);
        sortedInputs = x;
      }
      currentInputs.add(Input.ENTER);
      var ins = currentInputs;
      currentInputs = new ArrayList<>();
      return ins;
    }

    public List<Input> fromInput(Input input) {
      var ch =
          switch (input) {
            case DOWN -> 'v';
            case LEFT -> '<';
            case UP -> '^';
            case RIGHT -> '>';
            case ENTER -> 'A';
          };
      return pressButton(ch);
    }

    private class InputComparator implements Comparator<Input> {
      @Override
      public int compare(Input o1, Input o2) {
        if (o1.equals(Input.LEFT) && dirs[currentRow][currentCol - 1] == null) {
          return 1;
        }
        if (o2.equals(Input.LEFT) && dirs[currentRow][currentCol - 1] == null) {
          return -1;
        }
        if (o1.equals(Input.UP) && dirs[currentRow - 1][currentCol] == null) {
          return 1;
        }
        if (o2.equals(Input.UP) && dirs[currentRow - 1][currentCol] == null) {
          return -1;
        }
        if (o1.equals(lastRequested)) {
          return -1;
        } else if (o2.equals(lastRequested)) {
          return 1;
        }
        return 0;
      }
    }
  }

  public class PadTaskComparator implements Comparator<PadTask> {
    @Override
    public int compare(PadTask o1, PadTask o2) {
      return o1.padDepth.compareTo(o2.padDepth);
    }
  }
}
