package tom.engine.compiler;

import java.util.ArrayList;
import java.util.Iterator;

import tom.engine.TomBase;
import tom.engine.adt.tomterm.types.*;
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
import tom.library.sl.Visitable;


/**
 * This class is in charge with launching all the generators,
 * until no more generations can be made 
 */
public class TomGenerationManager {

//------------------------------------------------------------	
  %include { ../adt/tomsignature/TomSignature.tom }	
  %include { java/util/types/Collection.tom}
  %include { ../../library/mapping/java/sl.tom}
//------------------------------------------------------------	
 
  private static final String generatorsPackage = "tom.engine.compiler.generator.";
  // the list of all generators
  private static final String[] generatorsNames = {"TomSyntacticGenerator","TomVariadicGenerator","TomArrayGenerator"};

  public static Instruction performGenerations(Constraint constraint, Instruction action) 
  throws ClassNotFoundException,InstantiationException,IllegalAccessException{		
    // counts the generators that didn't change the instruction
    int genCounter = 0;
    int genNb = generatorsNames.length;
    
    Expression result = null;    
    Expression expression = prepareGeneration(constraint);
    // cache the generators
    TomIBaseGenerator[] gen = new TomIBaseGenerator[genNb];
    for(int i=0 ; i < genNb ; i++) {
      gen[i] = (TomIBaseGenerator)Class.forName(generatorsPackage + generatorsNames[i]).newInstance();
    }
    
    // iterate until all propagators are applied and nothing was changed 
    mainLoop: while(true){		
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
   * Prepares the generation phase: globally translates constraints into expressions
   *   
   */
  private static Expression prepareGeneration(Constraint constraint){    
    %match(constraint){
      and@AndConstraint(m,X*) -> {        
        //TODO - erase the test when the match non-au will be available
        if ((`and) instanceof AndConstraint){
          return `And(prepareGeneration(m),
              prepareGeneration(AndConstraint(X*)));
        }
      }      
      OrConstraintDisjunction(m,X*) -> {        
        return `OrExpressionDisjunction(prepareGeneration(m),
            prepareGeneration(OrConstraintDisjunction(X*)));
      }
      m@MatchConstraint[] -> {        
        return `ConstraintToExpression(m);
      }
      AntiMatchConstraint(constr) -> {
        return `AntiMatchExpression(prepareGeneration(constr));
      }
      Negate(c) -> {
        return `Negation(prepareGeneration(c));
      }
      EmptyListConstraint(opName,variable) ->{				
        return `genIsEmptyList(opName,variable);
      }
      EmptyArrayConstraint(opName,variable,index) ->{                          
        return `IsEmptyArray(opName,Ref(variable),Ref(index));
      }
    }			
    throw new TomRuntimeException("TomGenerationManager.prepareGeneration - strange constraint:" + constraint);
  }	

  /**
   * Converts the resulted expression (after generation) into instructions
   */
  private static Instruction buildInstructionFromExpression(Expression expression, Instruction action){		
    // it is done innermost because the expression is also simplified		
    expression = (Expression)`InnermostId(ReplaceSubterms()).fire(expression);		
    // generate automata
    Instruction automata = generateAutomata(expression,action);    
    // make sure that each variable is declared only once
    ArrayList<TomName> declaredVariables = new ArrayList<TomName>(); 		
    automata = (Instruction)`TopDown(ChangeVarDeclarations(declaredVariables)).fire(automata);    
    return automata;
  }

  /**
   * Generates the automata from the expression
   */
  private static Instruction generateAutomata(Expression expression, Instruction action){
    %match(expression){
      And(left,right) -> {
        Instruction subInstruction = generateAutomata(`right,action);
        return `generateAutomata(left,subInstruction);
      } 
      // variables' assignments
      ConstraintToExpression(MatchConstraint(v@(Variable|UnamedVariable|VariableStar)[],t)) -> {
        return `LetRef(v,TomTermToExpression(t),action);
      }  
      // do while
      DoWhileExpression(expr,condition) -> {
        Instruction subInstruction = generateAutomata(`expr,`Nop());
        return `DoWhile(UnamedBlock(concInstruction(action,subInstruction)),condition);
      }
      // 'if'
      IfExpression(condition, EqualTerm[Kid1=left1,Kid2=right1], EqualTerm[Kid1=left2,Kid2=right2]) -> {
        return `If(condition,LetAssign(left1,TomTermToExpression(right1),Nop()),LetAssign(left2,TomTermToExpression(right2),Nop()));
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
    throw new TomRuntimeException("TomGenerationManager.generateAutomata - strange expression:" + expression);
  }
 
  /**
   * Makes sure that no variable is declared if the same variable was declared above  
   */
  %strategy ChangeVarDeclarations(declaredVariables:Collection) extends Identity(){
    visit Instruction{
      LetRef(var@(Variable|VariableStar)[AstName=name],source,instruction) ->{
        ArrayList<Boolean> list = new ArrayList<Boolean>();
        Visitable root = getEnvironment().getRoot();
        if (root != getEnvironment().getSubject()) {
          getEnvironment().getPosition().getOmegaPath(`CheckVarExistence(name,list)).fire(root);        
          if (list.size() > 0){
            return `LetAssign(var,source,instruction);
          }		
        }
      }
    }// end visit
  }// end strategy
  
  // TODO - change this with a more appropriate method
  %strategy CheckVarExistence(varName:TomName,bag:Collection) extends Identity(){
    visit Instruction {
      LetRef[Variable=v@(Variable|VariableStar)[AstName=name]] -> {
        if (varName == (`name) ){
          bag.add(new Boolean(true));
        }
      }
    } // end visit
  }// end strategy
  

  /**
   * Converts 'Subterm' to 'GetSlot'
   */
  %strategy ReplaceSubterms() extends Identity(){
    visit TomTerm{
      s@Subterm(constructorName@Name(name), slotName, term) ->{
        TomSymbol tomSymbol = TomConstraintCompiler.getSymbolTable().getSymbolFromName(`name);
        TomType subtermType = TomBase.getSlotType(tomSymbol, `slotName);	        	
        return `ExpressionToTomTerm(GetSlot(subtermType, constructorName, slotName.getString(), term));
      }
    }
  }
  
  /*
   * Takes the OrConstraintDisjunction and generates the tests
   * 
   * boolean flag = false;
   * var1 = null;
   * var2 = null;
   * ....
   * if (is_fsym(f1)){
   *    flag = true;
   *    var1 = subterm1_f1();
   *    var2 = subterm2_f1();
   *    .....    
   * }else if (is_fsym(f2)) {
   *    flag = true;
   *    var1 = subterm1_f2();
   *    var2 = subterm2_f2();
   *    ..... 
   * } ....
   * if (flag == true) ...
   *  
   */
  private static Instruction buildExpressionDisjunction(Expression orDisjunction,Instruction action){     
    TomTerm flag = TomConstraintCompiler.getFreshVariable(TomConstraintCompiler.getBooleanType());
    Instruction assignFlagTrue = `LetAssign(flag,TrueTL(),Nop());
    ArrayList<TomTerm> freshVarList = new ArrayList<TomTerm>();
    // collect variables
    `TopDown(CollectVar(freshVarList)).fire(orDisjunction);
    Instruction instruction = buildDisjunctionIfElse(orDisjunction,assignFlagTrue);
    // add the final test
    instruction = `AbstractBlock(concInstruction(instruction,
        If(EqualTerm(TomConstraintCompiler.getBooleanType(),flag,ExpressionToTomTerm(TrueTL())),action,Nop())));    
    // add fresh variables' declarations
    for(TomTerm var:freshVarList){
      instruction = `LetRef(var,Bottom(var.getAstType()),instruction);
    }
    // stick the flag declaration also
    return `LetRef(flag,FalseTL(),instruction);
  }
  
  private static Instruction buildDisjunctionIfElse(Expression orDisjunction,Instruction assignFlagTrue){    
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
    throw new TomRuntimeException("TomGenerationManager.buildDisjunctionIfElse - strange expression:" + orDisjunction);
  }
  
  /**
   * generates:
   * 
   * bool matchSuccessful = false;
   * if (expression){
   *    matchSuccessful = true;
   * }
   * if (matchSuccessful == false){
   *    action;
   * }
   */
  private static Instruction buildAntiMatchInstruction(Expression expression, Instruction action){
    TomTerm flag = TomConstraintCompiler.getFreshVariable(TomConstraintCompiler.getBooleanType());    
    Instruction assignFlagTrue = `LetAssign(flag,TrueTL(),Nop());
    Instruction automata = generateAutomata(expression, assignFlagTrue);    
    // add the final test
    Instruction result = `AbstractBlock(concInstruction(automata,
        If(EqualTerm(TomConstraintCompiler.getBooleanType(),flag,ExpressionToTomTerm(FalseTL())),action,Nop())));
    return `LetRef(flag,FalseTL(),result);
  }

  /**
   * Collect the variables in a term   
   */
  %strategy CollectVar(varList:Collection) extends Identity(){
    visit Constraint{
      MatchConstraint(v@Variable[],_) ->{        
        if (!varList.contains(`v)) { varList.add(`v); }        
      }      
    }// end visit
  }// end strategy 

  /**
   * check that the list is empty
   * when domain=codomain, the test is extended to:
   *   is_empty(l) || l==make_empty()
   *   this is needed because get_tail() may return the neutral element 
   */ 
  public static Expression genIsEmptyList(TomName opName, TomTerm var) {
    TomSymbol tomSymbol = TomConstraintCompiler.getSymbolTable().getSymbolFromName(((Name)opName).getString());
    TomType domain = TomBase.getSymbolDomain(tomSymbol).getHeadconcTomType();
    TomType codomain = TomBase.getSymbolCodomain(tomSymbol);
    if(domain==codomain) {
      return `Or(IsEmptyList(opName, var), EqualTerm(codomain,var,BuildEmptyList(opName)));
    }
    return `IsEmptyList(opName, var);
  }
}