
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

*/

package jtom.tools;

import java.io.*;
import aterm.*;

public class Tools {
  public final static String TEXT_NODE = "TextNode";
  public final static String COMMENT_NODE = "CommentNode";
  public final static String PROCESSING_INSTRUCTION_NODE = "ProcessingInstructionNode";
  public final static String ATTRIBUTE_NODE = "AttributeNode";
  public final static String CONC_TNODE = "concNodeTerm";
  public final static String ELEMENT_NODE = "ElementNode";
  


  
  public static void generateOutput(String outputFileName, ATerm ast) {
    try {
      OutputStream output = new FileOutputStream(outputFileName);
      Writer writer = new BufferedWriter(new OutputStreamWriter(output));
      writer.write(ast.toString());
      writer.flush();
      writer.close();
    } catch(IOException e) {
      System.out.println("write error");
      e.printStackTrace();
    }
  }

}
