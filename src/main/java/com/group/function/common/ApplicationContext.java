package com.group.function.common;

import com.group.function.service.SudokuFileReader;
import com.group.function.service.SudokuResolver;
import lombok.experimental.SuperBuilder;

import java.util.function.Function;

/**
 * @author nbtwszol
 * Is thread safe. All object are inistanicaed inside the contex.
 */

@SuperBuilder
public final class ApplicationContext {

    private final PropertyManager propertyManager;

    private final SudokuResolver sudokuResolver;

    private final SudokuFileReader sudokuFileReader;

    public void run() {
        Function<String, int[][]> reader = sudokuFileReader::parseFile;
        Function<int[][], MessageOutput> validator = sudokuResolver::validateMatrix;
        reader.andThen(validator).apply(propertyManager.getFilePath()).invokeSignal();
    }
}
