package tools;

import polygraphicprogram.types.*;
import org.w3c.dom.*;
import javax.xml.parsers.*;
import tools.XMLhandler;

//print an understandable output provided the original output is composed with nat, list or booleans and that they are only composed with constructors

public class ReadOutput{

	%include { polygraphicprogram/PolygraphicProgram.tom }
	%include { sl.tom }
	%include{ dom.tom }

	public static String path="/Users/aurelien/polygraphWorkspace/PolygraphesApp/polygraphes/polygraphesWithID/";

	
	public static void main(String[] args) {
		System.out.println("RESULT:\n----------------------------");
		System.out.println(result(path+"XMLoutput.xml"));
		System.out.println("----------------------------");
	}

	//called in main, return the result
	public static String result(String filename){
		TwoPath resultpath=`TwoId(Id());
		try {
			Document dom = DocumentBuilderFactory.newInstance().newDocumentBuilder().parse(filename);
			Element e = dom.getDocumentElement();
			//first we transform the xml into a 2-path and we try to analyze it with other functions, first assuming this would be a list
			resultpath=XMLhandler.makeTwoPath(e);
			return resultList(resultpath);
		}catch(Exception e){e.printStackTrace();}
		resultpath.print();
		return "Error-->non implemented yet";
	}

	//analyze the path assuming it is a list, otherwise, it sends the path to other function to analyze boolean or nat
	public static String resultList(TwoPath resultat){
		%match (TwoPath resultat){
			TwoId(Id()) -> { return "0"; }
			TwoC1(TwoC0(TwoCell("consList",_,_,Constructor(),_)),TwoCell("add",_,_,_,_),NAT*) -> {return ""+resultNat(`NAT);}
			TwoC1(TwoC0(NAT*,LIST*),TwoCell("add",_,_,_,_)) -> {if(`NAT!=`TwoId(Id())){return resultNat(`NAT)+" "+resultList(`LIST);}}
			TwoC0(LeftSplit*,RightSplit*) -> {if (`LeftSplit!=`TwoId(Id())){return resultList(`LeftSplit)+ "|| " + resultList(`RightSplit);}}
			TwoCell("consList",_,_,Constructor(),_) -> {return "";}
			}
		if(resultat.target()==`OneCell("nat")){return " "+resultNat(resultat);}//if the target is a natural number
		if(resultat.target()==`OneCell("boolean")){return " "+resultBool(resultat);}//if the target is a boolean
		System.out.println("RECOGNITION ERROR");resultat.print();
		return "Error";
	}
	
	// use to try to analyse nat results
	public static int resultNat(TwoPath resultat){
		%match (TwoPath resultat){
			TwoId(Id()) -> { return 0; }
			TwoCell("zero",_,_,_,_) -> {return 0;}
			TwoCell("succ",_,_,_,_) -> {return 1;}
			TwoC1(TwoCell("zero",_,_,_,_),X*) -> {return resultNat(`X*);}
			TwoC1(TwoCell("succ",_,_,_,_),X*) -> {return 1+resultNat(`X*);}
			TwoC1(TwoCell("succ",_,_,_,_),TwoCell("succ",_,_,_,_)) -> {return 2; }
			}
		System.out.println("RECOGNITION ERROR NAT");resultat.print();
		return 0;
	}

	// use to try to analyse boolean results
	public static String resultBool(TwoPath resultat){
		%match (TwoPath resultat){
			TwoCell(name,_,OneCell("boolean"),Constructor(),_) -> {return `name;}
		}
		System.out.println("RECOGNITION ERROR BOOL");resultat.print();
		return "Error";
	}

}