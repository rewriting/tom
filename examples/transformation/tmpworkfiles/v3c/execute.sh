#!/bin/sh

echo "(Prototype: SimplePDLToPetrinet v3c)"
echo "Compiling SimplePDLToPetrinet3c.t file"
echo 
tom -p -i SimplePDLToPetri3c.t

echo "Compiling SimplePDLToPetrinet3c.java file"
echo 
javac -cp .:simplepdlsemantics_1.1.0c.jar:petrinetsemantics_1.0.0.jar:$CLASSPATH SimplePDLToPetri3c.java

echo 
echo "Executing SimplePDLToPetri3c"
echo 
java -cp .:simplepdlsemantics_1.1.0c.jar:petrinetsemantics_1.0.0.jar:$CLASSPATH SimplePDLToPetri3c

