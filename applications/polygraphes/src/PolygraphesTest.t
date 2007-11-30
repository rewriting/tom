package polygraphestest;

import polygraphestest.*;
import polygraphestest.types.*;
import polygraphestest.types.twopath.*;
import tom.library.sl.*;
import java.io.*;

import java.util.HashSet;
import java.util.Iterator;

public class PolygraphesTest{

%include { polygraphestest/PolygraphesTest.tom }
%include { sl.tom }

private static HashSet<ThreePath> rewritingRules=new HashSet<ThreePath>();


//-----------------------------------------------------------------------------
// NAT
//-----------------------------------------------------------------------------
// 1-chemin representant les entiers naturels
private static	OnePath nat=`OneCell("nat");
// constructeurs sur les entiers naturels
private static	TwoPath zero=`TwoCell("zero",Id(),nat,Constructor());
private static	TwoPath succ =`TwoCell("succ",nat,nat,Constructor());
// 2-cellules de structure
private static	TwoPath eraser= `TwoCell("eraser",nat,Id(),Function());
private static	TwoPath duplication= `TwoCell("duplication",nat,OneC0(nat,nat),Function());
private static	TwoPath permutation = `TwoCell("permutation",OneC0(nat,nat),OneC0(nat,nat),Function());
// addition, soustraction et division
private static	TwoPath plus = `TwoCell("plus",OneC0(nat,nat),nat,Function());
private static	TwoPath minus = `TwoCell("minus",OneC0(nat,nat),nat,Function());
private static	TwoPath division = `TwoCell("division",OneC0(nat,nat),nat,Function());
private static	TwoPath multiplication = `TwoCell("multiplication",OneC0(nat,nat),nat,Function());
// 3-cellules de structure
private static	ThreePath zeroPerm1 = `ThreeCell("zeroPerm1",TwoC1(TwoC0(zero,TwoId(nat)),permutation),TwoC0(TwoId(nat),zero),Function());
private static	ThreePath zeroPerm2 = `ThreeCell("zeroPerm2",TwoC1(TwoC0(TwoId(nat),zero),permutation),TwoC0(zero,TwoId(nat)),Function());
private static	ThreePath zeroDup = `ThreeCell("zeroDup",TwoC1(zero,duplication),TwoC0(zero,zero),Function());
private static	ThreePath zeroEraz = `ThreeCell("zeroEraz",TwoC1(zero,eraser),TwoId(Id()),Function());
private static	ThreePath succPerm1 = `ThreeCell("succPerm1",TwoC1(TwoC0(succ,TwoId(nat)),permutation),TwoC1(permutation,TwoC0(TwoId(nat),succ)),Function());
private static	ThreePath succPerm2 = `ThreeCell("succPerm2",TwoC1(TwoC0(TwoId(nat),succ),permutation),TwoC1(permutation,TwoC0(succ,TwoId(nat))),Function());
private static	ThreePath succDup = `ThreeCell("succDup",TwoC1(succ,duplication),TwoC1(duplication,TwoC0(succ,succ)),Function());
private static	ThreePath succEraz = `ThreeCell("succEraz",TwoC1(succ,eraser),TwoC1(TwoId(nat),eraser),Function());
// regles
private static	ThreePath plusZero = `ThreeCell("plusZero",TwoC1(TwoC0(zero,TwoId(nat)),plus),TwoId(nat),Function());
private static	ThreePath plusSucc = `ThreeCell("plusSucc",TwoC1(TwoC0(succ,TwoId(nat)),plus),TwoC1(plus,succ),Function());
private static	ThreePath minusZero1 = `ThreeCell("minusZero1",TwoC1(TwoC0(zero,TwoId(nat)),minus),TwoC1(eraser,zero),Function());
private static	ThreePath minusZero2 = `ThreeCell("minusZero2",TwoC1(TwoC0(TwoId(nat),zero),minus),TwoId(nat),Function());
private static	ThreePath minusDoubleSucc = `ThreeCell("minusDoubleSucc",TwoC1(TwoC0(succ,succ),minus),minus,Function());
private static	ThreePath divZero = `ThreeCell("divZero",TwoC1(TwoC0(zero,TwoId(nat)),division),TwoC1(eraser,zero),Function());	
private static	ThreePath divSucc = `ThreeCell("divSucc",TwoC1(TwoC0(succ,TwoId(nat)),division),TwoC1(TwoC0(TwoId(nat),duplication),TwoC0(minus,TwoId(nat)),division,succ),Function());
private static	ThreePath multZero = `ThreeCell("multZero",TwoC1(TwoC0(zero,TwoId(nat)),multiplication),TwoC1(eraser,zero),Function());	
private static	ThreePath multSucc = `ThreeCell("multSucc",TwoC1(TwoC0(succ,TwoId(nat)),multiplication),TwoC1(TwoC0(TwoId(nat),duplication),TwoC0(multiplication,TwoId(nat)),plus),Function());
//-----------------------------------------------------------------------------
// LIST
//-----------------------------------------------------------------------------
// 1-chemin representant les entiers naturels
private static 	OnePath list=`OneCell("list");
// constructeurs sur les entiers naturels
private static	TwoPath consList=`TwoCell("consList",Id(),list,Constructor());
private static	TwoPath add =`TwoCell("add",OneC0(nat,list),list,Constructor());
private static  TwoPath append=`TwoCell("append",OneC0(list,nat),list,Constructor());
// 2-cellules de structure
private static	TwoPath eraserList= `TwoCell("eraserList",list,Id(),Function());
private static	TwoPath duplicationList= `TwoCell("duplicationList",list,OneC0(list,list),Function());
private static	TwoPath permutationList = `TwoCell("permutationList",OneC0(list,list),OneC0(list,list),Function());
private static	TwoPath permutationNL = `TwoCell("permutationNL",OneC0(nat,list),OneC0(list,nat),Function());
private static	TwoPath permutationLN = `TwoCell("permutationLN",OneC0(list,nat),OneC0(nat,list),Function());
// addition, soustraction et division
private static	TwoPath sort = `TwoCell("sort",list,list,Function());
private static	TwoPath split = `TwoCell("split",list,OneC0(list,list),Function());
private static	TwoPath merge = `TwoCell("merge",OneC0(list,list),list,Function());
//regles
private static ThreePath erazList = `ThreeCell("erazList",TwoC1(consList,eraserList),TwoId(Id()),Function());
private static ThreePath duppList = `ThreeCell ("duppList",TwoC1(consList,duplicationList),TwoC0(consList,consList),Function());
private static ThreePath permList1 = `ThreeCell ("permList1",TwoC1(TwoC0(TwoId(list),consList),permutationList),TwoC0(consList,TwoId(list)),Function());
private static ThreePath permList2 = `ThreeCell ("permList2",TwoC1(TwoC0(consList,TwoId(list)),permutationList),TwoC0(TwoId(list),consList),Function());
private static ThreePath permNLList = `ThreeCell ("permNLList",TwoC1(TwoC0(TwoId(nat),consList),permutationNL),TwoC0(consList,TwoId(nat)),Function());
private static ThreePath permLNList = `ThreeCell ("permLNList",TwoC1(TwoC0(consList,TwoId(nat)),permutationLN),TwoC0(TwoId(nat),consList),Function());
private static ThreePath permNLzero = `ThreeCell ("permNLzero",TwoC1(TwoC0(zero,TwoId(list)),permutationNL),TwoC0(TwoId(list),zero),Function());
private static ThreePath permLNzero = `ThreeCell ("permLNLzero",TwoC1(TwoC0(TwoId(list),zero),permutationLN),TwoC0(zero,TwoId(list)),Function());
private static ThreePath permNLsucc = `ThreeCell ("permNLsucc",TwoC1(TwoC0(succ,TwoId(list)),permutationNL),TwoC1(permutationNL,TwoC0(TwoId(list),succ)),Function());
private static ThreePath permLNsucc = `ThreeCell ("permLNLsucc",TwoC1(TwoC0(TwoId(list),succ),permutationLN),TwoC1(permutationLN,TwoC0(succ,TwoId(list))),Function());
//section append-----------------------
//private static ThreePath appendToAdd= `ThreeCell("appendToAdd",append,TwoC1(permutationLN,add),Function());
private static ThreePath appendToAdd= `ThreeCell("appendToAdd",TwoC1(TwoC0(consList,TwoId(nat)),append),TwoC1(TwoC0(TwoId(nat),consList),add),Function());
private static ThreePath addAppend = `ThreeCell("addAppend",TwoC1(TwoC0(TwoC1(TwoC0(TwoId(nat),TwoId(list)),add),TwoId(nat)),append),TwoC1(TwoC0(TwoId(nat),TwoC1(TwoC0(TwoId(list),TwoId(nat)),append)),add),Function());
private static ThreePath appendEraser =`ThreeCell("appendEraser",TwoC1(append,eraserList),TwoC0(eraserList,eraser),Function());
private static ThreePath appendDup = `ThreeCell("appendDup",TwoC1(append,duplicationList),TwoC1(TwoC0(duplicationList,duplication),TwoC0(TwoId(list),permutationLN,TwoId(nat)),TwoC0(append,append)),Function());
private static ThreePath permAppend1 = `ThreeCell ("permAppend1",TwoC1(TwoC0(TwoId(list),append),permutationList),TwoC1(TwoC0(permutationList,TwoId(nat)),TwoC0(TwoId(list),permutationLN),TwoC0(append,TwoId(list))),Function());
private static ThreePath permAppend2 = `ThreeCell ("permAppend2",TwoC1(TwoC0(append,TwoId(list)),permutationList),TwoC1(TwoC0(TwoId(list),permutationNL),TwoC0(permutationList,TwoId(nat)),TwoC0(TwoId(list),append)),Function());
private static ThreePath permNLAppend = `ThreeCell ("permNLAppend",TwoC1(TwoC0(TwoId(nat),append),permutationNL),TwoC1(TwoC0(permutationNL,TwoId(nat)),TwoC0(TwoId(list),permutation),TwoC0(append,TwoId(nat))),Function());
private static ThreePath permLNAppend = `ThreeCell ("permLNAppend",TwoC1(TwoC0(append,TwoId(nat)),permutationLN),TwoC1(TwoC0(TwoId(list),permutation),TwoC0(permutationLN,TwoId(nat)),TwoC0(TwoId(nat),append)),Function());
//-------------------------------------
private static ThreePath addEraser = `ThreeCell("addEraser",TwoC1(add,eraserList),TwoC0(eraser,eraserList),Function());
private static ThreePath addDup = `ThreeCell("addDup",TwoC1(add,duplicationList),TwoC1(TwoC0(duplication,duplicationList),TwoC0(TwoId(nat),permutationNL,TwoId(list)),TwoC0(add,add)),Function());
private static ThreePath permAdd1 = `ThreeCell ("permAdd1",TwoC1(TwoC0(TwoId(list),add),permutationList),TwoC1(TwoC0(permutationLN,TwoId(list)),TwoC0(TwoId(nat),permutationList),TwoC0(add,TwoId(list))),Function());
private static ThreePath permAdd2 = `ThreeCell ("permAdd2",TwoC1(TwoC0(add,TwoId(list)),permutationList),TwoC1(TwoC0(TwoId(nat),permutationList),TwoC0(permutationNL,TwoId(list)),TwoC0(TwoId(list),add)),Function());
private static ThreePath permNLAdd = `ThreeCell ("permNLAdd",TwoC1(TwoC0(TwoId(nat),add),permutationNL),TwoC1(TwoC0(permutation,TwoId(list)),TwoC0(TwoId(nat),permutationNL),TwoC0(add,TwoId(nat))),Function());
private static ThreePath permLNAdd = `ThreeCell ("permLNAdd",TwoC1(TwoC0(add,TwoId(nat)),permutationLN),TwoC1(TwoC0(TwoId(nat),permutationLN),TwoC0(permutation,TwoId(list)),TwoC0(TwoId(nat),add)),Function());
//regles liees aux fonctions
private static ThreePath consListSort = `ThreeCell("consListSort",TwoC1(consList,sort),consList,Function());
private static ThreePath addSort = `ThreeCell("addSort",TwoC1(TwoC0(TwoId(nat),consList),add,sort),TwoC1(TwoC0(TwoId(nat),consList),add),Function());
private static ThreePath doubleAddSort = `ThreeCell("doubleAddSort",TwoC1(TwoC0(TwoId(nat),TwoC1(TwoC0(TwoId(nat),TwoId(list)),add)),add,sort),TwoC1(TwoC0(TwoId(nat),TwoId(nat),split),TwoC0(TwoId(nat),permutationNL,TwoId(list)),TwoC0(add,add),TwoC0(sort,sort),merge),Function());
private static ThreePath consListSplit = `ThreeCell("consListSplit",TwoC1(consList,split),TwoC0(consList,consList),Function());
private static ThreePath addSplit = `ThreeCell("addList",TwoC1(TwoC0(TwoId(nat),consList),add,split),TwoC0(TwoC1(TwoC0(TwoId(nat),consList),add),consList),Function());
private static ThreePath doubleAddSplit = `ThreeCell("doubleAddSplit",TwoC1(TwoC0(TwoId(nat),TwoC1(TwoC0(TwoId(nat),TwoId(list)),add)),add,split),TwoC1(TwoC0(TwoId(nat),TwoId(nat),split),TwoC0(TwoId(nat),permutationNL,TwoId(list)),TwoC0(add,add)),Function());
private static ThreePath consListMerge1 = `ThreeCell("consListMerge1",TwoC1(TwoC0(consList,TwoId(list)),merge),TwoId(list),Function());
private static ThreePath consListMerge2 = `ThreeCell("consListMerge2",TwoC1(TwoC0(TwoId(list),consList),merge),TwoId(list),Function());
 

public static void main(String[] args) {
rewritingRules.add(zeroPerm1);
rewritingRules.add(zeroPerm2);
rewritingRules.add(zeroDup);
rewritingRules.add(zeroEraz);
rewritingRules.add(succPerm1);
rewritingRules.add(succPerm2);
rewritingRules.add(succDup);
rewritingRules.add(succEraz);
rewritingRules.add(plusZero);
rewritingRules.add(plusSucc);
rewritingRules.add(minusZero1);
rewritingRules.add(minusZero2);
rewritingRules.add(minusDoubleSucc);
rewritingRules.add(divZero);
rewritingRules.add(divSucc);
rewritingRules.add(multZero);
rewritingRules.add(multSucc);

rewritingRules.add(erazList);
rewritingRules.add(duppList);
rewritingRules.add(permList1);
rewritingRules.add(permList2);
rewritingRules.add(permNLList);
rewritingRules.add(permLNList);
rewritingRules.add(permNLzero);
rewritingRules.add(permLNzero);
rewritingRules.add(permNLsucc);
rewritingRules.add(permLNsucc);
rewritingRules.add(appendToAdd);
rewritingRules.add(addAppend);
rewritingRules.add(appendEraser);
rewritingRules.add(appendDup);
rewritingRules.add(permAppend1);
rewritingRules.add(permAppend2);
rewritingRules.add(permNLAppend);
rewritingRules.add(permLNAppend);
rewritingRules.add(addEraser);
rewritingRules.add(addDup);
rewritingRules.add(permAdd1);
rewritingRules.add(permAdd2);
rewritingRules.add(permNLAdd);
rewritingRules.add(permLNAdd);
rewritingRules.add(consListSort);
rewritingRules.add(addSort);
rewritingRules.add(doubleAddSort);
rewritingRules.add(consListSplit);
rewritingRules.add(addSplit);
rewritingRules.add(doubleAddSplit);
rewritingRules.add(consListMerge1);
rewritingRules.add(consListMerge2);




String testrules="<PolygraphicProgram Name=\"TestProgram\">\n";
for (Iterator iterator = rewritingRules.iterator(); iterator.hasNext();) {
	ThreePath rule = (ThreePath) iterator.next();
	testrules+=threePath2XML(rule);
}


testrules+="</PolygraphicProgram>";
try{
save(testrules,new File("/Users/aurelien/polygraphWorkspace/PolygraphesApp/polygraphes/src/testprogram.xml"));
}catch(Exception e){e.printStackTrace();}

TwoPath un=`TwoC1(zero,succ);
TwoPath deux=`TwoC1(un,succ);
TwoPath trois=`TwoC1(deux,succ);
TwoPath quatre=`TwoC1(trois,succ);
TwoPath cinq=`TwoC1(quatre,succ);
TwoPath six=`TwoC1(cinq,succ);
TwoPath sept=`TwoC1(six,succ);
TwoPath huit=`TwoC1(sept,succ);
TwoPath neuf=`TwoC1(huit,succ);
TwoPath dix=`TwoC1(neuf,succ);



TwoPath rule=`TwoC1(TwoC0(zero,zero),TwoC0(succ,succ),permutation,minus,eraser);
TwoPath rule2=`TwoC1(zero,TwoC0(succ,zero),division);
TwoPath rule3=`TwoC1(TwoC0(TwoC1(zero,succ,succ,succ),TwoC1(zero,succ,succ)),multiplication);
TwoPath rule4=`TwoC1(zero,succ,succ,TwoC0(succ,zero),TwoC0(succ,succ),TwoC0(succ,succ),division);
TwoPath rule5=`TwoC1(TwoC0(TwoC1(zero,succ,succ,succ),TwoC1(zero)),division);
TwoPath add=`TwoC1(TwoC0(TwoC1(zero,succ,succ,succ,succ,succ),TwoC1(zero,succ,succ,succ)),plus);
TwoPath div=`TwoC1(TwoC0(add,TwoC1(zero,succ,succ,succ)),division);
TwoPath total=`TwoC1(TwoC0(div,TwoC1(zero,succ,succ,succ,succ)),multiplication);
TwoPath nine=`TwoC1(TwoC0(TwoC1(TwoC0(six,six),multiplication),trois),minus);
//test(nine);
//System.out.println(rule3);
}
public static void save(String fileContent,File file) throws IOException {

		PrintWriter printWriter = new PrintWriter(new FileOutputStream(
				file));
		printWriter.print(fileContent);
		printWriter.flush();
		printWriter.close();
	}
public static String twoPath2XML(TwoPath path){
%match (TwoPath path){
			TwoId(onepath) -> {return "<TwoPath>\n<TwoId>\n"+onePath2XML(`onepath)+"</TwoId>\n</TwoPath>\n";}
			TwoCell(name,source,target,type) -> { return "<TwoPath>\n<TwoCell Name=\""+`name+"\" Type=\""+`type.toString().replace("()","")+"\">\n<Source>\n"+onePath2XML(`source)+"</Source>\n<Target>\n"+onePath2XML(`target)+"</Target>\n</TwoCell>\n</TwoPath>\n"; }
			TwoC0(head,tail*) -> {return "<TwoPath>\n<TwoC0>\n"+twoC02XML(`head)+twoC02XML(`tail)+"</TwoC0>\n</TwoPath>\n";}
			TwoC1(head,tail*) -> {return "<TwoPath>\n<TwoC1>\n"+twoC12XML(`head)+twoC12XML(`tail)+"</TwoC1>\n</TwoPath>\n";}
}
return "";
}
public static String twoC02XML(TwoPath path){
%match (TwoPath path){
			TwoId(onepath) -> {return "<TwoId>\n"+onePath2XML(`onepath)+"</TwoId>\n";}
			TwoCell(name,source,target,type) -> { return "<TwoCell Name=\""+`name+"\" Type=\""+`type.toString().replace("()","")+"\">\n<Source>\n"+onePath2XML(`source)+"</Source>\n<Target>\n"+onePath2XML(`target)+"</Target>\n</TwoCell>\n"; }
			TwoC0(head,tail*) -> {return twoC02XML(`head)+twoC02XML(`tail);}
			TwoC1(head,tail*) -> {return "<TwoC1>\n"+twoC12XML(`head)+twoC12XML(`tail)+"</TwoC1>\n";}
}
return "";
}
public static String twoC12XML(TwoPath path){
%match (TwoPath path){
			TwoId(onepath) -> {return "<TwoId>\n"+onePath2XML(`onepath)+"</TwoId>\n";}
			TwoCell(name,source,target,type) -> { return "<TwoCell Name=\""+`name+"\" Type=\""+`type.toString().replace("()","")+"\">\n<Source>\n"+onePath2XML(`source)+"</Source>\n<Target>\n"+onePath2XML(`target)+"</Target>\n</TwoCell>\n"; }
			TwoC0(head,tail*) -> {return "<TwoC0>\n"+twoC02XML(`head)+twoC02XML(`tail)+"</TwoC0>\n";}
			TwoC1(head,tail*) -> {return twoC12XML(`head)+twoC12XML(`tail);}
}
return "";
}
public static String onePath2XML(OnePath path){
%match (OnePath path){
			Id() -> {return "<OnePath>\n<Id></Id>\n</OnePath>\n";}
			OneCell(name) -> { return "<OnePath>\n<OneCell Name=\""+`name+"\"></OneCell>\n</OnePath>\n"; }
			OneC0(head,tail*)->{ return "<OnePath>\n<OneC0>\n"+oneC02XML(`head)+oneC02XML(`tail)+"</OneC0>\n</OnePath>\n";}
}
return "";
}
public static String oneC02XML(OnePath path){
%match (OnePath path){
			Id() -> {return "<Id></Id>\n";}
			OneCell(name) -> { return "<OneCell Name=\""+`name+"\"></OneCell>\n"; }
			OneC0(head,tail*)->{ return oneC02XML(`head)+oneC02XML(`tail);}
}
return "";
}

public static String threePath2XML(ThreePath path){
if(path instanceof ThreePath){
return "<ThreeCell Name=\""+path.getName()+"\" Type=\""+path.getType().toString().replace("()","")+"\">\n<Source>\n"+twoPath2XML(path.getSource())+"</Source>\n<Target>\n"+twoPath2XML(path.getTarget())+"</Target>\n</ThreeCell>\n";
}
else {System.out.println("this is not a ThreeCell !"+path);return null;} 
}

}