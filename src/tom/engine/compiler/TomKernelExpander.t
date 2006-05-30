/*
 *
 * TOM - To One Matching Compiler
 * 
 * Copyright (c) 2000-2006, INRIA
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

import tom.engine.TomBase;
import tom.engine.adt.tomsignature.types.*;
import tom.engine.exception.TomRuntimeException;
import tom.engine.tools.SymbolTable;
import tom.engine.tools.ASTFactory;
import aterm.ATerm;

import tom.library.strategy.mutraveler.MuTraveler;
import tom.library.strategy.mutraveler.Identity;
import jjtraveler.reflective.VisitableVisitor;
import jjtraveler.VisitFailure;


public class TomKernelExpander extends TomBase {

  %include { mutraveler.tom}

  %typeterm TomKernelExpander {
    implement { TomKernelExpander }
  }

  %op Strategy ChoiceTopDown(s1:Strategy) {
    make(v) { `mu(MuVar("x"),ChoiceId(v,All(MuVar("x")))) }
  }

  private SymbolTable symbolTable;

  public TomKernelExpander() {
    super();
  }

  public void setSymbolTable(SymbolTable symbolTable) {
    this.symbolTable = symbolTable;
  }

  private SymbolTable getSymbolTable() {
    return symbolTable;
  }

  protected TomSymbol getSymbolFromName(String tomName) {
    return getSymbolFromName(tomName, getSymbolTable());
  }

  protected TomSymbol getSymbolFromType(TomType tomType) {
    return getSymbolFromType(tomType, getSymbolTable());
  }
  // ------------------------------------------------------------
  %include { adt/tomsignature/TomSignature.tom }
  // ------------------------------------------------------------

  /*
   * The "expandVariable" phase expands RecordAppl into Variable
   * we focus on
   * - RewriteRule
   * - MatchingCondition
   * - EqualityCondition
   * - Match
   *
   * Variable and TermAppl are are expanded in the TomTerm case
   */

  %strategy replace_expandVariable(contextSubject:TomTerm,expander:TomKernelExpander) extends `Identity(){

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

    visit TomRule {
      RewriteRule(Term(lhs@RecordAppl[nameList=(Name(tomName))]),
          Term(rhs),
          condList,
          option)  -> {
        TomSymbol tomSymbol = expander.getSymbolFromName(`tomName);
        TomType symbolType = getSymbolCodomain(tomSymbol);
        TomTerm newLhs = `Term(expander.expandVariable(contextSubject,lhs));
        // build the list of variables that occur in the lhs
        HashSet set = new HashSet();
        collectVariable(set,newLhs);
        TomList varList = ASTFactory.makeList(set);
        InstructionList newCondList = `emptyInstructionList();
        while(!`condList.isEmpty()) {
          Instruction cond = `condList.getHead();

          Instruction newCond = expander.replaceInstantiatedVariableInstruction(`varList,cond);
          newCond = (Instruction) expander.expandVariableInstruction(contextSubject,newCond);

          newCondList = `manyInstructionList(newCond,newCondList);
          collectVariable(set,newCond);
          varList = ASTFactory.makeList(set);
          `condList = `condList.getTail();
        }

        TomTerm newRhs = expander.replaceInstantiatedVariable(`varList,`rhs);
        newRhs = `Term(expander.expandVariable(TomTypeToTomTerm(symbolType),newRhs));

        return `RewriteRule(newLhs,newRhs,newCondList,option);
      }
    }

    visit TomVisit {
      VisitTerm(type,patternInstructionList,options) -> {
        TomType newType = `expander.expandType(contextSubject,`type);
        PatternInstructionList newPatternInstructionList = expander.expandVariablePatternInstructionList(`TomTypeToTomTerm(newType),`patternInstructionList);
        return `VisitTerm(newType, newPatternInstructionList,options);
      }
    }
    visit Instruction {
      MatchingCondition[lhs=lhs@Variable[astName=Name(_), astType=lhsType],
        rhs=rhs@Variable[astName=Name(_), astType=rhsType]] -> {
          TomTerm newLhs = expander.expandVariable(`TomTypeToTomTerm(rhsType),`lhs);
          return `MatchingCondition(newLhs,rhs);
        }

      MatchingCondition[lhs=lhs@RecordAppl[nameList=(Name(lhsName))],
        rhs=rhs@Variable[astName=Name(_), astType=rhsType]] -> {
          TomSymbol lhsSymbol = expander.getSymbolFromName(`lhsName);
          TomType type;
          if(lhsSymbol != null) {
            type = getSymbolCodomain(lhsSymbol);
          } else {
            throw new TomRuntimeException("lhs has an unknown sort: " + `lhsName);
          }

          TomTerm newLhs = expander.expandVariable(`TomTypeToTomTerm(type),`lhs);
          TomTerm newRhs = expander.expandVariable(`TomTypeToTomTerm(type),`rhs);
          return `MatchingCondition(newLhs,newRhs);
        }

      MatchingCondition[lhs=lhs@Variable[astName=Name(_), astType=lhsType],
        rhs=rhs@RecordAppl[nameList=(Name(rhsName))]] -> {
          TomSymbol rhsSymbol = expander.getSymbolFromName(`rhsName);
          TomType type;
          if(rhsSymbol != null) {
            type = getSymbolCodomain(rhsSymbol);
          } else {
            throw new TomRuntimeException("rhs has an unknown sort: " + `rhsName);
          }

          TomTerm newLhs = expander.expandVariable(`TomTypeToTomTerm(type),`lhs);
          TomTerm newRhs = expander.expandVariable(`TomTypeToTomTerm(type),`rhs);
          return `MatchingCondition(newLhs,newRhs);
        }

      MatchingCondition[lhs=lhs@RecordAppl[nameList=(Name(lhsName),_*)],
        rhs=rhs@RecordAppl[nameList=(Name(rhsName))]] -> {
          TomSymbol lhsSymbol = expander.getSymbolFromName(`lhsName);
          TomSymbol rhsSymbol = expander.getSymbolFromName(`rhsName);
          TomType type;
          // rhs is an application
          if(lhsSymbol != null) {
            type = getSymbolCodomain(lhsSymbol);
          } else if(rhsSymbol != null) {
            type = getSymbolCodomain(rhsSymbol);
          } else {
            // lhs is a variable, but rhs has an unknown top symbol
            // since lhs is a fresh variable, we look for rhs type
            throw new TomRuntimeException("rhs has an unknown sort: " + `rhsName);
          }

          TomTerm newLhs = expander.expandVariable(`TomTypeToTomTerm(type),`lhs);
          TomTerm newRhs = expander.expandVariable(`TomTypeToTomTerm(type),`rhs);
          return `MatchingCondition(newLhs,newRhs);
        }

      EqualityCondition[lhs=lhs@Variable[astName=Name(_), astType=type],
        rhs=rhs@Variable[astName=Name(_), astType=type]] -> {
          return `TypedEqualityCondition(type,lhs,rhs);
        }

      EqualityCondition[lhs=lhs@Variable[astName=Name(_), astType=type],
        rhs=rhs@RecordAppl[nameList=(Name(_))]] -> {
          TomTerm newRhs = expander.expandVariable(`TomTypeToTomTerm(type),`rhs);
          return `TypedEqualityCondition(type,lhs,newRhs);
        }

      EqualityCondition[lhs=lhs@RecordAppl[nameList=(Name(_))],
        rhs=rhs@Variable[astName=Name(_), astType=type]] -> {
          TomTerm newLhs = expander.expandVariable(`TomTypeToTomTerm(type),`lhs);
          return `TypedEqualityCondition(type,newLhs,rhs);
        }

      EqualityCondition[lhs=lhs@RecordAppl[nameList=(Name(lhsName))],
        rhs=rhs@RecordAppl[nameList=(Name(rhsName))]] -> {
          TomSymbol lhsSymbol = expander.getSymbolFromName(`lhsName);
          TomSymbol rhsSymbol = expander.getSymbolFromName(`rhsName);
          TomType type;

          if(lhsSymbol != null) {
            type = getSymbolCodomain(lhsSymbol);
          } else if(rhsSymbol != null) {
            type = getSymbolCodomain(rhsSymbol);
          } else {
            // lhs and rhs have an unknown top symbol
            throw new TomRuntimeException("lhs and rhs have an unknown sort: " + `lhsName + ",  " + `rhsName);
          }

          //System.out.println("EqualityCondition type = " + type);

          TomTerm newLhs = expander.expandVariable(`TomTypeToTomTerm(type),`lhs);
          TomTerm newRhs = expander.expandVariable(`TomTypeToTomTerm(type),`rhs);

          //System.out.println("lhs    = " + lhs);
          //System.out.println("newLhs = " + newLhs);

          return `TypedEqualityCondition(type,newLhs,newRhs);
        }

      Match(tomSubjectList,patternInstructionList, option) -> {
        //System.out.println("tomSubjectList = " + tomSubjectList);
        TomTerm newSubjectList = expander.expandVariable(contextSubject,`tomSubjectList);
        //System.out.println("newSubjectList = " + newSubjectList);
        PatternInstructionList newPatternInstructionList = expander.expandVariablePatternInstructionList(newSubjectList,`patternInstructionList);
        return `Match(newSubjectList,newPatternInstructionList, option);
      }
    }

    visit Pattern {
      Pattern(subjectList,termList, guardList) -> {
        %match(TomTerm contextSubject){
          SubjectList(l1) ->{
            //System.out.println("expandVariable.9: "+l1+"(" + termList + ")");

            // process a list of subterms
            ArrayList list = new ArrayList();
            while(!`termList.isEmpty()) {
              list.add(expander.expandVariable(`l1.getHead(), `termList.getHead()));
              `termList = `termList.getTail();
              `l1 = `l1.getTail();
            }
            TomList newTermList = ASTFactory.makeList(list);

            // process a list of guards
            list.clear();
            // build the list of variables that occur in the lhs
            HashSet set = new HashSet();
            collectVariable(set,newTermList);
            TomList varList = ASTFactory.makeList(set);
            //System.out.println("varList = " + varList);
            while(!`guardList.isEmpty()) {
              list.add(expander.replaceInstantiatedVariable(`varList, `guardList.getHead()));
              `guardList = `guardList.getTail();
            }
            TomList newGuardList = ASTFactory.makeList(list);
            //System.out.println("newGuardList = " + newGuardList);
            return `Pattern(subjectList,newTermList,newGuardList);
          }
        }
      }
    }	
    visit TomTerm {
      RecordAppl[option=option,nameList=nameList@(Name(tomName),_*),slots=slotList,constraints=constraints] -> {
        TomSymbol tomSymbol = null;
        if(`tomName.equals("")) {
          if(contextSubject.hasAstType()) {
            tomSymbol = expander.getSymbolFromType(contextSubject.getAstType());
            if(tomSymbol==null) {
              throw new TomRuntimeException("No symbol found for type '" + contextSubject.getAstType() + "'");
            }
            `nameList = `concTomName(tomSymbol.getAstName());
          }
        } else {
          tomSymbol = expander.getSymbolFromName(`tomName);
        }

        if(tomSymbol != null) {
          SlotList subterm = expander.expandVariableList(tomSymbol, `slotList);
          ConstraintList newConstraints = expander.expandVariableConstraintList(`TomTypeToTomTerm(getSymbolCodomain(tomSymbol)),`constraints);
          return `RecordAppl(option,nameList,subterm,newConstraints);
        } else {
          %match(TomTerm contextSubject) {
            TomTypeToTomTerm(type@Type[]) -> {
              SlotList subterm = expander.expandVariableList(`emptySymbol(), `slotList);
              ConstraintList newConstraints = expander.expandVariableConstraintList(`emptyTerm(),`constraints);
              return `RecordAppl(option,nameList,subterm,newConstraints);
            }
            Variable[astType=type] -> {
              ConstraintList newConstraints = expander.expandVariableConstraintList(`TomTypeToTomTerm(type),`constraints);
              SlotList subterm = expander.expandVariableList(`emptySymbol(), `slotList);
              return `RecordAppl(option,nameList,subterm,newConstraints);
            }
            Tom(concTomTerm(_*,var@Variable[astName=Name(varName)],_*)) -> {
              ConstraintList newConstraints = expander.expandVariableConstraintList(`contextSubject,`constraints);
              SlotList subterm = expander.expandVariableList(`emptySymbol(), `slotList);
              return `RecordAppl(option,nameList,subterm,newConstraints);
            }

            _ -> {
              // do nothing

              //System.out.println("contextSubject = " + contextSubject);
              //System.out.println("subject        = " + subject);

            }
          }
        }
      }


      Variable[option=option,astName=astName,astType=TomTypeAlone[],constraints=constraints] -> {
        %match(TomTerm contextSubject){
          context@TomTypeToTomTerm(type@Type[]) ->{
            // create a variable
            return `Variable(option,astName,type,expander.expandVariableConstraintList(context,constraints));
          }
        }
      }

      Variable[option=option,astName=astName,astType=TomTypeAlone[],constraints=constraints] -> {
        %match(TomTerm contextSubject){
          Variable[astType=type1] -> {
            ConstraintList newConstraints = expander.expandVariableConstraintList(`TomTypeToTomTerm(type1),`constraints);
            // create a variable
            return `Variable(option,astName,type1,newConstraints);
          }
        }
      }

      Variable[option=option,astName=Name(strName),astType=TomTypeAlone(tomType),constraints=constraints] -> {
        // create a variable
        TomType localType = expander.getType(`tomType);
        if(localType != null) {
          return `Variable(option,Name(strName),localType,constraints);
        } else {
          // do nothing
        }
      }

      Placeholder[option=option,constraints=constraints] -> {
        %match(TomTerm contextSubject){
          TomTypeToTomTerm(type@Type[])->{ 
            ConstraintList newConstraints = expander.expandVariableConstraintList(`TomTypeToTomTerm(type),`constraints);
            // create an unamed variable
            return `UnamedVariable(option,type,newConstraints);
          }
        }
      }

      Placeholder[option=option,constraints=constraints] -> {
        %match(TomTerm contextSubject){
          Variable[astType=type1] -> {
            ConstraintList newConstraints = expander.expandVariableConstraintList(`TomTypeToTomTerm(type1),`constraints);
            // create an unamed variable
            return `UnamedVariable(option,type1,newConstraints);
          }
        }
      }

      TLVar(strName,TomTypeAlone(tomType)) -> {
        // create a variable: its type is ensured by checker
        TomType localType = expander.getType(`tomType);
        OptionList option = ASTFactory.makeOption();
        return `Variable(option,Name(strName),localType,concConstraint());
      }

      TLVar(strName,localType@Type[]) -> {
        // create a variable: its type is ensured by checker
        OptionList option = ASTFactory.makeOption();
        return `Variable(option,Name(strName),localType,concConstraint());
      }

    }
  }

  protected TomTerm expandVariable(TomTerm contextSubject, TomTerm subject) {
    if(contextSubject == null) {
      throw new TomRuntimeException("expandVariable: null contextSubject");
    }
    try{
      return  (TomTerm) (`ChoiceTopDown(replace_expandVariable(contextSubject,this))).visit(subject);
    } catch(VisitFailure e) {
      return subject;
    }
  }

  protected TomType expandType(TomTerm contextSubject, TomType subject) {
    if(contextSubject == null) {
      throw new TomRuntimeException("expandVariable: null contextSubject");
    }
    try {
      return  (TomType) (`ChoiceTopDown(replace_expandVariable(contextSubject,this))).visit(subject);
    } catch(VisitFailure e) {
      return subject;
    }
  }

  private Instruction expandVariableInstruction(TomTerm contextSubject, Instruction subject) {
    if(contextSubject == null) {
      throw new TomRuntimeException("expandVariable: null contextSubject");
    }
    try {
      return (Instruction) (`ChoiceTopDown(replace_expandVariable(contextSubject,this))).visit(subject);
    } catch(VisitFailure e) {
      return subject;
    }
  }

  protected ConstraintList expandVariableConstraintList(TomTerm contextSubject, ConstraintList subject) {
    if(contextSubject == null) {
      throw new TomRuntimeException("expandVariable: null contextSubject");
    }
    try{
      return  (ConstraintList) (`ChoiceTopDown(replace_expandVariable(contextSubject,this))).visit(subject);
    } catch(VisitFailure e) {
      return subject;
    }
  }

  protected PatternInstructionList expandVariablePatternInstructionList(TomTerm contextSubject, PatternInstructionList subject) {
    if(contextSubject == null) {
      throw new TomRuntimeException("expandVariable: null contextSubject");
    }
    try{
      return  (PatternInstructionList) (`ChoiceTopDown(replace_expandVariable(contextSubject,this))).visit(subject);
    }catch(VisitFailure e){return subject;}
  }


  private TomType getTypeFromVariableList(TomName name, TomList list) {

    //System.out.println("name = " + name);
    //System.out.println("list = " + list);

    %match(TomName name,TomList list) {
      _,emptyTomList() -> {
        System.out.println("getTypeFromVariableList. Stange case '" + name + "' not found");
        throw new TomRuntimeException("getTypeFromVariableList. Stange case '" + name + "' not found");
      }

      varName, manyTomList(Variable[astName=varName,astType=type@Type[]],_) -> { return `type; }
      varName, manyTomList(VariableStar[astName=varName,astType=type@Type[]],_) -> { return `type; }
      _, manyTomList(_,tail) -> { return getTypeFromVariableList(name,`tail); }

    }
    return null;
  }

  /*
   * perform type inference of subterms (subtermList)
   * under a given operator (symbol)
   */
  private SlotList expandVariableList(TomSymbol symbol, SlotList subtermList) {
    if(symbol == null) {
      throw new TomRuntimeException("expandVariableList: null symbol");
    }

    if(subtermList.isEmpty()) {
      return `emptySlotList();
    }

    //System.out.println("symbol = " + subject.getastname());
    %match(TomSymbol symbol, SlotList subtermList) {
      emptySymbol(), manySlotList(PairSlotAppl(slotName,slotAppl),tail) -> {
        /*
         * if the top symbol is unknown, the subterms
         * are expanded in an empty context
         */
        return `manySlotList(PairSlotAppl(slotName,expandVariable(emptyTerm(),slotAppl)), expandVariableList(symbol,tail));
      }

      symb@Symbol[typesToType=TypesToType(typelist,codomain)],
        manySlotList(PairSlotAppl(slotName,slotAppl),tail) -> {
          // process a list of subterms and a list of types
          if(isListOperator(`symb) || isArrayOperator(`symb)) {
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

            //System.out.println("listoperator: " + symb);
            //System.out.println("subtermlist: " + subtermlist);

            %match(TomTerm slotAppl) {
              VariableStar[option=option,astName=name,constraints=constraints] -> {
                ConstraintList newconstraints = expandVariableConstraintList(`TomTypeToTomTerm(codomain),`constraints);
                return `manySlotList(PairSlotAppl(slotName,VariableStar(option,name,codomain,newconstraints)), expandVariableList(symbol,tail));
              }

              UnamedVariableStar[option=option,constraints=constraints] -> {
                ConstraintList newconstraints = expandVariableConstraintList(`TomTypeToTomTerm(codomain),`constraints);
                return `manySlotList(PairSlotAppl(slotName,UnamedVariableStar(option,codomain,newconstraints)), expandVariableList(symbol,tail));
              }

              _ -> {
                TomType domaintype = `typelist.getHead();
                return `manySlotList(PairSlotAppl(slotName,expandVariable(TomTypeToTomTerm(domaintype), slotAppl)), expandVariableList(symbol,tail));

              }
            }
          } else {
            //TomType type = `typelist.getHead();
            return `manySlotList(PairSlotAppl(slotName,expandVariable(TomTypeToTomTerm(getSlotType(symb,slotName)), slotAppl)), expandVariableList(symbol,tail));
          }
        }
    }
    System.out.println("expandVariableList: strange case: '" + symbol + "'");
    throw new TomRuntimeException("expandVariableList: strange case: '" + symbol + "'");
  }

  %strategy replace_replaceInstantiatedVariable(instantiatedVariable:TomList) extends `Identity() {
    visit TomTerm {
      subject -> {
        %match(TomTerm subject, TomList instantiatedVariable) {
          RecordAppl[nameList=(opNameAST),slots=concSlot()] , concTomTerm(_*,var@(Variable|VariableStar)[astName=opNameAST] ,_*) -> {
            return `var;
          }
          Variable[astName=opNameAST], concTomTerm(_*,var@Variable[astName=opNameAST] ,_*) -> {
            return `var;
          }
          VariableStar[astName=opNameAST],concTomTerm(_*,var@VariableStar[astName=opNameAST] ,_*) -> {
            return `var;
          }
        }
      }
    }
  }

  protected TomTerm replaceInstantiatedVariable(TomList instantiatedVariable, TomTerm subject) {
    if(instantiatedVariable == null) {
      throw new TomRuntimeException("replaceInstantiatedVariable: null instantiatedVariable");
    }
    try{
      return (TomTerm)(`ChoiceTopDown(replace_replaceInstantiatedVariable(instantiatedVariable))).visit(subject);
    }catch(VisitFailure e){
      return subject;}
  }
  protected Instruction replaceInstantiatedVariableInstruction(TomList instantiatedVariable, Instruction subject) {

    if(instantiatedVariable == null) {
      throw new TomRuntimeException("replaceInstantiatedVariable: null instantiatedVariable");
    }
    try{
      return (Instruction) (`ChoiceTopDown(replace_replaceInstantiatedVariable(instantiatedVariable))).visit(subject);
    }catch(VisitFailure e){
      return subject;
    }
  }

  private TomType getType(String tomName) {
    TomType tomType = getSymbolTable().getType(tomName);
    return tomType;
  }

}
