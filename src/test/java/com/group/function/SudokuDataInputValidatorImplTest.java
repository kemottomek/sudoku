package com.group.function;

import com.group.function.common.MessageOutput;
import com.group.function.exception.SudokuRuntimeException;
import com.group.function.service.SudokuDataInputValidatorImpl;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import uk.org.webcompere.systemstubs.jupiter.SystemStubsExtension;

import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

/**
 * @author nbtwszol
 */
@ExtendWith(SystemStubsExtension.class)
public class SudokuDataInputValidatorImplTest {

    private final SudokuDataInputValidatorImpl sudokuDataInputValidator = SudokuDataInputValidatorImpl.of();

    @ParameterizedTest
    @MethodSource("inputs")
    public void validate_failing_matrix(int[][] matrix, MessageOutput.Codes expected) {
        var thrown = assertThrows(SudokuRuntimeException.class, () -> sudokuDataInputValidator.sanityCheck(matrix));
        assertEquals(expected.getExitCode(), thrown.getExitCode());
    }

    @Test
    public void validate_valid_matrix() {
        var matrix = new int[][]{{1, 2, 3, 4, 5, 6, 7, 8, 9}
                , {1, 2, 3, 4, 5, 6, 7, 8, 9}
                , {1, 2, 3, 4, 5, 6, 7, 8, 9}
                , {1, 2, 3, 4, 5, 6, 7, 8, 9}
                , {1, 2, 3, 4, 5, 6, 7, 8, 9}
                , {1, 2, 3, 4, 5, 6, 7, 8, 9}
                , {1, 2, 3, 4, 5, 6, 7, 8, 9}
                , {1, 2, 3, 4, 5, 6, 7, 8, 9}
                , {1, 2, 3, 4, 5, 6, 7, 8, 9}};
        int statusCode = sudokuDataInputValidator.sanityCheck(matrix).getCode().getExitCode();
        assertEquals(MessageOutput.Codes.SUCCESSFULLY.getExitCode(), statusCode);
    }

    private static Stream<Arguments> inputs() {
        return Stream.of(
                Arguments.of(null, MessageOutput.Codes.MATRIX_IS_NULL_OR_EMPTY),
                Arguments.of(new int[][]{{}, {}}, MessageOutput.Codes.MATRIX_MAX_ELEMENTS_IN_ROW),
                Arguments.of(new int[][]{{}, {}, {}, {}, {}, {}, {}, {}, {}}, MessageOutput.Codes.MATRIX_MAX_ELEMENTS_IN_ROW),
                Arguments.of(new int[][]{{1, 2, 3, 4, 5, 6, 7, 8, 9}
                        , {1, 2, 3, 4, 5, 6, 7, 8, 9}
                        , {1, 2, 3, 4, 5, 6, 7, 8, 9}
                        , {1, 2, 3, 4, 5, 6, 7, 8, -9}
                        , {1, 2, 3, 4, 5, 6, 7, 8, 9}
                        , {1, 2, 3, 4, 5, 6, 7, 8, 9}
                        , {1, 2, 3, 4, 5, 6, 7, 8, 9}
                        , {1, 2, 3, 4, 5, 6, 7, 8, 9}
                        , {1, 2, 3, 4, 5, 6, 7, 8, 9}}, MessageOutput.Codes.MATRIX_INVALID_NUMBER)
        );
    }
}
