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
package gnu.prolog.vm.buildins.arithmetics;
import gnu.prolog.term.*;
import gnu.prolog.vm.*;

/** prolog code 
  */
public class Predicate_greater_than implements PrologCode
{

  /** this method is used for execution of code
    * @param interpreter interpreter in which context code is executed 
    * @param backtrackMode true if predicate is called on backtracking and false otherwise
    * @param args arguments of code
    * @return either SUCCESS, SUCCESS_LAST, or FAIL.
    */
  public int execute(Interpreter interpreter, boolean backtrackMode, gnu.prolog.term.Term args[]) 
         throws PrologException
  {
    Term arg0 = Evaluate.evaluate(args[0]);
    Term arg1 = Evaluate.evaluate(args[1]);
    if (arg0 instanceof IntegerTerm && arg1 instanceof IntegerTerm)
    {
      IntegerTerm i0 = (IntegerTerm)arg0;
      IntegerTerm i1 = (IntegerTerm)arg1;
      return i0.value>i1.value? SUCCESS_LAST : FAIL;
    }
    else if (arg0 instanceof FloatTerm && arg1 instanceof IntegerTerm)
    {
      FloatTerm f0 = (FloatTerm)arg0;
      IntegerTerm i1 = (IntegerTerm)arg1;
      return f0.value>i1.value? SUCCESS_LAST : FAIL;
    }
    else if (arg0 instanceof IntegerTerm && arg1 instanceof FloatTerm)
    {
      IntegerTerm i0 = (IntegerTerm)arg0;
      FloatTerm f1 = (FloatTerm)arg1;
      return i0.value>f1.value? SUCCESS_LAST : FAIL;
    }
    else if (arg0 instanceof FloatTerm && arg1 instanceof FloatTerm)
    {
      FloatTerm f0 = (FloatTerm)arg0;
      FloatTerm f1 = (FloatTerm)arg1;
      return f0.value>f1.value? SUCCESS_LAST : FAIL;
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

