#!/bin/tcsh
tom tools/TestCellSet.t 
tom tools/MakeProgram.t
tom tools/MakeInput.t
tom tools/ReadOutput.t
tom tools/MakeValidationTestSet.t
tom tools/MakeValidationTestFile.t
javac tools/TestCellSet.java
javac tools/MakeProgram.java
javac tools/MakeInput.java
javac tools/ReadOutput.java
javac tools/MakeValidationTestSet.java
javac tools/MakeValidationTestFile.java