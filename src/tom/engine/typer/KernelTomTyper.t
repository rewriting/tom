/*
 *
 * TOM - To One Matching Compiler
 * 
 * Copyright (c) 2000-2009, INRIA
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
import tom.engine.adt.tomconstraint.types.constraint.*;
import tom.engine.adt.tomdeclaration.types.*;
import tom.engine.adt.tomexpression.types.*;
import tom.engine.adt.tominstruction.types.*;
import tom.engine.adt.tomname.types.*;
import tom.engine.adt.tomoption.types.*;
import tom.engine.adt.tomsignature.types.*;
import tom.engine.adt.tomterm.types.*;
import tom.engine.adt.tomslot.types.*;
import tom.engine.adt.tomtype.types.*;
import tom.engine.adt.code.types.*;

import tom.engine.tools.SymbolTable;
import tom.engine.tools.ASTFactory;

import tom.library.sl.*;

public class KernelTomTyper {
  %include { ../../library/mapping/java/sl.tom}
  %include { ../../library/mapping/java/util/types/Collection.tom}

  %typeterm KernelTomTyper {
    implement { KernelTomTyper }
    is_sort(t) { ($t instanceof KernelTomTyper) }
  }

  private SymbolTable symbolTable;

  public KernelTomTyper() {
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
  %include { ../../library/mapping/java/util/types/HashMap.tom}
  // ------------------------------------------------------------

  /**
   * If a variable with a type X is found, then all the variables that have the same name and 
   * with type 'unknown' get this type
   *  - apply this for each rhs
   */
  protected Code propagateVariablesTypes(Code workingTerm){
    try{
      return `TopDown(ProcessRhsForVarTypePropagation()).visitLight(workingTerm);  
    } catch(tom.library.sl.VisitFailure e) {
      throw new TomRuntimeException("propagateVariablesTypes: failure on " + workingTerm);
    }
  }
  %strategy ProcessRhsForVarTypePropagation() extends Identity() {
    visit ConstraintInstruction {
      c@ConstraintInstruction(constr,action,option) -> {
        HashMap<String,TomType> varTypes = new HashMap<String,TomType>();
        `TopDown(CollectAllVariablesTypes(varTypes)).visitLight(`constr);        
        return `TopDown(PropagateVariablesTypes(varTypes)).visitLight(`c);        
      }
    }
  }

  %strategy CollectAllVariablesTypes(HashMap map) extends Identity() {
    visit TomTerm {       
      (Variable|VariableStar)[AstName=Name(name),AstType=type] && !EmptyType[] << type  -> {
        if(`type!=SymbolTable.TYPE_UNKNOWN) {
          map.put(`name,`type);
        }
      }
    }
  }

  %strategy PropagateVariablesTypes(HashMap map) extends Identity() {
    visit BQTerm {
      v@(BQVariable|BQVariableStar)[AstName=Name(name),AstType=type] -> {
        if(`type==SymbolTable.TYPE_UNKNOWN || `type.isEmptyType()) {
          if (map.containsKey(`name)) {
            return `v.setAstType((TomType)map.get(`name)); 
          }
        }
      }
    }
    visit TomTerm {
      v@(Variable|VariableStar)[AstName=Name(name),AstType=type] -> {
        if(`type==SymbolTable.TYPE_UNKNOWN || `type.isEmptyType()) {
          if (map.containsKey(`name)) {
            return `v.setAstType((TomType)map.get(`name)); 
          }
        }
      }
    }
  }

  /*
   * The "typeVariable" phase types RecordAppl into Variable
   * we focus on
   * - Match
   *
   * The types of subjects are inferred from the patterns
   *
   * Variable and TermAppl are typed in the TomTerm case
   */

  public <T extends tom.library.sl.Visitable> T typeVariable(TomType contextType, T subject) {
    if(contextType == null) {
      throw new TomRuntimeException("typeVariable: null contextType");
    }
    try {
      //System.out.println("typeVariable: " + contextType);
      //System.out.println("typeVariable subject: " + subject);
      T res = `TopDownStopOnSuccess(replace_typeVariable(contextType,this)).visitLight(subject);
      //System.out.println("res: " + res);
      return res;
    } catch(tom.library.sl.VisitFailure e) {
      throw new TomRuntimeException("typeVariable: failure on " + subject);
    }
  }

  %strategy replace_typeVariable(contextType:TomType,kernelTomTyper:KernelTomTyper) extends Fail() {

    visit Option {
      subject@OriginTracking[] -> { return `subject; }
    }

    visit TargetLanguage {
      subject@TL[] -> { return `subject; }
      subject@ITL[] -> { return `subject; }
      subject@Comment[] -> { return `subject; }
    }

    visit TomType {
      subject@Type(tomType,EmptyType()) -> {
        TomType type = kernelTomTyper.getType(`tomType);
        if(type != null) {
          return type;
        } else {
          return `subject; // useful for SymbolTable.TYPE_UNKNOWN
        }
      }
    }

    visit TomVisit {
      VisitTerm(type,constraintInstructionList,options) -> {
        TomType newType = `kernelTomTyper.typeVariable(contextType,`type);
        HashSet<Constraint> matchAndNumericConstraints = new HashSet<Constraint>();
        `TopDownCollect(CollectMatchAndNumericConstraints(matchAndNumericConstraints)).visitLight(`constraintInstructionList);
        return `VisitTerm(newType, kernelTomTyper.typeConstraintInstructionList(newType,constraintInstructionList,matchAndNumericConstraints),options);
      }
    }

    visit Instruction {
      /*
       * Expansion of a Match construct
       * to add types in subjects
       * to add types in variables of patterns and rhs
       */
      Match(constraintInstructionList, options) -> {
        TomType newType = contextType;
        HashSet<Constraint> matchAndNumericConstraints = new HashSet<Constraint>();
        `TopDownCollect(CollectMatchAndNumericConstraints(matchAndNumericConstraints)).visitLight(`constraintInstructionList);
        return `Match(kernelTomTyper.typeConstraintInstructionList(newType,constraintInstructionList,matchAndNumericConstraints),options);
      }
    }

    visit TomTerm {
      RecordAppl[Option=option,NameList=nameList@(Name(tomName),_*),Slots=slotList,Constraints=constraints] -> {
        TomSymbol tomSymbol = null;
        if(`tomName.equals("")) {
          try {
            tomSymbol = kernelTomTyper.getSymbolFromType(contextType);
            if(tomSymbol==null) {
              throw new TomRuntimeException("No symbol found for type '" + contextType + "'");
            }
            `nameList = `concTomName(tomSymbol.getAstName());
          } catch(UnsupportedOperationException e) {
            // contextType has no AstType slot
            tomSymbol = null;
          }
        } else {
          tomSymbol = kernelTomTyper.getSymbolFromName(`tomName);
        }

        if(tomSymbol != null) {
          SlotList subterm = kernelTomTyper.typeVariableList(tomSymbol, `slotList);
          ConstraintList newConstraints = kernelTomTyper.typeVariable(TomBase.getSymbolCodomain(tomSymbol),`constraints);
          return `RecordAppl(option,nameList,subterm,newConstraints);
        } else {
          //System.out.println("contextType = " + contextType);

          %match(contextType) {
            type@(Type|TypeWithSymbol)[] -> {
              SlotList subterm = kernelTomTyper.typeVariableList(`emptySymbol(), `slotList);
              ConstraintList newConstraints = kernelTomTyper.typeVariable(`type,`constraints);
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

      var@(Variable|UnamedVariable)[AstType=Type(tomType,EmptyType()),Constraints=constraints] -> {
        TomType localType = kernelTomTyper.getType(`tomType);
        //System.out.println("localType = " + localType);
        if(localType != null) {
          // The variable has already a known type
          return `var.setAstType(localType);
        }

        //System.out.println("contextType = " + contextType);
        %match(contextType) {
          (Type|TypeWithSymbol)[TomType=tomType,TlType=tlType] -> {
            TomType ctype = `Type(tomType,tlType);
            ConstraintList newConstraints = kernelTomTyper.typeVariable(ctype,`constraints);
            TomTerm newVar = `var.setAstType(ctype);
            //System.out.println("newVar = " + newVar);
            return newVar.setConstraints(newConstraints);
          }
        }
      }
    }

    visit BQTerm {

      BQAppl[Option=option,AstName=name@Name(tomName),Args=args] -> {
        TomSymbol tomSymbol = null;
        if(`tomName.equals("")) {
          try {
            tomSymbol = kernelTomTyper.getSymbolFromType(contextType);
            if(tomSymbol==null) {
              throw new TomRuntimeException("No symbol found for type '" + contextType + "'");
            }
            `name = `tomSymbol.getAstName();
          } catch(UnsupportedOperationException e) {
            // contextType has no AstType slot
            tomSymbol = null;
          }
        } else {
          tomSymbol = kernelTomTyper.getSymbolFromName(`tomName);
        }

        if(tomSymbol != null) {
          BQTermList subterm = kernelTomTyper.typeVariableList(tomSymbol, `args);
          return `BQAppl(option,name,subterm);
        } else {
          //System.out.println("contextType = " + contextType);

          %match(contextType) {
            type@(Type|TypeWithSymbol)[] -> {
              BQTermList subterm = kernelTomTyper.typeVariableList(`emptySymbol(), `args);
              return `BQAppl(option,name,subterm);
            }

            _ -> {
              // do nothing
              //System.out.println("contextType = " + contextType);
              //System.out.println("subject        = " + subject);
            }
          }
        }
      }

      var@BQVariable[AstType=Type(tomType,EmptyType())] -> {
        TomType localType = kernelTomTyper.getType(`tomType);
        //System.out.println("localType = " + localType);
        if(localType != null) {
          // The variable has already a known type
          return `var.setAstType(localType);
        }

        //System.out.println("contextType = " + contextType);
        %match(contextType) {
          (Type|TypeWithSymbol)[TomType=tomType,TlType=tlType] -> {
            TomType ctype = `Type(tomType,tlType);
            return `var.setAstType(ctype);
          }
        }
      }
    }
  }

  /*
   *
   * @param contextType
   * @param constraintInstructionList a list of ConstraintInstruction
   * @param matchAndNumericConstraints a collection of MatchConstraint and NumericConstraint
   */
  private ConstraintInstructionList typeConstraintInstructionList(TomType contextType, ConstraintInstructionList constraintInstructionList, Collection<Constraint> matchAndNumericConstraints) {
    %match(constraintInstructionList) {
      concConstraintInstruction() -> {
        return constraintInstructionList; 
      }

      concConstraintInstruction(ConstraintInstruction(constraint,action,optionConstraint),tail*) -> { 
        try {
          Collection<TomTerm> lhsVariable = new HashSet<TomTerm>();
          Constraint newConstraint = `TopDownStopOnSuccess(typeConstraint(contextType,lhsVariable,matchAndNumericConstraints,this)).visitLight(`constraint);
          TomList varList = ASTFactory.makeTomList(lhsVariable);
          Instruction newAction = (Instruction) replaceInstantiatedVariable(`varList,`action);
          newAction = typeVariable(`EmptyType(),`newAction);
          ConstraintInstructionList newTail = typeConstraintInstructionList(contextType,`tail,matchAndNumericConstraints);
          return `concConstraintInstruction(ConstraintInstruction(newConstraint,newAction,optionConstraint),newTail*);
        } catch(VisitFailure e) {}
      }
    }        
    throw new TomRuntimeException("Bad ConstraintInstruction: " + constraintInstructionList);
  }

  /**
   * Try to guess the type for the subjects
   * @param contextType the context in which the constraint is typed
   * @param lhsVariable (computed by this strategy) the list of variables that occur in all the lhs 
   * @param matchAndNumericConstraints a collection of MatchConstraint and NumericConstraint
   * @param kernelTomTyper the current class
   */
  %strategy typeConstraint(TomType contextType, Collection lhsVariable,
      Collection matchAndNumericConstraints, KernelTomTyper kernelTomTyper) extends Fail() {
    visit Constraint {
      constraint@MatchConstraint[Pattern=pattern,Subject=subject] -> {
        BQTerm newSubject = `subject;
        TomType newSubjectType = `EmptyType();
        %match(subject) {
          (BQVariable|BQVariableStar)(variableOption,astName@Name(name),tomType) -> {
            BQTerm newVariable = null;
            // tomType may be a TomTypeAlone or a type from an typed variable
            String type = TomBase.getTomType(`tomType);
            //System.out.println("match type = " + type);
            if(kernelTomTyper.getType(`type) == null) {
              /* the subject is a variable with an unknown type */
              newSubjectType = kernelTomTyper.guessSubjectType(`subject,matchAndNumericConstraints);
              if(newSubjectType != null) {
                newVariable = `BQVariable(variableOption,astName,newSubjectType);
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

          t@BQAppl[AstName=n@Name(name),Args=args] -> {
            TomSymbol symbol = kernelTomTyper.getSymbolFromName(`name);
            TomType type = null;
            if(symbol!=null) {
              type = TomBase.getSymbolCodomain(symbol);
              if(type != null) {
                newSubject = `t;
              }
            } else {
              // unknown function call
              type = kernelTomTyper.guessSubjectType(`subject,matchAndNumericConstraints);
              if(type != null) {
                newSubject = `FunctionCall(n,type,args);
              }
            }
            if (type == null) {
              throw new TomRuntimeException("No symbol found for name '" + `name + "'");
            } else {
              newSubjectType = type;
            }                   
          }

          // the user specified the type (already checked for consistence in SyntaxChecker)
          term@BuildReducedTerm[AstType=userType] -> {            
            newSubjectType = `userType;
            newSubject = `term;
          }
          
         c@BuildConstant(Name(n)) -> {
            newSubjectType = TomBase.getSymbolCodomain(kernelTomTyper.getSymbolTable().getSymbolFromName(`n));
            newSubject = `c;
         } 
        } // end match subject     
        
        newSubjectType = kernelTomTyper.typeVariable(contextType,newSubjectType);
        newSubject = kernelTomTyper.typeVariable(newSubjectType, newSubject);                  
        TomTerm newPattern = kernelTomTyper.typeVariable(newSubjectType, `pattern);
        TomBase.collectVariable(lhsVariable,newPattern);
        return `constraint.setPattern(newPattern).setSubject(newSubject);               
      }

      constraint@NumericConstraint[Left=lhs,Right=rhs] -> {
        // if it is numeric, we do not care about the type
        BQTerm newLhs = kernelTomTyper.typeVariable(`EmptyType(), `lhs);                  
        BQTerm newRhs = kernelTomTyper.typeVariable(`EmptyType(), `rhs);                  
        return `constraint.setLeft(newLhs).setRight(newRhs);               
      }       
    } 
  }

  private TomType guessSubjectType(BQTerm subject, Collection matchConstraints) {
    for(Object constr:matchConstraints) {
      %match(constr) {
        MatchConstraint(pattern,s) -> {
          // we want two terms to be equal even if their option is different 
          // ( because of their possition for example )
matchL:  %match(subject,s){
           BQVariable[AstName=astName,AstType=tomType],BQVariable[AstName=astName,AstType=tomType] -> {break matchL;}
           BQAppl[AstName=astName,Args=tomList],BQAppl[AstName=astName,Args=tomList] -> {break matchL;}
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

  /*
   * perform type inference of subterms (subtermList)
   * under a given operator (symbol)
   */
  private BQTermList typeVariableList(TomSymbol symbol, BQTermList subtermList) {
    if(symbol == null) {
      throw new TomRuntimeException("typeVariableList: null symbol");
    }

    if(subtermList.isEmptyconcBQTerm()) {
      return `concBQTerm();
    }

    //System.out.println("symbol = " + symbol.getastname());
    %match(symbol, subtermList) {
      symb@emptySymbol(), concBQTerm(head,tail*) -> {
        /*
         * if the top symbol is unknown, the subterms
         * are typed in an empty context
         */
        BQTermList sl = typeVariableList(`symb,`tail);
        return `concBQTerm(typeVariable(EmptyType(),head),sl*);
      }

      symb@Symbol[AstName=symbolName,TypesToType=TypesToType(typelist,codomain@Type(tomCodomain,tlCodomain))],
        concBQTerm(head,tail*) -> {
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

            %match(head) {
              BQVariableStar[Option=option,AstName=name] -> {
                BQTermList sl = typeVariableList(`symb,`tail);
                return `concBQTerm(BQVariableStar(option,name,TypeWithSymbol(tomCodomain,tlCodomain,symbolName)),sl*);
              }
              _ -> {
                //we cannot know the type precisely (the var can be of domain or codomain type)
                BQTermList sl = typeVariableList(`symb,`tail);
                return `concBQTerm(typeVariable(EmptyType(), head),sl*);
              }
            }
          } else {
            BQTermList sl = typeVariableList(`symb,`tail);
            //TODO: find the correct type of this argument (using its rank)
            return `concBQTerm(typeVariable(EmptyType(), head),sl*);
          }
        }
    }
    throw new TomRuntimeException("typeVariableList: strange case: '" + symbol + "'");
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
        return `concSlot(PairSlotAppl(slotName,typeVariable(EmptyType(),slotAppl)),sl*);
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
                ConstraintList newconstraints = typeVariable(`codomain,`constraints);
                SlotList sl = typeVariableList(`symb,`tail);
                return `concSlot(PairSlotAppl(slotName,VariableStar(option,name,TypeWithSymbol(tomCodomain,tlCodomain,symbolName),newconstraints)),sl*);
              }

              UnamedVariableStar[Option=option,Constraints=constraints] -> {
                ConstraintList newconstraints = typeVariable(`codomain,`constraints);
                SlotList sl = typeVariableList(`symb,`tail);
                return `concSlot(PairSlotAppl(slotName,UnamedVariableStar(option,codomain,newconstraints)),sl*);
              }

              _ -> {
                TomType domaintype = `typelist.getHeadconcTomType();
                SlotList sl = typeVariableList(`symb,`tail);
                SlotList res = `concSlot(PairSlotAppl(slotName,typeVariable(domaintype, slotAppl)),sl*);
                //System.out.println("domaintype = " + domaintype);
                //System.out.println("res = " + res);
                return res;

              }
            }
          } else {
            SlotList sl = typeVariableList(`symb,`tail);
            return `concSlot(PairSlotAppl(slotName,typeVariable(TomBase.getSlotType(symb,slotName), slotAppl)),sl*);
          }
        }
    }
    throw new TomRuntimeException("typeVariableList: strange case: '" + symbol + "'");
  }

  %strategy replace_replaceInstantiatedVariable(instantiatedVariable:TomList) extends Fail() {
    visit TomTerm {
      subject -> {
        %match(subject, instantiatedVariable) {
          RecordAppl[NameList=(opNameAST),Slots=concSlot()] , concTomTerm(_*,var@(Variable|VariableStar)[AstName=opNameAST],_*) -> {
            return `var;
          }
          Variable[AstName=opNameAST], concTomTerm(_*,var@(Variable|VariableStar)[AstName=opNameAST],_*) -> {
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
      return `TopDownStopOnSuccess(replace_replaceInstantiatedVariable(instantiatedVariable)).visitLight(subject);
    } catch(tom.library.sl.VisitFailure e) {
      throw new TomRuntimeException("replaceInstantiatedVariable: failure on " + instantiatedVariable);
    }
  }

  private TomType getType(String tomName) {
    TomType tomType = getSymbolTable().getType(tomName);
    return tomType;
  }

  /**
   * Collect the constraints (match and numeric)
   */
  %strategy CollectMatchAndNumericConstraints(constrList:Collection) extends Identity() {
    visit Constraint {
      c@(MatchConstraint|NumericConstraint)[] -> {        
        constrList.add(`c);
        throw new VisitFailure();// to stop the top-down
      }      
    }
  }
}

