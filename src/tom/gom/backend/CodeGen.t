/*
 * Gom
 *
 * Copyright (C) 2006-2007, INRIA
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
 * Antoine Reilles  e-mail: Antoine.Reilles@loria.fr
 *
 **/

package tom.gom.backend;

import java.io.Writer;
import java.io.StringWriter;
import java.util.logging.Logger;
import java.util.logging.Level;
import tom.gom.tools.error.GomRuntimeException;
import tom.gom.adt.code.types.*;

public class CodeGen {

  %include { ../adt/code/Code.tom}

  private CodeGen() {
    /* Prevent instantiation */
  }
  
  /**
    * Generate code in a String.
    * 
    * @params code the Code to generate
    * @return the generated code
    */
  public static String generateCode(Code code) {
    StringWriter writer = new StringWriter();
    try {
      generateCode(code,writer);
    } catch (java.io.IOException e) {
      Logger.getLogger("CodeGen").log(
          Level.SEVERE,"Failed to generate code for " + code);
      
    }
    return writer.toString();
  }

  /**
    * Generate code in a writer.
    * 
    * @params code the Code to generate
    * @params writer where to generate
    */
  public static void generateCode(Code code, Writer writer)
    throws java.io.IOException {
    %match(code) {
      Code(scode) -> {
        writer.write(`scode);
        return;
      }
      CodeList() -> { return ; }
      CodeList(h,t*) -> {
        generateCode(`h,writer);
        generateCode(`t,writer);
        return;
      }
    }
    throw new GomRuntimeException("Can't generate code for " + code);
  }
}
