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

  package jtom.backend;
 

import java.io.FileOutputStream;
import java.io.BufferedWriter;
import java.io.OutputStreamWriter;
import java.io.Writer;
import java.io.IOException;

import jtom.checker.TomCheckerMessage;
import jtom.tools.TomTask;
import jtom.tools.OutputCode;
import jtom.TomEnvironment;

public class TomBackend extends TomTask {
  
  private final static int defaultDeep = 2;
  private TomAbstractGenerator generator;
  private Writer writer;
  public TomBackend(TomEnvironment environment) {
    super("Tom Generator", environment);
  }

  public void initProcess() {
    try {
      boolean pretty = getInput().isPretty();
        // initialize outputCode
      writer = new BufferedWriter(
        new OutputStreamWriter(
          new FileOutputStream(getInput().getOutputFileName())
          ));
      OutputCode output = new OutputCode(writer, 
                                         getInput().isCCode(), 
                                         pretty, 
                                         defaultDeep);
        // give the "good" generator
      if (getInput().isCCode()) {
        generator = new TomCGenerator (environment(), output, getInput());
      } else if (getInput().isJCode()) {
        generator = new TomJavaGenerator (environment(), output, getInput());
      } else if (getInput().isCamlCode()) {
        generator = new TomCamlGenerator (environment(), output, getInput());
      }
    } catch (Exception e) {
      addError("Exception occurs in TomBackend Init: "+e.getMessage(), 
               getInput().getInputFileName(), TomCheckerMessage.DEFAULT_ERROR_LINE_NUMBER, TomCheckerMessage.TOM_ERROR);
      e.printStackTrace();
    }
  }
	
  public void process() {
    try {
      boolean verbose = getInput().isVerbose();
      long startChrono = 0;
      if(verbose) {
        startChrono = System.currentTimeMillis();
      }
      generator.generate(defaultDeep, getInput().getTerm());
      if(verbose) {
        System.out.println("TOM generation phase (" + (System.currentTimeMillis()-startChrono)+ " ms)");
      }
    } catch (Exception e) {
      addError("Exception occurs in TomGenerator: "+e.getMessage(), 
               getInput().getInputFileName(), TomCheckerMessage.DEFAULT_ERROR_LINE_NUMBER, TomCheckerMessage.TOM_ERROR);
      e.printStackTrace();
      return;
    }
  }

  protected void closeProcess() {
    super.closeProcess();
    try {
      writer.close();
    } catch(IOException e) {}
  }

}
  
