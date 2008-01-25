package tools;

import polygraphicprogram.*;
import polygraphicprogram.types.*;
import polygraphicprogram.types.onepath.*;
import compiler.StructureRuleHandler;

public class TestCellSet{

	%include { polygraphicprogram/PolygraphicProgram.tom }

	//here we define some cells to generate some test inputs

	// -----------------------------------------------------------------------------
	// NAT
	// -----------------------------------------------------------------------------
	// 1-Cell defining the nat type
	public static	OnePath nat=`OneCell("nat");
	// Constructors for nat
	public static	TwoPath zero=`TwoCell("zero",Id(),nat,Constructor(),0);
	public static	TwoPath succ =`TwoCell("succ",nat,nat,Constructor(),0);
	// Structure 2-Cells for nat
	public static	TwoPath eraser= StructureRuleHandler.makeEraser("nat");
	public static	TwoPath duplication= StructureRuleHandler.makeDuplication("nat");
	public static	TwoPath permutation = StructureRuleHandler.makePermutation((OneCell)nat,(OneCell)nat);
	// Function cells for nat
	public static	TwoPath plus = `TwoCell("plus",OneC0(nat,nat),nat,Function(),0);
	public static	TwoPath minus = `TwoCell("minus",OneC0(nat,nat),nat,Function(),0);
	public static	TwoPath division = `TwoCell("division",OneC0(nat,nat),nat,Function(),0);
	public static	TwoPath multiplication = `TwoCell("multiplication",OneC0(nat,nat),nat,Function(),0);
	// associated rules
	public static	ThreePath plusZero = `ThreeCell("plusZero",TwoC1(TwoC0(zero,TwoId(nat)),plus),TwoId(nat),Function());
	public static	ThreePath plusSucc = `ThreeCell("plusSucc",TwoC1(TwoC0(succ,TwoId(nat)),plus),TwoC1(plus,succ),Function());
	public static	ThreePath minusZero1 = `ThreeCell("minusZero1",TwoC1(TwoC0(zero,TwoId(nat)),minus),TwoC1(eraser,zero),Function());
	public static	ThreePath minusZero2 = `ThreeCell("minusZero2",TwoC1(TwoC0(TwoId(nat),zero),minus),TwoId(nat),Function());
	public static	ThreePath minusDoubleSucc = `ThreeCell("minusDoubleSucc",TwoC1(TwoC0(succ,succ),minus),minus,Function());
	public static	ThreePath divZero = `ThreeCell("divZero",TwoC1(TwoC0(zero,TwoId(nat)),division),TwoC1(eraser,zero),Function());	
	public static	ThreePath divSucc = `ThreeCell("divSucc",TwoC1(TwoC0(succ,TwoId(nat)),division),TwoC1(TwoC0(TwoId(nat),duplication),TwoC0(minus,TwoId(nat)),division,succ),Function());
	public static	ThreePath multZero = `ThreeCell("multZero",TwoC1(TwoC0(zero,TwoId(nat)),multiplication),TwoC1(eraser,zero),Function());	
	public static	ThreePath multSucc = `ThreeCell("multSucc",TwoC1(TwoC0(succ,TwoId(nat)),multiplication),TwoC1(TwoC0(TwoId(nat),duplication),TwoC0(multiplication,TwoId(nat)),plus),Function());

