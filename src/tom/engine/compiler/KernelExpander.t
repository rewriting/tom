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
 * Pierre-Etienne Moreau  e-mail: Pierre-Etienne.Moreau@loria.fr
 *
 **/

package tom.engine.compiler;

import java.util.*;
import java.util.HashSet;
import java.util.Iterator;
import java.util.logging.Level;
import java.util.logging.Logger;

import tom.engine.TomBase;
import tom.engine.TomMessage;
import tom.engine.exception.TomRuntimeException;

import tom.engine.adt.tomsignature.*;
import tom.engine.adt.tomconstraint.types.*;
import tom.engine.adt.tomdeclaration.types.*;
import tom.engine.adt.tomexpression.types.*;
import tom.engine.adt.tominstruction.types.*;
import tom.engine.adt.tomname.types.*;
import tom.engine.adt.tomoption.types.*;
import tom.engine.adt.tomsignature.types.*;
import tom.engine.adt.tomterm.types.*;
import tom.engine.adt.tomslot.types.*;
import tom.engine.adt.tomtype.types.*;

import tom.engine.tools.SymbolTable;
import tom.engine.tools.ASTFactory;

import tom.library.sl.*;

public class KernelExpander {
  %include { ../../library/mapping/java/sl.tom}
  %include { ../../library/mapping/java/util/types/Collection.tom}

  %typeterm KernelExpander {
    implement { KernelExpander }
    is_sort(t) { t instanceof KernelExpander }
  }

  %op Strategy ChoiceTopDown(s1:Strategy) {
    make(v) { `mu(MuVar("x"),ChoiceId(v,All(MuVar("x")))) }
  }

  %op Strategy TopDownStop(s1:Strategy) {
    make(v) { `mu(MuVar("x"),Choice(v,All(MuVar("x")))) }
  }

  private SymbolTable symbolTable;

  public KernelExpander() {
    super();
  }

  public void setSymbolTable(SymbolTable symbolTable) {
    this.symbolTable = symbolTable;
  }

  private SymbolTable getSymbolTable() {
    return symbolTable;
  }

  protected TomSymbol getSymbolFromName(String tomName) {
    return TomBase.getSymbolFromName(tomName, getSymbolTable());
  }

  protected TomSymbol getSymbolFromType(TomType tomType) {
    return TomBase.getSymbolFromType(tomType, getSymbolTable());
  }
  // ------------------------------------------------------------
  %include { ../adt/tomsignature/TomSignature.tom }
  // ------------------------------------------------------------

  /*
   * The "expandVariable" phase expands RecordAppl into Variable
   * we focus on
   * - Match
   *
   * The types of subjects are inferred from the patterns
   *
   * Variable and TermAppl are expanded in the TomTerm case
   */

  protected tom.library.sl.Visitable expandType(tom.library.sl.Visitable subject) {
    try {
      return `TopDown(expandType(this)).visit(subject);
    } catch(tom.library.sl.VisitFailure e) {
      throw new TomRuntimeException("expandType: failure on " + subject);
    }
  }
  %strategy expandType(expander:KernelExpander) extends `Identity() {
    visit TomType {
      subject@TomTypeAlone(tomType) -> {
        TomType type = expander.getType(`tomType);
        if(type != null) {
          return type;
        } else {
          return `subject; // useful for TomTypeAlone("unknown type")
        }
      }
    }
  }

  %strategy replace_expandVariable(contextType:TomType,expander:KernelExpander) extends `Fail() {

    visit Option {
      subject@OriginTracking[] -> { return `subject; }
    }

