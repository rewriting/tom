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
      RewriteRule(Term(lhs@RecordAppl[NameList=(Name(tomName))]),
          Term(rhs),
          condList,
          option)  -> {
        TomSymbol tomSymbol = expander.getSymbolFromName(`tomName);
        TomType symbolType = getSymbolCodomain(tomSymbol);
        TomTerm newLhs = `Term((TomTerm)expander.expandVariable(contextSubject,lhs));
        // build the list of variables that occur in the lhs
        HashSet set = new HashSet();
        collectVariable(set,newLhs);
        TomList varList = ASTFactory.makeList(set);
        InstructionList newCondList = `concInstruction();
        while(!`condList.isEmptyconcInstruction()) {
          Instruction cond = `condList.getHeadconcInstruction();

          Instruction newCond = (Instruction)expander.replaceInstantiatedVariable(`varList,cond);
          newCond = (Instruction) expander.expandVariable(contextSubject,newCond);

          newCondList = `concInstruction(newCond,newCondList*);
          collectVariable(set,newCond);
          varList = ASTFactory.makeList(set);
          `condList = `condList.getTailconcInstruction();
        }

        TomTerm newRhs = (TomTerm)expander.replaceInstantiatedVariable(`varList,`rhs);
        newRhs = `Term((TomTerm)expander.expandVariable(TomTypeToTomTerm(symbolType),newRhs));

        return `RewriteRule(newLhs,newRhs,newCondList,option);
      }
    }

    visit TomVisit {
      VisitTerm(type,patternInstructionList,options) -> {
        TomType newType = (TomType)`expander.expandVariable(contextSubject,`type);
        PatternInstructionList newPatternInstructionList = (PatternInstructionList)expander.expandVariable(`TomTypeToTomTerm(newType),`patternInstructionList);
        return `VisitTerm(newType, newPatternInstructionList,options);
      }
    }
    visit Instruction {
      MatchingCondition[Lhs=lhs@Variable[AstName=Name(_), AstType=lhsType],
        Rhs=rhs@Variable[AstName=Name(_), AstType=rhsType]] -> {
          TomTerm newLhs = (TomTerm)expander.expandVariable(`TomTypeToTomTerm(rhsType),`lhs);
          return `MatchingCondition(newLhs,rhs);
        }

      MatchingCondition[Lhs=lhs@RecordAppl[NameList=(Name(lhsName))],
        Rhs=rhs@Variable[AstName=Name(_), AstType=rhsType]] -> {
          TomSymbol lhsSymbol = expander.getSymbolFromName(`lhsName);
          TomType type;
          if(lhsSymbol != null) {
            type = getSymbolCodomain(lhsSymbol);
          } else {
            throw new TomRuntimeException("lhs has an unknown sort: " + `lhsName);
          }

          TomTerm newLhs = (TomTerm)expander.expandVariable(`TomTypeToTomTerm(type),`lhs);
          TomTerm newRhs = (TomTerm)expander.expandVariable(`TomTypeToTomTerm(type),`rhs);
          return `MatchingCondition(newLhs,newRhs);
        }

      MatchingCondition[Lhs=lhs@Variable[AstName=Name(_), AstType=lhsType],
        Rhs=rhs@RecordAppl[NameList=(Name(rhsName))]] -> {
          TomSymbol rhsSymbol = expander.getSymbolFromName(`rhsName);
          TomType type;
          if(rhsSymbol != null) {
            type = getSymbolCodomain(rhsSymbol);
          } else {
            throw new TomRuntimeException("rhs has an unknown sort: " + `rhsName);
          }

          TomTerm newLhs = (TomTerm)expander.expandVariable(`TomTypeToTomTerm(type),`lhs);
          TomTerm newRhs = (TomTerm)expander.expandVariable(`TomTypeToTomTerm(type),`rhs);
          return `MatchingCondition(newLhs,newRhs);
        }

      MatchingCondition[Lhs=lhs@RecordAppl[NameList=(Name(lhsName),_*)],
        Rhs=rhs@RecordAppl[NameList=(Name(rhsName))]] -> {
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

          TomTerm newLhs = (TomTerm)expander.expandVariable(`TomTypeToTomTerm(type),`lhs);
          TomTerm newRhs = (TomTerm)expander.expandVariable(`TomTypeToTomTerm(type),`rhs);
          return `MatchingCondition(newLhs,newRhs);
        }

      EqualityCondition[Lhs=lhs@Variable[AstName=Name(_), AstType=type],
        Rhs=rhs@Variable[AstName=Name(_), AstType=type]] -> {
          return `TypedEqualityCondition(type,lhs,rhs);
        }

      EqualityCondition[Lhs=lhs@Variable[AstName=Name(_), AstType=type],
        Rhs=rhs@RecordAppl[NameList=(Name(_))]] -> {
          TomTerm newRhs = (TomTerm)expander.expandVariable(`TomTypeToTomTerm(type),`rhs);
          return `TypedEqualityCondition(type,lhs,newRhs);
        }

      EqualityCondition[Lhs=lhs@RecordAppl[NameList=(Name(_))],
        Rhs=rhs@Variable[AstName=Name(_), AstType=type]] -> {
          TomTerm newLhs = (TomTerm)expander.expandVariable(`TomTypeToTomTerm(type),`lhs);
          return `TypedEqualityCondition(type,newLhs,rhs);
        }

      EqualityCondition[Lhs=lhs@RecordAppl[NameList=(Name(lhsName))],
        Rhs=rhs@RecordAppl[NameList=(Name(rhsName))]] -> {
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

          TomTerm newLhs = (TomTerm)expander.expandVariable(`TomTypeToTomTerm(type),`lhs);
          TomTerm newRhs = (TomTerm)expander.expandVariable(`TomTypeToTomTerm(type),`rhs);

          //System.out.println("lhs    = " + lhs);
          //System.out.println("newLhs = " + newLhs);

          return `TypedEqualityCondition(type,newLhs,newRhs);
        }

      Match(tomSubjectList,patternInstructionList, option) -> {
        //System.out.println("tomSubjectList = " + tomSubjectList);
        TomTerm newSubjectList = (TomTerm)expander.expandVariable(contextSubject,`tomSubjectList);
        //System.out.println("newSubjectList = " + newSubjectList);
        PatternInstructionList newPatternInstructionList = (PatternInstructionList)expander.expandVariable(newSubjectList,`patternInstructionList);
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
            while(!`termList.isEmptyconcTomTerm()) {
              list.add((TomTerm)expander.expandVariable(`l1.getHeadconcTomTerm(), `termList.getHeadconcTomTerm()));
              `termList = `termList.getTailconcTomTerm();
              `l1 = `l1.getTailconcTomTerm();
            }
            TomList newTermList = ASTFactory.makeList(list);

            // process a list of guards
            list.clear();
            // build the list of variables that occur in the lhs
            HashSet set = new HashSet();
            collectVariable(set,newTermList);
            TomList varList = ASTFactory.makeList(set);
            //System.out.println("varList = " + varList);
            while(!`guardList.isEmptyconcTomTerm()) {
              list.add((TomTerm)expander.replaceInstantiatedVariable(`varList, `guardList.getHeadconcTomTerm()));
              `guardList = `guardList.getTailconcTomTerm();
            }
            TomList newGuardList = ASTFactory.makeList(list);
            //System.out.println("newGuardList = " + newGuardList);
            return `Pattern(subjectList,newTermList,newGuardList);
          }
        }
      }
    }	
    visit TomTerm {
      RecordAppl[Option=option,NameList=nameList@(Name(tomName),_*),Slots=slotList,Constraints=constraints] -> {
        TomSymbol tomSymbol = null;
        if(`tomName.equals("")) {
          try {
            tomSymbol = expander.getSymbolFromType(contextSubject.getAstType());
            if(tomSymbol==null) {
              throw new TomRuntimeException("No symbol found for type '" + contextSubject.getAstType() + "'");
            }
            `nameList = `concTomName(tomSymbol.getAstName());
          } catch(UnsupportedOperationException e) {
            // contextSubject has no AstType slot
            tomSymbol = null;
          }
        } else {
          tomSymbol = expander.getSymbolFromName(`tomName);
        }

        if(tomSymbol != null) {
          SlotList subterm = expander.expandVariableList(tomSymbol, `slotList);
          ConstraintList newConstraints = (ConstraintList)expander.expandVariable(`TomTypeToTomTerm(getSymbolCodomain(tomSymbol)),`constraints);
          return `RecordAppl(option,nameList,subterm,newConstraints);
        } else {
          %match(TomTerm contextSubject) {
            TomTypeToTomTerm(type@Type[]) -> {
              SlotList subterm = expander.expandVariableList(`emptySymbol(), `slotList);
              ConstraintList newConstraints = (ConstraintList)expander.expandVariable(`EmptyContext(),`constraints);
              return `RecordAppl(option,nameList,subterm,newConstraints);
            }
            Variable[AstType=type] -> {
              ConstraintList newConstraints = (ConstraintList)expander.expandVariable(`TomTypeToTomTerm(type),`constraints);
              SlotList subterm = expander.expandVariableList(`emptySymbol(), `slotList);
              return `RecordAppl(option,nameList,subterm,newConstraints);
            }
            Tom(concTomTerm(_*,var@Variable[AstName=Name(varName)],_*)) -> {
              ConstraintList newConstraints = (ConstraintList)expander.expandVariable(`contextSubject,`constraints);
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


      Variable[Option=option,AstName=astName,AstType=TomTypeAlone[],Constraints=constraints] -> {
        %match(TomTerm contextSubject){
          context@TomTypeToTomTerm(type@Type[]) ->{
            // create a variable
            return `Variable(option,astName,type,(ConstraintList)expander.expandVariable(context,constraints));
          }
        }
      }

      Variable[Option=option,AstName=astName,AstType=TomTypeAlone[],Constraints=constraints] -> {
        %match(TomTerm contextSubject){
          Variable[AstType=type1] -> {
            ConstraintList newConstraints = (ConstraintList)expander.expandVariable(`TomTypeToTomTerm(type1),`constraints);
            // create a variable
            return `Variable(option,astName,type1,newConstraints);
          }
        }
      }

      Variable[Option=option,AstName=Name(strName),AstType=TomTypeAlone(tomType),Constraints=constraints] -> {
        // create a variable
        TomType localType = expander.getType(`tomType);
        if(localType != null) {
          return `Variable(option,Name(strName),localType,constraints);
        } else {
          // do nothing
        }
      }

      UnamedVariable[Option=option,Constraints=constraints] -> {
        %match(TomTerm contextSubject){
          (TomTypeToTomTerm|Variable)[AstType=type@Type[]] -> { 
            ConstraintList newConstraints = (ConstraintList)expander.expandVariable(`TomTypeToTomTerm(type),`constraints);
            return `UnamedVariable(option,type,newConstraints);
          }
        }
      }

      TLVar(strName,TomTypeAlone(tomType)) -> {
        // create a variable: its type is ensured by checker
        TomType localType = expander.getType(`tomType);
        OptionList option = `concOption();
        return `Variable(option,Name(strName),localType,concConstraint());
      }

      TLVar(strName,localType@Type[]) -> {
        // create a variable: its type is ensured by checker
        OptionList option = `concOption();
        return `Variable(option,Name(strName),localType,concConstraint());
      }

    }
  }

  protected jjtraveler.Visitable expandVariable(TomTerm contextSubject, jjtraveler.Visitable subject) {
    if(contextSubject == null) {
      throw new TomRuntimeException("expandVariable: null contextSubject");
    }
    try{
      return `ChoiceTopDown(replace_expandVariable(contextSubject,this)).visit(subject);
    } catch(VisitFailure e) {
      return subject;
    }
  }

  private TomType getTypeFromVariableList(TomName name, TomList list) {

    //System.out.println("name = " + name);
    //System.out.println("list = " + list);

    %match(TomName name,TomList list) {
      _,concTomTerm() -> {
        System.out.println("getTypeFromVariableList. Stange case '" + name + "' not found");
        throw new TomRuntimeException("getTypeFromVariableList. Stange case '" + name + "' not found");
      }

      varName, concTomTerm(Variable[AstName=varName,AstType=type@Type[]],_*) -> { return `type; }
      varName, concTomTerm(VariableStar[AstName=varName,AstType=type@Type[]],_*) -> { return `type; }
      _, concTomTerm(_,tail*) -> { return getTypeFromVariableList(name,`tail); }

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

    if(subtermList.isEmptyconcSlot()) {
      return `concSlot();
    }

    //System.out.println("symbol = " + subject.getastname());
    %match(TomSymbol symbol, SlotList subtermList) {
      emptySymbol(), concSlot(PairSlotAppl(slotName,slotAppl),tail*) -> {
        /*
         * if the top symbol is unknown, the subterms
         * are expanded in an empty context
         */
        SlotList sl = expandVariableList(symbol,`tail);
        return `concSlot(PairSlotAppl(slotName,(TomTerm)expandVariable(EmptyContext(),slotAppl)),sl*);
      }

      symb@Symbol[TypesToType=TypesToType(typelist,codomain)],
        concSlot(PairSlotAppl(slotName,slotAppl),tail*) -> {
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
              VariableStar[Option=option,AstName=name,Constraints=constraints] -> {
                ConstraintList newconstraints = (ConstraintList)expandVariable(`TomTypeToTomTerm(codomain),`constraints);
                SlotList sl = expandVariableList(symbol,`tail);
                return `concSlot(PairSlotAppl(slotName,VariableStar(option,name,codomain,newconstraints)),sl*);
              }

              UnamedVariableStar[Option=option,Constraints=constraints] -> {
                ConstraintList newconstraints = (ConstraintList)expandVariable(`TomTypeToTomTerm(codomain),`constraints);
                SlotList sl = expandVariableList(symbol,`tail);
                return `concSlot(PairSlotAppl(slotName,UnamedVariableStar(option,codomain,newconstraints)),sl*);
              }

              _ -> {
                TomType domaintype = `typelist.getHeadconcTomType();
                SlotList sl = expandVariableList(symbol,`tail);
                return `concSlot(PairSlotAppl(slotName,(TomTerm)expandVariable(TomTypeToTomTerm(domaintype), slotAppl)),sl*);

              }
            }
          } else {
            SlotList sl = expandVariableList(symbol,`tail);
            return `concSlot(PairSlotAppl(slotName,(TomTerm)expandVariable(TomTypeToTomTerm(getSlotType(symb,slotName)), slotAppl)),sl*);
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
          RecordAppl[NameList=(opNameAST),Slots=concSlot()] , concTomTerm(_*,var@(Variable|VariableStar)[AstName=opNameAST] ,_*) -> {
            return `var;
          }
          Variable[AstName=opNameAST], concTomTerm(_*,var@Variable[AstName=opNameAST] ,_*) -> {
            return `var;
          }
          VariableStar[AstName=opNameAST],concTomTerm(_*,var@VariableStar[AstName=opNameAST] ,_*) -> {
            return `var;
          }
        }
      }
    }
  }

  protected jjtraveler.Visitable replaceInstantiatedVariable(TomList instantiatedVariable, jjtraveler.Visitable subject) {
    if(instantiatedVariable == null) {
      throw new TomRuntimeException("replaceInstantiatedVariable: null instantiatedVariable");
    }
    try {
      return `ChoiceTopDown(replace_replaceInstantiatedVariable(instantiatedVariable)).visit(subject);
    } catch(VisitFailure e) {
      return subject;
    }
  }

  private TomType getType(String tomName) {
    TomType tomType = getSymbolTable().getType(tomName);
    return tomType;
  }

}
