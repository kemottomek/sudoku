package com.group.function.service;

import com.group.function.common.MessageOutput;

import java.util.ArrayList;
import java.util.List;

import static com.group.function.common.MessageOutput.Codes.*;

/**
 * @author nbtwszol
 */
public final class SudokuDataInputValidatorImpl implements SudokuDataInputValidator {

    private final List<SudokuDataInputValidator> sudokuDataInputValidatorList = new ArrayList<>();

    private SudokuDataInputValidatorImpl() {
        register(matrix -> matrix == null ? MessageOutput.of(MATRIX_IS_NULL_OR_EMPTY) : MessageOutput.of(SUCCESSFULLY));
        register(matrix -> matrix.length != 9 ? MessageOutput.of(MATRIX_MAX_ELEMENTS_IN_ROW, String.valueOf(matrix.length)) : MessageOutput.of(SUCCESSFULLY));
        register(matrix -> {
            for (int[] ints : matrix) {
                var colLength = ints.length;
                if (colLength != 9) {
                    return MessageOutput.of(MATRIX_MAX_ELEMENTS_IN_ROW, String.valueOf(colLength));
                }
                for (int cell : ints) {
                    if (cell < 1 || cell > 9) {
                        return MessageOutput.of(MATRIX_INVALID_NUMBER, String.valueOf(cell));
                    }
                }
            }
            return MessageOutput.of(SUCCESSFULLY);
        });

    }

    public static SudokuDataInputValidatorImpl of() {
        return new SudokuDataInputValidatorImpl();
    }

    @Override
    public MessageOutput sanityCheck(int[][] inputData) {
        var resultIsValid = MessageOutput.of(SUCCESSFULLY);
        for (var validator : sudokuDataInputValidatorList) {
            resultIsValid = validator.sanityCheck(inputData);
            if (SUCCESSFULLY != resultIsValid.getCode()) resultIsValid.invokeSignal();
        }
        return resultIsValid;
    }


    private void register(SudokuDataInputValidator sudokuDataInputValidator) {
        sudokuDataInputValidatorList.add(sudokuDataInputValidator);
    }
}
