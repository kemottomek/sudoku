package com.group.function.service;

import com.group.function.common.MessageOutput;

/**
 * @author nbtwszol
 */
public interface SudokuDataInputValidator {
    /**
     * @param inputData input matrix contains 9x9 numbers from <1,9></1,9>
     * @return fail-fast when validation error occurs.
     */
    MessageOutput sanityCheck(int[][] inputData);
}
