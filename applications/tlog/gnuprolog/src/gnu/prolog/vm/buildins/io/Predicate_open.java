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

/** prolog code for open/4
  */
public class Predicate_open implements PrologCode, TermConstants
{

  /** this method is used for execution of code
    * @param interpreter interpreter in which context code is executed 
    * @param backtrackMode true if predicate is called on backtracking and false otherwise
    * @param args arguments of code
    * @return either SUCCESS, SUCCESS_LAST, or FAIL.
    */
  public int execute(Interpreter interpreter, boolean backtrackMode, gnu.prolog.term.Term args[]) 
         throws PrologException
  {
    Term tsource_sink = args[0];
    Term tmode = args[1];
    Term tstream = args[2];
    Term optionsList = args[3];
    
    PrologStream.OpenOptions options = new PrologStream.OpenOptions();
    AtomTerm source_sink = null;
    AtomTerm mode = null;
    VariableTerm vstream = null;    
    // check source/sink
    if (tsource_sink instanceof VariableTerm)
    {
      PrologException.instantiationError();
    }
    if (!(tsource_sink instanceof AtomTerm))
    {
      PrologException.domainError(sourceSinkAtom, tsource_sink);
    }
    source_sink = (AtomTerm)tsource_sink;
    // check mode
    if (tmode instanceof VariableTerm)
    {
      PrologException.instantiationError();
    }
    if (!(tmode instanceof AtomTerm))
    {
      PrologException.typeError(atomAtom, tmode);
    }
    if (tmode != PrologStream.readAtom &&
        tmode != PrologStream.writeAtom &&
        tmode != PrologStream.appendAtom)
    {
      PrologException.domainError(ioModeAtom, tmode);
    }
    mode = (AtomTerm)tmode;
    // check stream
    if (!(tstream instanceof VariableTerm))
    {
      PrologException.typeError(variableAtom, tstream);
    }
    vstream = (VariableTerm)tstream;
    // parse options
    Term cur = optionsList;
    while (cur != AtomTerm.emptyList)
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
        PrologException.domainError(streamOptionAtom, head);
      }
      CompoundTerm op = (CompoundTerm)head;
      if (op.tag == PrologStream.typeTag)
      {
        Term val = op.args[0].dereference();
        if (val != PrologStream.textAtom && val != PrologStream.binaryAtom)
        {
          PrologException.domainError(streamOptionAtom, op);
        }
        options.type = (AtomTerm)val;
      }
      else if (op.tag == PrologStream.repositionTag)
      {
        Term val = op.args[0].dereference();
        if (val != PrologStream.trueAtom && val != PrologStream.falseAtom)
        {
          PrologException.domainError(streamOptionAtom, op);
        }
        options.reposition = (AtomTerm)val;
      }
      else if (op.tag == PrologStream.aliasTag)
      {
        Term val = op.args[0].dereference();
        if (!(val instanceof AtomTerm))
        {
          PrologException.domainError(streamOptionAtom, op);
        }
        options.aliases.add(val);
      }
      else if (op.tag == PrologStream.eofActionTag)
      {
        Term val = op.args[0].dereference();
        if (val != PrologStream.errorAtom && 
            val != PrologStream.eofCodeAtom &&
            val != PrologStream.resetAtom)
        {
          PrologException.domainError(streamOptionAtom, op);
        }
        options.reposition = (AtomTerm)val;
      }
      else
      {
        PrologException.domainError(streamOptionAtom, op);
      }
    }
    options.filename=source_sink;
    options.mode=mode;
    options.environment=interpreter.environment;
    vstream.value = interpreter.environment.open(source_sink,mode,options);
    interpreter.addVariableUndo(vstream);
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
    
}

