package compiler;

import polygraphicprogram.types.*;
import polygraphicprogram.types.twopath.*;
import polygraphicprogram.types.onepath.*;
import sun.reflect.generics.scope.ConstructorScope;

import java.io.*;
import org.w3c.dom.*;

import com.sun.org.apache.xerces.internal.impl.xpath.regex.Match;

import java.util.Vector;
import java.util.Iterator;

public class MakeProgramv2bis{

%include { polygraphicprogram/PolygraphicProgram.tom }
%include { sl.tom }
%include{ dom.tom }

private static Vector<ThreePath> rewritingRules=new Vector<ThreePath>();
private static Vector<OneCell> types=new Vector<OneCell>();
private static Vector<TwoPath> constructors=new Vector<TwoPath>();
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
// LISTS
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
private static ThreePath permLNzero = `ThreeCell ("permLNzero",TwoC1(TwoC0(TwoId(list),zero),permutationLN),TwoC0(zero,TwoId(list)),Function());
private static ThreePath permNLsucc = `ThreeCell ("permNLsucc",TwoC1(TwoC0(succ,TwoId(list)),permutationNL),TwoC1(permutationNL,TwoC0(TwoId(list),succ)),Function());
private static ThreePath permLNsucc = `ThreeCell ("permLNsucc",TwoC1(TwoC0(TwoId(list),succ),permutationLN),TwoC1(permutationLN,TwoC0(succ,TwoId(list))),Function());
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
private static	TwoPath and = `TwoCell("and",OneC0(bool,bool),bool,Function());
private static	TwoPath or = `TwoCell("or",OneC0(bool,bool),bool,Function());
private static	TwoPath mergeSwitch = `TwoCell("mergeSwitch",OneC0(bool,nat,list,nat,list),list,Function());
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
//regles
private static 	ThreePath trueEraz= `ThreeCell("trueEraz",TwoC1(vrai,eraserBool),TwoId(Id()),Function());
private static 	ThreePath falseEraz= `ThreeCell("falseEraz",TwoC1(faux,eraserBool),TwoId(Id()),Function());
private static 	ThreePath falseDup= `ThreeCell("falseDup",TwoC1(faux,duplicationBool),TwoC0(faux,faux),Function());
private static 	ThreePath trueDup= `ThreeCell("trueDup",TwoC1(vrai,duplicationBool),TwoC0(vrai,vrai),Function());
private static 	ThreePath truePerm1= `ThreeCell("truePerm1",TwoC1(TwoC0(vrai,TwoId(bool)),permutationBool),TwoC0(TwoId(bool),vrai),Function());
private static 	ThreePath truePerm2= `ThreeCell("truePerm2",TwoC1(TwoC0(TwoId(bool),vrai),permutationBool),TwoC0(vrai,TwoId(bool)),Function());
private static 	ThreePath falsePerm1= `ThreeCell("falsePerm1",TwoC1(TwoC0(faux,TwoId(bool)),permutationBool),TwoC0(TwoId(bool),faux),Function());
private static 	ThreePath falsePerm2= `ThreeCell("falsePerm2",TwoC1(TwoC0(TwoId(bool),faux),permutationBool),TwoC0(faux,TwoId(bool)),Function());
private static 	ThreePath trueAndTrue= `ThreeCell("trueAndTrue",TwoC1(TwoC0(vrai,vrai),and),vrai,Function());
private static 	ThreePath trueAndFalse= `ThreeCell("trueAndFalse",TwoC1(TwoC0(vrai,faux),and),faux,Function());
private static 	ThreePath FalseAndTrue= `ThreeCell("FalseAndTrue",TwoC1(TwoC0(faux,vrai),and),faux,Function());
private static 	ThreePath FalseAndFalse= `ThreeCell("FalseAndFalse",TwoC1(TwoC0(faux,faux),and),faux,Function());
private static 	ThreePath trueOrTrue= `ThreeCell("trueOrTrue",TwoC1(TwoC0(vrai,vrai),or),vrai,Function());
private static 	ThreePath trueOrFalse= `ThreeCell("trueOrFalse",TwoC1(TwoC0(vrai,faux),or),vrai,Function());
private static 	ThreePath FalseOrTrue= `ThreeCell("FalseOrTrue",TwoC1(TwoC0(faux,vrai),or),vrai,Function());
private static 	ThreePath FalseOrFalse= `ThreeCell("FalseOrFalse",TwoC1(TwoC0(faux,faux),or),faux,Function());
private static 	ThreePath notTrue= `ThreeCell("notTrue",TwoC1(vrai,not),faux,Function());
private static 	ThreePath notFalse= `ThreeCell("notFalse",TwoC1(faux,not),vrai,Function());
//introduction de la comparaison
private static	TwoPath lessOrEqual = `TwoCell("lessOrEqual",OneC0(nat,nat),bool,Function());
private static 	ThreePath zeroLess= `ThreeCell("zeroLess",TwoC1(TwoC0(zero,TwoId(nat)),lessOrEqual),TwoC1(eraser,vrai),Function());
private static 	ThreePath succZeroLess= `ThreeCell("succZeroLess",TwoC1(TwoC0(succ,zero),lessOrEqual),TwoC1(eraser,faux),Function());
private static 	ThreePath doubleSuccLess= `ThreeCell("doubleSuccLess",TwoC1(TwoC0(succ,succ),lessOrEqual),lessOrEqual,Function());
//merge
private static ThreePath doubleAddMerge = `ThreeCell("doubleAddMerge",TwoC1(TwoC0(TwoId(nat),TwoId(list),TwoId(nat),TwoId(list)),TwoC0(add,add),merge),TwoC1(TwoC0(duplication,TwoId(list),duplication,TwoId(list)),TwoC0(TwoId(nat),TwoId(nat),permutationLN,TwoId(nat),TwoId(list)),TwoC0(TwoId(nat),permutation,TwoId(list),TwoId(nat),TwoId(list)),TwoC0(lessOrEqual,TwoId(nat),TwoId(list),TwoId(nat),TwoId(list)),mergeSwitch),Function());
private static ThreePath merge1 = `ThreeCell("merge1",TwoC1(TwoC0(vrai,TwoId(nat),TwoId(list),TwoId(nat),TwoId(list)),mergeSwitch),TwoC1(TwoC0(TwoId(nat),TwoC1(TwoC0(TwoId(list),TwoC1(TwoC0(TwoId(nat),TwoId(list)),add)),merge)),add),Function());
private static ThreePath merge2 = `ThreeCell("merge1",TwoC1(TwoC0(faux,TwoId(nat),TwoId(list),TwoId(nat),TwoId(list)),mergeSwitch),TwoC1(TwoC0(add,TwoId(nat),TwoId(list)),TwoC0(permutationLN,TwoId(list)),TwoC0(TwoId(nat),merge),add),Function());
//carre et cube
private static TwoPath carre = `TwoCell("carre",nat,nat,Function());
private static ThreePath zeroCarre = `ThreeCell("zeroCarre",TwoC1(zero,carre),zero,Function());
private static ThreePath succCarre = `ThreeCell("succCarre",TwoC1(succ,carre),TwoC1(TwoC0(TwoC1(duplication,TwoC0(TwoC1(TwoC0(TwoC1(zero,succ,succ),succ),multiplication),carre),plus),TwoC1(zero,succ)),minus),Function());
//private static ThreePath succCarre = `ThreeCell("succCarre",TwoC1(succ,carre),TwoC1(succ,duplication,multiplication),Function());
private static TwoPath cube = `TwoCell("cube",nat,nat,Function());
private static ThreePath zeroCube = `ThreeCell("zeroCube",TwoC1(zero,cube),zero,Function());
private static ThreePath succCube = `ThreeCell("succCube",TwoC1(succ,cube),TwoC1(TwoC0(TwoC1(zero,succ,succ,succ),duplication),TwoC0(TwoC1(TwoC0(TwoC1(zero,succ),multiplication),plus),TwoC1(duplication,TwoC0(TwoC1(TwoC0(TwoC1(zero,succ,succ,succ),carre),multiplication),cube),plus)),plus),Function());
//egalité nat
private static TwoPath equal=`TwoCell("equal",OneC0(nat,nat),bool,Function());
private static ThreePath zeroEqualZero = `ThreeCell("zeroEqualZero",TwoC1(TwoC0(zero,zero),equal),vrai,Function());
private static ThreePath succEqualSucc = `ThreeCell("succEqualSucc",TwoC1(TwoC0(succ,succ),equal),equal,Function());
private static ThreePath zeroEqualSucc = `ThreeCell("zeroEqualSucc",TwoC1(TwoC0(zero,succ),equal),TwoC1(eraser,faux),Function());
private static ThreePath succEqualZero = `ThreeCell("succEqualZero",TwoC1(TwoC0(succ,zero),equal),TwoC1(eraser,faux),Function());
//egalite list
private static TwoPath lEqual=`TwoCell("lEqual",OneC0(list,list),bool,Function());
private static ThreePath consListEqualconsList = `ThreeCell("consListEqualconsList",TwoC1(TwoC0(consList,consList),lEqual),vrai,Function());
private static ThreePath addEqualconsList = `ThreeCell("addEqualconsList",TwoC1(TwoC0(add,consList),lEqual),TwoC1(TwoC0(eraser,eraserList),faux),Function());
private static ThreePath addEqualAdd = `ThreeCell("addEqualAdd",TwoC1(TwoC0(add,add),lEqual),TwoC1(TwoC0(TwoId(nat),permutationLN,TwoId(list)),TwoC0(equal,lEqual),and),Function());
private static ThreePath consListEqualAdd = `ThreeCell("consListEqualAdd",TwoC1(TwoC0(add,consList),lEqual),TwoC1(TwoC0(eraser,eraserList),faux),Function());



public static void main(String[] args) {

types.add((OneCell)nat);
types.add((OneCell)list);
types.add((OneCell)bool);

constructors.add(zero);
constructors.add(succ);
constructors.add(consList);
constructors.add(add);
constructors.add(append);
constructors.add(vrai);
constructors.add(faux);

rewritingRules.add(plusZero);
rewritingRules.add(plusSucc);
rewritingRules.add(minusZero1);
rewritingRules.add(minusZero2);
rewritingRules.add(minusDoubleSucc);
rewritingRules.add(divZero);
rewritingRules.add(divSucc);
rewritingRules.add(multZero);
rewritingRules.add(multSucc);

rewritingRules.add(appendToAdd);
rewritingRules.add(addAppend);
rewritingRules.add(consListSort);
rewritingRules.add(addSort);
rewritingRules.add(doubleAddSort);
rewritingRules.add(consListSplit);
rewritingRules.add(addSplit);
rewritingRules.add(doubleAddSplit);
rewritingRules.add(consListMerge1);
rewritingRules.add(consListMerge2);


rewritingRules.add(trueAndTrue);
rewritingRules.add(trueAndFalse);
rewritingRules.add(FalseAndTrue);
rewritingRules.add(FalseAndFalse);
rewritingRules.add(trueOrTrue);
rewritingRules.add(trueOrFalse);
rewritingRules.add(FalseOrTrue);
rewritingRules.add(FalseOrFalse);
rewritingRules.add(notTrue);
rewritingRules.add(notFalse);
rewritingRules.add(zeroLess);
rewritingRules.add(succZeroLess);
rewritingRules.add(doubleSuccLess);
rewritingRules.add(doubleAddMerge);
rewritingRules.add(merge1);
rewritingRules.add(merge2);

rewritingRules.add(zeroCarre);
rewritingRules.add(succCarre);
rewritingRules.add(zeroCube);
rewritingRules.add(succCube);

rewritingRules.add(zeroEqualZero);
rewritingRules.add(succEqualSucc);
rewritingRules.add(succEqualZero);
rewritingRules.add(zeroEqualSucc);

rewritingRules.add(consListEqualconsList);
rewritingRules.add(addEqualconsList);
rewritingRules.add(consListEqualAdd);
rewritingRules.add(addEqualAdd);
/*
for (Iterator iterator = constructors.iterator(); iterator.hasNext();) {
	TwoPath constructor = (TwoPath) iterator.next();
	Vector<ThreePath> constructorRules=makeStructureRules(constructor,types);
	for (Iterator iterator2 = constructorRules.iterator(); iterator2.hasNext();) {
		ThreePath rule = (ThreePath) iterator2.next();
		rewritingRules.add(rule);
	}
}

System.out.println(rewritingRules.size());
*/

String testrules="<PolygraphicProgram Name=\"TestProgramv2\">\n";
testrules+="<Types>\n";
for (Iterator iterator = types.iterator(); iterator.hasNext();) {
	OnePath rule = (OnePath) iterator.next();
	testrules+=onePath2XML(rule);
}
testrules+="</Types>\n";
testrules+="<Constructors>\n";
for (Iterator iterator = constructors.iterator(); iterator.hasNext();) {
	TwoPath rule = (TwoPath) iterator.next();
	testrules+=twoPath2XML(rule);
}
testrules+="</Constructors>\n";
testrules+="<Rules>\n";
for (Iterator iterator = rewritingRules.iterator(); iterator.hasNext();) {
	ThreePath rule = (ThreePath) iterator.next();
	testrules+=threePath2XML(rule);
}
testrules+="</Rules>\n";
testrules+="</PolygraphicProgram>";
try{
save(testrules,new File("/Users/aurelien/polygraphWorkspace/PolygraphesApp/polygraphes/src/testprogramv2.xml"));
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


public static Vector<ThreePath> makeStructureRules(TwoPath constructor,Vector<OneCell> types)
{
Vector<ThreePath> rules =new Vector<ThreePath>();
if(constructor.getType().isConstructor())
{
OnePath source= constructor.getSource();
//eraser
ThreePath eraserRule=`ThreeCell(constructor.getName()+"eraser",TwoC1(constructor,makeEraser(constructor.getTarget().getName())),makeEraserRuleTarget(source),Function());
System.out.println(eraserRule);
rules.add(eraserRule);
//permutation, pour chaque type possible, on fait 2 regles avec le constructeur de chaque cote possible
for (Iterator iterator = types.iterator(); iterator.hasNext();) {
	OneCell type = (OneCell) iterator.next();
	TwoPath leftPermutationCell=makePermutation((OneCell)constructor.getTarget(),type);
	TwoPath leftPermutationRuleTarget=`TwoC0(TwoId(type),constructor);
	if(!source.isId()&&!source.isConsOneC0()){leftPermutationRuleTarget=`TwoC1(leftPermutationCell,leftPermutationRuleTarget);}
	if(source.isConsOneC0()){leftPermutationRuleTarget=`TwoC1(makeLeftPermutationRuleTarget(source,type),leftPermutationRuleTarget);}
	ThreePath leftPermutationRule=`ThreeCell(constructor.getName()+"leftpermutation"+constructor.getTarget().getName()+type.getName(),TwoC1(TwoC0(constructor,TwoId(type)),leftPermutationCell),leftPermutationRuleTarget,Function());
	System.out.println(leftPermutationRule);
	rules.add(leftPermutationRule);
	TwoPath rightPermutationCell=makePermutation(type,(OneCell)constructor.getTarget());
	TwoPath rightPermutationRuleTarget=`TwoC0(constructor,TwoId(type));
	if(!source.isId()&&!source.isConsOneC0()){rightPermutationRuleTarget=`TwoC1(rightPermutationCell,rightPermutationRuleTarget);}
	if(source.isConsOneC0()){rightPermutationRuleTarget=`TwoC1(makeRightPermutationRuleTarget(source,type),rightPermutationRuleTarget);}
	ThreePath rightPermutationRule=`ThreeCell(constructor.getName()+"rightpermutation"+type.getName()+constructor.getTarget().getName(),TwoC1(TwoC0(TwoId(type),constructor),rightPermutationCell),rightPermutationRuleTarget,Function());
	System.out.println(rightPermutationRule);
	rules.add(rightPermutationRule);
	}
//duplication
TwoPath duplicationRuleTarget=`TwoC0(constructor,constructor);
if(!source.isId()){duplicationRuleTarget=`TwoC1(makeDuplicationRuleTarget(source),duplicationRuleTarget);}
ThreePath duplicationRule = `ThreeCell(constructor.getName()+"duplication",TwoC1(constructor,makeDuplication(constructor.getTarget().getName())),duplicationRuleTarget ,Function());
rules.add(duplicationRule);
System.out.println(duplicationRule);
return rules;
}
System.out.println("error : this is not a constructor");
constructor.print();
return null;
}

public static TwoCell makeEraser(String name){
return (TwoCell)`TwoCell(name+"eraser",OneCell(name),Id(),Function());
}

