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
 *
 **/

package jtom.backend;

import java.io.IOException;


import jtom.adt.tomsignature.types.*;
import jtom.tools.OutputCode;

import tom.platform.OptionManager;

public class TomJavaGenerator extends TomImperativeGenerator {
   
  public TomJavaGenerator(OutputCode output, OptionManager optionManager) {
    super(output, optionManager);
    this.modifier += "public " ;
  }

// ------------------------------------------------------------
  %include { adt/TomSignature.tom }
// ------------------------------------------------------------

  protected void buildExpTrue(int deep) throws IOException {
    output.write(" true ");
  }
  
  protected void buildExpFalse(int deep) throws IOException {
    output.write(" false ");
  }
   
  protected void buildNamedBlock(int deep, String blockName, InstructionList instList) throws IOException {
    output.writeln(blockName + ": {");
    generateInstructionList(deep+1,instList);
    output.writeln("}");
  }

} // class TomJavaGenerator
