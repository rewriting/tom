package tom.engine.compiler.propagator;

import tom.engine.adt.tomconstraint.types.*;
import tom.engine.adt.tomterm.types.*;
import tom.engine.adt.tomname.types.*;
import tom.library.sl.*;
import tom.engine.adt.tomslot.types.*;
import tom.engine.tools.SymbolTable;
import tom.engine.compiler.*;
import tom.engine.TomBase;

/**
 * Syntactic propagator
 */
public class TomSyntacticPropagator implements TomIBasePropagator{

//--------------------------------------------------------	
  %include { adt/tomsignature/TomSignature.tom }
  %include { sl.tom }	
//--------------------------------------------------------

  public Constraint propagate(Constraint constraint){
    return  (Constraint)`InnermostId(SyntacticPatternMatching()).fire(constraint);
  }	

  %strategy SyntacticPatternMatching() extends `Identity(){		
    visit Constraint{			
      // Decompose
      // f(t1,...,tn) = g -> f = SymbolOf(g) /\ freshVar1=subterm1(g) /\ t1=freshVar1 /\ ... /\ freshVarn=subtermn(g) /\ tn=freshVarn
      //
      // we can decompose only if 'g' != SymbolOf            
      m@MatchConstraint(lhs@RecordAppl(options,nameList@(name@Name(tomName)),slots,constraints),g@!SymbolOf[]) -> {
        // if this a list or array, nothing to do
        if(!TomBase.isSyntacticOperator(
            TomConstraintCompiler.getSymbolTable().getSymbolFromName(`tomName))) {return `m;}
        Constraint l = `AndConstraint();
        // for each slot
        %match(slots){
          concSlot(_*,PairSlotAppl(slotName,appl),_*) ->{                        
            TomTerm freshVar = TomConstraintCompiler.getFreshVariable(TomConstraintCompiler.getSlotType(`name,`slotName));            
            l = `AndConstraint(l*,
                MatchConstraint(freshVar,Subterm(name,slotName,g)),
                MatchConstraint(appl,freshVar));
          }
        }
        // add head equality condition
        l = `AndConstraint(MatchConstraint(lhs.setSlots(concSlot()),SymbolOf(g)),l*);			
        return l;
      }	
      
      // Decompose
      // if f has multiple names (from f1|f2):
      // (f1|f2)(t1,...,tn) = g1|g2 -> ( f1 = SymbolOf(g) /\ freshVar1=subterm1_f1(g) or f2 = SymbolOf(g) /\ freshVar1=subterm1_f2(g1) ) 
      // /\ t1=freshVar1 /\ ... /\ freshVarn=subtermn(g) /\ tn=freshVarn
      //
      // we can decompose only if 'g' != SymbolOf      
      m@MatchConstraint(lhs@RecordAppl(options,nameList@(Name(tomName),_,_*),slots,constraints),g@!SymbolOf[]) -> {
        // if this a list or array, nothing to do
        if(!TomBase.isSyntacticOperator(
            TomConstraintCompiler.getSymbolTable().getSymbolFromName(`tomName))) {return `m;}
        Constraint l = `OrConstraintDisjunction();
        TomName slotName = null;
        TomTerm subject = null;
        //take the first slot
        %match(slots){
          concSlot(PairSlotAppl(slotName,appl),_*) ->{
            slotName = `slotName;
            subject = `appl;
          }
        }
        TomTerm freshVar = null;
        // add condition for each name
        %match(nameList){
          concTomName(_*,name,_*) ->{              
            freshVar = TomConstraintCompiler.getFreshVariable(TomConstraintCompiler.getSlotType(`name,`slotName));
            l = `OrConstraintDisjunction(l*,AndConstraint(
                MatchConstraint(RecordAppl(options,concTomName(name),concSlot(),constraints),SymbolOf(g)),
                MatchConstraint(freshVar,Subterm(name,slotName,g))));              
          }
        }// end match
        
        // add equality with the subject
        l = `AndConstraint(l*,MatchConstraint(subject,freshVar));
        
        // now continue with each of the other slots
        %match(slots){
          concSlot(_,_*,PairSlotAppl(slotName,appl),_*) ->{                        
            freshVar = TomConstraintCompiler.getFreshVariable(TomConstraintCompiler.getSlotType(`Name(tomName),`slotName));            
            l = `AndConstraint(l*,
                MatchConstraint(freshVar,Subterm(name,slotName,g)),
                MatchConstraint(appl,freshVar));
          }
        }
        return l;
      }
      
      // Replace
      // Context1 /\ z = t /\ Context2( z = u ) -> z = t /\ Context( t = u )			 
      // we only apply this rule from right to left; this is not important for
      // classical pattern matching, but when anti-patterns are involved, if we replace
      // right_to_left, results are not always correct
      AndConstraint(X*,eq@MatchConstraint(Variable[AstName=z],t),Y*) ->{
        Constraint toApplyOn = `AndConstraint(Y*);
        Constraint res = (Constraint)`TopDown(ReplaceVariable(z,t)).fire(toApplyOn);
        if (res != toApplyOn){					
          return `AndConstraint(X*,eq,res);
        }
      }
      // // z = p1 /\ p2 = z -> z = p1 /\ p2 = p1 (this can occur because of the annotations of terms)
//    // Delete
//    EqualConstraint(a,a) ->{				
//    return `TrueConstraint();
//    }

//    // SymbolClash
//    EqualConstraint(RecordAppl[NameList=name1],RecordAppl[NameList=name2]) -> {
//    if(`name1 != `name2) {					
//    return `FalseConstraint();
//    }
//    }

//    // PropagateClash
//    AndConstraint(concAnd(_*,FalseConstraint(),_*)) -> {
//    return `FalseConstraint();
//    }		

//    // PropagateSuccess
//    AndConstraint(concAnd(X*,TrueConstraint(),Y*)) -> {
//    return `AndConstraint(concAnd(X*,Y*));
//    }
    }
  }// end %strategy	

  %strategy ReplaceVariable(varName:TomName, value:TomTerm) extends `Identity(){
    visit Constraint{
      MatchConstraint(Variable[AstName=name],t) ->{
        if (`name == varName) { return `MatchConstraint(value,t); }
      }
    }
  }// end strategy
}
