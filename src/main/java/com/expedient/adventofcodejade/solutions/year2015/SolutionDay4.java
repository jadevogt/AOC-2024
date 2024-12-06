package com.expedient.adventofcodejade.solutions.year2015;

import com.expedient.adventofcodejade.BaseSolution;
import com.expedient.adventofcodejade.common.PuzzleInput;
import com.expedient.adventofcodejade.util.StringTools;

import java.io.UnsupportedEncodingException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

public class SolutionDay4 extends BaseSolution {

  public SolutionDay4(PuzzleInput input, PuzzleInput sampleInputOne, PuzzleInput sampleInputTwo) {
    super(input, sampleInputOne, sampleInputTwo);
  }

  public static String findHashStartingWithZeroes(PuzzleInput input, int zeroCount) {
    MessageDigest md;
    String s = input.getString();
    try {
      md = MessageDigest.getInstance("MD5");
    } catch (NoSuchAlgorithmException e) {
      throw new RuntimeException(e);
    }
    int i = 0;
    while (true) {
      try {
        byte[] bytes = (s + i).getBytes("UTF-8");
        byte[] b = md.digest(bytes);
        String hash = StringTools.toHex(b);
        if (hash.startsWith("0".repeat(zeroCount))) {
          break;
        }
      } catch (UnsupportedEncodingException e) {
        throw new RuntimeException(e);
      }
      i++;
    }
    return Integer.toString(i);
  }

  @Override
  public String partOne(PuzzleInput input) {
    return "DISABLED";
    // return findHashStartingWithZeroes(input, 5);
  }

  @Override
  public String partTwo(PuzzleInput input) {
    return "DISABLED";
    // return findHashStartingWithZeroes(input, 6);
  }
}
