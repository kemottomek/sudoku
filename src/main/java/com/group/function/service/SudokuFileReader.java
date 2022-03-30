package com.group.function.service;

/**
 * @author nbtwszol
 */
public interface SudokuFileReader {
    /**
     * @param filePath path to the file
     * @return sudo proper matrix 9x9
     */
    int[][] parseFile(String filePath);
}
