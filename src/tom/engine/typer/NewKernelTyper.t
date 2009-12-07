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
 * Cláudia Tavares  e-mail: Claudia.Tavares@loria.fr
 * Jean-Christophe Bach e-mail: Jeanchristophe.Bach@loria.fr
 *
 **/


//TODO

package tom.engine.typer;

import java.util.ArrayList;
import java.util.HashMap;

import tom.engine.adt.tomsignature.*;
import tom.engine.adt.tomsignature.types.*;
import tom.engine.adt.tomconstraint.types.*;
import tom.engine.adt.tomconstraint.types.constraint.*;
import tom.engine.adt.tomdeclaration.types.*;
import tom.engine.adt.tomexpression.types.*;
import tom.engine.adt.tominstruction.types.*;
import tom.engine.adt.tomname.types.*;
import tom.engine.adt.tomname.types.tomname.*;
import tom.engine.adt.tomoption.types.*;
import tom.engine.adt.tomslot.types.*;
import tom.engine.adt.tomtype.types.*;
import tom.engine.adt.tomtype.types.tomtypelist.concTomType;
import tom.engine.adt.tomterm.types.*;
import tom.engine.adt.tomterm.types.tomlist.concTomTerm;
import tom.engine.adt.code.types.*;
import tom.engine.adt.code.types.bqtermlist.concBQTerm;

import tom.engine.adt.typeconstraints.*;
import tom.engine.adt.typeconstraints.types.*;

import tom.engine.TomBase;
import tom.engine.TomMessage;
import tom.engine.exception.TomRuntimeException;

import tom.engine.tools.SymbolTable;
import tom.engine.tools.ASTFactory;

import tom.library.sl.*;

public class NewKernelTyper {
  %include { ../../library/mapping/java/sl.tom}
//  %include { ../../library/mapping/java/util/types/Collection.tom}
  %include { ../adt/tomsignature/TomSignature.tom }
//  %include { ../../library/mapping/java/util/types/HashMap.tom}

  %typeterm NewKernelTyper {
    implement { NewKernelTyper }
    is_sort(t) { ($t instanceof NewKernelTyper) }
  }

//  %typeterm SubstitutionList { implement { HashMap<TomType,TomType> } }

  private SymbolTable symbolTable;
  private int freshTypeVarCounter = 0;
  // List for variables of pattern (match constraints)
  private TomList varPatternList;
  // List for variables of subject and of numeric constraints
  private BQTermList varList;
  // List for type constraints (for fresh type variables)
  private TypeConstraintList typeConstraints;
  // Set of pairs (freshVar,groundVar)
  private HashMap<TomType,TomType> substitutions;

  public NewKernelTyper() {
    super();
  }

  public SymbolTable getSymbolTable() {
    return this.symbolTable;
  }

  public void setSymbolTable(SymbolTable symbolTable) {
    this.symbolTable = symbolTable;
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

  protected TomType getType(TomTerm term) {
    %match(term) {
      (Variable|VariableStar)[AstType=type] -> {
        return `type;
      }
      RecordAppl[NameList=(Name(name),_*)] -> {
        if(`name.equals("")) {
          // Maybe we need to discover the symbol using the context type
          // information (i.e. the type of subject)
          throw new TomRuntimeException("No symbol found because there is no name.");
        }
        TomSymbol tomSymbol = getSymbolFromName(`name);
        return TomBase.getSymbolCodomain(tomSymbol);
      }
    } 
    throw new TomRuntimeException("getType: should not be here.");
  }

  // TODO
  protected TomType getType(BQTerm term) {
    return `EmptyType(); 
  }

  protected TomType getFreshTypeVar() {
    return `TypeVar(freshTypeVarCounter++);
  }

  protected void addConstraint(TypeConstraint constraint) {
    this.typeConstraints = `concTypeConstraint(constraint,typeConstraints*);
  }

  protected void addTomTerm(TomTerm term) {
    ((`concTomTerm) varPatternList).append(term);
  }

  protected void addBQTerm(BQTerm term) {
    ((`concBQTerm) varList).append(term);
  }

  //Before resetting varPatternList, we need to check if varList contains
  //a corresponding BQTerm, and also remove it from varList
  protected void resetVarPatternList() {
    try {
      `TopDown(removeFromVarList(this)).visitLight(varList);
    } catch(tom.library.sl.VisitFailure e) {
      throw new TomRuntimeException("resetVarPatternList: failure on " + varList);
    }
    varPatternList = `concTomTerm();
  }

  protected void initBQTerm() {
    varList = `concBQTerm();
  }

  %strategy removeFromVarList(nkt:NewKernelTyper) extends Identity() {
    visit BQTermList {
      list -> {
        %match(nkt.varPatternList,list) {
          concTomTerm(_*,(Variable|VariableStar)[AstName=name],_*),concBQTerm(x*,(BQVariable|BQVariableStar)[AstName=name],y*) -> {
            return `concBQTerm(x*,y*);
          }
        }
      }
    }
  }

