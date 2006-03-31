@echo off

set LOCALCLASSPATH=./stable/lib/junit.jar
set CLASSPATH=%LOCALCLASSPATH%

rem Get command line arguments and save them in 
rem the CMD_LINE_ARGS environment variable
set CMD_LINE_ARGS=
:setArgs
if ""%1""=="""" goto doneSetArgs
set CMD_LINE_ARGS=%CMD_LINE_ARGS% %1
shift
goto setArgs
:doneSetArgs

echo %CMD_LINE_ARGS%

ant %CMD_LINE_ARGS%
