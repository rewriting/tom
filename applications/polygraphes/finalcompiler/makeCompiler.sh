#!/bin/tcsh
cd $1
gom adt/PolygraphicProgram.gom
tom library/XMLhandler.t
tom library/StructureRuleHandler.t
tom compiler/XMLProgramHandler.t
tom compiler/Compiler.t
javac library/XMLhandler.java
javac library/StructureRuleHandler.java
javac compiler/XMLProgramHandler.java
javac compiler/Compiler.java
