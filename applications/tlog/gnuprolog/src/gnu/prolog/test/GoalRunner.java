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
import gnu.prolog.term.*;
import gnu.prolog.io.*;
import java.io.*;
import java.util.*;
import gnu.prolog.database.PrologTextLoaderError;
public class GoalRunner
{
  public static void main (String args[]) 
  {
    try
    {
      System.out.println("GNU Prolog for Java Goal runner (c) Constantine Plotnikov, 1997-1999.");
      if (args.length < 2)
      {
        System.out.println("usage: java gnu.prolog.test.GoalRunner <text to load> <goal to run>");
        System.out.println("example: java gnu.prolog.test.GoalRunner append.pro append([a,b],[c,d],R)");
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
      rd_ops.operatorSet = env.getOperatorSet();
      WriteOptions wr_ops = new WriteOptions();
      wr_ops.operatorSet = env.getOperatorSet();
      Term goalTerm = trd.readTermEof(rd_ops);
      // temp
      //PrologCode code = env.getPrologCode(CompoundTermTag.get("append",3));
      //System.err.println(code);
      // end temp

      Interpreter.Goal goal = interpreter.prepareGoal(goalTerm);
      String response;
      loop: do
      {
        long startTime = System.currentTimeMillis();
        int rc = interpreter.execute(goal);
        long stopTime = System.currentTimeMillis();
        System.out.println("time = "+(stopTime-startTime)+"ms");
        response = "n";
        switch (rc)
        {
        case PrologCode.SUCCESS:
          {
            WriteOptions options = new WriteOptions();
            options.operatorSet = new OperatorSet();
            Iterator ivars2 = rd_ops.variableNames.keySet().iterator();
            Iterator ivars = rd_ops.variableNames.keySet().iterator();
            while (ivars.hasNext())
            {
              String name = (String)ivars.next();
              out.print(name+" = ");
              out.print(options,((Term)rd_ops.variableNames.get(name)).dereference());
              out.print("; ");
            }
            out.println();
            out.print("SUCCESS. redo (y/n/a)?");
            out.flush();
            response = kin.readLine();
  
            if (response.equals("a"))
            {
              interpreter.stop(goal);
              goal = interpreter.prepareGoal(goalTerm);
            }
  
            if (response.equals("n"))
            {
              return;
            }
            break;
          }
        case PrologCode.SUCCESS_LAST:
          {
            WriteOptions options = new WriteOptions();
            options.operatorSet = new OperatorSet();
            Iterator ivars2 = rd_ops.variableNames.keySet().iterator();
            while (ivars2.hasNext())
            {
              String name = (String)ivars2.next();
              out.print(name+" = ");
              out.print(options, ((Term)rd_ops.variableNames.get(name)).dereference());
              out.print("; ");
            }
            out.println();
            out.println("SUCCESS LAST");
            out.flush();
            return;
          }
        case PrologCode.FAIL:
          out.println("FAIL");
          out.flush();
          return;
        }
      } while (true);
    }
    catch(Exception ex)
    {
      ex.printStackTrace();
    }
  }
}
