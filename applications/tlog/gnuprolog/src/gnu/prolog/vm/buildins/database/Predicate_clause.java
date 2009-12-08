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
import java.util.ArrayList;
import java.util.Iterator;

/** prolog code 
  */
public class Predicate_clause implements PrologCode
{

  static final AtomTerm accessAtom = AtomTerm.get("access");
  static final AtomTerm callableAtom = AtomTerm.get("callable");
  static final AtomTerm privateProcedureAtom = AtomTerm.get("private_procedure");
  static final CompoundTermTag clauseTag = CompoundTermTag.get(":-",2);
  static final CompoundTermTag conjunctionTag = CompoundTermTag.get(",",2);
  static final CompoundTermTag disjunctionTag = CompoundTermTag.get(";",2);
  static final CompoundTermTag ifTag = CompoundTermTag.get("->",2);

  private class ClauseBacktrackInfo extends BacktrackInfo
  {
    ClauseBacktrackInfo()
    {
      super(-1,-1);
    }
    ArrayList clauses;
    int  position;
    int  startUndoPosition;
    Term clause;

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
      ClauseBacktrackInfo bi = (ClauseBacktrackInfo)interpreter.popBacktrackInfo();
      interpreter.undo(bi.startUndoPosition);
      return nextSolution(interpreter, bi);
    }
    else
    {
      Term head = args[0];
      Term body = args[1];
      if (head instanceof VariableTerm)
      {
        PrologException.instantiationError();
      }
      CompoundTermTag tag = null;
      if (head instanceof AtomTerm)
      {
        tag = CompoundTermTag.get((AtomTerm)head, 0);
      }
      else if (head instanceof CompoundTerm)
      {
        tag = ((CompoundTerm)head).tag;
      }
      else
      {
        PrologException.typeError(callableAtom, head);
      }
      Predicate p = interpreter.environment.getModule().getDefinedPredicate(tag);
      if (p == null) // if predicate not found
      {
        return FAIL;
      }
      //System.err.println("p type = "+p.getType());
      //System.err.println("p dyn = "+p.isDynamic());
      if (p.getType() != Predicate.USER_DEFINED || !p.isDynamic())
      {
        PrologException.permissionError(accessAtom, privateProcedureAtom, tag.getPredicateIndicator());
      }
      if (!isCallable(body))
      {
        PrologException.typeError(callableAtom, body);
      }
      ArrayList clauses = new ArrayList();
      for(Iterator ic = p.getClauses().iterator();ic.hasNext();)
      {
        clauses.add((Term)((Term)ic.next()).clone());
      }
      if (clauses.size() == 0)
      {
        return FAIL;
      }
      else 
      {
        ClauseBacktrackInfo bi = new ClauseBacktrackInfo();
        bi.startUndoPosition = interpreter.getUndoPosition();
        bi.position = 0;
        bi.clauses = clauses;
        bi.clause = new CompoundTerm(clauseTag, head, body);
        return nextSolution(interpreter, bi);
      }

    }
  }

  private int nextSolution(Interpreter interpreter, ClauseBacktrackInfo bi) throws PrologException
  {
    while(bi.position < bi.clauses.size())
    {
      int rc = interpreter.unify((Term)bi.clauses.get(bi.position++),bi.clause);
      if (rc == SUCCESS_LAST)
      {
        interpreter.pushBacktrackInfo(bi);
        return SUCCESS;
      }
    }
    return FAIL;
  }

  public static boolean isCallable(Term body)
  {
    if (body instanceof VariableTerm)
    {
      return true;
    }
    else if (body instanceof CompoundTerm)
    {
      CompoundTerm ct = (CompoundTerm)body;
      if (ct.tag == conjunctionTag ||
          ct.tag == disjunctionTag ||
          ct.tag == ifTag)
      {
        return isCallable(ct.args[0].dereference()) && 
               isCallable(ct.args[1].dereference());
      }
      return true;
    }
    else if (body instanceof AtomTerm)
    {
      return true;
    }
    return false;
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

