package tools;

import polygraphicprogram.*;
import polygraphicprogram.types.*;
import polygraphicprogram.types.onepath.*;
import java.io.*;
import compiler.XMLhandler;
import compiler.StructureRuleHandler;
import tools.TestCellSet;

public class MakeValidationTestSet{
//Generates a set of tests

	%include { polygraphicprogram/PolygraphicProgram.tom }

	public static String path="/Users/aurelien/polygraphWorkspace/PolygraphesApp/polygraphes/polygraphesWithID/";

	//main : creates a xml file ("input.xml") describing a 2-Path 
	public static void main(String[] args) {
	//tests	
		//xml initialization
		String testxml="<ValidationTest>\n";
		//TEST SET
		//eraser test
		TwoPath eraserSource=`TwoC1(MakeInput.makeNat(6),StructureRuleHandler.makeEraser("nat"));
		TwoPath eraserTarget=`TwoId(Id());
		testxml+=makeTest("eraser test",eraserSource,eraserTarget);
		//duplication test
		TwoPath duplicationSource=`TwoC1(MakeInput.makeNat(3),StructureRuleHandler.makeDuplication("nat"));
		TwoPath duplicationTarget=`TwoC0(MakeInput.makeNat(3),MakeInput.makeNat(3));
		testxml+=makeTest("duplication test",duplicationSource,duplicationTarget);
		//permutation test
		TwoPath permutationSource=`TwoC1(TwoC0(MakeInput.makeNat(3),MakeInput.makeNat(1)),StructureRuleHandler.makePermutation((OneCell)TestCellSet.nat,(OneCell)TestCellSet.nat));
		TwoPath permutationTarget=`TwoC0(MakeInput.makeNat(1),MakeInput.makeNat(3));
		testxml+=makeTest("permutation test",permutationSource,permutationTarget);
		//addition test
		TwoPath additionSource=`TwoC1(TwoC0(MakeInput.makeNat(3),MakeInput.makeNat(2)),TestCellSet.plus);
		TwoPath additionTarget=MakeInput.makeNat(5);
		testxml+=makeTest("addition test",additionSource,additionTarget);
		//soustraction test
		TwoPath substractionSource=`TwoC1(TwoC0(MakeInput.makeNat(5),MakeInput.makeNat(2)),TestCellSet.minus);
		TwoPath substractionTarget=MakeInput.makeNat(3);
		testxml+=makeTest("substraction test",substractionSource,substractionTarget);
		//multiplication test
		TwoPath multiplicationSource=`TwoC1(TwoC0(MakeInput.makeNat(4),MakeInput.makeNat(2)),TestCellSet.multiplication);
		TwoPath multiplicationTarget=MakeInput.makeNat(8);
		testxml+=makeTest("multiplication test",multiplicationSource,multiplicationTarget);
		//division test
		TwoPath divisionSource=`TwoC1(TwoC0(MakeInput.makeNat(7),MakeInput.makeNat(2)),TestCellSet.division);
		TwoPath divisionTarget=MakeInput.makeNat(3);
		testxml+=makeTest("division test",divisionSource,divisionTarget);
		//combined test
		TwoPath combinedSource=``TwoC1(TwoC0(TwoC1(TwoC0(MakeInput.makeNat(2),MakeInput.makeNat(6)),TestCellSet.multiplication),MakeInput.makeNat(3)),TestCellSet.division);
		TwoPath combinedTarget=MakeInput.makeNat(3);
		testxml+=makeTest("combined test",combinedSource,combinedTarget);
		//square test
		TwoPath squareSource=`TwoC1(MakeInput.makeNat(3),TestCellSet.carre);
		TwoPath squareTarget=MakeInput.makeNat(9);
		testxml+=makeTest("square test",squareSource,squareTarget);
		//cube test
		TwoPath cubeSource=`TwoC1(MakeInput.makeNat(2),TestCellSet.cube);
		TwoPath cubeTarget=MakeInput.makeNat(8);
		testxml+=makeTest("cube test",cubeSource,cubeTarget);
		//equal test true
		TwoPath equalSource=`TwoC1(TwoC0(TwoC1(TwoC0(MakeInput.makeNat(2),MakeInput.makeNat(6)),TestCellSet.plus),TwoC1(TwoC0(MakeInput.makeNat(2),MakeInput.makeNat(4)),TestCellSet.multiplication)),TestCellSet.equal);
		TwoPath equalTarget=TestCellSet.vrai;
		testxml+=makeTest("equal test true",equalSource,equalTarget);
		//equal test false
		TwoPath equalSource2=`TwoC1(TwoC0(TwoC1(TwoC0(MakeInput.makeNat(2),MakeInput.makeNat(0)),TestCellSet.plus),TwoC1(TwoC0(MakeInput.makeNat(2),MakeInput.makeNat(4)),TestCellSet.multiplication)),TestCellSet.equal);
		TwoPath equalTarget2=TestCellSet.faux;
		testxml+=makeTest("equal test false",equalSource2,equalTarget2);
		//less or equal test true
		TwoPath lessOrEqualSource=`TwoC1(TwoC0(MakeInput.makeNat(6),MakeInput.makeNat(7)),TestCellSet.lessOrEqual);
		TwoPath lessOrEqualTarget=TestCellSet.vrai;
		testxml+=makeTest("less or equal test true",lessOrEqualSource,lessOrEqualTarget);
		//less or equal test false
		TwoPath lessOrEqualSource2=`TwoC1(TwoC0(MakeInput.makeNat(6),MakeInput.makeNat(4)),TestCellSet.lessOrEqual);
		TwoPath lessOrEqualTarget2=TestCellSet.faux;
		testxml+=makeTest("less or equal test false",lessOrEqualSource2,lessOrEqualTarget2);
		//sort test
		int[] list1={7,8,3,2,6};
		int[] list2={9,5,7,4,1};
		int[] list3={1,2,3,4,5,6,7,7,8,9};
		TwoPath sortSource=`TwoC1(TwoC0(MakeInput.makeList(list1),MakeInput.makeList(list2)),TestCellSet.merge,TestCellSet.sort);
		TwoPath sortTarget=MakeInput.makeList(list3);
		testxml+=makeTest("sort test",sortSource,sortTarget);
		//list equality test
		TwoPath lEqualSource=`TwoC1(TwoC0(MakeInput.makeList(list1),MakeInput.makeList(list1)),TestCellSet.lEqual);
		TwoPath lEqualTarget=TestCellSet.vrai;
		testxml+=makeTest("list equality test",lEqualSource,lEqualTarget);
		//list equality test false
		TwoPath lEqualSource2=`TwoC1(TwoC0(MakeInput.makeList(list1),MakeInput.makeList(list2)),TestCellSet.lEqual);
		TwoPath lEqualTarget2=TestCellSet.faux;
		testxml+=makeTest("list equality test false",lEqualSource2,lEqualTarget2);
		//saves it
		testxml+="\n</ValidationTest>";
		try{
			XMLhandler.save(testxml,new File(path+"testset.xml"));
		}catch(Exception e){e.printStackTrace();}

	}
	
	public static String makeTest(String description,TwoPath source,TwoPath target) {
		String xmltest="<Test name=\""+description+"\" >\n";
		xmltest+="<Source>\n"+XMLhandler.twoPath2XML(source)+"\n</Source>";
		xmltest+="<Target>\n"+XMLhandler.twoPath2XML(target)+"\n</Target>";
		return xmltest+"\n</Test>";
		}

}