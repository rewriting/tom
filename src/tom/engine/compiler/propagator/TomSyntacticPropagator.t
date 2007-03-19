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
//[pem] why not ... -> f(t1,...,tn)=SymbolOf(g)? '=' is not commutative
			// f(t1,...,tn) = g -> SymbolOf(g)=f /\ t1=subterm1(g) /\ ... /\ tn=subtermn(g) 
			m@MatchConstraint(RecordAppl(options,nameList@(name@Name(tomName),_*),slots,constraints),g) -> {
//[pem] why not creating a new rule: m@MatchConstraint(_,SymbolOf(_)) -> m
				// if we cannot decompose, stop
				%match(g) {
					SymbolOf(_) -> {return `m;}
				}
				// if this a list or array, nothing to do
				if(!TomConstraintCompiler.isSyntacticOperator(
						TomConstraintCompiler.getSymbolTable().getSymbolFromName(`tomName))) {return `m;}
//[pem] may b use %match consConstraint(_*,...Slot(slotName,appl)...,_*)
				ConstraintList l = `concConstraint();
				SlotList sList = `slots;
				while(!sList.isEmptyconcSlot()) {
					Slot headSlot = sList.getHeadconcSlot();
					l = `concConstraint(MatchConstraint(headSlot.getAppl(),Subterm(name,headSlot.getSlotName(),g)),l*);					
					sList = sList.getTailconcSlot();										
				}				
				l = l.reverse();
//[pem] why not reusing the lhs and set concSlot() ?
				// add head equality condition
				l = `concConstraint(MatchConstraint(RecordAppl(options,nameList,concSlot(),constraints),SymbolOf(g)),l*)
//[pem] can we use a real A or AU operator for AndConstraint ?
				return `AndConstraint(l);
			}		
			
			// Replace
			// z = t /\ Context( z = u ) -> z = t /\ Context( t = u )			 
			AndConstraint(concConstraint(X*,eq@MatchConstraint(Variable[AstName=z],t),Y*)) ->{
//[pem] why not considering X*? Add a comment to explain why
				Constraint toApplyOn = `AndConstraint(concConstraint(Y*));
//[pem] is TopDown as efficient as Map on lists?
				Constraint res = (Constraint)`TopDown(ReplaceVariable(z,t)).fire(toApplyOn);
				if (res != toApplyOn){					
					return `AndConstraint(concConstraint(X*,eq,res));
				}
			}
			// // z = p1 /\ p2 = z -> z = p1 /\ p2 = p1 (this can occur because of the annotations of terms)
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
		}
	}// end %strategy	
	
	%strategy ReplaceVariable(varName:TomName, value:TomTerm) extends `Identity(){
		visit Constraint{
			MatchConstraint(Variable[AstName=name],t) ->{
				if (`name == varName) { return `MatchConstraint(value,t); }
			}
		}
//		visit TomTerm {
//			Variable[AstName=name] -> {
//				if (`name == varName) { return value; }  
//			}
//		}
	}
}
