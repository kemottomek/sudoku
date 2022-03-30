package com.group.function.service;

import com.group.function.common.MessageOutput;
import lombok.AllArgsConstructor;

/**
 * @author nbtwszol
 */
@AllArgsConstructor(staticName = "of")
public final class SudokuResolverImpl implements SudokuResolver {

    private final SudokuDataInputValidator sudokuDataInputValidator;

    @Override
    public MessageOutput validateMatrix(int[][] inputData) {
        var messageValidate = sudokuDataInputValidator.sanityCheck(inputData);
        messageValidate.invokeSignal();
        return check(inputData);
    }


    private MessageOutput check(int[][] inputData) {
        var matrixSize = inputData.length;
        var bucketSize = 3;
        var bucketIndexed = new int[bucketSize][matrixSize][matrixSize + 1];

        for (var rowIdx = 0; rowIdx < inputData[0].length; rowIdx++) {
            for (var colIdx = 0; colIdx < inputData.length; colIdx++) {
                var cellValue = inputData[rowIdx][colIdx];
                if (bucketIndexed[0][rowIdx][cellValue] != 0) {
                    return MessageOutput.of(MessageOutput.Codes.ROW_IS_INVALID, String.valueOf(rowIdx)
                            , "[" + (rowIdx + 1) + "," + (colIdx + 1) + "]"
                            , String.valueOf(cellValue));
                } else {
                    bucketIndexed[0][rowIdx][cellValue] = 1;
                }
                if (bucketIndexed[1][colIdx][cellValue] != 0) {
                    return MessageOutput.of(MessageOutput.Codes.COLUMN_IS_INVALID, String.valueOf(colIdx)
                            , "[" + (rowIdx + 1) + "," + (colIdx + 1) + "]"
                            , String.valueOf(cellValue));
                } else {
                    bucketIndexed[1][colIdx][cellValue] = 1;
                }

                var subIdx = (3 * (rowIdx / 3)) + (colIdx / 3);
                if (bucketIndexed[2][subIdx][cellValue] != 0) {
                    return MessageOutput.of(MessageOutput.Codes.SUB_MATRIX_IS_INVALID, String.valueOf(subIdx)
                            , "[" + (rowIdx + 1) + "," + (colIdx + 1) + "]"
                            , String.valueOf(cellValue));
                } else {
                    bucketIndexed[2][subIdx][cellValue] = 1;
                }
            }
        }
        return MessageOutput.of(MessageOutput.Codes.SUCCESSFULLY);
    }
}
