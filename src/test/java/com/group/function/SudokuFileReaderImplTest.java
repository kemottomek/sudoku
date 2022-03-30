package com.group.function;

import com.group.function.common.MessageOutput;
import com.group.function.common.PropertyManager;
import com.group.function.exception.SudokuRuntimeException;
import com.group.function.service.SudokuFileReaderImpl;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import uk.org.webcompere.systemstubs.jupiter.SystemStubsExtension;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.util.stream.Stream;

import static com.group.function.common.MessageOutput.Codes.*;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

/**
 * @author nbtwszol
 */
@ExtendWith(SystemStubsExtension.class)
public class SudokuFileReaderImplTest {

    @ParameterizedTest
    @MethodSource("propertyFailures")
    public void read_valid_files(String fileName, MessageOutput.Codes expected) {

        var resolvedPath = getFilePath(fileName);
        var sudokuFileReader = SudokuFileReaderImpl.of(PropertyManager.of(new String[]{resolvedPath}),
                new SudokuFileReaderImpl.BufferedProcessor());
        var thrown = assertThrows(SudokuRuntimeException.class, () -> sudokuFileReader.parseFile(resolvedPath));
        assertEquals(expected.getExitCode(), thrown.getExitCode());
    }

    @ParameterizedTest
    @MethodSource("propertyCorrect")
    public void read_invalid_files(String fileName) {
        var resolvedPath = getFilePath(fileName);
        var sudokuFileReader = SudokuFileReaderImpl.of(PropertyManager.of(new String[]{resolvedPath}),
                new SudokuFileReaderImpl.BufferedProcessor());
        sudokuFileReader.parseFile(resolvedPath);

    }

    @Test
    public void check_ioexception() {
        var tmpProcessor = new SudokuFileReaderImpl.BufferedProcessor() {
            @Override
            protected BufferedReader getBuffer(String fileName) throws IOException {
                throw new IOException("Test");
            }

        };
        var resolvedPath = getFilePath("sudoku/Sudoku1_VALID.txt");
        var sudokuFileReader = SudokuFileReaderImpl.of(PropertyManager.of(new String[]{resolvedPath}),
                tmpProcessor);
        var thrown = assertThrows(SudokuRuntimeException.class, () -> sudokuFileReader.parseFile(resolvedPath));
        assertEquals(OTHERS.getExitCode(), thrown.getExitCode());

    }


    private String getFilePath(String filePath) {
        var resolvedPath = this.getClass().getClassLoader().getResource(filePath);
        if (resolvedPath != null)
            return new File(resolvedPath.getPath()).toPath().toAbsolutePath().toString();
        return filePath;
    }

    private static Stream<Arguments> propertyFailures() {
        return Stream.of(
                Arguments.of("sudoku/Sudoku3_INVALID.txt", MATRIX_INVALID_CHARACTER),
                Arguments.of("sudoku/Sudoku4_INVALID.txt", MATRIX_MAX_ELEMENTS_IN_ROW),
                Arguments.of("sudoku/Sudoku5_INVALID.txt", FILE_IS_TO_BIG),
                Arguments.of("XYZ", FILE_NOT_FOUND),
                Arguments.of("sudoku", FILE_IS_A_DIRECTORY)
        );
    }

    private static Stream<Arguments> propertyCorrect() {
        return Stream.of(
                Arguments.of("sudoku/Sudoku1_VALID.txt"),
                Arguments.of("sudoku/Sudoku2_VALID.csv"),
                Arguments.of("sudoku/Sudoku7_VALID_UTF8BOM.csv")

        );
    }
}
