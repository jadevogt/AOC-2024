# Advent of Code 2024 - Jade

My solutions for the 2024 [Advent of Code](https://adventofcode.com/2024/about) challenges.
Everything is written in Java and somewhat over-engineered. I'm trying to avoid using any dependencies other than what's
available in the standard [Java Class Library](https://docs.oracle.com/en/java/javase/20/docs/api/index.html).

If you're a Python developer or haven't worked with Java much, here are some interesting features on offer:

- [Strong Typing](https://docs.oracle.com/javase/specs/jls/se7/html/jls-4.html) - Helps to avoid common bugs that occur
  due to passing around random values by ensuring you always know what type a given value is.
- [Generics](https://docs.oracle.com/javase/tutorial/java/generics/types.html) - Allow you to write classes and methods
  that work with many different types.
- [Streams](https://www.oracle.com/technical-resources/articles/java/ma14-java-se-8-streams.html) - Make it much easier
  to work with collections and provide access to Functional Programming patterns.
- [JavaDoc](https://www.oracle.com/technical-resources/articles/java/javadoc-tool.html) - Generates easily browsable
  HTML pages based on comments you've written in your code.

If I write any code for a solution that seems like it'll be even remotely helpful down the line / seems funny to spend
time abstracting out into a separate class or package, I'll take the opportunity to do so every time.

Emphasis is placed on getting the solutions done quickly and easily, rather than on correctness or efficiency in terms
of memory or runtime. I am trying to ensure I document the important classes and methods if I have extra time after
solving puzzles.

Per the Expedient competition guidelines, this repo and available documentation / download
links will be updated around 24 hours after a challenge goes online to avoid spoiling solutions.

Other Expedient employees' solutions can be found [on GitHub](https://github.com/Expedient/AOC-2024/forks).

### Documentation

[Browsable documentation (JavaDoc) is available](https://jadevogt.com/aoc/jdoc) on my website. Information is provided
about the solutions, the solution runner, etc.

### Download

You can [download the executable JAR file here](https://jadevogt.com/aoc/aoc.jar), please note that it is compiled with
Java 17, so you will need a relatively new JDK available to run it. Microsoft's distribution of
[OpenJDK is freely available to download here](https://learn.microsoft.com/en-us/java/openjdk/download#openjdk-21).
Otherwise, it should work anywhere.

### Usage

`java -jar ./aoc.jar` to run the solutions. If you have test input files located in `./inputs` named with numbers
corresponding to each day, it'll use those to output the solutions. Otherwise, it will default to the built-in sample
inputs.

Available options:

- `--test` - use the sample inputs even if you have inputs available in `./inputs`
- `--all` - run all available solutions and print their output
- `--day=x` - runs the solution for day `x`
- `--help` - display command line usage information