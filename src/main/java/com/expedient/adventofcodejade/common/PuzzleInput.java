package com.expedient.adventofcodejade.common;

import com.expedient.adventofcodejade.util.StringTools;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.function.Function;
import java.util.function.Predicate;
import java.util.stream.Collectors;

/** Handles both sample and actual input files */
public class PuzzleInput {
  private final List<String> fileLines;
  private final boolean isTest;

  private PuzzleInput(String inputString, boolean isTest) {
    this.fileLines = Arrays.stream(inputString.split("\n")).map(String::trim).toList();
    this.isTest = isTest;
  }

  private PuzzleInput(String inputString) {
    this(inputString, false);
  }

  /**
   * Constructs a PuzzleInput from a given filesystem path
   *
   * @param inputPath path where the input is located
   * @return PuzzleInput object that contains the file's contents
   * @throws IOException when the file is not found
   */
  public static PuzzleInput fromPath(String inputPath) throws IOException {
    File puzzleInputFile = new File(inputPath);
    return new PuzzleInput(Files.readString(puzzleInputFile.toPath()));
  }

  /**
   * Constructs a PuzzleInput from the given resource path
   *
   * @param resourcePath path of resource (contained in /src/main/resources)
   * @return PuzzleInput object that contains the resource's contents
   * @throws IOException when the given resource can't be loaded
   */
  public static PuzzleInput fromResource(String resourcePath) throws IOException {
    try (InputStream in = PuzzleInput.class.getClassLoader().getResourceAsStream(resourcePath)) {
      if (in == null) {
        throw new IOException("Resource not found");
      }
      return new PuzzleInput(new String(in.readAllBytes(), StandardCharsets.UTF_8), true);
    }
  }

  /**
   * Gets a PuzzleInput using the resource for a specific day (located in /src/main/resources). Will
   * first attempt to find sample input with the naming scheme [day]-[part] in case sample differs
   * between parts 1 and 2, otherwise falls back to input with the naming scheme [day]
   *
   * @param day day for which the input is loaded
   * @param partOne whether the sample is being fetched for part one
   * @return PuzzleInput object containing the given day's sample input
   * @throws IOException when the given day's input cannot be found in resources
   */
  public static PuzzleInput sampleForDay(int year, int day, boolean partOne) throws IOException {
    try {
      int suffix = partOne ? 1 : 2;
      return PuzzleInput.fromResource("samples/%d/%d-%d".formatted(year, day, suffix));
    } catch (IOException e) {
      return PuzzleInput.fromResource("samples/%d/%d".formatted(year, day));
    }
  }

  public boolean isTest() {
    return isTest;
  }

  /**
   * Get the lines from the file
   *
   * @return a list of strings containing the input as-is
   */
  public List<String> getLines() {
    return this.fileLines;
  }

  /**
   * Get the input as a single string, rather than as a list of lines
   *
   * @return String containing the entire input
   */
  public String getString() {
    return String.join("\n", fileLines);
  }

  /**
   * Return two lists based on split lines from the input
   *
   * @param conversion function used to convert the string into a type of your choice
   * @param delimiterRegex delimiter used to split the lines
   * @param <T> the type contained within the returned lists
   * @return two lists, composed of the elements that were split from each input
   */
  public <T> List<List<T>> getTwoLists(Function<String, T> conversion, String delimiterRegex) {
    ArrayList<T> listOne = new ArrayList<>();
    ArrayList<T> listTwo = new ArrayList<>();
    for (String line : this.fileLines) {
      String[] split = line.split(delimiterRegex);
      listOne.add(conversion.apply(split[0]));
      listTwo.add(conversion.apply(split[1]));
    }
    // This has to be a list, can't create an array of a Generic type
    return Arrays.asList(listOne, listTwo);
  }

  /**
   * Converts each line to a list of type T values, after iterating over each Character in the line
   * and applying a provided filter function. Optionally can apply a transformation to the line
   * string before running the filter.
   *
   * @param test Test to determine whether a given value will be included in the list for each line
   * @param conversion Function to convert the Character value to type T
   * @param transform Function to apply to each line before processing it. If this is null, the
   *     transformation step is skipped
   * @param <T> The desired output type for the operation
   * @return List of lists composed of values from each line matching the test function and
   *     converted to type T
   */
  public <T> List<List<T>> allMatchesPerLine(
      Predicate<Character> test,
      Function<Character, T> conversion,
      Function<String, String> transform) {
    ArrayList<List<T>> parsed = new ArrayList<>();
    for (String line : this.fileLines) {
      String l = line;
      if (transform != null) {
        l = transform.apply(l);
      }
      Character[] chars = StringTools.ToCharacterArray(l);
      parsed.add(Arrays.stream(chars).filter(test).map(conversion).toList());
    }
    return parsed;
  }

  /**
   * Returns the input as a rectangular 2D array of Character. Can't use generics because generic
   * array creation is not possible. The input must not be empty and must be composed of equally
   * sized rows.
   *
   * @return 2D array of Character derived from input
   * @throws IllegalArgumentException if the array would be ragged / the rows are of differing
   *     lengths
   */
  public Grid<Character> getGrid() throws IllegalArgumentException {
    return Grid.fromStringList(fileLines);
  }

  /**
   * Converts all starting lines with numbers separated by '|' characters into Pairs of Integers,
   * then creates a List of Lists containing Integers based on the comma separated lists of numbers
   * in the second half of the input, separated from the first section by a blank line
   *
   * @return A Pair containing the List of Pairs of Integers, as well as the List of Lists of
   *     Integers
   */
  public Pair<List<Pair<Integer, Integer>>, List<List<Integer>>> day5Input() {
    List<Pair<Integer, Integer>> pairList = new ArrayList<>();
    List<List<Integer>> intLists = new ArrayList<>();
    int breakPoint = 0;
    for (int i = 0; i < fileLines.size(); i++) {
      if (fileLines.get(i).isBlank()) {
        breakPoint = i + 1;
        break;
      }
      String[] split = fileLines.get(i).split(("\\|"));
      pairList.add(new Pair<>(Integer.parseInt(split[0]), Integer.parseInt(split[1])));
    }
    for (int i = breakPoint; i < fileLines.size(); i++) {
      String[] split = fileLines.get(i).split(",");
      intLists.add(Arrays.stream(split).map(Integer::parseInt).collect(Collectors.toList()));
    }
    return new Pair<>(pairList, intLists);
  }
}