  private void init() {
    varPatternList = `concTomTerm();
    varList = `concBQTerm();
    typeConstraints = `concTypeConstraint();
    substitutions = new HashMap<TomType,TomType>();
  }

  private Code inferTypeCode(Code code) {
    try {
//      Code result =
       return `TopDownStopOnSuccess(splitConstraintInstruction(this)).visitLight(code);
    } catch(tom.library.sl.VisitFailure e) {
      throw new TomRuntimeException("inferTypeCode: failure on " + code);
    }
  }

  %strategy splitConstraintInstruction(nkt:NewKernelTyper) extends Fail() {
    visit Instruction {
      Match(constraintInstructionList,options) -> {
        nkt.init();
        // Generate type constraints for a %match
        ConstraintInstructionList result = nkt.inferConstraintInstructionList(`constraintInstructionList);
        nkt.solveConstraints();
        result = nkt.replaceInConstraintInstructionList(`result);
        return `Match(result,options);
      }
    } 
  }

  private ConstraintInstructionList inferConstraintInstructionList(ConstraintInstructionList cilist) {
    %match(cilist) {
      concConstraintInstruction() -> { return cilist; }
      concConstraintInstruction(headCIList@ConstraintInstruction(constraint,action,_),tailCIList*) -> {
        try {
          // Collect variables and type them with fresh type variables
          // Rename variables of pattern that already exist in varPatternList
          ConstraintInstruction typedHead =
            `TopDownCollect(CollectVars(this)).visitLight(`headCIList);
          `TopDown(inferConstraint(this)).visitLight(`constraint);
          // In inferInstruction we can call this method
          // inferConstraintInstructionList 
          inferInstruction(`action);
          resetVarPatternList();
          ConstraintInstructionList typedTail =
            `inferConstraintInstructionList(tailCIList);
          return `concConstraintInstruction(typedHead, typedTail*);

        } catch(tom.library.sl.VisitFailure e) {
          throw new TomRuntimeException("inferConstraintInstructionList: failure on " + `headCIList);

        }
      }
    }
    throw new TomRuntimeException("inferConstraintInstructionList: should not be here.");
  }

  %strategy CollectVars(nkt:NewKernelTyper) extends Identity() {
    visit TomTerm {
      var -> {
        %match(var,nkt.varPatternList) {
          // Case of Variable
          // If the variable already exists in varPatternList
          Variable[Option=option,AstName=name,AstType=type1,Constraints=cilist], 
          concTomTerm(_*,(Variable|VariableStar)(_,name,type2,_),_*) -> {
            if(`type1 != SymbolTable.TYPE_UNKNOWN) {
              nkt.addConstraint(`Equation(type1,type2));
            }
            return `Variable(option,name,type2,cilist);
          }
          Variable[Option=option,AstName=name1,AstType=type1,Constraints=cilist], 
          concTomTerm(_*,(Variable|VariableStar)(_,name2@!name1,_,_),_*) -> {
            TomTerm newVar = `var;
            if(`type1 == SymbolTable.TYPE_UNKNOWN) {
              TomType freshType = nkt.getFreshTypeVar();
              newVar = `Variable(option,name1,freshType,cilist);
            }
            nkt.addTomTerm(newVar);
            return newVar;
          }

          // Case of VariableStar
          VariableStar[Option=option,AstName=name,AstType=type1,Constraints=cilist], 
          concTomTerm(_*,(Variable|VariableStar)(_,name,type2,_),_*) -> {
            if(`type1 != SymbolTable.TYPE_UNKNOWN) {
              nkt.addConstraint(`Equation(type1,type2));
            }
            return `VariableStar(option,name,type2,cilist);
          }
          VariableStar[Option=option,AstName=name1,AstType=type1,Constraints=cilist], 
          concTomTerm(_*,(Variable|VariableStar)(_,name2@!name1,_,_),_*) -> {
            TomTerm newVar = `var;
            if(`type1 == SymbolTable.TYPE_UNKNOWN) {
              TomType freshType = nkt.getFreshTypeVar();
              newVar = `VariableStar(option,name1,freshType,cilist);
            }
            nkt.addTomTerm(newVar);
            return newVar;
          }
        }
      }
    }
    visit BQTerm {
      bqvar -> {
        %match(bqvar,nkt.varList) {
          // Case of Variable
          // If the variable already exists in varPatternList
          BQVariable[Option=option,AstName=name,AstType=type1], 
          concBQTerm(_*,(BQVariable|BQVariableStar)(_,name,type2),_*) -> {
            if(`type1 != SymbolTable.TYPE_UNKNOWN) {
              nkt.addConstraint(`Equation(type1,type2));
            }
            return `BQVariable(option,name,type2);
          }
          BQVariable[Option=option,AstName=name1,AstType=type1], 
          concBQTerm(_*,(BQVariable|BQVariableStar)(_,name2@!name1,_),_*) -> {
            BQTerm newVar = `bqvar;
            if(`type1 == SymbolTable.TYPE_UNKNOWN) {
              TomType freshType = nkt.getFreshTypeVar();
              newVar = `BQVariable(option,name1,freshType);
            }
            nkt.addBQTerm(newVar);
            return newVar;
          }

          // Case of VariableStar
          BQVariableStar[Option=option,AstName=name,AstType=type1], 
          concBQTerm(_*,(BQVariable|BQVariableStar)(_,name,type2),_*) -> {
            if(`type1 != SymbolTable.TYPE_UNKNOWN) {
              nkt.addConstraint(`Equation(type1,type2));
            }
            return `BQVariableStar(option,name,type2);
          }
          BQVariableStar[Option=option,AstName=name1,AstType=type1], 
          concBQTerm(_*,(BQVariable|BQVariableStar)(_,name2@!name1,_),_*) -> {
            BQTerm newVar = `bqvar;
            if(`type1 == SymbolTable.TYPE_UNKNOWN) {
              TomType freshType = nkt.getFreshTypeVar();
              newVar = `BQVariableStar(option,name1,freshType);
            }
            nkt.addBQTerm(newVar);
            return newVar;
          }
        }
      }
    }
  } 

  %strategy inferConstraint(nkt:NewKernelTyper) extends Identity() {
    visit Constraint {
      constraint -> {
        %match {
          MatchConstraint(pattern,subject) << constraint &&
          concTomTerm(_*,pattern,_*) << nkt.varPatternList &&
          concBQTerm(_*,subject,_*) << nkt.varList -> {
            TomType freshType1 = nkt.getFreshTypeVar();
            TomType freshType2 = nkt.getFreshTypeVar();
            nkt.addConstraint(`Equation(freshType1,nkt.getType(pattern)));
            nkt.addConstraint(`Equation(freshType2,nkt.getType(subject)));
            nkt.inferTomTerm(`pattern,freshType1);
            nkt.inferBQTerm(`subject,freshType2);
          }
          NumericConstraint(left,right,_) << constraint &&
          concBQTerm(_*,left,_*) << nkt.varList &&
          concBQTerm(_*,right,_*) << nkt.varList -> {
            TomType freshType1 = nkt.getFreshTypeVar();
            TomType freshType2 = nkt.getFreshTypeVar();
            // It will be useful for subtyping
            // TomType freshType3 = nkt.getFreshTypeVar();
            nkt.addConstraint(`Equation(freshType1,nkt.getType(left)));
            nkt.addConstraint(`Equation(freshType2,nkt.getType(right)));
            nkt.addConstraint(`Equation(freshType1,freshType2));
            nkt.inferBQTerm(`left,freshType1);
            nkt.inferBQTerm(`right,freshType2);

          }
        }
      }
    }  
  } 

  private void inferTomTerm(TomTerm term, TomType type) {}

  private void inferBQTerm(BQTerm term, TomType type) {}

  /*
   public void typeVariableList(TomSymbol symb, TomTypeList domain, TomType te, SlotList slist) {
    if(TomBase.isListOperator(`symb) || TomBase.isArrayOperator(`symb)) {
      %match(slist) {
        concSlot(VariableStar(_, n, t, _), s*) -> {
              //inferTerm(fs, t);
              addConstraint(`Equation(t,te));
              typeVariableList(symb, domain, te, s*);
              break;
            }
            concSlot(Variable(_, n, t, _), s*) -> {
              TomType tt = getFreshTypeVar();
              //inferTerm(,);
              addConstraint(`Equation(t,tt));
              typeVariableList();
              break;
            }
            concSlot(RecordAppl(_,concTomName(Name(tn),_*),_,_), s*) -> {
                //fdom = getSimpleDom();
                //syme = findSymblo(tn, fdom);
                //if(syme!= symb) {
                //tt = getFreshTypeVar();
                //addConstraint(`Equation(tt, fdom));
                //inferTerm(fs, tt);
                //} else {
                //  inferTerm(fs, te);
                //}
                typeVariableList(symb, domain, te, s);
                break;
            }
      }
    } else {
      while(!domain.isEmptyconcTomType()) {
        TomType tt = getFreshTypeVar();
        addconstraint(`Equation(tt, domain.getHeadconcTomType())); //Eq(tt, fdom)
        TomTerm farg = slist.getHeadconcSlot().getAppl();
        inferTerm(farg, tt);
        domain = domain.getTailconcTomType();
        slist = slist.getTailconcSlot();
      }
    } 
  }
*/

  //TODO
  private void inferInstruction(Instruction action) {
    // addTypeConstraints
  }


  /*
  public void inferNumMatch(TomTerm e1, TomTerm e2) {
    TomType t1 = getFreshTypeVar();
    TomType t2 = getFreshTypeVar();
    addConstraint(`Equation(t1,t2));
    inferTerm(e1, t1);
    inferTerm(e2, t2);
  }

  public void inferTerm(TomTerm e, TomType te) {
    %match(e) {
      (Variable|VariableStar)(_, _,type, _) -> { addConstraint(`Equation(type,te)); }
      RecordAppl(optionList, NameList(headName,_*), slotList, constraintList) -> {
        String name = null;
        TomType t;
        if(`(headName) instanceof AntiName) {
          name = ((AntiName)`headName).getName().getString();
        } else {
          name = ((TomName)`headName).getString();
        }
        TomSymbol symbol = getSymbolTable().getSymbolFromName(name);
        if(symbol!=null) {
          t = symbol.getTypesToType().getCodomain();
        } else {
          t = `EmptyType();
        }

        addConstraint(`Equation(t,te));

        concTomType tl;
        if(symbol!=null) {
          tl = symbol.getTypesToType().getDomain();
        } else {
          tl = `concTomType();
        }

        typeVariableList(symbol, tl, te, slotList);
      }
    }
  }

  public void inferCond() {}

  public void inferBlock() {
    //initGlobal()
    //collectSubjectVariables()
  }

  public void inferRule(Instruction instr, TomList localContext) {
    //  %match(instr) {
    //   Instruction(cond, action) -> {
    //initLocal()
    //inferCond()
    //propagate()
    // }
    //  }
  }

  public TypeConstraintList addConstraint(TypeConstraint constraint, TypeConstraintList
      constraintList) {
    return `concTypeConstraint(constraint,constraintList);
  }
*/
  // TODO
  private void solveConstraints() {
    // Add a new substitution in "substitutions"
  }

  // TODO
  private ConstraintInstructionList replaceInCode(ConstraintInstructionList
      cilist) {
    return cilist;
  }

  //TODO
  private ConstraintInstructionList replaceInConstraintInstructionList(ConstraintInstructionList cil) {
    return `cil;
  }


/*
  %strategy propagate(nkt:NewKernelTyper) extends Fail() {
  }
*/
} // NewKernelTyper
