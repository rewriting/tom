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
package gnu.prolog.vm.interpreter;
import gnu.prolog.vm.*;
import gnu.prolog.term.*;

/** Interpreted Code. This class is used by call_term instruction and
  * InterpretedCode   
  */
public class InterpretedCode implements PrologCode
{
  
  
  /** constructor of code */
  InterpretedCode(CompoundTermTag  codeTag, Instruction instructions[], ExceptionHandlerInfo exceptionHandlers[])
  {
    this.codeTag = codeTag;
    this.instructions = (Instruction[])instructions.clone();
    this.exceptionHandlers = (ExceptionHandlerInfo[])exceptionHandlers.clone();
  }
  
  /** tag of this code */
  CompoundTermTag  codeTag;
  
  /** set of instructions */
  Instruction instructions[];

  /** set of exception handlers 
    * nested exception go first. 
    * This value cannot be null. It should be empty array if code has 
    * no exception handlers.
    */
  ExceptionHandlerInfo exceptionHandlers[];

  /** this method is used for execution of code
    * @param interpreter interpreter in which context code is executed 
    * @param backtrackMode true if predicate is called on backtracking and false otherwise
    * @param args arguments of code
    * @return either SUCCESS, SUCCESS_LAST, or FAIL.
    */
  public int execute(Interpreter interpreter, boolean backtrackMode, gnu.prolog.term.Term args[]) 
         throws PrologException
  {
    ExecutionState executionState;
    BacktrackInfo cur_bi;
    int rc;
    int currentPosition;
    BacktrackInfo bi = backtrackMode?interpreter.popBacktrackInfo():null;
    // this should not happens, except in case of bug with backtrack management
    if (bi != null && !(bi instanceof LeaveBacktrackInfo))
    {
      PrologException.systemError();
    }

    if (bi != null) // if redo, restore execution state
    {
      executionState = ((LeaveBacktrackInfo)bi).executionState;
      cur_bi = executionState.popBacktrackInfo();
      currentPosition = cur_bi.codePosition;
    }
    else // else create new execution state
    {
      executionState = new ExecutionState();
      executionState.interpreter = interpreter;
      executionState.startBacktrackInfo = executionState.getEnterBacktrackInfo();
      executionState.pushBacktrackInfo(executionState.startBacktrackInfo);
      currentPosition = 0;
      executionState.args = args;
      bi = executionState.getLeaveBacktrackInfo();
      cur_bi = null;
    }

    interpreter_loop: while (true) // interpreter loop
    {
      // fetch instruction
      Instruction instr = instructions[currentPosition];
      try
      {
        try
        {
          // later this will be converted to BIG switch in order to
          // improve performance
          //System.err.println("instr "+instr);
          rc = instr.execute(executionState, cur_bi);
          cur_bi = null; // clear backtrack information
          switch (rc)
          {
          case ExecutionState.BACKTRACK: // backtrack is needed
            cur_bi = executionState.popBacktrackInfo();
            currentPosition = cur_bi.codePosition;
            if (cur_bi == executionState.startBacktrackInfo)
            {
              cur_bi.undo(interpreter);
              return PrologCode.FAIL;
            }
            continue interpreter_loop;
          case ExecutionState.NEXT: // continue to next instruction
            currentPosition++;
            continue interpreter_loop;
          case ExecutionState.JUMP: // jump to location stored in executionState.jumpPosition
            currentPosition = executionState.jumpPosition;
            continue interpreter_loop;
          case ExecutionState.RETURN: // return with success
            if (executionState.peekBacktrackInfo() == executionState.startBacktrackInfo)
            {
              // no more alternatives left, do SUCCESS_LAST
              executionState.popBacktrackInfo();
              return PrologCode.SUCCESS_LAST;
            }
            else
            {
              // redo is possible, just success
              executionState.pushBacktrackInfo(executionState.getLeaveBacktrackInfo());
              return PrologCode.SUCCESS;
            }

          } // end of return code switch
        }
        catch (RuntimeException ex)
        {
          ex.printStackTrace();
          // unchecked exception behaves as system_error
          PrologException.systemError();
        }
      }
      catch (PrologException ex)
      {
        cur_bi = null;
        int ei, en = exceptionHandlers.length;
        for (ei=0; ei<en; ei++)
        {
          ExceptionHandlerInfo cur = exceptionHandlers[ei];
          if (cur.startPosition < currentPosition 
              && currentPosition < cur.endPosition)
          {
            // exception handler for this position found
            executionState.pushPushDown(ex.getTerm());
            currentPosition = cur.handlerPosition;
            continue interpreter_loop;
          }
        }
        // no handler was found
        do
        {
          cur_bi = executionState.popBacktrackInfo();
        } while (executionState.startBacktrackInfo != cur_bi);
        cur_bi.undo(executionState.interpreter);
        throw ex;
      }
    }
  }


  /** this method is called when code is installed to the environment
    * code can be installed only for one environment.
    * @param environment environemnt to install the predicate
    */
  public void install(Environment env)
  {
    for (int i = instructions.length-1;i>=0;i--)
    {
      instructions[i].install(env);
    }
  }

  /** this method is called when code is uninstalled from the environment
    * @param environment environemnt to install the predicate
    */
  public void uninstall(Environment env)
  {
    for (int i = instructions.length-1;i>=0;i--)
    {
      instructions[i].uninstall(env);
    }
  }

  /** convert code to string */
  public String toString()
  {
    int i,n;
    String rc = "interpreted code\n";
    n = instructions.length;
    for (i=0;i<n;i++)
    {
      rc += instructions[i]+"\n";
    }
    rc += "exceptions\n";
    n = exceptionHandlers.length;
    for (i=0;i<n;i++)
    {
      rc += exceptionHandlers[i]+"\n";
    }
    rc += "end interpreted code\n";
    return rc;
  }


}
