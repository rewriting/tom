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

package tom.engine.typer;

import java.util.*;
import java.util.HashSet;
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

public class KernelTyper {
  %include { ../../library/mapping/java/sl.tom}
  %include { ../../library/mapping/java/util/types/Collection.tom}

  %typeterm KernelTyper {
    implement { KernelTyper }
    is_sort(t) { ($t instanceof KernelTyper) }
  }

  %op Strategy ChoiceTopDown(s1:Strategy) {
    make(v) { (`mu(MuVar("x"),ChoiceId(v,All(MuVar("x"))))) }
  }

  %op Strategy TopDownStop(s1:Strategy) {
    make(v) { (`mu(MuVar("x"),Choice(v,All(MuVar("x"))))) }
  }

  private SymbolTable symbolTable;

  public KernelTyper() {
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

  protected TomSymbol getSymbolFromType(TomType type) {
    %match(type) {
      TypeWithSymbol[TomType=tomType, TlType=tlType] -> {
        return TomBase.getSymbolFromType(`Type(tomType,tlType), getSymbolTable()); 
      }
    }
    return TomBase.getSymbolFromType(type, getSymbolTable()); 
  }
  // ------------------------------------------------------------
  %include { ../adt/tomsignature/TomSignature.tom }
  // ------------------------------------------------------------

  /*
   * The "typeVariable" phase types RecordAppl into Variable
   * we focus on
   * - Match
   *
   * The types of subjects are inferred from the patterns
   *
   * Variable and TermAppl are typed in the TomTerm case
   */

  protected tom.library.sl.Visitable typeType(tom.library.sl.Visitable subject) {
    try {
      return `TopDown(typeType(this)).visit(subject);
    } catch(tom.library.sl.VisitFailure e) {
      throw new TomRuntimeException("typeType: failure on " + subject);
    }
  }
  %strategy typeType(typeer:KernelTyper) extends `Identity() {
    visit TomType {
      subject@TomTypeAlone(tomType) -> {
        TomType type = typeer.getType(`tomType);
        if(type != null) {
          return type;
        } else {
          return `subject; // useful for TomTypeAlone("unknown type")
        }
      }
    }
  }

  %strategy replace_typeVariable(contextType:TomType,typeer:KernelTyper) extends `Fail() {

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
        TomType type = typeer.getType(`tomType);
        if(type != null) {
          return type;
        } else {
          return `subject; // useful for TomTypeAlone("unknown type")
        }
      }
    }

    visit TomVisit {
      VisitTerm(type,constraintInstructionList,options) -> {
        ArrayList<Constraint> constraints = new ArrayList<Constraint>();
        `TopDown(CollectConstraints(constraints)).visitLight(`constraintInstructionList);
        TomType newType = (TomType)`typeer.typeVariable(contextType,`type);
        ConstraintInstructionList newConstraintInstructionList = `concConstraintInstruction();
        %match(constraintInstructionList) {
          concConstraintInstruction(_*,ConstraintInstruction(constraint,action,optionConstraint),_*) -> {
            /*
             * Try to guess types for tomSubjectList
             */
            ArrayList<TomTerm> newPatternList = new ArrayList<TomTerm>();
            Constraint newConstraint = (Constraint)`TopDown(typeConstraint(newType,newPatternList,constraints,typeer)).visitLight(`constraint);
            Instruction newAction = typeAction(`action,ASTFactory.makeList(newPatternList),typeer);
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
        ArrayList<Constraint> constraints = new ArrayList<Constraint>();
        `TopDown(CollectConstraints(constraints)).visitLight(`constraintInstructionList);
        ConstraintInstructionList newConstraintInstructionList = `concConstraintInstruction();
        %match(constraintInstructionList) {
          concConstraintInstruction(_*,ConstraintInstruction(constraint,action,optionConstraint),_*) -> {
            /*
             * Try to guess types for tomSubjectList
             */
            ArrayList<TomTerm> newPatternList = new ArrayList<TomTerm>();
            Constraint newConstraint = (Constraint)`TopDown(typeConstraint(contextType,newPatternList,constraints,typeer)).visitLight(`constraint);
            Instruction newAction = typeAction(`action,ASTFactory.makeList(newPatternList),typeer);
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
            tomSymbol = typeer.getSymbolFromType(contextType);
            if(tomSymbol==null) {
              throw new TomRuntimeException("No symbol found for type '" + contextType + "'");
            }
            `nameList = `concTomName(tomSymbol.getAstName());
          } catch(UnsupportedOperationException e) {
            // contextType has no AstType slot
            tomSymbol = null;
          }
        } else {
          tomSymbol = typeer.getSymbolFromName(`tomName);
        }
        
        if(tomSymbol != null) {
          SlotList subterm = typeer.typeVariableList(tomSymbol, `slotList);
          ConstraintList newConstraints = (ConstraintList)typeer.typeVariable(TomBase.getSymbolCodomain(tomSymbol),`constraints);
          return `RecordAppl(option,nameList,subterm,newConstraints);
        } else {
          //System.out.println("contextType = " + contextType);

          %match(contextType) {
            type@(Type|TypeWithSymbol)[] -> {
              SlotList subterm = typeer.typeVariableList(`emptySymbol(), `slotList);
              ConstraintList newConstraints = (ConstraintList)typeer.typeVariable(`type,`constraints);
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
        TomType localType = typeer.getType(`tomType);
        //System.out.println("localType = " + localType);
        if(localType != null) {
          // The variable has already a known type
          return `var.setAstType(localType);
        }

        //System.out.println("contextType = " + contextType);
        %match(contextType) {
          (Type|TypeWithSymbol)[TomType=tomType,TlType=tlType] -> {
            TomType ctype = `Type(tomType,tlType);
            ConstraintList newConstraints = (ConstraintList)typeer.typeVariable(ctype,`constraints);
            TomTerm newVar = `var.setAstType(ctype);
            //System.out.println("newVar = " + newVar);
            return newVar.setConstraints(newConstraints);
          }
        }
      }
    }
   }
  
  private static Instruction typeAction(Instruction action, TomList newPatternList, KernelTyper typeer) { 
    //  build the list of variables that occur in the lhs
    HashSet set = new HashSet();
    TomBase.collectVariable(set,newPatternList);
    TomList varList = ASTFactory.makeList(set);
    Instruction newAction = (Instruction)typeer.replaceInstantiatedVariable(`varList,`action);
    //System.out.println("newAction1 = " + newAction);
    newAction = (Instruction)`typeer.typeVariable(`EmptyType(),`newAction);
    return newAction;
  }
  
  /**
   * Try to guess the type for the subjects
   */
  %strategy typeConstraint(TomType contextType, Collection newPatternList, Collection constraintList, KernelTyper typeer) extends Identity(){
    visit Constraint {
 visitL:constr -> {
        TomTerm subject = null;
        TomTerm pattern = null;
        NumericConstraintType numericType = null;
        boolean isMatchConstraint = false;
 matchL:%match(constr) {
          MatchConstraint(p, s) -> {pattern = `p;subject = `s;isMatchConstraint = true;break matchL;}
          NumericConstraint(left, right, nt) -> {pattern = `left;subject = `right;numericType = `nt;break matchL;}
          _ -> { break visitL; }
        }
        TomTerm newSubject = null;
        TomType newSubjectType = null;        
        %match(subject) {
          (Variable|VariableStar)(variableOption,astName@Name(name),tomType,constraints) -> {
            TomTerm newVariable = null;
            // tomType may be a TomTypeAlone or a type from an typed variable
            String type = TomBase.getTomType(`tomType);
            //System.out.println("match type = " + type);
            if(typeer.getType(`type) == null) {
              /* the subject is a variable with an unknown type */
              newSubjectType = typeer.guessSubjectType(`subject,constraintList);
              if(newSubjectType != null) {
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
            TomSymbol symbol = typeer.getSymbolFromName(`name);
            TomType type = null;
            if(symbol!=null) {
              type = TomBase.getSymbolCodomain(symbol);
            } else {
              // unknown function call
              type = typeer.guessSubjectType(`subject,constraintList);
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
          
          // the user specified the type (already checked for consistence in SyntaxChecker)
          term@BuildReducedTerm[AstType=userType] -> {            
            newSubjectType = `userType;
            newSubject = `term;
          }
          
        } // end match subject        
        newSubjectType = (TomType)typeer.typeVariable(contextType,newSubjectType);
        newSubject = (TomTerm)typeer.typeVariable(newSubjectType, newSubject);
        TomTerm newPattern = (TomTerm)typeer.typeVariable(newSubjectType, `pattern);
        newPatternList.add(newPattern);
        return isMatchConstraint ? `MatchConstraint(newPattern,newSubject) : `NumericConstraint(newPattern,newSubject,numericType);
      }
    }
  } 

  private TomType guessSubjectType(TomTerm subject, Collection matchConstraints){    
    for(Object constr:matchConstraints){
      %match(constr){
        MatchConstraint(pattern,s) -> {
          // we want two terms to be equal even if their option is different 
          //( because of their possition for example )
matchL:  %match(subject,s){
            Variable[AstName=astName,AstType=tomType],Variable[AstName=astName,AstType=tomType] -> {break matchL;}
            TermAppl[NameList=tomNameList,Args=tomList],TermAppl[NameList=tomNameList,Args=tomList] -> {break matchL;}
            RecordAppl[NameList=tomNameList,Slots=slotList],RecordAppl[NameList=tomNameList,Slots=slotList] -> {break matchL;}
            XMLAppl[NameList=tomNameList,AttrList=tomList,ChildList=tomList],XMLAppl[NameList=tomNameList,AttrList=tomList,ChildList=tomList] -> { break matchL; }
            BuildReducedTerm(TermAppl[NameList=tomNameList,Args=tomList],type),BuildReducedTerm(TermAppl[NameList=tomNameList,Args=tomList],type) -> {break matchL;}
            _,_ -> { continue; }
          }
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

    protected tom.library.sl.Visitable typeVariable(TomType contextType, tom.library.sl.Visitable subject) {
      if(contextType == null) {
        throw new TomRuntimeException("typeVariable: null contextType");
      }
      try {
        //System.out.println("typeVariable: " + contextType);
        //System.out.println("typeVariable subject: " + subject);
        tom.library.sl.Visitable res = `TopDownStop(replace_typeVariable(contextType,this)).visit(subject);
        //System.out.println("res: " + res);
        return res;
      } catch(tom.library.sl.VisitFailure e) {
        throw new TomRuntimeException("typeVariable: failure on " + subject);
        //return subject;
      }
    }

    /*
     * perform type inference of subterms (subtermList)
     * under a given operator (symbol)
     */
    private SlotList typeVariableList(TomSymbol symbol, SlotList subtermList) {
      if(symbol == null) {
        throw new TomRuntimeException("typeVariableList: null symbol");
      }

      if(subtermList.isEmptyconcSlot()) {
        return `concSlot();
      }

      //System.out.println("symbol = " + symbol.getastname());
      %match(symbol, subtermList) {
        symb@emptySymbol(), concSlot(PairSlotAppl(slotName,slotAppl),tail*) -> {
          /*
           * if the top symbol is unknown, the subterms
           * are typed in an empty context
           */
          SlotList sl = typeVariableList(`symb,`tail);
          return `concSlot(PairSlotAppl(slotName,(TomTerm)typeVariable(EmptyType(),slotAppl)),sl*);
        }

        symb@Symbol[AstName=symbolName,TypesToType=TypesToType(typelist,codomain@Type(tomCodomain,tlCodomain))],
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
                  ConstraintList newconstraints = (ConstraintList)typeVariable(`codomain,`constraints);
                  SlotList sl = typeVariableList(`symb,`tail);
                  return `concSlot(PairSlotAppl(slotName,VariableStar(option,name,TypeWithSymbol(tomCodomain,tlCodomain,symbolName),newconstraints)),sl*);
                }

                UnamedVariableStar[Option=option,Constraints=constraints] -> {
                  ConstraintList newconstraints = (ConstraintList)typeVariable(`codomain,`constraints);
                  SlotList sl = typeVariableList(`symb,`tail);
                  return `concSlot(PairSlotAppl(slotName,UnamedVariableStar(option,codomain,newconstraints)),sl*);
                }

                _ -> {
                  TomType domaintype = `typelist.getHeadconcTomType();
                  SlotList sl = typeVariableList(`symb,`tail);
                  SlotList res = `concSlot(PairSlotAppl(slotName,(TomTerm)typeVariable(domaintype, slotAppl)),sl*);
                  //System.out.println("domaintype = " + domaintype);
                  //System.out.println("res = " + res);
                  return res;

                }
              }
            } else {
              SlotList sl = typeVariableList(`symb,`tail);
              return `concSlot(PairSlotAppl(slotName,(TomTerm)typeVariable(TomBase.getSlotType(symb,slotName), slotAppl)),sl*);
            }
          }
      }
      throw new TomRuntimeException("typeVariableList: strange case: '" + symbol + "'");
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

    /**
     * Collect the constraints (match and numeric)
     */
    %strategy CollectConstraints(constrList:Collection) extends Identity(){
      visit Constraint{
        c@(MatchConstraint|NumericConstraint)[] -> {        
          constrList.add(`c);         
        }      
      }// end visit
    }// end strategy   
  }

