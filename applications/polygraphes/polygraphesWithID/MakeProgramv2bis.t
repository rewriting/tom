package compiler;

import polygraphicprogram.types.*;
import polygraphicprogram.types.twopath.*;
import polygraphicprogram.types.onepath.*;

import java.io.*;
import org.w3c.dom.*;

import java.util.Vector;
import java.util.Iterator;

public class MakeProgramv2bis{

%include { polygraphicprogram/PolygraphicProgram.tom }
%include { sl.tom }
%include{ dom.tom }

//-----------------------------------------------------------------------------
// NAT
//-----------------------------------------------------------------------------
// 1-chemin representant les entiers naturels
private static	OnePath nat=`OneCell("nat");
// constructeurs sur les entiers naturels
private static	TwoPath zero=`TwoCell("zero",Id(),nat,Constructor(),0);
private static	TwoPath succ =`TwoCell("succ",nat,nat,Constructor(),0);
// 2-cellules de structure
private static	TwoPath eraser= makeEraser("nat");
private static	TwoPath duplication= makeDuplication("nat");
private static	TwoPath permutation = makePermutation((OneCell)nat,(OneCell)nat);
// addition, soustraction et division
private static	TwoPath plus = `TwoCell("plus",OneC0(nat,nat),nat,Function(),0);
private static	TwoPath minus = `TwoCell("minus",OneC0(nat,nat),nat,Function(),0);
private static	TwoPath division = `TwoCell("division",OneC0(nat,nat),nat,Function(),0);
private static	TwoPath multiplication = `TwoCell("multiplication",OneC0(nat,nat),nat,Function(),0);
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
private static	TwoPath consList=`TwoCell("consList",Id(),list,Constructor(),0);
private static	TwoPath add =`TwoCell("add",OneC0(nat,list),list,Constructor(),0);
// 2-cellules de structure
private static	TwoPath eraserList= makeEraser("list");
private static	TwoPath duplicationList= makeDuplication("list");
private static	TwoPath permutationList = makePermutation((OneCell)list,(OneCell)list);
private static	TwoPath permutationNL = makePermutation((OneCell)nat,(OneCell)list);
private static	TwoPath permutationLN = makePermutation((OneCell)list,(OneCell)nat);
// addition, soustraction et division
private static	TwoPath sort = `TwoCell("sort",list,list,Function(),0);
private static	TwoPath split = `TwoCell("split",list,OneC0(list,list),Function(),0);
private static	TwoPath merge = `TwoCell("merge",OneC0(list,list),list,Function(),0);
//regles
//section append-----------------------
private static  TwoPath append=`TwoCell("append",OneC0(list,nat),list,Function(),0);
//private static ThreePath appendToAdd= `ThreeCell("appendToAdd",append,TwoC1(permutationLN,add),Function());
private static ThreePath appendToAdd= `ThreeCell("appendToAdd",TwoC1(TwoC0(consList,TwoId(nat)),append),TwoC1(TwoC0(TwoId(nat),consList),add),Function());
private static ThreePath addAppend = `ThreeCell("addAppend",TwoC1(TwoC0(TwoC1(TwoC0(TwoId(nat),TwoId(list)),add),TwoId(nat)),append),TwoC1(TwoC0(TwoId(nat),TwoC1(TwoC0(TwoId(list),TwoId(nat)),append)),add),Function());
//-------------------------------------
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
private static	TwoPath vrai=`TwoCell("true",Id(),bool,Constructor(),0);
private static	TwoPath faux =`TwoCell("false",Id(),bool,Constructor(),0);
// 2-cellules de structure
private static	TwoPath eraserBool= makeEraser("boolean");
private static	TwoPath duplicationBool= makeDuplication("boolean");
private static	TwoPath permutationBool = makePermutation((OneCell)bool,(OneCell)bool);
//Et il faudrait mettre toutes les permutations possibles ici
// addition, soustraction et division
private static	TwoPath not = `TwoCell("not",bool,bool,Function(),0);
private static	TwoPath and = `TwoCell("and",OneC0(bool,bool),bool,Function(),0);
private static	TwoPath or = `TwoCell("or",OneC0(bool,bool),bool,Function(),0);
private static	TwoPath mergeSwitch = `TwoCell("mergeSwitch",OneC0(bool,nat,list,nat,list),list,Function(),0);
//permutation bool et list
private static TwoPath permutationBL= makePermutation((OneCell)bool,(OneCell)list);
private static TwoPath permutationLB= makePermutation((OneCell)list,(OneCell)bool);
//permutation nat et bool
private static TwoPath permutationBN= makePermutation((OneCell)bool,(OneCell)nat);
private static TwoPath permutationNB= makePermutation((OneCell)nat,(OneCell)bool);
//regles
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
private static	TwoPath lessOrEqual = `TwoCell("lessOrEqual",OneC0(nat,nat),bool,Function(),0);
private static 	ThreePath zeroLess= `ThreeCell("zeroLess",TwoC1(TwoC0(zero,TwoId(nat)),lessOrEqual),TwoC1(eraser,vrai),Function());
private static 	ThreePath succZeroLess= `ThreeCell("succZeroLess",TwoC1(TwoC0(succ,zero),lessOrEqual),TwoC1(eraser,faux),Function());
private static 	ThreePath doubleSuccLess= `ThreeCell("doubleSuccLess",TwoC1(TwoC0(succ,succ),lessOrEqual),lessOrEqual,Function());
//merge
private static ThreePath doubleAddMerge = `ThreeCell("doubleAddMerge",TwoC1(TwoC0(TwoId(nat),TwoId(list),TwoId(nat),TwoId(list)),TwoC0(add,add),merge),TwoC1(TwoC0(duplication,TwoId(list),duplication,TwoId(list)),TwoC0(TwoId(nat),TwoId(nat),permutationLN,TwoId(nat),TwoId(list)),TwoC0(TwoId(nat),permutation,TwoId(list),TwoId(nat),TwoId(list)),TwoC0(lessOrEqual,TwoId(nat),TwoId(list),TwoId(nat),TwoId(list)),mergeSwitch),Function());
private static ThreePath merge1 = `ThreeCell("merge1",TwoC1(TwoC0(vrai,TwoId(nat),TwoId(list),TwoId(nat),TwoId(list)),mergeSwitch),TwoC1(TwoC0(TwoId(nat),TwoC1(TwoC0(TwoId(list),TwoC1(TwoC0(TwoId(nat),TwoId(list)),add)),merge)),add),Function());
private static ThreePath merge2 = `ThreeCell("merge1",TwoC1(TwoC0(faux,TwoId(nat),TwoId(list),TwoId(nat),TwoId(list)),mergeSwitch),TwoC1(TwoC0(add,TwoId(nat),TwoId(list)),TwoC0(permutationLN,TwoId(list)),TwoC0(TwoId(nat),merge),add),Function());
//carre et cube
private static TwoPath carre = `TwoCell("carre",nat,nat,Function(),0);
private static ThreePath zeroCarre = `ThreeCell("zeroCarre",TwoC1(zero,carre),zero,Function());
private static ThreePath succCarre = `ThreeCell("succCarre",TwoC1(succ,carre),TwoC1(TwoC0(TwoC1(duplication,TwoC0(TwoC1(TwoC0(TwoC1(zero,succ,succ),succ),multiplication),carre),plus),TwoC1(zero,succ)),minus),Function());
//private static ThreePath succCarre = `ThreeCell("succCarre",TwoC1(succ,carre),TwoC1(succ,duplication,multiplication),Function());
private static TwoPath cube = `TwoCell("cube",nat,nat,Function(),0);
private static ThreePath zeroCube = `ThreeCell("zeroCube",TwoC1(zero,cube),zero,Function());
private static ThreePath succCube = `ThreeCell("succCube",TwoC1(succ,cube),TwoC1(TwoC0(TwoC1(zero,succ,succ,succ),duplication),TwoC0(TwoC1(TwoC0(TwoC1(zero,succ),multiplication),plus),TwoC1(duplication,TwoC0(TwoC1(TwoC0(TwoC1(zero,succ,succ,succ),carre),multiplication),cube),plus)),plus),Function());
//egalité nat
private static TwoPath equal=`TwoCell("equal",OneC0(nat,nat),bool,Function(),0);
private static ThreePath zeroEqualZero = `ThreeCell("zeroEqualZero",TwoC1(TwoC0(zero,zero),equal),vrai,Function());
private static ThreePath succEqualSucc = `ThreeCell("succEqualSucc",TwoC1(TwoC0(succ,succ),equal),equal,Function());
private static ThreePath zeroEqualSucc = `ThreeCell("zeroEqualSucc",TwoC1(TwoC0(zero,succ),equal),TwoC1(eraser,faux),Function());
private static ThreePath succEqualZero = `ThreeCell("succEqualZero",TwoC1(TwoC0(succ,zero),equal),TwoC1(eraser,faux),Function());
//egalite list
private static TwoPath lEqual=`TwoCell("lEqual",OneC0(list,list),bool,Function(),0);
private static ThreePath consListEqualconsList = `ThreeCell("consListEqualconsList",TwoC1(TwoC0(consList,consList),lEqual),vrai,Function());
private static ThreePath addEqualconsList = `ThreeCell("addEqualconsList",TwoC1(TwoC0(add,consList),lEqual),TwoC1(TwoC0(eraser,eraserList),faux),Function());
private static ThreePath addEqualAdd = `ThreeCell("addEqualAdd",TwoC1(TwoC0(add,add),lEqual),TwoC1(TwoC0(TwoId(nat),permutationLN,TwoId(list)),TwoC0(equal,lEqual),and),Function());
private static ThreePath consListEqualAdd = `ThreeCell("consListEqualAdd",TwoC1(TwoC0(add,consList),lEqual),TwoC1(TwoC0(eraser,eraserList),faux),Function());



public static void main(String[] args) {

//TYPES
//nat
Vector<TwoPath> natConstructors=new Vector<TwoPath>();
natConstructors.add(zero);
natConstructors.add(succ);
//list
Vector<TwoPath> listConstructors=new Vector<TwoPath>();
listConstructors.add(consList);
listConstructors.add(add);
//boolean
Vector<TwoPath> booleanConstructors=new Vector<TwoPath>();
booleanConstructors.add(vrai);
booleanConstructors.add(faux);

//FUNCTIONS
//plus
Vector<ThreePath> plusRules=new Vector<ThreePath>();
plusRules.add(plusZero);
plusRules.add(plusSucc);
//minus
Vector<ThreePath> minusRules=new Vector<ThreePath>();
minusRules.add(minusZero1);
minusRules.add(minusZero2);
minusRules.add(minusDoubleSucc);
//multiplication
Vector<ThreePath> multiplicationRules=new Vector<ThreePath>();
multiplicationRules.add(multZero);
multiplicationRules.add(multSucc);
//division
Vector<ThreePath> divisionRules=new Vector<ThreePath>();
divisionRules.add(divZero);
divisionRules.add(divSucc);
//carre
Vector<ThreePath> carreRules=new Vector<ThreePath>();
carreRules.add(zeroCarre);
carreRules.add(succCarre);
//cube
Vector<ThreePath> cubeRules=new Vector<ThreePath>();
cubeRules.add(zeroCube);
cubeRules.add(succCube);
//equal
Vector<ThreePath> equalRules=new Vector<ThreePath>();
equalRules.add(zeroEqualZero);
equalRules.add(succEqualSucc);
equalRules.add(succEqualZero);
equalRules.add(zeroEqualSucc);
//append
Vector<ThreePath> appendRules=new Vector<ThreePath>();
appendRules.add(appendToAdd);
//split
Vector<ThreePath> splitRules=new Vector<ThreePath>();
splitRules.add(consListSplit);
splitRules.add(addSplit);
splitRules.add(doubleAddSplit);
//sort
Vector<ThreePath> sortRules=new Vector<ThreePath>();
sortRules.add(consListSort);
sortRules.add(addSort);
sortRules.add(doubleAddSort);
//merge
Vector<ThreePath> mergeRules=new Vector<ThreePath>();
mergeRules.add(consListMerge1);
mergeRules.add(consListMerge2);
mergeRules.add(doubleAddMerge);
//lessOrEqual
Vector<ThreePath> lessOrEqualRules=new Vector<ThreePath>();
lessOrEqualRules.add(zeroLess);
lessOrEqualRules.add(succZeroLess);
lessOrEqualRules.add(doubleSuccLess);
//lEqual
Vector<ThreePath> lEqualRules=new Vector<ThreePath>();
lEqualRules.add(consListEqualconsList);
lEqualRules.add(addEqualconsList);
lEqualRules.add(consListEqualAdd);
lEqualRules.add(addEqualAdd);
//mergeSwitch
Vector<ThreePath> mergeSwitchRules=new Vector<ThreePath>();
mergeSwitchRules.add(merge1);
mergeSwitchRules.add(merge2);
//not
Vector<ThreePath> notRules=new Vector<ThreePath>();
notRules.add(notTrue);
notRules.add(notFalse);
//or
Vector<ThreePath> orRules=new Vector<ThreePath>();
orRules.add(trueOrTrue);
orRules.add(trueOrFalse);
orRules.add(FalseOrTrue);
orRules.add(FalseOrFalse);
//and
Vector<ThreePath> andRules=new Vector<ThreePath>();
andRules.add(trueAndTrue);
andRules.add(trueAndFalse);
andRules.add(FalseAndTrue);
andRules.add(FalseAndFalse);


String program="<PolygraphicProgram Name=\"TestProgramv2\">\n";

program+=makeTypeXML(nat,natConstructors);
program+=makeTypeXML(list,listConstructors);
program+=makeTypeXML(bool,booleanConstructors);

program+=makeFunctionXML(plus,plusRules);
program+=makeFunctionXML(minus,minusRules);
program+=makeFunctionXML(multiplication,multiplicationRules);
program+=makeFunctionXML(division,divisionRules);
program+=makeFunctionXML(carre,carreRules);
program+=makeFunctionXML(cube,cubeRules);
program+=makeFunctionXML(equal,equalRules);
program+=makeFunctionXML(split,splitRules);
program+=makeFunctionXML(sort,sortRules);
program+=makeFunctionXML(merge,mergeRules);
program+=makeFunctionXML(lEqual,lEqualRules);
program+=makeFunctionXML(mergeSwitch,mergeSwitchRules);
program+=makeFunctionXML(lessOrEqual,lessOrEqualRules);
program+=makeFunctionXML(append,appendRules);
program+=makeFunctionXML(not,notRules);
program+=makeFunctionXML(or,orRules);
program+=makeFunctionXML(and,andRules);

program+="</PolygraphicProgram>";
try{
save(program,new File("/Users/aurelien/polygraphWorkspace/PolygraphesWithID/src/testprogramv2.xml"));
}catch(Exception e){e.printStackTrace();}
}

public static String makeFunctionXML(TwoPath function, Vector<ThreePath> functionRules){
String xml="";
xml+="<Function>\n";
xml+=twoPath2XML(function);
for (Iterator iterator = functionRules.iterator(); iterator.hasNext();) {
	ThreePath rule = (ThreePath) iterator.next();
	xml+="<Rule>\n";
	xml+=threePath2XML(rule);
	xml+="</Rule>\n";
}
xml+="</Function>\n";
return xml;
}

public static String makeTypeXML(OnePath type, Vector<TwoPath> constructors){
String xml="";
xml+="<Type>\n";
xml+=onePath2XML(type);
for (Iterator iterator = constructors.iterator(); iterator.hasNext();) {
	TwoPath constructor = (TwoPath) iterator.next();
	xml+="<Constructor>\n";
	xml+=twoPath2XML(constructor);
	xml+="</Constructor>\n";
}
xml+="</Type>\n";
return xml;
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
			TwoCell(name,source,target,type,id) -> { return "<TwoPath>\n<TwoCell Name=\""+`name+"\" Type=\""+`type.toString().replace("()","")+"\">\n<Source>\n"+onePath2XML(`source)+"</Source>\n<Target>\n"+onePath2XML(`target)+"</Target>\n</TwoCell>\n</TwoPath>\n"; }
			TwoC0(head,tail*) -> {return "<TwoPath>\n<TwoC0>\n"+twoC02XML(`head)+twoC02XML(`tail)+"</TwoC0>\n</TwoPath>\n";}
			TwoC1(head,tail*) -> {return "<TwoPath>\n<TwoC1>\n"+twoC12XML(`head)+twoC12XML(`tail)+"</TwoC1>\n</TwoPath>\n";}
}
return "";
}

public static String twoC02XML(TwoPath path){
%match (TwoPath path){
			TwoId(onepath) -> {return "<TwoId>\n"+onePath2XML(`onepath)+"</TwoId>\n";}
			TwoCell(name,source,target,type,id) -> { return "<TwoCell Name=\""+`name+"\" Type=\""+`type.toString().replace("()","")+"\">\n<Source>\n"+onePath2XML(`source)+"</Source>\n<Target>\n"+onePath2XML(`target)+"</Target>\n</TwoCell>\n"; }
			TwoC0(head,tail*) -> {return twoC02XML(`head)+twoC02XML(`tail);}
			TwoC1(head,tail*) -> {return "<TwoC1>\n"+twoC12XML(`head)+twoC12XML(`tail)+"</TwoC1>\n";}
}
return "";
}

public static String twoC12XML(TwoPath path){
%match (TwoPath path){
			TwoId(onepath) -> {return "<TwoId>\n"+onePath2XML(`onepath)+"</TwoId>\n";}
			TwoCell(name,source,target,type,id) -> { return "<TwoCell Name=\""+`name+"\" Type=\""+`type.toString().replace("()","")+"\">\n<Source>\n"+onePath2XML(`source)+"</Source>\n<Target>\n"+onePath2XML(`target)+"</Target>\n</TwoCell>\n"; }
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
return (TwoCell)`TwoCell(name+"eraser",OneCell(name),Id(),Function(),0);
}

public static TwoCell makePermutation(OneCell left, OneCell right){
return (TwoCell)`TwoCell("permutation"+left.getName()+right.getName(),OneC0(left,right),OneC0(right,left),Function(),0);
}

public static TwoCell makeDuplication(String name){
return (TwoCell)`TwoCell("duplication"+name,OneCell(name),OneC0(OneCell(name),OneCell(name)),Function(),0);
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