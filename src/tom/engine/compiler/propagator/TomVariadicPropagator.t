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
	%include { mustrategy.tom}
// --------------------------------------------------------
	
	private static short freshVarCounter = 0;
	
	public Constraint propagate(Constraint constraint){
		return  (Constraint)`InnermostId(VariadicPatternMatching()).apply(constraint);
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
				TomTerm freshVariable = getFreshVariableStar(listType,"freshList_");
				Constraint freshVarDeclaration = `MatchConstraint(freshVariable,g);
				
				ConstraintList l = `concConstraint();				
				// SlotList sList = `slots;
				%match(slots){
					concSlot(_*,PairSlotAppl[Appl=appl],_*)->{
						// if we have a variable
						if(((`appl) instanceof VariableStar) || ((`appl) instanceof UnamedVariableStar)){
							TomTerm beginSublist = getFreshVariableStar(listType,"begin_");
							TomTerm endSublist = getFreshVariableStar(listType,"end_");
							// we put them in the inverse order in the list because later on we do a 'reverse' 
							l = `concConstraint(MatchConstraint(freshVariable,endSublist),
									MatchConstraint(appl,VariableHeadList(name,beginSublist,endSublist)),
									MatchConstraint(endSublist,freshVariable),
									MatchConstraint(beginSublist,freshVariable),l*);
						}else{	// a term or a syntactic variable						
							// we put them in the inverse order in the list because later on we do a 'reverse'
							l = `concConstraint(MatchConstraint(freshVariable,ExpressionToTomTerm(GetTail(name,freshVariable))),
									MatchConstraint(appl,ExpressionToTomTerm(GetHead(name,listType,freshVariable))),
									NotEmptyListConstraint(name,freshVariable),l*);
						}
					}
				}// end match
				l = l.reverse();
				// add head equality condition + fresh var declaration
				l = `concConstraint(MatchConstraint(RecordAppl(options,nameList,concSlot(),constraints),SymbolOf(g)),
						freshVarDeclaration,l*);
				
				return `AndConstraint(l);
			}
					
			// Merge for star variables (we only deal with the variables of the pattern, ignoring the introduced ones) 
			// X* = p1 /\ X* = p2 -> X* = p1 /\ freshVar = p2 /\ freshVar == X*					
//			andC@AndConstraint(concConstraint(X*,eq@MatchConstraint(VariableStar[AstName=x@!PositionName[]],p1),Y*,MatchConstraint(v@VariableStar[AstName=x],p2),Z*)) ->{
//				TomNumberList path = TomConstraintCompiler.getRootpath();
//				TomName freshVarName  = `PositionName(concTomNumber(path*,NameNumber(Name("freshVar_" + (++freshVarCounter)))));
//				TomTerm freshVar = `v.setAstName(freshVarName);
//				return `AndConstraint(concConstraint(X*,eq,Y*,MatchConstraint(freshVar,p2),MatchConstraint(TestVarStar(freshVar),v),Z*));
//			}
					
			// Merge for star variables (we only deal with the variables of the pattern, ignoring the introduced ones)
			// X* = p1 /\ X* = p2 -> X* = p1 /\ freshVar = p2 /\ freshVar == X*
			andC@AndConstraint(concConstraint(X*,eq@MatchConstraint(v@VariableStar[AstName=x@!PositionName[]],p1),Y*)) ->{
				Constraint toApplyOn = `AndConstraint(concConstraint(Y*));
				TomNumberList path = TomConstraintCompiler.getRootpath();
				TomName freshVarName  = `PositionName(concTomNumber(path*,NameNumber(Name("freshVar_" + (++freshVarCounter)))));
				TomTerm freshVar = `v.setAstName(freshVarName);
				Constraint res = (Constraint)`OnceTopDownId(ReplaceMatchConstraint(x,freshVar)).apply(toApplyOn);
				if (res != toApplyOn){					
					return `AndConstraint(concConstraint(X*,eq,res));
				}
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
	
	%strategy ReplaceMatchConstraint(varName:TomName, freshVar:TomTerm) extends `Identity(){
		visit Constraint {
			MatchConstraint(v@VariableStar[AstName=name],p) -> {
				if (`name == varName) {					
					return `AndConstraint(concConstraint(MatchConstraint(freshVar,p),MatchConstraint(TestVarStar(freshVar),v)));
				}				  
			}
		}
	}
	
	private static TomTerm getFreshVariableStar(TomType type, String namePart){
		TomNumberList path = TomConstraintCompiler.getRootpath();
		TomName freshVarName  = `PositionName(concTomNumber(path*,NameNumber(Name(namePart + (++freshVarCounter)))));
		return `VariableStar(concOption(),freshVarName,type,concConstraint());
	}
}