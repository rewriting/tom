/*
 *
 * TOM - To One Matching Compiler
 * 
 * Copyright (c) 2000-2014, Universite de Lorraine, Inria
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
import tom.engine.adt.code.types.*;
import tom.library.sl.*;
import tom.engine.adt.tomslot.types.*;
import tom.engine.compiler.*;
import tom.engine.tools.*;
import tom.engine.TomBase;
import tom.engine.exception.TomRuntimeException;
import java.util.*;
import tom.engine.compiler.Compiler;

/**
 * Syntactic propagator
 */
public class VariadicPropagator implements IBasePropagator {

//--------------------------------------------------------	
  %include { ../../adt/tomsignature/TomSignature.tom }	
  %include { ../../../library/mapping/java/sl.tom }
  %include { constraintstrategies.tom }	
//--------------------------------------------------------

  %typeterm VariadicPropagator {
    implement { VariadicPropagator }
    is_sort(t) { ($t instanceof VariadicPropagator) }
  }

  %typeterm GeneralPurposePropagator {
    implement { GeneralPurposePropagator }
    is_sort(t) { ($t instanceof GeneralPurposePropagator) }
  }

  private Compiler compiler;  
  private GeneralPurposePropagator generalPurposePropagator; 
  private ConstraintPropagator constraintPropagator; 
 
  public VariadicPropagator(Compiler myCompiler, ConstraintPropagator myConstraintPropagator) {
    this.compiler = myCompiler;
    this.constraintPropagator = myConstraintPropagator;
    this.generalPurposePropagator = new GeneralPurposePropagator(this.compiler, this.constraintPropagator);
  }

  public Compiler getCompiler() {
    return this.compiler;
  }
 
  public GeneralPurposePropagator getGeneralPurposePropagator() {
    return this.generalPurposePropagator;
  }
 
  public ConstraintPropagator getConstraintPropagator() {
    return this.constraintPropagator;
  }
 
