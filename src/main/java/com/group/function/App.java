package com.group.function;

import com.group.function.common.ApplicationContext;
import com.group.function.common.PropertyManager;
import com.group.function.exception.SudokuRuntimeException;
import com.group.function.service.SudokuDataInputValidatorImpl;
import com.group.function.service.SudokuFileReaderImpl;
import com.group.function.service.SudokuResolverImpl;
import lombok.extern.slf4j.Slf4j;

/**
 * @author nbtwszol
 */
@Slf4j
public class App {
    public static void main(String[] args) {
        try {
            new AppRun(args) {
                @Override
                void beforeRun() {
                    // doNothing
                }

            }.run();
        } catch (SudokuRuntimeException ex) {
            log.info(ex.getMessage());
            System.exit(ex.getExitCode());
        }
    }

    abstract static class AppRun {
        private final ApplicationContext applicationContext;

        protected AppRun(String[] args) {
            var propertyManager = PropertyManager.of(args);
            var sudokuValidator = SudokuDataInputValidatorImpl.of();
            var sudokuResolver = SudokuResolverImpl.of(sudokuValidator);
            var sudokuFileReader = SudokuFileReaderImpl.of(propertyManager,
                    new SudokuFileReaderImpl.BufferedProcessor());

            applicationContext = ApplicationContext.builder()
                    .propertyManager(propertyManager)
                    .sudokuResolver(sudokuResolver)
                    .sudokuFileReader(sudokuFileReader).build();
        }

        protected void run() {
            beforeRun();
            applicationContext.run();

        }

        abstract void beforeRun();
    }
}


