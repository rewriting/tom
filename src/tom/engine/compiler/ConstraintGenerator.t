/*
 *
 * TOM - To One Matching Compiler
 * 
 * Copyright (c) 2000-2008, INRIA
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

import java.util.ArrayList;
import java.util.Iterator;

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
import tom.engine.compiler.generator.*;
import tom.engine.exception.TomRuntimeException;
import tom.engine.adt.tomsignature.types.*;
import tom.engine.adt.tomtype.types.*;
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
 
  private static final String generatorsPackage = "tom.engine.compiler.generator.";
  // the list of all generators
  private static final String[] generatorsNames = {"SyntacticGenerator","VariadicGenerator","ArrayGenerator"};

  public static Instruction performGenerations(Expression expression, Instruction action) 
       throws ClassNotFoundException,InstantiationException,IllegalAccessException,VisitFailure{		
    // counts the generators that didn't change the instruction
    int genCounter = 0;
    int genNb = generatorsNames.length;
    
    Expression result = null;
    // cache the generators
    IBaseGenerator[] gen = new IBaseGenerator[genNb];
    for(int i=0 ; i < genNb ; i++) {
      gen[i] = (IBaseGenerator)Class.forName(generatorsPackage + generatorsNames[i]).newInstance();
    }
    
    // iterate until all generators are applied and nothing was changed 
    mainLoop: while(true) {
      for(int i=0 ; i < genNb ; i++){        
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
    //System.out.println("result: " + result);
    return buildInstructionFromExpression(result,action);
  }
  
  /**
   * Converts the resulted expression (after generation) into instructions
   */
  private static Instruction buildInstructionFromExpression(Expression expression, Instruction action)
      throws VisitFailure {		
    // it is done innermost because the expression is also simplified		
    expression = (Expression)`TopDown(ReplaceSubterms()).visitLight(expression);
    // generate automata
    Instruction automata = generateAutomata(expression,action);    
    return automata;
  }

  /**
   * Generates the automata from the expression
   */
  private static Instruction generateAutomata(Expression expression, Instruction action) throws VisitFailure {
    %match(expression){
      And(left,right) -> {
        Instruction subInstruction = generateAutomata(`right,action);
        return `generateAutomata(left,subInstruction);
      }
      conn@OrConnector(_*) -> {        
        return buildConstraintDisjunction(`conn,action);
      }
      // variables' assignments
      ConstraintToExpression(MatchConstraint(v@(Variable|VariableStar)[],t)) -> {
        return `LetRef(v,TomTermToExpression(t),action);
      }  
      // nothing for unamed ones
      ConstraintToExpression(MatchConstraint((UnamedVariableStar|UnamedVariable)[],_)) -> {       
        return action;      
      }
      // numeric constraints
      ConstraintToExpression(n@NumericConstraint[]) -> {
        return buildNumericCondition(`n,action);
      }
      // do while
      DoWhileExpression(expr,condition) -> {
        Instruction subInstruction = generateAutomata(`expr,`Nop());
        return `DoWhile(UnamedBlock(concInstruction(action,subInstruction)),condition);
      }
      // 'if'
      IfExpression(condition, EqualTerm[Kid1=left1,Kid2=right1], EqualTerm[Kid1=left2,Kid2=right2]) -> {
        return `If(condition,Assign(left1,TomTermToExpression(right1)),Assign(left2,TomTermToExpression(right2)));
      }
      // disjunction of symbols
      or@OrExpressionDisjunction(_*) -> {
        return buildExpressionDisjunction(`or,action);
      }
      // anti-match
      AntiMatchExpression(expr) -> {
        return buildAntiMatchInstruction(`expr,action);
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
  %strategy ReplaceSubterms() extends Identity() {
    visit TomTerm {
      Subterm(constructorName@Name(name), slotName, term) -> {
        TomSymbol tomSymbol = Compiler.getSymbolTable().getSymbolFromName(`name);
        TomType subtermType = TomBase.getSlotType(tomSymbol, `slotName);	        	
        return `ExpressionToTomTerm(GetSlot(subtermType, constructorName, slotName.getString(), term));
      }
    }
  }
  
  /*
   * Takes the OrConnector and generates the tests
   * (this is for the OrConstraint - the disjunction of constraints)
   *
   * var1 = null; // all the free variables
   * var2 = null;
   * ....
   * varm = null;
   * int counter = 0;
   * do{ // n is the number of 'or'
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
  private static Instruction buildConstraintDisjunction(Expression orConnector, Instruction action) throws VisitFailure {    
    TomTerm flag = Compiler.getFreshVariable(Compiler.getBooleanType());
    Instruction assignFlagTrue = `Assign(flag,TrueTL());
    TomType intType = Compiler.getIntType();
    TomTerm counter = Compiler.getFreshVariable(intType);    
    // build the ifs
    Instruction instruction = `buildTestsInConstraintDisjuction(0,assignFlagTrue,counter,intType,orConnector);    
    // add the final test
    instruction = `AbstractBlock(concInstruction(instruction,
          If(EqualTerm(Compiler.getBooleanType(),flag,ExpressionToTomTerm(TrueTL())),action,Nop())));
    // counter++ : expression at the end of the loop 
    Instruction counterIncrement = `Assign(counter,AddOne(counter));
    //  stick the flag declaration and the counterIncrement   
    instruction = `LetRef(flag,FalseTL(),AbstractBlock(concInstruction(instruction,counterIncrement)));      
    instruction = `DoWhile(instruction,LessThan(TomTermToExpression(counter),
        TomTermToExpression(Variable(concOption(),Name(orConnector.length()+""),intType,concConstraint()))));    
    // add fresh variables' declarations
    ArrayList<TomTerm> freshVarList = new ArrayList<TomTerm>();
    // collect free variables    
    `TopDownCollect(CollectFreeVar(freshVarList)).visitLight(orConnector);
    for(TomTerm var:freshVarList) {
      instruction = `LetRef(var,Bottom(var.getAstType()),instruction);
    }
    // stick the counter declaration
    return `LetRef(counter,TomTermToExpression(Variable(concOption(),Name(0+""),intType,concConstraint())),instruction);
  }
  /**
   * builds the ifs in a constraint disjunction (see buildConstraintDisjunction above for details)
   */
  private static Instruction buildTestsInConstraintDisjuction(int cnt, Instruction assignFlagTrue, 
      TomTerm counter, TomType intType, Expression orConnector) throws VisitFailure {
    %match(orConnector){
      OrConnector(x,Y*) -> {        
        TomTerm counterValue = `Variable(concOption(),Name(cnt+""),intType,concConstraint());
        return `If(And(GreaterOrEqualThan(TomTermToExpression(counter),TomTermToExpression(counterValue)),
                  LessOrEqualThan(TomTermToExpression(counter),TomTermToExpression(counterValue))),
                    generateAutomata(x,assignFlagTrue),buildTestsInConstraintDisjuction(++cnt,assignFlagTrue,counter,intType,OrConnector(Y*)));        
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
  private static Instruction buildExpressionDisjunction(Expression orDisjunction,Instruction action)
         throws VisitFailure {     
    TomTerm flag = Compiler.getFreshVariable(Compiler.getBooleanType());
    Instruction assignFlagTrue = `Assign(flag,TrueTL());
    ArrayList<TomTerm> freshVarList = new ArrayList<TomTerm>();
    // collect variables    
    `TopDown(CollectVar(freshVarList)).visitLight(orDisjunction);    
    Instruction instruction = buildDisjunctionIfElse(orDisjunction,assignFlagTrue);
    // add the final test
    instruction = `AbstractBlock(concInstruction(instruction,
          If(EqualTerm(Compiler.getBooleanType(),flag,ExpressionToTomTerm(TrueTL())),action,Nop())));    
    // add fresh variables' declarations
    for(TomTerm var:freshVarList){
      instruction = `LetRef(var,Bottom(var.getAstType()),instruction);
    }
    // stick the flag declaration also
    return `LetRef(flag,FalseTL(),instruction);
  }

  private static Instruction buildDisjunctionIfElse(Expression orDisjunction,Instruction assignFlagTrue)
      throws VisitFailure {    
    %match(orDisjunction){
      OrExpressionDisjunction() -> {
        return `Nop();
      }
      OrExpressionDisjunction(And(check,assign),X*) -> {        
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
  private static Instruction buildAntiMatchInstruction(Expression expression, Instruction action)
      throws VisitFailure {
    TomTerm flag = Compiler.getFreshVariable(Compiler.getBooleanType());    
    Instruction assignFlagTrue = `Assign(flag,TrueTL());
    Instruction automata = generateAutomata(expression, assignFlagTrue);    
    // add the final test
    Instruction result = `AbstractBlock(concInstruction(automata,
          If(EqualTerm(Compiler.getBooleanType(),flag,ExpressionToTomTerm(FalseTL())),action,Nop())));
    return `LetRef(flag,FalseTL(),result);
  }

  /**
   * Collect the variables in a term   
   */
  %strategy CollectVar(varList:Collection) extends Identity() {
    visit Constraint {
      MatchConstraint(v@Variable[],_) -> {
        if(!varList.contains(`v)) { varList.add(`v); }        
      }      
    }// end visit
  }// end strategy   
  
  /**
   * Collect the free variables in an expression (do not inspect under a anti)  
   */
  %strategy CollectFreeVar(varList:Collection) extends Identity() {     
    visit Expression {
      ConstraintToExpression(MatchConstraint(v@Variable[],_)) -> {
        if(!varList.contains(`v)) { varList.add(`v); }     
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
  public static Expression genIsEmptyList(TomName opName, TomTerm var) {
    TomSymbol tomSymbol = Compiler.getSymbolTable().getSymbolFromName(((Name)opName).getString());
    TomType domain = TomBase.getSymbolDomain(tomSymbol).getHeadconcTomType();
    TomType codomain = TomBase.getSymbolCodomain(tomSymbol);
    if(domain==codomain) {
      return `Or(IsEmptyList(opName, var), EqualTerm(codomain,var,BuildEmptyList(opName)));
    }
    return `IsEmptyList(opName, var);
  }
  
  private static Instruction buildNumericCondition(Constraint c, Instruction action) {
    %match(c){
      NumericConstraint(left,right,type) -> {        
        TomType tomType = Compiler.getTermTypeFromTerm(`left);
        Expression leftExpr = null;
      m:%match(left){
          RecordAppl[Option=concOption(_*,Constant(),_*),NameList=concTomName(name)] -> {
            leftExpr = `TomTermToExpression(BuildConstant(name));            
            break m;
          }
          _ -> { leftExpr = `TomTermToExpression(left); } 
        }
        Expression rightExpr = `TomTermToExpression(right);
        %match(type){
          NumLessThan()             -> { return `If(LessThan(leftExpr,rightExpr),action,Nop());} 
          NumLessOrEqualThan()      -> { return `If(LessOrEqualThan(leftExpr,rightExpr),action,Nop());}
          NumGreaterThan()          -> { return `If(GreaterThan(leftExpr,rightExpr),action,Nop());}
          NumGreaterOrEqualThan()   -> { return `If(GreaterOrEqualThan(leftExpr,rightExpr),action,Nop());}
          NumEqual()                -> { return `If(EqualTerm(tomType,right,left),action,Nop());}
          NumDifferent()            -> { return `If(Negation(EqualTerm(tomType,right,left)),action,Nop());}                
        }
      }
    }
    // should never reach here
    throw new TomRuntimeException("Untreated numeric constraint: " + `c);
  }
}
