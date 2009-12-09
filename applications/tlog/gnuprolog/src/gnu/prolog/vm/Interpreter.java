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
import java.util.*;
import gnu.prolog.term.*;
/** This class represent interpreter, it should be used only from one thread
  * If you need to use interpreter from two threads, create new interpreter from 
  * Environment
  */

public final class Interpreter
{
  /** environemt for this interpreter */
  public Environment environment;

  /** this constructor should not be used by client programs 
    */
  Interpreter(Environment environment)
  {
    this.environment = environment;
  }

  /** get environment */
  Environment getEnvironment()
  {
    return environment;
  }

  // backtrack menthods
  ///** stack that contains backtrack info */
  //ArrayList backtrackInfoStack = new ArrayList(4096,4096);
  //int backtrackInfoStackSize = 0;
  
  ///** push backtrack information */
  //public void pushBacktrackInfo(BacktrackInfo bi)
  //{
  //  backtrackInfoStack.add(bi);
  //}

  ///** pop backtrack information */
  //public BacktrackInfo popBacktrackInfo()
  //{
  //  return (BacktrackInfo)backtrackInfoStack.remove(backtrackInfoStack.size()-1);
  //}
  
  //public void popBacktrackInfoUntil(BacktrackInfo cutPoint)
  //{
  //  int pos = backtrackInfoStack.lastIndexOf(cutPoint);
  //  if (pos >= 0)
  //  {
  //    backtrackInfoStack.removeRange(pos, backtrackInfoStack.size()-1);
  //  }
  //  else
  //  {
  //    System.err.println("pop until failed ");
  //  }
  //}

  ///** peek top backtrack information */
  //public BacktrackInfo peekBacktrackInfo()
  //{
  //  return (BacktrackInfo)backtrackInfoStack.get(backtrackInfoStack.size()-1);
  //}

  BacktrackInfo backtrackInfoStack[] = new BacktrackInfo[4096];
  int backtrackInfoAmount = 0;
  int backtrackInfoMax = 4096;
  int backtrackInfoGrow = 4096;

  /** push backtrack information */
  public void pushBacktrackInfo(BacktrackInfo bi)
  {
    if (backtrackInfoAmount == backtrackInfoMax)
    {
      BacktrackInfo tmp[] = new BacktrackInfo[backtrackInfoMax+backtrackInfoGrow];
      System.arraycopy(backtrackInfoStack,0,tmp,0,backtrackInfoAmount);
      backtrackInfoStack = tmp;
      backtrackInfoMax += backtrackInfoGrow;
    }
    backtrackInfoStack[backtrackInfoAmount++] = bi;
  }

  /** pop backtrack information */
  public BacktrackInfo popBacktrackInfo()
  {
    BacktrackInfo rc = backtrackInfoStack[--backtrackInfoAmount];
    backtrackInfoStack[backtrackInfoAmount]=null;
    return rc;
  }
  
  public void popBacktrackInfoUntil(BacktrackInfo cutPoint)
  {
    int pos = backtrackInfoAmount-1;
    while (pos >= 0 && cutPoint != backtrackInfoStack[pos])
    {
      pos--;
    }
    if (pos < 0)
    {
      throw new IllegalArgumentException("cutPoint not found");
    }
    for (int i=pos+1;i<backtrackInfoAmount;i++)
    {
      backtrackInfoStack[i]=null;
    }
    backtrackInfoAmount = pos+1;
  }
  /** peek top backtrack information */
  public BacktrackInfo peekBacktrackInfo()
  {
    return backtrackInfoStack[backtrackInfoAmount-1];
  }


  // Undo Stack methods
  /** get current undo position */
  public int  getUndoPosition()
  {
    undoPositionAsked = true;
    return undoData_amount;
  }


  /** undo changes until this position */
  public void undo(int position)
  {
    //System.err.println("undo "+position+" current="+undoData_amount);
    for(int i =undoData_amount-1; i>=position; i--)
    {
      undoData[i].undo();  
      undoData[i] = null;  
    }
    undoData_amount = position; 
    undoPositionAsked = true;
  }

  /** add variable undo */
  public void addVariableUndo(VariableTerm variable)
  {
    if(undoPositionAsked || !(undoData[undoData_amount-1] instanceof VariableUndoData))
    {
      addSpecialUndo(new VariableUndoData());
    }
    if(variables_amount == variables.length)
    {
      VariableTerm tmp[] = new VariableTerm[growSize+variables.length];
      System.arraycopy(variables,0,tmp,0,variables_amount);
      variables = tmp;
    }
    variables[variables_amount]=variable;
    variables_amount++;
  }

