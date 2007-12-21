

import polygraphicprogram.types.*;

import java.io.*;
import org.w3c.dom.*;
import java.util.Vector;
import java.util.Iterator;

public class MakeQBFProgram{

%include { polygraphicprogram/PolygraphicProgram.tom }
%include { sl.tom }
%include{ dom.tom }

private static Vector<ThreePath> rewritingRules=new Vector<ThreePath>();

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
// 3-cellules de structure
private static	ThreePath zeroPerm1 = `ThreeCell("zeroPerm1",TwoC1(TwoC0(zero,TwoId(nat)),permutation),TwoC0(TwoId(nat),zero),Function());
private static	ThreePath zeroPerm2 = `ThreeCell("zeroPerm2",TwoC1(TwoC0(TwoId(nat),zero),permutation),TwoC0(zero,TwoId(nat)),Function());
private static	ThreePath zeroDup = `ThreeCell("zeroDup",TwoC1(zero,duplication),TwoC0(zero,zero),Function());
private static	ThreePath zeroEraz = `ThreeCell("zeroEraz",TwoC1(zero,eraser),TwoId(Id()),Function());
private static	ThreePath succPerm1 = `ThreeCell("succPerm1",TwoC1(TwoC0(succ,TwoId(nat)),permutation),TwoC1(permutation,TwoC0(TwoId(nat),succ)),Function());
private static	ThreePath succPerm2 = `ThreeCell("succPerm2",TwoC1(TwoC0(TwoId(nat),succ),permutation),TwoC1(permutation,TwoC0(succ,TwoId(nat))),Function());
private static	ThreePath succDup = `ThreeCell("succDup",TwoC1(succ,duplication),TwoC1(duplication,TwoC0(succ,succ)),Function());
private static	ThreePath succEraz = `ThreeCell("succEraz",TwoC1(succ,eraser),TwoC1(TwoId(nat),eraser),Function());
//-----------------------------------------------------------------------------
// LISTS
//-----------------------------------------------------------------------------
// 1-chemin representant les entiers naturels
private static 	OnePath list=`OneCell("list");
// constructeurs sur les entiers naturels
private static	TwoPath consList=`TwoCell("consList",Id(),list,Constructor());
private static	TwoPath add =`TwoCell("add",OneC0(nat,list),list,Constructor());
// 2-cellules de structure
private static	TwoPath eraserList= `TwoCell("eraserList",list,Id(),Function());
private static	TwoPath duplicationList= `TwoCell("duplicationList",list,OneC0(list,list),Function());
private static	TwoPath permutationList = `TwoCell("permutationList",OneC0(list,list),OneC0(list,list),Function());
private static	TwoPath permutationNL = `TwoCell("permutationNL",OneC0(nat,list),OneC0(list,nat),Function());
private static	TwoPath permutationLN = `TwoCell("permutationLN",OneC0(list,nat),OneC0(nat,list),Function());
//regles
private static ThreePath erazList = `ThreeCell("erazList",TwoC1(consList,eraserList),TwoId(Id()),Function());
private static ThreePath duppList = `ThreeCell ("duppList",TwoC1(consList,duplicationList),TwoC0(consList,consList),Function());
private static ThreePath permList1 = `ThreeCell ("permList1",TwoC1(TwoC0(TwoId(list),consList),permutationList),TwoC0(consList,TwoId(list)),Function());
private static ThreePath permList2 = `ThreeCell ("permList2",TwoC1(TwoC0(consList,TwoId(list)),permutationList),TwoC0(TwoId(list),consList),Function());
private static ThreePath permNLList = `ThreeCell ("permNLList",TwoC1(TwoC0(TwoId(nat),consList),permutationNL),TwoC0(consList,TwoId(nat)),Function());
private static ThreePath permLNList = `ThreeCell ("permLNList",TwoC1(TwoC0(consList,TwoId(nat)),permutationLN),TwoC0(TwoId(nat),consList),Function());
private static ThreePath permNLzero = `ThreeCell ("permNLzero",TwoC1(TwoC0(zero,TwoId(list)),permutationNL),TwoC0(TwoId(list),zero),Function());
private static ThreePath permLNzero = `ThreeCell ("permLNzero",TwoC1(TwoC0(TwoId(list),zero),permutationLN),TwoC0(zero,TwoId(list)),Function());
private static ThreePath permNLsucc = `ThreeCell ("permNLsucc",TwoC1(TwoC0(succ,TwoId(list)),permutationNL),TwoC1(permutationNL,TwoC0(TwoId(list),succ)),Function());
private static ThreePath permLNsucc = `ThreeCell ("permLNsucc",TwoC1(TwoC0(TwoId(list),succ),permutationLN),TwoC1(permutationLN,TwoC0(succ,TwoId(list))),Function());

private static ThreePath addEraser = `ThreeCell("addEraser",TwoC1(add,eraserList),TwoC0(eraser,eraserList),Function());
private static ThreePath addDup = `ThreeCell("addDup",TwoC1(add,duplicationList),TwoC1(TwoC0(duplication,duplicationList),TwoC0(TwoId(nat),permutationNL,TwoId(list)),TwoC0(add,add)),Function());
private static ThreePath permAdd1 = `ThreeCell ("permAdd1",TwoC1(TwoC0(TwoId(list),add),permutationList),TwoC1(TwoC0(permutationLN,TwoId(list)),TwoC0(TwoId(nat),permutationList),TwoC0(add,TwoId(list))),Function());
private static ThreePath permAdd2 = `ThreeCell ("permAdd2",TwoC1(TwoC0(add,TwoId(list)),permutationList),TwoC1(TwoC0(TwoId(nat),permutationList),TwoC0(permutationNL,TwoId(list)),TwoC0(TwoId(list),add)),Function());
private static ThreePath permNLAdd = `ThreeCell ("permNLAdd",TwoC1(TwoC0(TwoId(nat),add),permutationNL),TwoC1(TwoC0(permutation,TwoId(list)),TwoC0(TwoId(nat),permutationNL),TwoC0(add,TwoId(nat))),Function());
private static ThreePath permLNAdd = `ThreeCell ("permLNAdd",TwoC1(TwoC0(add,TwoId(nat)),permutationLN),TwoC1(TwoC0(TwoId(nat),permutationLN),TwoC0(permutation,TwoId(list)),TwoC0(TwoId(nat),add)),Function());

//-----------------------------------------------------------------------------
// BOOLEANS
//-----------------------------------------------------------------------------
// 1-chemin representant les entiers naturels
private static 	OnePath bool=`OneCell("boolean");
// constructeurs sur les entiers naturels
private static	TwoPath vrai=`TwoCell("true",Id(),bool,Constructor());
private static	TwoPath faux =`TwoCell("false",Id(),bool,Constructor());
// 2-cellules de structure
private static	TwoPath eraserBool= `TwoCell("eraserBool",bool,Id(),Function());
private static	TwoPath duplicationBool= `TwoCell("duplicationBool",bool,OneC0(bool,bool),Function());
private static	TwoPath permutationBool = `TwoCell("permutationBool",OneC0(bool,bool),OneC0(bool,bool),Function());
//Et il faudrait mettre toutes les permutations possibles ici
// addition, soustraction et division
private static	TwoPath not = `TwoCell("not",bool,bool,Function());
private static	TwoPath or = `TwoCell("or",OneC0(bool,bool),bool,Function());
//regles
private static 	ThreePath trueEraz= `ThreeCell("trueEraz",TwoC1(vrai,eraserBool),TwoId(Id()),Function());
private static 	ThreePath falseEraz= `ThreeCell("falseEraz",TwoC1(faux,eraserBool),TwoId(Id()),Function());
private static 	ThreePath falseDup= `ThreeCell("falseDup",TwoC1(faux,duplicationBool),TwoC0(faux,faux),Function());
private static 	ThreePath trueDup= `ThreeCell("trueDup",TwoC1(vrai,duplicationBool),TwoC0(vrai,vrai),Function());
private static 	ThreePath truePerm1= `ThreeCell("truePerm1",TwoC1(TwoC0(vrai,TwoId(bool)),permutationBool),TwoC0(TwoId(bool),vrai),Function());
private static 	ThreePath truePerm2= `ThreeCell("truePerm2",TwoC1(TwoC0(TwoId(bool),vrai),permutationBool),TwoC0(vrai,TwoId(bool)),Function());
private static 	ThreePath falsePerm1= `ThreeCell("falsePerm1",TwoC1(TwoC0(faux,TwoId(bool)),permutationBool),TwoC0(TwoId(bool),faux),Function());
private static 	ThreePath falsePerm2= `ThreeCell("falsePerm2",TwoC1(TwoC0(TwoId(bool),faux),permutationBool),TwoC0(faux,TwoId(bool)),Function());
private static 	ThreePath trueOrTrue= `ThreeCell("trueOrTrue",TwoC1(TwoC0(vrai,vrai),or),vrai,Function());
private static 	ThreePath trueOrFalse= `ThreeCell("trueOrFalse",TwoC1(TwoC0(vrai,faux),or),vrai,Function());
private static 	ThreePath FalseOrTrue= `ThreeCell("FalseOrTrue",TwoC1(TwoC0(faux,vrai),or),vrai,Function());
private static 	ThreePath FalseOrFalse= `ThreeCell("FalseOrFalse",TwoC1(TwoC0(faux,faux),or),faux,Function());
private static 	ThreePath notTrue= `ThreeCell("notTrue",TwoC1(vrai,not),faux,Function());
private static 	ThreePath notFalse= `ThreeCell("notFalse",TwoC1(faux,not),vrai,Function());
//egalité nat
private static TwoPath equal=`TwoCell("equal",OneC0(nat,nat),bool,Function());
private static ThreePath zeroEqualZero = `ThreeCell("zeroEqualZero",TwoC1(TwoC0(zero,zero),equal),vrai,Function());
private static ThreePath succEqualSucc = `ThreeCell("succEqualSucc",TwoC1(TwoC0(succ,succ),equal),equal,Function());
private static ThreePath zeroEqualSucc = `ThreeCell("zeroEqualSucc",TwoC1(TwoC0(zero,succ),equal),TwoC1(eraser,faux),Function());
private static ThreePath succEqualZero = `ThreeCell("succEqualZero",TwoC1(TwoC0(succ,zero),equal),TwoC1(eraser,faux),Function());
//permutation bool et list
private static TwoPath permutationBL= `TwoCell("permutationBL",OneC0(bool,list),OneC0(list,bool),Function());
private static TwoPath permutationLB= `TwoCell("permutationBL",OneC0(list,bool),OneC0(bool,list),Function());
private static ThreePath permBLList = `ThreeCell ("permBLList",TwoC1(TwoC0(TwoId(bool),consList),permutationBL),TwoC0(consList,TwoId(bool)),Function());
private static ThreePath permLBList = `ThreeCell ("permLBList",TwoC1(TwoC0(consList,TwoId(bool)),permutationLB),TwoC0(TwoId(bool),consList),Function());
private static ThreePath permBLtrue = `ThreeCell ("permBLtrue",TwoC1(TwoC0(vrai,TwoId(list)),permutationBL),TwoC0(TwoId(list),vrai),Function());
private static ThreePath permLBtrue = `ThreeCell ("permLBtrue",TwoC1(TwoC0(TwoId(list),vrai),permutationLB),TwoC0(vrai,TwoId(list)),Function());
private static ThreePath permBLfalse = `ThreeCell ("permBLfalse",TwoC1(TwoC0(faux,TwoId(list)),permutationBL),TwoC0(TwoId(list),faux),Function());
private static ThreePath permLBfalse = `ThreeCell ("permLBfalse",TwoC1(TwoC0(TwoId(list),faux),permutationLB),TwoC0(faux,TwoId(list)),Function());
//permutation nat et bool
private static TwoPath permutationBN= `TwoCell("permutationBN",OneC0(bool,nat),OneC0(nat,bool),Function());
private static TwoPath permutationNB= `TwoCell("permutationNB",OneC0(nat,bool),OneC0(bool,nat),Function());
private static ThreePath permBNtrue = `ThreeCell ("permBNtrue",TwoC1(TwoC0(vrai,TwoId(nat)),permutationBN),TwoC0(TwoId(nat),vrai),Function());
private static ThreePath permNBtrue = `ThreeCell ("permNBtrue",TwoC1(TwoC0(TwoId(nat),vrai),permutationNB),TwoC0(vrai,TwoId(nat)),Function());
private static ThreePath permBNfalse = `ThreeCell ("permBNfalse",TwoC1(TwoC0(faux,TwoId(nat)),permutationBN),TwoC0(TwoId(nat),faux),Function());
private static ThreePath permNBfalse = `ThreeCell ("permNBfalse",TwoC1(TwoC0(TwoId(nat),faux),permutationNB),TwoC0(faux,TwoId(nat)),Function());
private static ThreePath permNBzero = `ThreeCell ("permNBzero",TwoC1(TwoC0(zero,TwoId(bool)),permutationNB),TwoC0(TwoId(bool),zero),Function());
private static ThreePath permBNzero = `ThreeCell ("permBNzero",TwoC1(TwoC0(TwoId(bool),zero),permutationBN),TwoC0(zero,TwoId(bool)),Function());
private static ThreePath permNBsucc = `ThreeCell ("permNBsucc",TwoC1(TwoC0(succ,TwoId(bool)),permutationNB),TwoC1(permutationNB,TwoC0(TwoId(bool),succ)),Function());
private static ThreePath permBNsucc = `ThreeCell ("permBNsucc",TwoC1(TwoC0(TwoId(bool),succ),permutationBN),TwoC1(permutationBN,TwoC0(succ,TwoId(bool))),Function());
//qbf
private static TwoPath var=`TwoCell("var",nat,bool,Constructor());
private static TwoPath exists=`TwoCell("exists",OneC0(nat,bool),bool,Constructor());
private static TwoPath isIn=`TwoCell("isIn",OneC0(nat,list),bool,Function());
private static TwoPath verify=`TwoCell("verify",OneC0(bool,list),bool,Function());
private static TwoPath isInTest=`TwoCell("isInTest",OneC0(bool,nat,list),bool,Function());
//isIn
private static ThreePath consListIsIn = `ThreeCell("consListIsIn",TwoC1(TwoC0(TwoId(nat),consList),isIn),TwoC1(eraser,faux),Function());
private static ThreePath listIsIn = `ThreeCell("listIsIn",TwoC1(TwoC0(TwoId(nat),add),isIn),TwoC1(TwoC0(TwoC1(TwoC0(duplication,TwoId(nat)),TwoC0(TwoId(nat),permutation),TwoC0(equal,TwoId(nat))),TwoId(list)),isInTest),Function());
private static ThreePath trueIsInTest = `ThreeCell("trueIsInTest",TwoC1(TwoC0(vrai,TwoId(nat),TwoId(list)),isInTest),TwoC1(TwoC0(eraser,eraserList),vrai),Function());
private static ThreePath falseIsInTest = `ThreeCell("falseIsInTest",TwoC1(TwoC0(faux,TwoId(nat),TwoId(list)),isInTest),isIn,Function());
//verify
private static ThreePath varVerify = `ThreeCell("varVerify",TwoC1(TwoC0(var,TwoId(list)),verify),isIn,Function());
private static ThreePath notVerify = `ThreeCell("notVerify",TwoC1(TwoC0(not,TwoId(list)),verify),TwoC1(verify,not),Function());
private static ThreePath orVerify = `ThreeCell("orVerify",TwoC1(TwoC0(or,TwoId(list)),verify),TwoC1(TwoC0(TwoId(bool),TwoId(bool),duplicationList),TwoC0(TwoId(bool),permutationBL,TwoId(list)),TwoC0(verify,verify),or),Function());
private static ThreePath existsVerify = `ThreeCell("existsVerify",TwoC1(TwoC0(exists,TwoId(list)),verify),TwoC1(TwoC0(TwoId(nat),duplicationBool,duplicationList),TwoC0(permutationNB,permutationBL,TwoId(list)),TwoC0(TwoId(bool),permutationNL,TwoId(bool),TwoId(list)),TwoC0(TwoId(bool),TwoId(list),permutationNB,TwoId(list)),TwoC0(TwoId(bool),TwoId(list),TwoId(bool),add),TwoC0(verify,verify),or),Function());
private static ThreePath trueVerify = `ThreeCell("trueVerify",TwoC1(TwoC0(vrai,TwoId(list)),verify),TwoC1(eraserList,vrai),Function());
private static ThreePath falseVerify = `ThreeCell("falseVerify",TwoC1(TwoC0(faux,TwoId(list)),verify),TwoC1(eraserList,faux),Function());
//duplication et permutation constructeurs formules
private static ThreePath varDup = `ThreeCell("varDup",TwoC1(var,duplicationBool),TwoC1(duplication,TwoC0(var,var)),Function());
private static ThreePath existDup = `ThreeCell("existDup",TwoC1(exists,duplicationBool),TwoC1(TwoC0(duplication,duplicationBool),TwoC0(TwoId(nat),permutationNB,TwoId(bool)),TwoC0(exists,exists)),Function());
private static ThreePath permBLvar = `ThreeCell ("permBLvar",TwoC1(TwoC0(var,TwoId(list)),permutationBL),TwoC1(permutationNL,TwoC0(TwoId(list),var)),Function());
private static ThreePath permLBvar = `ThreeCell ("permLBvar",TwoC1(TwoC0(TwoId(list),var),permutationLB),TwoC1(permutationLN,TwoC0(var,TwoId(list))),Function());
private static ThreePath permBLexists = `ThreeCell ("permBLexists",TwoC1(TwoC0(exists,TwoId(list)),permutationBL),TwoC1(TwoC0(TwoId(nat),permutationBL),TwoC0(permutationNL,TwoId(bool)),TwoC0(TwoId(list),exists)),Function());
private static ThreePath permLBexists = `ThreeCell ("permLBexists",TwoC1(TwoC0(TwoId(list),exists),permutationLB),TwoC1(TwoC0(permutationLN,TwoId(bool)),TwoC0(TwoId(nat),permutationLB),TwoC0(exists,TwoId(list))),Function());
private static ThreePath permBNvar = `ThreeCell ("permBNvar",TwoC1(TwoC0(var,TwoId(nat)),permutationBN),TwoC1(permutation,TwoC0(TwoId(nat),var)),Function());
private static ThreePath permNBvar = `ThreeCell ("permNBvar",TwoC1(TwoC0(TwoId(nat),var),permutationNB),TwoC1(permutation,TwoC0(var,TwoId(nat))),Function());
private static ThreePath permBNexists = `ThreeCell ("permBNexists",TwoC1(TwoC0(exists,TwoId(nat)),permutationBN),TwoC1(TwoC0(TwoId(nat),permutationBN),TwoC0(permutation,TwoId(bool)),TwoC0(TwoId(nat),exists)),Function());
private static ThreePath permNBexists = `ThreeCell ("permNBexists",TwoC1(TwoC0(TwoId(nat),exists),permutationNB),TwoC1(TwoC0(permutation,TwoId(bool)),TwoC0(TwoId(nat),permutationNB),TwoC0(exists,TwoId(nat))),Function());

public static void main(String[] args) {

rewritingRules.add(zeroPerm1);
rewritingRules.add(zeroPerm2);
rewritingRules.add(zeroDup);
rewritingRules.add(zeroEraz);
rewritingRules.add(succPerm1);
rewritingRules.add(succPerm2);
rewritingRules.add(succDup);
rewritingRules.add(succEraz);


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

rewritingRules.add(addEraser);
rewritingRules.add(addDup);
rewritingRules.add(permAdd1);
rewritingRules.add(permAdd2);
rewritingRules.add(permNLAdd);
rewritingRules.add(permLNAdd);

rewritingRules.add(permBLList);
rewritingRules.add(permLBList);
rewritingRules.add(permBLtrue);
rewritingRules.add(permLBtrue);
rewritingRules.add(permBLfalse);
rewritingRules.add(permLBfalse);
rewritingRules.add(permNBzero);
rewritingRules.add(permBNzero);
rewritingRules.add(permNBsucc);
rewritingRules.add(permBNsucc);
rewritingRules.add(permBNtrue);
rewritingRules.add(permNBtrue);
rewritingRules.add(permBNfalse);
rewritingRules.add(permNBfalse);

rewritingRules.add(trueEraz);
rewritingRules.add(falseEraz);
rewritingRules.add(falseDup);
rewritingRules.add(zeroEraz);
rewritingRules.add(trueDup);
rewritingRules.add(truePerm1);
rewritingRules.add(truePerm2);
rewritingRules.add(falsePerm1);
rewritingRules.add(falsePerm2);
rewritingRules.add(trueOrTrue);
rewritingRules.add(trueOrFalse);
rewritingRules.add(FalseOrTrue);
rewritingRules.add(FalseOrFalse);
rewritingRules.add(notTrue);
rewritingRules.add(notFalse);


rewritingRules.add(zeroEqualZero);
rewritingRules.add(succEqualSucc);
rewritingRules.add(succEqualZero);
rewritingRules.add(zeroEqualSucc);

rewritingRules.add(consListIsIn);
rewritingRules.add(listIsIn);
rewritingRules.add(trueIsInTest);
rewritingRules.add(falseIsInTest);
rewritingRules.add(varVerify);
rewritingRules.add(notVerify);
rewritingRules.add(orVerify);
rewritingRules.add(existsVerify);
rewritingRules.add(trueVerify);
rewritingRules.add(falseVerify);

rewritingRules.add(varDup);
rewritingRules.add(existDup);
rewritingRules.add(permBLvar);
rewritingRules.add(permLBvar);
rewritingRules.add(permBLexists);
rewritingRules.add(permLBexists);
rewritingRules.add(permBNvar);
rewritingRules.add(permNBvar);
rewritingRules.add(permBNexists);
rewritingRules.add(permNBexists);

String testrules="<PolygraphicProgram Name=\"QBFProgram\">\n";
for (Iterator iterator = rewritingRules.iterator(); iterator.hasNext();) {
	ThreePath rule = (ThreePath) iterator.next();
	testrules+=threePath2XML(rule);
}
testrules+="</PolygraphicProgram>";
try{
save(testrules,new File("/Users/aurelien/polygraphWorkspace/PolygraphesApp/polygraphes/src/qbfprogram.xml"));
}catch(Exception e){e.printStackTrace();}
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


public static OnePath makeOnePath(Node node){
String nodeName =node.getNodeName();
		// System.out.println(nodeName);
		if(nodeName.equals("OnePath")){
			NodeList nodeChilds=node.getChildNodes();
			for (int i = 0; i < nodeChilds.getLength(); i++) {
				Node nodeChild=nodeChilds.item(i);
				if(!nodeChild.getNodeName().equals("#text")){return makeOnePath(nodeChild);}
			}
		}
		if(nodeName.equals("OneCell")){
			NamedNodeMap attributes=node.getAttributes();
				String name=attributes.getNamedItem("Name").getNodeValue();
				return `OneCell(name);
		}
		/*
		 * if(nodeName.equals("Id")){ return `Id(); }
		 */// useless
		if(nodeName.equals("OneC0")){
			NodeList oneC0s=node.getChildNodes();
			OnePath res=`Id();
			for (int j =oneC0s.getLength()-1 ; j > 0; j--) {
				Node oneC0Element = oneC0s.item(j);
				if(!oneC0Element.getNodeName().contains("#text")){
					res=`OneC0(makeOnePath(oneC0Element),res);
				}				
			}		
			return res;
		}
NodeList childs=node.getChildNodes();
for (int i = 0; i < childs.getLength(); i++) {
	Node child = childs.item(i);
		if(!child.getNodeName().equals("#text")){
			return makeOnePath(child);}
}
return `Id();
}

public static TwoPath makeTwoPath(Node node){
String nodeName =node.getNodeName();
		if(nodeName.equals("TwoPath")){// en prevision des 3-cellules
			NodeList nodeChilds=node.getChildNodes();
			for (int i = 0; i < nodeChilds.getLength(); i++) {
				Node nodeChild=nodeChilds.item(i);
				if(!nodeChild.getNodeName().equals("#text")){return makeTwoPath(nodeChild);}
			}
		}
		if(nodeName.equals("TwoId")){
			NodeList nodeChilds=node.getChildNodes();
			for (int i = 0; i < nodeChilds.getLength(); i++) {
				Node nodeChild=nodeChilds.item(i);
				if(!nodeChild.getNodeName().equals("#text")){return `TwoId(makeOnePath(nodeChild));}
			}
		}
		if(nodeName.equals("TwoCell")){
				NamedNodeMap attributes=node.getAttributes();
				String name=attributes.getNamedItem("Name").getNodeValue();
				String type=attributes.getNamedItem("Type").getNodeValue();
		CellType celltype=null;
		if(type.equals("Function")){celltype=`Function();}
		if(type.equals("Constructor")){celltype=`Constructor();}
		NodeList io=node.getChildNodes();
		OnePath source=`Id();
		OnePath target=`Id();
		for (int j = 0; j < io.getLength(); j++) {
			Node ioChild=io.item(j);
			String ioName =ioChild.getNodeName();
			if(ioName.equals("Source")){source=makeOnePath(ioChild);}
			if(ioName.equals("Target")){target=makeOnePath(ioChild);}
		}
		return `TwoCell(name,source,target,celltype);
		}
		if(nodeName.equals("TwoC0")){
			NodeList twoC0s=node.getChildNodes();
			TwoPath res=`TwoId(Id());
			for (int j = twoC0s.getLength()-1; j >0; j--) {
				Node twoC0Element = twoC0s.item(j);
				if(!twoC0Element.getNodeName().contains("#text")){
					// System.out.println(twoC0Element.getNodeName());
					res=`TwoC0(makeTwoPath(twoC0Element),res);
				}				
			}
			// System.out.println(res);
			return res;
		}
		if(nodeName.equals("TwoC1")){
			NodeList twoC1s=node.getChildNodes();
			TwoPath res=`TwoId(Id());
			for (int j = twoC1s.getLength()-1; j >0; j--) {
				Node twoC1Element = twoC1s.item(j);
				if(!twoC1Element.getNodeName().contains("#text")){
					if(res==`TwoId(Id())){res=`makeTwoPath(twoC1Element);}
					else{
					res=`TwoC1(makeTwoPath(twoC1Element),res);
					}
				}				
			}		
			return res;
		}
NodeList childs=node.getChildNodes();
for (int i = 0; i < childs.getLength(); i++) {
	Node child = childs.item(i);
	if(!child.getNodeName().equals("#text")){
		return makeTwoPath(child);
		}
}
return `TwoId(Id());
}


public static ThreePath makeThreeCell(Node node){
		NamedNodeMap attributes=node.getAttributes();
		String name=attributes.getNamedItem("Name").getNodeValue();
		String type=attributes.getNamedItem("Type").getNodeValue();
		CellType celltype=null;
		if(type.equals("Function")){celltype=`Function();}
		if(type.equals("Constructor")){celltype=`Constructor();}
		NodeList io=node.getChildNodes();
		TwoPath source=`TwoId(Id());
		TwoPath target=`TwoId(Id());
		for (int j = 0; j < io.getLength(); j++) {
			Node ioChild=io.item(j);
			String ioName =ioChild.getNodeName();
			if(ioName.equals("Source")){source=makeTwoPath(ioChild);}
			if(ioName.equals("Target")){target=makeTwoPath(ioChild);}
		}
		return `ThreeCell(name,source,target,celltype);
}

}