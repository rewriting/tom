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
package gnu.prolog.vm.buildins.database;
import gnu.prolog.term.*;
import gnu.prolog.vm.*;
import gnu.prolog.database.*;
import java.util.HashSet;
import java.util.Iterator;

/** prolog code 
  */
public class Predicate_current_predicate implements PrologCode
{
  CompoundTermTag divideTag = CompoundTermTag.get("/",2);
  AtomTerm predicateIndicatorAtom = AtomTerm.get("predicate_indicator"); 

  private class CurrentPredicateBacktrackInfo extends BacktrackInfo
  {
    CurrentPredicateBacktrackInfo()
    {
      super(-1,-1);
    }
    int startUndoPosition;
    Iterator tagsIterator;
    Term     pi;
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
      CurrentPredicateBacktrackInfo bi = 
        (CurrentPredicateBacktrackInfo)interpreter.popBacktrackInfo();
      interpreter.undo(bi.startUndoPosition);
      return nextSolution(interpreter, bi);
    }
    else
    {
      Term pi = args[0];
      if (pi instanceof VariableTerm)
      {
      }
      else if (pi instanceof CompoundTerm)
      {
        CompoundTerm ct = (CompoundTerm)pi;
        if (ct.tag != divideTag)
        {
          PrologException.typeError(predicateIndicatorAtom,pi);
        }
        Term n = ct.args[0].dereference();
        Term a = ct.args[1].dereference();
        if (!(n instanceof VariableTerm || n instanceof AtomTerm))
        {
          PrologException.typeError(predicateIndicatorAtom,pi);
        }
        if (!(a instanceof VariableTerm || a instanceof IntegerTerm))
        {
          PrologException.typeError(predicateIndicatorAtom,pi);
        }
      }
      else
      {
        PrologException.typeError(predicateIndicatorAtom,pi);
      }
      HashSet tagSet = new HashSet(interpreter.environment.getModule().getPredicateTags());
      CurrentPredicateBacktrackInfo bi = new CurrentPredicateBacktrackInfo();
      bi.startUndoPosition = interpreter.getUndoPosition();
      bi.pi = pi;
      bi.tagsIterator = tagSet.iterator();
      return nextSolution(interpreter, bi);
    }


  }

  private static int nextSolution(Interpreter interpreter, CurrentPredicateBacktrackInfo bi) throws PrologException
  {
    while (bi.tagsIterator.hasNext())
    {
      CompoundTermTag tag = (CompoundTermTag)bi.tagsIterator.next();
      Predicate p = interpreter.environment.getModule().getDefinedPredicate(tag);
      if (p == null) // if was destroyed 
      {
        continue;
      }
      if (p.getType() != Predicate.USER_DEFINED && p.getType() != Predicate.EXTERNAL) // no buidins
      {
        continue;
      }
      int rc = interpreter.unify(bi.pi, tag.getPredicateIndicator());
      if (rc == SUCCESS_LAST)
      {
        interpreter.pushBacktrackInfo(bi);
        return SUCCESS;
      }
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

