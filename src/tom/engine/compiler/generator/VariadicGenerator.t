/*
 *
 * TOM - To One Matching Compiler
 * 
 * Copyright (c) 2000-2014, Universite de Lorraine, Inria
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
package tom.engine.compiler.generator;

import tom.engine.adt.tominstruction.types.*;
import tom.engine.adt.tomexpression.types.*;
import tom.engine.adt.tomexpression.types.expression.*;
import tom.engine.adt.tomname.types.*;
import tom.engine.adt.tomname.types.tomname.*;
import tom.engine.adt.tomterm.types.*;
import tom.engine.adt.tomtype.types.*;
import tom.engine.adt.tomterm.types.tomterm.*;
import tom.engine.adt.code.types.*;
import tom.library.sl.*;
import tom.engine.tools.SymbolTable;
import tom.engine.exception.TomRuntimeException;
import tom.engine.adt.tomsignature.types.*;
import tom.engine.TomBase;
import tom.engine.compiler.ConstraintGenerator;
import tom.engine.compiler.Compiler;

/**
 * Variadic Generator
 */
public class VariadicGenerator implements IBaseGenerator {

  %include { ../../adt/tomsignature/TomSignature.tom }
  %include { ../../../library/mapping/java/sl.tom }	
  %include { expressionstrategies.tom }	

  %typeterm VariadicGenerator {
    implement { VariadicGenerator }
    is_sort(t) { ($t instanceof VariadicGenerator) }
  }

  private Compiler compiler; 
  private ConstraintGenerator constraintGenerator; 
 
  public VariadicGenerator(Compiler myCompiler, ConstraintGenerator myConstraintGenerator) {
    this.compiler = myCompiler;
    this.constraintGenerator = myConstraintGenerator;
  }

  public Compiler getCompiler() {
    return this.compiler;
  }
 
  public ConstraintGenerator getConstraintGenerator() {
    return this.constraintGenerator;
  }
 
  public Expression generate(Expression expression) throws VisitFailure {
    return `TopDownWhenExprConstrOrTerm(Generator(this)).visitLight(expression);
  }

  // If we find ConstraintToExpression it means that this constraint was not processed	
  %strategy Generator(vg:VariadicGenerator) extends Identity() {
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
      ConstraintToExpression(MatchConstraint[Pattern=v@VariableStar[],Subject=VariableHeadList(opName,begin,end@BQVariableStar[AstType=type]),AstType=aType]) -> {
        Expression doWhileTest = `Negation(EqualBQTerm(type,end,begin));
        Expression testEmpty = vg.getConstraintGenerator().genIsEmptyList(`opName,`end);
        Expression endExpression = 
               `TomInstructionToExpression(If(testEmpty,Assign(end,BQTermToExpression(begin)),Assign(end,vg.genGetTail(opName,end))));
        /* if we have a varStar, we generate its declaration also */
        if (`v.isVariableStar()) {
          Expression varDeclaration =
            `ConstraintToExpression(MatchConstraint(v,ExpressionToBQTerm(GetSliceList(opName,begin,end,BuildEmptyList(opName))),aType));
          return `And(DoWhileExpression(endExpression,doWhileTest),varDeclaration);
        }
        return `DoWhileExpression(endExpression,doWhileTest);		        		      
      }
    } // end visit
    visit BQTerm {
      // generate getHead
      ListHead(opName,type,variable) -> {
        return `ExpressionToBQTerm(vg.genGetHead(opName,type,variable));
      }
      // generate getTail
      ListTail(opName,variable) -> {
        return `ExpressionToBQTerm(vg.genGetTail(opName,variable));
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
  private Expression genGetHead(TomName opName, TomType type, BQTerm var) {
    TomSymbol tomSymbol = getCompiler().getSymbolTable().getSymbolFromName(((Name)opName).getString());
    TomType domain = TomBase.getSymbolDomain(tomSymbol).getHeadconcTomType();
    TomType codomain = TomBase.getSymbolCodomain(tomSymbol);
    if(domain==codomain) {
      return `Conditional(IsFsym(opName,var),GetHead(opName, type, var),BQTermToExpression(var));
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
  private Expression genGetTail(TomName opName, BQTerm var) {
    TomSymbol tomSymbol = getCompiler().getSymbolTable().getSymbolFromName(((Name)opName).getString());
    TomType domain = TomBase.getSymbolDomain(tomSymbol).getHeadconcTomType();
    TomType codomain = TomBase.getSymbolCodomain(tomSymbol);
    if(domain==codomain) {
      return `Conditional(IsFsym(opName,var),GetTail(opName, var), BQTermToExpression(BuildEmptyList(opName)));
    }
    return `GetTail(opName, var);
  }
}
