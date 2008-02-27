/*
 *
 * TOM - To One Matching Compiler
 * 
 * Copyright (c) 2000-2008, INRIA
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
import tom.engine.compiler.Compiler;

/**
 * Syntactic propagator
 */
public class ArrayPropagator implements IBasePropagator {

//--------------------------------------------------------	
  %include { ../../adt/tomsignature/TomSignature.tom }	
  %include { ../../../library/mapping/java/sl.tom}
//--------------------------------------------------------

  public Constraint propagate(Constraint constraint) throws VisitFailure {
    return (Constraint)`TopDown(ArrayPatternMatching()).visitLight(constraint);		
  }	

  %strategy ArrayPatternMatching() extends Identity() {
    visit Constraint {
      /**
       * Detach sublists
       * 
       * Make sure that the sublists in a list are replaced by star variables - this is only happening 
       * when the lists and the sublists have the same name
       * 
       * conc(X*,conc(some_pattern),Y*) << t -> conc(X*,Z*,Y*) << t /\ conc(some_pattern) << Z*  
       * 
       */ 
      m@MatchConstraint(RecordAppl[NameList=(Name(tomName)),Slots=!concSlot()],_) -> {
        // if this is not an array, nothing to do
        if(!TomBase.isArrayOperator(Compiler.getSymbolTable().
            getSymbolFromName(`tomName))) { return `m; }
        Constraint detachedConstr = GeneralPurposePropagator.detachSublists(`m);
        // if something changed
        if (detachedConstr != `m) { return detachedConstr; }
      }      
      
      // [radu] TODO - hasElement shouldn't be generated here (same for Variadic propagator)
      // (we should generate the has-element test in the generators before generating the code for a GetElement )      
 
      // array[t1,X*,t2,Y*] = g -> freshSubj = g /\ array=SymbolOf(freshSubj) /\ fresh_index = 0 
      // /\ HasElement(fresh_index,freshSubj)  /\ t1=GetElement(fresh_index,freshSubj) /\ fresh_index1 = fresh_index + 1 
      // /\ begin1 = fresh_index1  /\ end1 = fresh_index1 /\ X* = VariableHeadArray(begin1,end1) /\ fresh_index2 = end1
      // /\ HasElement(fresh_index2,freshSubj) /\ t2=GetElement(fresh_index2,freshSubj)/\ fresh_index3 = fresh_index2 + 1  
      // /\ begin2 = fresh_index3  /\ end2 = fresh_index3 /\ Y* = VariableHeadArray(begin2,end2) /\ fresh_index4 = end2
      m@MatchConstraint(t@RecordAppl(options,nameList@(name@Name(tomName),_*),slots,_),g@!SymbolOf[]) -> {      
            // if this is not an array, nothing to do
            if(!TomBase.isArrayOperator(Compiler.getSymbolTable().
                getSymbolFromName(`tomName))) {return `m;}        
            // declare fresh variable            
            TomType termType = Compiler.getTermTypeFromTerm(`g);            
            TomTerm freshVariable = Compiler.getFreshVariableStar(termType);
            Constraint freshVarDeclaration = `MatchConstraint(freshVariable,g);
            
            // declare fresh index = 0            
            TomTerm freshIndex = getFreshIndex();				
            Constraint freshIndexDeclaration = `MatchConstraint(freshIndex,TargetLanguageToTomTerm(ITL("0")));
            Constraint l = `AndConstraint();
    match:  %match(slots) {
              concSlot() -> {
                l = `AndConstraint(l*,EmptyArrayConstraint(name,freshVariable,freshIndex));
              }
              concSlot(_*,PairSlotAppl[Appl=appl],X*) -> {
                TomTerm newFreshIndex = getFreshIndex();                
          mAppl:%match(appl){
                  // if we have a variable star
                  (VariableStar | UnamedVariableStar)[] -> {
                    // if it is the last element               
// [pem] same remark: move the test outside the match
                    if(`X.length() == 0) {
                      // we should only assign it, without generating a loop
                      l = `AndConstraint(l*,MatchConstraint(appl,ExpressionToTomTerm(
                            GetSliceArray(name,freshVariable,freshIndex,ExpressionToTomTerm(GetSize(name,freshVariable))))));
                    } else {
                      TomTerm beginIndex = getBeginIndex();
                      TomTerm endIndex = getEndIndex();
                      l = `AndConstraint(l*,
                          MatchConstraint(beginIndex,freshIndex),
                          MatchConstraint(endIndex,freshIndex),
                          MatchConstraint(appl,VariableHeadArray(name,freshVariable,beginIndex,endIndex)),
                          MatchConstraint(newFreshIndex,endIndex));     
                    }
                    break mAppl;
                  }
                  _ -> {                    
                    l = `AndConstraint(l*,                      
                        Negate(EmptyArrayConstraint(name,freshVariable,freshIndex)),                      
                        MatchConstraint(appl,ExpressionToTomTerm(GetElement(name,Compiler.getTermTypeFromTerm(appl),freshVariable,freshIndex))),
                        MatchConstraint(newFreshIndex,ExpressionToTomTerm(AddOne(freshIndex))));
                    // for the last element, we should also check that the list ends
                    if(`X.length() == 0) {                  
                      l = `AndConstraint(l*, EmptyArrayConstraint(name,freshVariable,newFreshIndex));
                    }
                  }
                }// end match
                freshIndex = newFreshIndex;
              }
            }// end match                        
            // add head equality condition + fresh var declaration + detached constraints
            l = `AndConstraint(freshVarDeclaration,MatchConstraint(RecordAppl(options,nameList,concSlot(),concConstraint()),SymbolOf(freshVariable)),
                freshIndexDeclaration,ConstraintPropagator.performDetach(m),l*);
            return l;
        }
    }
  }// end %strategy

  private static TomTerm getBeginIndex() {
    return Compiler.getBeginVariableStar(Compiler.getIntType());
  }

  private static TomTerm getEndIndex() {
    return Compiler.getEndVariableStar(Compiler.getIntType());
  }

  private static TomTerm getFreshIndex() {
    return Compiler.getFreshVariableStar(Compiler.getIntType());    
  }
}
