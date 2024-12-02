package com.expedient.adventofcodejade.common;

import java.io.File;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.function.Function;

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
     * Gets a PuzzleInput using the resource for a specific day (located in /src/main/resources)
     * @param day day for which the input is loaded
     * @return PuzzleInput object containing the given day's sample input
     * @throws IOException when the given day's input cannot be found in resources
     */
    public static PuzzleInput sampleForDay(int day) throws IOException {
        return PuzzleInput.fromResource("samples/%d".formatted(day));
    }


    /**
     * Get the lines from the file
     * @return a list of strings containing the input as-is
     */
    public List<String> getFileLines() {
        return this.fileLines;
    }


    /**
     * Return two lists based on split lines from the input
     * @param conversion function used to convert the string into a type of your choice
     * @param delimiterRegex delimiter used to split the lines
     * @return two lists, composed of the elements that were split from each input
     * @param <T> the type contained within the returned lists
     */
    public <T> List<List<T>> getTwoLists(Function <String, T> conversion, String delimiterRegex) {
        var listOne = new ArrayList<T>();
        var listTwo = new ArrayList<T>();
        for (var line : this.fileLines) {
            var split = line.split(delimiterRegex);
            listOne.add(conversion.apply(split[0]));
            listTwo.add(conversion.apply(split[1]));
        }
        return Arrays.asList(listOne, listTwo);
    }

}
