package tools;

import adt.polygraphicprogram.types.*;
import library.XMLhandler;
import tools.TestCellSet;
import java.io.*;
import java.util.ArrayList;
import java.util.Iterator;

//generates a test program in xml
public class MakeProgram{

	%include { ../adt/polygraphicprogram/PolygraphicProgram.tom }


	public static void main(String[] args) {
		
		String programPath="";
		if(args.length!=1){
			System.out.println("you must indicate the desired path of the xml program");
			}
		else{
			if(args[0].substring(args[0].length()-4, args[0].length()).equals(".xml")){
				programPath=args[0];
	//first we prepare and associate all types and their constructors and also all functions and their rules

	//TYPES & associated CONSTRUCTORS
		//nat
		ArrayList<TwoPath> natConstructors=new ArrayList<TwoPath>();
		natConstructors.add(TestCellSet.zero);
		natConstructors.add(TestCellSet.succ);
		//list
		ArrayList<TwoPath> listConstructors=new ArrayList<TwoPath>();
		listConstructors.add(TestCellSet.consList);
		listConstructors.add(TestCellSet.add);
		//boolean
		ArrayList<TwoPath> booleanConstructors=new ArrayList<TwoPath>();
		booleanConstructors.add(TestCellSet.vrai);
		booleanConstructors.add(TestCellSet.faux);

	//FUNCTIONS & associated RULES
		//plus
		ArrayList<ThreePath> plusRules=new ArrayList<ThreePath>();
		plusRules.add(TestCellSet.plusZero);
		plusRules.add(TestCellSet.plusSucc);
		//minus
		ArrayList<ThreePath> minusRules=new ArrayList<ThreePath>();
		minusRules.add(TestCellSet.minusZero1);
		minusRules.add(TestCellSet.minusZero2);
		minusRules.add(TestCellSet.minusDoubleSucc);
		//multiplication
		ArrayList<ThreePath> multiplicationRules=new ArrayList<ThreePath>();
		multiplicationRules.add(TestCellSet.multZero);
		multiplicationRules.add(TestCellSet.multSucc);
		//division
		ArrayList<ThreePath> divisionRules=new ArrayList<ThreePath>();
		divisionRules.add(TestCellSet.divZero);
		divisionRules.add(TestCellSet.divSucc);
		//carre
		ArrayList<ThreePath> carreRules=new ArrayList<ThreePath>();
		carreRules.add(TestCellSet.zeroCarre);
		carreRules.add(TestCellSet.succCarre);
		//cube
		ArrayList<ThreePath> cubeRules=new ArrayList<ThreePath>();
		cubeRules.add(TestCellSet.zeroCube);
		cubeRules.add(TestCellSet.succCube);
		//equal
		ArrayList<ThreePath> equalRules=new ArrayList<ThreePath>();
		equalRules.add(TestCellSet.zeroEqualZero);
		equalRules.add(TestCellSet.succEqualSucc);
		equalRules.add(TestCellSet.succEqualZero);
		equalRules.add(TestCellSet.zeroEqualSucc);
		//append
		ArrayList<ThreePath> appendRules=new ArrayList<ThreePath>();
		appendRules.add(TestCellSet.appendToAdd);
		//split
		ArrayList<ThreePath> splitRules=new ArrayList<ThreePath>();
		splitRules.add(TestCellSet.consListSplit);
		splitRules.add(TestCellSet.addSplit);
		splitRules.add(TestCellSet.doubleAddSplit);
		//sort
		ArrayList<ThreePath> sortRules=new ArrayList<ThreePath>();
		sortRules.add(TestCellSet.consListSort);
		sortRules.add(TestCellSet.addSort);
		sortRules.add(TestCellSet.doubleAddSort);
		//merge
		ArrayList<ThreePath> mergeRules=new ArrayList<ThreePath>();
		mergeRules.add(TestCellSet.consListMerge1);
		mergeRules.add(TestCellSet.consListMerge2);
		mergeRules.add(TestCellSet.doubleAddMerge);
		//lessOrEqual
		ArrayList<ThreePath> lessOrEqualRules=new ArrayList<ThreePath>();
		lessOrEqualRules.add(TestCellSet.zeroLess);
		lessOrEqualRules.add(TestCellSet.succZeroLess);
		lessOrEqualRules.add(TestCellSet.doubleSuccLess);
		//lEqual
		ArrayList<ThreePath> lEqualRules=new ArrayList<ThreePath>();
		lEqualRules.add(TestCellSet.consListEqualconsList);
		lEqualRules.add(TestCellSet.addEqualconsList);
		lEqualRules.add(TestCellSet.consListEqualAdd);
		lEqualRules.add(TestCellSet.addEqualAdd);
		//mergeSwitch
		ArrayList<ThreePath> mergeSwitchRules=new ArrayList<ThreePath>();
		mergeSwitchRules.add(TestCellSet.merge1);
		mergeSwitchRules.add(TestCellSet.merge2);
		//not
		ArrayList<ThreePath> notRules=new ArrayList<ThreePath>();
		notRules.add(TestCellSet.notTrue);
		notRules.add(TestCellSet.notFalse);
		//or
		ArrayList<ThreePath> orRules=new ArrayList<ThreePath>();
		orRules.add(TestCellSet.trueOrTrue);
		orRules.add(TestCellSet.trueOrFalse);
		orRules.add(TestCellSet.FalseOrTrue);
		orRules.add(TestCellSet.FalseOrFalse);
		//and
		ArrayList<ThreePath> andRules=new ArrayList<ThreePath>();
		andRules.add(TestCellSet.trueAndTrue);
		andRules.add(TestCellSet.trueAndFalse);
		andRules.add(TestCellSet.FalseAndTrue);
		andRules.add(TestCellSet.FalseAndFalse);
		//Fibonacci
		ArrayList<ThreePath> fiboRules=new ArrayList<ThreePath>();
		fiboRules.add(TestCellSet.fibZero);
		fiboRules.add(TestCellSet.fibSuccZero);
		fiboRules.add(TestCellSet.fibDoubleSucc);
		
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
		program+=makeFunctionXML(TestCellSet.fibonacci,fiboRules);
		//we close the root tag
		program+="</PolygraphicProgram>";
		//and we save the string in a file
		try{
			String[] folderPath=programPath.split("[^/]+\\.xml");
			(new File(folderPath[0])).mkdir();
			XMLhandler.save(program,new File(programPath));
		}catch(Exception e){e.printStackTrace();}
			}
		else{
		System.out.println("the first and only argument must be an xml file (*.xml)");}
		}
	}

	//constructs a xml description of a function with its rules
	public static String makeFunctionXML(TwoPath function, ArrayList<ThreePath> functionRules){
		String xml="";
		xml+="<Function>\n";
		xml+=XMLhandler.twoPath2XML(function);
		for (Iterator<ThreePath> iterator = functionRules.iterator(); iterator.hasNext();) {
			ThreePath rule = iterator.next();
			xml+="<Rule>\n";
			xml+=XMLhandler.threePath2XML(rule);
			xml+="</Rule>\n";
		}
		xml+="</Function>\n";
	return xml;
	}

	//constructs a xml description of a type with its constructors
	public static String makeTypeXML(OnePath type, ArrayList<TwoPath> constructors){
		String xml="";
		xml+="<Type>\n";
		xml+=XMLhandler.onePath2XML(type);
		for (Iterator<TwoPath> iterator = constructors.iterator(); iterator.hasNext();) {
			TwoPath constructor = iterator.next();
			xml+="<Constructor>\n";
			xml+=XMLhandler.twoPath2XML(constructor);
			xml+="</Constructor>\n";
		}
		xml+="</Type>\n";
		return xml;
	}

}