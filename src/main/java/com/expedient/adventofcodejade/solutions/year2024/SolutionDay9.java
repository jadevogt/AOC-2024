package com.expedient.adventofcodejade.solutions.year2024;

import com.expedient.adventofcodejade.BaseSolution;
import com.expedient.adventofcodejade.common.Pair;
import com.expedient.adventofcodejade.common.PuzzleInput;
import com.expedient.adventofcodejade.util.StringTools;

import java.util.*;

public class SolutionDay9 extends BaseSolution {
  public SolutionDay9(PuzzleInput input, PuzzleInput sampleInputOne, PuzzleInput sampleInputTwo) {
    super(input, sampleInputOne, sampleInputTwo);
  }

  public static Long calculateCheckSum(FileSystemBlock[] fileSystem) {
    long checkSum = 0;
    for (int i = 0; i < fileSystem.length; i++) {
      if (fileSystem[i] == null) {
        continue;
      }
      checkSum += ((long) i * fileSystem[i].fileId());
    }
    return checkSum;
  }

  public static Pair<FileSystemBlock[], List<File>> buildFileSystem(PuzzleInput input) {
    var line = input.getLines().get(0);
    var lineAsChars = StringTools.ToCharacterArray(input.getLines().get(0));
    int totalSize = 0;
    List<File> files = new ArrayList<>();
    for (Character lineAsChar : lineAsChars) {
      totalSize += Integer.parseInt(lineAsChar.toString());
    }
    FileSystemBlock[] fileSystem = new FileSystemBlock[totalSize];
    int placeIntFileSystem = 0;
    int fileId = 0;
    for (int i = 0; i < lineAsChars.length; i++) {
      var currentLineLength = Integer.parseInt(lineAsChars[i].toString());
      if (i % 2 != 0) {
        for (int j = placeIntFileSystem; j < placeIntFileSystem + currentLineLength; j++) {
          fileSystem[j] = null;
        }
        placeIntFileSystem += currentLineLength;
      } else {
        files.add(new File(placeIntFileSystem, placeIntFileSystem + currentLineLength - 1, fileId));
        for (int j = placeIntFileSystem; j < placeIntFileSystem + currentLineLength; j++) {
          fileSystem[j] = new FileSystemBlock(fileId, false);
        }
        placeIntFileSystem += currentLineLength;
        fileId++;
      }
    }
    return new Pair<>(fileSystem, files);
  }

  public static void SortFileSystem(FileSystemBlock[] fileSystem) {
    Integer lastFilled = 0;
    for (int i = fileSystem.length - 1; i >= 0; i--) {
      if (fileSystem[i] == null) {
        continue;
      }
      if (i <= lastFilled) {
        break;
      }
      for (int j = 0; j < fileSystem.length; j++) {
        if (fileSystem[j] == null) {
          fileSystem[j] = fileSystem[i];
          fileSystem[i] = null;
          lastFilled = j;
          break;
        }
      }
    }
  }

  public static record File(Integer fileStart, Integer fileEnd, Integer fileId) {
    public int length() {
      return fileEnd - fileStart + 1;
    }
  }

  public static void OverwriteNextNullWithFile(
      FileSystemBlock[] fileSystem, int fileLength, int fileId, int fileEnd) {
    for (int i = 0; i < fileSystem.length - fileLength; i++) {
      if (fileSystem[fileEnd] != null && fileSystem[fileEnd].moved) return;
      if (i > fileEnd) break;
      boolean valid = false;
      for (int j = i; j < i + fileLength; j++) {
        if (fileSystem[j] != null) {
          valid = false;
          break;
        } else {
          valid = true;
        }
      }
      if (valid) {
        for (int j = i; j < i + fileLength; j++) {
          fileSystem[j] = new FileSystemBlock(fileId, true);
        }
        for (int j = fileEnd; j > fileEnd - fileLength; j--) {
          fileSystem[j] = null;
        }
        break;
      }
    }
  }

  public static void SortFileSystemByFile(FileSystemBlock[] fileSystem, List<File> files) {
    var currentFileId = -1;
    var currentFileLength = 0;
    var currentFileEnd = -1;
    for (int i = fileSystem.length - 1; i >= 0; i--) {
      if (fileSystem[i] != null) {
        if (currentFileId == fileSystem[i].fileId()) {
          currentFileLength++;
        } else if (currentFileId == -1) {
          currentFileLength = 1;
          currentFileId = fileSystem[i].fileId();
          currentFileEnd = i;
        } else {
          OverwriteNextNullWithFile(fileSystem, currentFileLength, currentFileId, currentFileEnd);
          currentFileLength = 1;
          currentFileId = fileSystem[i].fileId();
          currentFileEnd = i;
        }
      } else {
        if (currentFileId != -1) {
          OverwriteNextNullWithFile(fileSystem, currentFileLength, currentFileId, currentFileEnd);
          currentFileLength = 0;
          currentFileId = -1;
          currentFileEnd = -1;
        }
      }
    }
  }

  public static record FileSystemBlock(Integer fileId, boolean moved) {}

  @Override
  public Object partOne(PuzzleInput input) {
    var fsAndFiles = buildFileSystem(input);
    var fs = fsAndFiles.one();
    // run this garbage sorting twice bc it doesnt fully work the first time lol
    SortFileSystem(fs);
    SortFileSystem(fs);
    return calculateCheckSum(fs);
  }

  @Override
  public Object partTwo(PuzzleInput input) {
    var fsAndFiles = buildFileSystem(input);
    var fs = fsAndFiles.one();
    var files = fsAndFiles.two();
    SortFileSystemByFile(fs, files);
    return calculateCheckSum(fs);
  }
}
