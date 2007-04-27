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
import tom.engine.TomBase;

/**
 * Syntactic propagator
 */
public class TomVariadicPropagator implements TomIBasePropagator {

//--------------------------------------------------------	
  %include { ../../adt/tomsignature/TomSignature.tom }	
  %include { mustrategy.tom}
//--------------------------------------------------------

  private static short beginEndCounter = 0;

  public Constraint propagate(Constraint constraint) {
    return (Constraint)`InnermostId(VariadicPatternMatching()).apply(constraint);		
  }	

  %strategy VariadicPatternMatching() extends `Identity() {
    visit Constraint {
//    [pem] I need some explanations to deeply understand the rules
//    TODO    	
      // Decompose - only if 'g' != SymbolOf 
      // conc(t1,X*,t2,Y*) = g -> conc=SymbolOf(g) /\ fresh_var = g 
      // /\ NotEmpty(fresh_Var)  /\ t1=ListHead(fresh_var) /\ fresh_var1 = ListTail(fresh_var) 
      // /\ begin1 = fresh_var1  /\ end1 = fresh_var1 /\ X* = VariableHeadList(begin1,end1) /\ fresh_var2 = end1
      // /\ NotEmpty(fresh_Var2) /\ t2=ListHead(fresh_var2) /\ fresh_var3 = ListTail(fresh_var2)  
      // /\ begin2 = fresh_var3  /\ end2 = fresh_var3 /\ Y* = VariableHeadList(begin2,end2) /\ fresh_var4 = end2
      m@MatchConstraint(t@RecordAppl(options,nameList@(name@Name(tomName),_*),slots,constraints),g@!SymbolOf[]) -> {        
        // if this is not a list, nothing to do
        if(!TomBase.isListOperator(TomConstraintCompiler.getSymbolTable().
            getSymbolFromName(`tomName))) { return `m; }
        // declare fresh variable
        TomType listType = TomConstraintCompiler.getTermTypeFromTerm(`t);
        TomTerm freshVariable = TomConstraintCompiler.getFreshVariableStar(listType);				
        Constraint freshVarDeclaration = `MatchConstraint(freshVariable,g);
        Constraint l = `AndConstraint();        
mSlots:  %match(slots) {
          concSlot() -> {
            l = `AndConstraint(l*,EmptyListConstraint(name,freshVariable));
          }
          concSlot(_*,PairSlotAppl[Appl=appl],X*) -> {
            TomTerm newFreshVarList = TomConstraintCompiler.getFreshVariableStar(listType);            
// [pem] I would have extracted the X.length==0 outside the mAppl match
// [pem] if(X.lenght==0) { if(appl.isVariableStar) ... else ... }
// [radu] it would duplicate  all the assignments for the last part           
//            // if it is the last element               
//            if(`X.length() == 0) {
//              // and if it is a varStar we should only assign it, without generating a loop
//              // (if it is unamed, we do nothing)
//              if((`appl).isVariableStar()) {
//                l = `AndConstraint(l*,MatchConstraint(appl,freshVariable));
//              } else {
//                if (!(`appl).isUnamedVariableStar()){
//                  // for the last element, we should also check that the list ends
//                  l = `AndConstraint(l*, EmptyListConstraint(name,newFreshVarList));
//                }
//              }              
//            }// end if(X.lenght==0) 
            
      mAppl:%match(appl) {
              // if we have a variable star
              (VariableStar | UnamedVariableStar)[] -> {                
                // if it is the last element               
                if(`X.length() == 0) {
                  // and if it is a varStar we should only assign it, without generating a loop
                  // (if it is unamed, we do nothing)
                  if((`appl).isVariableStar()) {
                    l = `AndConstraint(l*,MatchConstraint(appl,freshVariable));
                  }
                } else {
                  TomTerm beginSublist = getBeginVariableStar(listType);
                  TomTerm endSublist = getEndVariableStar(listType);              
                  l = `AndConstraint(l*,
                      MatchConstraint(beginSublist,freshVariable),
                      MatchConstraint(endSublist,freshVariable),             
                      MatchConstraint(appl,VariableHeadList(name,beginSublist,endSublist)),
                      MatchConstraint(newFreshVarList,endSublist));
                }
                break mAppl;
              }
              _ -> {
                l = `AndConstraint(l*,                      
                    Negate(EmptyListConstraint(name,freshVariable)),
                    MatchConstraint(appl,ListHead(name,listType,freshVariable)),
                    MatchConstraint(newFreshVarList,ListTail(name,freshVariable)));
                // for the last element, we should also check that the list ends
                if (`X.length() == 0) {                  
                  l = `AndConstraint(l*, EmptyListConstraint(name,newFreshVarList));
                }
              }
            }// end match
            freshVariable = newFreshVarList;
          }          
        }// end match
        // add head equality condition + fresh var declaration
        l = `AndConstraint(MatchConstraint(RecordAppl(options,nameList,concSlot(),constraints),SymbolOf(g)),
            freshVarDeclaration,l*);
        return l;
      }					
      /*
       * Merge for star variables (we only deal with the variables of the pattern, ignoring the introduced ones)
       * X* = p1 /\ Context( X* = p2 ) -> X* = p1 /\ Context( freshVar = p2 /\ freshVar == X* ) 
       */
      andC@AndConstraint(X*,eq@MatchConstraint(v@VariableStar[AstName=x@!PositionName[],AstType=type],p1),Y*) -> {
        Constraint toApplyOn = `AndConstraint(Y*);        
        TomTerm freshVar = TomConstraintCompiler.getFreshVariableStar(`type);
        Constraint res = (Constraint)`OnceTopDownId(ReplaceMatchConstraint(x,freshVar)).apply(toApplyOn);
        if(res != toApplyOn) {
          return `AndConstraint(X*,eq,res);
        }
      }
    }
  }// end %strategy

  %strategy ReplaceMatchConstraint(varName:TomName, freshVar:TomTerm) extends `Identity() {
    visit Constraint {
      MatchConstraint(v@VariableStar[AstName=name],p) -> {        
        if(`name == varName) {					
          return `AndConstraint(MatchConstraint(freshVar,p),MatchConstraint(TestVar(freshVar),v));
        }				  
      }
    }
  }

  private static TomTerm getBeginVariableStar(TomType type) {
    return TomConstraintCompiler.getFreshVariableStar("begin_" + (++beginEndCounter),type);
  }

  private static TomTerm getEndVariableStar(TomType type) {
    return TomConstraintCompiler.getFreshVariableStar("end_" + beginEndCounter,type);
  }
}
