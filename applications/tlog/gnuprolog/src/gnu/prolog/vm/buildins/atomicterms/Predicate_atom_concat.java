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
package gnu.prolog.vm.buildins.atomicterms;
import gnu.prolog.term.*;
import gnu.prolog.vm.*;

/** prolog code 
  */
public class Predicate_atom_concat implements PrologCode
{
  static final AtomTerm atomAtom = AtomTerm.get("atom");
  static final AtomTerm nullAtom = AtomTerm.get("");
  /** cutomized bactrackinfo */
  private static class AtomConcatBacktrackInfo extends BacktrackInfo
  {
    AtomConcatBacktrackInfo(int atomPosition, int startUndoPosition, String atom)
    {
      super(-1,-1);
      this.atomPosition = atomPosition; 
      this.startUndoPosition = startUndoPosition;
      this.atom = atom;
    }
    int atomPosition;
    int startUndoPosition;
    String atom;
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
      AtomConcatBacktrackInfo acbi = (AtomConcatBacktrackInfo)interpreter.popBacktrackInfo();
      interpreter.undo(acbi.startUndoPosition);
      int al = acbi.atom.length();
      int pos = acbi.atomPosition;
      VariableTerm v1 = (VariableTerm)args[0];
      VariableTerm v2 = (VariableTerm)args[1];
      if (acbi.atomPosition == al)
      {
        interpreter.addVariableUndo(v1);
        v1.value = args[2];
        interpreter.addVariableUndo(v2);
        v2.value = nullAtom;
        return SUCCESS_LAST;
      }
      interpreter.addVariableUndo(v1);
      v1.value = AtomTerm.get(acbi.atom.substring(0,pos));
      interpreter.addVariableUndo(v2);
      v2.value = AtomTerm.get(acbi.atom.substring(pos,al));
      acbi.atomPosition++;
      interpreter.pushBacktrackInfo(acbi);
      return SUCCESS;
    }
    else
    {
      Term t1 = args[0];
      Term t2 = args[1];
      Term t12 = args[2];
      int startUndoPosition = interpreter.getUndoPosition();
  
      if (!(t1 instanceof VariableTerm || t1 instanceof AtomTerm))
      {
        PrologException.typeError(atomAtom,t1);
      }
      if (!(t2 instanceof VariableTerm || t2 instanceof AtomTerm))
      {
        PrologException.typeError(atomAtom,t2);
      }
      if (!(t12 instanceof VariableTerm || t12 instanceof AtomTerm))
      {
        PrologException.typeError(atomAtom,t12);
      }
      if (t12 instanceof VariableTerm)
      {
        VariableTerm v12 = (VariableTerm)t12;
        if (t1 instanceof VariableTerm)
        {
          PrologException.instantiationError();
        }
        if (t2 instanceof VariableTerm)
        {
          PrologException.instantiationError();
        }
        AtomTerm a1 = (AtomTerm)t1; 
        AtomTerm a2 = (AtomTerm)t2; 
        AtomTerm a3 = AtomTerm.get(a1.value+a2.value);
        interpreter.addVariableUndo(v12);
        v12.value = a3;
        return SUCCESS_LAST;
      }
      else // t12 is AtomTerm
      {
        AtomTerm a12 = (AtomTerm)t12;
        String s12 = a12.value;
        if (t1 instanceof VariableTerm && t2 instanceof VariableTerm)
        {
          VariableTerm v1 = (VariableTerm)t1;
          VariableTerm v2 = (VariableTerm)t2;
          if (s12.length() == 0)
          {
            interpreter.addVariableUndo(v1);
            v1.value = a12;
            interpreter.addVariableUndo(v2);
            v2.value = a12;
            return SUCCESS_LAST;
          }
          interpreter.addVariableUndo(v1);
          v1.value = nullAtom;
          interpreter.addVariableUndo(v2);
          v2.value = a12;
          interpreter.pushBacktrackInfo(
            new AtomConcatBacktrackInfo(1,startUndoPosition,s12)
          );
          return SUCCESS;
        }
        else if (t1 instanceof VariableTerm)
        {
          VariableTerm v1 = (VariableTerm)t1;
          AtomTerm a2 = (AtomTerm)t2; 
          String s2 = a2.value;
          if (s12.endsWith(s2))
          {
            interpreter.addVariableUndo(v1);
            v1.value = AtomTerm.get(s12.substring(0,s12.length()-s2.length()));
            return SUCCESS_LAST;
          }
          else
          {
            return FAIL;
          }
        }
        else if (t2 instanceof VariableTerm)
        {
          AtomTerm a1 = (AtomTerm)t1; 
          VariableTerm v2 = (VariableTerm)t2;
          String s1 = a1.value;
          if (s12.startsWith(s1))
          {
            interpreter.addVariableUndo(v2);
            int l1 = s1.length();
            int l12 = s12.length();
            v2.value = AtomTerm.get(s12.substring(l1,l12));
            return SUCCESS_LAST;
          }
          else
          {
            return FAIL;
          }
        }
        else 
        {
          AtomTerm a1 = (AtomTerm)t1; 
          AtomTerm a2 = (AtomTerm)t2; 
          String s1 = a1.value;
          String s2 = a1.value;
          if (s12.equals(s1+s2))
          {
            return SUCCESS_LAST;
          }
          else
          {
            return FAIL;
          }
        }
      }
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

