/*
  
    TOM - To One Matching Compiler

    Copyright (C) 2000-2003 INRIA
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
 
import aterm.*;

import java.io.FileOutputStream;
import java.io.BufferedWriter;
import java.io.OutputStreamWriter;
import java.io.Writer;
import java.io.IOException;
import java.util.HashMap;

import jtom.adt.tomsignature.types.*;

import jtom.tools.TomTask;
import jtom.tools.TomTaskInput;
import jtom.tools.OutputCode;
import jtom.tools.SingleLineOutputCode;
import jtom.exception.TomRuntimeException;
import jtom.TomEnvironment;

public class TomJavaGenerator extends TomImperativeGenerator {
  
  public TomJavaGenerator(TomEnvironment environment, OutputCode output, TomTaskInput input) {
		super(environment, output, input);
  }

// ------------------------------------------------------------
  %include { ../../adt/TomSignature.tom }
// ------------------------------------------------------------

	protected void buildDeclaration(TomTerm var, String name, String type, TomType tlType) {
		out.write(deep,getTLCode(tlType) + " ");
		generate(out,deep,var);
    
		if(!isBoolType(type) &&
			 !isIntType(type) &&
			 !isDoubleType(type)) {
			out.writeln(" = null;");
		} else {
			out.writeln(";");
		}
	}

}