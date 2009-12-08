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
package gnu.prolog.vm.buildins.allsolutions;
import gnu.prolog.term.*;
import gnu.prolog.vm.*;
import java.util.*;

/** prolog code 
  */
public class Predicate_bagof implements PrologCode
{
  static final CompoundTermTag plusTag = CompoundTermTag.get("+",2);

  private class BagOfBacktrackInfo extends BacktrackInfo
  {
    BagOfBacktrackInfo(){super(-1,-1);}
    int startUndoPosition;
    ArrayList solutionList;
    Term witness;
    Term instances;
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
      BagOfBacktrackInfo bi = (BagOfBacktrackInfo)interpreter.popBacktrackInfo();
      interpreter.undo(bi.startUndoPosition);
      return nextSolution(interpreter, bi);
    }
    else
    {
      Term ptemplate = args[0];
      Term pgoal = args[1];
      Term pinstances = args[2];
      Predicate_findall.checkList(pinstances);
      HashSet wset = new HashSet();
      Term findallGoal = TermUtils.getFreeVariableSet(pgoal,ptemplate,wset);
      Term witness = TermUtils.getWitness(wset);
      CompoundTerm findallTemplate = new CompoundTerm(plusTag,witness,ptemplate);
      ArrayList list = new ArrayList();
      int rc = Predicate_findall.findall(interpreter, false, findallTemplate, findallGoal, list);
      if (rc == FAIL || list.size()==0)
      {
        return FAIL;
      }
      BagOfBacktrackInfo bi = new BagOfBacktrackInfo();
      bi.startUndoPosition = interpreter.getUndoPosition();
      bi.solutionList = list;
      bi.witness = witness;
      bi.instances = pinstances;
      return nextSolution(interpreter, bi);
    }

  }


  public int nextSolution(Interpreter interpreter, BagOfBacktrackInfo bi)
         throws PrologException
  {
    ArrayList curTList = new ArrayList();
    int undoPos = interpreter.getUndoPosition();
    while (bi.solutionList.size() != 0)
    {
      CompoundTerm curInstance = (CompoundTerm)((Term)bi.solutionList.remove(0)).dereference();
      Term curWitness = curInstance.args[0].dereference();
      int rc = interpreter.simple_unify(bi.witness, curWitness);
      if (rc == FAIL)
      {
        throw new IllegalStateException("unexpected unify fail");
      }
      curTList.add(curInstance.args[1].dereference());
      ListIterator isol = bi.solutionList.listIterator();
      while (isol.hasNext())
      {
        CompoundTerm ct = (CompoundTerm)isol.next();
        Term w = ct.args[0].dereference();
        if (TermUtils.isVariant(curWitness,w))
        {
          rc = interpreter.simple_unify(bi.witness, w);
          if (rc == FAIL)
          {
            throw new IllegalStateException("unexpected unify fail");
          }
          curTList.add(ct.args[1].dereference());
          isol.remove();
        }
      }
      processList(curTList);
      rc = interpreter.unify(CompoundTerm.getList(curTList),
                             bi.instances.dereference());
      if (rc == SUCCESS_LAST)
      {
        if (bi.solutionList.size() != 0)
        {
          interpreter.pushBacktrackInfo(bi);
          return SUCCESS;
        }
        else
        {
          return SUCCESS_LAST;
        }
      }
      interpreter.undo(undoPos);
      curTList.clear();
    }
    return FAIL;
  }

  protected void processList(ArrayList curTList)
  {
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


