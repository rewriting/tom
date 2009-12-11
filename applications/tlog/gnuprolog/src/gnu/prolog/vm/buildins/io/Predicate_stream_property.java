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
import java.util.*;

/** prolog code 
  */
public class Predicate_stream_property implements PrologCode, TermConstants
{

  private class StreamPropertyBacktrackInfo extends BacktrackInfo
  {
    StreamPropertyBacktrackInfo() {super(-1,-1);}
    int startUndoPosition;
    Map stream2option;
    Iterator streams;
    Term currentStream;
    Iterator options;

    Term stream;
    Term property;
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
      StreamPropertyBacktrackInfo bi 
        = (StreamPropertyBacktrackInfo)interpreter.popBacktrackInfo();
      interpreter.undo(bi.startUndoPosition);
      return nextSolution(interpreter,bi);
    }
    else
    {
      Term stream = args[0];
      if (!(stream instanceof VariableTerm))
      {
        if (stream instanceof JavaObjectTerm)
        {
          JavaObjectTerm jt = (JavaObjectTerm)stream;
          if (jt.value instanceof PrologStream)
          {
            PrologStream ps = (PrologStream)jt.value;
            ps.checkExists();
          }
          else
          {
            PrologException.domainError(streamAtom,stream);
          }
        }
        else
        {
          PrologException.domainError(streamAtom,stream);
        }
      }
      Term property = args[1];
      if (property instanceof VariableTerm || 
          property == inputAtom || 
          property == outputAtom)
      {
      }
      else if (property instanceof CompoundTerm)
      {
        CompoundTerm ct = (CompoundTerm)property;
        if (ct.tag == PrologStream.filenameTag)
        {
          if (!(ct.args[0] instanceof AtomTerm || 
                ct.args[0] instanceof VariableTerm))
          {
            PrologException.domainError(streamPropertyAtom,property);
          }
        }
        else if (ct.tag == PrologStream.aliasTag)
        {
          if (!(ct.args[0] instanceof AtomTerm || 
                ct.args[0] instanceof VariableTerm))
          {
            PrologException.domainError(streamPropertyAtom,property);
          }
        }
        else if (ct.tag == PrologStream.endOfStreamTag)
        {
          if (!(ct.args[0] == PrologStream.atAtom || 
                ct.args[0] == PrologStream.pastAtom || 
                ct.args[0] == PrologStream.notAtom || 
                ct.args[0] instanceof VariableTerm))
          {
            PrologException.domainError(streamPropertyAtom,property);
          }
        }
        else if (ct.tag == PrologStream.eofActionTag)
        {
          if (!(ct.args[0] == PrologStream.errorAtom || 
                ct.args[0] == PrologStream.eofCodeAtom || 
                ct.args[0] == PrologStream.resetAtom || 
                ct.args[0] instanceof VariableTerm))
          {
            PrologException.domainError(streamPropertyAtom,property);
          }
        }
        else if (ct.tag == PrologStream.repositionTag)
        {
          if (!(ct.args[0] == trueAtom || ct.args[0] == falseAtom || 
                ct.args[0] instanceof VariableTerm))
          {
            PrologException.domainError(streamPropertyAtom,property);
          }
        }
        else if (ct.tag == PrologStream.positionTag)
        {
          if (!(ct.args[0] == trueAtom || ct.args[0] == falseAtom || 
                ct.args[0] instanceof VariableTerm))
          {
            PrologException.domainError(streamPropertyAtom,property);
          }
        }
        else if (ct.tag == PrologStream.typeTag)
        {
          if (!(ct.args[0] == PrologStream.textAtom || 
                ct.args[0] == PrologStream.binaryAtom || 
                ct.args[0] instanceof VariableTerm))
          {
            PrologException.domainError(streamPropertyAtom,property);
          }
        }
        else
        {
          PrologException.domainError(streamPropertyAtom,property);
        }
      }
      else
      {
        PrologException.domainError(streamPropertyAtom,property);
      }
      StreamPropertyBacktrackInfo bi = new StreamPropertyBacktrackInfo();
      bi.startUndoPosition = interpreter.getUndoPosition();
      bi.stream2option = interpreter.environment.getStreamProperties();
      bi.streams = bi.stream2option.keySet().iterator();
      bi.stream = args[0];
      bi.property = args[1];
      //bi.currentStream;
      //bi.options;
      return nextSolution(interpreter,bi);
    }
  }

  private int nextSolution(Interpreter interpreter, StreamPropertyBacktrackInfo bi) throws PrologException
  {
    int undoPos = interpreter.getUndoPosition();
    while (true)
    {
      if (bi.options == null || !bi.options.hasNext() )
      {
        if (bi.streams.hasNext())
        {
          PrologStream stream = (PrologStream)bi.streams.next();
          bi.currentStream = stream.getStreamTerm();
          bi.options = ((List)bi.stream2option.get(stream)).iterator();
          continue;
        }
        else
        {
          return FAIL;
        }
      }
      Term currentProp = (Term)bi.options.next();
      if (interpreter.simple_unify(bi.stream, bi.currentStream) == SUCCESS_LAST &&
          interpreter.simple_unify(bi.property, currentProp) == SUCCESS_LAST)
      {
        interpreter.pushBacktrackInfo(bi);
        return SUCCESS;
      }
      interpreter.undo(undoPos);
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

