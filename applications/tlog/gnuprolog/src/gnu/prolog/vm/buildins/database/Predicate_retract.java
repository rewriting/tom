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
import java.util.*;

/** prolog code 
  */
public class Predicate_retract implements PrologCode
{
  
  static final CompoundTermTag clauseTag      = CompoundTermTag.get(":-",2); 
  static final AtomTerm trueAtom = AtomTerm.get("true"); 
  static final AtomTerm modifyAtom = AtomTerm.get("modify"); 
  static final AtomTerm staticProcedureAtom = AtomTerm.get("static_procedure"); 
  static final AtomTerm callableAtom = AtomTerm.get("callable"); 

  private class RetractBacktrackInfo extends BacktrackInfo
  {
    RetractBacktrackInfo()
    {
      super(-1,-1);
    }
    Iterator iclauses;
    HashMap clauseMap;
    int startUndoPosition;
    Term clause;
    Predicate pred;
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
      RetractBacktrackInfo bi = (RetractBacktrackInfo)interpreter.popBacktrackInfo();
      interpreter.undo(bi.startUndoPosition);
      return nextSolution(interpreter,bi);
    }
    else
    {
      Term clause = args[0];
      Term head = null;
      Term body = null;
      if (clause instanceof VariableTerm)
      {
        PrologException.instantiationError();
      }
      else if (clause instanceof CompoundTerm)
      {
        CompoundTerm ct = (CompoundTerm)clause;
        if (ct.tag == clauseTag )
        {
          head = ct.args[0].dereference();
          body = ct.args[1].dereference();
        }
        else
        {
          head = ct;
          body = trueAtom;
        }
      }
      else if (clause instanceof AtomTerm)
      {
        head = clause;
        body = trueAtom;
      }
      else 
      {
        PrologException.typeError(callableAtom,clause);
      }
      CompoundTermTag predTag = null;
      if (head instanceof VariableTerm)
      {
        PrologException.instantiationError();
      }
      else if (head instanceof CompoundTerm)
      {
        predTag = ((CompoundTerm)head).tag;
      }
      else if (head instanceof AtomTerm)
      {
        predTag = CompoundTermTag.get((AtomTerm)head,0);
      }
      else
      {
        PrologException.typeError(callableAtom,head);
      }
      Predicate p = interpreter.environment.getModule().getDefinedPredicate(predTag);
      if (p == null)
      {
        return FAIL;
      }
      else if (p.getType() == Predicate.USER_DEFINED)
      {
        if (!p.isDynamic())
        {
          PrologException.permissionError(modifyAtom,staticProcedureAtom,predTag.getPredicateIndicator());
        }
      }
      else
      {
        PrologException.permissionError(modifyAtom,staticProcedureAtom,predTag.getPredicateIndicator());
      }
      HashMap map = new HashMap();
      ArrayList list = new ArrayList(p.getClauses().size());
      for (Iterator ic = p.getClauses().iterator();ic.hasNext();)
      {
        Term cl = (Term)ic.next();
        Term cp = (Term)cl.clone();
        map.put(cp, cl);
        list.add(cp);
      }
      RetractBacktrackInfo bi = new RetractBacktrackInfo();
      bi.iclauses = list.iterator();
      bi.clauseMap = map;
      bi.startUndoPosition = interpreter.getUndoPosition();
      bi.clause = new CompoundTerm(clauseTag, head, body);
      bi.pred = p;
      return nextSolution(interpreter,bi);
    }
  }

  private static int nextSolution(Interpreter interpreter, RetractBacktrackInfo bi) throws PrologException
  {
    while (bi.iclauses.hasNext())
    {
      Term term = (Term)bi.iclauses.next();
      int rc = interpreter.unify(bi.clause,term);
      if (rc == SUCCESS_LAST)
      {
        bi.pred.removeClause((Term)bi.clauseMap.get(term));
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