  /** add special undo */ 
  public void addSpecialUndo(UndoData undoDatum)
  {
    if(undoData_amount == undoData.length)
    {
      UndoData tmp[] = new UndoData[growSize+undoData.length];
      System.arraycopy(undoData,0,tmp,0,undoData_amount);
      undoData = tmp;
    }
    undoData[undoData_amount]=undoDatum;
    undoData_amount++;
  }

  
  // final static int maxStackSize = 0x1000000; // is not used
  final static int pageSize = 0x4096;
  final static int growSize = 0x4096;

  VariableTerm variables[] = new VariableTerm[4096];
  int      variables_amount = 0;

  UndoData undoData[] = new UndoData[4096];
  int      undoData_amount = 0;
  boolean  undoPositionAsked = true;
 
  private class VariableUndoData implements UndoData// maybe later this class will be pooled
  {
    int startPosion;
    VariableUndoData()
    {
      startPosion = variables_amount;
    }

    public void undo()
    {
      for(int i = variables_amount-1; i >= startPosion; i--)
      {
        variables[i].value = null;
        variables[i] = null;
      }
      variables_amount = startPosion;
    }
  }
  /** unify two terms, no undo done */
  public int simple_unify(Term t1, Term t2) throws PrologException
  {
    int rc = PrologCode.SUCCESS_LAST;
    if (t1 == t2)
    {
    }
    else if (t1 instanceof VariableTerm)
    {
      VariableTerm vt1 = (VariableTerm)t1;
      addVariableUndo(vt1);
      vt1.value = t2;
    }
    else if (t2 instanceof VariableTerm)
    {
      VariableTerm vt2 = (VariableTerm)t2;
      addVariableUndo(vt2);
      vt2.value = t1;
    }
    else if (t1.getClass() !=  t2.getClass())
    {
      rc = PrologCode.FAIL;
    }
    else if (t1 instanceof CompoundTerm/* && t2 instanceof CompoundTerm*/)
    {
      CompoundTerm ct1 = (CompoundTerm)t1;
      CompoundTerm ct2 = (CompoundTerm)t2;
      if (ct1.tag != ct2.tag)
      {
        rc = PrologCode.FAIL;
      }
      else
      {
        Term args1[] = ct1.args;
        Term args2[] = ct2.args;
        //System.err.println("unify "+ct2.tag+" al1 = "+args1.length+" al2 = "+args2.length);
        unify_loop: for (int i=args2.length-1; i>=0; i--)
        {
          rc = simple_unify(args1[i].dereference(), args2[i].dereference());
          if (rc == PrologCode.FAIL)
          {
            break unify_loop; 
          }
        }
      }
    }
    else if (t1 instanceof FloatTerm/* && t2 instanceof FloatTerm*/)
    {
      FloatTerm ct1 = (FloatTerm)t1;
      FloatTerm ct2 = (FloatTerm)t2;
      if (ct1.value != ct2.value)
      {
        rc = PrologCode.FAIL;
      }
    }
    else if (t1 instanceof IntegerTerm /*&& t2 instanceof IntegerTerm*/)
    {
      IntegerTerm ct1 = (IntegerTerm)t1;
      IntegerTerm ct2 = (IntegerTerm)t2;
      if (ct1.value != ct2.value)
      {
        rc = PrologCode.FAIL;
      }
    }
    else if (t1 instanceof JavaObjectTerm /*&& t2 instanceof JavaObjectTerm*/)
    {
      JavaObjectTerm ct1 = (JavaObjectTerm)t1;
      JavaObjectTerm ct2 = (JavaObjectTerm)t2;
      if (ct1.value != ct2.value)
      {
        rc = PrologCode.FAIL;
      }
    }
    else 
    {
      rc = PrologCode.FAIL;
    }
    return rc;
  }

