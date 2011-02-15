@echo off
setlocal

set LOCALCLASSPATH=./stable/lib/compiletime/junit.jar;./stable/lib/compiletime/ant-launcher.jar;./stable/lib/compiletime/ant.jar;./stable/lib/compiletime/ant-antlr.jar;./stable/lib/compiletime/antlr3.jar;./stable/lib/compiletime/ant-junit.jar

set ANT_OPTS=-Xmx512m -XX:PermSize=128m -XX:MaxPermSize=128m
set ant_exec_command=java %ANT_OPTS% -classpath "%LOCALCLASSPATH%" -Dant.library.dir="./stable/lib/compiletime" org.apache.tools.ant.launch.Launcher %ANT_ARGS% -cp "%CLASSPATH%"

rem Get command line arguments and save them in
rem the CMD_LINE_ARGS environment variable
set CMD_LINE_ARGS=%ant_exec_command%
:setArgs
if ""%1""=="""" goto doneSetArgs
set CMD_LINE_ARGS=%CMD_LINE_ARGS% %1
shift
goto setArgs
:doneSetArgs

rem echo %CMD_LINE_ARGS%

%CMD_LINE_ARGS%
