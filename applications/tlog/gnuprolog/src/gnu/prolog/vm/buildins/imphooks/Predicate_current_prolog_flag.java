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
package gnu.prolog.vm.buildins.imphooks;
import gnu.prolog.term.*;
import gnu.prolog.vm.*;
import java.util.*;

/** prolog code 
  */
public class Predicate_current_prolog_flag implements PrologCode
{
   private class CurrentPrologFlagBacktrackInfo extends BacktrackInfo
   {
     CurrentPrologFlagBacktrackInfo(){super(-1,-1);}
     Map map;
     Iterator keys; 
     int startUndoPosition;
     Term flag;
     Term value;
   }
  /** this method is used for execution of code
    * @param interpreter interpreter in which context code is executed 
    * @param backtrackMode true if predicate is called on backtracking and false otherwise
    * @param args arguments of code
    * @return either SUCCESS, SUCCESS_LAST, or FAIL.
    */
  public int execute(Interpreter interpreter, boolean backtrackMode, gnu.prolog.term.Term args[]) 
         throws PrologException
  {
    if (backtrackMode)
    {
      CurrentPrologFlagBacktrackInfo bi = 
        (CurrentPrologFlagBacktrackInfo)interpreter.popBacktrackInfo();
      interpreter.undo(bi.startUndoPosition);
      return nextSolution(interpreter,bi);
    }
    else
    {
      Term flag = args[0];
      Term value = args[1];
      if (flag instanceof AtomTerm)
      {
        Term val = interpreter.environment.getPrologFlag((AtomTerm)flag);
        if (val == null)
        {
          return FAIL;
        }
        return interpreter.unify(value, val.dereference());
      }
      CurrentPrologFlagBacktrackInfo bi = new CurrentPrologFlagBacktrackInfo();
      bi.map = interpreter.environment.getPrologFlags();
      bi.keys = bi.map.keySet().iterator();
      bi.startUndoPosition = interpreter.getUndoPosition();
      bi.flag = flag;
      bi.value = value;
      return nextSolution(interpreter,bi);
    }
  }

  private int nextSolution(Interpreter interpreter, 
                           CurrentPrologFlagBacktrackInfo bi) throws PrologException
  {
    while (bi.keys.hasNext())
    {
      AtomTerm f = (AtomTerm)bi.keys.next();
      Term v = (Term)bi.map.get(f);
      int rc = interpreter.simple_unify(f,bi.flag);
      if (rc == FAIL)
      {
        interpreter.undo(bi.startUndoPosition);
        continue;
      }
      rc = interpreter.simple_unify(v,bi.value);
      if (rc == FAIL)
      {
        interpreter.undo(bi.startUndoPosition);
        continue;
      }
      interpreter.pushBacktrackInfo(bi);
      return SUCCESS;
    }
    return FAIL;
  }
  /** this method is called when code is installed to the environment
    * code can be installed only for one environment.
    * @param environment environemnt to install the predicate
    */
  public void install(Environment env)
  {

  }

  /** this method is called when code is uninstalled from the environment
    * @param environment environemnt to install the predicate
    */
  public void uninstall(Environment env)
  {
  }
    
}

