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
   * Variable and TermAppl are expanded in the TomTerm case
   */

  %strategy replace_expandVariable(contextType:TomType,expander:TomKernelExpander) extends `Identity() {

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
	TomTerm newLhs = `Term((TomTerm)expander.expandVariable(contextType,lhs));
	// build the list of variables that occur in the lhs
	HashSet set = new HashSet();
	collectVariable(set,newLhs);
	TomList varList = ASTFactory.makeList(set);
	InstructionList newCondList = `concInstruction();
	while(!`condList.isEmptyconcInstruction()) {
	  Instruction cond = `condList.getHeadconcInstruction();

	  Instruction newCond = (Instruction)expander.replaceInstantiatedVariable(`varList,cond);
	  newCond = (Instruction) expander.expandVariable(contextType,newCond);

	  newCondList = `concInstruction(newCond,newCondList*);
	  collectVariable(set,newCond);
	  varList = ASTFactory.makeList(set);
	  `condList = `condList.getTailconcInstruction();
	}

	TomTerm newRhs = (TomTerm)expander.replaceInstantiatedVariable(`varList,`rhs);
	newRhs = `Term((TomTerm)expander.expandVariable(symbolType,newRhs));

	return `RewriteRule(newLhs,newRhs,newCondList,option);
      }
    }

    visit TomVisit {
      VisitTerm(type,patternInstructionList,options) -> {
	TomType newType = (TomType)`expander.expandVariable(contextType,`type);
	PatternInstructionList newPatternInstructionList = (PatternInstructionList)expander.expandVariable(newType,`patternInstructionList);
	return `VisitTerm(newType, newPatternInstructionList,options);
      }
    }
    visit Instruction {
      MatchingCondition[Lhs=lhs@Variable[AstName=Name(_), AstType=lhsType],
	Rhs=rhs@Variable[AstName=Name(_), AstType=rhsType]] -> {
	  TomTerm newLhs = (TomTerm)expander.expandVariable(`rhsType,`lhs);
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

	  TomTerm newLhs = (TomTerm)expander.expandVariable(`type,`lhs);
	  TomTerm newRhs = (TomTerm)expander.expandVariable(`type,`rhs);
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

	  TomTerm newLhs = (TomTerm)expander.expandVariable(`type,`lhs);
	  TomTerm newRhs = (TomTerm)expander.expandVariable(`type,`rhs);
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

	  TomTerm newLhs = (TomTerm)expander.expandVariable(`type,`lhs);
	  TomTerm newRhs = (TomTerm)expander.expandVariable(`type,`rhs);
	  return `MatchingCondition(newLhs,newRhs);
	}

      EqualityCondition[Lhs=lhs@Variable[AstName=Name(_), AstType=type],
	Rhs=rhs@Variable[AstName=Name(_), AstType=type]] -> {
	  return `TypedEqualityCondition(type,lhs,rhs);
	}

      EqualityCondition[Lhs=lhs@Variable[AstName=Name(_), AstType=type],
	Rhs=rhs@RecordAppl[NameList=(Name(_))]] -> {
	  TomTerm newRhs = (TomTerm)expander.expandVariable(`type,`rhs);
	  return `TypedEqualityCondition(type,lhs,newRhs);
	}

      EqualityCondition[Lhs=lhs@RecordAppl[NameList=(Name(_))],
	Rhs=rhs@Variable[AstName=Name(_), AstType=type]] -> {
	  TomTerm newLhs = (TomTerm)expander.expandVariable(`type,`lhs);
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

	  TomTerm newLhs = (TomTerm)expander.expandVariable(`type,`lhs);
	  TomTerm newRhs = (TomTerm)expander.expandVariable(`type,`rhs);

	  //System.out.println("lhs    = " + lhs);
	  //System.out.println("newLhs = " + newLhs);

	  return `TypedEqualityCondition(type,newLhs,newRhs);
	}

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
	      %match(subject) {
		Variable(variableOption,astName@Name(name),tomType@TomTypeAlone(type),constraints) -> {
		  TomTerm newVariable = null;
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
		  }

		  break matchBlock;
		}

		t@(TermAppl|RecordAppl|ListAppl)[NameList=concTomName(tomName@Name(name),_*)] -> {
		  TomSymbol symbol = expander.getSymbolFromName(`name);
		  TomType type = null;
		  if(symbol!=null) {
		    type = getSymbolCodomain(symbol);
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
		}

	      }
	    } // end matchBlock
	    index++;
	    `tomSubjectList=`tomSubjectList.getTailconcTomTerm();
	}

      TomTerm newTomSubjectList = (TomTerm)expander.expandVariable(contextType, `SubjectList(ASTFactory.makeList(newSubjectList)));
      //System.out.println("newTomSubjectList = " + newTomSubjectList);
      TomType newTypeList = `TypeList((TomTypeList)expander.expandVariable(contextType,typeList));

      PatternInstructionList newPatternInstructionList = (PatternInstructionList)expander.expandVariable(newTypeList,`patternInstructionList);
      //System.out.println("newPatternInstructionList = " + newPatternInstructionList);
      return `Match(newTomSubjectList,newPatternInstructionList, option);
    }
  }



  /*
   * given a list of subjects
   * for each pattern, perform type expansion according to the type of subjects
   */
  visit Pattern {
    Pattern(subjectList,termList, guardList) -> {
      %match(contextType) {
	TypeList(typeList) -> {
	  //System.out.println("expandVariable.9: "+l1+"(" + termList + ")");

	  // process a list of subterms
	  ArrayList list = new ArrayList();
	  while(!`termList.isEmptyconcTomTerm()) {
	    //System.out.println("type: " + `typeList.getHeadconcTomType());
	    //System.out.println("term: " + `termList.getHeadconcTomTerm());
	    list.add((TomTerm)expander.expandVariable(`typeList.getHeadconcTomType(), `termList.getHeadconcTomTerm()));
	    `termList = `termList.getTailconcTomTerm();
	    `typeList = `typeList.getTailconcTomType();
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
	ConstraintList newConstraints = (ConstraintList)expander.expandVariable(getSymbolCodomain(tomSymbol),`constraints);
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
      if(localType != null) {
	// The variable has already a known type
	return `var.setAstType(localType);
      }

      %match(contextType){
	type@Type[] ->{
	  ConstraintList newConstraints = (ConstraintList)expander.expandVariable(`type,`constraints);
	  return `var.setAstType(`type).setConstraints(newConstraints);
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
    concPatternInstruction(_*, PatternInstruction[
	Pattern=Pattern[TomList=concTomTerm(X*,(TermAppl|RecordAppl|ListAppl)[NameList=concTomName(Name(name),_*)],_*)]], _*) -> {
      //System.out.println("X.length = " + `X*.length());
      if(`X*.length() == index) {
	TomSymbol symbol = getSymbolFromName(`name);
	//System.out.println("name = " + `name);
	if(symbol!=null) {
	  TomType newType = getSymbolCodomain(symbol);
	  //System.out.println("newType = " + `newType);
	  return `newType;
	} else {
	  return null;
	}
      }
    }
  }
  return null;
}

protected jjtraveler.Visitable expandVariable(TomType contextType, jjtraveler.Visitable subject) {
  if(contextType == null) {
    throw new TomRuntimeException("expandVariable: null contextType");
  }
  try{
    return `ChoiceTopDown(replace_expandVariable(contextType,this)).visit(subject);
  } catch(VisitFailure e) {
    return subject;
  }
}

private TomType getTypeFromVariableList(TomName name, TomList list) {

  //System.out.println("name = " + name);
  //System.out.println("list = " + list);

  %match(list) {
    concTomTerm() -> {
      System.out.println("getTypeFromVariableList. Stange case '" + name + "' not found");
      throw new TomRuntimeException("getTypeFromVariableList. Stange case '" + name + "' not found");
    }

    concTomTerm(Variable[AstName=varName,AstType=type@Type[]],_*) -> { if(name==`varName) return `type; }
    concTomTerm(VariableStar[AstName=varName,AstType=type@Type[]],_*) -> { if(name==`varName) return `type; }
    concTomTerm(_,tail*) -> { return getTypeFromVariableList(name,`tail); }

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
	      return `concSlot(PairSlotAppl(slotName,(TomTerm)expandVariable(domaintype, slotAppl)),sl*);

	    }
	  }
	} else {
	  SlotList sl = expandVariableList(symbol,`tail);
	  return `concSlot(PairSlotAppl(slotName,(TomTerm)expandVariable(getSlotType(symb,slotName), slotAppl)),sl*);
	}
      }
  }
  System.out.println("expandVariableList: strange case: '" + symbol + "'");
  throw new TomRuntimeException("expandVariableList: strange case: '" + symbol + "'");
}

%strategy replace_replaceInstantiatedVariable(instantiatedVariable:TomList) extends `Identity() {
  visit TomTerm {
    subject -> {
      %match(subject, instantiatedVariable) {
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
