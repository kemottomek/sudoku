package com.group.function.service;

import com.group.function.common.MessageOutput;

/**
 * @author nbtwszol
 */
public interface SudokuResolver {

    /**
     * @param inputData 2-dimensional array with sudo results
     * @return fail-fast when data are invalid
     */
    MessageOutput validateMatrix(int[][] inputData);

}
