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
package tom.engine.compiler;

import java.util.*;

import java.lang.reflect.*;

import tom.engine.TomBase;
import tom.engine.adt.tomterm.types.*;
import tom.engine.adt.tomterm.types.tomterm.*;
import tom.engine.adt.tomname.types.*;
import tom.engine.adt.tomname.types.tomname.*;
import tom.engine.adt.tominstruction.types.*;
import tom.engine.adt.tomconstraint.types.*;
import tom.engine.adt.tomconstraint.types.constraint.*;
import tom.engine.adt.tomexpression.types.*;
import tom.engine.tools.SymbolTable;
import tom.engine.compiler.*;
import tom.engine.compiler.generator.*;
import tom.engine.exception.TomRuntimeException;
import tom.engine.adt.tomsignature.types.*;
import tom.engine.adt.tomtype.types.*;
import tom.engine.adt.code.types.*;
import tom.library.sl.*;

/**
 * This class is in charge with launching all the generators,
 * until no more generations can be made 
 */
public class ConstraintGenerator {

//------------------------------------------------------------	
  %include { ../adt/tomsignature/TomSignature.tom }	
  %include { java/util/types/Collection.tom}
  %include { ../../library/mapping/java/sl.tom}
//------------------------------------------------------------	

  %typeterm ConstraintGenerator {
    implement { ConstraintGenerator }
    is_sort(t) { ($t instanceof ConstraintGenerator) }
  }

  %typeterm BQTermCollection {
    implement { java.util.Collection<BQTerm> }
    is_sort(t) { ($t instanceof java.util.Collection) }
  }

  private Compiler compiler;

  public ConstraintGenerator(Compiler myCompiler) {
    this.compiler = myCompiler; 
  } 

  public Compiler getCompiler() {
    return this.compiler;
  }
 
  private static final String generatorsPackage = "tom.engine.compiler.generator.";
  // the list of all generators
  private static final String[] generatorsNames = {"ACGenerator", "SyntacticGenerator","VariadicGenerator","ArrayGenerator"};
  // constants
  public static final String computeLengthFuncName = "__computeLength";
  public static final String multiplicityFuncName = "__getMultiplicities";
  public static final String getTermForMultiplicityFuncName = "__getTermForMult";  

  public Instruction performGenerations(Expression expression, Instruction action) 
       throws ClassNotFoundException,InstantiationException,IllegalAccessException,VisitFailure,InvocationTargetException,NoSuchMethodException{
    // counts the generators that didn't change the instruction
    int genCounter = 0;
    int genNb = generatorsNames.length;
    
    Expression result = null;
    // cache the generators
    IBaseGenerator[] gen = new IBaseGenerator[genNb];
    Class[] classTab = new Class[]{Class.forName("tom.engine.compiler.Compiler"), Class.forName("tom.engine.compiler.ConstraintGenerator")};
    for(int i=0 ; i < genNb ; i++) {
      Class myClass = Class.forName(generatorsPackage + generatorsNames[i]);
      java.lang.reflect.Constructor constructor = myClass.getConstructor(classTab);
      gen[i] = (IBaseGenerator)constructor.newInstance(this.getCompiler(),this);
    }
    
    // iterate until all generators are applied and nothing was changed 
    mainLoop: while(true) {
      for(int i=0 ; i < genNb ; i++) {
        result = gen[i].generate(expression);
        // if nothing was done, start counting 
        genCounter = (result == expression) ? (genCounter + 1) : 0;        				
        // if we applied all the generators and nothing changed,
        // it's time to stop
        if (genCounter == genNb) { break mainLoop; }
        // reinitialize
        expression = result; 
      }
    } // end while
    return buildInstructionFromExpression(result,action);
  }
  