  public Constraint propagate(Constraint constraint) throws VisitFailure {
    return `TopDownWhenConstraint(VariadicPatternMatching(this)).visitLight(constraint);		
  }	

  %strategy VariadicPatternMatching(vp:VariadicPropagator) extends Identity() {
    visit Constraint {      
      /**
       * Detach sublists
       * 
       * Make sure that the sublists in a list are replaced by star variables
       * this is only happening when the lists and the sublists have the same name
       * 
       * conc(X*,conc(some_pattern),Y*) << t -> conc(X*,Z*,Y*) << t /\ conc(some_pattern) << Z*  
       * 
       */ 
      m@MatchConstraint[Pattern=pattern@RecordAppl[NameList=concTomName(Name(tomName)),Slots=!concSlot()]] -> {
        if(TomBase.hasTheory(`pattern,`AC())) {
          return `m;
        }
        // if this is not a list, nothing to do
        TomSymbol symb = vp.getCompiler().getSymbolTable().getSymbolFromName(`tomName);
        if(!TomBase.isListOperator(symb)) {
          return `m; 
        }
        Constraint detachedConstr = vp.getGeneralPurposePropagator().detachSublists(`m);
        if(detachedConstr != `m) {
          return detachedConstr; 
        }
      }

      /**    
       * conc(t1:S,X*:T',t2:S,Y*:T'):T' << T' g:U 
       *    -> [1] fresh_var:T' << T' g:U 
       *    /\ [2] conc():T' << T' SymbolOf(fresh_var:T')  
       *    /\ [3] NotEmpty(fresh_Var:T')  
       *    - for t1:
       *    /\ [4] t1:S << S ListHead(fresh_var:T'):T 
       *    /\ [5] fresh_var1:T' << T' ListTail(fresh_var:T'):T' 
       *    - for X*:
       *    /\ [6] begin1:T' << T' fresh_var1:T'  
       *    /\ [7] end1:T' << T' fresh_var1:T' 
       *    /\ [8] X*:T' << T' VariableHeadList(begin1:T',end1:T'):T' 
       *    /\ [9] fresh_var2:T' << T' end1:T'
       *    /\ [10] NotEmpty(fresh_Var2:T') 
       *    - for t2:
       *    /\ [11] t2:S < S ListHead(fresh_var2:T'):T 
       *    /\ [12] fresh_var3:T' << T' ListTail(fresh_var2:T'):T'  
       *    - for Y*:
       *    /\ [13] begin2:T' << T' fresh_var3:T'  
       *    /\ [14] end2:T' << T' fresh_var3:T' 
       *    /\ [15] Y*:T'<< T' VariableHeadList(begin2:T',end2:T'):T' 
       *    /\ [16] fresh_var4:T' << T' end2:T'
       * where conc: T* -> T' and S is a subtype of T and T' is a subtype of U
       * conc(t1,X*,t2,Y*) = g -> fresh_var = g /\ conc=SymbolOf(fresh_var)  
       * 
       * if the symbol was annotated, annotations are detached: 
       *        a@...b@conc(...) << t -> conc(...) << t /\ a << t /\ ... /\ b << t   
       */
      m@MatchConstraint[Pattern=t@RecordAppl(options,nameList@concTomName(name@Name(tomName),_*),slots,_),Subject=g@!SymbolOf[],AstType=aType] -> {
        if(TomBase.hasTheory(`t,`AC())) {
          return `m;
        }
        // if this is not a list, nothing to do
        TomSymbol symb = vp.getCompiler().getSymbolTable().getSymbolFromName(`tomName);
        if(!TomBase.isListOperator(symb)) {
          return `m;
        }        
        // declare fresh variable
        TomType slotType =
                  symb.getTypesToType().getDomain().getHeadconcTomType();
        BQTerm freshVariable = vp.getCompiler().getFreshVariableStar(`aType);
        Constraint freshVarDeclaration =
          `MatchConstraint(TomBase.convertFromBQVarToVar(freshVariable),g,aType);//[1]
        Constraint isSymbolConstr =
          `MatchConstraint(RecordAppl(options,nameList,concSlot(),concConstraint()),SymbolOf(freshVariable),aType);//[2]
        List<Constraint> l = new ArrayList<Constraint>();
        %match(slots) {
          concSlot() -> {
            l.add(`EmptyListConstraint(name,freshVariable));//[3] 
          }
          concSlot(_*,PairSlotAppl[Appl=appl],X*) -> {
            BQTerm newFreshVarList = vp.getCompiler().getFreshVariableStar(`aType);            

mAppl:      %match(appl) {
              // if we have a variable star
              VariableStar[] -> {                
                // if it is the last element               
                if(`X.length() == 0) {
                  // we should only assign it, without generating a loop
                  l.add(`MatchConstraint(appl,freshVariable,aType));
                } else {
                  BQTerm beginSublist = vp.getCompiler().getBeginVariableStar(`aType);
                  BQTerm endSublist = vp.getCompiler().getEndVariableStar(`aType);              
                  l.add(`MatchConstraint(TomBase.convertFromBQVarToVar(beginSublist),freshVariable,aType));//[6]
                  l.add(`MatchConstraint(TomBase.convertFromBQVarToVar(endSublist),freshVariable,aType));//[7]
                  l.add(`MatchConstraint(appl,VariableHeadList(name,beginSublist,endSublist),aType));//[8]
                  l.add(`MatchConstraint(TomBase.convertFromBQVarToVar(newFreshVarList),endSublist,aType));//[9]
                }
                break mAppl;
              }
              _ -> {
                TomType applType = vp.getCompiler().getTermTypeFromTerm(`appl);
                l.add(`Negate(EmptyListConstraint(name,freshVariable)));//[10]
                //DEBUG System.out.println("appl = " + `appl);
                //DEBUG System.out.println("applType = " + `applType);
                //DEBUG System.out.println("slotType = " + `slotType);
                // applType can be a subtype of slotType
                l.add(`MatchConstraint(appl,ListHead(name,slotType,freshVariable),applType));//[4]
                l.add(`MatchConstraint(TomBase.convertFromBQVarToVar(newFreshVarList),ListTail(name,freshVariable),aType));//[5]
                // for the last element, we should also check that the list ends
                if(`X.length() == 0) {                  
                  l.add(`EmptyListConstraint(name,newFreshVarList));
                }
              }
            }// end match
            freshVariable = newFreshVarList;
          }
        }// end match
        // fresh var declaration + add head equality condition + detached constraints
        l.add(0,vp.getConstraintPropagator().performDetach(`m));
        l.add(0,isSymbolConstr);
        l.add(0,freshVarDeclaration);
        return ASTFactory.makeAndConstraint(l);
        //return `AndConstraint(freshVarDeclaration, isSymbolConstr, vp.getConstraintPropagator().performDetach(m),l*);
      }					
    }
  }// end %strategy

}
