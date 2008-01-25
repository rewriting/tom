package tools;

import polygraphicprogram.types.*;
import tools.XMLhandler;
import tools.TestCellSet;
import java.io.*;
import java.util.Vector;
import java.util.Iterator;

//generates a test program in xml
public class MakeProgram{

	%include { polygraphicprogram/PolygraphicProgram.tom }


	public static String path="/Users/aurelien/polygraphWorkspace/PolygraphesApp/polygraphes/polygraphesWithID/";


	public static void main(String[] args) {

	//first we prepare and associate all types and their constructors and also all functions and their rules

	//TYPES & associated CONSTRUCTORS
		//nat
		Vector<TwoPath> natConstructors=new Vector<TwoPath>();
		natConstructors.add(TestCellSet.zero);
		natConstructors.add(TestCellSet.succ);
		//list
		Vector<TwoPath> listConstructors=new Vector<TwoPath>();
		listConstructors.add(TestCellSet.consList);
		listConstructors.add(TestCellSet.add);
		//boolean
		Vector<TwoPath> booleanConstructors=new Vector<TwoPath>();
		booleanConstructors.add(TestCellSet.vrai);
		booleanConstructors.add(TestCellSet.faux);

	//FUNCTIONS & associated RULES
		//plus
		Vector<ThreePath> plusRules=new Vector<ThreePath>();
		plusRules.add(TestCellSet.plusZero);
		plusRules.add(TestCellSet.plusSucc);
		//minus
		Vector<ThreePath> minusRules=new Vector<ThreePath>();
		minusRules.add(TestCellSet.minusZero1);
		minusRules.add(TestCellSet.minusZero2);
		minusRules.add(TestCellSet.minusDoubleSucc);
		//multiplication
		Vector<ThreePath> multiplicationRules=new Vector<ThreePath>();
		multiplicationRules.add(TestCellSet.multZero);
		multiplicationRules.add(TestCellSet.multSucc);
		//division
		Vector<ThreePath> divisionRules=new Vector<ThreePath>();
		divisionRules.add(TestCellSet.divZero);
		divisionRules.add(TestCellSet.divSucc);
		//carre
		Vector<ThreePath> carreRules=new Vector<ThreePath>();
		carreRules.add(TestCellSet.zeroCarre);
		carreRules.add(TestCellSet.succCarre);
		//cube
		Vector<ThreePath> cubeRules=new Vector<ThreePath>();
		cubeRules.add(TestCellSet.zeroCube);
		cubeRules.add(TestCellSet.succCube);
		//equal
		Vector<ThreePath> equalRules=new Vector<ThreePath>();
		equalRules.add(TestCellSet.zeroEqualZero);
		equalRules.add(TestCellSet.succEqualSucc);
		equalRules.add(TestCellSet.succEqualZero);
		equalRules.add(TestCellSet.zeroEqualSucc);
		//append
		Vector<ThreePath> appendRules=new Vector<ThreePath>();
		appendRules.add(TestCellSet.appendToAdd);
		//split
		Vector<ThreePath> splitRules=new Vector<ThreePath>();
		splitRules.add(TestCellSet.consListSplit);
		splitRules.add(TestCellSet.addSplit);
		splitRules.add(TestCellSet.doubleAddSplit);
		//sort
		Vector<ThreePath> sortRules=new Vector<ThreePath>();
		sortRules.add(TestCellSet.consListSort);
		sortRules.add(TestCellSet.addSort);
		sortRules.add(TestCellSet.doubleAddSort);
		//merge
		Vector<ThreePath> mergeRules=new Vector<ThreePath>();
		mergeRules.add(TestCellSet.consListMerge1);
		mergeRules.add(TestCellSet.consListMerge2);
		mergeRules.add(TestCellSet.doubleAddMerge);
		//lessOrEqual
		Vector<ThreePath> lessOrEqualRules=new Vector<ThreePath>();
		lessOrEqualRules.add(TestCellSet.zeroLess);
		lessOrEqualRules.add(TestCellSet.succZeroLess);
		lessOrEqualRules.add(TestCellSet.doubleSuccLess);
		//lEqual
		Vector<ThreePath> lEqualRules=new Vector<ThreePath>();
		lEqualRules.add(TestCellSet.consListEqualconsList);
		lEqualRules.add(TestCellSet.addEqualconsList);
		lEqualRules.add(TestCellSet.consListEqualAdd);
		lEqualRules.add(TestCellSet.addEqualAdd);
		//mergeSwitch
		Vector<ThreePath> mergeSwitchRules=new Vector<ThreePath>();
		mergeSwitchRules.add(TestCellSet.merge1);
		mergeSwitchRules.add(TestCellSet.merge2);
		//not
		Vector<ThreePath> notRules=new Vector<ThreePath>();
		notRules.add(TestCellSet.notTrue);
		notRules.add(TestCellSet.notFalse);
		//or
		Vector<ThreePath> orRules=new Vector<ThreePath>();
		orRules.add(TestCellSet.trueOrTrue);
		orRules.add(TestCellSet.trueOrFalse);
		orRules.add(TestCellSet.FalseOrTrue);
		orRules.add(TestCellSet.FalseOrFalse);
		//and
		Vector<ThreePath> andRules=new Vector<ThreePath>();
		andRules.add(TestCellSet.trueAndTrue);
		andRules.add(TestCellSet.trueAndFalse);
		andRules.add(TestCellSet.FalseAndTrue);
		andRules.add(TestCellSet.FalseAndFalse);

		//then we prepare the string for the xml program file
		
		//root
		String program="<PolygraphicProgram Name=\"TestProgramv2\">\n";
		//we add the types and their constructors
		program+=makeTypeXML(TestCellSet.nat,natConstructors);
		program+=makeTypeXML(TestCellSet.list,listConstructors);
		program+=makeTypeXML(TestCellSet.bool,booleanConstructors);
		//and we add the functions and their rules
		program+=makeFunctionXML(TestCellSet.plus,plusRules);
		program+=makeFunctionXML(TestCellSet.minus,minusRules);
		program+=makeFunctionXML(TestCellSet.multiplication,multiplicationRules);
		program+=makeFunctionXML(TestCellSet.division,divisionRules);
		program+=makeFunctionXML(TestCellSet.carre,carreRules);
		program+=makeFunctionXML(TestCellSet.cube,cubeRules);
		program+=makeFunctionXML(TestCellSet.equal,equalRules);
		program+=makeFunctionXML(TestCellSet.split,splitRules);
		program+=makeFunctionXML(TestCellSet.sort,sortRules);
		program+=makeFunctionXML(TestCellSet.merge,mergeRules);
		program+=makeFunctionXML(TestCellSet.lEqual,lEqualRules);
		program+=makeFunctionXML(TestCellSet.mergeSwitch,mergeSwitchRules);
		program+=makeFunctionXML(TestCellSet.lessOrEqual,lessOrEqualRules);
		program+=makeFunctionXML(TestCellSet.append,appendRules);
		program+=makeFunctionXML(TestCellSet.not,notRules);
		program+=makeFunctionXML(TestCellSet.or,orRules);
		program+=makeFunctionXML(TestCellSet.and,andRules);
		//we close the root tag
		program+="</PolygraphicProgram>";
		//and we save the string in a file
		try{
			XMLhandler.save(program,new File(path+"testprogramv2.xml"));
		}catch(Exception e){e.printStackTrace();}
	}

	//constructs a xml description of a function with its rules
	public static String makeFunctionXML(TwoPath function, Vector<ThreePath> functionRules){
		String xml="";
		xml+="<Function>\n";
		xml+=XMLhandler.twoPath2XML(function);
		for (Iterator iterator = functionRules.iterator(); iterator.hasNext();) {
			ThreePath rule = (ThreePath) iterator.next();
			xml+="<Rule>\n";
			xml+=XMLhandler.threePath2XML(rule);
			xml+="</Rule>\n";
		}
		xml+="</Function>\n";
	return xml;
	}

	//constructs a xml description of a type with its constructors
	public static String makeTypeXML(OnePath type, Vector<TwoPath> constructors){
		String xml="";
		xml+="<Type>\n";
		xml+=XMLhandler.onePath2XML(type);
		for (Iterator iterator = constructors.iterator(); iterator.hasNext();) {
			TwoPath constructor = (TwoPath) iterator.next();
			xml+="<Constructor>\n";
			xml+=XMLhandler.twoPath2XML(constructor);
			xml+="</Constructor>\n";
		}
		xml+="</Type>\n";
		return xml;
	}

}