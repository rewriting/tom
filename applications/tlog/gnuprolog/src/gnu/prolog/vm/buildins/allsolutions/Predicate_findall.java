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
package gnu.prolog.vm.buildins.allsolutions;
import gnu.prolog.term.*;
import gnu.prolog.vm.*;
import gnu.prolog.vm.interpreter.Predicate_call;
import java.util.*;

/** prolog code 
  */
public class Predicate_findall implements PrologCode
{
  static final AtomTerm listAtom = AtomTerm.get("list");
  static final CompoundTermTag listTag = CompoundTermTag.get(".",2);
  /** this method is used for execution of code
    * @param interpreter interpreter in which context code is executed 
    * @param backtrackMode true if predicate is called on backtracking and false otherwise
    * @param args arguments of code
    * @return either SUCCESS, SUCCESS_LAST, or FAIL.
    */
  public int execute(Interpreter interpreter, boolean backtrackMode, gnu.prolog.term.Term args[]) 
         throws PrologException
  {
    ArrayList list = new ArrayList(); 
    checkList(args[2]);
    int rc = findall(interpreter, backtrackMode, args[0], args[1], list);
    if (rc == SUCCESS_LAST)
    {
      return interpreter.unify(args[2],CompoundTerm.getList(list));
    }
    return FAIL;
  }

  /** this method is used for execution of code
    * @param interpreter interpreter in which context code is executed 
    * @param backtrackMode true if predicate is called on backtracking and false otherwise
    * @param args arguments of code
    * @return either SUCCESS, SUCCESS_LAST, or FAIL.
    */
  public static int findall(Interpreter interpreter, boolean backtrackMode, Term template, Term goal, ArrayList list) 
         throws PrologException
  {
    int startUndoPosition = interpreter.getUndoPosition();
    BacktrackInfo startBi = interpreter.peekBacktrackInfo();
    try
    {
      try
      {
        boolean callBacktrackMode = false;
        int rc;
        do
        {
          rc = Predicate_call.staticExecute(interpreter, callBacktrackMode, goal);
          callBacktrackMode = true;
          if (rc != FAIL)
          {
            list.add(template.clone());
          }
        } while (rc != SUCCESS_LAST && rc != FAIL);
        if (rc == SUCCESS_LAST)
        {
          interpreter.undo(startUndoPosition);
        }
        return SUCCESS_LAST;
      }
      catch(RuntimeException rex)
      {
        PrologException.systemError();
        return FAIL; //fake return
      }
    }
    catch(PrologException ex)
    {
      interpreter.popBacktrackInfoUntil(startBi);
      interpreter.undo(startUndoPosition);
      throw ex;
    }
  }


  public static void checkList(Term list) throws PrologException
  {
    Term exArg = list;
    while(list != AtomTerm.emptyList)
    {
      if (list instanceof VariableTerm)
      {
        return;
      }
      if (!(list instanceof CompoundTerm))
      {
        PrologException.typeError(listAtom, exArg);
      }
      CompoundTerm ct = (CompoundTerm)list;
      if (ct.tag != listTag)
      {
        PrologException.typeError(listAtom, exArg);
      }
      list = ct.args[1].dereference();
    }
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

