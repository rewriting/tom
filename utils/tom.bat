@echo off
if "%OS%" == "Windows_NT" setlocal
rem ---------------------------------------------------------------------------
rem Script for TOM compiler
rem
rem Environment Variable Prequisites
rem
rem   TOM_HOME      May point at your Tom "build" directory.
rem                 if not, we try to guess.
rem
rem   TOM_OPTS      (Optional) TOM options.
rem
rem   TOM_LIB       (Optional) TOM classpath.
rem
rem   JAVA_HOME     Must point at your Java Development Kit installation.
rem
rem   JAVA_OPTS     (Optional) Java runtime options.
rem
rem $Id: tom.bat,v 1.2 2004/10/09 14:40:12 mmoossen Exp $
rem $Id: tom.bat,v 1.1 2004/09/15 20:17:43 mmoossen Exp $
rem $Id: tom.bat,v 1.0 2004/09/15 19:53:43 mmoossen Exp $
rem ---------------------------------------------------------------------------

rem Make sure prerequisite environment variables are set
if not "%JAVA_HOME%" == "" goto gotJavaHome
echo The JAVA_HOME environment variable is not defined
echo This environment variable is needed to run this program
goto end
:gotJavaHome
if not exist "%JAVA_HOME%\bin\java.exe" goto noJavaHome
if not exist "%JAVA_HOME%\bin\javaw.exe" goto noJavaHome
if not exist "%JAVA_HOME%\bin\jdb.exe" goto noJavaHome
if not exist "%JAVA_HOME%\bin\javac.exe" goto noJavaHome
goto okJavaHome
:noJavaHome
echo The JAVA_HOME environment variable is not defined correctly
echo This environment variable is needed to run this program
goto end
:okJavaHome

rem Guess TOM_HOME if not defined
if not "%TOM_HOME%" == "" goto gotHome
set TOM_HOME=.
if exist "%TOM_HOME%\bin\tom.bat" goto okHome
set TOM_HOME=..
:gotHome
if exist "%TOM_HOME%\bin\tom.bat" goto okHome
echo The TOM_HOME environment variable is not defined correctly
echo This environment variable is needed to run this program
goto end
:okHome

rem Check for TOM_LIB variable
if not "%TOM_LIB%" == "" goto okLib
rem Add all jars in lib dir to TOM_LIB variable (will include a initial semicolon ";")
for %%i in ("%TOM_HOME%\lib\*.jar") do call "%TOM_HOME%\bin\tlappend.bat" %%i
:okLib

rem Check for TOM_OPTS variable
if not "%TOM_OPTS%" == "" goto okOpts
rem Use standard options
set TOM_OPTS=-X "%TOM_HOME%\tom.xml" --import "%TOM_HOME%\share\jtom"
set STD_OPTS=true
:okOpts

rem ----- Execute The Requested Command ---------------------------------------
echo Using JAVA_HOME:       %JAVA_HOME%
echo Using TOM_HOME:        %TOM_HOME%

rem Set standard command for invoking Java.
set _RUNJAVA="%JAVA_HOME%\bin\java"
set MAINCLASS=jtom.Tom

rem Get command line arguments and save them in 
rem the CMD_LINE_ARGS environment variable
set CMD_LINE_ARGS=
:setArgs
if ""%1""=="""" goto doneSetArgs
if not ""%STD_OPTS%"" == ""true"" goto noStdOpts
rem Check for options clash when using standard options
if ""%1""==""-X"" goto optClash
if ""%1""==""-I"" goto optClash
if ""%1""==""--import"" goto optClash
:noStdOpts
set CMD_LINE_ARGS=%CMD_LINE_ARGS% %1
shift
goto setArgs
:optClash
rem Here there are two possibilities:
rem 1. to encorage the user to use the TOM_OPTS environment variable
%echo You should specify the configuration and import files
%echo using the TOM_OPTS environment variable
%goto end
rem 2. to use the user arguments instead of the TOM_OPTS environment variable
set TOM_OPTS=
:doneSetArgs

rem execute TOM
%_RUNJAVA% %JAVA_OPTS% -classpath "%CLASSPATH%%TOM_LIB%" %MAINCLASS% %TOM_OPTS% %CMD_LINE_ARGS%

:end