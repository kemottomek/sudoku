package com.group.function.common;

import lombok.Getter;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

/**
 * @author nbtwszol
 * Holds reference to application properties
 */
@Getter
public final class PropertyManager {

    public static final String MAX_FILE_SIZE = "SUDOKU_MAX_FILE_SIZE";
    public static final String MIME_TYPES = "SUDOKU_MIMETYPES";
    public static final String SUDOKU_SEPARATOR = "SUDOKU_SEPARATOR";

    private String filePath;
    private String separator = ",";
    private Long maxFileSize = 1_0000L;
    private List<String> mimetypes = List.of("csv", "text/plain", "application/vnd.ms-excel");

    private PropertyManager() {
    }

    public static PropertyManager of(String[] args) {
        var prop = new PropertyManager();
        return prop.load(args);
    }


    private PropertyManager load(String[] args) {
        if (args == null || args.length == 0) {
            MessageOutput.of(MessageOutput.Codes.EXECUTION_PARAM_IS_EMPTY).invokeSignal();
        } else if (args.length > 1) {
            MessageOutput.of(MessageOutput.Codes.EXECUTION_PARAM_CAN_BE_ONLY_ONE).invokeSignal();
        } else {
            filePath = args[0];
            if (filePath.isEmpty()) MessageOutput.of(MessageOutput.Codes.EXECUTION_PARAM_IS_EMPTY).invokeSignal();
        }
        setupEnvVariables();
        return this;
    }

    private void setupEnvVariables() {
        var env = System.getenv();
        separator = Optional.ofNullable(System.getenv(SUDOKU_SEPARATOR)).orElse(separator);
        mimetypes = env.get(MIME_TYPES) != null ? Arrays.stream(env.get(MIME_TYPES).split(",")).collect(Collectors.toList()) : mimetypes;
        var readMaxFileSize = env.get(MAX_FILE_SIZE);
        try {
            maxFileSize = readMaxFileSize != null ? Long.parseLong(readMaxFileSize) : maxFileSize;
        } catch (NumberFormatException ex) {
            MessageOutput.of(MessageOutput.Codes.EXECUTION_PARAM_CANNOT_READ_PROPERLY, MAX_FILE_SIZE, readMaxFileSize).invokeSignal();
        }
    }


}