  /**
   * Converts the resulted expression (after generation) into instructions
   */
  private Instruction buildInstructionFromExpression(Expression expression, Instruction action)
      throws VisitFailure {		
    // it is done innermost because the expression is also simplified		
    expression = `TopDown(ReplaceSubterms(this)).visitLight(expression);
    // generate automata
    return generateAutomata(expression,action);    
  }

  /**
   * Generates the automata from the expression
   */
  private Instruction generateAutomata(Expression expression, Instruction action) throws VisitFailure {
    %match(expression){
      And(left,right) -> {
        Instruction subInstruction = `generateAutomata(right,action);
        return `generateAutomata(left,subInstruction);
      }
      conn@OrConnector(_*) -> {        
        return buildConstraintDisjunction(`conn,action);
      }
      // variables' assignments
      ConstraintToExpression(MatchConstraint[Pattern=v@(Variable|VariableStar)[],Subject=t]) -> {
        SymbolTable symbolTable = getCompiler().getSymbolTable();

        //System.out.println("In Constraint Generator with v = " + `v + '\n');
        //System.out.println("In Constraint Generator with t = " + `t + '\n');
        TomType patternType = TomBase.getTermType(`v,symbolTable);
        TomType subjectType = TomBase.getTermType(`t,symbolTable);

        //if(patternType == null) {
        //  System.out.println("pattern     =  " + `v);
        //  System.out.println("patternType =  " + patternType);
        // }
        //if(subjectType == null) {
        //  System.out.println("subject     =  " + `t);
        //  System.out.println("subjectType =  " + subjectType);
        // }


        if(TomBase.getTomType(patternType) != TomBase.getTomType(subjectType)) {
          return `LetRef(TomBase.convertFromVarToBQVar(v),Cast(patternType,BQTermToExpression(t)),action);
        } else {
          return `LetRef(TomBase.convertFromVarToBQVar(v),BQTermToExpression(t),action);
        }
      }  

      // numeric constraints
      ConstraintToExpression(n@NumericConstraint[]) -> {
        return buildNumericCondition(`n,action);
      }
      // do while
      DoWhileExpression(expr,condition) -> {
        Instruction subInstruction = `generateAutomata(expr,Nop());
        return `DoWhile(UnamedBlock(concInstruction(action,subInstruction)),condition);
      }
      // instruction encapsulated in an expression
      TomInstructionToExpression(i) -> {
        return `i;
      }
      // disjunction of symbols
      or@OrExpressionDisjunction(_*) -> {
        return buildExpressionDisjunction(`or,action);
      }
      // anti-match
      AntiMatchExpression(expr) -> {
        return buildAntiMatchInstruction(`expr,action);
      }
      // AC loop
      ACMatchLoop(symbolName,var_x,var_y,mult_y,subject) -> {
        return `buildACMatchLoop(symbolName, var_x, var_y, mult_y, subject, action);
      }
      // conditions			
      x -> {
        return `If(x,action,Nop());
      }	
    }
    throw new TomRuntimeException("ConstraintGenerator.generateAutomata - strange expression:" + expression);
  }

  /**
   * Converts 'Subterm' to 'GetSlot'
   */
  %strategy ReplaceSubterms(cg:ConstraintGenerator) extends Identity() {
    visit BQTerm {
      Subterm(constructorName@Name(name), slotName, term) -> {
        TomSymbol tomSymbol = cg.getCompiler().getSymbolTable().getSymbolFromName(`name);
        TomType subtermType = TomBase.getSlotType(tomSymbol, `slotName);	        	
        return `ExpressionToBQTerm(GetSlot(subtermType, constructorName, slotName.getString(), term));
      }
    }
  }


  /**
   * compile a disjunction of pattern by duplication the action part
   * we are forced to duplication the action to have the same semantics as
   * without disjunctions
   */
  private Instruction buildConstraintDisjunction(Expression orConnector, Instruction action) throws VisitFailure {    
    %match(orConnector) {
      OrConnector(x,Y*) -> {        
        return `AbstractBlock(concInstruction(
              generateAutomata(x,action),
              buildConstraintDisjunction(Y*,action)
              ));
      }
    }
    return `Nop();
  }

  /*
   * Takes the OrConstraintDisjunction and generates the tests
   * (this is for the disjunction of symbols)
   * 
   * boolean flag = false;
   * var1 = null;
   * var2 = null;
   * ....
   * if (is_fsym(f1)) {
   *    flag = true;
   *    var1 = subterm1_f1();
   *    var2 = subterm2_f1();
   *    .....    
   * } else if (is_fsym(f2)) {
   *    flag = true;
   *    var1 = subterm1_f2();
   *    var2 = subterm2_f2();
   *    ..... 
   * } ....
   * if (flag == true) ...
   *  
   */
  private Instruction buildExpressionDisjunction(Expression orDisjunction,Instruction action)
         throws VisitFailure {     
    BQTerm flag = getCompiler().getFreshVariable(getCompiler().getSymbolTable().getBooleanType());
    Instruction assignFlagTrue = `Assign(flag,TrueTL());
    Collection<BQTerm> freshVarList = new HashSet<BQTerm>();
    // collect variables    
    `TopDown(CollectVar(freshVarList)).visitLight(orDisjunction);    
    Instruction instruction = buildDisjunctionIfElse(orDisjunction,assignFlagTrue);
    // add the final test
    instruction = `AbstractBlock(concInstruction(instruction,If(BQTermToExpression(flag),action,Nop())));    
    // add fresh variables' declarations
    for(BQTerm var:freshVarList) {
      instruction = `LetRef(var,Bottom(var.getAstType()),instruction);
    }
    // stick the flag declaration also
    return `LetRef(flag,FalseTL(),instruction);
  }

  private Instruction buildDisjunctionIfElse(Expression orDisjunction,Instruction assignFlagTrue)
      throws VisitFailure {    
    %match(orDisjunction){
      OrExpressionDisjunction() -> {
        return `Nop();
      }
      OrExpressionDisjunction(And(check@IsSort[],assign),X*) -> {
        return
          `If(check,buildDisjunctionIfElse(OrExpressionDisjunction(assign,X*),assignFlagTrue),Nop());
      }

      OrExpressionDisjunction(And(check,assign),X*) -> {        
        //DEBUG System.out.println("orDisjunction = " + `orDisjunction);
        Instruction subtest = buildDisjunctionIfElse(`OrExpressionDisjunction(X*),assignFlagTrue);
        return `If(check,UnamedBlock(concInstruction(assignFlagTrue,generateAutomata(assign,Nop()))),subtest);
      }
      OrExpressionDisjunction(check,X*) -> {        
        Instruction subtest = buildDisjunctionIfElse(`OrExpressionDisjunction(X*),assignFlagTrue);
        return `If(check, assignFlagTrue, subtest);
      }
    }
    throw new TomRuntimeException("ConstraintGenerator.buildDisjunctionIfElse - strange expression:" + orDisjunction);
  }

  /**
   * generates:
   * 
   * bool matchSuccessful = false;
   * if (expression) {
   *    matchSuccessful = true;
   * }
   * if (matchSuccessful == false) {
   *    action;
   * }
   */
  private Instruction buildAntiMatchInstruction(Expression expression, Instruction action)
      throws VisitFailure {
    BQTerm flag = getCompiler().getFreshVariable(getCompiler().getSymbolTable().getBooleanType());
    Instruction assignFlagTrue = `Assign(flag,TrueTL());
    Instruction automata = generateAutomata(expression, assignFlagTrue);    
    // add the final test
    Instruction result = `AbstractBlock(concInstruction(automata,
         If(Negation(BQTermToExpression(flag)),action,Nop())));
    return `LetRef(flag,FalseTL(),result);
  }

  /**
   * Collect the variables in a match constraint
   */
  %strategy CollectVar(varList:BQTermCollection) extends Identity() {
    visit Constraint {
      MatchConstraint[Pattern=v@Variable[]] -> {
        if(!varList.contains(`v)) { varList.add(TomBase.convertFromVarToBQVar(`v)); }        
      }      
    }// end visit
  }// end strategy   
  
  /**
   * Collect the free variables in an expression (do not inspect under an anti)  
   */
  %strategy CollectFreeVar(varList:BQTermCollection) extends Identity() {     
    visit Expression {
      ConstraintToExpression(MatchConstraint[Pattern=v@Variable[]]) -> {
        if(!varList.contains(`v)) { varList.add(TomBase.convertFromVarToBQVar(`v)); }     
        throw new VisitFailure();
      }
      AntiMatchExpression[] -> {        
        throw new VisitFailure();
      }
    }
  }
  
  /**
   * check that the list is empty
   * when domain=codomain, the test is extended to:
   *   is_empty(l) || l==make_empty()
   *   this is needed because get_tail() may return the neutral element 
   */ 
  public Expression genIsEmptyList(TomName opName, BQTerm var) {
    TomSymbol tomSymbol = getCompiler().getSymbolTable().getSymbolFromName(opName.getString());
    TomType domain = TomBase.getSymbolDomain(tomSymbol).getHeadconcTomType();
    TomType codomain = TomBase.getSymbolCodomain(tomSymbol);
    if(domain==codomain) {
      //TODO: we should check if the BuildEmptyList exists for the mapping (can be null)
      return `Or(IsEmptyList(opName, var), EqualBQTerm(codomain,var,BuildEmptyList(opName)));
    }
    return `IsEmptyList(opName, var);
  }
  
  private Instruction buildNumericCondition(Constraint c, Instruction action) {
    %match(c) {
      NumericConstraint(left,right,type) -> {        
        Expression leftExpr = `BQTermToExpression(left);
        Expression rightExpr = `BQTermToExpression(right);
        %match(type) {
          NumLessThan()             -> { return `If(LessThan(leftExpr,rightExpr),action,Nop());} 
          NumLessOrEqualThan()      -> { return `If(LessOrEqualThan(leftExpr,rightExpr),action,Nop());}
          NumGreaterThan()          -> { return `If(GreaterThan(leftExpr,rightExpr),action,Nop());}
          NumGreaterOrEqualThan()   -> { return `If(GreaterOrEqualThan(leftExpr,rightExpr),action,Nop());}
          NumEqual()                -> { TomType tomType = getCompiler().getTermTypeFromTerm(`left);
                                         return `If(EqualBQTerm(tomType,right,left),action,Nop()); }
          NumDifferent()            -> { TomType tomType = getCompiler().getTermTypeFromTerm(`left);
                                         return `If(Negation(EqualBQTerm(tomType,right,left)),action,Nop()); }
        }
      }
    }
    // should never reach here
    throw new TomRuntimeException("Untreated numeric constraint: " + `c);
  }


  /*
   * NO LONGER USEFUL: DEAD CODE
   *
   * Takes the OrConnector and generates the tests
   * (this is for the OrConstraint - the disjunction of constraints)
   *
   * var1 = null; // all the free variables
   * var2 = null;
   * ....
   * varm = null;
   * int counter = 0;
   * do { // n is the number of 'or'
   *    boolean flag = false;
   *    if (counter >= 0 && counter <=0 ) { // generated like this because if we use "counter == 0", we have to include "int.tom" 
   *        if ( code_for_first_constraint_in_disjunction ) {
   *            flag = true;
   *        }
   *    } else if (counter >= 1 && counter <= 1 ) {
   *    ....
   *    else if (counter >= n-1 && counter <= n-1) {
   *        if ( code_for_n_constraint_in_disjunction ) {
   *            flag = true;
   *        }
   *    }    
   *    if (flag == true) ...
   *    counter++;
   * } while (counter < n)
   *  
   */
  private Instruction buildConstraintDisjunctionWithoutCopy(Expression orConnector, Instruction action) throws VisitFailure {    
    BQTerm flag = getCompiler().getFreshVariable(getCompiler().getSymbolTable().getBooleanType());
    Instruction assignFlagTrue = `Assign(flag,TrueTL());
    TomType intType = getCompiler().getSymbolTable().getIntType();
    BQTerm counter = getCompiler().getFreshVariable(intType);    
    // build the ifs
    Instruction instruction = `buildTestsInConstraintDisjuction(0,assignFlagTrue,counter,intType,orConnector);    
    // add the final test
    instruction = `AbstractBlock(concInstruction(instruction,
          If(EqualTerm(getCompiler().getSymbolTable().getBooleanType(),flag,TruePattern()),action,Nop())));
    // counter++ : expression at the end of the loop 
    Instruction counterIncrement = `Assign(counter,AddOne(counter));
    //  stick the flag declaration and the counterIncrement   
    instruction = `LetRef(flag,FalseTL(),AbstractBlock(concInstruction(instruction,counterIncrement)));      
    instruction = `DoWhile(instruction,LessThan(BQTermToExpression(counter), Integer(orConnector.length())));    
    // add fresh variables' declarations
    Collection<BQTerm> freshVarList = new HashSet<BQTerm>();
    // collect free variables    
    `TopDownCollect(CollectFreeVar(freshVarList)).visitLight(orConnector);
    for(BQTerm var:freshVarList) {
      instruction = `LetRef(var,Bottom(var.getAstType()),instruction);
    }
    // stick the counter declaration
    return `LetRef(counter,Integer(0),instruction);
  }

  /**
   * NO LONGER USEFUL: DEAD CODE
   *
   * builds the ifs in a constraint disjunction (see buildConstraintDisjunction above for details)
   */
  private Instruction buildTestsInConstraintDisjuction(int cnt, Instruction assignFlagTrue, 
      BQTerm counter, TomType intType, Expression orConnector) throws VisitFailure {
    %match(orConnector) {
      OrConnector(x,Y*) -> {        
        return `If(And(GreaterOrEqualThan(BQTermToExpression(counter),Integer(cnt)),
                  LessOrEqualThan(BQTermToExpression(counter),Integer(cnt))),
                  generateAutomata(x,assignFlagTrue),
                  buildTestsInConstraintDisjuction(cnt,assignFlagTrue,counter,intType,OrConnector(Y*)));        
      }       
    }
    return `Nop();
  }

  /**
    * generate a loop and a call to next_minimal_extract
    */
  private Instruction buildACMatchLoop(String symbolName, TomTerm var_x, TomTerm var_y, int mult_y, BQTerm subject, Instruction action) {
    //System.out.println("buildACMatchLoop: mult_y = " + mult_y);

    SymbolTable symbolTable = getCompiler().getSymbolTable();
    TomType intType = symbolTable.getIntType();
    TomType intArrayType = symbolTable.getIntArrayType();
    // a 0
    ///Expression zero = `Integer(0);
    // the name of the int[] operator
    TomName intArrayName = `Name(symbolTable.getIntArrayOp());

    BQTerm alpha = getCompiler().getFreshVariable("alpha",intArrayType);
    BQTerm tempSol = getCompiler().getFreshVariable("tempSol",intArrayType);
    BQTerm position = getCompiler().getFreshVariable("position",intType);
    BQTerm length = getCompiler().getFreshVariable("length",intType);                
    BQTerm multiplicity = getCompiler().getFreshVariable("multiplicity",intType);                

    BQTermList getTermArgs = `concBQTerm(tempSol,alpha,subject);        
    TomType subtermType = getCompiler().getTermTypeFromTerm(var_x);
    TomType booleanType = symbolTable.getBooleanType();
    
    Expression whileCond = `BQTermToExpression(FunctionCall(Name("next_minimal_extract"),booleanType,concBQTerm(multiplicity,length,alpha,tempSol)));

    Instruction instruction = `DoWhile(
          LetRef(TomBase.convertFromVarToBQVar(var_x),
            BQTermToExpression(FunctionCall(Name(ConstraintGenerator.getTermForMultiplicityFuncName + "_" + symbolName), subtermType,concBQTerm(getTermArgs*,ExpressionToBQTerm(FalseTL())))),
            LetRef(TomBase.convertFromVarToBQVar(var_y),
              BQTermToExpression(FunctionCall(Name(ConstraintGenerator.getTermForMultiplicityFuncName + "_" + symbolName), subtermType,concBQTerm(getTermArgs*,ExpressionToBQTerm(TrueTL())))),
              action
            )
          ), whileCond);

    instruction = `LetRef(multiplicity,Integer(mult_y),instruction);
    instruction = `LetRef(position,SubstractOne(length),instruction);
    instruction = `LetRef(tempSol,BQTermToExpression(BuildEmptyArray(intArrayName,length)),instruction);
    instruction = `LetRef(length,GetSize(intArrayName,alpha),instruction);
    instruction = `LetRef(alpha,BQTermToExpression(FunctionCall(
            Name(ConstraintGenerator.multiplicityFuncName + "_" + symbolName),
            intArrayType,concBQTerm(subject))),instruction);

    return instruction;
  }

}
