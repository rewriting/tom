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
import gnu.prolog.term.*;
import gnu.prolog.vm.*;
import gnu.prolog.io.*;
import java.util.*;

/** prolog code 
  */
public class Predicate_op implements PrologCode, TermConstants
{
  static final AtomTerm xfxAtom = AtomTerm.get("xfx");
  static final AtomTerm xfyAtom = AtomTerm.get("xfy");
  static final AtomTerm yfxAtom = AtomTerm.get("yfx");
  static final AtomTerm fxAtom  = AtomTerm.get("fx");
  static final AtomTerm fyAtom  = AtomTerm.get("fy");
  static final AtomTerm xfAtom  = AtomTerm.get("xf");
  static final AtomTerm yfAtom  = AtomTerm.get("yf");
  static final AtomTerm commaAtom  = AtomTerm.get(",");

  /** this method is used for execution of code
    * @param interpreter interpreter in which context code is executed 
    * @param backtrackMode true if predicate is called on backtracking and false otherwise
    * @param args arguments of code
    * @return either SUCCESS, SUCCESS_LAST, or FAIL.
    */
  public int execute(Interpreter interpreter, boolean backtrackMode, gnu.prolog.term.Term args[]) 
         throws PrologException
  {
    Term tpriority = args[0];
    Term topspec = args[1];
    Term tops = args[2];

    int priority=0; // parsed priority
    int opspec=-1;  // parsed operator specifier
    HashSet ops = new HashSet(); // set of operators
    boolean fx = false;
    boolean xf = false;
    boolean xfx = false;
    OperatorSet opSet = interpreter.environment.getOperatorSet();

    // parse arguments
    // priority
    if (tpriority instanceof VariableTerm)
    {
      PrologException.instantiationError();
    }
    if (!(tpriority instanceof IntegerTerm))
    {
      PrologException.typeError(integerAtom, tpriority);
    }
    priority = ((IntegerTerm)tpriority).value;
    if (priority < 0 || 1200 < priority)
    {
      PrologException.domainError(operatorPriorityAtom, tpriority);
    }
    // specifier
    if (topspec instanceof VariableTerm)
    {
      PrologException.instantiationError();
    }
    if (!(topspec instanceof AtomTerm))
    {
      PrologException.typeError(atomAtom, topspec);
    }

    if (topspec == xfxAtom)
    {
      opspec = Operator.XFX;
      xfx = true;
    }
    else if (topspec == xfyAtom)
    {
      opspec = Operator.XFY;
      xfx = true;
    }
    else if (topspec == yfxAtom)
    {
      opspec = Operator.YFX;
      xfx = true;
    }
    else if (topspec == fxAtom)
    {
      opspec = Operator.FX;
      fx = true;
    }
    else if (topspec == fyAtom)
    {
      opspec = Operator.FY;
      fx = true;
    }
    else if (topspec == xfAtom)
    {
      opspec = Operator.XF;
      xf = true;
    }
    else if (topspec == yfAtom)
    {
      opspec = Operator.YF;
      xf = true;
    }
    else
    {
      PrologException.domainError(operatorSpecifierAtom, topspec);
    }
    // parse ops
    if (tops == emptyListAtom)
    {
      // do nothing
    }
    else if (tops instanceof AtomTerm)
    {
      validateOp(priority, opspec, (AtomTerm)tops, opSet);
      ops.add(tops);
    }
    else if (tops instanceof CompoundTerm)
    {
      Term cur = tops;
      while (cur != emptyListAtom)
      {
        if (cur instanceof VariableTerm)
        {
          PrologException.instantiationError();
        }
        if (!(cur instanceof CompoundTerm))
        {
          PrologException.typeError(listAtom, tops);
        }
        CompoundTerm ct = (CompoundTerm)cur;
        if (ct.tag != listTag)
        {
          PrologException.typeError(listAtom, tops);
        }
        Term head = ct.args[0].dereference();
        cur = ct.args[1].dereference();
        if (head instanceof VariableTerm)
        {
          PrologException.instantiationError();
        }
        if (!(head instanceof AtomTerm))
        {
          PrologException.typeError(atomAtom, head);
        }
        validateOp(priority, opspec, (AtomTerm)head, opSet);
        ops.add(head);
      }
    }
    else
    {
      PrologException.typeError(listAtom, tops);
    }
    if (priority == 0) // if remove requested
    {
      Iterator i = ops.iterator();
      while (i.hasNext())
      {
        AtomTerm op = (AtomTerm)i.next();
        opSet.remove(opspec, op.value);
      }
    }
    else
    {
      Iterator i = ops.iterator();
      while (i.hasNext())
      {
        AtomTerm op = (AtomTerm)i.next();
        opSet.add(priority, opspec, op.value);
      }
    }
    return SUCCESS_LAST;
  }

  private static void validateOp(int priority, int specifier, AtomTerm opAtom, OperatorSet opSet) throws PrologException
  {
    if (opAtom == commaAtom)
    {
      PrologException.permissionError(modifyAtom,operatorAtom,opAtom);
    }
    switch (specifier)
    {
    case Operator. FX:
    case Operator. FY:
      break;
    case Operator.XF :
    case Operator.YF :
      {
        Operator op = opSet.lookupXf(opAtom.value);
        if (op.specifier != Operator.YF && specifier != Operator.XF)
        {
          PrologException.permissionError(createAtom,operatorAtom,opAtom);
        }
        break;
      }
    case Operator.XFX:
    case Operator.XFY:
    case Operator.YFX:
      {
        Operator op = opSet.lookupXf(opAtom.value);
        if (op.specifier == Operator.YF || specifier == Operator.XF)
        {
          PrologException.permissionError(createAtom,operatorAtom,opAtom);
        }
        break;
      }
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

