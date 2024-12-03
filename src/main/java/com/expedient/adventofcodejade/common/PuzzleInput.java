package com.expedient.adventofcodejade.common;

import com.expedient.adventofcodejade.util.StringTools;

import java.io.File;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.function.Function;
import java.util.function.Predicate;

/**
 * Handles both sample and actual input files
 */
public class PuzzleInput {
    private final List<String> fileLines;

    private PuzzleInput(String inputString) {
        this.fileLines = Arrays.stream(inputString.split("\n")).toList();
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
        try (var in = PuzzleInput.class.getClassLoader().getResourceAsStream(resourcePath)) {
            if (in == null) {
                throw new IOException("Resource not found");
            }
            return new PuzzleInput(new String(in.readAllBytes(), StandardCharsets.UTF_8));
        }
    }

    /**
     * Gets a PuzzleInput using the resource for a specific day (located in /src/main/resources). Will first attempt to
     * find sample input with the naming scheme [day]-[part] in case sample differs between parts 1 and 2, otherwise
     * falls back to input with the naming scheme [day]
     *
     * @param day day for which the input is loaded
     * @param partOne whether the sample is being fetched for part one
     * @return PuzzleInput object containing the given day's sample input
     * @throws IOException when the given day's input cannot be found in resources
     */
    public static PuzzleInput sampleForDay(int day, boolean partOne) throws IOException {
        try {
            var sfx = partOne ? 1 : 2;
            return PuzzleInput.fromResource("samples/%d-%d".formatted(day, sfx));
        } catch (IOException e) {
            return PuzzleInput.fromResource("samples/%d".formatted(day));
        }
    }


    /**
     * Get the lines from the file
     *
     * @return a list of strings containing the input as-is
     */
    public List<String> getFileLines() {
        return this.fileLines;
    }

    /**
     * Get the input as a single string, rather than as a list of lines
     * @return String containing the entire input
     */
    public String getSingleString() {
        return String.join("\n", fileLines);
    }


    /**
     * Return two lists based on split lines from the input
     *
     * @param conversion     function used to convert the string into a type of your choice
     * @param delimiterRegex delimiter used to split the lines
     * @param <T>            the type contained within the returned lists
     * @return two lists, composed of the elements that were split from each input
     */
    public <T> List<List<T>> getTwoLists(Function<String, T> conversion, String delimiterRegex) {
        var listOne = new ArrayList<T>();
        var listTwo = new ArrayList<T>();
        for (var line : this.fileLines) {
            var split = line.split(delimiterRegex);
            listOne.add(conversion.apply(split[0]));
            listTwo.add(conversion.apply(split[1]));
        }
        // This has to be a list, can't create an array of a Generic type
        return Arrays.asList(listOne, listTwo);
    }

    /**
     * Converts each line to a list of type T values, after iterating over each Character in the line and applying a
     * provided filter function. Optionally can apply a transformation to the line string before running the filter.
     *
     * @param test       Test to determine whether a given value will be included in the list for each line
     * @param conversion Function to convert the Character value to type T
     * @param transform  Function to apply to each line before processing it. If this is null, the transformation step is skipped
     * @param <T>        The desired output type for the operation
     * @return List of lists composed of values from each line matching the test function and converted to type T
     */
    public <T> List<List<T>> allMatchesPerLine(Predicate<Character> test, Function<Character, T> conversion, Function<String, String> transform) {
        var parsed = new ArrayList<List<T>>();
        for (var line : this.fileLines) {
            var l = line;
            if (transform != null) {
                l = transform.apply(l);
            }
            var chars = StringTools.ToCharacterArray(l);
            parsed.add(Arrays.stream(chars).filter(test).map(conversion).toList());
        }
        return parsed;
    }


    /**
     * Returns the input as a rectangular 2D array of Character
     * @return 2D array of Character derived from input
     * @throws RuntimeException if the array would be ragged / the rows are of differing lengths
     */
    public Character[][] asTwoDimensionalArray() throws RuntimeException {
        var height = fileLines.size();
        var width = fileLines.get(0).length();
        if (fileLines.stream().anyMatch(i -> i.length() != width)) {
            throw new RuntimeException("The input has rows with inconsistent lengths, so the array would be ragged.");
        }
        Character[][] block = new Character[width][height];
        for (int i = 0; i < height; i++) {
            block[i] = StringTools.ToCharacterArray(fileLines.get(i));
        }
        return block;
    }

}
