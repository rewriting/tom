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
 * Julien Guyon
 *
 **/

package tom.engine.checker;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.logging.Level;

import tom.engine.TomMessage;
import tom.engine.adt.tomsignature.types.*;
import tom.engine.exception.TomRuntimeException;
import tom.engine.xml.Constants;
import tom.platform.OptionParser;
import tom.platform.adt.platformoption.types.PlatformOptionList;
import aterm.ATerm;
import tom.engine.tools.ASTFactory;

import tom.library.strategy.mutraveler.MuTraveler;
import jjtraveler.reflective.VisitableVisitor;
import jjtraveler.VisitFailure;

/**
 * The TomSyntaxChecker plugin - justs adds anti-pattern facilities to the TomSyntaxChecker.
 */
public class TomSyntaxCheckerAp extends TomSyntaxChecker {

  %include { adt/tomsignature/TomSignature.tom }

  /**
   * Basicaly ingnores the anti-symbol
   */
  public  TermDescription validateTerm(TomTerm term, TomType expectedType, boolean listSymbol, boolean topLevel, boolean permissive) {
    
	  %match(TomTerm term) {
    	
    	// validate that after the anti symbol we have a valid term  
    	AntiTerm(t) ->{
    		return super.validateTerm(`t, expectedType, listSymbol, topLevel, permissive);
    	}
	  }
	  
	  return super.validateTerm(term, expectedType, listSymbol, topLevel, permissive);
  }

  public  TermDescription analyseTerm(TomTerm term) {
   
      %match(TomTerm term) {
    	  
        // for the moment, the anti only on termappl and on named variables
    	AntiTerm(t@TermAppl(_,_,_,_))  -> {
    		return super.analyseTerm(`t);
    	}
    	AntiTerm(v@Variable(_,_,_,_))  -> {
    		return super.analyseTerm(`v);
    	}    	  
      }
      
      return super.analyseTerm(term);
  }
}
