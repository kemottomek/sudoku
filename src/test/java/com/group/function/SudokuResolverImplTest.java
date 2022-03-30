package com.group.function;

import com.group.function.common.MessageOutput;
import com.group.function.exception.SudokuRuntimeException;
import com.group.function.service.SudokuDataInputValidatorImpl;
import com.group.function.service.SudokuResolverImpl;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import uk.org.webcompere.systemstubs.jupiter.SystemStubsExtension;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

/**
 * @author nbtwszol
 */
@ExtendWith(SystemStubsExtension.class)
public class SudokuResolverImplTest {

    private final SudokuResolverImpl sudokuResolver = SudokuResolverImpl.of(SudokuDataInputValidatorImpl.of());

    @Test
    public void validate_incorrect_rows_matrix() {
        var matrix = new int[][]{
                {1, 2, 3, 4, 5, 6, 6, 8, 9}
                , {1, 2, 3, 4, 5, 6, 7, 8, 9}
                , {1, 2, 3, 4, 5, 6, 7, 8, 9}
                , {1, 2, 3, 4, 5, 6, 7, 8, 9}
                , {1, 2, 3, 4, 5, 6, 7, 8, 9}
                , {1, 2, 3, 4, 5, 6, 7, 8, 9}
                , {1, 2, 3, 4, 5, 6, 7, 8, 9}
                , {1, 2, 3, 4, 5, 6, 7, 8, 9}
                , {1, 2, 3, 4, 5, 6, 7, 8, 9}};
        var thrown = assertThrows(SudokuRuntimeException.class, () -> sudokuResolver.validateMatrix(matrix).invokeSignal());
        assertEquals(MessageOutput.Codes.ROW_IS_INVALID.getExitCode(), thrown.getExitCode());
    }

    @Test
    public void validate_incorrect_columns_matrix() {
        var matrix = new int[][]{
                {1, 7, 2, 5, 4, 9, 6, 8, 3}
                , {6, 4, 5, 8, 7, 3, 2, 1, 9}
                , {3, 8, 9, 2, 6, 1, 7, 4, 5}
                , {4, 9, 6, 3, 2, 7, 8, 5, 1}
                , {4, 2, 3, 4, 5, 6, 7, 8, 9}
                , {1, 2, 3, 4, 5, 6, 7, 8, 9}
                , {1, 2, 3, 4, 5, 6, 7, 8, 9}
                , {1, 2, 3, 4, 5, 6, 7, 8, 9}
                , {1, 2, 3, 4, 5, 6, 7, 8, 9}};
        var thrown = assertThrows(SudokuRuntimeException.class, () -> sudokuResolver.validateMatrix(matrix).invokeSignal());
        assertEquals(MessageOutput.Codes.COLUMN_IS_INVALID.getExitCode(), thrown.getExitCode());
    }

    @Test
    public void validate_incorrect_sub_matrix() {
        var matrix = new int[][]{
                {1, 7, 2, 5, 4, 9, 6, 8, 3}
                , {7, 4, 5, 8, 7, 3, 2, 1, 9}
                , {3, 8, 9, 2, 6, 1, 7, 4, 5}
                , {4, 9, 6, 3, 2, 7, 8, 5, 1}
                , {4, 2, 3, 4, 5, 6, 7, 8, 9}
                , {1, 2, 3, 4, 5, 6, 7, 8, 9}
                , {1, 2, 3, 4, 5, 6, 7, 8, 9}
                , {1, 2, 3, 4, 5, 6, 7, 8, 9}
                , {1, 2, 3, 4, 5, 6, 7, 8, 9}};
        var thrown = assertThrows(SudokuRuntimeException.class, () -> sudokuResolver.validateMatrix(matrix).invokeSignal());
        assertEquals(MessageOutput.Codes.SUB_MATRIX_IS_INVALID.getExitCode(), thrown.getExitCode());
    }

    @Test
    public void validate_correct_matrix() {
        var matrix = new int[][]{
                {1, 7, 2, 5, 4, 9, 6, 8, 3}
                , {6, 4, 5, 8, 7, 3, 2, 1, 9}
                , {3, 8, 9, 2, 6, 1, 7, 4, 5}
                , {4, 9, 6, 3, 2, 7, 8, 5, 1}
                , {8, 1, 3, 4, 5, 6, 9, 7, 2}
                , {2, 5, 7, 1, 9, 8, 4, 3, 6}
                , {9, 6, 4, 7, 1, 5, 3, 2, 8}
                , {7, 3, 1, 6, 8, 2, 5, 9, 4}
                , {5, 2, 8, 9, 3, 4, 1, 6, 7}};
        int statusCode = sudokuResolver.validateMatrix(matrix).getCode().getExitCode();
        assertEquals(MessageOutput.Codes.SUCCESSFULLY.getExitCode(), statusCode);
    }
}
