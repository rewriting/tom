/*
 *
 * TOM - To One Matching Compiler
 * 
 * Copyright (c) 2000-2016, Universite de Lorraine, Inria
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

import tom.engine.adt.tomsignature.types.*;
import tom.engine.adt.tomconstraint.types.*;
import tom.engine.adt.tomterm.types.*;
import tom.engine.adt.tomtype.types.*;
import tom.engine.adt.tomname.types.*;
import tom.engine.adt.code.types.*;
import tom.library.sl.*;
import tom.engine.adt.tomslot.types.*;
import tom.engine.tools.SymbolTable;
import tom.engine.tools.ASTFactory;
import tom.engine.compiler.*;
import tom.engine.TomBase;
import java.util.*;
import tom.engine.exception.TomRuntimeException;
import tom.engine.compiler.Compiler;

/**
 * Syntactic propagator
 */
public class SyntacticPropagator implements IBasePropagator {

//--------------------------------------------------------
  %include { ../../adt/tomsignature/TomSignature.tom }
  %include { ../../../library/mapping/java/sl.tom }	
  %include { constraintstrategies.tom }	
//--------------------------------------------------------
  
  %typeterm SyntacticPropagator {
    implement { SyntacticPropagator }
    is_sort(t) { ($t instanceof SyntacticPropagator) }
  }

  private Compiler compiler; 
  private ConstraintPropagator constraintPropagator;
 
  public SyntacticPropagator(Compiler myCompiler, ConstraintPropagator myConstraintPropagator) {
    this.compiler = myCompiler;
    this.constraintPropagator = myConstraintPropagator;
  }

  public Compiler getCompiler() {
    return this.compiler;
  }
 
  public ConstraintPropagator getConstraintPropagator() {
    return this.constraintPropagator;
  }
 

  public Constraint propagate(Constraint constraint) throws VisitFailure {   
    return  `TopDownWhenConstraint(SyntacticPatternMatching(this)).visitLight(constraint);
  }	

  %strategy SyntacticPatternMatching(sp:SyntacticPropagator) extends `Identity() {
    visit Constraint {
      /**
       * Decompose
       * 
       * f1:aType[slot1:T1=t1:S1,...,slotn:Tn=tn:Sn] << aType g:gType 
       * -> freshSubject:gType << gType g:gType 
       *    /\ IsSort(aType,freshSubject) # if aType != gType
       *    /\ f1:aType << aType SymbolOf(freshSubject:gType) 
       *    /\ freshVar1:T1 << T1 subterm1_f(freshSubject:gType):T1 
       *    /\ ... 
       *    /\ freshVarn:Tn << Tn subtermn_f(freshSubject:gType):Tn 
       *    /\ t1:S1 << S1 freshVar1:T1
       *    /\ ... 
       *    /\ tn:Sn << Sn freshVarn:Tn
       * where aType is a subtype of gType and Si is a subtype of Ti for i in [i,n] 
       *
       * if f has multiple names (from f1|f2): 
       * (f1|f2)(t1,...,tn) = g 
       * -> freshSubject = g /\ ( (f1 = SymbolOf(freshSubject) /\ freshVar1=subterm1_f1(freshSubject) /\ ... /\ freshVarn=subtermn_f1(freshSubject)) 
       *       \/ (f2 = SymbolOf(freshSubject) /\ freshVar1=subterm1_f2(freshSubject) /\ ... /\ freshVarn=subtermn_f2(freshSubject)) ) 
       *  /\ t1=freshVar1 /\ ... /\ tn=freshVarn
       * 
       * if the symbol was annotated, annotations are detached:
       *        a@...b@f(...) << t -> f(...) << t /\ a << t /\ ... /\ b << t
       */
      m@MatchConstraint[Pattern=RecordAppl(options,nameList@concTomName(firstName@Name(tomName),_*),slots,_),Subject=g@!SymbolOf[],AstType=aType] -> {
        // if this a list or array, nothing to do
        if(!TomBase.isSyntacticOperator(sp.getCompiler().getSymbolTable().getSymbolFromName(`tomName))) {
          return `m; 
        }
       
        List<Constraint> lastPart = new ArrayList<Constraint>();
        ArrayList<BQTerm> freshVarList = new ArrayList<BQTerm>();
        // we build the last part only once, and we store the fresh variables we generate
        %match(slots) {
          concSlot(_*,PairSlotAppl(slotName,appl),_*) -> {
            TomType applType = sp.getCompiler().getTermTypeFromTerm(`appl);
            TomType slotType = sp.getCompiler().getSlotType(`firstName,`slotName);
            BQTerm freshVar = sp.getCompiler().getFreshVariable(slotType);
            // store the fresh variable
            freshVarList.add(freshVar);
            // build the last part
            if(applType != slotType) {
              /*
                 This test is important when the type of the argument (appl) is a subtype
                 of the type expected by the slot
                 e.g.: assume that f: A -> A and g: B -> B and b(): B and B<:A

                     %match {
                       f(x) << subject && g(b()) << g(x) -> { System.out.println("x = " +`x); }
                     }

                     If x has type A in runtime, then javac
                     generates an exception
               */
              lastPart.add(`IsSortConstraint(applType,freshVar));
            }
            lastPart.add(`MatchConstraint(appl,freshVar,applType));
          }
        }
        
        TomType gType = sp.getCompiler().getTermTypeFromTerm(`g);
        BQTerm freshSubject = sp.getCompiler().getFreshVariable(gType);
        //System.out.println("gType = " + gType);
        //System.out.println("aType = " + `aType);

        // take each symbol and build the disjunction (OrConstraintDisjunction)
        Constraint l = `OrConstraintDisjunction();
        %match(nameList) {
          concTomName(_*,name,_*) -> {
            // the 'and' conjunction for each name
            List<Constraint> andForName = new ArrayList<Constraint>();
            if(`aType != gType) {
              andForName.add(`IsSortConstraint(aType,freshSubject));
            }
            // add condition for symbolOf
            andForName.add(`MatchConstraint(RecordAppl(options,concTomName(name),concSlot(),concConstraint()),SymbolOf(freshSubject),aType));
            int counter = 0;

            BQTerm freshCastedSubject = sp.getCompiler().getFreshVariable(`aType);
            TomTerm var = TomBase.convertFromBQVarToVar(freshCastedSubject);
            andForName.add(`MatchConstraint(var,freshSubject,aType));

            // for each slot
            %match(slots) {
              concSlot(_*,PairSlotAppl(slotName,_),_*) -> {                                          
                BQTerm freshVar = freshVarList.get(counter);
                TomType slotType =
                  sp.getCompiler().getSlotType(`name,`slotName); 
                andForName.add(`MatchConstraint(TomBase.convertFromBQVarToVar(freshVar),Subterm(name,slotName,freshCastedSubject),slotType));
                counter++;
              }
            }// match slots
            l = `OrConstraintDisjunction(l*,ASTFactory.makeAndConstraint(andForName));
          }
        }
        lastPart.add(0,l);
        lastPart.add(0,`MatchConstraint(TomBase.convertFromBQVarToVar(freshSubject),g,aType));
        lastPart.add(sp.getConstraintPropagator().performDetach(`m));
        return ASTFactory.makeAndConstraint(lastPart);
      }      
    }
  }// end %strategy
}
