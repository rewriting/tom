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
package tom.engine.compiler.propagator;

import tom.engine.adt.tomconstraint.types.*;
import tom.engine.adt.tomsignature.types.*;
import tom.engine.adt.tomtype.types.*;
import tom.engine.adt.tomterm.types.*;
import tom.engine.adt.tomname.types.*;
import tom.engine.adt.tomterm.types.tomterm.*;
import tom.library.sl.*;
import tom.engine.adt.tomslot.types.*;
import tom.engine.compiler.*;
import tom.engine.TomBase;
import tom.engine.exception.TomRuntimeException;
import java.util.ArrayList;
import tom.engine.compiler.Compiler;

/**
 * AC propagator
 */
public class ACPropagator implements IBasePropagator {

//--------------------------------------------------------	
  %include { ../../adt/tomsignature/TomSignature.tom }	
  %include { ../../../library/mapping/java/sl.tom}
//--------------------------------------------------------

  public Constraint propagate(Constraint constraint) throws VisitFailure {
    return `constraint;
    //return (Constraint)`TopDown(ACPatternMatching()).visitLight(constraint);		
  }	

  %strategy ACMatching() extends Identity() {
    visit Constraint {
      // TODO RK 04/07/2008: here we should handle all the cases of the AC match
      // that are supposed to be solved by programm transformation 
      // (basically to reduce all the cases to f(X*,Y*))
    }
  }

}
