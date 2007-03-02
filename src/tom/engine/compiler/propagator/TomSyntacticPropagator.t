package tom.engine.compiler.propagator;

import tom.engine.adt.tomconstraint.types.*;
import tom.library.sl.*;
import tom.engine.adt.tomslot.types.*;
import tom.engine.tools.SymbolTable;
import tom.engine.compiler.*;
/**
 * Syntactic propagator
 */
public class TomSyntacticPropagator implements TomIBasePropagator{

// --------------------------------------------------------	
	%include { adt/tomsignature/TomSignature.tom }
	%include { sl.tom }	
// --------------------------------------------------------
	
	public Constraint propagate(Constraint constraint){
		return  (Constraint)`InnermostId(SyntacticPatternMatching()).fire(constraint);
	}	

	%strategy SyntacticPatternMatching() extends `Identity(){		
		visit Constraint{			
			// Decompose
			// f(t1,...,tn) = g -> SymbolOf(g)=f /\ t1=subterm1(g) /\ ... /\ tn=subtermn(g) 
			m@MatchConstraint(RecordAppl(options,nameList@(name@Name(tomName),_*),slots,constraints),g) -> {
				// if we cannot decompose, stop
				%match(g) {
					SymbolOf(_) -> {return `m;}
				}								
				// if this a list or array, nothing to do
				if(!TomConstraintCompiler.isSyntacticOperator(
						TomConstraintCompiler.getSymbolTable().getSymbolFromName(`tomName))) {return `m;}
				
				ConstraintList l = `concConstraint();
				SlotList sList = `slots;
				while(!sList.isEmptyconcSlot()) {
					Slot headSlot = sList.getHeadconcSlot();
					l = `concConstraint(MatchConstraint(headSlot.getAppl(),Subterm(name,headSlot.getSlotName(),g)),l*);					
					sList = sList.getTailconcSlot();										
				}				
				l = l.reverse();
				// add head equality condition
				l = `concConstraint(MatchConstraint(RecordAppl(options,nameList,concSlot(),constraints),SymbolOf(g)),l*);
				
				return `AndConstraint(l);
			}		
			
			// Merge 1
			// z = t /\ z = u -> z = t /\ t = u
//			AndConstraint(concConstraint(X*,eq@MatchConstraint(Variable[AstName=z],t),Y*,MatchConstraint(Variable[AstName=z],u),Z*)) ->{				
//				return `AndConstraint(concConstraint(X*,eq,Y*,MatchConstraint(t,u),Z*));
//			}
			AndConstraint(concConstraint(X*,eq@MatchConstraint(Variable[AstName=z],t),Y*)) ->{
				Constraint toApplyOn = `AndConstraint(concConstraint(Y*));
				Constraint res = (Constraint)`TopDown(ReplaceVariable(z,t)).fire(toApplyOn);
				if (res != toApplyOn){					
					return `AndConstraint(concConstraint(X*,eq,res));
				}
			}
			
			
			// Merge 2 (this can occur because of the annotations of terms)
			// z = p1 /\ p2 = z -> z = p1 /\ p2 = p1
//			AndConstraint(concConstraint(X*,eq@MatchConstraint(Variable[AstName=z],p1),Y*,MatchConstraint(p2,Variable[AstName=z]),Z*)) ->{				
//				return `AndConstraint(concConstraint(X*,eq,Y*,MatchConstraint(p2,p1),Z*));
//			}
			
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
	
	%strategy ReplaceVariable(varName:TomName, value:TomTerm) extends `Identity(){
		visit TomTerm {
			Variable[AstName=name] -> {
				if (`name == varName) { return value; }  
			}
		}
	}
}