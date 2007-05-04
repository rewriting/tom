/*
 *
 * TOM - To One Matching Compiler
 * 
 * Copyright (c) 2000-2007, INRIA
 * Nancy, France.
 * 
 * This program is free software; you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation; either version 2 of the License, or
 * (at your option) any later version.
 * 
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 * 
 * You should have received a copy of the GNU General Public License
 * along with this program; if not, write to the Free Software
 * Foundation, Inc., 59 Temple Place, Suite 330, Boston, MA  02111-1307 USA
 * 
 * Radu Kopetz e-mail: Radu.Kopetz@loria.fr
 * Pierre-Etienne Moreau  e-mail: Pierre-Etienne.Moreau@loria.fr
 *
 **/
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
import tom.engine.exception.TomRuntimeException;
import tom.engine.TomBase;

/**
 * Syntactic propagator
 */
public class ArrayPropagator implements IBasePropagator {

//--------------------------------------------------------	
  %include { ../../adt/tomsignature/TomSignature.tom }	
  %include { ../../../library/mapping/java/sl.tom}
//--------------------------------------------------------

  public Constraint propagate(Constraint constraint) {
    try {
      return (Constraint)`InnermostId(ArrayPatternMatching()).visit(constraint);		
    } catch (tom.library.sl.VisitFailure e) {
      throw new TomRuntimeException("Unexpected strategy failure!");
    }
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
            if(!TomBase.isArrayOperator(ConstraintCompiler.getSymbolTable().
                getSymbolFromName(`tomName))) {return `m;}        
            TomType termType = ConstraintCompiler.getTermTypeFromTerm(`t);
            // declare fresh index = 0            
            TomTerm freshIndex = getFreshIndex();				
            Constraint freshIndexDeclaration = `MatchConstraint(freshIndex,TargetLanguageToTomTerm(ITL("0")));
            Constraint l = `AndConstraint();
    match:  %match(slots) {
              concSlot() -> {
                l = `AndConstraint(l*,EmptyArrayConstraint(name,g,freshIndex));
              }
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
        TomTerm freshVar = ConstraintCompiler.getFreshVariableStar(`type);
        try {
          Constraint res = (Constraint)`OnceTopDownId(ReplaceMatchConstraint(x,freshVar)).visit(toApplyOn);
          if(res != toApplyOn) {
            return `AndConstraint(X*,eq,res);
          }
        } catch (tom.library.sl.VisitFailure e) {
          throw new TomRuntimeException("Unexpected strategy failure!");
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
    return ConstraintCompiler.getBeginVariableStar(ConstraintCompiler.getIntType());
  }

  private static TomTerm getEndIndex() {
    return ConstraintCompiler.getEndVariableStar(ConstraintCompiler.getIntType());
  }

  private static TomTerm getFreshIndex() {
    return ConstraintCompiler.getFreshVariableStar(ConstraintCompiler.getIntType());    
  }
}
