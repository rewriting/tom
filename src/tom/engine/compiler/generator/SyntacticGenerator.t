/*
 *
 * TOM - To One Matching Compiler
 * 
 * Copyright (c) 2000-2007, INRIA
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
import tom.engine.adt.theory.types.*;

/**
 * Syntactic Generator
 */
public class SyntacticGenerator implements IBaseGenerator {

  %include { ../../adt/tomsignature/TomSignature.tom }
  %include { ../../../library/mapping/java/sl.tom}	

  public Expression generate(Expression expression) throws VisitFailure {
    return  (Expression)`TopDown(Generator()).visit(expression);
  }

  // If we find ConstraintToExpression it means that this constraint was not processed	
  %strategy Generator() extends Identity() {
    visit Expression {
      // generate is_fsym(t,f)
      ConstraintToExpression(MatchConstraint(currentTerm@RecordAppl[NameList=(name)],SymbolOf(subject))) -> {
        TomType termType = ConstraintCompiler.getTermTypeFromName(`name);        
        Expression check = `buildEqualFunctionSymbol(termType, subject, name, TomBase.getTheory(currentTerm));
        return check;
      }
      // generate equality
      ConstraintToExpression(MatchConstraint(t@Subterm[],u)) -> {
        return `EqualTerm(ConstraintCompiler.getTermTypeFromTerm(t),t,u);
      }
      // generate equality test
      ConstraintToExpression(MatchConstraint(TestVar(v@(Variable|VariableStar)[AstType=type]),t)) -> {
        return `EqualTerm(type,v,t);
      }
    } // end visit
  } // end strategy	
  
  private static Expression buildEqualFunctionSymbol(TomType type, TomTerm subject,  TomName name, Theory theory) {    
    TomSymbol tomSymbol = ConstraintCompiler.getSymbolTable().getSymbolFromName(name.getString());
    if(ConstraintCompiler.getSymbolTable().isBuiltinType(TomBase.getTomType(`type))) {
      if(TomBase.isListOperator(tomSymbol) || TomBase.isArrayOperator(tomSymbol) || TomBase.hasIsFsymDecl(tomSymbol)) {
        return `IsFsym(name,subject);
      } else {
        return `EqualTerm(type,BuildConstant(name),subject);
      }
    } else if(TomBase.hasTheory(theory, `TrueAU())) {
      return `IsSort(type,subject);
    } 
    return `IsFsym(name,subject);
  }
}
