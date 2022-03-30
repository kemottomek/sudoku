package com.group.function;

import com.group.function.common.MessageOutput;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import uk.org.webcompere.systemstubs.environment.EnvironmentVariables;
import uk.org.webcompere.systemstubs.jupiter.SystemStubsExtension;

import java.util.Map;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.Stream;

import static com.group.function.common.PropertyManager.MAX_FILE_SIZE;
import static com.group.function.common.PropertyManager.MIME_TYPES;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static uk.org.webcompere.systemstubs.SystemStubs.catchSystemExit;

/**
 * @author nbtwszol
 */
@ExtendWith(SystemStubsExtension.class)
public class AppTest {

    @ParameterizedTest
    @MethodSource("propertyFailures")
    public void read_valid_files(String params, MessageOutput.Codes expected, Map<String, String> env) throws Exception {
        AtomicInteger statusCode = new AtomicInteger();
        new EnvironmentVariables(env)
                .execute(() -> statusCode.set(catchSystemExit(() -> App.main(params.split(" ")))));
        assertEquals(expected.getExitCode(), statusCode.get());
    }

    @ParameterizedTest
    @MethodSource("propertyCorrect")
    public void read_invalid_files(String params) {
        App.main(params.split(" "));

    }

    @Test
    public void check_security_exception() {
        var tmpProcessor = new App.AppRun(new String[]{"src/test/resources/sudoku/Sudoku1_VALID.txt"}) {
            @Override
            public void beforeRun() {
                {
                    throw new SecurityException("Security");
                }

            }
        };
        var thrown = assertThrows(SecurityException.class, tmpProcessor::run);
        assertEquals("Security", thrown.getMessage());

    }

    private static Stream<Arguments> propertyFailures() {
        return Stream.of(
                Arguments.of("src/test/resources/sudoku/Sudoku1_VALID.txt"
                        , MessageOutput.Codes.FILE_IS_TO_BIG
                        , Map.of(MAX_FILE_SIZE, "1")
                ),
                Arguments.of("src/test/resources/sudoku/Sudoku1_VALID.txt"
                        , MessageOutput.Codes.FILE_IS_NOT_A_TEXT_FILE
                        , Map.of(MIME_TYPES, "test")
                ),
                Arguments.of("src/test/resources/sudoku/Sudoku6_INVALID.txt"
                        , MessageOutput.Codes.MATRIX_INVALID_LINE_NUMBER
                        , Map.of())
        );
    }

    private static Stream<Arguments> propertyCorrect() {
        return Stream.of(
                Arguments.of("src/test/resources/sudoku/Sudoku1_VALID.txt"),
                Arguments.of("src/test/resources/sudoku/Sudoku2_VALID.csv")

        );
    }
}
