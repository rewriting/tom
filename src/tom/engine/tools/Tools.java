/*
 *
 * TOM - To One Matching Compiler
 *
 * Copyright (c) 2000-2015, Universite de Lorraine, Inria
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

package tom.engine.tools;

import java.io.BufferedWriter;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.Writer;
import java.util.Collection;


public class Tools {
  public static void generateOutput(String outputFileName, Object ast) {
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

  public static void generateOutputFromCollection(String outputFileName, Collection collection) {
    try {
      OutputStream output = new FileOutputStream(outputFileName);
      Writer writer = new BufferedWriter(new OutputStreamWriter(output));
      writer.write(collection.toString());
      writer.flush();
      writer.close();
    } catch(IOException e) {
      System.out.println("write error");
      e.printStackTrace();
    }
  }
}