  /** unify two terms */
  public int unify(Term t1, Term t2) throws PrologException
  {
    int undoPos = getUndoPosition();
    int rc = PrologCode.SUCCESS_LAST;
    if (t1 == t2)
    {
    }
    else if (t1 instanceof VariableTerm)
    {
      VariableTerm vt1 = (VariableTerm)t1;
      addVariableUndo(vt1);
      vt1.value = t2;
    }
    else if (t2 instanceof VariableTerm)
    {
      VariableTerm vt2 = (VariableTerm)t2;
      addVariableUndo(vt2);
      vt2.value = t1;
    }
    else if (t1.getClass() !=  t2.getClass())
    {
      rc = PrologCode.FAIL;
    }
    else if (t1 instanceof CompoundTerm/* && t2 instanceof CompoundTerm*/)
    {
      CompoundTerm ct1 = (CompoundTerm)t1;
      CompoundTerm ct2 = (CompoundTerm)t2;
      if (ct1.tag != ct2.tag)
      {
        rc = PrologCode.FAIL;
      }
      else
      {
        Term args1[] = ct1.args;
        Term args2[] = ct2.args;
        //System.err.println("unify "+ct2.tag+" al1 = "+args1.length+" al2 = "+args2.length);
        unify_loop: for (int i=args2.length-1; i>=0; i--)
        {
          rc = simple_unify(args1[i].dereference(), args2[i].dereference());
          if (rc == PrologCode.FAIL)
          {
            break unify_loop; 
          }
        }
      }
    }
    else if (t1 instanceof FloatTerm/* && t2 instanceof FloatTerm*/)
    {
      FloatTerm ct1 = (FloatTerm)t1;
      FloatTerm ct2 = (FloatTerm)t2;
      if (ct1.value != ct2.value)
      {
        rc = PrologCode.FAIL;
      }
    }
    else if (t1 instanceof IntegerTerm /*&& t2 instanceof IntegerTerm*/)
    {
      IntegerTerm ct1 = (IntegerTerm)t1;
      IntegerTerm ct2 = (IntegerTerm)t2;
      if (ct1.value != ct2.value)
      {
        rc = PrologCode.FAIL;
      }
    }
    else if (t1 instanceof JavaObjectTerm /*&& t2 instanceof JavaObjectTerm*/)
    {
      JavaObjectTerm ct1 = (JavaObjectTerm)t1;
      JavaObjectTerm ct2 = (JavaObjectTerm)t2;
      if (ct1.value != ct2.value)
      {
        rc = PrologCode.FAIL;
      }
    }
    else 
    {
      rc = PrologCode.FAIL;
    }
    if (rc == PrologCode.FAIL)
    {
      undo(undoPos);
    }
    return rc;
  }

  /** user level calls */
  public class Goal
  {
    Term goal = null;
    boolean firstTime = true;
    boolean stopped = false;
    Term args[]=null;
  }
  Goal currentGoal;
  
  /** prepare goal for execution */
  public Goal prepareGoal(Term term) throws PrologException
  {
    if (currentGoal != null)
    {
      throw new IllegalStateException("There is currently active goal");
    }
    currentGoal = new Goal();
    currentGoal.goal = term;
    currentGoal.args = new Term[]{term};
    return currentGoal;
  }
  public int execute(Goal goal) throws PrologException
  {
    try
    {
      try
      {
        if (currentGoal == null)
        {
          throw new IllegalStateException("The goal is not prepared");
        }
        if (currentGoal != goal)
        {
          throw new IllegalArgumentException("The goal is not currently active");
        }
        if (goal.stopped)
        {
          throw new IllegalStateException("The goal is already stopped");
        }
        int rc = gnu.prolog.vm.interpreter.Predicate_call.staticExecute(this,!goal.firstTime,goal.goal);
        switch (rc)
        {
        case PrologCode.SUCCESS_LAST:
        case PrologCode.FAIL:
          goal.stopped = true;
          currentGoal = null;
          break;
        case PrologCode.SUCCESS:
          goal.firstTime = false;
          break;
        }
        return rc;
      }
      catch(RuntimeException rex)
      {
        PrologException.systemError();
        throw rex; // fake
      }
    }
    catch(PrologException ex)
    {
      stop(goal);
      throw ex;
    }
  }

  public void stop(Goal goal)
  {
    if (currentGoal != goal)
    {
      throw new IllegalArgumentException("The goal is not currently active");
    }
    if (goal.stopped)
    {
      throw new IllegalStateException("The goal is already stopped");
    }
    undo(0);
    //backtrackInfoStack.removeRange(0, backtrackInfoStack.size()-1);
    for (int i=0;i<backtrackInfoAmount;i++)
    {
      backtrackInfoStack[i]=null;
    }
    backtrackInfoAmount = 0;
    currentGoal = null;
  }
}
