/* GNU Prolog for Java
 * Copyright (C) 1997-1999  Constantine Plotnikov
 * This library is free software; you can redistribute it and/or
 * modify it under the terms of the GNU Library General Public
 * License as published by the Free Software Foundation; either
 * version 2 of the License, or (at your option) any later version.
 * This library is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the GNU
 * Library General Public License for more details.
 *
 * You should have received a copy of the GNU Library General Public
 * License along with this library; if not, write to the
 * Free Software Foundation, Inc., 59 Temple Place - Suite 330,
 * Boston, MA  02111-1307, USA. The text ol license can be also found 
 * at http://www.gnu.org/copyleft/lgpl.html
 */
package gnu.prolog.test;
import gnu.prolog.vm.*;
import gnu.prolog.database.*;
import gnu.prolog.term.*;
import gnu.prolog.io.*;
import java.io.*;
import java.util.*;

public class CodeDumper
{
  public static void main (String args[]) 
  {
    try
    {
      System.out.println("GNU Prolog for Java  Interprted Code dumper (c) Constantine Plotnikov, 1997-1999.");
      if (args.length < 2)
      {
        System.out.println("usage: java gnu.prolog.test.CodeDumper <text to load> <predicate indicator>");
        System.out.println("example: java gnu.prolog.test.CodeDumper append.pro append/3");
      }
      String textToLoad = args[0];
      String goalToRun = args[1];
      Environment env = new Environment();
      env.ensureLoaded(AtomTerm.get(textToLoad));
      Interpreter interpreter = env.createInterpreter();
      env.runIntialization(interpreter);
      for (Iterator ierr = env.getLoadingErrors().iterator();ierr.hasNext();)
      {
        PrologTextLoaderError err = (PrologTextLoaderError)ierr.next();
        System.err.println(err);
        //err.printStackTrace();
      }
      LineNumberReader kin = new LineNumberReader(new InputStreamReader(System.in));
      StringReader rd = new StringReader(goalToRun);
      TermReader trd = new TermReader(rd);
      TermWriter out = new TermWriter(new OutputStreamWriter(System.out));
      ReadOptions rd_ops = new ReadOptions();
      rd_ops.operatorSet = new OperatorSet();
      WriteOptions wr_ops = new WriteOptions();
      Term goalTerm = trd.readTermEof(rd_ops);
      PrologCode code = env.getPrologCode(CompoundTermTag.get(goalTerm));
      System.out.println(code);
    }
    catch(Exception ex)
    {
      ex.printStackTrace();
    }
  }
}
