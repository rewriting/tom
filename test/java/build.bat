@echo off
setlocal

set LOCALCLASSPATH=%TOM_HOME%/lib/compiletime/junit.jar;%TOM_HOME%/lib/compiletime/ant-launcher.jar;%TOM_HOME%/lib/compiletime/ant.jar;%TOM_HOME%/lib/compiletime/ant-antlr.jar;%TOM_HOME%/lib/compiletime/antlr3.jar;%TOM_HOME%/lib/compiletime/ant-junit.jar

set ANT_OPTS=
set ant_exec_command=java %ANT_OPTS% -classpath "%LOCALCLASSPATH%" -Dant.library.dir="%TOM_HOME%/lib/compiletime" org.apache.tools.ant.launch.Launcher %ANT_ARGS% -cp "%CLASSPATH%"

rem Get command line arguments and save them in
rem the CMD_LINE_ARGS environment variable
set CMD_LINE_ARGS=
:setArgs
if ""%1""=="""" goto doneSetArgs
if ""%CMD_LINE_ARGS%""=="""" (set CMD_LINE_ARGS=%1) else (set CMD_LINE_ARGS=%CMD_LINE_ARGS% %1)
shift
goto setArgs
:doneSetArgs

if exist %CMD_LINE_ARGS% set CMD_LINE_ARGS=-Dexample="%CMD_LINE_ARGS%" build

%ant_exec_command% %CMD_LINE_ARGS%
