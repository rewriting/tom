#!/bin/tcsh
cd $1
gom PolygraphicProgram.gom
tom XMLhandler.t
tom StructureRuleHandler.t
tom XMLProgramHandler.t
tom Compiler.t
javac compiler/XMLhandler.java
javac compiler/StructureRuleHandler.java
javac compiler/XMLProgramHandler.java
javac compiler/Compiler.java
