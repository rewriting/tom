 /*
  
    TOM - To One Matching Compiler

    Copyright (C) 2000-2004 INRIA
			            Nancy, France.

    This program is free software; you can redistribute it and/or modify
    it under the terms of the GNU General Public License as published by
    the Free Software Foundation; either version 2 of the License, or
    (at your option) any later version.

    This program is distributed in the hope that it will be useful,
    but WITHOUT ANY WARRANTY; without even the implied warranty of
    MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
    GNU General Public License for more details.

    You should have received a copy of the GNU General Public License
    along with this program; if not, write to the Free Software
    Foundation, Inc., 59 Temple Place, Suite 330, Boston, MA  02111-1307 USA

    Pierre-Etienne Moreau	e-mail: Pierre-Etienne.Moreau@loria.fr

*/

package jtom.optimizer;

import aterm.*;

import java.util.*;

import jtom.TomEnvironment;
import jtom.adt.tomsignature.types.*;
import jtom.tools.TomTask;
import jtom.runtime.Collect1;
import jtom.xml.Constants;
import jtom.exception.*;

public class TomOptimizer extends TomTask {
	
  // ------------------------------------------------------------
  %include { ../../adt/TomSignature.tom }
  // ------------------------------------------------------------
		
  public TomOptimizer(TomEnvironment tomEnvironment) {
    super("Tom Optimizer", tomEnvironment);
  }

  public void initProcess() {
  } 
  
  public void process() {
    try {
      long startChrono = 0;
      boolean verbose = getInput().isVerbose();
      if(verbose) { startChrono = System.currentTimeMillis();}
      
      TomTerm optimizedTerm = optimize(getInput().getTerm());
      
      if(verbose) {
        System.out.println("TOM optimization phase (" + (System.currentTimeMillis()-startChrono)+ " ms)");
      }
      getInput().setTerm(optimizedTerm);
      
    } catch (Exception e) {
      addError("Exception occurs in TomOptimizer: "+e.getMessage(), getInput().getInputFileName(), 0, 0);
      e.printStackTrace();
      return;
    }
  }

  public TomTerm optimize(TomTerm subject) {
    return subject;
  }

}  //Class TomOptimizer
