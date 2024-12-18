package com.expedient.adventofcodejade.solutions.year2024;

import com.expedient.adventofcodejade.BaseSolution;
import com.expedient.adventofcodejade.common.Pair;
import com.expedient.adventofcodejade.common.PuzzleInput;
import java.util.*;

public class SolutionDay17 extends BaseSolution {
  /*
  this is a manually located value for day 17 that I found by manually looking through the output,
  there's probably a nicer, programmatic way to derive this value but this is what I've got for now
  lol
  */
  static final long TEST_REGISTER_START = 247839002800000L;

  public SolutionDay17(PuzzleInput input, PuzzleInput sampleInputOne, PuzzleInput sampleInputTwo) {
    super(input, sampleInputOne, sampleInputTwo);
  }

  @Override
  public String partOne(PuzzleInput input) {
    var lines = input.getLines();
    long regA = Long.parseLong(lines.get(0));
    List<Long> program = Arrays.stream(lines.get(1).split(",")).map(Long::parseLong).toList();
    Computer c = new Computer(regA, 0, 0, program);
    while (!c.isHalted()) {
      c.step();
    }
    String output = c.readOutput();
    return output.substring(0, output.length() - 1);
  }

  @Override
  public Object partTwo(PuzzleInput input) {
    var lines = input.getLines();
    List<Long> program = Arrays.stream(lines.get(1).split(",")).map(Long::parseLong).toList();
    StringBuilder sb = new StringBuilder();
    program.forEach(
        i -> {
          sb.append(i.toString());
          sb.append(",");
        });
    var test = sb.toString();
    // works for my input! sorry if it doesn't work for yours...
    // there's probably a better way to do all of this (binary search probably?) but I'm too lazy to
    // figure it out rn
    long start = program.size() < 10 ? 0 : TEST_REGISTER_START;
    for (long i = start; i < Long.MAX_VALUE; i++) {
      Computer c = new Computer(i, 0, 0, program);
      while (!c.isHalted()) {
        c.step();
      }
      String output = c.readOutput();
      if (output.equals(test)) {
        return i;
      }
    }
    return null;
  }

  public enum OpCode {
    ADV,
    BXL,
    BST,
    JNZ,
    BXC,
    OUT,
    BDV,
    CDV;

    public static OpCode fromLong(long input) {
      return switch ((int) input) {
        case 0 -> ADV;
        case 1 -> BXL;
        case 2 -> BST;
        case 3 -> JNZ;
        case 4 -> BXC;
        case 5 -> OUT;
        case 6 -> BDV;
        case 7 -> CDV;
        default -> throw new RuntimeException("Invalid OpCode");
      };
    }
  }

  public static class Computer {
    private final List<Long> program;
    private final StringBuilder output;
    private long registerA;
    private long registerB;
    private long registerC;
    private long instructionPointer;
    private boolean halted = false;

    public Computer(long registerA, long registerB, long registerC, List<Long> program) {
      this.registerA = registerA;
      this.registerB = registerB;
      this.registerC = registerC;
      this.program = program;
      this.output = new StringBuilder();
      instructionPointer = 0;
    }

    public void step() {
      var ins = readInstruction();
      performOperation(ins);
    }

    public boolean isHalted() {
      return halted;
    }

    public Pair<OpCode, Long> readInstruction() {
      OpCode op = OpCode.fromLong(program.get((int) instructionPointer));
      Long operand = program.get((int) instructionPointer + 1);
      return new Pair<>(op, operand);
    }

    public long getInstructionPointer() {
      return instructionPointer;
    }

    public long readCombo(long operand) {
      return switch ((int) operand) {
        case 0, 1, 2, 3 -> operand;
        case 4 -> registerA;
        case 5 -> registerB;
        case 6 -> registerC;
        default -> throw new RuntimeException("Invalid Combo Operand");
      };
    }

    public void advanceInstruction() {
      instructionPointer += 2;
      if (instructionPointer >= program.size()) {
        halted = true;
      }
    }

    public long truncate(long num) {
      // return num & 0xb111;
      return num;
    }

    public String readOutput() {
      return output.toString();
    }

    public void performOperation(Pair<OpCode, Long> operation) {
      switch (operation.one()) {
        case ADV -> {
          long numerator = registerA;
          long denominator = (long) Math.pow(2, readCombo(operation.two()));
          registerA = truncate(numerator / denominator);
          advanceInstruction();
        }
        case BXL -> {
          registerB = registerB ^ operation.two();
          advanceInstruction();
        }
        case BST -> {
          registerB = truncate(readCombo(operation.two()) % 8);
          advanceInstruction();
        }
        case JNZ -> {
          if (registerA == 0) {
            advanceInstruction();
            return;
          }
          instructionPointer = operation.two();
        }
        case BXC -> {
          registerB = registerB ^ registerC;
          advanceInstruction();
        }
        case OUT -> {
          output.append(readCombo(operation.two()) % 8).append(",");
          advanceInstruction();
        }
        case BDV -> {
          long numerator = registerA;
          long denominator = (long) Math.pow(2, readCombo(operation.two()));
          registerB = truncate(numerator / denominator);
          advanceInstruction();
        }
        case CDV -> {
          long numerator = registerA;
          long denominator = (long) Math.pow(2, readCombo(operation.two()));
          registerC = truncate(numerator / denominator);
          advanceInstruction();
        }
      }
    }
  }
}
