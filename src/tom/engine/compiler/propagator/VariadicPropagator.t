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
import tom.engine.TomBase;
import tom.engine.exception.TomRuntimeException;
import java.util.ArrayList;

/**
 * Syntactic propagator
 */
public class VariadicPropagator implements IBasePropagator {

//--------------------------------------------------------	
  %include { ../../adt/tomsignature/TomSignature.tom }	
  %include { ../../../library/mapping/java/sl.tom}
//--------------------------------------------------------

  public Constraint propagate(Constraint constraint) throws VisitFailure {
    return (Constraint)`InnermostId(VariadicPatternMatching()).visit(constraint);		
  }	

  %strategy VariadicPatternMatching() extends `Identity() {
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
      m@MatchConstraint(RecordAppl[NameList=(Name(tomName)),Slots=slots@!concSlot()],g) -> {
        // if this is not a list, nothing to do
        if(!TomBase.isListOperator(ConstraintCompiler.getSymbolTable().
            getSymbolFromName(`tomName))) { return `m; }
        Constraint detachedConstr = GeneralPurposePropagator.detachSublists(`m);
        if (detachedConstr != `m) { return detachedConstr; }
      }
      
      /**    
       * 
       * [pem] I need some explanations to deeply understand the rules
       *    //TODO    	
       * Decompose - only if 'g' != SymbolOf
       *  
       * conc(t1,X*,t2,Y*) = g -> conc=SymbolOf(g) /\ fresh_var = g 
       * /\ NotEmpty(fresh_Var)  /\ t1=ListHead(fresh_var) /\ fresh_var1 = ListTail(fresh_var) 
       * /\ begin1 = fresh_var1  /\ end1 = fresh_var1 /\ X* = VariableHeadList(begin1,end1) /\ fresh_var2 = end1
       * /\ NotEmpty(fresh_Var2) /\ t2=ListHead(fresh_var2) /\ fresh_var3 = ListTail(fresh_var2)  
       * /\ begin2 = fresh_var3  /\ end2 = fresh_var3 /\ Y* = VariableHeadList(begin2,end2) /\ fresh_var4 = end2
       * 
       * 
       * if the symbol was annotated, annotations are detached: 
       *        a@...b@conc(...) << t -> conc(...) << t /\ a << t /\ ... /\ b << t   
       */
      m@MatchConstraint(t@RecordAppl(options,nameList@(name@Name(tomName),_*),slots,constraints),g@!SymbolOf[]) -> {
        // if this is not a list, nothing to do
        if(!TomBase.isListOperator(ConstraintCompiler.getSymbolTable().
            getSymbolFromName(`tomName))) { return `m; }        
        // declare fresh variable
        TomType listType = ConstraintCompiler.getTermTypeFromTerm(`t);
        TomTerm freshVariable = ConstraintCompiler.getFreshVariableStar(listType);				
        Constraint freshVarDeclaration = `MatchConstraint(freshVariable,g);
        Constraint l = `AndConstraint();        
mSlots:  %match(slots) {
          concSlot() -> {
            l = `AndConstraint(l*,EmptyListConstraint(name,freshVariable));
          }
          concSlot(_*,PairSlotAppl[Appl=appl],X*) -> {
            TomTerm newFreshVarList = ConstraintCompiler.getFreshVariableStar(listType);            
      mAppl:%match(appl) {
              // if we have a variable star
              (VariableStar | UnamedVariableStar)[] -> {                
                // if it is the last element               
                if(`X.length() == 0) {
                  // we should only assign it, without generating a loop
                  l = `AndConstraint(l*,MatchConstraint(appl,freshVariable));
                } else {
                  TomTerm beginSublist = ConstraintCompiler.getBeginVariableStar(listType);
                  TomTerm endSublist = ConstraintCompiler.getEndVariableStar(listType);              
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
                    MatchConstraint(appl,ListHead(name,ConstraintCompiler.getTermTypeFromTerm(appl),freshVariable)),
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
        // add head equality condition + fresh var declaration + detached constraints        
        l = `AndConstraint(MatchConstraint(RecordAppl(options,nameList,concSlot(),concConstraint()),SymbolOf(g)),
            freshVarDeclaration,ConstraintPropagator.performDetach(m),l*);
        return l;
      }					
    }
  }// end %strategy

}
