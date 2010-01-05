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

/** prolog code 
  */
public abstract class Predicate_assert implements PrologCode
{
  static final CompoundTermTag clauseTag      = CompoundTermTag.get(":-",2); 
  static final CompoundTermTag conjunctionTag = CompoundTermTag.get(",",2); 
  static final CompoundTermTag disjunctionTag = CompoundTermTag.get(";",2); 
  static final CompoundTermTag ifTag          = CompoundTermTag.get("->",2); 
  static final CompoundTermTag callTag        = CompoundTermTag.get("call",1); 
  static final AtomTerm trueAtom = AtomTerm.get("true"); 
  static final AtomTerm modifyAtom = AtomTerm.get("modify"); 
  static final AtomTerm staticProcedureAtom = AtomTerm.get("static_procedure"); 
  static final AtomTerm callableAtom = AtomTerm.get("callable"); 

  /** assert a clause */
  protected abstract void assertt(Predicate p, CompoundTerm clause);

  /** this method is used for execution of code
    * @param interpreter interpreter in which context code is executed 
    * @param backtrackMode true if predicate is called on backtracking and false otherwise
    * @param args arguments of code
    * @return either SUCCESS, SUCCESS_LAST, or FAIL.
    */
  public int execute(Interpreter interpreter, boolean backtrackMode, gnu.prolog.term.Term args[]) 
         throws PrologException
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
        body = prepareBody(body,body);
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
      p = interpreter.environment.getModule().createDefinedPredicate(predTag);
      p.setType(Predicate.USER_DEFINED); 
      p.setDynamic(); 
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
    assertt(p, (CompoundTerm)new CompoundTerm(clauseTag,head,body).clone());
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
    

  public static Term prepareBody(Term body, Term term) throws PrologException
  {
    if (body instanceof VariableTerm)
    {
      return new CompoundTerm(callTag,body);
    }
    else if (body instanceof AtomTerm)
    {
      return body;
    }
    else if (body instanceof CompoundTerm)
    {
      CompoundTerm ct = (CompoundTerm)body;
      if (ct.tag == conjunctionTag ||
          ct.tag == disjunctionTag ||
          ct.tag == ifTag)
      {
        return new CompoundTerm(ct.tag, 
                                prepareBody(ct.args[0].dereference(),term),
                                prepareBody(ct.args[1].dereference(),term));
      }
      return body;
    }
    else
    {
      PrologException.typeError(callableAtom,term);
      return null;
    }
  }

}

