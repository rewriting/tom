 /*
  
    TOM - To One Matching Compiler

    Copyright (C) 2000-2003  LORIA (CNRS, INPL, INRIA, UHP, U-Nancy 2)
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
    Christophe Mayer            ESIAL Student
    Julien Guyon

*/

package jtom.checker;

import jtom.TomEnvironment;
import jtom.tools.TomTaskInput;

public class TomSyntaxChecker extends TomChecker {

  public TomSyntaxChecker(TomEnvironment env) {
  	super(env);	
  }

  public void process(TomTaskInput input) {
    try {
    	this.input = input;
			strictType = input.isStrictType();
			warningAll = input.isWarningAll();
			noWarning = input.isNoWarning();
			long startChrono = 0;
			boolean verbose = input.isVerbose();
			if(verbose) {
				startChrono = System.currentTimeMillis();
			}
    	syntaxCheck(input.getTerm());
	  	if(verbose) {
		  	System.out.println("TOM syntax Checking phase (" + (System.currentTimeMillis()-startChrono)+ " ms)");
	  	}
    } catch (Exception e) {
			e.printStackTrace();
			return;
    }
		int nbError = getNumberFoundError();
		if(nbError > 0 ) {
		  for(int i=0 ; i<nbError ; i++) {
				System.out.println(getMessage(i));
		  }
		  String msg = "Tom Checker:  Encountered " + nbError + " errors during verification phase.\nNo file generated.";
		  System.out.println(msg);
		  return;
		}
			//	Start next task
    if(nextTask != null) {
      nextTask.process(input);
    }
  }

} // Class TomSyntaxChecker