    visit TargetLanguage {
      subject@TL[] -> { return `subject; }
      subject@ITL[] -> { return `subject; }
      subject@Comment[] -> { return `subject; }
    }

    visit TomType {
      subject@TomTypeAlone(tomType) -> {
        TomType type = expander.getType(`tomType);
        if(type != null) {
          return type;
        } else {
          return `subject; // useful for TomTypeAlone("unknown type")
        }
      }
    }

    visit TomVisit {
      VisitTerm(type,constraintInstructionList,options) -> {
        ArrayList<Constraint> matchConstraints = new ArrayList<Constraint>();
        `TopDown(CollectMatchConstraints(matchConstraints)).visitLight(`constraintInstructionList);
        TomType newType = (TomType)`expander.expandVariable(contextType,`type);
        ConstraintInstructionList newConstraintInstructionList = `concConstraintInstruction();
        %match(constraintInstructionList) {
          concConstraintInstruction(_*,ConstraintInstruction(constraint,action,optionConstraint),_*) -> {
            /*
             * Try to guess types for tomSubjectList
             */
            ArrayList<TomTerm> newPatternList = new ArrayList<TomTerm>();
            Constraint newConstraint = (Constraint)`TopDown(expandConstraint(newType,newPatternList,matchConstraints,expander)).visitLight(`constraint);
            Instruction newAction = expandAction(`action,ASTFactory.makeList(newPatternList),expander);
            newConstraintInstructionList = `concConstraintInstruction(newConstraintInstructionList*,ConstraintInstruction(newConstraint,newAction,optionConstraint));
          }
        }        
        return `VisitTerm(newType, newConstraintInstructionList,options);   
      }
    }

    visit Instruction {
      /*
       * Expansion of a Match construct
       * to add types in subjects
       * to add types in variables of patterns and rhs
       */
      Match(constraintInstructionList, option) -> {
        ArrayList<Constraint> matchConstraints = new ArrayList<Constraint>();
        `TopDown(CollectMatchConstraints(matchConstraints)).visitLight(`constraintInstructionList);
        ConstraintInstructionList newConstraintInstructionList = `concConstraintInstruction();
        %match(constraintInstructionList) {
          concConstraintInstruction(_*,ConstraintInstruction(constraint,action,optionConstraint),_*) -> {
            /*
             * Try to guess types for tomSubjectList
             */
            ArrayList<TomTerm> newPatternList = new ArrayList<TomTerm>();
            Constraint newConstraint = (Constraint)`TopDown(expandConstraint(contextType,newPatternList,matchConstraints,expander)).visitLight(`constraint);
            Instruction newAction = expandAction(`action,ASTFactory.makeList(newPatternList),expander);
            newConstraintInstructionList = `concConstraintInstruction(newConstraintInstructionList*,ConstraintInstruction(newConstraint,newAction,optionConstraint));
          }
        }        
        return `Match(newConstraintInstructionList,option);      
      }      
    }
    
    visit TomTerm {
      RecordAppl[Option=option,NameList=nameList@(Name(tomName),_*),Slots=slotList,Constraints=constraints] -> {
        TomSymbol tomSymbol = null;
        if(`tomName.equals("")) {
          try {
            tomSymbol = expander.getSymbolFromType(contextType);
            if(tomSymbol==null) {
              throw new TomRuntimeException("No symbol found for type '" + contextType + "'");
            }
            `nameList = `concTomName(tomSymbol.getAstName());
          } catch(UnsupportedOperationException e) {
            // contextType has no AstType slot
            tomSymbol = null;
          }
        } else {
          tomSymbol = expander.getSymbolFromName(`tomName);
        }

        if(tomSymbol != null) {
          SlotList subterm = expander.expandVariableList(tomSymbol, `slotList);
          ConstraintList newConstraints = (ConstraintList)expander.expandVariable(TomBase.getSymbolCodomain(tomSymbol),`constraints);
          return `RecordAppl(option,nameList,subterm,newConstraints);
        } else {
          %match(contextType) {
            type@Type[] -> {
              SlotList subterm = expander.expandVariableList(`emptySymbol(), `slotList);
              ConstraintList newConstraints = (ConstraintList)expander.expandVariable(`type,`constraints);
              return `RecordAppl(option,nameList,subterm,newConstraints);
            }

            _ -> {
              // do nothing
              //System.out.println("contextType = " + contextType);
              //System.out.println("subject        = " + subject);
            }
          }
        }
      }

      var@(Variable|UnamedVariable)[AstType=TomTypeAlone(tomType),Constraints=constraints] -> {
        TomType localType = expander.getType(`tomType);
        //System.out.println("localType = " + localType);
        if(localType != null) {
          // The variable has already a known type
          return `var.setAstType(localType);
        }

        //System.out.println("contextType = " + contextType);
        %match(contextType) {
          ctype@Type[] -> {
            ConstraintList newConstraints = (ConstraintList)expander.expandVariable(`ctype,`constraints);
            TomTerm newVar = `var.setAstType(`ctype);
            //System.out.println("newVar = " + newVar);
            return newVar.setConstraints(newConstraints);
          }
        }
      }
    }
   }
  
  private static Instruction expandAction(Instruction action, TomList newPatternList, KernelExpander expander) { 
    //  build the list of variables that occur in the lhs
    HashSet set = new HashSet();
    TomBase.collectVariable(set,newPatternList);
    TomList varList = ASTFactory.makeList(set);
    Instruction newAction = (Instruction)expander.replaceInstantiatedVariable(`varList,`action);
    //System.out.println("newAction1 = " + newAction);
    newAction = (Instruction)`expander.expandVariable(`EmptyType(),`newAction);
    return newAction;
  }
  
  /**
   * Try to guess the type for the subjects
   */
  %strategy expandConstraint(TomType contextType, Collection newPatternList, Collection matchConstraints, KernelExpander expander) extends Identity(){
    visit Constraint {
      MatchConstraint(pattern, subject) -> {
        TomTerm newSubject = null;
        TomType newSubjectType = null;        
        %match(subject) {
          (Variable|VariableStar)(variableOption,astName@Name(name),tomType,constraints) -> {
            TomTerm newVariable = null;
            // tomType may be a TomTypeAlone or a type from an expanded variable
            String type = TomBase.getTomType(`tomType);
            //System.out.println("match type = " + type);
            if(expander.getType(`type) == null) {
              /* the subject is a variable with an unknown type */
              newSubjectType = expander.guessSubjectType(`subject,matchConstraints);
              if( newSubjectType != null ) {
                newVariable = `Variable(variableOption,astName,newSubjectType,constraints);
              } else {
                throw new TomRuntimeException("No symbol found for name '" + `name + "'");
              }
            } else {
              newVariable = `subject;
            }
            if(newVariable == null) {
              throw new TomRuntimeException("Type cannot be guessed for '" + `subject + "'");
            } else {
              newSubject = newVariable;
              newSubjectType = newVariable.getAstType();
            }                  
          }

          t@(TermAppl|RecordAppl)[NameList=concTomName(Name(name),_*)] -> {
            TomSymbol symbol = expander.getSymbolFromName(`name);
            TomType type = null;
            if(symbol!=null) {
              type = TomBase.getSymbolCodomain(symbol);
            } else {
              // unknown function call
              type = expander.guessSubjectType(`subject,matchConstraints);
            }
            if( type != null ) {
              newSubject = `BuildReducedTerm(t,type);
            } else {
              throw new TomRuntimeException("No symbol found for name '" + `name + "'");
            }
            newSubjectType = type;                    
          }
          
          TomTypeToTomTerm(type) -> {
            newSubject = `Variable(concOption(),Name("tom__arg"),type,concConstraint());
            newSubjectType = `type;
          }
          
        } // end match subject        
        newSubjectType = (TomType)expander.expandVariable(contextType,newSubjectType);
        newSubject = (TomTerm)expander.expandVariable(newSubjectType, newSubject);
        TomTerm newPattern = (TomTerm)expander.expandVariable(newSubjectType, `pattern);
        newPatternList.add(newPattern);
        return `MatchConstraint(newPattern,newSubject);
      }
    }
  } 

  private TomType guessSubjectType(TomTerm subject, Collection matchConstraints){    
    for(Object constr:matchConstraints){
      %match(constr,TomTerm subject){
        MatchConstraint(pattern,s),s -> {
          TomTerm patt = `pattern;
          %match(pattern) {
            AntiTerm(p) -> { patt = `p; }
          }
          %match(patt) {
            (TermAppl|RecordAppl|XMLAppl)[NameList=concTomName(Name(name),_*)] -> {        
                TomSymbol symbol = null;
                symbol = getSymbolFromName(`name);
                // System.out.println("name = " + `name);
                if( symbol != null ) {
                  return TomBase.getSymbolCodomain(symbol);
                }
             }      
          }
        }
      }
    }// for    
    return null;
  }

    protected tom.library.sl.Visitable expandVariable(TomType contextType, tom.library.sl.Visitable subject) {
      if(contextType == null) {
        throw new TomRuntimeException("expandVariable: null contextType");
      }
      try {
        //System.out.println("expandVariable: " + contextType);
        //System.out.println("expandVariable subject: " + subject);
        tom.library.sl.Visitable res = `TopDownStop(replace_expandVariable(contextType,this)).visit(subject);
        //System.out.println("res: " + res);
        return res;
      } catch(tom.library.sl.VisitFailure e) {
        throw new TomRuntimeException("expandVariable: failure on " + subject);
        //return subject;
      }
    }

    /*
     * perform type inference of subterms (subtermList)
     * under a given operator (symbol)
     */
    private SlotList expandVariableList(TomSymbol symbol, SlotList subtermList) {
      if(symbol == null) {
        throw new TomRuntimeException("expandVariableList: null symbol");
      }

      if(subtermList.isEmptyconcSlot()) {
        return `concSlot();
      }

      //System.out.println("symbol = " + symbol.getastname());
      %match(symbol, subtermList) {
        emptySymbol(), concSlot(PairSlotAppl(slotName,slotAppl),tail*) -> {
          /*
           * if the top symbol is unknown, the subterms
           * are expanded in an empty context
           */
          SlotList sl = expandVariableList(symbol,`tail);
          return `concSlot(PairSlotAppl(slotName,(TomTerm)expandVariable(EmptyType(),slotAppl)),sl*);
        }

        symb@Symbol[TypesToType=TypesToType(typelist,codomain)],
          concSlot(PairSlotAppl(slotName,slotAppl),tail*) -> {
            //System.out.println("codomain = " + `codomain);
            // process a list of subterms and a list of types
            if(TomBase.isListOperator(`symb) || TomBase.isArrayOperator(`symb)) {
              /*
               * todo:
               * when the symbol is an associative operator,
               * the signature has the form: list conc( element* )
               * the list of types is reduced to the singleton { element }
               *
               * consider a pattern: conc(e1*,x,e2*,y,e3*)
               *  assign the type "element" to each subterm: x and y
               *  assign the type "list" to each subtermlist: e1*,e2* and e3*
               */

              //System.out.println("listoperator: " + `symb);
              //System.out.println("subtermlist: " + subtermList);
              //System.out.println("slotAppl: " + `slotAppl);

              %match(slotAppl) {
                VariableStar[Option=option,AstName=name,Constraints=constraints] -> {
                  ConstraintList newconstraints = (ConstraintList)expandVariable(`codomain,`constraints);
                  SlotList sl = expandVariableList(symbol,`tail);
                  return `concSlot(PairSlotAppl(slotName,VariableStar(option,name,codomain,newconstraints)),sl*);
                }

                UnamedVariableStar[Option=option,Constraints=constraints] -> {
                  ConstraintList newconstraints = (ConstraintList)expandVariable(`codomain,`constraints);
                  SlotList sl = expandVariableList(symbol,`tail);
                  return `concSlot(PairSlotAppl(slotName,UnamedVariableStar(option,codomain,newconstraints)),sl*);
                }

                _ -> {
                  TomType domaintype = `typelist.getHeadconcTomType();
                  SlotList sl = expandVariableList(symbol,`tail);
                  SlotList res = `concSlot(PairSlotAppl(slotName,(TomTerm)expandVariable(domaintype, slotAppl)),sl*);
                  //System.out.println("domaintype = " + domaintype);
                  //System.out.println("res = " + res);
                  return res;

                }
              }
            } else {
              SlotList sl = expandVariableList(symbol,`tail);
              return `concSlot(PairSlotAppl(slotName,(TomTerm)expandVariable(TomBase.getSlotType(symb,slotName), slotAppl)),sl*);
            }
          }
      }
      throw new TomRuntimeException("expandVariableList: strange case: '" + symbol + "'");
    }

    %strategy replace_replaceInstantiatedVariable(instantiatedVariable:TomList) extends `Fail() {
      visit TomTerm {
        subject -> {
          %match(subject, instantiatedVariable) {
            RecordAppl[NameList=(opNameAST),Slots=concSlot()] , concTomTerm(_*,var@(Variable|VariableStar)[AstName=opNameAST],_*) -> {
              return `var;
            }
            Variable[AstName=opNameAST], concTomTerm(_*,var@Variable[AstName=opNameAST],_*) -> {
              return `var;
            }
            Variable[AstName=opNameAST], concTomTerm(_*,var@VariableStar[AstName=opNameAST],_*) -> {
              return `var;
            }
            VariableStar[AstName=opNameAST], concTomTerm(_*,var@VariableStar[AstName=opNameAST],_*) -> {
              return `var;
            }
          }
        }
      }
    }

    protected tom.library.sl.Visitable replaceInstantiatedVariable(TomList instantiatedVariable, tom.library.sl.Visitable subject) {
      try {
        //System.out.println("varlist = " + instantiatedVariable);
        //System.out.println("subject = " + subject);
        return `TopDownStop(replace_replaceInstantiatedVariable(instantiatedVariable)).visit(subject);
      } catch(tom.library.sl.VisitFailure e) {
        throw new TomRuntimeException("replaceInstantiatedVariable: failure on " + instantiatedVariable);
      }
    }

    private TomType getType(String tomName) {
      TomType tomType = getSymbolTable().getType(tomName);
      return tomType;
    }
    
    /**
     * Collect the matchConstraints in a list of constraints   
     */
    %strategy CollectMatchConstraints(constrList:Collection) extends Identity(){
      visit Constraint{
        m@MatchConstraint[] -> {        
          constrList.add(`m);         
        }      
      }// end visit
    }// end strategy   

  }

