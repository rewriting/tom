#!bin/bash
#echo "${TOM_HOME}/src/lib/tools/antlr-3.2.jar"
java -cp ${TOM_HOME}/src/lib/tools/antlr-3.2.jar:$CLASSPATH org.antlr.Tool *.g
