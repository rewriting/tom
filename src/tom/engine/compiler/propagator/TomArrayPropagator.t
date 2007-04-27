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
public class TomArrayPropagator implements TomIBasePropagator {

//--------------------------------------------------------	
  %include { ../../adt/tomsignature/TomSignature.tom }	
  %include { sl.tom}
//--------------------------------------------------------

  private static short beginEndCounter = 0;

  public Constraint propagate(Constraint constraint) {
    return (Constraint)`InnermostId(ArrayPatternMatching()).fire(constraint);		
  }	

  %strategy ArrayPatternMatching() extends `Identity() {
    visit Constraint {
      // Decompose - only if 'g' != SymbolOf 
      // array[t1,X*,t2,Y*] = g -> array=SymbolOf(g) /\ fresh_index = 0 
      // /\ HasElement(fresh_index,g)  /\ t1=GetElement(fresh_index,g) /\ fresh_index1 = fresh_index + 1 
      // /\ begin1 = fresh_index1  /\ end1 = fresh_index1 /\ X* = VariableHeadArray(begin1,end1) /\ fresh_index2 = end1
      // /\ HasElement(fresh_index2,g) /\ t2=GetElement(fresh_index2,g)/\ fresh_index3 = fresh_index2 + 1  
      // /\ begin2 = fresh_index3  /\ end2 = fresh_index3 /\ Y* = VariableHeadArray(begin2,end2) /\ fresh_index4 = end2
      m@MatchConstraint(t@RecordAppl(options,nameList@(name@Name(tomName),_*),slots,constraints),g@!SymbolOf[]) -> {      
            // if this is not an array, nothing to do
            if(!TomBase.isArrayOperator(TomConstraintCompiler.getSymbolTable().
                getSymbolFromName(`tomName))) {return `m;}        
            TomType termType = TomConstraintCompiler.getTermTypeFromTerm(`t);
            // declare fresh index = 0            
            TomTerm freshIndex = getFreshIndex();				
            Constraint freshIndexDeclaration = `MatchConstraint(freshIndex,TargetLanguageToTomTerm(ITL("0")));
            Constraint l = `AndConstraint();
    match:  %match(slots) {
              concSlot(_*,PairSlotAppl[Appl=appl],X*) -> {
                TomTerm newFreshIndex = getFreshIndex();                
          mAppl:%match(appl){
                  // if we have a variable star
                  (VariableStar | UnamedVariableStar)[] -> {
                    // if it is the last element               
// [pem] same remark: move the test outside the match
                    if(`X.length() == 0) {
                      // and if it is a varStar we should only assign it, without generating a loop
                      // (if it is unamed, we do nothing)
                      if((`appl).isVariableStar()) {
                        l = `AndConstraint(l*,MatchConstraint(appl,ExpressionToTomTerm(
                            GetSliceArray(name,g,freshIndex,ExpressionToTomTerm(GetSize(name,g))))));
                      }
                    } else {
                      TomTerm beginIndex = getBeginIndex();
                      TomTerm endIndex = getEndIndex();
                      l = `AndConstraint(l*,
                          MatchConstraint(beginIndex,freshIndex),
                          MatchConstraint(endIndex,freshIndex),
                          MatchConstraint(appl,VariableHeadArray(name,g,beginIndex,endIndex)),
                          MatchConstraint(newFreshIndex,endIndex));     
                    }
                    break mAppl;
                  }
                  _ -> {
                    l = `AndConstraint(l*,                      
                        Negate(EmptyArrayConstraint(name,g,freshIndex)),                      
                        MatchConstraint(appl,ExpressionToTomTerm(GetElement(name,termType,g,Ref(freshIndex)))),
                        MatchConstraint(newFreshIndex,ExpressionToTomTerm(AddOne(Ref(freshIndex)))));
                    // for the last element, we should also check that the list ends
                    if(`X.length() == 0) {                  
                      l = `AndConstraint(l*, EmptyArrayConstraint(name,g,newFreshIndex));
                    }
                  }
                }// end match
                freshIndex = newFreshIndex;
              }
// [pem] same remark: move up
              concSlot() -> {
                l = `AndConstraint(l*,EmptyArrayConstraint(name,g,freshIndex));
              }
            }// end match                        
            // add head equality condition + fresh var declaration
            l = `AndConstraint(MatchConstraint(RecordAppl(options,nameList,concSlot(),constraints),SymbolOf(g)),
                freshIndexDeclaration,l*);
            return l;
          }					
          // Merge for star variables (we only deal with the variables of the pattern, ignoring the introduced ones)
          // X* = p1 /\ X* = p2 -> X* = p1 /\ freshVar = p2 /\ freshVar == X*
          andC@AndConstraint(X*,eq@MatchConstraint(v@VariableStar[AstName=x@!PositionName[],AstType=type],p1),Y*) -> {
            Constraint toApplyOn = `AndConstraint(Y*);            
            TomTerm freshVar = TomConstraintCompiler.getFreshVariableStar(`type);
            Constraint res = (Constraint)`OnceTopDownId(ReplaceMatchConstraint(x,freshVar)).fire(toApplyOn);
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

  private static TomTerm getBeginIndex() {
    return TomConstraintCompiler.getFreshVariableStar("begin_" + (++beginEndCounter),
        TomConstraintCompiler.getIntType());
  }

  private static TomTerm getEndIndex() {
    return TomConstraintCompiler.getFreshVariableStar("end_" + beginEndCounter,
        TomConstraintCompiler.getIntType());
  }

  private static TomTerm getFreshIndex() {
    return TomConstraintCompiler.getFreshVariableStar(TomConstraintCompiler.getIntType());    
  }
}
