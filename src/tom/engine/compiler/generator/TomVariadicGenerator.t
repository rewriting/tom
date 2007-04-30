package tom.engine.compiler.generator;

import tom.engine.adt.tominstruction.types.*;
import tom.engine.adt.tomexpression.types.*;
import tom.engine.adt.tomexpression.types.expression.*;
import tom.engine.adt.tomname.types.*;
import tom.engine.adt.tomname.types.tomname.*;
import tom.engine.adt.tomterm.types.*;
import tom.engine.adt.tomtype.types.*;
import tom.engine.adt.tomterm.types.tomterm.*;
import tom.library.sl.*;
import tom.engine.tools.SymbolTable;
import tom.engine.exception.TomRuntimeException;
import tom.engine.adt.tomsignature.types.*;
import tom.engine.TomBase;

import tom.engine.compiler.*;
/**
 * Variadic Generator
 */
public class TomVariadicGenerator implements TomIBaseGenerator {

  %include { ../../adt/tomsignature/TomSignature.tom }
  %include { ../../../library/mapping/java/sl.tom}	

  public Expression generate(Expression expression) {
    return (Expression)`TopDown(VariadicGenerator()).fire(expression);		
  }

  // If we find ConstraintToExpression it means that this constraint was not processed	
  %strategy VariadicGenerator() extends Identity() {
    visit Expression {
      // generate pre-loop for X* = or _* = 
      /*
       * do {      
       *   ...
       *   if(IS_EMPTY_TomList(end_i) )
       *     end_i = begin_i
       *   else *** use this impossible value to indicate the end of the loop ***
       *     end_i = (TomList) GET_TAIL_TomList(end_i);
       * } while( end_i != begin_i ) 
       */
      ConstraintToExpression(MatchConstraint(v@(VariableStar|UnamedVariableStar)[],VariableHeadList(opName,begin,end@VariableStar[AstType=type]))) -> {
        Expression doWhileTest = `Negation(EqualTerm(type,end,begin));
        Expression testEmpty = TomGenerationManager.genIsEmptyList(`opName,`end);
        Expression endExpression = `IfExpression(testEmpty,EqualTerm(type,end,begin),EqualTerm(type,end,ListTail(opName,end)));
        // if we have a varStar, we generate its declaration also
        if (`v.isVariableStar()) {
          Expression varDeclaration = `ConstraintToExpression(MatchConstraint(v,ExpressionToTomTerm(GetSliceList(opName,begin,end,BuildEmptyList(opName)))));
          return `And(DoWhileExpression(endExpression,doWhileTest),varDeclaration);
        }
        return `DoWhileExpression(endExpression,doWhileTest);		        		      
      }
      // generate equal - this can come from variable's replacement ( in the syntactic propagator )
      ConstraintToExpression(MatchConstraint(e@ExpressionToTomTerm(getHead),t)) -> {
        %match(getHead){
          GetHead[Codomain = type] -> {
            return `EqualTerm(type,e,t);
          }
          Conditional[Then = GetHead[Codomain = type]] -> {
            return `EqualTerm(type,e,t);
          }
        }
      }
    } // end visit
    visit TomTerm {
      // generate getHead
      ListHead(opName,type,variable) -> {
        return `ExpressionToTomTerm(genGetHead(opName,type,variable));
      }
      // generate getTail
      ListTail(opName,variable) -> {
        return `ExpressionToTomTerm(genGetTail(opName,variable));
      }
    }
  } // end strategy	
  
  /**
   * return the head of the list
   * when domain=codomain, the test is extended to:
   *   is_fsym_f(t)?get_head(t):t 
   *   the element itself is returned when it is not a list operator
   *   this occurs because the last element of a loop may not be a list
   */ 
  private static Expression genGetHead(TomName opName, TomType type, TomTerm var) {
    TomSymbol tomSymbol = TomConstraintCompiler.getSymbolTable().getSymbolFromName(((Name)opName).getString());
    TomType domain = TomBase.getSymbolDomain(tomSymbol).getHeadconcTomType();
    TomType codomain = TomBase.getSymbolCodomain(tomSymbol);
    if(domain==codomain) {
      return `Conditional(IsFsym(opName,var),GetHead(opName, type, var),TomTermToExpression(var));
    }
    return `GetHead(opName, type, var);
  }

  /**
   * return the tail of the list
   * when domain=codomain, the test is extended to:
   *   is_fsym_f(t)?get_tail(t):make_empty() 
   *   the neutral element is returned when it is not a list operator
   *   this occurs because the last element of a loop may not be a list
   */ 
  private static Expression genGetTail(TomName opName, TomTerm var) {
    TomSymbol tomSymbol = TomConstraintCompiler.getSymbolTable().getSymbolFromName(((Name)opName).getString());
    TomType domain = TomBase.getSymbolDomain(tomSymbol).getHeadconcTomType();
    TomType codomain = TomBase.getSymbolCodomain(tomSymbol);
    if(domain==codomain) {
      return `Conditional(IsFsym(opName,var),GetTail(opName, var), TomTermToExpression(BuildEmptyList(opName)));
    }
    return `GetTail(opName, var);
  }
}
