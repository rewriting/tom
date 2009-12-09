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

/** prolog code 
  */
public class Predicate_close implements PrologCode, TermConstants
{

  CompoundTermTag forceTag = CompoundTermTag.get("force",1);
  /** this method is used for execution of code
    * @param interpreter interpreter in which context code is executed 
    * @param backtrackMode true if predicate is called on backtracking and false otherwise
    * @param args arguments of code
    * @return either SUCCESS, SUCCESS_LAST, or FAIL.
    */
  public int execute(Interpreter interpreter, boolean backtrackMode, gnu.prolog.term.Term args[]) 
         throws PrologException
  {
    Term cur = args[1];
    Term force = falseAtom;
    while (cur != emptyListAtom)
    {
      if (cur instanceof VariableTerm)
      {
        PrologException.instantiationError();
      }
      if (!(cur instanceof CompoundTerm))
      {
        PrologException.typeError(listAtom, args[1]);
      }
      CompoundTerm ct = (CompoundTerm)cur;
      if (ct.tag != listTag)
      {
        PrologException.typeError(listAtom, args[1]);
      }
      Term head = ct.args[0].dereference();
      cur = ct.args[1].dereference();
      
      if (head instanceof VariableTerm)
      {
        PrologException.instantiationError();
      }
      if (!(head instanceof CompoundTerm))
      {
        PrologException.domainError(closeOptionAtom, head);
      }
      CompoundTerm e = (CompoundTerm)head;
      if (e.tag != forceTag || (e.args[0] != trueAtom && e.args[0] != falseAtom))
      {
        PrologException.domainError(closeOptionAtom, head);
      }
      force = e.args[0];
    }
    
    PrologStream stream = interpreter.environment.resolveStream(args[0]);
    if (stream == interpreter.environment.getUserInput())
    {
      return SUCCESS_LAST;
    }
    if (stream == interpreter.environment.getUserOutput())
    {
      return SUCCESS_LAST;
    }
    stream.close(force == trueAtom);
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

