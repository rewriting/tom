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
 * Pierre-Etienne Moreau  e-mail: Pierre-Etienne.Moreau@loria.fr
 * Antoine Reilles
 *
 **/

package jtom.verifier;

import jtom.tools.*;
import jtom.adt.tomsignature.types.*;
import jtom.TomMessage;

public class TomVerifierExtract extends TomTask {

    // ------------------------------------------------------------
  %include { ../adt/TomSignature.tom }
    // ------------------------------------------------------------

  public TomVerifierExtract() { 
    super("Tom verifier");
  }
  
  public TomVerifierExtract(String name) {
    super(name);
  }

  protected void process() {
    try {
      long startChrono = 0;
      boolean verbose = getInput().isVerbose();
      if(verbose) { startChrono = System.currentTimeMillis(); }
        // I may use my own datatype
      TomTerm extractTerm = `emptyTerm();
        // here the extraction stuff
      
      
      if(verbose) {
        System.out.println("TOM Verifier first extraction phase (" + (System.currentTimeMillis()-startChrono)+ " ms)");
      }
        // put extrated data in a file
      Tools.generateOutput(
			getInput().getOutputFileNameWithoutSuffix() + TomTaskInput.verifExtractionSuffix, 
        extractTerm);
    } catch (Exception e) {
      environment().messageError("Exception occured in TomVerifierExtract: " + e.getMessage(),
                                 getInput().getInputFile().getName(), 
                                 TomMessage.DEFAULT_ERROR_LINE_NUMBER);
      e.printStackTrace();
      return;
    }
  }
  
}
