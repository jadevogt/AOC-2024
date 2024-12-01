package com.expedient.adventofcodejade.util;

import java.io.File;
import java.io.IOException;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.function.Function;

public class PuzzleInput {
    private final ArrayList<String> fileLines;

    public PuzzleInput(String inputPath) throws IOException {
        File puzzleInputFile = new File(inputPath);
        this.fileLines = new ArrayList<>(Files.readAllLines(puzzleInputFile.toPath(), Charset.defaultCharset()));
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
