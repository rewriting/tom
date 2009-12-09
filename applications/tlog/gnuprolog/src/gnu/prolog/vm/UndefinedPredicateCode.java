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
package gnu.prolog.vm;
import gnu.prolog.term.*;

/** prolog code 
  */
public class UndefinedPredicateCode implements PrologCode
{
  /** predicate indicator for thuis precedure */
  CompoundTerm predicateIndicator;
  /** predicate tag */
  CompoundTermTag predicateTag;

  public final static AtomTerm unknownAtom = AtomTerm.get("unknown");
  public final static AtomTerm errorAtom = AtomTerm.get("error");
  public final static AtomTerm failAtom = AtomTerm.get("fail");
  public final static AtomTerm warningAtom = AtomTerm.get("warning");
  public final static AtomTerm procedureAtom = AtomTerm.get("procedure");
  
  /** construct new instance of undefiend predicate */
  public UndefinedPredicateCode(CompoundTermTag predicateTag)
  {
    this.predicateTag = predicateTag;
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
    Term flg = interpreter.getEnvironment().getPrologFlag(unknownAtom);
    if (flg == errorAtom)
    {
      if (predicateIndicator == null)
      {
        predicateIndicator = predicateTag.getPredicateIndicator();
      }
      PrologException.existenceError(procedureAtom, predicateIndicator);
    }
    else if (flg == failAtom)
    {
      return FAIL;
    }
    else if (flg == warningAtom)
    {
      // later should be replaced by output to 'user'
      System.err.println("predicate "+predicateTag.functor.value+"/"+predicateTag.arity+" does not exists.");
      return FAIL;
    }
    throw new PrologException(PrologException.systemErrorAtom);
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

