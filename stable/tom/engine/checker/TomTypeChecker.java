/*
 *   
 * TOM - To One Matching Compiler
 * 
 * Copyright (C) 2000-2004 INRIA
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
 * Pierre-Etienne Moreau	e-mail: Pierre-Etienne.Moreau@loria.fr
 * Julien Guyon
 *
 **/

package jtom.checker;

import jtom.TomEnvironment;
import jtom.TomMessage;
import jtom.tools.TomTaskInput;

public class TomTypeChecker extends TomChecker {

  public TomTypeChecker() {
  	super("Tom TypeChecker");	
  }
	
  public void process() {
    try {
		  long startChrono = 0;
		  if(verbose) { startChrono = System.currentTimeMillis();
		  		  	}
			checkTypeInference(environment().getTerm());
	  	
	  	if(verbose) {
	    	System.out.println("TOM type Checking phase (" + (System.currentTimeMillis()-startChrono)+ " ms)");
	  	}
    } catch (Exception e) {
			addError("Exception occurs in TomTypeChecker: "+e.getMessage(), getInput().getInputFile().getName(), TomMessage.DEFAULT_ERROR_LINE_NUMBER, TomMessage.TOM_ERROR);
			e.printStackTrace();
			return;
    }
  }

} // Class TomSyntaxChecker
