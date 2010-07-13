#!/bin/sh
FILELIST="Array.t Peano.t Test.t TestAlgebraic.t TestArray.t TestArray2.t TestArrayNonLinear.t TestBackQuote.t TestBuiltinNS.t TestDoubleSubject.t TestLabelNS.t TestList2NS.t TestListNS.t TestListNonLinearNS.t TestMatch.t TestMatchInference.t TestNonVarSubjects.t TestOptimizer.t TestPeano.t TestReflectiveStrategy.t TestStrategy.t TestSublistsNS.t TestVarVarStar.t TomListNS.t cfib1.t loulou.t acmatching/TestAC.t acmatching/TestBool3.t andor/TestAndOr.t andor/TestAndOrConstraintOnlyNS.t andor/TestDottedNotation.t numeric/TestNumericConditions.t rule/Array.t rule/Peano.t rule/TestArray.t rule/TestPeano.t rule/TomListNS.t xml/EmptyXml.t xml/TestEncoding.t xml/TestXml.t xml/Xml.t"

tom -nt $FILELIST &> logAllFiles.txt

#FILELIST2="Array Peano Test TestAlgebraic TestArray TestArray2 TestArrayNonLinear TestBackQuote TestBuiltinNS TestDoubleSubject TestLabelNS TestList2NS TestListNS TestListNonLinearNS TestMatch TestMatchInference TestNonVarSubjects TestOptimizer TestPeano TestReflectiveStrategy TestStrategy TestSublistsNS TestVarVarStar TomListNS cfib1 loulou"

#for f in $FILELIST2; do
#	tom -nt $f.t &> $f.log
#done

#for f in $FILELIST2; do
#	javac $f.java
#done
