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
			// f(t1,...,tn) = g -> f = SymbolOf(g) /\ t1=subterm1(g) /\ ... /\ tn=subtermn(g) 
			// we can decompose only if 'g' != SymbolOf 
			m@MatchConstraint(lhs@RecordAppl(options,nameList@(name@Name(tomName),_*),slots,constraints),g@!SymbolOf[]) -> {
				// if this a list or array, nothing to do
				if(!TomConstraintCompiler.isSyntacticOperator(
						TomConstraintCompiler.getSymbolTable().getSymbolFromName(`tomName))) {return `m;}
				ConstraintList l = `concConstraint();				
				// for each slot
				%match(slots){
					concSlot(_*,PairSlotAppl(slotName,appl),_*) ->{
						l = `concConstraint(MatchConstraint(appl,Subterm(name,slotName,g)),l*);
					}
				}								
				l = l.reverse();
				// add head equality condition
				l = `concConstraint(MatchConstraint(SymbolOf(g),lhs.setSlots(concSlot())),l*);
//[pem] can we use a real A or AU operator for AndConstraint ?
//[radu] normally, yes: AU, with True() as neutral
//[radu]: TODO				
				return `AndConstraint(l);
			}		
			
			// Replace
			// z = t /\ Context( z = u ) -> z = t /\ Context( t = u )			 
			// we only apply this rule from right to left; this is not important for
			// classical pattern matching, but when anti-patterns are involved, if we replace
			// right_to_left, results are not always correct
			AndConstraint(concConstraint(X*,eq@MatchConstraint(Variable[AstName=z],t),Y*)) ->{
				Constraint toApplyOn = `AndConstraint(concConstraint(Y*));
//[pem] is TopDown as efficient as Map on lists?
//[radu] TODO: I am not sure I understand: you mean a list of extracted variables ? first extract variables, and put them in a Map ?      				
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
