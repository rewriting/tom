package compiler;

import polygraphicprogram.types.*;
import polygraphicprogram.types.twopath.*;
import polygraphicprogram.types.onepath.*;
import java.util.Vector;
import java.util.Iterator;


//generates automatically the structure cells and rules
public class StructureRuleHandler{

	%include { polygraphicprogram/PolygraphicProgram.tom }
	%include { sl.tom }

	//provided a constructor and a set of types, returns the corresponding structure rules
	public static Vector<ThreePath> makeStructureRules(TwoPath constructor,Vector<OneCell> types) {
		Vector<ThreePath> rules =new Vector<ThreePath>();
		if(constructor.getType().isConstructor()) {
			OnePath source= constructor.getSource();
			//first the eraser
			ThreePath eraserRule=`ThreeCell(constructor.getName()+"eraser",TwoC1(constructor,makeEraser(constructor.getTarget().getName())),makeEraserRuleTarget(source),Function());
			System.out.println(eraserRule);
			rules.add(eraserRule);
			//permutation, for each type of the set of type we have to do it with the constructor at each side (left and right) of the corresponding permutation cell
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
			//at last the duplication
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


	//generates an eraser cell for a given type
	public static TwoCell makeEraser(String typename){
		return (TwoCell)`TwoCell(typename+"eraser",OneCell(typename),Id(),Function(),0);
	}

	//generates a permutation cell provided left and right inputs type
	public static TwoCell makePermutation(OneCell left, OneCell right){
		return (TwoCell)`TwoCell("permutation"+left.getName()+right.getName(),OneC0(left,right),OneC0(right,left),Function(),0);
	}

	//generates a duplication cell for a given type
	public static TwoCell makeDuplication(String typename){
		return (TwoCell)`TwoCell("duplication"+typename,OneCell(typename),OneC0(OneCell(typename),OneCell(typename)),Function(),0);
	}

	//generates the target for the eraser rule given the source of the corresponding constructor
	public static TwoPath makeEraserRuleTarget(OnePath constructorSource){
		%match (OnePath constructorSource){
			Id() -> {return `TwoId(Id()); }
			OneCell(name) -> { return makeEraser(`name); }
			OneC0(head,tail*) -> {return `TwoC0(makeEraser(head.getName()),makeEraserRuleTarget(tail));}
		}
		return `TwoId(Id());
	}

	//generates the target for a permutation rule for the case in which the constructor is at left side of the input
	public static TwoPath makeLeftPermutationRuleTarget(OnePath constructorSource,OneCell permutationType){
		OnePath[] c0array= c0ToArray((OneC0)constructorSource);
		TwoPath[] twoC0Array=new TwoPath[c0array.length];
		for (int i = 0; i < c0array.length; i++) {
			twoC0Array[i]=`TwoId(c0array[i]);
		}
		TwoPath permutation=`TwoId(permutationType);
		TwoPath target=`TwoId(Id());
		//difficult part : get the left input to the right part
		for (int j = twoC0Array.length-1; j >= 0; j--) {
			TwoPath mem=twoC0Array[j];
			twoC0Array[j]=makePermutation((OneCell)twoC0Array[j].getonePath(),(OneCell)permutation.getonePath());
			if(target==`TwoId(Id())){target=(TwoC0)fromArray(twoC0Array);}
			else{target=`TwoC1(target,(TwoC0)fromArray(twoC0Array));}
			twoC0Array[j]=mem;
		}
		return target;
	}

	//symetrical one for the other side
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
	
	//generates the target path of a duplication rule given the associated constructor
	public static TwoPath makeDuplicationRuleTarget(OnePath constructorSource){
		TwoPath target=`TwoId(Id());
		if(constructorSource.isConsOneC0()){//constructor source size >=2
			//we view its input as an array, easier to manipulate
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
				for (int j2 = j; j2 < twoC0Array.length-j; j2+=2) {
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
		else{return `makeDuplication(constructorSource.getName());}//trivial case with a constructor with a source of size<=1
	}

	//transforms a TwoPath array into a TwoC0
 	public static TwoPath fromArray(TwoPath[] array) {
 		TwoPath res = EmptyTwoC0.make();
 		for(int i = array.length; i>0;) {
 			res = ConsTwoC0.make(array[--i],res);
 		}
 		return res;
 	}


 	//transforms a one into an array of onepaths
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