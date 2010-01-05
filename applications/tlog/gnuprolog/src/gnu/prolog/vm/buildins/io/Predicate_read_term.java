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
public class Predicate_read_term implements PrologCode, TermConstants
{
  final static CompoundTermTag variablesTag     = CompoundTermTag.get("variables",1);
  final static CompoundTermTag variableNamesTag = CompoundTermTag.get("variable_names",1);
  final static CompoundTermTag singletonsTag    = CompoundTermTag.get("singletons",1);
  final static CompoundTermTag unifyTag    = CompoundTermTag.get("=",2);

  /** this method is used for execution of code
    * @param interpreter interpreter in which context code is executed 
    * @param backtrackMode true if predicate is called on backtracking and false otherwise
    * @param args arguments of code
    * @return either SUCCESS, SUCCESS_LAST, or FAIL.
    */
  public int execute(Interpreter interpreter, boolean backtrackMode, gnu.prolog.term.Term args[]) 
         throws PrologException
  {
    PrologStream stream = interpreter.environment.resolveStream(args[0]);
    Term optionsList = args[2];
    ReadOptions options = new ReadOptions();
    options.operatorSet = interpreter.environment.getOperatorSet();
    
    ArrayList singletons = new ArrayList();
    ArrayList variableLists = new ArrayList();
    ArrayList vnlists = new ArrayList();
    
    // parse and unify options
    Term cur = optionsList;
    while (cur != emptyListAtom)
    {
      if (cur instanceof VariableTerm)
      {
        PrologException.instantiationError();
      }
      if (!(cur instanceof CompoundTerm))
      {
        PrologException.typeError(listAtom, optionsList);
      }
      CompoundTerm ct = (CompoundTerm)cur;
      if (ct.tag != listTag)
      {
        PrologException.typeError(listAtom, optionsList);
      }
      Term head = ct.args[0].dereference();
      cur = ct.args[1].dereference();
      if (head instanceof VariableTerm)
      {
        PrologException.instantiationError();
      }
      if (!(head instanceof CompoundTerm))
      {
        PrologException.domainError(readOptionAtom, head);
      }
      CompoundTerm op = (CompoundTerm)head;
      if (op.tag == variablesTag)
      {
        variableLists.add(op.args[0]);
      }
      else if (op.tag == singletonsTag)
      {
        singletons.add(op.args[0]);
      }
      else if (op.tag == variableNamesTag)
      {
        vnlists.add(op.args[0]);
      }
      else 
      {
        PrologException.domainError(readOptionAtom, head);
      }
    }
    
    Term readTerm = stream.readTerm(args[0],interpreter,options);
    int undoPos = interpreter.getUndoPosition();
    
    try
    {
      int rc = interpreter.simple_unify(args[1], readTerm);
      if (rc == FAIL)
      {
        interpreter.undo(undoPos);
        return FAIL;
      }
      Iterator i=singletons.iterator();
      if (i.hasNext())
      {
        Term singletonsList = mapToList(options.singletons);
        while (i.hasNext())
        {
          Term t = (Term)i.next();
          t = t.dereference();
          rc = interpreter.simple_unify(t,singletonsList);
          if (rc == FAIL)
          {
            interpreter.undo(undoPos);
            return FAIL;
          }
        }
      }
      i=vnlists.iterator();
      if (i.hasNext())
      {
        Term vnlist = mapToList(options.variableNames);
        while (i.hasNext())
        {
          Term t = (Term)i.next();
          t = t.dereference();
          rc = interpreter.simple_unify(t,vnlist);
          if (rc == FAIL)
          {
            interpreter.undo(undoPos);
            return FAIL;
          }
        }
      }
      i=variableLists.iterator();
      if (i.hasNext())
      {
        Term vnlist = CompoundTerm.getList(options.variables);
        while (i.hasNext())
        {
          Term t = (Term)i.next();
          t = t.dereference();
          rc = interpreter.simple_unify(t,vnlist);
          if (rc == FAIL)
          {
            interpreter.undo(undoPos);
            return FAIL;
          }
        }
      }
      return SUCCESS_LAST;
    }
    catch(PrologException ex)
    {
      interpreter.undo(undoPos);
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

  private static Term mapToList(Map map)
  {
    Iterator i = map.keySet().iterator();
    Term rc = emptyListAtom;
    while (i.hasNext())
    {
      String key = (String)i.next();
      Term val = (Term)map.get(key);
      rc = CompoundTerm.getList(new CompoundTerm(unifyTag,AtomTerm.get(key),val), rc);
    }
    return rc;
  }

  /** this method is called when code is uninstalled from the environment
    * @param environment environemnt to install the predicate
    */
  public void uninstall(Environment env)
  {
  }
    
}

