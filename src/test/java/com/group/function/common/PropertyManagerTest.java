package com.group.function.common;

import com.group.function.exception.SudokuRuntimeException;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import uk.org.webcompere.systemstubs.environment.EnvironmentVariables;
import uk.org.webcompere.systemstubs.jupiter.SystemStubsExtension;

import java.util.List;
import java.util.Map;
import java.util.concurrent.atomic.AtomicReference;
import java.util.function.Function;
import java.util.stream.Stream;

import static com.group.function.common.PropertyManager.MIME_TYPES;
import static com.group.function.common.PropertyManager.SUDOKU_SEPARATOR;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

/**
 * @author nbtwszol
 */
@ExtendWith(SystemStubsExtension.class)
public class PropertyManagerTest {

    @ParameterizedTest
    @MethodSource("propertyTypesFailures")
    public void test_property_inputs_failures(String inputs, MessageOutput.Codes expected) {
        var thrown = assertThrows(SudokuRuntimeException.class, () -> {
            if (inputs == null) {
                PropertyManager.of(null);
            } else {
                PropertyManager.of(inputs.split(" "));
            }

        });
        assertEquals(expected.getExitCode(), thrown.getExitCode());
    }

    @ParameterizedTest
    @MethodSource("propertyTypesCorrect")
    public void test_property_inputs_correct(String inputs, Function<PropertyManager, Object> f, Object expected, Map<String, String> env) throws Exception {

        AtomicReference<Object> result = new AtomicReference<>();
        new EnvironmentVariables(env)
                .execute(() -> {
                    var propertyManager = PropertyManager.of(inputs.split(" "));
                    result.set(f.apply(propertyManager));
                });
        assertEquals(expected, result.get());
    }

    private static Stream<Arguments> propertyTypesFailures() {
        return Stream.of(
                Arguments.of(null, MessageOutput.Codes.EXECUTION_PARAM_IS_EMPTY),
                Arguments.of("", MessageOutput.Codes.EXECUTION_PARAM_IS_EMPTY),
                Arguments.of("file file", MessageOutput.Codes.EXECUTION_PARAM_CAN_BE_ONLY_ONE)
        );
    }

    private static Stream<Arguments> propertyTypesCorrect() {
        return Stream.of(
                Arguments.of("file",
                        (Function<PropertyManager, Object>) PropertyManager::getFilePath
                        , "file"
                        , Map.of()),
                Arguments.of("file"
                        , (Function<PropertyManager, Object>) PropertyManager::getSeparator
                        , ";;"
                        , Map.of(SUDOKU_SEPARATOR, ";;")),
                Arguments.of("file"
                        , (Function<PropertyManager, Object>) PropertyManager::getMimetypes
                        , List.of("file")
                        , Map.of(MIME_TYPES, "file"))
        );
    }
}
