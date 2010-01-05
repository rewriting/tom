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
package gnu.prolog.vm.buildins.termcreation;
import gnu.prolog.term.*;
import gnu.prolog.vm.*;

/** prolog code 
  */
public class Predicate_arg implements PrologCode
{

  public static final AtomTerm compoundAtom        = AtomTerm.get("compound");
  public static final AtomTerm integerAtom         = AtomTerm.get("integer");
  public static final AtomTerm notLessThenZeroAtom = AtomTerm.get("not_less_then_zero");


  /** this method is used for execution of code
    * @param interpreter interpreter in which context code is executed 
    * @param backtrackMode true if predicate is called on backtracking and false otherwise
    * @param args arguments of code
    * @return either SUCCESS, SUCCESS_LAST, or FAIL.
    */
  public int execute(Interpreter interpreter, boolean backtrackMode, gnu.prolog.term.Term args[]) 
         throws PrologException
  {
    Term n = args[0];
    Term term = args[1];
    Term arg = args[2];
    if (n instanceof VariableTerm)
    {
      PrologException.instantiationError();
    }
    if (term instanceof VariableTerm)
    {
      PrologException.instantiationError();
    }
    if (!(n instanceof IntegerTerm))
    {
      PrologException.typeError(integerAtom, n);
    }
    IntegerTerm in = (IntegerTerm)n;
    if (in.value < 0)
    {
      PrologException.domainError(notLessThenZeroAtom, in);
    }
    if (!(term instanceof CompoundTerm))
    {
      PrologException.typeError(compoundAtom, term);
    }
    CompoundTerm ct = (CompoundTerm)term;
    if (ct.tag.arity < in.value)
    {
      return FAIL;
    }
    if (in.value == 0)
    {
      return FAIL;
    }
    int undoPos = interpreter.getUndoPosition();
    int rc = interpreter.unify(ct.args[in.value-1],arg);
    if (rc == FAIL)
    {
      interpreter.undo(undoPos);
      return FAIL;
    }
    return SUCCESS_LAST;
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

