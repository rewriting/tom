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
import tom.engine.adt.code.types.*;
import tom.library.sl.*;
import tom.engine.tools.SymbolTable;
import tom.engine.exception.TomRuntimeException;
import tom.engine.adt.tomsignature.types.*;
import tom.engine.TomBase;
import tom.engine.compiler.*;
import tom.engine.adt.theory.types.*;
import tom.engine.compiler.Compiler;

/**
 * Syntactic Generator
 */
public class SyntacticGenerator implements IBaseGenerator {

  %include { ../../adt/tomsignature/TomSignature.tom }
  %include { ../../../library/mapping/java/sl.tom }
  %include { expressionstrategies.tom }	

  %typeterm SyntacticGenerator {
    implement { SyntacticGenerator }
    is_sort(t) { ($t instanceof SyntacticGenerator) }
  }

  private Compiler compiler; 
  private ConstraintGenerator constraintGenerator; // only present for "compatibility" : cf. ConstraintGenerator.t and look around ".newInstance(this.getCompiler(),this);" 
 
  public SyntacticGenerator(Compiler myCompiler, ConstraintGenerator myConstraintGenerator) {
    this.compiler = myCompiler;
    this.constraintGenerator = myConstraintGenerator; // only present for "compatibility" : cf. ConstraintGenerator.t and look around ".newInstance(this.getCompiler(),this);" 
  }

  public Compiler getCompiler() {
    return this.compiler;
  }
 
  public ConstraintGenerator getConstraintGenerator() {
    return this.constraintGenerator;
  }
 
  public Expression generate(Expression expression) throws VisitFailure {
    return  `TopDownWhenExpression(Generator(this)).visitLight(expression);
  }

  // If we find ConstraintToExpression it means that this constraint was not processed	
  %strategy Generator(sg:SyntacticGenerator) extends Identity() {
    visit Expression {
      // generate is_fsym(t,f)
      ConstraintToExpression(MatchConstraint[Pattern=currentTerm@RecordAppl[NameList=concTomName(name)],Subject=SymbolOf(subject),AstType=aType]) -> {
        //TomType termType = sg.getCompiler().getTermTypeFromName(`name);
        //return sg.buildEqualFunctionSymbol(termType,`subject,`name,TomBase.getTheory(`currentTerm));
        return sg.buildEqualFunctionSymbol(`aType,`subject,`name,TomBase.getTheory(`currentTerm));
      }
      // generate equality test
      ConstraintToExpression(MatchConstraint[Pattern=TestVar(v@(Variable|VariableStar)[AstType=type]),Subject=t]) -> {
        return `EqualBQTerm(type,t,TomBase.convertFromVarToBQVar(v));
      }
    } // end visit
  } // end strategy	
  
  private Expression buildEqualFunctionSymbol(TomType type, BQTerm subject, TomName name, Theory theory) {
    TomSymbol tomSymbol = getCompiler().getSymbolTable().getSymbolFromName(name.getString());
    if(getCompiler().getSymbolTable().isBuiltinType(TomBase.getTomType(`type))) {
      if(TomBase.isListOperator(tomSymbol) || TomBase.isArrayOperator(tomSymbol) || TomBase.hasIsFsymDecl(tomSymbol)) {
        return `IsFsym(name,subject);
      } else {
        return `EqualBQTerm(type,BuildConstant(name),subject);
      }
    } else if(TomBase.hasTheory(theory, `AU())) {
      return `IsSort(type,subject);
    }
    //DEBUG System.out.println("In buildEqualFunctionSymbol name = " + name);
    return
      `IsFsym(name,ExpressionToBQTerm(Cast(type,BQTermToExpression(subject))));
      //`IsFsym(name,subject);
  }
}
