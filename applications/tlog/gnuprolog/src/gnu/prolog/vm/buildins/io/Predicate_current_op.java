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
package gnu.prolog.vm.buildins.io;
import gnu.prolog.vm.*;
import gnu.prolog.term.*;
import gnu.prolog.io.*;
import java.util.*;

/** prolog code 
  */
public class Predicate_current_op implements PrologCode, TermConstants
{

  static final AtomTerm xfxAtom = AtomTerm.get("xfx");
  static final AtomTerm xfyAtom = AtomTerm.get("xfy");
  static final AtomTerm yfxAtom = AtomTerm.get("yfx");
  static final AtomTerm fxAtom  = AtomTerm.get("fx");
  static final AtomTerm fyAtom  = AtomTerm.get("fy");
  static final AtomTerm xfAtom  = AtomTerm.get("xf");
  static final AtomTerm yfAtom  = AtomTerm.get("yf");


  private class CurrentOpBacktrackInfo extends BacktrackInfo
  {
    CurrentOpBacktrackInfo() {super(-1,-1);}

    int startUndoPosition;

    Iterator ops;
    Iterator specifiers;
    Iterator priorities;
    
    Term op;
    Term specifier;
    Term priority;
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
      CurrentOpBacktrackInfo bi = (CurrentOpBacktrackInfo)interpreter.popBacktrackInfo();
      interpreter.undo(bi.startUndoPosition);
      return nextSolution(interpreter,bi);
    }
    else
    {
      Term op        = args[2];
      Term specifier = args[1];
      Term priority  = args[0];
      // validate args
      if (!(op instanceof AtomTerm || op instanceof VariableTerm))
      {
        PrologException.typeError(atomAtom, op);
      }
      if (!(specifier instanceof VariableTerm || specifier == xfxAtom ||
            specifier == xfyAtom || specifier == yfxAtom || 
            specifier == fxAtom  || specifier == fyAtom  ||
            specifier == xfAtom  || specifier == yfAtom))
      {
        PrologException.domainError(operatorSpecifierAtom, specifier);
      }
      if (priority instanceof VariableTerm)
      {
      }
      else if (priority instanceof IntegerTerm)
      {
        IntegerTerm tt = (IntegerTerm)priority;
        if (tt.value <= 0 || 1200 < tt.value)
        {
          PrologException.domainError(operatorPriorityAtom, priority);
        }
      }
      else 
      {
        PrologException.domainError(operatorPriorityAtom, priority);
      }

      // prepare and exec
      ArrayList ops = new ArrayList();
      ArrayList specifiers = new ArrayList();
      ArrayList priorities = new ArrayList();
      
      Iterator i = interpreter.environment.getOperatorSet().getOperators().iterator();
      while (i.hasNext())
      {
        Operator o = (Operator)i.next();
        ops.add(o.tag.functor);
        priorities.add(IntegerTerm.get(o.priority));
        AtomTerm a = null;
        switch (o.specifier)
        {
        case Operator. FX: a =  fxAtom;break;
        case Operator. FY: a =  fyAtom;break;
        case Operator.XF : a = xfAtom;break;
        case Operator.YF : a = yfAtom;break;
        case Operator.XFX: a = xfxAtom;break;
        case Operator.XFY: a = xfyAtom;break;
        case Operator.YFX: a = yfxAtom;break;
        }
        specifiers.add(a);
      }
      CurrentOpBacktrackInfo bi = new CurrentOpBacktrackInfo();
      bi.startUndoPosition = interpreter.getUndoPosition();
      bi.ops        = ops.iterator();
      bi.specifiers = specifiers.iterator();
      bi.priorities = priorities.iterator();
      bi.op        = op       ;
      bi.specifier = specifier;
      bi.priority  = priority ;
      return nextSolution(interpreter,bi);
    }
  }
  
  private static int nextSolution(Interpreter interpreter, CurrentOpBacktrackInfo bi) throws PrologException
  {
    try
    {
      while (bi.ops.hasNext())
      {
        Term op        = ((Term)bi.ops.next());       
        Term specifier = ((Term)bi.specifiers.next());
        Term priority  = ((Term)bi.priorities.next());
        if ( interpreter.simple_unify(op       ,bi.op       ) == SUCCESS_LAST && 
             interpreter.simple_unify(specifier,bi.specifier) == SUCCESS_LAST &&
             interpreter.simple_unify(priority ,bi.priority ) == SUCCESS_LAST)
        {
          interpreter.pushBacktrackInfo(bi);
          return SUCCESS;
        }
        interpreter.undo(bi.startUndoPosition);
      }
      return FAIL;
    }
    catch(PrologException ex)
    {
      interpreter.undo(bi.startUndoPosition);
      throw ex;
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

