package tools;

import polygraphicprogram.*;
import polygraphicprogram.types.*;
import java.io.*;
import javax.xml.parsers.*;
import java.util.regex.*;
import compiler.XMLhandler;
import tools.TestCellSet;

public class MakeInput{

	%include { polygraphicprogram/PolygraphicProgram.tom }
	
	//main : creates a xml file ("input.xml") describing a 2-Path 
	public static void main(String[] args) {
		String inputPath="";
		if(args.length!=1){
			System.out.println("you must indicate the desired path of the xml input");
			}
		else{
			if(args[0].substring(args[0].length()-1, args[0].length()).equals("/")){
				inputPath=args[0];
				TwoPath testNat=`TwoC1(TwoC0(TwoC1(TwoC0(makeNat(2),makeNat(6)),TestCellSet.multiplication),makeNat(3)),TestCellSet.division);
				TwoPath testFibo=`TwoC1(makeNat(10),TestCellSet.fibonacci);
				int[] list1={7,8,3,2,6};
				int[] list2={9,5,7,4,1};
				TwoPath testEqualList = `TwoC1(TwoC0(TwoC1(makeList(list1),TestCellSet.sort),TwoC1(makeList(list2),TestCellSet.sort)),TestCellSet.lEqual);
				TwoPath testsortcomplex= `TwoC1(TwoC0(makeList(list1),makeList(list2)),TestCellSet.merge,TestCellSet.sort);
				//defines the input
				String input=XMLhandler.twoPath2XML(testFibo);
				//saves it
				if(!input.equals("")){
					try{
						save(input,new File(inputPath));
					}catch(Exception e){e.printStackTrace();}
				}
			}
			else{
			System.out.println("the first and only argument must be an xml file i.e. with the extension .xml");}
		}
	}

	//save string in a file
	public static void save(String fileContent,File file) throws IOException {

		PrintWriter printWriter = new PrintWriter(new FileOutputStream(
				file));
		printWriter.print(fileContent);
		printWriter.flush();
		printWriter.close();
	}

	//make a polygraphic list from an array of int
	public static TwoPath makeList(int[] array){
		TwoPath list=`TwoC1(TwoC0(makeNat(array[array.length-1]),TwoCell("consList",Id(),OneCell("list"),Constructor(),0)),TestCellSet.add);
		for (int i = array.length-2; i>=0 ;i--) {
			list=`TwoC1(TwoC0(makeNat(array[i]),list),TestCellSet.add);
		}
		return list;
	}

	//make a polygraphic nat from an int
	public static TwoPath makeNat(int nat){
		TwoPath natPath=TestCellSet.zero;
		for (int i = nat; i >0; i--) {
			natPath=`TwoC1(natPath,TestCellSet.succ);
		}
		return natPath;
	}

	//aborted work supposed to make polygraphes from a keyboard input : 
/*
	 public static int inputInt() {
	      BufferedReader input = new BufferedReader(new InputStreamReader(System.in));
	      int i =0; 
	      try { 
	          i = Integer.parseInt(input.readLine());
	      } 
	      catch (Exception e) { 
	          e.printStackTrace(); 
	      } 
	      return i; 
	    }
	
	
	 public static String inputString() {
	      BufferedReader input = new BufferedReader(new InputStreamReader(System.in));
	      String string="";
	      try { 
	          string = input.readLine();
	      } 
	      catch (Exception e) { 
	          e.printStackTrace(); 
	      } 
	      return string; 
	    }

	 public static String menu(){
		 String input="";
		 System.out.println("-----------------------\nchoose your input mode : \n1\tNat only\n2\tList only\n3\tBoolean only\n4\tCustom\n5\tExit\n-----------------------");
		 int choix=inputInt();
		 if(choix==1){System.out.println("*****Nat Only Mode*****\nEnter a 2-Path : \n");input=parseNat(inputString());}
		 else if(choix==2){System.out.println("*****List Only Mode*****\nEnter a 2-Path : \n");input=parseList(inputString());}
		 else if(choix==3){System.out.println("*****Boolean Only Mode*****\nEnter a 2-Path : \n");input=parseBoolean(inputString());}
		 else if(choix==4){System.out.println("*****Custom Mode*****\nEnter a 2-Path : \n");input=inputString();}
		 else if(choix==5){}
		 else{System.out.println("unvalid choice !!\nTRY AGAIN");menu();}
		 return input;
	 	}

	public static String parseNat(String userInput){
		TwoPath path=`TwoId(Id());
		//todo
		return twoPath2XML(path);
	}

	public static String parseList(String userInput){
		TwoPath path=`TwoId(Id());
		//todo
		return twoPath2XML(path);
	}

	public static String parseBoolean(String userInput){
		TwoPath path=`TwoId(Id());
		//todo
		return twoPath2XML(path);
	}

	public static String splitNat(String userInput){//aborted
		try{Pattern splitOperator=Pattern.compile("^\\W");
		String[] split=splitOperator.split(userInput);
		String operator=split[0];
		//Pattern leftOperand=Pattern.compile("^\\W\(");
		Pattern rightOperand;
		//then test if there is a right operand
		
		}
		catch(Exception e){System.out.println("error : invalide expression");}
		return null;
		}
*/


}