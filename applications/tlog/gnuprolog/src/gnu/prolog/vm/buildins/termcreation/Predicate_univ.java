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
import java.util.*;

/** prolog code 
  */
public class Predicate_univ implements PrologCode
{
  public final static AtomTerm nonEmptyListAtom = AtomTerm.get("non_empty_list");
  public final static AtomTerm listAtom = AtomTerm.get("list");
  public final static AtomTerm atomAtom = AtomTerm.get("atom");
  public final static AtomTerm atomicAtom = AtomTerm.get("atomic");
  public final static Term termArrayType[] = new Term[0];
  public final static CompoundTermTag listTag = CompoundTermTag.get(".",2);

  /** this method is used for execution of code
    * @param interpreter interpreter in which context code is executed 
    * @param backtrackMode true if predicate is called on backtracking and false otherwise
    * @param args arguments of code
    * @return either SUCCESS, SUCCESS_LAST, or FAIL.
    */
  public int execute(Interpreter interpreter, boolean backtrackMode, gnu.prolog.term.Term args[]) 
         throws PrologException
  {
    int undoPos = interpreter.getUndoPosition();
    Term term = args[0];
    Term list = args[1];
    if (term instanceof AtomicTerm)
    {
      checkList(list, false);
      if (list instanceof VariableTerm)
      {
        VariableTerm lvar = (VariableTerm)list;
        interpreter.addVariableUndo(lvar);
        lvar.value = CompoundTerm.getList(term, AtomTerm.emptyList);
        return SUCCESS_LAST;
      }
      CompoundTerm ct = (CompoundTerm)list;
      Term head = ct.args[0].dereference();
      Term tail = ct.args[1].dereference();
      if (head instanceof CompoundTerm)
      {
        PrologException.typeError(atomicAtom, head); 
      }
      Term t = CompoundTerm.getList(term, AtomTerm.emptyList);
      int rc = interpreter.unify(t, list);
      return rc;
    }
    else if (term instanceof CompoundTerm)
    {
      checkList(list, false);
      CompoundTerm ct = (CompoundTerm)term;
      CompoundTermTag tag = ct.tag;
      AtomTerm functor = tag.functor;
      Term tmp = AtomTerm.emptyList;
      Term targs[] = ct.args;
      for (int i=tag.arity-1; i>=0; i--)
      {
        tmp = CompoundTerm.getList(targs[i].dereference(), tmp);
      }
      tmp = CompoundTerm.getList(functor, tmp);
      int rc = interpreter.unify(tmp, list);
      if (rc == FAIL)
      {
        interpreter.undo(undoPos);
      }
      return rc;
    }
    else if (term instanceof VariableTerm)
    {
      checkList(list, true);
      VariableTerm vt = (VariableTerm)term;
      if (list == AtomTerm.emptyList)
      {
        PrologException.domainError(nonEmptyListAtom, list);
      }
      CompoundTerm ct = (CompoundTerm)list;
      if (ct.tag != listTag)
      {
        PrologException.typeError(listAtom, list);
      }
      Term head = ct.args[0].dereference();
      Term tail = ct.args[1].dereference();
      if (head instanceof VariableTerm)
      {
        PrologException.instantiationError();
      }
      if (tail == AtomTerm.emptyList)
      {
        interpreter.addVariableUndo(vt);
        vt.value = head;
        return SUCCESS_LAST;
      }
      if (!(head instanceof AtomTerm))
      {
        PrologException.typeError(atomAtom,head);
      }
      AtomTerm functor = (AtomTerm)head;
      ArrayList argList = new ArrayList();
      do
      {
        ct = (CompoundTerm)tail;
        head = ct.args[0].dereference();
        tail = ct.args[1].dereference();
        argList.add(head);
      } while ( tail != AtomTerm.emptyList);
      Term targs[] = (Term[])argList.toArray(termArrayType);
      interpreter.addVariableUndo(vt);
      vt.value = new CompoundTerm(functor, targs);
      return SUCCESS_LAST;
    }
    return FAIL;
  }

  private static void checkList(Term list, boolean nonPartial) throws PrologException
  {
    Term exArg = list;
    
    if (list == AtomTerm.emptyList)
    {
      return;
    }
    if (list instanceof VariableTerm)
    {
      if (nonPartial)
      {
        PrologException.instantiationError();
      }
      else
      {
        return;
      }
    }
    if (!(list instanceof CompoundTerm))
    {
      PrologException.typeError(listAtom, exArg);
    }
    CompoundTerm ct = (CompoundTerm)list;
    if (ct.tag != listTag)
    {
      PrologException.typeError(listAtom, exArg);
    }
    Term head = ct.args[0].dereference();
    Term tail = ct.args[1].dereference();
    if (tail == AtomTerm.emptyList)
    {
      if (head instanceof CompoundTerm)
      {
        PrologException.typeError(atomicAtom, head);
      }
      return;
    }
    else
    {
      if (!(head instanceof VariableTerm) && !(head instanceof AtomicTerm))
      {
        PrologException.typeError(atomAtom, head);
      }
    }
    
    list = tail;
    while(true)
    {
      if (list == AtomTerm.emptyList)
      {
        return;
      }
      if (list instanceof VariableTerm)
      {
        if (nonPartial)
        {
          PrologException.instantiationError();
        }
        else
        {
          return;
        }
      }
      if (!(list instanceof CompoundTerm))
      {
        PrologException.typeError(listAtom, exArg);
      }
      ct = (CompoundTerm)list;
      if (ct.tag != listTag)
      {
        PrologException.typeError(listAtom, exArg);
      }
      //Term head = ct.args[1].dereference();
      list = ct.args[1].dereference();
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

