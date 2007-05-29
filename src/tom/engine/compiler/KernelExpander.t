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

import java.util.ArrayList;
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
      VisitTerm(type,patternInstructionList,options) -> {
        //System.out.println("expander: patternInstructionList = " + `patternInstructionList);  
        TomType newType = (TomType)`expander.expandVariable(contextType,`type);
        //System.out.println("newType = " + newType);
        PatternInstructionList newPatternInstructionList = (PatternInstructionList)expander.expandVariable(`TypeList(concTomType(newType)),`patternInstructionList);
        //System.out.println("after: patternInstructionList = " + `newPatternInstructionList);  
        return `VisitTerm(newType, newPatternInstructionList,options);
      }
    }

    visit Instruction {
      /*
       * Expansion of a Match construct
       * to add types in subjects
       * to add types in variables of patterns and rhs
       */
      Match(SubjectList(tomSubjectList),patternInstructionList, option) -> {
        /*
         * Try to guess types for tomSubjectList
         */
        ArrayList newSubjectList = new ArrayList();
        TomTypeList typeList = `concTomType();
        int index = 0;
        while(!`tomSubjectList.isEmptyconcTomTerm()) {
          TomTerm subject = `tomSubjectList.getHeadconcTomTerm();
matchBlock: {
              //System.out.println("subject = " + subject);
              %match(subject) {
                (Variable|VariableStar)(variableOption,astName@Name(name),tomType,constraints) -> {
                  TomTerm newVariable = null;
                  // tomType may be a TomTypeAlone or a type from an expanded variable
                  String type = TomBase.getTomType(`tomType);
                  //System.out.println("match type = " + type);
                  if(expander.getType(`type) == null) {
                    /* the subject is a variable with an unknown type */
                    TomType newType = expander.guessTypeFromPatterns(`patternInstructionList,index);
                    if(newType!=null) {
                      newVariable = `Variable(variableOption,astName,newType,constraints);
                    } else {
                      throw new TomRuntimeException("No symbol found for name '" + `name + "'");
                    }
                  } else {
                    newVariable = subject;
                  }
                  if(newVariable == null) {
                    throw new TomRuntimeException("Type cannot be guessed for '" + subject + "'");
                  } else {
                    newSubjectList.add(newVariable);
                    typeList = `concTomType(typeList*,newVariable.getAstType());
                    //System.out.println("add type = " + newVariable.getAstType());
                  }
                  break matchBlock;
                }

                t@(TermAppl|RecordAppl)[NameList=concTomName(Name(name),_*)] -> {
                  TomSymbol symbol = expander.getSymbolFromName(`name);
                  TomType type = null;
                  if(symbol!=null) {
                    type = TomBase.getSymbolCodomain(symbol);
                  } else {
                    // unknown function call
                    type = expander.guessTypeFromPatterns(`patternInstructionList,index);
                  }
                  if(type!=null) {
                    newSubjectList.add(`BuildReducedTerm(t,type));
                  } else {
                    throw new TomRuntimeException("No symbol found for name '" + `name + "'");
                  }
                  typeList = `concTomType(typeList*,type);
                    //System.out.println("add type2 = " + type);
                }

              }
            } // end matchBlock
            index++;
            `tomSubjectList=`tomSubjectList.getTailconcTomTerm();
        }

        TomTerm newTomSubjectList = (TomTerm)expander.expandVariable(contextType, `SubjectList(ASTFactory.makeList(newSubjectList)));
        //System.out.println("newTomSubjectList = " + newTomSubjectList);
        TomType newTypeList = `TypeList((TomTypeList)expander.expandVariable(contextType,typeList));
        //System.out.println("context = " + contextType);
        //System.out.println("typelist = " + typeList);
        //System.out.println("newtl = " + newTypeList);

        PatternInstructionList newPatternInstructionList = (PatternInstructionList)expander.expandVariable(newTypeList,`patternInstructionList);
        //System.out.println("newPatternInstructionList = " + newPatternInstructionList);
        return `Match(newTomSubjectList,newPatternInstructionList, option);
      }
    }

    /*
     * given a list of subjects
     * for each pattern, perform type expansion according to the type of subjects
     */
    //visit Pattern {
    visit PatternInstruction {
      pa@PatternInstruction(Pattern(subjectList,termList, guardList), action, optionList) -> {
        //System.out.println("start pa: " + `pa);
        //System.out.println("contextType = " + `contextType);
      //Pattern(subjectList,termList, guardList) -> {
        %match(contextType) {
          TypeList(typeList) -> {
            //System.out.println("expandVariable.9: "+l1+"(" + termList + ")");

             // System.out.println("type: " + `typeList);
             // System.out.println("term: " + `termList);

            // process a list of subterms
            TomList newTermList = `concTomTerm();
            TomList newSubjectList = `concTomTerm();
            while(!`typeList.isEmptyconcTomType()) {
              //System.out.println("type: " + `typeList.getHeadconcTomType());
              //System.out.println("term: " + `termList.getHeadconcTomTerm());
              //System.out.println("subject: " + `subjectList.getHeadconcTomTerm());
              newTermList = `concTomTerm(newTermList*, (TomTerm)expander.expandVariable(typeList.getHeadconcTomType(), termList.getHeadconcTomTerm()));
              TomTerm newSubject = (TomTerm)expander.expandVariable(`typeList.getHeadconcTomType(), `subjectList.getHeadconcTomTerm());
              //System.out.println("newSubject: " + newSubject);
              newSubjectList = `concTomTerm(newSubjectList*, newSubject);
              `termList = `termList.getTailconcTomTerm();
              `subjectList = `subjectList.getTailconcTomTerm();
              `typeList = `typeList.getTailconcTomType();
            }
            //System.out.println("newTermList: " + newTermList);
            //System.out.println("newSubjectList: " + newSubjectList);

            // process a list of guards
            // build the list of variables that occur in the lhs
            HashSet set = new HashSet();
            TomBase.collectVariable(set,newTermList);
            TomList varList = ASTFactory.makeList(set);
            //System.out.println("varList = " + varList);
            TomList newGuardList = `concTomTerm();
            while(!`guardList.isEmptyconcTomTerm()) {
              newGuardList = `concTomTerm(newGuardList*, (TomTerm)expander.replaceInstantiatedVariable(varList, guardList.getHeadconcTomTerm()));
              `guardList = `guardList.getTailconcTomTerm();
            }
            //System.out.println("newGuardList = " + newGuardList);
            Instruction newAction = (Instruction)expander.replaceInstantiatedVariable(`varList,`action);
            //System.out.println("newAction1 = " + newAction);
            newAction = (Instruction)`expander.expandVariable(`EmptyType(),`newAction);
            //OptionList newOptionList = (OptionList)`expander.expandVariable(`EmptyType(),``optionList);
            OptionList newOptionList = `optionList;
            return `PatternInstruction(Pattern(newSubjectList,newTermList,newGuardList), newAction,newOptionList);
            //return `Pattern(subjectList,newTermList,newGuardList);
          }

          _ -> {
            System.out.println("Bad contextType: " + `contextType);
            System.out.println(`pa);
            //throw new TomRuntimeException("Bad contextType: " + `contextType);
          }
        }
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

    /**
     * @param index the column-index of the type that has to be infered
     */
    private TomType guessTypeFromPatterns(PatternInstructionList patternInstructionList, int index) {
      %match(patternInstructionList) {
        concPatternInstruction(_*, PatternInstruction[Pattern=Pattern[TomList=concTomTerm(X*,tmpSubject,_*)]], _*) -> {
          TomTerm subject = `tmpSubject;
          %match(subject) {
            AntiTerm(p) -> { subject = `p; }
          }
          %match(subject) {
            (TermAppl|RecordAppl)[NameList=concTomName(Name(name),_*)] -> {
              //System.out.println("X.length = " + `X*.length());
              if(`X*.length() == index) {
                TomSymbol symbol = getSymbolFromName(`name);
                //System.out.println("name = " + `name);
                if(symbol!=null) {
                  TomType newType = TomBase.getSymbolCodomain(symbol);
                  //System.out.println("newType = " + `newType);
                  return `newType;
                } else {
                  return null;
                }
              }
            }
          }
        }
      }
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

  }
