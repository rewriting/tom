rem ---------------------------------------------------------------------------
rem Append to TOM_LIB
rem
rem $Id: tlappend.bat,v 1.0 2004/09/15 20:17:09 mmoossen Exp $
rem ---------------------------------------------------------------------------

rem Process the first argument
if ""%1"" == """" goto end
set TOM_LIB=%TOM_LIB%;%1
shift

rem Process the remaining arguments
rem if the element to add contains spaces
:setArgs
if ""%1"" == """" goto doneSetArgs
set TOM_LIB=%TOM_LIB% %1
shift
goto setArgs
:doneSetArgs
:end
