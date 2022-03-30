package com.group.function.exception;

import com.group.function.common.MessageOutput;
import lombok.Getter;

/**
 * @author nbtwszol
 */
public class SudokuRuntimeException extends RuntimeException {

    @Getter
    private final int exitCode;

    public SudokuRuntimeException(MessageOutput message) {
        super(message.getCode().name() + " " +  message.getMessage());
        this.exitCode = message.getCode().getExitCode();
    }
}