public static TwoCell makePermutation(OneCell left, OneCell right){
return (TwoCell)`TwoCell("permutation"+left.getName()+right.getName(),OneC0(left,right),OneC0(right,left),Function());
}

public static TwoCell makeDuplication(String name){
return (TwoCell)`TwoCell("duplication"+name,OneCell(name),OneC0(OneCell(name),OneCell(name)),Function());
}

public static TwoPath makeEraserRuleTarget(OnePath constructorSource){
%match (OnePath constructorSource){
			Id() -> {return `TwoId(Id()); }
			OneCell(name) -> { return makeEraser(`name); }
			OneC0(head,tail*) -> {return `TwoC0(makeEraser(head.getName()),makeEraserRuleTarget(tail));}
}
return `TwoId(Id());
}

public static TwoPath makeLeftPermutationRuleTarget(OnePath constructorSource,OneCell permutationType){
OnePath[] c0array= c0ToArray((OneC0)constructorSource);
TwoPath[] twoC0Array=new TwoPath[c0array.length];
for (int i = 0; i < c0array.length; i++) {
	twoC0Array[i]=`TwoId(c0array[i]);
}
TwoPath permutation=`TwoId(permutationType);
TwoPath target=`TwoId(Id());
for (int j = twoC0Array.length-1; j >= 0; j--) {
	TwoPath mem=twoC0Array[j];
	twoC0Array[j]=makePermutation((OneCell)twoC0Array[j].getonePath(),(OneCell)permutation.getonePath());
	if(target==`TwoId(Id())){target=(TwoC0)fromArray(twoC0Array);}
	else{target=`TwoC1(target,(TwoC0)fromArray(twoC0Array));}
	twoC0Array[j]=mem;
}
return target;
}


public static TwoPath makeRightPermutationRuleTarget(OnePath constructorSource,OneCell permutationType){
OnePath[] c0array= c0ToArray((OneC0)constructorSource);
TwoPath[] twoC0Array=new TwoPath[c0array.length];
for (int i = 0; i < c0array.length; i++) {
	twoC0Array[i]=`TwoId(c0array[i]);
}
TwoPath permutation=`TwoId(permutationType);
TwoPath target=`TwoId(Id());
for (int j = 0; j <twoC0Array.length; j++) {
	TwoPath mem=twoC0Array[j];
	twoC0Array[j]=makePermutation((OneCell)permutation.getonePath(),(OneCell)twoC0Array[j].getonePath());
	if(target==`TwoId(Id())){target=(TwoC0)fromArray(twoC0Array);}
	else{target=`TwoC1(target,(TwoC0)fromArray(twoC0Array));}
	twoC0Array[j]=mem;
}
return target;
}

public static TwoPath makeDuplicationRuleTarget(OnePath constructorSource){
TwoPath target=`TwoId(Id());
if(constructorSource.isConsOneC0()){
OnePath[] c0array= c0ToArray((OneC0)constructorSource);
TwoPath[] twoC0Array=new TwoPath[2*c0array.length];
TwoPath permutationLine = `TwoId(Id());
for (int i = 0; i < c0array.length; i++) {
	twoC0Array[2*i]=`TwoId(c0array[i]);
	twoC0Array[2*i+1]=`TwoId(c0array[i]);
	if(i==0){permutationLine=makeDuplication(c0array[i].getName());}
	else{permutationLine=`TwoC0(permutationLine,makeDuplication(c0array[i].getName()));}
}
for (int j = 1; j < c0array.length; j++) {
	TwoPath path=twoC0Array[0];
	for (int k = 1; k < j; k++) {
		path=`TwoC0(path,twoC0Array[k]);
	}
	for (int j2 = j; j2 < twoC0Array.length-j; j2+=2) {//pb ici
		path=`TwoC0(path,makePermutation((OneCell)twoC0Array[j2].getonePath(),(OneCell)twoC0Array[j2+1].getonePath()));
		TwoPath mem=twoC0Array[j2];
		twoC0Array[j2]=twoC0Array[j2+1];
		twoC0Array[j2+1]=mem;
	}
	for (int l = twoC0Array.length-j; l < twoC0Array.length; l++) {
		path=`TwoC0(path,twoC0Array[l]);
	}
	if(j==1){target=path;}
	else{target=`TwoC1(target,path);}
}
return `TwoC1(permutationLine,target);}
else{return `makeDuplication(constructorSource.getName());}
}

 public static TwoPath fromArray(TwoPath[] array) {
    TwoPath res = EmptyTwoC0.make();
    for(int i = array.length; i>0;) {
      res = ConsTwoC0.make(array[--i],res);
    }
    return res;
  }

public static OnePath[] c0ToArray(OneC0 oneC0) {
    int size = oneC0.length();
    OnePath[] array = new OnePath[size];
    int i=0;
    if(oneC0 instanceof ConsOneC0) {
      OnePath cur = oneC0;
      while(cur instanceof ConsOneC0) {
        OnePath elem = ((ConsOneC0)cur).getHeadOneC0();
        array[i] = elem;
        i++;
        cur = ((ConsOneC0)cur).getTailOneC0();
        
      }
      array[i] = cur;
    }
    return array;
  }
}