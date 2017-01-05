/*
 *
 * TOM - To One Matching Compiler
 * 
 * Copyright (c) 2000-2017, Universite de Lorraine, Inria
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

public class KernelTyper {
  %include { ../../library/mapping/java/sl.tom}
  %include { ../../library/mapping/java/util/types/Collection.tom}

  private static Logger logger = Logger.getLogger("tom.engine.typer.KernelTyper");
  
  %typeterm KernelTyper {
    implement { KernelTyper }
    is_sort(t) { ($t instanceof KernelTyper) }
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

  // ------------------------------------------------------------
  %include { ../adt/tomsignature/TomSignature.tom }
  %include { ../../library/mapping/java/util/types/HashMap.tom}
  // ------------------------------------------------------------

  /**
   * If a variable with a type X is found, then all the variables that have the same name and 
   * with type 'unknown' get this type
   *  - apply this for each rhs
   */
  protected Code propagateVariablesTypes(Code workingTerm) {
    try{
      return `TopDown(ProcessRhsForVarTypePropagation()).visitLight(workingTerm);  
    } catch(tom.library.sl.VisitFailure e) {
      throw new TomRuntimeException("propagateVariablesTypes: failure on " + workingTerm);
    }
  }

  %strategy ProcessRhsForVarTypePropagation() extends Identity() {
    visit ConstraintInstruction {
      c@ConstraintInstruction(constr,_,_) -> {
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
   * Type(_,EmptyTargetLanguageType()) are expanded
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

  %strategy replace_typeVariable(contextType:TomType,kernelTyper:KernelTyper) extends Fail() {

    visit Option {
      /* cut the traversal */
      subject@OriginTracking[] -> { return `subject; }
    }

    visit TargetLanguage {
      /* cut the traversal */
      subject@TL[] -> { return `subject; }
      subject@ITL[] -> { return `subject; }
      subject@Comment[] -> { return `subject; }
    }

    visit TomType {
      /* Type(_,EmptyTargetLanguageType()) are expanded */
      subject@Type[TomType=tomType,TlType=EmptyTargetLanguageType()] -> {
        TomType type = kernelTyper.getType(`tomType);
        if(type != null) {
          return type;
        } else {
          return `subject; /* useful for SymbolTable.TYPE_UNKNOWN */
        }
      }
    }

    visit TomVisit {
      VisitTerm(type,constraintInstructionList,options) -> {
        /* expands the type (remember that the strategy is applied top-down) */
        TomType newType = `kernelTyper.typeVariable(contextType,`type);
        HashSet<Constraint> matchAndNumericConstraints = new HashSet<Constraint>();
        /* collect one level of MatchConstraint and NumericConstraint */
        `TopDownCollect(CollectMatchAndNumericConstraints(matchAndNumericConstraints)).visitLight(`constraintInstructionList);
        ConstraintInstructionList newConstraintInstructionList = 
          kernelTyper.typeConstraintInstructionList(newType,`constraintInstructionList,matchAndNumericConstraints);
        return `VisitTerm(newType,newConstraintInstructionList,options);
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
        ConstraintInstructionList newConstraintInstructionList = 
          kernelTyper.typeConstraintInstructionList(newType,`constraintInstructionList,matchAndNumericConstraints);
        return `Match(newConstraintInstructionList,options);
      }
    }

    visit TomTerm {
      RecordAppl[Options=optionList,NameList=nameList@concTomName(Name(tomName),_*),Slots=slotList,Constraints=constraints] -> {
        TomSymbol tomSymbol = kernelTyper.getSymbolFromName(`tomName);
        if(tomSymbol != null) {
          SlotList subterm = kernelTyper.typeVariableList(tomSymbol, `slotList);
          ConstraintList newConstraints = kernelTyper.typeVariable(TomBase.getSymbolCodomain(tomSymbol),`constraints);
          return `RecordAppl(optionList,nameList,subterm,newConstraints);
        } else {

          %match(contextType) {
            type@Type[] -> {
              SlotList subterm = kernelTyper.typeVariableList(`EmptySymbol(), `slotList);
              ConstraintList newConstraints = kernelTyper.typeVariable(`type,`constraints);
              return `RecordAppl(optionList,nameList,subterm,newConstraints);
            }

          }
        }
      }

      var@Variable[AstType=Type[TomType=tomType,TlType=EmptyTargetLanguageType()],Constraints=constraints] -> {
        TomType localType = kernelTyper.getType(`tomType);
        if(localType != null) {
          /* The variable has already a known type */
          return `var.setAstType(localType);
        }

        %match(contextType) {
          Type[TypeOptions=tOptions,TomType=tomType,TlType=tlType] -> {
            TomType ctype = `Type(tOptions,tomType,tlType);
            ConstraintList newConstraints = kernelTyper.typeVariable(ctype,`constraints);
            TomTerm newVar = `var.setAstType(ctype);
            return newVar.setConstraints(newConstraints);
          }
        }
      }
    }

    visit BQTerm {

      bq@BQAppl[Options=optionList,AstName=name@Name(tomName),Args=args] -> {
        TomSymbol tomSymbol = kernelTyper.getSymbolFromName(`tomName);
        if(tomSymbol != null) {
          BQTermList subterm = kernelTyper.typeVariableList(tomSymbol, `args);
          return `BQAppl(optionList,name,subterm);
        } else {

          %match(contextType) {
            Type[] -> {
              BQTermList subterm = kernelTyper.typeVariableList(`EmptySymbol(), `args);
              return `BQAppl(optionList,name,subterm);
            }
          }
        }
      }

      var@BQVariable[AstType=Type[TomType=tomType,TlType=EmptyTargetLanguageType()]] -> {
        TomType localType = kernelTyper.getType(`tomType);
        if(localType != null) {
          /* The variable has already a known type */
          return `var.setAstType(localType);
        }

        %match(contextType) {
          Type[TypeOptions=tOptions,TomType=tomType,TlType=tlType] -> {
            TomType ctype = `Type(tOptions,tomType,tlType);
            return `var.setAstType(ctype);
          }
        }
      }
    }
  }

  /*
   ** type all elements of the ConstraintInstructionList
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
          //System.out.println("\n ConstraintInstruction = " + `c);
          Collection<TomTerm> lhsVariable = new HashSet<TomTerm>();
          Constraint newConstraint = `TopDownStopOnSuccess(typeConstraint(contextType,lhsVariable,matchAndNumericConstraints,this)).visitLight(`constraint);
          TomList varList = ASTFactory.makeTomList(lhsVariable);
          Instruction newAction = (Instruction) replaceInstantiatedVariable(`varList,`action);
          newAction = typeVariable(`EmptyType(),`newAction);
          ConstraintInstructionList newTail = typeConstraintInstructionList(contextType,`tail,matchAndNumericConstraints);
          return `concConstraintInstruction(ConstraintInstruction(newConstraint,newAction,optionConstraint),newTail*);
        } catch(VisitFailure e) {
          throw new TomRuntimeException("should not be there");
        }
      }
    }        
    throw new TomRuntimeException("Bad ConstraintInstruction: " + constraintInstructionList);
  }

  /**
   * Try to guess the type for the subjects
   * @param contextType the context in which the constraint is typed
   * @param lhsVariable (computed by this strategy) the list of variables that occur in all the lhs 
   * @param matchAndNumericConstraints a collection of MatchConstraint and NumericConstraint
   * @param kernelTyper the current class
   */
  %strategy typeConstraint(TomType contextType, Collection lhsVariable,
      Collection matchAndNumericConstraints, KernelTyper kernelTyper) extends Fail() {
    visit Constraint {
      constraint@MatchConstraint[Pattern=pattern,Subject=subject,AstType=aType] -> {
        BQTerm newSubject = `subject;
        TomType newSubjectType = `EmptyType();
        %match(subject) {
          (BQVariable|BQVariableStar)(variableOptions,astName@Name(name),_) -> {
            `subject = `subject.setAstType(`aType);
            newSubject = `subject;
            /* tomType may be a Type(_,EmptyTargetLanguageType()) or a type from an typed variable */
            String type = TomBase.getTomType(`aType);
            if(kernelTyper.getType(type) == null) {
              /* the subject is a variable with an unknown type */
              newSubjectType = kernelTyper.guessSubjectType(`subject,matchAndNumericConstraints);
              if(newSubjectType != null) {
                newSubject = `BQVariable(variableOptions,astName,newSubjectType);
              } else {
                TomMessage.error(logger,null,0, TomMessage.cannotGuessMatchType,`name);
                throw new VisitFailure();
              }
            } else {
              newSubject = `subject;
            }
            newSubjectType = newSubject.getAstType();
          }

          t@BQAppl[AstName=n@Name(name),Args=args] -> {
            TomSymbol symbol = kernelTyper.getSymbolFromName(`name);
            TomType type = null;
            if(symbol!=null) {
              type = TomBase.getSymbolCodomain(symbol);
              if(type != null) {
                newSubject = `t;
              }
            } else {
              /* unknown function call */
              type = kernelTyper.guessSubjectType(`subject,matchAndNumericConstraints);
              if(type != null) {
                newSubject = `FunctionCall(n,type,args);
              }
            }
            if(type == null) {
              throw new TomRuntimeException("No symbol found for name '" + `name + "'");
            } else {
              newSubjectType = type;
            }                   
          }

          c@BuildConstant(Name(name)) -> {
            newSubject = `c;
            TomSymbol symbol = kernelTyper.getSymbolFromName(`name);
            TomType type = TomBase.getSymbolCodomain(symbol);
            if(type!=null) {
              newSubjectType = type;
            } else {
              throw new TomRuntimeException("No type found for name '" + `name + "'");
            }
          } 
        } // end match subject     

        newSubjectType = kernelTyper.typeVariable(contextType,newSubjectType);
        newSubject = kernelTyper.typeVariable(newSubjectType, newSubject);                  
        TomTerm newPattern = kernelTyper.typeVariable(newSubjectType, `pattern);
        TomBase.collectVariable(lhsVariable,newPattern,false);
        return
          `constraint.setPattern(newPattern).setSubject(newSubject).setAstType(newSubjectType);               
      }

      NumericConstraint[Left=lhs,Right=rhs,Type=nct] -> {
        /* if it is numeric, we do not care about the type */
        BQTerm newLhs = kernelTyper.typeVariable(`EmptyType(), `lhs);                  
        BQTerm newRhs = kernelTyper.typeVariable(`EmptyType(), `rhs);                  
        return `NumericConstraint(lhs,rhs,nct);
      }       
    } 
  }

  private TomType guessSubjectType(BQTerm subject, Collection matchConstraints) {
    for(Object constr:matchConstraints) {
      %match(constr) {
        MatchConstraint[Pattern=pattern,Subject=s] -> {
          /* we want two terms to be equal even if their option is different */
          /* ( because of their position for example ) */
matchL:  %match(subject,s) {
           BQVariable[AstName=astName,AstType=tomType],BQVariable[AstName=astName,AstType=tomType] -> {break matchL;}
           BQAppl[AstName=astName,Args=tomList],BQAppl[AstName=astName,Args=tomList] -> {break matchL;}
           _,_ -> { continue; }
         }
         TomTerm patt = `pattern;
         %match(pattern) {
           AntiTerm(p) -> { patt = `p; }
         }
         %match(patt) {
           (TermAppl|RecordAppl)[NameList=concTomName(Name(name),_*)] -> {        
             TomSymbol symbol = getSymbolFromName(`name);
             if(symbol != null) {
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
      symb@EmptySymbol(), concBQTerm(head,tail*) -> {
        /*
         * if the top symbol is unknown, the subterms
         * are typed in an empty context
         */
        BQTermList sl = typeVariableList(`symb,`tail);
        return `concBQTerm(typeVariable(EmptyType(),head),sl*);
      }

      symb@Symbol[AstName=symbolName,TypesToType=TypesToType(_,Type[TypeOptions=tOptions,TomType=tomCodomain,TlType=tlCodomain])],
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
              BQVariableStar[Options=optionList,AstName=name] -> {
                BQTermList sl = typeVariableList(`symb,`tail);
                TypeOptionList newTOptions = `tOptions;
                %match {
                  concTypeOption(_*,WithSymbol[RootSymbolName=rsName],_*) <<
                    tOptions && (rsName != symbolName) -> {
                      throw new TomRuntimeException("typeVariableList: symbol '"
                          + `symb + "' with more than one constructor (rootsymbolname)");
                    }
                  concTypeOption(_*,!WithSymbol[],_*) << tOptions -> {
                    newTOptions =
                      `concTypeOption(WithSymbol(symbolName),tOptions*);
                  }
                }
                return
                  `concBQTerm(BQVariableStar(optionList,name,Type(newTOptions,tomCodomain,tlCodomain)),sl*);
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
      symb@EmptySymbol(), concSlot(PairSlotAppl(slotName,slotAppl),tail*) -> {
        /*
         * if the top symbol is unknown, the subterms
         * are typed in an empty context
         */
        SlotList sl = typeVariableList(`symb,`tail);
        return `concSlot(PairSlotAppl(slotName,typeVariable(EmptyType(),slotAppl)),sl*);
      }

      symb@Symbol[AstName=symbolName,TypesToType=TypesToType(typelist,codomain@Type[TypeOptions=tOptions,TomType=tomCodomain,TlType=tlCodomain])],
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
              VariableStar[Options=optionList,AstName=name,Constraints=constraints] -> {
                ConstraintList newconstraints = typeVariable(`codomain,`constraints);
                SlotList sl = typeVariableList(`symb,`tail);
                TypeOptionList newTOptions = `tOptions;
                %match {
                  concTypeOption(_*,WithSymbol[RootSymbolName=rsName],_*) <<
                    tOptions && (rsName != symbolName) -> {
                      throw new TomRuntimeException("typeVariableList: symbol '"
                          + `symb + "' with more than one constructor (rootsymbolname)");
                    }
                  concTypeOption(_*,!WithSymbol[],_*) << tOptions -> {
                    newTOptions =
                      `concTypeOption(WithSymbol(symbolName),tOptions*);
                  }
                }
                return
                  `concSlot(PairSlotAppl(slotName,VariableStar(optionList,name,Type(newTOptions,tomCodomain,tlCodomain),newconstraints)),sl*);
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

  // Strategy called when there exist a %match with another one (or more) %match
  // inside it, so tthe strategy links all variables which have the same name
  %strategy replace_replaceInstantiatedVariable(instantiatedVariable:TomList) extends Fail() {
    visit TomTerm {
      subject -> {
        %match(subject, instantiatedVariable) {
          RecordAppl[NameList=concTomName(opNameAST),Slots=concSlot()] , concTomTerm(_*,var@(Variable|VariableStar)[AstName=opNameAST],_*) -> {
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
      //System.out.println("\nvarlist = " + instantiatedVariable);
      //System.out.println("\nsubject = " + subject);
      return `TopDownStopOnSuccess(replace_replaceInstantiatedVariable(instantiatedVariable)).visitLight(subject);
    } catch(tom.library.sl.VisitFailure e) {
      throw new TomRuntimeException("replaceInstantiatedVariable: failure on " + instantiatedVariable);
    }
  }

  private TomType getType(String tomName) {
    return getSymbolTable().getType(tomName);
  }

  /**
   * Collect the constraints (match and numeric)
   */
  %strategy CollectMatchAndNumericConstraints(constrList:Collection) extends Identity() {
    visit Constraint {
      c@(MatchConstraint|NumericConstraint)[] -> {        
        constrList.add(`c);
        throw new VisitFailure();/* to stop the top-down */
      }      
    }
  }
}

