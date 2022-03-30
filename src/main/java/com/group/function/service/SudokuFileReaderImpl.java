package com.group.function.service;

import com.group.function.common.MessageOutput;
import com.group.function.common.PropertyManager;
import lombok.AllArgsConstructor;
import lombok.NonNull;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;

import static com.group.function.common.MessageOutput.Codes.*;

/**
 * @author nbtwszol
 */
@AllArgsConstructor(staticName = "of")
public class SudokuFileReaderImpl implements SudokuFileReader {

    @NonNull
    private PropertyManager propertyManager;

    @NonNull
    private Processor sudokuProcessor;

    @Override
    public int[][] parseFile(String filePath) {
        validateFile(filePath).invokeSignal();
        return sudokuProcessor.process(filePath, propertyManager);
    }

    private MessageOutput validateFile(String filePath) {
        try {
            var path = Paths.get(filePath);
            if (!Files.exists(path)) return MessageOutput.of(FILE_NOT_FOUND, filePath);
            if (!Files.isReadable(path)) return MessageOutput.of(FILE_CANNOT_READ, filePath);
            if (Files.isDirectory(path)) return MessageOutput.of(FILE_IS_A_DIRECTORY, filePath);
            var sizeFile = Files.size(path);
            if (sizeFile > propertyManager.getMaxFileSize())
                return MessageOutput.of(FILE_IS_TO_BIG, filePath, "" + propertyManager.getMaxFileSize(), "" + sizeFile);
            var mimeType = Files.probeContentType(path);
            if (mimeType != null && !propertyManager.getMimetypes().contains(mimeType.toLowerCase()))
                return MessageOutput.of(FILE_IS_NOT_A_TEXT_FILE, filePath, mimeType);

            return MessageOutput.of(MessageOutput.Codes.SUCCESSFULLY);
        } catch (IOException e) {
            return MessageOutput.of(e);
        }

    }

    @AllArgsConstructor
    public static class BufferedProcessor implements Processor {

        @Override
        public int[][] process(String fileName, PropertyManager propertyManager) {
            var result = new int[9][9];
            try (BufferedReader fileBufferReader = getBuffer(fileName)) {
                String fileLineContent;
                var idx = 0;
                var realLineIdx = 1;
                while ((fileLineContent = fileBufferReader.readLine()) != null) {
                    if (realLineIdx == 1) {
                        fileLineContent = skipUTF8BOM(fileLineContent);
                    }
                    var split = fileLineContent.split(propertyManager.getSeparator());
                    var row = safeTransform(split);
                    if (idx > 8) {
                        MessageOutput.of(MATRIX_INVALID_LINE_NUMBER
                                , String.valueOf(realLineIdx)).invokeSignal();
                    }
                    result[idx] = row;
                    idx++;
                    realLineIdx++;
                }
                if (idx != 9) MessageOutput.of(MATRIX_INVALID_LINE_NUMBER
                        , String.valueOf(idx)).invokeSignal();
            } catch (IOException e) {
                MessageOutput.of(e).invokeSignal();
            }

            return result;
        }

        private String skipUTF8BOM(String string) {
            var uTF8BOM = "\uFEFF";
            if (string.startsWith(uTF8BOM)) {
                string = string.substring(1);
            }
            return string;
        }

        protected BufferedReader getBuffer(String fileName) throws IOException {
            return new BufferedReader(new FileReader(fileName, StandardCharsets.UTF_8));
        }

        private int[] safeTransform(String[] row) {
            if (row.length != 9)
                MessageOutput.of(MATRIX_MAX_ELEMENTS_IN_ROW
                        , String.valueOf(row.length)).invokeSignal();
            var result = new int[9];
            for (var idx = 0; idx < row.length; idx++) {
                try {
                    var cellValue = Integer.parseInt(row[idx]);
                    result[idx] = cellValue;
                } catch (NumberFormatException e) {
                    MessageOutput.of(MATRIX_INVALID_CHARACTER
                            , row[idx]).invokeSignal();
                }

            }
            return result;
        }
    }

    /**
     * Can be used any file processor in case when extra performance would be required
     */
    interface Processor {
        int[][] process(String fileName, PropertyManager propertyManager);

    }
}



