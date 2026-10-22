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
 **/

package tom.engine.checker;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.logging.Level;

import tom.engine.TomMessage;
import tom.engine.tools.TomGenericPlugin;
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
import tom.engine.adt.tomconstraint.types.constraint.*;

import tom.engine.tools.ASTFactory;

import tom.library.sl.*;
import tom.library.sl.VisitFailure;

/**
 * The SyntaxChecker plugin - justs adds anti-pattern facilities to the
 * SyntaxCheckerPlugin.
 */
public class SyntaxCheckerApPlugin extends SyntaxCheckerPlugin {

  %include { ../adt/tomsignature/TomSignature.tom }
  %include { ../../library/mapping/java/sl.tom }
 
  %typeterm SyntaxCheckerPlugin { implement { SyntaxCheckerPlugin } }

  /**
   * Basicaly ignores the anti-symbol
   */

  public TermDescription validateTerm(TomTerm term, TomType expectedType, boolean listSymbol, boolean topLevel) {
    %match(TomTerm term) {
      // validate that after the anti symbol we have a valid term  
      AntiTerm(t@(TermAppl|Variable|RecordAppl)[Options=options]) -> {
        checkForAnnotations(`t,`options);
        return super.validateTerm(`t, expectedType, listSymbol, topLevel);
      }
    }
    return super.validateTerm(term, expectedType, listSymbol, topLevel);
  }

  /**
   * Checks if the given term contains annotations
   * 
   * @param t the term to search
   */

  private void checkForAnnotations(TomTerm t, OptionList options) {	  
    String fileName = findOriginTrackingFileName(options);
    int decLine = findOriginTrackingLine(options);
    try {
      `TopDown(CheckForAnnotations(fileName,decLine,t,this)).visitLight(t);
    } catch(VisitFailure e) {
      throw new TomRuntimeException("Cannot be there");
    }
  }

  /**
   * Given a term, it checks if it contains annotations
   * - if the annotations are on head, allow them
   * - error otherwise 
   */ 

  %strategy CheckForAnnotations(fileName:String, decLine:int, headTerm: TomTerm, tsca:SyntaxCheckerPlugin) extends Identity() {
    visit TomTerm {
      t@(TermAppl|Variable|RecordAppl)[Constraints=concConstraint(_*,AliasTo[],_*)] -> {
        if(`t != headTerm) {
          TomMessage.error(tsca.getLogger(),fileName,decLine,TomMessage.illegalAnnotationInAntiPattern);
        }	
      }
    }
  }

}
