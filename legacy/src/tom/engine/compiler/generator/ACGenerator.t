/*
 *
 * TOM - To One Matching Compiler
 * 
 * Copyright (c) 2000-2017, Inria
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

import java.util.logging.Logger;

import tom.engine.adt.tominstruction.types.*;
import tom.engine.adt.tomexpression.types.*;
import tom.engine.adt.tomexpression.types.expression.*;
import tom.engine.adt.tomname.types.*;
import tom.engine.adt.tomname.types.tomname.*;
import tom.engine.adt.tomterm.types.*;
import tom.engine.adt.tomtype.types.*;
import tom.engine.adt.tomslot.types.*;
import tom.engine.adt.tomoption.types.*;
import tom.engine.adt.tomterm.types.tomterm.*;
import tom.library.sl.*;
import tom.engine.tools.SymbolTable;
import tom.engine.tools.TomConstraintPrettyPrinter;
import tom.engine.exception.TomRuntimeException;
import tom.engine.adt.tomsignature.types.*;
import tom.engine.TomBase;
import tom.engine.TomMessage;
import tom.engine.compiler.*;
import tom.engine.compiler.Compiler;

// TODO : move all this in the constraintgenerator.
// we should only generate the functions once per operator

/**
 * AC Generator
 */
public class ACGenerator implements IBaseGenerator {

  %include { ../../adt/tomsignature/TomSignature.tom }
  %include { ../../../library/mapping/java/sl.tom}	

  %typeterm ACGenerator {
    implement { ACGenerator }
    is_sort(t) { ($t instanceof ACGenerator) }
  }

  private Compiler compiler; 
  private ConstraintGenerator constraintGenerator; 
  
  public Compiler getCompiler() {
    return this.compiler;
  }

  private Logger getLogger() {
    return Logger.getLogger(getClass().getName());
  }
  
  /**
   * Shared Functions 
   */
  protected String findOriginTrackingFileName(OptionList optionList) {
    %match(optionList) {
      concOption(_*,OriginTracking[FileName=fileName],_*) -> { return `fileName; }
    }
    return "unknown filename";
  }

  protected int findOriginTrackingLine(OptionList optionList) {
    %match(optionList) {
      concOption(_*,OriginTracking[Line=line],_*) -> { return `line; }
    }
    return -1;
  }

  public ACGenerator(Compiler compiler, ConstraintGenerator generator) {
    this.compiler = compiler;
    this.constraintGenerator = generator;
  }
 
  public Expression generate(Expression expression) throws VisitFailure {
    //System.out.println("\n *** generate: " + expression);
    //return `TopDownIdStopOnSuccess(Generator(this)).visitLight(expression);
    return `TopDown(Generator(this)).visitLight(expression);
  }

  /**
   * generate code for a pattern of the form F_ac(X*,Y*,...,Y*)
   *
   * If we find ConstraintToExpression it means that this constraint was not processed	
   */
  %strategy Generator(acg:ACGenerator) extends Identity() {
    visit Expression {
      ConstraintToExpression(MatchConstraint[Pattern=pattern@RecordAppl[NameList=concTomName(Name(symbolName)),Slots=concSlot(PairSlotAppl[Appl=var_x@VariableStar[AstName=name_x]],tail*), Options=options],Subject=subject]) -> {
        if (TomBase.hasTheory(`pattern,`AC())) {
          int mult_x = 1;
          int mult_y = 0;
          TomName name_y = null;
          TomTerm var_y = null;

          //System.out.println("\n *** ACGenerator on: " + `pattern);

          for(Slot t:`tail.getCollectionconcSlot()) {
            %match(t) {
              PairSlotAppl[Appl=var@VariableStar[AstName=name]] -> {
                if (null == name_y && ! `name.equals(`name_x)) {
                  name_y = `name;
                  var_y = `var;
                }

                //System.out.println("name   = " + `name);
                //System.out.println("name_x = " + `name_x);
                //System.out.println("name_y = " + name_y);

                if (`name.equals(`name_x)) {
                  mult_x++;
                } else if (`name.equals(name_y)) {
                  mult_y++;
                } else {
                  throw new TomRuntimeException("Bad VariableStar: " + `var);
                }
              }
            }
          }
          //System.out.println("mult_x = " + mult_x);
          //System.out.println("mult_y = " + mult_y);
          if (mult_x>=1 && mult_y==1) {
            return `ACMatchLoop(symbolName,var_x,var_y,mult_x,subject);
          } else if (mult_x==1 && mult_y>=1) {
            return `ACMatchLoop(symbolName,var_y,var_x,mult_y,subject);
          } else {
            //System.out.println("Cannot compile this AC pattern: " + TomConstraintPrettyPrinter.prettyPrint(`pattern));
            //throw new TomRuntimeException("Bad AC pattern: " + `pattern);
            String fileName = acg.findOriginTrackingFileName(`options);
            int line = acg.findOriginTrackingLine(`options);
            TomMessage.error(acg.getLogger(), fileName, line,
                TomMessage.cannotCompileACPattern, TomConstraintPrettyPrinter.prettyPrint(`pattern));
          }
        }

      }
    } // end visit
  } // end strategy  

}
