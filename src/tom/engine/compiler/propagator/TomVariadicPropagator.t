package tom.engine.compiler.propagator;

import tom.engine.adt.tomconstraint.types.*;
import tom.engine.adt.tomsignature.types.*;
import tom.engine.adt.tomtype.types.*;
import tom.engine.adt.tomterm.types.*;
import tom.engine.adt.tomname.types.*;
import tom.engine.adt.tomterm.types.tomterm.*;
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
	
	private static short freshVarCounter = 0;
	
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
			m@MatchConstraint(t@RecordAppl(options,nameList@(name@Name(tomName),_*),slots
					,constraints),g) -> {
				// if we cannot decompose, stop
				%match(g) {
					SymbolOf(_) -> {return `m;}
				}
				// if this is not a list, nothing to do
				if(!TomConstraintCompiler.isListOperator(TomConstraintCompiler.getSymbolTable().
						getSymbolFromName(`tomName))) {return `m;}
				// declare fresh variable
				TomType listType = TomConstraintCompiler.getTermTypeFromTerm(`t);
				TomTerm freshVariable = getFreshVariableStar(listType,"_freshList_");
				Constraint freshVarDeclaration = `MatchConstraint(freshVariable,g);
				
				ConstraintList l = `concConstraint();				
				// SlotList sList = `slots;
				%match(slots){
					concSlot(_*,PairSlotAppl[Appl=appl],_*)->{
						// if we have a variable
						if(((`appl) instanceof VariableStar) || ((`appl) instanceof UnamedVariableStar)){
							TomTerm beginSublist = getFreshVariableStar(listType,"_begin_");
							TomTerm endSublist = getFreshVariableStar(listType,"_end_");
							l = `concConstraint(MatchConstraint(beginSublist,freshVariable),
									MatchConstraint(endSublist,freshVariable),
									MatchConstraint(appl,VariableHeadList(beginSublist,endSublist)),
									MatchConstraint(freshVariable,endSublist),l*);
						}else{	// a term or a syntactic variable						
							l = `concConstraint(NotEmptyListConstraint(name,freshVariable),
									MatchConstraint(appl,ExpressionToTomTerm(GetHead(name,listType,freshVariable))),
									MatchConstraint(freshVariable,ExpressionToTomTerm(GetTail(name,freshVariable))),l*);
						}
					}
				}// end match			
							
				l = l.reverse();
				// add head equality condition + fresh var declaration
				l = `concConstraint(MatchConstraint(RecordAppl(options,nameList,concSlot(),constraints),SymbolOf(g)),
						freshVarDeclaration,l*);
				
				return `AndConstraint(l);
			}
					
			// Merge for star variables
			// X* = p1 /\ X* = p2 -> X* = p1 /\ X* == p2 
			AndConstraint(concConstraint(X*,eq@MatchConstraint(VariableStar[AstName=x],p1),Y*,MatchConstraint(v@VariableStar[AstName=x],p2),Z*)) ->{				
				return `AndConstraint(concConstraint(X*,eq,Y*,MatchConstraint(TestVarStar(v),p2),Z*));
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
	
	private static TomTerm getFreshVariableStar(TomType type, String namePart){
		TomNumberList path = TomConstraintCompiler.getRootpath();
		TomName freshVarName  = `PositionName(concTomNumber(path*,NameNumber(Name(namePart + (++freshVarCounter)))));
		return `VariableStar(concOption(),freshVarName,type,concConstraint());
	}
}