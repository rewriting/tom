package tom.engine.compiler.propagator;

import tom.engine.adt.tomconstraint.types.*;
import tom.library.sl.*;
import tom.engine.adt.tomslot.types.*;

/**
 * Syntactic propagator
 */
public class TomSyntacticPropagator implements TomIBasePropagator{
	
	%include { adt/tomsignature/TomSignature.tom }
	%include { sl.tom }	
	
	public Constraint propagate(Constraint constraint){
		return  (Constraint)((Strategy)`TopDown(SyntacticPatternMatching())).fire(constraint);
	}
	
	// TODO - don't forget the constraints attached to terms
	%strategy SyntacticPatternMatching() extends `Identity(){		
		visit Constraint{			
			// Decompose
			// f(t1,...,tn) = g -> SymbolOf(g)=f /\ t1=subterm1(g) /\ ... /\ tn=subtermn(g) 
			m@MatchConstraint(RecordAppl(options,name,slots,constraints),g) -> {
				// if we cannot decompose, stop
				%match(g) {
					SymbolOf(_) -> {return `m;}
				}				
				
				ConstraintList l = `concConstraint();
				SlotList sList = `slots;
				while(!sList.isEmptyconcSlot()) {
					Slot headSlot = sList.getHeadconcSlot();
					l = `concConstraint(MatchConstraint(headSlot.getAppl(),Subterm(name.getHeadconcTomName()
							,headSlot.getSlotName(),g)),l*);					
					sList = sList.getTailconcSlot();										
				}				
				l = l.reverse();
				// add head equality condition
				l = `concConstraint(MatchConstraint(RecordAppl(options,name,concSlot(),constraints),SymbolOf(g)),l*);
				
				return `AndConstraint(l);
			}		
			
			// Merge
			// z = t /\ z = u -> z = t /\ t = u
			AndConstraint(concConstraint(X*,eq@MatchConstraint(Variable[AstName=z],t),Y*,MatchConstraint(Variable[AstName=z],u),Z*)) ->{				
				return `AndConstraint(concConstraint(X*,eq,Y*,MatchConstraint(t,u),Z*));
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