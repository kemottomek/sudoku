package com.group.function.common;

import com.group.function.exception.SudokuRuntimeException;
import lombok.Getter;

import static com.group.function.common.PropertyManager.*;

/**
 * @author nbtwszol
 * List of signals - current set up is for exception  np. returned by main thread
 */
@Getter
public final class MessageOutput {
    private final Codes code;
    private final String message;
    private final Throwable throwable;

    private MessageOutput(Codes code, String message) {
        this(code, message, null);
    }

    private MessageOutput(Codes code, String message, Throwable throwable) {
        this.code = code;
        this.message = message;
        this.throwable = throwable;
    }

    public static MessageOutput of(Codes code, String... args) {
        return new MessageOutput(code, format(code.message, args));
    }

    public static MessageOutput of(Throwable throwable) {
        return new MessageOutput(Codes.OTHERS, throwable.getClass().getSimpleName() + " " + throwable.getLocalizedMessage(), throwable);
    }

    public static MessageOutput of(Codes code) {
        return new MessageOutput(code, format(code.message));

    }

    /**
     * Single point of termination, in case of direct termination method can send an exit signal directly
     */
    public void invokeSignal() {
        if (Codes.SUCCESSFULLY != this.getCode())
            throw new SudokuRuntimeException(this);
    }

    public static String format(String message, String... args) {
        return String.format(message, (Object[]) args);
    }

    @Getter
    public enum Codes {

        SUCCESSFULLY(0),
        FILE_NOT_FOUND(1, "File not found: %s"),
        FILE_IS_A_DIRECTORY(2, "File: %s is a directory"),
        FILE_IS_NOT_A_TEXT_FILE(3, "File: %s is not a text: %s"),
        FILE_IS_TO_BIG(4, "File: %s is to big, there is max quota set to: %s. Current: %s "),
        FILE_CANNOT_READ(5, "Cannot read file %s"),

        EXECUTION_PARAM_HELPER(6, "validate.bat [FILE]\n" +
                "where\n" +
                "FILE - path to text file containing sudo puzzle solution\n" +
                "available env properties to set up:\n" +
                MAX_FILE_SIZE + " " + " - max file size \n" +
                MIME_TYPES + " " + " - mime types validation\n" +
                SUDOKU_SEPARATOR + " " + " - separator"),
        EXECUTION_PARAM_CAN_BE_ONLY_ONE(7, "Can be only one file defined\n" + EXECUTION_PARAM_HELPER.message),
        EXECUTION_PARAM_CANNOT_READ_PROPERLY(8, "Optional param %s cannot be read - value: %s, please fallow the script instruction\n" + EXECUTION_PARAM_HELPER.message),
        EXECUTION_PARAM_IS_EMPTY(9, "Param file name is empty\n" + EXECUTION_PARAM_HELPER.message),
        EXECUTION_PARAM_CANNOT_FIND_FILE_PARAM(10, "Please use at least one file param.\n" + EXECUTION_PARAM_HELPER.message),

        MATRIX_INVALID_NUMBER(200, "The matrix should contains value <1,9> but was found: %s"),
        MATRIX_INVALID_CHARACTER(201, "The matrix contains invalid character: %s should be between <1,9> "),
        MATRIX_INVALID_LINE_NUMBER(203, "The matrix line to process should be equal to 9 but found => %s"),
        MATRIX_IS_NULL_OR_EMPTY(204, "The matrix is null or empty"),
        MATRIX_MAX_ELEMENTS_IN_ROW(202, "The matrix elements in w row should be: 9 but found =>: %s"),

        ROW_IS_INVALID(500, "Row no: %s, cell at position: %s has invalid value already defined as: %s "),
        COLUMN_IS_INVALID(501, "Column no: %s, cell at position: %s has invalid value already defined as: %s "),
        SUB_MATRIX_IS_INVALID(502, "Sub-matrix no: %s at start position: %s has invalid value already defined as: %s"),

        OTHERS(999, "Unexpected exception, exception name: %s ");


        private final int exitCode;
        private final String message;

        Codes(int exitCode, String message) {
            this.exitCode = exitCode;
            this.message = message;
        }

        Codes(int exitCode) {
            this.exitCode = exitCode;
            this.message = "";
        }
    }
}


