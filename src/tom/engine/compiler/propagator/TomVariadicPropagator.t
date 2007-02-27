package tom.engine.compiler.propagator;

import tom.engine.adt.tomconstraint.types.*;
import tom.library.sl.*;
import tom.engine.adt.tomslot.types.*;
import tom.engine.compiler.*;

/**
 * Syntactic propagator
 */
public class TomVariadicPropagator implements TomIBasePropagator{

// --------------------------------------------------------	
	%include { adt/tomsignature/TomSignature.tom }
	%include { sl.tom }	
// --------------------------------------------------------
	
	private static freshVarCounter = 0;
	
	public Constraint propagate(Constraint constraint){
		return  (Constraint)`InnermostId(VariadicPatternMatching()).fire(constraint);
	}	

	%strategy VariadicPatternMatching() extends `Identity(){		
		visit Constraint{			
			// Decompose
			// conc(t1,X*,t2,Y*) = g -> SymbolOf(g)=conc /\ fresh_var = g 
			// /\ NotEmpty(fresh_Var) /\ t1=GetHead(fresh_var) /\ fresh_var = GetTail(fresh_var) 
			// /\ begin1 = fresh_var /\ end1 = fresh_var /\	X* = VariableHeadList(begin1,end1) /\ fresh_var = end1
			// /\ NotEmpty(fresh_Var) /\ t2=GetHead(fresh_var) /\ fresh_var = GetTail(fresh_var) 
			// /\ begin2 = fresh_var /\ end2 = fresh_var /\	Y* = VariableHeadList(begin2,end2) /\ fresh_var = end2
			m@MatchConstraint(RecordAppl(options,nameList@(name@Name(tomName),_*),
					,constraints),g) -> {
				// if we cannot decompose, stop
				%match(g) {
					SymbolOf(_) -> {return `m;}
				}
				// if this is not a list, nothing to do
				if(!TomConstraintCompiler.isListOperator(TomConstraintCompiler.getSymbolTable().
						getSymbolFromName(`tomName))) {return `m;}
				// declare fresh variable
				TomNumberList path = TomConstraintCompiler.getRootpath();
				TomName freshVarName  = `PositionName(concTomNumber(path*,NameNumber(Name("_freshList_"+ (++freshVarCounter)))));
				TomTerm freshVariable = `Variable(concOption(),freshVarName,TomConstraintCompiler.getTermTypeFromTerm(t),concConstraint());
				
				TomName freshVarName = TomConstraintCompiler.getRootPath();
				Constraint freshVarDeclaration = MatchConstraint(VariableStar(,AstName:TomName,AstType:TomType,Constraints:ConstraintList),g);
				
				ConstraintList l = `concConstraint();
				short syntacticSlotsCount = 0;
				short varSublistIndex = 0;
				// SlotList sList = `slots;
				%match(slots){
					concSlot(_*,PairSlotAppl[Appl=appl],_*)->{
						// if we have a VariableSublist
						if((`appl) instanceof VariableStar or (`appl) instanceof UnamedVariableStar){
							l = `concConstraint(MatchConstraint(appl,VariableSublist(g,syntacticSlotsCount)),l*);
						}else{							
							l = `concConstraint(NotEmptyListConstraint(name,),
									MatchConstraint(appl,Sublist(g,syntacticSlotsCount,syntacticSlotsCount),l*);
							syntacticSlotsCount++;
						}
					}
				}// end match
				
				while(!sList.isEmptyconcSlot()) {
					Slot headSlot = sList.getHeadconcSlot();
					if()
					l = `concConstraint(MatchConstraint(headSlot.getAppl(),Subterm(name.getHeadconcTomName()
							,headSlot.getSlotName(),g)),l*);					
					sList = sList.getTailconcSlot();										
				}				
				l = l.reverse();
				// add head equality condition
				// TODO + length condition
				l = `concConstraint(MatchConstraint(RecordAppl(options,nameList,concSlot(),constraints),SymbolOf(g)),l*);
				
				return `AndConstraint(l);
			}		
			
			// Merge
			// z = t /\ z = u -> z = t /\ t = u
			AndConstraint(concConstraint(X*,eq@MatchConstraint(Variable[AstName=z],t),Y*,MatchConstraint(Variable[AstName=z],u),Z*)) ->{				
				return `AndConstraint(concConstraint(X*,eq,Y*,MatchConstraint(t,u),Z*));
			}
			
			// Merge
			// z = p1 /\ p2 = z -> z = p1 /\ p2 = p1
			AndConstraint(concConstraint(X*,eq@MatchConstraint(Variable[AstName=z],p1),Y*,MatchConstraint(p2,Variable[AstName=z]),Z*)) ->{				
				return `AndConstraint(concConstraint(X*,eq,Y*,MatchConstraint(p2,p1),Z*));
			}			

			
//			// Delete
//			EqualConstraint(a,a) ->{				
//				return `TrueConstraint();
//			}
			
//			// SymbolClash
//			EqualConstraint(RecordAppl[NameList=name1],RecordAppl[NameList=name2]) -> {
//				if(`name1 != `name2) {					
//					return `FalseConstraint();
//				}
//			}
			
//			// PropagateClash
//			AndConstraint(concAnd(_*,FalseConstraint(),_*)) -> {
//				return `FalseConstraint();
//			}		
//			
//			// PropagateSuccess
//			AndConstraint(concAnd(X*,TrueConstraint(),Y*)) -> {
//				return `AndConstraint(concAnd(X*,Y*));
//			}
			
			// TODO - move in hooks ?	
			
			// clean
			AndConstraint(concConstraint()) -> {
				return `TrueConstraint();
			}
			AndConstraint(concConstraint(t)) -> {
				return `t;
			}
			AndConstraint(concConstraint(X*,AndConstraint(concConstraint(Y*)),Z*)) ->{
				return `AndConstraint(concConstraint(X*,Y*,Z*));
			}
		}
	}// end %strategy	
}