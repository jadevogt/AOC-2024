package com.expedient.adventofcodejade.common;

/**
 * Represents a pair of two values of any type
 *
 * @param one the first item in the pair
 * @param two the second item in the pair
 * @param <T> type of the first item in the pair
 * @param <U> type of the second item in the pair
 */
public record Pair<T, U>(T one, U two) {}
