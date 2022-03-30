@ECHO OFF
ECHO Parameters list is: %*
ECHO '********START*************'

REM SET SUDOKU_MAX_FILE_SIZE=1
REM SET SUDOKU_MIMETYPES=csv
REM SET SUDOKU_SEPARATOR=1

java -jar Sudoku-executable-jar-with-dependencies.jar %*
ECHO %ERRORLEVEL%
