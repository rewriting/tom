package tom.engine.compiler.propagator;

import tom.engine.adt.tomconstraint.types.*;
import tom.engine.adt.tomterm.types.*;
import tom.engine.adt.tomtype.types.*;
import tom.engine.adt.tomname.types.*;
import tom.library.sl.*;
import tom.engine.adt.tomslot.types.*;
import tom.engine.tools.SymbolTable;
import tom.engine.compiler.*;
import tom.engine.TomBase;
import java.util.*;

/**
 * Syntactic propagator
 */
public class TomSyntacticPropagator implements TomIBasePropagator {

//--------------------------------------------------------	
  %include { ../../adt/tomsignature/TomSignature.tom }
  %include { sl.tom }	
//--------------------------------------------------------

  public Constraint propagate(Constraint constraint) {
    return  (Constraint)`InnermostId(SyntacticPatternMatching()).fire(constraint);
  }	

  %strategy SyntacticPatternMatching() extends `Identity() {
    visit Constraint {
      /* Decompose
       * 
       * f(t1,...,tn) = g -> f1 = SymbolOf(g) /\ freshVar1=subterm1_f(g) /\ ... /\ freshVarn=subterm1_f(g) 
       * /\ t1=freshVar1 /\ ... /\ tn=freshVarn
       * 
       * if f has multiple names (from f1|f2): 
       * (f1|f2)(t1,...,tn) = g -> ( (f1 = SymbolOf(g) /\ freshVar1=subterm1_f1(g) /\ ... /\ freshVarn=subtermn_f1(g))
       * \/ (f2 = SymbolOf(g) /\ freshVar1=subterm1_f2(g) /\ ... /\ freshVarn=subtermn_f2(g)) ) /\ t1=freshVar1 /\ ... /\ tn=freshVarn
       * 
       * we can decompose only if 'g' != SymbolOf
       */
      m@MatchConstraint(RecordAppl(options,nameList@(Name(tomName),_*),slots,constraints),g@!SymbolOf[]) -> {
        // if this a list or array, nothing to do
        if(!TomBase.isSyntacticOperator(
            TomConstraintCompiler.getSymbolTable().getSymbolFromName(`tomName))) { return `m; }
        
        Constraint l = `OrConstraintDisjunction();
        Constraint lastPart = `AndConstraint();
        ArrayList<TomTerm> freshVarList = new ArrayList<TomTerm>();
        // used to build the last part only once, and not for each name
        boolean firstTime = true;
        int counter = 0;
        // for each name
// [pem] can't we duplicate the following match to consider the case concTomName(name,_*) and compute freshVarList
// [pem] and then consider the general case, without 'firstTime'
// [pem] this would make the algorithm simpler
        %match(nameList) {
          concTomName(_*,name,_*) -> {
            // the 'and' conjunction for each name
            Constraint andForName = `AndConstraint();
            // add condition for symbolOf
            andForName = `AndConstraint(MatchConstraint(RecordAppl(options,concTomName(name),concSlot(),constraints),SymbolOf(g)));
            // for each slot
            %match(slots) {
              concSlot(_*,PairSlotAppl(slotName,appl),_*) -> {
                TomTerm freshVar = null;
                if(firstTime) {
                  freshVar = TomConstraintCompiler.getFreshVariable(TomConstraintCompiler.getSlotType(`name,`slotName));
                  // store the fresh variable
                  freshVarList.add(freshVar);
                  // build the last part
                  lastPart = `AndConstraint(lastPart*,MatchConstraint(appl,freshVar));
                } else {                
                  freshVar = freshVarList.get(counter);
                }
                andForName = `AndConstraint(andForName*,MatchConstraint(freshVar,Subterm(name,slotName,g)));
                counter++;
              }
            }// match slots
            l = `OrConstraintDisjunction(l*,andForName);
            firstTime = false;
            counter = 0;
          }
        }// end match        
        return `AndConstraint(l*,lastPart*);
      }
      // Switch
      // an antimatch should be always at the end, after the match constraints
      // ex: for f(!x,x) << t -> we should generate x << t_2 /\ !x << t_1 and not 
      // !x << t_1 /\ x << t_2 because at the generation the free x should be propagated
      // and not the other one
      AndConstraint(X*,antiMatch@AntiMatchConstraint[],Y*,match@MatchConstraint[],Z*) -> {
        return `AndConstraint(X*,Y*,match,antiMatch,Z*);        
      }
      
      // an anti-pattern: just transform this into a AntiMatchConstraint and handle the @
      // a@b@!p << t -> !p << t /\ a << t /\ b << t  
      MatchConstraint(AntiTerm(term@(Variable|RecordAppl)[Constraints=constraints]),s) -> {
        Constraint assigns = `AndConstraint();
        // for each constraint
        %match(constraints) {
          concConstraint(_*,AssignTo(var),_*) -> {            
            assigns = `AndConstraint(MatchConstraint(var,s),assigns*);                                                                                                                       
          }
        }// end match
        return `AndConstraint(AntiMatchConstraint(MatchConstraint(term,s)),assigns*);
      }
      
      // Replace
      // Context1 /\ z = t /\ Context2( z = u ) -> Context1 /\ z = t /\ Context2( t = u )			 
      // we only apply this rule from left to right; this is not important for
      // classical pattern matching, but when anti-patterns are involved, if we replace
      // right_to_left, results are not always correct
      AndConstraint(X*,eq@MatchConstraint(Variable[AstName=z],t),Y*) -> {
        Constraint toApplyOn = `AndConstraint(Y*);
        Constraint res = (Constraint)`TopDown(ReplaceVariable(z,t)).fire(toApplyOn);
        if(res != toApplyOn) {
          return `AndConstraint(X*,eq,res);
        }
      }      
    }
  }// end %strategy	

  %strategy ReplaceVariable(varName:TomName, value:TomTerm) extends `Identity() {
    visit Constraint {
      MatchConstraint(Variable[AstName=name],t) -> {
        if (`name == varName) { return `MatchConstraint(value,t); }
      }
    }
  }// end strategy
}
