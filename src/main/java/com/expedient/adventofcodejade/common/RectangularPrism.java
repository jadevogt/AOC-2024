package com.expedient.adventofcodejade.common;

import java.util.List;
import java.util.stream.Stream;

/**
 * A rectangular prism
 *
 * @param length
 * @param width
 * @param height
 */
public record RectangularPrism(int length, int width, int height) {
  /**
   * @return the total surface area of the prism
   */
  public int surfaceArea() {
    return 2 * length * width + 2 * width * height + 2 * length * height;
  }

  /**
   * @return the surface area of one of the smallest side of the prism
   */
  public int smallestSurfaceArea() {
    return Stream.of(length, width, height).sorted().limit(2).reduce(1, (a, b) -> (a * b));
  }

  /**
   * @return the perimeter of the prism along the shortest portion
   */
  public int smallestPerimeter() {
    List<Integer> shortSides = Stream.of(length, width, height).sorted().limit(2).toList();
    return shortSides.get(0) * 2 + shortSides.get(1) * 2;
  }

  /**
   * @return the volume of the prism
   */
  public int volume() {
    return length * width * height;
  }
}