	// -----------------------------------------------------------------------------
	// LISTS
	// -----------------------------------------------------------------------------
	// 1-Cell defining the list type
	public static 	OnePath list=`OneCell("list");
	// Constructor 2-Cells for lists
	public static	TwoPath consList=`TwoCell("consList",Id(),list,Constructor(),0);
	public static	TwoPath add =`TwoCell("add",OneC0(nat,list),list,Constructor(),0);
	// Structure 2-Cells for list
	public static	TwoPath eraserList= StructureRuleHandler.makeEraser("list");
	public static	TwoPath duplicationList= StructureRuleHandler.makeDuplication("list");
	public static	TwoPath permutationList = StructureRuleHandler.makePermutation((OneCell)list,(OneCell)list);
	public static	TwoPath permutationNL = StructureRuleHandler.makePermutation((OneCell)nat,(OneCell)list);
	public static	TwoPath permutationLN = StructureRuleHandler.makePermutation((OneCell)list,(OneCell)nat);
	// Function cells for lists
	public static  TwoPath append=`TwoCell("append",OneC0(list,nat),list,Function(),0);
	public static	TwoPath sort = `TwoCell("sort",list,list,Function(),0);
	public static	TwoPath split = `TwoCell("split",list,OneC0(list,list),Function(),0);
	public static	TwoPath merge = `TwoCell("merge",OneC0(list,list),list,Function(),0);
	//associated rules
	public static ThreePath appendToAdd= `ThreeCell("appendToAdd",TwoC1(TwoC0(consList,TwoId(nat)),append),TwoC1(TwoC0(TwoId(nat),consList),add),Function());
	public static ThreePath addAppend = `ThreeCell("addAppend",TwoC1(TwoC0(TwoC1(TwoC0(TwoId(nat),TwoId(list)),add),TwoId(nat)),append),TwoC1(TwoC0(TwoId(nat),TwoC1(TwoC0(TwoId(list),TwoId(nat)),append)),add),Function());
	public static ThreePath consListSort = `ThreeCell("consListSort",TwoC1(consList,sort),consList,Function());
	public static ThreePath addSort = `ThreeCell("addSort",TwoC1(TwoC0(TwoId(nat),consList),add,sort),TwoC1(TwoC0(TwoId(nat),consList),add),Function());
	public static ThreePath doubleAddSort = `ThreeCell("doubleAddSort",TwoC1(TwoC0(TwoId(nat),TwoC1(TwoC0(TwoId(nat),TwoId(list)),add)),add,sort),TwoC1(TwoC0(TwoId(nat),TwoId(nat),split),TwoC0(TwoId(nat),permutationNL,TwoId(list)),TwoC0(add,add),TwoC0(sort,sort),merge),Function());
	public static ThreePath consListSplit = `ThreeCell("consListSplit",TwoC1(consList,split),TwoC0(consList,consList),Function());
	public static ThreePath addSplit = `ThreeCell("addList",TwoC1(TwoC0(TwoId(nat),consList),add,split),TwoC0(TwoC1(TwoC0(TwoId(nat),consList),add),consList),Function());
	public static ThreePath doubleAddSplit = `ThreeCell("doubleAddSplit",TwoC1(TwoC0(TwoId(nat),TwoC1(TwoC0(TwoId(nat),TwoId(list)),add)),add,split),TwoC1(TwoC0(TwoId(nat),TwoId(nat),split),TwoC0(TwoId(nat),permutationNL,TwoId(list)),TwoC0(add,add)),Function());
	public static ThreePath consListMerge1 = `ThreeCell("consListMerge1",TwoC1(TwoC0(consList,TwoId(list)),merge),TwoId(list),Function());
	public static ThreePath consListMerge2 = `ThreeCell("consListMerge2",TwoC1(TwoC0(TwoId(list),consList),merge),TwoId(list),Function());

	// -----------------------------------------------------------------------------
	// BOOLEANS
	// -----------------------------------------------------------------------------
	//  1-Cell defining the boolean type
	public static 	OnePath bool=`OneCell("boolean");
	// Structure 2-Cells for booleans
	public static	TwoPath vrai=`TwoCell("true",Id(),bool,Constructor(),0);
	public static	TwoPath faux =`TwoCell("false",Id(),bool,Constructor(),0);
	// Structure 2-Cells for booleans
	public static	TwoPath eraserBool= StructureRuleHandler.makeEraser("boolean");
	public static	TwoPath duplicationBool= StructureRuleHandler.makeDuplication("boolean");
	public static	TwoPath permutationBool = StructureRuleHandler.makePermutation((OneCell)bool,(OneCell)bool);
	// Function cells for lists
	public static	TwoPath not = `TwoCell("not",bool,bool,Function(),0);
	public static	TwoPath and = `TwoCell("and",OneC0(bool,bool),bool,Function(),0);
	public static	TwoPath or = `TwoCell("or",OneC0(bool,bool),bool,Function(),0);
	public static	TwoPath mergeSwitch = `TwoCell("mergeSwitch",OneC0(bool,nat,list,nat,list),list,Function(),0);
	//rules for booleans
	public static 	ThreePath trueAndTrue= `ThreeCell("trueAndTrue",TwoC1(TwoC0(vrai,vrai),and),vrai,Function());
	public static 	ThreePath trueAndFalse= `ThreeCell("trueAndFalse",TwoC1(TwoC0(vrai,faux),and),faux,Function());
	public static 	ThreePath FalseAndTrue= `ThreeCell("FalseAndTrue",TwoC1(TwoC0(faux,vrai),and),faux,Function());
	public static 	ThreePath FalseAndFalse= `ThreeCell("FalseAndFalse",TwoC1(TwoC0(faux,faux),and),faux,Function());
	public static 	ThreePath trueOrTrue= `ThreeCell("trueOrTrue",TwoC1(TwoC0(vrai,vrai),or),vrai,Function());
	public static 	ThreePath trueOrFalse= `ThreeCell("trueOrFalse",TwoC1(TwoC0(vrai,faux),or),vrai,Function());
	public static 	ThreePath FalseOrTrue= `ThreeCell("FalseOrTrue",TwoC1(TwoC0(faux,vrai),or),vrai,Function());
	public static 	ThreePath FalseOrFalse= `ThreeCell("FalseOrFalse",TwoC1(TwoC0(faux,faux),or),faux,Function());
	public static 	ThreePath notTrue= `ThreeCell("notTrue",TwoC1(vrai,not),faux,Function());
	public static 	ThreePath notFalse= `ThreeCell("notFalse",TwoC1(faux,not),vrai,Function());
	// -----------------------------------------------------------------------------
	// OTHERS
	// -----------------------------------------------------------------------------
	// comparison
	public static	TwoPath lessOrEqual = `TwoCell("lessOrEqual",OneC0(nat,nat),bool,Function(),0);
	public static 	ThreePath zeroLess= `ThreeCell("zeroLess",TwoC1(TwoC0(zero,TwoId(nat)),lessOrEqual),TwoC1(eraser,vrai),Function());
	public static 	ThreePath succZeroLess= `ThreeCell("succZeroLess",TwoC1(TwoC0(succ,zero),lessOrEqual),TwoC1(eraser,faux),Function());
	public static 	ThreePath doubleSuccLess= `ThreeCell("doubleSuccLess",TwoC1(TwoC0(succ,succ),lessOrEqual),lessOrEqual,Function());
	//merge rules
	public static ThreePath doubleAddMerge = `ThreeCell("doubleAddMerge",TwoC1(TwoC0(TwoId(nat),TwoId(list),TwoId(nat),TwoId(list)),TwoC0(add,add),merge),TwoC1(TwoC0(duplication,TwoId(list),duplication,TwoId(list)),TwoC0(TwoId(nat),TwoId(nat),permutationLN,TwoId(nat),TwoId(list)),TwoC0(TwoId(nat),permutation,TwoId(list),TwoId(nat),TwoId(list)),TwoC0(lessOrEqual,TwoId(nat),TwoId(list),TwoId(nat),TwoId(list)),mergeSwitch),Function());
	public static ThreePath merge1 = `ThreeCell("merge1",TwoC1(TwoC0(vrai,TwoId(nat),TwoId(list),TwoId(nat),TwoId(list)),mergeSwitch),TwoC1(TwoC0(TwoId(nat),TwoC1(TwoC0(TwoId(list),TwoC1(TwoC0(TwoId(nat),TwoId(list)),add)),merge)),add),Function());
	public static ThreePath merge2 = `ThreeCell("merge1",TwoC1(TwoC0(faux,TwoId(nat),TwoId(list),TwoId(nat),TwoId(list)),mergeSwitch),TwoC1(TwoC0(add,TwoId(nat),TwoId(list)),TwoC0(permutationLN,TwoId(list)),TwoC0(TwoId(nat),merge),add),Function());
	// square
	public static TwoPath carre = `TwoCell("square",nat,nat,Function(),0);
	public static ThreePath zeroCarre = `ThreeCell("zeroCarre",TwoC1(zero,carre),zero,Function());
	public static ThreePath succCarre = `ThreeCell("succCarre",TwoC1(succ,carre),TwoC1(TwoC0(TwoC1(duplication,TwoC0(TwoC1(TwoC0(TwoC1(zero,succ,succ),succ),multiplication),carre),plus),TwoC1(zero,succ)),minus),Function());
	//cube
	public static TwoPath cube = `TwoCell("cube",nat,nat,Function(),0);
	public static ThreePath zeroCube = `ThreeCell("zeroCube",TwoC1(zero,cube),zero,Function());
	public static ThreePath succCube = `ThreeCell("succCube",TwoC1(succ,cube),TwoC1(TwoC0(TwoC1(zero,succ,succ,succ),duplication),TwoC0(TwoC1(TwoC0(TwoC1(zero,succ),multiplication),plus),TwoC1(duplication,TwoC0(TwoC1(TwoC0(TwoC1(zero,succ,succ,succ),carre),multiplication),cube),plus)),plus),Function());
	// nat equality
	public static TwoPath equal=`TwoCell("equal",OneC0(nat,nat),bool,Function(),0);
	public static ThreePath zeroEqualZero = `ThreeCell("zeroEqualZero",TwoC1(TwoC0(zero,zero),equal),vrai,Function());
	public static ThreePath succEqualSucc = `ThreeCell("succEqualSucc",TwoC1(TwoC0(succ,succ),equal),equal,Function());
	public static ThreePath zeroEqualSucc = `ThreeCell("zeroEqualSucc",TwoC1(TwoC0(zero,succ),equal),TwoC1(eraser,faux),Function());
	public static ThreePath succEqualZero = `ThreeCell("succEqualZero",TwoC1(TwoC0(succ,zero),equal),TwoC1(eraser,faux),Function());
	// list equality
	public static TwoPath lEqual=`TwoCell("lEqual",OneC0(list,list),bool,Function(),0);
	public static ThreePath consListEqualconsList = `ThreeCell("consListEqualconsList",TwoC1(TwoC0(consList,consList),lEqual),vrai,Function());
	public static ThreePath addEqualconsList = `ThreeCell("addEqualconsList",TwoC1(TwoC0(add,consList),lEqual),TwoC1(TwoC0(eraser,eraserList),faux),Function());
	public static ThreePath addEqualAdd = `ThreeCell("addEqualAdd",TwoC1(TwoC0(add,add),lEqual),TwoC1(TwoC0(TwoId(nat),permutationLN,TwoId(list)),TwoC0(equal,lEqual),and),Function());
	public static ThreePath consListEqualAdd = `ThreeCell("consListEqualAdd",TwoC1(TwoC0(add,consList),lEqual),TwoC1(TwoC0(eraser,eraserList),faux),Function());

}